import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

const IMAGE_API = "http://localhost:8080/api/image/post";

@Injectable({
  providedIn: 'root'
})
export class ImageUploadService {

  constructor(private http: HttpClient) { }

  public uploadImageToPost(file: File, postId: number): Observable<any> {
    const uploadData = new FormData();
    uploadData.append("file", file);

    return this.http.post(IMAGE_API + postId + '/upload', uploadData);
  }

  public deletePostImage(postId: number): Observable<any> {
    return this.http.delete(IMAGE_API + postId + '/delete');
  }

  public getPostImage(postId: number): Observable<any> {
    return this.http.get(IMAGE_API + '/' + postId);
  }

}
