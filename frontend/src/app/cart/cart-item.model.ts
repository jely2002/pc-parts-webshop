import { ProductModel } from "../product/product.model";

export class CartItemModel {
  constructor(
    public productId: string,
    public amount: number,
    public product?: ProductModel,
  ) {}
}
