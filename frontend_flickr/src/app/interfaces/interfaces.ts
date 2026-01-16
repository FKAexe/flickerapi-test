export interface IImage {
  id: string;
  title: string;
  ownerName: string;
  description: string;
  tags: string;
  thumbnailUrl: string;
  largeUrl: string;
}

export interface ISearchResponse {
  images: IImage[];
  page: number;
  totalPages: number;
  totalResults: number;
}