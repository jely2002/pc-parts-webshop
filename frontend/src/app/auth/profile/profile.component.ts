import { Component, OnInit } from '@angular/core';
import { AuthService } from "../auth.service";
import { Router } from "@angular/router";
import { UpdateUserRequest, UserModel } from "../user.model";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { FieldErrorStateMatcher, FormErrorStateMatcher } from "../login/login.component";
import { SnackbarService } from "../../layout/snackbar.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackbarService: SnackbarService,
  ) { }

  user: UserModel;

  public profileForm = new FormGroup({
    firstName: new FormControl('', []),
    middleName: new FormControl('', []),
    lastName: new FormControl('', []),
    email: new FormControl('', [Validators.email]),
    password: new FormControl('', [Validators.minLength(8)]),
    passwordRepeat: new FormControl('', []),
  });

  public matcher = new FieldErrorStateMatcher();
  public formMatcher = new FormErrorStateMatcher();

  ngOnInit(): void {
    this.authService.checkIfLoggedIn().subscribe({
      next: user => {
        this.user = user;
      },
      error: () => {
        this.router.navigate(['/login']);
      }
    })
  }

  delete() {
    this.snackbarService.openSnackBar('Are you sure you want to close your account?', 'Yes', 10000).subscribe(() => {
      this.authService.delete(this.user.id).subscribe(() => {
        this.snackbarService.openSnackBar('Your account has been closed.', 'Ok', 5000)
        this.authService.logout();
        this.router.navigate(['']);
      })
    })
  }

  save() {
    const user: UpdateUserRequest = {};
    if (this.profileForm.value.firstName) {
      user.firstName = this.profileForm.value.firstName;
    }
    if (this.profileForm.value.middleName) {
      user.middleName = this.profileForm.value.middleName;
    }
    if (this.profileForm.value.lastName) {
      user.lastName = this.profileForm.value.lastName;
    }
    if (this.profileForm.value.email) {
      user.email = this.profileForm.value.email;
    }
    this.authService.updateUser(user, this.user.id, this.user.email).then(user => {
      this.snackbarService.openSnackBar('User details have been saved', 'Ok', 5000);
      this.user = user;
      this.profileForm.setValue(user);
    });
  }

  logout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['']);
    });
  }

}
