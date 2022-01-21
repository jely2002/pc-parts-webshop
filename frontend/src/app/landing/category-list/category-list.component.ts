import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../product/category.service";
import { CategoryModel } from "../../product/category.model";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  constructor(private categoryService: CategoryService) { }

  public categories: CategoryModel[];

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    })
  }

}
