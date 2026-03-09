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
import {formatPrice} from "../../utils/DecimalHelper.ts";
import { decamelize } from 'humps';
import { SortDirection } from "../../types/product/SortDirection.ts";
import {BottomTable} from "./BottomTable.tsx";

const columnHelper = createColumnHelper<ProductQueryItem>();

const columns = [
    columnHelper.accessor('name', {
        header: 'Наименование',
        cell: info => info.getValue(),
        enableSorting: false,
        enableHiding: false
    }),
    columnHelper.accessor('minPrice', {
        header: 'Цена (руб)',
        cell: info => formatPrice(info.getValue()),
        enableSorting: true,
        enableHiding: false
    }),
    columnHelper.accessor('totalQuantity', {
        header: 'Количество',
        cell: info => info.getValue(),
        enableSorting: true,
        enableHiding: false
    }),
    columnHelper.accessor('priceWithMargin', {
        header: 'Цена продавца (руб)',
        cell: info => formatPrice(info.getValue()),
        enableSorting: false,
        enableHiding: true
    }),
    columnHelper.accessor('vendorTitle', {
        header: 'Поставщик',
        cell: info => info.getValue(),
        enableSorting: false,
        enableHiding: true
    })
];

interface QueryTableProps {
    formData: ProductQuery;
    isClicked: boolean;
    onReacted: () => void;
}

export default function QueryTable({ formData, isClicked, onReacted }: QueryTableProps) {
    const productManager = new ProductManager();
    const [showLoader, setShowLoader] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [data, setData] = useState<ProductQueryItem[]>([]);
    const [sortingMap, setSortingMap] = useState<Map<string, SortDirection>>(new Map());
    const [pagination, setPagination] = useState<Pagination>({
        totalPages: 0,
        totalRecords: 0n,
        pageIndex: 0,
        pageSize: 10
    });

    const getSortingConditions = (): Record<string, SortDirection> => {
        const conditions: Record<string, SortDirection> = {};
        sortingMap.forEach((direction, field) => {
            conditions[decamelize(field)] = direction;
        });
        return conditions;
    };

    const loadPage = async () => {
        try {
            const timer = setTimeout(() => setShowLoader(true), 70);

            const query = {
                ...formData,
                pageSize: pagination.pageSize,
                pageIndex: pagination.pageIndex,
                vendorIds: Array.from(formData.vendorIds || []),
                sortingConditions: getSortingConditions()
            };

            const result = await productManager.query(query);

            clearTimeout(timer);
            setPagination(result.pagination);
            setData(result.productData);
            setError(null);
        }
        catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to process query');
        }
        finally {
            setShowLoader(false);
        }
    };

    const handleSorting = (columnId: string) => {
        setSortingMap(prevMap => {
            const newMap = new Map(prevMap);

            if (newMap.has(columnId)) {
                const currentDirection = newMap.get(columnId);
                if (currentDirection === SortDirection.ASC) {
                    newMap.set(columnId, SortDirection.DESC);
                }
                else {
                    newMap.delete(columnId);
                }
            }
            else {
                newMap.set(columnId, SortDirection.ASC);
            }

            return newMap;
        });

        setPagination(prev => ({ ...prev, pageIndex: 0 }));
    };

    useEffect(() => {
        if (isClicked) {
            setPagination(prev => ({ ...prev, pageIndex: 0 }));
            setSortingMap(new Map());
            loadPage();
            onReacted();
        }
    }, [isClicked]);

    useEffect(() => {
        if (pagination.pageIndex > 0 || data.length > 0 || sortingMap.size > 0) {
            loadPage();
        }
    }, [pagination.pageIndex, sortingMap]);

    const table = useReactTable({
        data,
        columns,
        getCoreRowModel: getCoreRowModel(),
        manualPagination: true,
        manualSorting: true
    });

    const handlePagination = (pageIndex: number)=> {
        setPagination(prev => ({ ...prev, pageIndex }));
    };

    const handleHideColumns = () => {
        const hideableColumns = table.getAllColumns()
            .filter(column => column.getCanHide());

        if (hideableColumns.length === 0) return;

        const allHideableVisible = hideableColumns.every(column => column.getIsVisible());
        if (allHideableVisible) {
            hideableColumns.forEach(column => {
                column.toggleVisibility(false);
            });
        }
        else {
            hideableColumns.forEach(column => {
                column.toggleVisibility(true);
            });
        }
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

    if (data.length === 0) {
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
                            {headerGroup.headers.map(header => {
                                const direction = sortingMap.get(header.column.id) || null;

                                return (
                                    <th
                                        key={header.id}
                                        onClick={header.column.getCanSort() ? () => handleSorting(header.column.id) : undefined}
                                        className="sortable"
                                    >
                                        <div className="th-content">
                                            {flexRender(
                                                header.column.columnDef.header,
                                                header.getContext()
                                            )}
                                            {direction && (
                                                <span className="sort-indicator">
                                                    {direction === SortDirection.ASC ? ' ↑' : ' ↓'}
                                                </span>
                                            )}
                                        </div>
                                    </th>
                                );
                            })}
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

                {showLoader && data.length > 0 && (
                    <div className="loading-overlay">
                        <p>Загрузка...</p>
                    </div>
                )}
            </div>

            <BottomTable
                onConcealColumnsChange={handleHideColumns}
                pagination={pagination}
                onPaginationChange={handlePagination}
            />

        </div>
    );
}