import { PropertyTypeModel } from "./property-type.model";

export class PropertyModel {
  constructor(
    public id: string,
    public propertyType: PropertyTypeModel,
    public value: string,
    public description: string,
    public highlight: boolean,
  ) {}
}
