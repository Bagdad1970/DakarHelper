import type {Pagination} from "../../types/product/Pagination";
import "../../assets/styles/product-finder/PaginationView.css";

interface PaginationProps {
    pagination: Pagination,
    onPageChange: (pageIndex: number) => void;
}

export function PaginationView({pagination, onPageChange}: PaginationProps) {

    const canClickPrevPage = pagination ? pagination.pageIndex > 0 : false;
    const canClickNextPage = pagination ? pagination.pageIndex < pagination.totalPages - 1 : false;

    const handlePrevPageClick = () => {
        if (pagination.pageIndex > 0) {
            onPageChange(pagination.pageIndex - 1);
        }
    }

    const handleNextPageClick = () => {
        if (pagination.pageIndex < pagination.totalPages - 1) {
            onPageChange(pagination.pageIndex + 1);
        }
    }

    const handleFirstPageClick = () => {
        if (pagination.pageIndex > 0) {
            onPageChange(0);
        }
    }

    const handleLastPageClick = () => {
        if (pagination.pageIndex < pagination.totalPages - 1) {
            onPageChange(pagination.totalPages - 1);
        }
    };

    return (

        <div className="pagination">
            <button
                onClick={handleFirstPageClick}
                disabled={!canClickPrevPage}
                title="Первая страница"
            >
                {'<<'}
            </button>
            <button
                onClick={handlePrevPageClick}
                disabled={!canClickPrevPage}
                title="Предыдущая страница"
            >
                {'<'}
            </button>

            <span className="pagination-info">
                Страница {pagination.pageIndex + 1} из {pagination.totalPages}
            </span>

            <button
                onClick={handleNextPageClick}
                disabled={!canClickNextPage}
                title="Следующая страница"
            >
                {'>'}
            </button>
            <button
                onClick={handleLastPageClick}
                disabled={!canClickNextPage}
                title="Последняя страница"
            >
                {'>>'}
            </button>
        </div>

    );

}