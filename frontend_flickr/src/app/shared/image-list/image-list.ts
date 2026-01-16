import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageCardComponent } from '../image-card/image-card';
import { IImage } from '../../interfaces/interfaces';

@Component({
  selector: 'app-image-list',
  standalone: true,
  imports: [CommonModule, ImageCardComponent],
  templateUrl: './image-list.html',
  styleUrls: ['./image-list.css']
})
export class ImageListComponent {
  @Input() images: IImage[] = [];
  @Input() isLoading: boolean = false;
  @Output() imageRemoved = new EventEmitter<string>();

  onRemoveImage(imageId: string): void {
    this.imageRemoved.emit(imageId);
  }

}