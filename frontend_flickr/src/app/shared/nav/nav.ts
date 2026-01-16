import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MATERIAL_MODULES } from '../../material-imports';
@Component({
  selector: 'app-nav',
  imports: [RouterLink, ...MATERIAL_MODULES],
  templateUrl: './nav.html',
  styleUrl: './nav.css',
})
export class Nav {

}
