import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment.prod";
import { CategoryModel } from "./category.model";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(private http: HttpClient) {}

  public getCategories(): Observable<CategoryModel[]> {
    return this.http.get<CategoryModel[]>(`${environment.apiUrl}/category`);
  }

  public delete(category: string): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/category/${category}`);
  }

  public add(category: CategoryModel): Observable<CategoryModel> {
    return this.http.post<CategoryModel>(`${environment.apiUrl}/category`, category);
  }

}
