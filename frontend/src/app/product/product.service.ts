import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ProductModel } from "./product.model";
import { Observable } from "rxjs";
import { environment } from "../../environments/environment.prod";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  public getProducts(category: string): Observable<ProductModel[]> {
    return this.http.get<ProductModel[]>(`${environment.apiUrl}/product/category/${category}`);
  }

  public getProductsById(ids: string[]): Observable<ProductModel[]> {
    const stringifiedIds = ids.join(',');
    return this.http.get<ProductModel[]>(`${environment.apiUrl}/product/${stringifiedIds}`);
  }

}
