import { Category } from '../Category.ts';

export type HeaderCell = {
    id: bigint;
    subcategoryId: bigint | null;
    originalName: string;
    category: Category;
    isProcessing: boolean;
}