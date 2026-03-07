import type {ProductQuery} from "../types/product/ProductQuery.ts";
import type {ProductQueryResponse} from "../types/product/ProductQueryResponse.ts";
import apiClient from "./ApiClient.ts";

export class ProductManager {

    async query(obj: ProductQuery): Promise<ProductQueryResponse> {
        try {
            const response = await apiClient.post<ProductQueryResponse>("products/query", obj);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching products:", error);
            throw error;
        }
    }

}