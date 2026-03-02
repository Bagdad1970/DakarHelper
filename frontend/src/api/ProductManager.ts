import type {ProductQuery} from "../types/product/ProductQuery.ts";
import type {ProductQueryResponse} from "../types/product/ProductQueryResponse.ts";
import api from "./Client.ts";

export class ProductManager {

    async query(obj: ProductQuery): Promise<ProductQueryResponse> {
        try {
            const response = await api.post<ProductQueryResponse>("products/query", obj);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching products:", error);
            throw error;
        }
    }

}