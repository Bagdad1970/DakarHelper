import { Category } from '../Category.ts';

export type HeaderCellWithSubcategory = {
    id: bigint;
    subcategoryName: string;
    originalName: string;
    category: Category;
    isProcessing: boolean;
}