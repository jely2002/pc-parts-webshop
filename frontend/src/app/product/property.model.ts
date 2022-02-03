import { PropertyTypeModel } from "./property-type.model";

export type CreatePropertyRequest = {
  value: string,
  description: string,
  highlight: boolean,
  propertyType: PropertyTypeModel,
}

export class PropertyModel {
  constructor(
    public id: string,
    public propertyType: PropertyTypeModel,
    public value: string,
    public description: string,
    public highlight: boolean,
  ) {}
}
