import { Component, Input, OnInit } from '@angular/core';
import { ProductModel } from "../product.model";
import { PropertyModel } from "../property.model";
import { CartService } from "../../cart/cart.service";
import { SnackbarService } from "../../layout/snackbar.service";
import { Router } from "@angular/router";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {
  @Input('product') product: ProductModel;

  highlightedProperties: PropertyModel[];

  constructor(
    private cartService: CartService,
    private snackbarService: SnackbarService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.highlightedProperties = this.product.properties.filter(property => property.highlight);
  }

  addToCart(product: ProductModel) {
    const newAmount = this.cartService.add(product.id);
    this.snackbarService.openSnackBar(`${product.name} added to cart (${newAmount})`, 'View cart', 2000)
      .subscribe(() => {
        this.router.navigate(['cart']);
      });
  }

}
