import Decimal from "decimal.js";

export type ProductQueryItem = {
    name: string;
    vendorTitle: string;
    price: Decimal;
    totalQuantity: number;
    priceWithMargin: Decimal;
}