import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from "@angular/forms";
import { FieldErrorStateMatcher, FormErrorStateMatcher } from "../login/login.component";
import { AuthService } from "../auth.service";
import { SnackbarService } from "../../layout/snackbar.service";
import { HttpErrorResponse } from "@angular/common/http";
import { Router } from "@angular/router";
import { RecaptchaComponent } from "ng-recaptcha";
import { environment } from "../../../environments/environment";

export const passwordMatchingValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const password = control.get('password');
  const confirmPassword = control.get('passwordRepeat');

  return password?.value === confirmPassword?.value ? null : { notmatched: true };
};

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  @ViewChild('captcha', { static: true }) captcha: RecaptchaComponent;

  constructor(
    private authService: AuthService,
    private snackbarService: SnackbarService,
    private router: Router,
  ) { }

  public siteKey = environment.captchaKey;

  registerForm = new FormGroup({
    middleName: new FormControl('', []),
    firstName: new FormControl('', [Validators.required, Validators.minLength(1)]),
    lastName: new FormControl('', [Validators.required, Validators.minLength(1)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)]),
    passwordRepeat: new FormControl('', [Validators.required]),
  }, { validators: passwordMatchingValidator })

  matcher = new FieldErrorStateMatcher();
  formMatcher = new FormErrorStateMatcher();

  submit(captchaResponse: string) {
    if (captchaResponse == null) {
      this.captcha.reset();
      return;
    }
    const data = this.registerForm.value;
    this.authService.verifyCaptcha(captchaResponse).subscribe({
      next: () => {
        this.authService.register(data.email, data.password, data.firstName, data.middleName, data.lastName).then(user => {
          this.snackbarService.openSnackBar(`Welcome to PC Parts ${user.firstName}!`, '', 5000);
          this.router.navigate(['profile']);
        }).catch((error: HttpErrorResponse) => {
          switch (error.status) {
            case 409:
              this.snackbarService.openSnackBar('This email address is already in use', '', 5000);
              break;
            default:
              this.snackbarService.openSnackBar('Unable to register', '', 5000);
          }
        });
      },
      error: () => {
        this.captcha.reset();
      }
    })
  }

}
