import { Injectable } from '@angular/core';
import {Observable} from "rxjs";

const TOKEN_KEY = 'auth-token';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() {}

  public saveToken(token: string): void {
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    // @ts-ignore
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public logOut(message: string): void {
    window.sessionStorage.clear();
    window.sessionStorage.setItem('reloadAfterPage', 'true');
    window.sessionStorage.setItem('notification-message', message);
    window.location.reload();
  }
}
