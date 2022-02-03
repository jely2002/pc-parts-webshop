import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment.prod";
import { CategoryModel, CreateCategoryRequest } from "./category.model";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) {}

  public getCategories(): Observable<CategoryModel[]> {
    return this.http.get<CategoryModel[]>(`${environment.apiUrl}/category`);
  }

  public getCategoryById(id: string): Observable<CategoryModel> {
    return this.http.get<CategoryModel>(`${environment.apiUrl}/category/${id}`);
  }

  public delete(category: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/category/${category}`);
  }

  public add(category: CreateCategoryRequest): Observable<CategoryModel> {
    category.name = category.name.toLowerCase();
    return this.http.post<CategoryModel>(`${environment.apiUrl}/category`, category);
  }

}
