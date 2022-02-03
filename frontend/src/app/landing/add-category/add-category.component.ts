import { Component, EventEmitter, Output } from '@angular/core';
import { FieldErrorStateMatcher } from "../../auth/login/login.component";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { CategoryService } from "../../product/category.service";
import { SnackbarService } from "../../layout/snackbar.service";
import { CategoryModel, CreateCategoryRequest } from "../../product/category.model";
import { CreatePropertyRequest, PropertyModel } from "../../product/property.model";
import { CreatePropertyTypeRequest, DataType } from "../../product/property-type.model";

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent {
  @Output('add') addEvent: EventEmitter<CategoryModel> = new EventEmitter<CategoryModel>();

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

  public propertyTypes: string[] = [];
  public dataTypes: string[] = Object.keys(DataType);

  addPropertyType() {
    const id = String(Math.random() * 1000);
    this.categoryForm.addControl(id + '.name',new FormControl('', [Validators.required]));
    this.categoryForm.addControl(id + '.type',new FormControl('STRING', []));
    this.propertyTypes.push(id);
  }

  removePropertyType(id: string) {
    this.propertyTypes = this.propertyTypes.filter(type => type !== id);
    this.categoryForm.removeControl(id + '.name');
    this.categoryForm.removeControl(id + '.type');
  }

  add() {
    const properties: CreatePropertyTypeRequest[] = this.propertyTypes.map(type => {
      return {
        name: this.categoryForm.value[type + '.name'],
        type: this.categoryForm.value[type + '.type'],
      }
    })
    const category: CreateCategoryRequest = this.categoryForm.value;
    category.propertyTypes = properties;
    this.categoryService.add(category).subscribe((model) => {
      this.panelOpenState = false;
      this.snackbarService.openSnackBar(`${this.categoryForm.value.name} category was added`, '', 5000);
      this.categoryForm.reset();
      this.categoryForm.clearValidators();
      this.addEvent.emit(model);
    });
  }

}
