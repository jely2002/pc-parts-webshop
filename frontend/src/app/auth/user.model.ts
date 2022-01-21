import { CartItemModel } from "../cart/cart-item.model";

export class UserModel {
  constructor(
    public id: string,
    public firstName: string,
    public middleName: string,
    public lastName: string,
    public role: string,
    public cartItems: CartItemModel[],
  ) {}
}
