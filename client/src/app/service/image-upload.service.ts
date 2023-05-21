import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const IMAGE_API = "http://localhost:8080/api/image";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {

  constructor(private http: HttpClient) { }

  public getPostImage(postId: number): Observable<any> {
    return this.http.get(IMAGE_API + '/post/' + postId);
  }

  public uploadImageToPost(file: File, postId: number): Observable<any> {
    const uploadData = new FormData();
    uploadData.append("file", file);

    return this.http.post(IMAGE_API + '/post/' + postId + '/upload', uploadData);
  }

  public deletePostImage(postId: number): Observable<any> {
    return this.http.delete(IMAGE_API + '/post/' + postId + '/delete');
  }

  public getGenerationTransportImage(generationTransportId: number): Observable<any> {
    return this.http.get(IMAGE_API + '/generation/' + generationTransportId);
  }

}
