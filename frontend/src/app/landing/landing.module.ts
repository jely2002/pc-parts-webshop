import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SplashComponent } from './splash/splash.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { LandingComponent } from './landing/landing.component';
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from "@angular/material/card";
import { MatButtonModule } from "@angular/material/button";
import { RouterModule } from "@angular/router";



@NgModule({
  declarations: [
    SplashComponent,
    CategoryListComponent,
    LandingComponent
  ],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    MatButtonModule,
    RouterModule
  ]
})
export class LandingModule { }
