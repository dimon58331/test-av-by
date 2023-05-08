import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import * as http from "http";

const USER_API = 'http://localhost:8080/api/user/';
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  public getCurrentUser(): Observable<any> {
    return this.http.get(USER_API);
  }

  public updateCurrentUser(user: any): Observable<any> {
    return this.http.post(USER_API + 'update', {
      firstname: user.firstname,
      lastname: user.lastname,
      patronymic: user.patronymic,
      email: user.email,
      phoneNumber: user.phoneNumber
    });
  }

  public deleteCurrentUser(): Observable<any> {
    return this.http.post(USER_API + 'delete', null);
  }
}
