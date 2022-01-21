import { Component, OnInit } from '@angular/core';
import { FieldErrorStateMatcher } from "../../auth/login/login.component";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CategoryService } from "../../product/category.service";
import { SnackbarService } from "../../layout/snackbar.service";

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent {

  constructor(
    private categoryService: CategoryService,
    private snackbarService: SnackbarService,
  ) { }

  public categoryForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
  });

  public matcher = new FieldErrorStateMatcher();
  public panelOpenState: boolean = false;

  add() {
    this.categoryService.add(this.categoryForm.value).subscribe(() => {
      this.panelOpenState = false;
      this.snackbarService.openSnackBar(`${this.categoryForm.value.name} category was added`, '', 5000);
      this.categoryForm.reset();
      this.categoryForm.clearValidators();
    });
  }

}
