import type {Subcategory} from "../types/subcategory/Subcategory.ts";
import apiClient from "./ApiClient.ts";

export default class SubcategoryManager  {

    async create(subcategory: Omit<Subcategory, 'id'>): Promise<Subcategory> {
        try {
            const response = await apiClient.post<Subcategory>("subcategories", subcategory);
            return response.data;
        }
        catch (error) {
            console.error("Error creating Subcategory:", error);
            throw error;
        }
    }

    async update(subcategory: Subcategory): Promise<Subcategory> {
        try {
            const response = await apiClient.put<Subcategory>(`subcategories/${subcategory.id}`, subcategory);
            return response.data;
        }
        catch (error) {
            console.error("Error updating Subcategory:", error);
            throw error;
        }
    }

    async findAll(): Promise<Subcategory[]> {
        try {
            const response = await apiClient.get<Subcategory[]>("subcategories");
            return response.data;
        }
        catch (error) {
            console.error("Error fetching Subcategories:", error);
            throw error;
        }
    }

    async findById(id: bigint): Promise<Subcategory> {
        try {
            const response = await apiClient.get<Subcategory>(`subcategories/${id}`);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching Subcategory by id:", error);
            throw error;
        }
    }

    async deleteById(id: bigint): Promise<void> {
        try {
            await apiClient.delete(`subcategories/${id}`);
        }
        catch (error) {
            console.error("Error deleting Subcategory:", error);
            throw error;
        }
    }

}