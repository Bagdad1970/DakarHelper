import apiClient from "./ApiClient.ts";

export default class ExcelParserManager {

    async parse(): Promise<void> {
        try {
            await apiClient.post("excel-parser/parse");
        }
        catch (error) {
            console.error("Error parsing Excel files:", error);
            throw error;
        }
    }

}