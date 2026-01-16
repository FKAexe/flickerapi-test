import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { IImage } from '../../interfaces/interfaces';
import { MATERIAL_MODULES } from '../../material-imports';

@Component({
  selector: 'app-image-card',
  standalone: true,
  imports: [CommonModule, RouterModule, ...MATERIAL_MODULES],
  templateUrl: './image-card.html',
  styleUrls: ['./image-card.css']
})
export class ImageCardComponent {
  @Input() image!: IImage;
  @Output() remove = new EventEmitter<string>();
  
  imageError: boolean = false;

  onRemove(event: Event): void {
    // Prevenir que el click navegue al detalle
    event.preventDefault();
    event.stopPropagation();
    this.remove.emit(this.image.id);
  }

  getTags(): string[] {
    if (!this.image.tags) return [];
    return this.image.tags
      .split(' ')
      .filter(tag => tag.length > 0)
      .slice(0, 3); // Mostrar máximo 3 tags
  }

  onImageError(): void {
    this.imageError = true;
    console.warn(`Image failed to load: ${this.image.id}`);
  }
}