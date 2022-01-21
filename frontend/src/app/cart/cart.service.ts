import { Injectable } from '@angular/core';
import { CookieService } from "ngx-cookie-service";
import { CartItemModel } from "./cart-item.model";

@Injectable({
  providedIn: 'root'
})
export class CartService {

  constructor(private cookieService: CookieService) { }

  public cartCookieName = 'pcparts_cart'

  public get(): CartItemModel[] {
    if (this.cookieService.check(this.cartCookieName)) {
      return JSON.parse(this.cookieService.get(this.cartCookieName));
    } else {
      return [];
    }
  }

  public empty() {
    this.cookieService.delete(this.cartCookieName);
  }

  public setAmount(productId: string, amount: number) {
    if (!this.cookieService.check(this.cartCookieName)) return;
    const existingCart: CartItemModel[] = JSON.parse(this.cookieService.get(this.cartCookieName));
    const existingItem = existingCart.find(item => item.productId === productId);
    if (existingItem == null) return;
    if (amount < 1) {
      const newCart = existingCart.filter(item => item.productId !== productId);
      this.cookieService.set(this.cartCookieName, JSON.stringify(newCart), this.getNewExpiry(), "/");
    } else {
      existingItem.amount = amount;
      this.cookieService.set(this.cartCookieName, JSON.stringify(existingCart), this.getNewExpiry(), "/");
    }
  }

  public add(productId: string): number {
    let newCart: CartItemModel[] = [{
      productId: productId,
      amount: 1,
    }];
    let amount = 1;
    if(this.cookieService.check(this.cartCookieName)) {
      const existingCart: CartItemModel[] = JSON.parse(this.cookieService.get(this.cartCookieName));
      const existingItem = existingCart.find(item => item.productId === productId);
      if (existingItem) {
        if (existingItem.amount >= 50) return existingItem.amount;
        existingItem.amount++;
        amount = existingItem.amount;
        newCart = existingCart;
      } else {
        newCart = existingCart.concat(newCart);
      }
    }
    this.cookieService.set(this.cartCookieName, JSON.stringify(newCart), this.getNewExpiry(), "/");
    return amount;
  }

  public getNewExpiry(): Date {
    const date = new Date();
    const millisecondsInMonth = 30*24*60*60000
    date.setDate(date.getDate() + millisecondsInMonth);
    return date;
  }

}
