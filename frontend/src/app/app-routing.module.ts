import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductListComponent } from "./product/product-list/product-list.component";
import { CartViewComponent } from "./cart/cart-view/cart-view.component";
import { LoginViewComponent } from "./auth/login-view/login-view.component";

const routes: Routes = [
  { path: 'parts/:category', component: ProductListComponent },
  { path: 'cart', component: CartViewComponent },
  { path: 'login', component: LoginViewComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
