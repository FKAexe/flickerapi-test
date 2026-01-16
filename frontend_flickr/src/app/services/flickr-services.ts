import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { IImage, ISearchResponse } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root'
})
export class FlickrService {
  private apiUrl = 'http://localhost:8080/api/images';

  constructor(private http: HttpClient) { }
/* Metodo de buscar imágenes */
  searchImages(query: string, page: number = 1, size: number = 20): Promise<ISearchResponse> {
    const params = new HttpParams().set('query', query).set('page', page.toString()).set('size', size.toString());
    return firstValueFrom(this.http.get<ISearchResponse>(`${this.apiUrl}/search`, { params }));
  }
/* Metodo de detalle imágenes*/
  getImageDetail(id: string): Promise<IImage> {
    return firstValueFrom(this.http.get<IImage>(`${this.apiUrl}/${id}`));
  }
}