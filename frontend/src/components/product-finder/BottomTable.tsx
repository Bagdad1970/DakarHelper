import {PaginationView} from "./PaginationView.tsx";
import type {Pagination} from "../../types/product/Pagination.ts";
import {ConcealButton} from "./ConcealButton.tsx";
import '../../assets/styles/product-finder/BottomTable.css'

interface BottomTableProps {
    onConcealColumnsChange: () => void;
    pagination: Pagination;
    onPaginationChange: (pageIndex: number) => void;
}

export const BottomTable = ({onConcealColumnsChange, pagination, onPaginationChange}: BottomTableProps) => {

    return (
        <div className="bottom-table">
            <ConcealButton
                onConcealClick={onConcealColumnsChange}
            />

            {pagination.totalPages > 1 && (
                <PaginationView
                    pagination={pagination}
                    onPageChange={onPaginationChange}
                />
            )}
        </div>
    );

}