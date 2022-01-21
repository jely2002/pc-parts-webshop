import { Component, OnInit } from '@angular/core';
import { ErrorStateMatcher } from "@angular/material/core";
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from "@angular/forms";
import { AuthService } from "../auth.service";
import { SnackbarService } from "../../layout/snackbar.service";
import { Router } from "@angular/router";

export class FieldErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

export class FormErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && (form && form.invalid) && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  constructor(
    private authService: AuthService,
    private snackbarService: SnackbarService,
    private router: Router,
  ) { }

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  })

  matcher = new FieldErrorStateMatcher();

  submit() {
    const data = this.loginForm.value;
    this.authService.login(data.email, data.password).then(user => {
      this.snackbarService.openSnackBar(`Welcome back ${user.firstName}!`, '', 5000);
      this.router.navigate(['profile']);
    }).catch(error => {
      this.snackbarService.openSnackBar('Username or password do not match', '', 5000);
    });
  }

}
