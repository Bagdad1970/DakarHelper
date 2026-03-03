import '../../assets/styles/product-finder/QueryTable.css';
import {
    createColumnHelper,
    flexRender,
    getCoreRowModel,
    useReactTable
} from "@tanstack/react-table";
import type { ProductQueryItem } from "../../types/product/ProductQueryItem.ts";
import { useEffect, useState } from "react";
import { ProductManager } from '../../api/ProductManager.ts';
import type { Pagination } from "../../types/product/Pagination.ts";
import type { ProductQuery } from "../../types/product/ProductQuery.ts";
import Decimal from "decimal.js";
import {PaginationView} from "./PaginationView.tsx";

const formatPrice = (decimal: Decimal): string => {
    const rounded = decimal.toFixed(2);
    const [integerPart, decimalPart] = rounded.split('.');
    const localizedIntegerPart = parseInt(integerPart).toLocaleString('ru-RU');
    return localizedIntegerPart + "," + decimalPart;
}

const columnHelper = createColumnHelper<ProductQueryItem>();

const columns = [
    columnHelper.accessor('name', {
        header: 'Наименование',
        cell: info => info.getValue(),
    }),
    columnHelper.accessor('price', {
        header: 'Цена',
        cell: info => formatPrice(info.getValue()),
    }),
    columnHelper.accessor('totalQuantity', {
        header: 'Количество',
        cell: info => info.getValue(),
    }),
    columnHelper.accessor('priceWithMargin', {
        header: 'Цена продавца',
        cell: info => formatPrice(info.getValue()),
    }),
    columnHelper.accessor('vendorTitle', {
        header: 'Поставщик',
        cell: info => info.getValue(),
    })
];

interface QueryTableProps {
    formData: ProductQuery;
    isClicked: boolean;
    onReacted: () => void;
}

export default function QueryTable({ formData, isClicked, onReacted }: QueryTableProps) {
    const productManager = new ProductManager();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [data, setData] = useState<ProductQueryItem[]>([]);
    const [pagination, setPagination] = useState<Pagination>({
        totalPages: 0,
        totalRecords: 0n,
        pageIndex: 0,
        pageSize: 20
    });

    const loadPage = async () => {
        try {
            console.log(formData);
            setLoading(true);

            const query = {
                ...formData,
                pageSize: pagination.pageSize,
                pageIndex: pagination.pageIndex,
                vendorIds: Array.from(formData.vendorIds)
            };

            const result = await productManager.query(query);

            setPagination(result.pagination);
            setData(result.productData);
            setError(null);
        }
        catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to process query');
        }
        finally {
            setLoading(false);
        }
    }

    // Загрузка при нажатии на кнопку поиска
    useEffect(() => {
        if (isClicked) {
            setPagination(prev => ({ ...prev, pageIndex: 0 }));
            loadPage();
            onReacted();
        }
    }, [isClicked]);

    useEffect(() => {
        if (pagination.pageIndex > 0 || data.length > 0) {
            loadPage();
        }
    }, [pagination.pageIndex]);

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true
    });

    const handlePagination = (pageIndex: number)=> {
        setPagination(prev => ({
            ...prev,
            pageIndex: pageIndex
        }));
    }

    if (loading && !data.length) {
        return (
            <div className="query-table-section">
                <p>Loading data...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="query-table-section">
                <div className="data-error">
                    <p>Error: {error}</p>
                </div>
            </div>
        );
    }

    if (data.length === 0 && !loading) {
        return (
            <div className="query-table-section">
                <div className="data-empty">
                    <div>Ничего не найдено</div>
                </div>
            </div>
        );
    }

    return (
        <div className="query-table-section">
            <div className="table-container">
                <table>
                    <thead>
                    {table.getHeaderGroups().map(headerGroup => (
                        <tr key={headerGroup.id}>
                            {headerGroup.headers.map(header => (
                                <th
                                    key={header.id}
                                    onClick={header.column.getToggleSortingHandler()}
                                    className={
                                        header.column.getCanSort()
                                            ? 'sortable' + (header.column.getIsSorted()
                                            ? header.column.getIsSorted() === 'asc'
                                                ? ' sorted-asc'
                                                : ' sorted-desc'
                                            : '')
                                            : ''
                                    }
                                >
                                    {flexRender(
                                        header.column.columnDef.header,
                                        header.getContext()
                                    )}
                                </th>
                            ))}
                        </tr>
                    ))}
                    </thead>
                    <tbody>
                    {table.getRowModel().rows.map(row => (
                        <tr key={row.id}>
                            {row.getVisibleCells().map(cell => (
                                <td key={cell.id}>
                                    {flexRender(
                                        cell.column.columnDef.cell,
                                        cell.getContext()
                                    )}
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>

                {loading && data.length > 0 && (
                    <div className="loading-overlay">
                        <p>Загрузка...</p>
                    </div>
                )}
            </div>

            {pagination && pagination.totalPages > 1 && (
                <PaginationView
                    pagination={pagination}
                    onPageChange={handlePagination}
                />
            )}
        </div>
    );
}