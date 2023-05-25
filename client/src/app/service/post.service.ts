import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

const POST_API = "http://localhost:8080/api/post";

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) { }

  public getAllPosts(page: number, size: number): Observable<any> {
    let queryParams = new HttpParams().append("page", page).append("size", size);
    console.log(queryParams);
    return this.http.get(POST_API + '/all', {params: queryParams});
  }

  public getAllPostsByParameters(httpParameters: Map<string, Array<number>>): Observable<any> {
    let httpParams = new HttpParams();

    httpParameters.forEach((value, key) => {
      httpParams = httpParams.append(key, value.toString());
    });

    return this.http.get(POST_API + '/all', {params: httpParams});
  }

  public getAllPostsForCurrentUser(): void {}
  public createPost(): void {}
  public likePost(): void {}
  public addTransportToPost(): void {}
  public updatePost(): void {}
  public deletePost(): void {}
}
