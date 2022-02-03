import { CartItemModel } from "../cart/cart-item.model";

export type UpdateUserRequest = {
  email?: string,
  firstName?: string,
  middleName?: string,
  lastName?: string,
  password?: string,
}

export class UserModel {
  constructor(
    public id: string,
    public email: string,
    public firstName: string,
    public middleName: string,
    public lastName: string,
    public role: string,
    public cartItems: CartItemModel[],
  ) {}
}
