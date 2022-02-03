import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProductModel } from "../product.model";
import { PropertyModel } from "../property.model";
import { CartService } from "../../cart/cart.service";
import { SnackbarService } from "../../layout/snackbar.service";
import { Router } from "@angular/router";
import { ProductService } from "../product.service";

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent implements OnInit {
  @Input('product') product: ProductModel;
  @Input('admin') admin: boolean;
  @Output('delete') deleteEvent: EventEmitter<ProductModel> = new EventEmitter<ProductModel>();

  highlightedProperties: PropertyModel[];

  constructor(
    private cartService: CartService,
    private snackbarService: SnackbarService,
    private router: Router,
    private productService: ProductService,
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

  delete(product: ProductModel) {
    this.productService.delete(product).subscribe({
      next: () => {
        this.snackbarService.openSnackBar(`${this.product.name} was deleted`, '', 5000);
        this.deleteEvent.emit(product);
      },
      error: () => {
        this.snackbarService.openSnackBar(`Unable to delete category`, '', 5000);
      }
    });
  }

}
