import { Category } from '../Category.ts';

export type HeaderCellCreateRequest = {
    subcategoryId: bigint | null;
    originalName: string;
    category: Category | null;
    isProcessing: boolean;
}