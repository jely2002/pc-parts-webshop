import { PropertyTypeModel } from "./property-type.model";

export class CategoryModel {
  constructor(
    public name: string,
    public description: string,
    public propertyTypes: PropertyTypeModel[]
  ) {}
}
