import { Category } from '../Category.ts';

export type Subcategory = {
    id: bigint;
    name: string;
    category: Category;
}