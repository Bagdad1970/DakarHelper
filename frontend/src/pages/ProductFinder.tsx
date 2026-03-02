import QueryParams from "../components/product-finder/QueryParams.tsx";
import QueryTable from "../components/product-finder/QueryTable.tsx";
import PageNav from "../components/PageNav.tsx";
import '../assets/styles/product-finder/ProductFinder.css';
import {useState, useCallback} from "react";
import type { ProductQuery } from "../types/product/ProductQuery.ts";

export default function ProductFinder() {
    const [formQuery, setFormQuery] = useState<ProductQuery>({
        name: '',
        price: null,
        quantity: 0,
        margin: null,
        vendorIds: new Set<bigint>(),
        pageIndex: 0,
        pageSize: 20
    });

    const [findClicked, setFindClicked] = useState<boolean>(false);

    const handleProductQueryChange = useCallback((updatedFields: Partial<ProductQuery>) => {
        setFormQuery(prev => ({
            ...prev,
            ...updatedFields
        }));
    }, []);

    const handleFindClick = () => {
        setFindClicked(prev => !prev);
    };

    return (
        <div className="product-finder-page">
            <PageNav />
            <div className="product-finder-content">
                <div className="query-params-section">
                    <QueryParams
                        onQueryChange={handleProductQueryChange}
                        onSearch={handleFindClick}
                    />
                </div>

                <QueryTable
                    productQuery={formQuery}
                    findClicked={findClicked}
                />
            </div>
        </div>
    );
}