import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { SnackbarService } from "../../layout/snackbar.service";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { FieldErrorStateMatcher } from "../../auth/login/login.component";
import { ProductService } from "../product.service";
import { CategoryService } from "../category.service";
import { ActivatedRoute } from "@angular/router";
import { CategoryModel } from "../category.model";
import { CreateProductRequest, ProductModel } from "../product.model";
import { CreatePropertyRequest } from "../property.model";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.css']
})
export class AddProductComponent implements OnInit {
  @Output('add') addEvent: EventEmitter<ProductModel> = new EventEmitter<ProductModel>();

  constructor(
    private categoryService: CategoryService,
    private productService: ProductService,
    private snackbarService: SnackbarService,
    private activatedRoute: ActivatedRoute,
  ) { }

  public category: CategoryModel;

  public productForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.required]),
    externalUrl: new FormControl('', []),
    price: new FormControl('', [Validators.required]),
    hasStock: new FormControl('', []),
    image: new FormControl('', [Validators.required]),
  });

  public matcher = new FieldErrorStateMatcher();
  public panelOpenState: boolean = false;
  public selectedImage: string = "Select a product image";
  public imageBlob: File;

  imageInputChange(fileInputEvent: any) {
    this.selectedImage = fileInputEvent.target.files[0].name;
    this.imageBlob = fileInputEvent.target.files[0];
  }

  add() {
    const properties: CreatePropertyRequest[] = this.category.propertyTypes.map(type => {
      return {
        value: this.productForm.value[type.id + '.value'],
        description: this.productForm.value[type.id + '.description'],
        highlight: this.productForm.value[type.id + '.highlight'] || false,
        propertyType: type,
      }
    })
    const product: CreateProductRequest = {
      name: this.productForm.get('name')?.value,
      description: this.productForm.get('description')?.value,
      externalUrl: this.productForm.get('externalUrl')?.value,
      price: this.productForm.get('price')?.value,
      hasStock: this.productForm.get('hasStock')?.value || false,
      category: this.category,
      properties: properties,
    }
    this.productService.add(product, this.imageBlob).subscribe((product) => {
      this.panelOpenState = false;
      this.snackbarService.openSnackBar(`${this.productForm.value.name} product was added`, '', 5000);
      this.productForm.reset();
      this.selectedImage = "Select a product image";
      this.productForm.clearValidators();
      this.addEvent.emit(product);
    });
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params) => {
      this.categoryService.getCategoryById(params.get('category') as string).subscribe(category => {
        this.category = category;
        category.propertyTypes.forEach(type => {
          this.productForm.addControl(type.id + '.value', new FormControl('', [Validators.required]));
          this.productForm.addControl(type.id + '.description', new FormControl('', [Validators.required]));
          this.productForm.addControl(type.id + '.highlight', new FormControl('', []));
        })
      })
    })
  }

}
