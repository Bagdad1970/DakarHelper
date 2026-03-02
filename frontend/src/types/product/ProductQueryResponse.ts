import type {ProductQueryItem} from "./ProductQueryItem.ts";
import type {Pagination} from "./Pagination.ts";

export type ProductQueryResponse = {
    productData: ProductQueryItem[];
    pagination: Pagination;
}