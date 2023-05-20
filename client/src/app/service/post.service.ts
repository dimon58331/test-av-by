import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Post} from "../models/Post";

const POST_API = "http://localhost:8080/api/post";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) { }

  public getAllPosts(page: number, size: number): Observable<any> {
    let queryParams = new HttpParams().append("page", page).append("size", size);
    return this.http.get(POST_API + '/all', {params: queryParams});
  }
  public getAllPostsForCurrentUser(): void {}
  public getAllPostsSortedByBrand(): void {}
  public getAllPostsSortedByModel(): void {}
  public getAllPostsSortedByGeneration(): void {}
  public getAllPostsSortedByTransportParameters(): void {}
  public getAllPostsSortedByMinAndMaxPrice(): void {}
  public createPost(): void {}
  public likePost(): void {}
  public addTransportToPost(): void {}
  public updatePost(): void {}
  public deletePost(): void {}
}
