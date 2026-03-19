import type {HeaderCell} from "../types/headercell/HeaderCell.ts";
import apiClient from "./ApiClient.ts";
import type {HeaderCellWithSubcategory} from "../types/headercell/HeaderCellWithSubcategory.ts";
import type {BatchDeleteRequest} from "../types/BatchDeleteRequest.ts";

export default class HeaderCellManager  {

    async create(headerCell: Omit<HeaderCell, 'id'>): Promise<HeaderCell> {
        try {
            const response = await apiClient.post<HeaderCell>("header-cells", headerCell);
            return response.data;
        }
        catch (error) {
            console.error("Error creating HeaderCell:", error);
            throw error;
        }
    }

    async update(headerCell: HeaderCell): Promise<HeaderCell> {
        try {
            const response = await apiClient.put<HeaderCell>(`header-cells/${headerCell.id}`, headerCell);
            return response.data;
        }
        catch (error) {
            console.error("Error updating HeaderCell:", error);
            throw error;
        }
    }

    async findAll(): Promise<HeaderCellWithSubcategory[]> {
        try {
            const response = await apiClient.get<HeaderCellWithSubcategory[]>("header-cells/with-subcategory");
            return response.data;
        }
        catch (error) {
            console.error("Error fetching HeaderCells:", error);
            throw error;
        }
    }

    async findById(id: bigint): Promise<HeaderCell> {
        try {
            const response = await apiClient.get<HeaderCell>(`header-cells/${id}`);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching HeaderCell by id:", error);
            throw error;
        }
    }

    async deleteById(id: bigint): Promise<void> {
        try {
            await apiClient.delete(`header-cells/${id}`);
        }
        catch (error) {
            console.error("Error deleting HeaderCell:", error);
            throw error;
        }
    }

    async batchDelete(ids: BatchDeleteRequest): Promise<void> {
        try {
            await apiClient.post(`header-cells/batch-delete`, ids);
        }
        catch (error) {
            console.error("Error deleting HeaderCell:", error);
            throw error;
        }
    }

}