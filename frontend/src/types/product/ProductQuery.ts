import Decimal from "decimal.js";

export interface ProductQuery {
    vendorIds: bigint[];
    name: string;
    price: Decimal | null;
    quantity: number;
    margin: Decimal | null;
    pageIndex: number;
    pageSize: number;
}