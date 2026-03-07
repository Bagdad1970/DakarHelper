import Decimal from "decimal.js";

export type ProductQueryItem = {
    name: string;
    vendorTitle: string;
    minPrice: Decimal;
    totalQuantity: number;
    priceWithMargin: Decimal;
}