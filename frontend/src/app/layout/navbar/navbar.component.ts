import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../product/category.service";
import { CategoryModel } from "../../product/category.model";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(
    private categoryService: CategoryService,
  ) { }

  public categories: CategoryModel[];

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe((categories) => {
      this.categories = categories;
    })
  }

}
