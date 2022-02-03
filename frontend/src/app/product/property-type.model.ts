export enum DataType {
  INT= "INT",
  DOUBLE= "DOUBLE",
  STRING = "STRING"
}

export type CreatePropertyTypeRequest = {
  type: DataType,
  name: string,
}

export class PropertyTypeModel {
  constructor(
    public id: string,
    public type: DataType,
    public name: string,
  ) {}
}
