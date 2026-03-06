import { VendorsCheckboxes } from "./VendorsCheckboxes.tsx";
import '../../assets/styles/product-finder/QueryParams.css';
import {type ChangeEvent, type FormEvent, useState} from "react";

interface QueryParamsProps {
    onFind: (data: Params) => void;
}

type Params = {
    name: string,
    price: string,
    quantity: number,
    margin: number,
    vendorIds: Set<bigint>
}

export default function QueryParams({ onFind }: QueryParamsProps) {
    const [formData, setFormData] = useState<Params>({
        name: '',
        price: '',
        quantity: 1,
        margin: 0,
        vendorIds: new Set<bigint>
    });

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

    const handleQueryChange = (e: ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const submitData = {
            name: formData.name,
            price: formData.price,
            quantity: formData.quantity,
            margin: formData.margin,
            vendorIds: formData.vendorIds
        };

        onFind(submitData);
    };

    return (
        <div className="query-params">
            <form className="query-params-form">
                <input
                    type="text"
                    name="name"
                    placeholder="Наименование"
                    value={formData.name}
                    onChange={handleQueryChange}
                />

                <input
                    type="number"
                    min="0"
                    step="100"
                    name="price"
                    placeholder="Цена (руб)"
                    value={formData.price}
                    onChange={handleQueryChange}
                />

                <input
                    type="number"
                    min="1"
                    name="quantity"
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
                    name="margin"
                    placeholder="Процент"
                    value={formData.margin}
                    onChange={handleQueryChange}
                />

                <button type="submit" onClick={handleSubmit}>Найти</button>
            </form>
        </div>
    );
}