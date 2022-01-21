import { Component, OnInit } from '@angular/core';
import { ProductService } from "../product.service";
import { ActivatedRoute } from "@angular/router";
import { ProductModel } from "../product.model";

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  constructor(
    private productService: ProductService,
    private activeRoute: ActivatedRoute
  ) { }

  public products: ProductModel[];

  ngOnInit(): void {
    this.activeRoute.paramMap.subscribe((params) => {
      this.productService.getProducts(params.get('category') as string).subscribe((products) => {
        this.products = products;
      })
    })
  }

}
