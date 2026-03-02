import { Category } from '../Category.ts';

export type SubcategoryCreateRequest = {
    name: string;
    category: Category;
}