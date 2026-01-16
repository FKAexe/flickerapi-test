import { Component, inject, Input } from '@angular/core';
import {FlickrService} from '../../services/flickr-services';
import {IImage} from '../../interfaces/interfaces';
import {RouterLink} from '@angular/router';
import {MATERIAL_MODULES} from '../../material-imports';
@Component({
  selector: 'app-details',
  imports: [RouterLink, ...MATERIAL_MODULES],
  templateUrl: './details.html',
  styleUrl: './details.css',
})
export class Details {

  @Input() id: string = "";
  image!: IImage;
  flickrService = inject(FlickrService);

  async ngOnInit() {
    const imageId = this.id;
    const response = await this.flickrService.getImageDetail(imageId);
    if(!response) {
      console.log('Image not found');
    }
    this.image = response!;
  } 
}
