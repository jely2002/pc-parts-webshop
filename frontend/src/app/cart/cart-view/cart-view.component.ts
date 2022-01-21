import { Component, OnInit } from '@angular/core';
import { CartService } from "../cart.service";
import { ProductService } from "../../product/product.service";
import { CartItemModel } from "../cart-item.model";

@Component({
  selector: 'app-cart-view',
  templateUrl: './cart-view.component.html',
  styleUrls: ['./cart-view.component.css']
})
export class CartViewComponent implements OnInit {

  constructor(
    private cartService: CartService,
    private productService: ProductService,
  ) { }

  products: CartItemModel[] = [];

  ngOnInit(): void {
   this.loadCart();
  }

  public loadCart() {
    this.products = [];
    const cartItems = this.cartService.get();
    const productIds = cartItems.map(item => item.productId);
    this.productService.getProductsById(productIds).subscribe(products => {
      products.map(product => {
        const cartItem = cartItems.find(item => item.productId === product.id);
        if (cartItem == null) return;
        cartItem.product = product;
        this.products.push(cartItem);
      })
    });
  }

  public updateCart() {
    const cartItems = this.cartService.get();
    this.products.forEach(product => {
      const cartItem = cartItems.find(item => item.productId === product.productId);
      if (cartItem == null) {
        this.products.splice(this.products.indexOf(product), 1);
        return;
      }
      product.amount = cartItem.amount;
    })
  }

  public getAmount(productId: string): string {
    const cartItem = this.products.find(item => item.productId === productId);
    if (cartItem == null) return '1';
    return cartItem.amount.toString();
  }

  public getTotal(): number {
    let total = 0;
    if (this.products == null) return 0;
    this.products.forEach(product => {
      total += (product.product?.price || 1) * product.amount;
    })
    return total;
  }

  public emptyCart() {
    this.cartService.empty();
    this.updateCart();
  }

  public amountChange(productId: string, event: Event) {
    if (event.target instanceof HTMLInputElement) {
      const target: HTMLInputElement = event.target;
      this.cartService.setAmount(productId, parseInt(target.value));
      this.updateCart();
    }
  }

}
