import { CategoryModel } from "./category.model";
import { PropertyModel } from "./property.model";

export class ProductModel {
  constructor(
    public id: string,
    public name: string,
    public description: string,
    public externalUrl: string,
    public price: number,
    public hasStock: boolean,
    public imageUrl: string,
    public category: CategoryModel,
    public properties: PropertyModel[]
  ) {}
}
