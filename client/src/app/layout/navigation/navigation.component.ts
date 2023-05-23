import {Component, OnInit} from '@angular/core';
import {User} from "../../models/User";
import {TokenStorageService} from "../../service/token-storage.service";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit{
  public user: User;
  public isLoggedIn = false;
  public isDataLoaded = false;
  constructor(
    private tokenService: TokenStorageService,
    private userService: UserService) {
    // @ts-ignore
    this.user = null;
  }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenService.getToken();
    if (this.isLoggedIn){
      this.userService.getCurrentUser()
        .subscribe(value => {
          this.user = value;
          this.isDataLoaded = true;
        });
    }
  }

  reload(){
    window.location.reload();
  }

  logOut(): void {
    this.tokenService.logOut('Successfully logout!');
  }
}
