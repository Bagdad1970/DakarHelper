import type { Vendor } from "../types/vendor/Vendor.ts";
import api from "./Client.ts";
import type {VendorCreateRequest} from "../types/vendor/VendorCreateRequest.ts";

export default class VendorManager {
    
    async create(vendor: VendorCreateRequest): Promise<Vendor> {
        try {
            console.log(vendor);
            const response = await api.post<Vendor>("vendors", vendor);
            return response.data;
        }
        catch (error) {
            console.error("Error creating vendor:", error);
            throw error;
        }
    }

    async update(vendor: Vendor): Promise<Vendor> {
        try {
            const response = await api.put<Vendor>(`vendors/${vendor.id}`, vendor);
            return response.data;
        }
        catch (error) {
            console.error("Error updating vendor:", error);
            throw error;
        }
    }

    async findAll(): Promise<Vendor[]> {
        try {
            const response = await api.get<Vendor[]>("vendors");
            return response.data;
        }
        catch (error) {
            console.error("Error fetching vendors:", error);
            throw error;
        }
    }

    async findById(id: bigint): Promise<Vendor> {
        try {
            const response = await api.get<Vendor>(`vendors/${id}`);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching Vendor by id:", error);
            throw error;
        }
    }

    async deleteById(id: bigint): Promise<void> {
        try {
            await api.delete(`vendors/${id}`);
        }
        catch (error) {
            console.error("Error deleting vendor:", error);
            throw error;
        }
    }

}