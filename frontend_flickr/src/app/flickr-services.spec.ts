import { TestBed } from '@angular/core/testing';

import { FlickrServices } from './flickr-services';

describe('FlickrServices', () => {
  let service: FlickrServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlickrServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
