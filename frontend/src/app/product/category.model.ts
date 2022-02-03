import { CreatePropertyTypeRequest, PropertyTypeModel } from "./property-type.model";

export type CreateCategoryRequest = {
  name: string,
  description: string,
  propertyTypes: CreatePropertyTypeRequest[],
}

export class CategoryModel {
  constructor(
    public name: string,
    public description: string,
    public propertyTypes: PropertyTypeModel[]
  ) {}
}
