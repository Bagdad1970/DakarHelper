    import { Category } from '../Category.ts';

    export type HeaderCellUpdateRequest = {
        id: bigint,
        subcategoryId: string | null;
        originalName: string;
        category: Category | null;
        isProcessing: boolean;
    }