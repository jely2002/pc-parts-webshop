import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../product/category.service";
import { CategoryModel } from "../../product/category.model";
import { AuthService } from "../../auth/auth.service";
import { UserModel } from "../../auth/user.model";
import { Router } from "@angular/router";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(
    private categoryService: CategoryService,
    private authService: AuthService,
    private router: Router,
  ) { }

  public categories: CategoryModel[];
  public user: UserModel;

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((categories) => {
      this.categories = categories;
    })
    this.router.events.subscribe(() => {
      this.user = this.authService.getUser();
    })
  }

}
