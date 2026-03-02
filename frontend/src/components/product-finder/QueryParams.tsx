import { VendorsCheckboxes } from "./VendorsCheckboxes.tsx";
import '../../assets/styles/product-finder/QueryParams.css';
import {useState, useEffect} from "react";
import type {ProductQuery} from "../../types/product/ProductQuery.ts";
import Decimal from "decimal.js";

interface QueryParamsProps {
    onQueryChange: (query: Partial<ProductQuery>) => void;
    onSearch: () => void;
}

type Params = {
    name: string,
    price: string,
    quantity: string,
    margin: string,
    vendorIds: Set<bigint>
}

export default function QueryParams({ onQueryChange, onSearch }: QueryParamsProps) {
    const [formData, setFormData] = useState<Params>({
        name: '',
        price: '',
        quantity: '',
        margin: '',
        vendorIds: new Set<bigint>
    });

    useEffect(() => {
        onQueryChange({
            name: formData.name,
            price: formData.price ? Decimal(formData.price) : null,
            quantity: formData.quantity ? parseInt(formData.quantity) : 0,
            margin: formData.margin ? Decimal(formData.margin) : null,
            vendorIds: formData.vendorIds
        });
    }, [formData, onQueryChange]);

    const handleCheckboxChange = (vendorId: bigint, checked: boolean) => {
        setFormData(prev => {
            const newSet = new Set(prev.vendorIds);
            if (checked) {
                newSet.add(vendorId);
            }
            else {
                newSet.delete(vendorId);
            }

            return {
                ...prev,
                vendorIds: newSet
            };
        });
    };

    const handleQueryChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { id, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [id.replace('product-', '')]: value
        }));
    };

    return (
        <div className="query-params">
            <form className="query-params-form" action={onSearch}>
                <input
                    type="text"
                    id="product-name"
                    placeholder="Наименование"
                    value={formData.name}
                    onChange={handleQueryChange}
                />

                <input
                    type="number"
                    min="0"
                    step="10"
                    id="product-price"
                    placeholder="Цена (руб)"
                    value={formData.price}
                    onChange={handleQueryChange}
                />

                <input
                    type="number"
                    min="0"
                    id="product-quantity"
                    placeholder="Количество"
                    value={formData.quantity}
                    onChange={handleQueryChange}
                />

                <VendorsCheckboxes
                    onCheckboxChange={handleCheckboxChange}
                    selectedIds={formData.vendorIds}
                />

                <input
                    type="number"
                    min="0"
                    step="0.1"
                    id="product-margin"
                    placeholder="Процент"
                    value={formData.margin}
                    onChange={handleQueryChange}
                />

                <button type="submit">Найти</button>
            </form>
        </div>
    );
}