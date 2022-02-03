import { Component, OnInit } from '@angular/core';
import { ProductService } from "../product.service";
import { ActivatedRoute } from "@angular/router";
import { ProductModel } from "../product.model";
import { UserModel } from "../../auth/user.model";
import { AuthService } from "../../auth/auth.service";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  constructor(
    private productService: ProductService,
    private activeRoute: ActivatedRoute,
    private authService: AuthService,
  ) { }

  public products: ProductModel[];
  public user: UserModel;
  public category: string;

  ngOnInit(): void {
    this.activeRoute.paramMap.subscribe((params) => {
      this.category = params.get('category') as string;
      this.productService.getProducts(this.category).subscribe((products) => {
        this.products = products;
      })
    })
    this.user = this.authService.getUser();
  }

  add() {
    this.productService.getProducts(this.category).subscribe((products) => {
      this.products = products;
    })
  }

  delete(event: ProductModel) {
    this.products = this.products.filter(product => product.id !== event.id);
  }

}
