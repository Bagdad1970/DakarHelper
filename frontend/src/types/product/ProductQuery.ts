import Decimal from "decimal.js";
import type {SortDirection} from "./SortDirection.ts";

export interface ProductQuery {
    vendorIds: bigint[];
    name: string;
    price: Decimal | null;
    quantity: number;
    margin: number;
    sortingConditions: Map<string, SortDirection>
    pageIndex: number;
    pageSize: number;
}