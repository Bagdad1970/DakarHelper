import QueryParams from "../components/product-finder/QueryParams.tsx";
import QueryTable from "../components/product-finder/QueryTable.tsx";
import PageNav from "../components/PageNav.tsx";
import '../assets/styles/product-finder/ProductFinder.css';
import {useState} from "react";
import type { ProductQuery } from "../types/product/ProductQuery.ts";

export default function ProductFinder() {
    const [formData, setFormData] = useState<ProductQuery>({
        name: '',
        minPrice: null,
        quantity: 1,
        margin: 0,
        vendorIds: new Set<bigint>(),
        pageIndex: 0,
        pageSize: 20
    });

    const [isButtonClicked, setIsButtonClicked] = useState(false);

    const handleFormSubmit = (data: ProductQuery) => {
        setFormData(data);
        setIsButtonClicked(true);
    }

    const handleReacted = () => {
        setIsButtonClicked(false);
    };

    return (
        <div className="product-finder-page">
            <PageNav />
            <div className="product-finder-content">
                <div className="query-params-section">
                    <QueryParams
                        onFind={handleFormSubmit}
                    />
                </div>

                <QueryTable
                    formData={formData}
                    isClicked={isButtonClicked}
                    onReacted={handleReacted}
                />
            </div>
        </div>
    );
}