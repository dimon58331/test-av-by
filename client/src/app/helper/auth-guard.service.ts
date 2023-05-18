import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
  UrlTree
} from "@angular/router";
import {TokenStorageService} from "../service/token-storage.service";
import {Observable} from "rxjs";
import {User} from "../models/User";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{
  constructor(private router: Router, private tokenService: TokenStorageService, private user: User) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const currentToken = this.tokenService.getToken();
    if (currentToken){
      console.log('Current person is active');
      return true;
    }
    console.log('Current user isn\'t active');
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }

}
