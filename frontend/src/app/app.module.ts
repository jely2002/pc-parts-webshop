import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProductModule } from "./product/product.module";
import { MatIconModule } from "@angular/material/icon";
import { MatToolbarModule } from "@angular/material/toolbar";
import { LayoutModule } from "./layout/layout.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { CartModule } from "./cart/cart.module";
import { AuthModule } from "./auth/auth.module";
import { LandingModule } from "./landing/landing.module";
import { AuthInterceptor } from "./auth/login/auth.interceptor";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ProductModule,
    MatIconModule,
    MatToolbarModule,
    LayoutModule,
    HttpClientModule,
    CartModule,
    AuthModule,
    LandingModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
