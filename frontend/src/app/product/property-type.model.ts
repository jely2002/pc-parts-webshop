export enum DataType {
  INT= "INT",
  DOUBLE= "DOUBLE",
  STRING = "STRING"

}

export class PropertyTypeModel {
  constructor(
    public id: string,
    public type: DataType,
    public name: string,
  ) {}
}
