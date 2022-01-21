import { Component, OnInit } from '@angular/core';
import { AuthService } from "../auth.service";
import { Router } from "@angular/router";
import { UserModel } from "../user.model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(
    private authService: AuthService,
    private router: Router,
  ) { }

  user: UserModel;

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

  logout() {
    this.authService.logout().subscribe(() => {
      this.router.navigate(['']);
    });
  }

}
