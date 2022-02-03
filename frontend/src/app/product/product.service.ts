import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { CreateProductRequest, ProductModel } from "./product.model";
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

  public delete(product: ProductModel): Observable<void> {
    return this.http.delete<void>(`${environment.apiUrl}/product/${product.id}`);
  }

  public add(product: CreateProductRequest, file: File): Observable<ProductModel> {
    return new Observable<ProductModel>(observer => {
      this.http.post<ProductModel>(`${environment.apiUrl}/product`, product).subscribe(product => {
        const formData: FormData = new FormData();
        formData.append("file", file, file.name);
        this.http.post<void>(`${environment.apiUrl}/product/${product.id}/image`, formData).subscribe(() => {
          observer.next(product);
          observer.complete();
        });
      })
    });

  }

}
