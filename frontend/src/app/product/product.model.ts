import { CategoryModel } from "./category.model";
import { CreatePropertyRequest, PropertyModel } from "./property.model";

export type CreateProductRequest = {
  name: string,
  description: string,
  externalUrl: string,
  price: number,
  hasStock: boolean,
  category: CategoryModel,
  properties: CreatePropertyRequest[],
}

export class ProductModel {
  constructor(
    public name: string,
    public description: string,
    public externalUrl: string,
    public price: number,
    public hasStock: boolean,
    public category: CategoryModel,
    public properties: PropertyModel[],
    public imageUrl: string,
    public id: string,
) {}
}
