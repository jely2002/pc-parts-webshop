import { Component, OnInit } from '@angular/core';
import { ActivationEnd, NavigationStart, Router } from "@angular/router";
import { CategoryModel } from "../../product/category.model";
import { CategoryService } from "../../product/category.service";
import { filter, map } from "rxjs";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(
    private categoryService: CategoryService,
    private router: Router,
  ) { }

  currentCategory: CategoryModel | undefined;
  showHeader: boolean = true;

  private activeCategoryName: string;

  updateHeader() {
    this.categoryService.getCategories().subscribe(categories => {
      this.currentCategory = categories.find(category => category.name.toLowerCase() === this.activeCategoryName);
    })
  }

  ngOnInit(): void {
    this.updateHeader();
    this.router.events
      .pipe(
        filter(e => (e instanceof ActivationEnd)),
        map(e => e instanceof ActivationEnd ? e.snapshot.params : {})
      )
      .subscribe(params => {
        this.activeCategoryName = params['category'];
        this.showHeader = !!this.activeCategoryName;
      });
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        this.updateHeader();
      }
    });
  }

}
