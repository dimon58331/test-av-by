import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {NotificationService} from "../../service/notification.service";
import {TokenStorageService} from "../../service/token-storage.service";
import {AuthService} from "../../service/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  public loginForm: FormGroup;
  constructor(private authService: AuthService,
              private tokenStorage: TokenStorageService,
              private notificationService: NotificationService,
              private router: Router,
              private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', Validators.compose([Validators.required])],
      password: ['', Validators.compose([Validators.required])]
    })

    if (this.tokenStorage.getToken()) {
      this.router.navigate(['/main']);
    }
  }

  submit(): void {
    this.authService.login({
      email: this.loginForm.value.email,
      password: this.loginForm.value.password
    }).subscribe(value => {
      console.log(value)

      this.tokenStorage.saveToken(value.token);

      this.router.navigate(['/main']);
      sessionStorage.setItem('reloadAfterPageLoad', 'true');
      sessionStorage.setItem('notification-message', 'Successfully logged in');
      window.location.reload();
    }, error => {
      console.log(error);
      sessionStorage.setItem('reloadAfterPageLoad', 'true');
      sessionStorage.setItem('notification-message', 'Invalid username or password!');
      window.location.reload();
    });
  }
}
