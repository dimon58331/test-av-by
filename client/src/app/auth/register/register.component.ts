import {Component, KeyValueDiffers} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {TokenStorageService} from "../../service/token-storage.service";
import {NotificationService} from "../../service/notification.service";
import {Router} from "@angular/router";
import {MyErrorStateMatcher} from "./validator/password-matcher";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  public registerForm: FormGroup;
  public matcher = new MyErrorStateMatcher();
  constructor(
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private notificationService: NotificationService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    if (this.tokenStorage.getToken()){
      this.router.navigate(['main']);
    }

    this.registerForm = this.formBuilder.group({
      email: ['', Validators.compose([Validators.required, Validators.email])],
      firstname: ['', Validators.compose([Validators.required])],
      lastname: ['', Validators.compose([Validators.required])],
      patronymic: [''],
      phoneNumber: ['', Validators.compose([Validators.required, Validators.pattern('^\\+?[0-9]{9,15}$')])],
      password: ['', Validators.compose([Validators.required])],
      confirmPassword: ['']
    }, {validators: this.checkPassword});
  }

  checkPassword: ValidatorFn = (group: AbstractControl): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('confirmPassword')?.value;
    return pass === confirmPass ? null : { notSame: true};
  }

  submit(): void {
    this.authService.register({
      email: this.registerForm.value.email,
      firstname: this.registerForm.value.firstname,
      lastname: this.registerForm.value.lastname,
      patronymic: this.registerForm.value.patronymic,
      phoneNumber: this.registerForm.value.phoneNumber,
      password: this.registerForm.value.password,
      confirmPassword: this.registerForm.value.confirmPassword
    }).subscribe(value => {
      console.log(value);

      this.notificationService.showSnackBar(value.message);
      this.router.navigate(['/login']);
    }, error => {
      console.log(error);
      sessionStorage.setItem('reloadAfterPageLoad', 'true');
      sessionStorage.setItem('notification-message', error.error.message);
      window.location.reload();
    });
  }

}
