import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {Nav} from '../../shared/nav/nav';
import { FlickrService } from '../../services/flickr-services';
import { IImage, ISearchResponse } from '../../interfaces/interfaces';
import { ImageListComponent } from '../../shared/image-list/image-list';
import { MATERIAL_MODULES } from '../../material-imports';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ImageListComponent,
    ...MATERIAL_MODULES
    ,Nav
  ],
  templateUrl: './search.html',
  styleUrls: ['./search.css']
})
export class Search {
  searchQuery: string = '';
  images: IImage[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';
  currentPage: number = 1;
  totalPages: number = 0;
  totalResults: number = 0;
  pageSize: number = 20;

  constructor(private imageService: FlickrService) {}

  /**
   * Ejecutar búsqueda (reinicia la paginación)
   */
  async onSearch(): Promise<void> {
    if (!this.searchQuery.trim()) {
      this.errorMessage = 'Please enter a search term';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.currentPage = 1;
    this.images = [];

    try {
      const response = await this.imageService.searchImages(this.searchQuery, this.currentPage, this.pageSize);
      this.images = response.images;
      this.totalPages = response.totalPages;
      this.totalResults = response.totalResults;
      console.log('Search results:', response);
    } catch (error) {
      console.error('Error searching images:', error);
      this.errorMessage = 'Error loading images. Please try again.';
      this.images = [];
    } finally {
      this.isLoading = false;
    }
  }

  /**
   * Cargar más resultados (paginación)
   */
  async loadMore(): Promise<void> {
    if (this.currentPage >= this.totalPages || this.isLoading) {
      return;
    }

    this.currentPage++;
    this.isLoading = true;

    try {
      const response = await this.imageService.searchImages(this.searchQuery, this.currentPage, this.pageSize);
      this.images = [...this.images, ...response.images];
      console.log(`Loaded page ${this.currentPage}/${this.totalPages}`);
    } catch (error) {
      console.error('Error loading more images:', error);
      this.errorMessage = 'Error loading more images. Please try again.';
      this.currentPage--;
    } finally {
      this.isLoading = false;
    }
  }
  onImageRemoved(imageId: string): void {
    this.images = this.images.filter(img => img.id !== imageId);
    console.log(`Image ${imageId} removed from list`);
  }

  onKeyPress(event: KeyboardEvent): void {
    if (event.key === 'Enter') {
      this.onSearch();
    }
  }

  hasMorePages(): boolean {
    return this.currentPage < this.totalPages;
  }
}
