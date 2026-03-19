import type { Vendor } from "../types/vendor/Vendor.ts";
import apiClient from "./ApiClient.ts";
import type {VendorCreateRequest} from "../types/vendor/VendorCreateRequest.ts";
import type {BatchDeleteRequest} from "../types/BatchDeleteRequest.ts";

export default class VendorManager {
    
    async create(vendor: VendorCreateRequest): Promise<Vendor> {
        try {
            const response = await apiClient.post<Vendor>("vendors", vendor);
            return response.data;
        }
        catch (error) {
            console.error("Error creating vendor:", error);
            throw error;
        }
    }

    async update(vendor: Vendor): Promise<Vendor> {
        try {
            const response = await apiClient.put<Vendor>(`vendors/${vendor.id}`, vendor);
            return response.data;
        }
        catch (error) {
            console.error("Error updating vendor:", error);
            throw error;
        }
    }

    async findAll(): Promise<Vendor[]> {
        try {
            const response = await apiClient.get<Vendor[]>("vendors");
            return response.data;
        }
        catch (error) {
            console.error("Error fetching vendors:", error);
            throw error;
        }
    }

    async findById(id: bigint): Promise<Vendor> {
        try {
            const response = await apiClient.get<Vendor>(`vendors/${id}`);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching Vendor by id:", error);
            throw error;
        }
    }

    async deleteById(id: bigint): Promise<void> {
        try {
            await apiClient.delete(`vendors/${id}`);
        }
        catch (error) {
            console.error("Error deleting vendor:", error);
            throw error;
        }
    }

    async batchDelete(ids: BatchDeleteRequest): Promise<void> {
        try {
            await apiClient.post(`header-cells/batch-delete`, ids);
        }
        catch (error) {
            console.error("Error deleting vendors:", error);
            throw error;
        }
    }

}