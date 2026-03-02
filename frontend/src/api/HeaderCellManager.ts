import type {HeaderCell} from "../types/headercell/HeaderCell.ts";
import api from "./Client.ts";

export default class HeaderCellManager  {

    async create(headerCell: Omit<HeaderCell, 'id'>): Promise<HeaderCell> {
        try {
            const response = await api.post<HeaderCell>("header-cells", headerCell);
            return response.data;
        }
        catch (error) {
            console.error("Error creating HeaderCell:", error);
            throw error;
        }
    }

    async update(headerCell: HeaderCell): Promise<HeaderCell> {
        try {
            const response = await api.put<HeaderCell>(`header-cells/${headerCell.id}`, headerCell);
            return response.data;
        }
        catch (error) {
            console.error("Error updating HeaderCell:", error);
            throw error;
        }
    }

    async findAll(): Promise<HeaderCell[]> {
        try {
            const response = await api.get<HeaderCell[]>("header-cells");
            return response.data;
        }
        catch (error) {
            console.error("Error fetching HeaderCells:", error);
            throw error;
        }
    }

    async findById(id: bigint): Promise<HeaderCell> {
        try {
            const response = await api.get<HeaderCell>(`header-cells/${id}`);
            return response.data;
        }
        catch (error) {
            console.error("Error fetching HeaderCell by id:", error);
            throw error;
        }
    }

    async deleteById(id: bigint): Promise<void> {
        try {
            await api.delete(`header-cells/${id}`);
        }
        catch (error) {
            console.error("Error deleting HeaderCell:", error);
            throw error;
        }
    }

}