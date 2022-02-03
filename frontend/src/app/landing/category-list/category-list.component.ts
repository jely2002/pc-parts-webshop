import { Component, OnInit } from '@angular/core';
import { CategoryService } from "../../product/category.service";
import { CategoryModel } from "../../product/category.model";
import { AuthService } from "../../auth/auth.service";
import { UserModel } from "../../auth/user.model";
import { SnackbarService } from "../../layout/snackbar.service";

@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.css']
})
export class CategoryListComponent implements OnInit {

  constructor(
    private categoryService: CategoryService,
    private authService: AuthService,
    private snackbarService: SnackbarService,
  ) {
  }

  public categories: CategoryModel[];
  public user: UserModel;

  ngOnInit(): void {
    this.categoryService.getCategories().subscribe(categories => {
      this.categories = categories;
    })
    this.user = this.authService.getUser();
  }

  add(event: CategoryModel) {
    this.categories.push(event);
  }

  delete(category: string) {
    this.categoryService.delete(category).subscribe({
        next: () => {
          this.categories = this.categories.filter(cat => cat.name !== category);
          this.snackbarService.openSnackBar(`${category} category was deleted`, '', 5000);
        },
        error: () => {
          this.snackbarService.openSnackBar(`Unable to delete category`, '', 5000);
        }
      }
    )
  }

}
