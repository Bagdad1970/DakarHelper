import { ConfigurationUnit } from "../../types/ConfigurationUnit.ts";
import { useEffect, useState } from "react";
import type { Vendor } from "../../types/vendor/Vendor.ts";
import "../../assets/styles/configuration-handler/ConfigurationUnitManager.css";
import type { Subcategory } from "../../types/subcategory/Subcategory.ts";
import type { HeaderCellWithSubcategory } from "../../types/headercell/HeaderCellWithSubcategory.ts";
import VendorManager from "../../api/VendorManager.ts";
import HeaderCellManager from "../../api/HeaderCellManager.ts";
import SubcategoryManager from "../../api/SubcategoryManager.ts";
import {EditModal} from "./EditModal.tsx";
import {CreateModal} from "./CreateModal.tsx";
import {DeleteModal} from "./DeleteModal.tsx";

interface ConfigurationUnitManagerProps {
    confUnit: ConfigurationUnit;
}

type ModalType = 'add' | 'update' | 'delete' | null;

export default function ConfigurationUnitManager({ confUnit }: ConfigurationUnitManagerProps) {
    const [confUnitGroup, setConfUnitGroup] = useState<Vendor[] | HeaderCellWithSubcategory[] | Subcategory[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [selectedItems, setSelectedItems] = useState<Set<bigint>>(new Set());
    const [modalType, setModalType] = useState<ModalType>(null);

    const manager = new Map<ConfigurationUnit, VendorManager | SubcategoryManager | HeaderCellManager>([
        [ConfigurationUnit.VENDOR, new VendorManager()],
        [ConfigurationUnit.SUBCATEGORY, new SubcategoryManager()],
        [ConfigurationUnit.HEADER_CELL, new HeaderCellManager()]
    ]);

    useEffect(() => {
        const loadData = async () => {
            try {
                setLoading(true);
                const currentManager = manager.get(confUnit);
                const data: Vendor[] | Subcategory[] | HeaderCell[] = await currentManager.findAll() ?? [];

                setConfUnitGroup(data);
                setError(null);
            }
            catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load data');
            }
            finally {
                setLoading(false);
            }
        };
        setSelectedItems(new Set());

        loadData()
    }, [confUnit]);

    const reloadData = async () => {
        try {
            setLoading(true);
            const currentManager = manager.get(confUnit);
            const data = await currentManager.findAll() ?? [];
            setConfUnitGroup(data);
            setError(null);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Failed to load data');
        } finally {
            setLoading(false);
        }
    };

    const handleSelectItem = (id: bigint) => {
        setSelectedItems(prev => {
            const newSet = new Set(prev);
            if (newSet.has(id)) {
                newSet.delete(id);
            }
            else {
                newSet.add(id);
            }
            return newSet;
        });
    };

    const handleAction = (action: 'add' | 'update' | 'delete') => {
        setModalType(action);
    };

    const handleSelectAll = () => {
        if (selectedItems.size === confUnitGroup.length) {
            setSelectedItems(new Set());
        }
        else {
            setSelectedItems(new Set(confUnitGroup.map(item => item.id)));
        }
    };

    const closeModal = () => {
        setModalType(null);
        if (modalType === 'update') {
            setSelectedItems(new Set());
        }
    };

    const renderModal = () => {
        if (!modalType) return null;

        if (modalType === 'add') {
            return (
                <CreateModal
                    isOpen={true}
                    onClose={closeModal}
                    onSuccess={reloadData}
                    confUnit={confUnit}
                    manager={manager.get(confUnit)}
                />
            );
        }

        if (modalType === 'update') {
            return (
                <EditModal
                    isOpen={true}
                    onClose={closeModal}
                    confUnit={confUnit}
                    onSuccess={reloadData}
                    selectedId={Array.from(selectedItems)[0]}
                    manager={manager.get(confUnit)}
                />
            );
        }

        if (modalType === 'delete') {
            return (
                <DeleteModal
                    isOpen={true}
                    onClose={closeModal}
                    onSuccess={reloadData}
                    selectedIds={Array.from(selectedItems)}
                    manager={manager.get(confUnit)}
                />
            );
        }
    };

    const isUpdateDisabled = selectedItems.size !== 1;
    const isDeleteDisabled = selectedItems.size === 0;

    const renderTable = () => {
        if (confUnit === ConfigurationUnit.VENDOR) {
            const vendors = confUnitGroup as Vendor[];
            return (
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th className="checkbox-cell">
                                <input
                                    type="checkbox"
                                    checked={selectedItems.size === vendors.length && vendors.length > 0}
                                    onChange={handleSelectAll}
                                    className="item-checkbox"
                                />
                            </th>
                            <th>Поставщик</th>
                        </tr>
                        </thead>
                        <tbody>
                        {vendors.map(vendor => (
                            <tr
                                key={vendor.id.toString()}
                                className={selectedItems.has(vendor.id) ? 'selected' : ''}
                            >
                                <td className="checkbox-cell">
                                    <input
                                        type="checkbox"
                                        checked={selectedItems.has(vendor.id)}
                                        onChange={() => handleSelectItem(vendor.id)}
                                        className="item-checkbox"
                                    />
                                </td>
                                <td>{vendor.title}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            );
        }

        if (confUnit === ConfigurationUnit.SUBCATEGORY) {
            const subcategories = confUnitGroup as Subcategory[];
            return (
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th className="checkbox-cell">
                                <input
                                    type="checkbox"
                                    checked={selectedItems.size === subcategories.length && subcategories.length > 0}
                                    onChange={handleSelectAll}
                                    className="item-checkbox"
                                />
                            </th>
                            <th>Название</th>
                            <th>Категория</th>
                        </tr>
                        </thead>
                        <tbody>
                        {subcategories.map(subcategory => (
                            <tr
                                key={subcategory.id.toString()}
                                className={selectedItems.has(subcategory.id) ? 'selected' : ''}
                            >
                                <td className="checkbox-cell">
                                    <input
                                        type="checkbox"
                                        checked={selectedItems.has(subcategory.id)}
                                        onChange={() => handleSelectItem(subcategory.id)}
                                        className="item-checkbox"
                                    />
                                </td>
                                <td>{subcategory.name}</td>
                                <td>
                                    <span className={`category-badge category-${subcategory.category}`}>
                                        {subcategory.category}
                                    </span>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            );
        }

        if (confUnit === ConfigurationUnit.HEADER_CELL) {
            const headerCells = confUnitGroup as HeaderCellWithSubcategory[];
            return (
                <div className="table-container">
                    <table className="data-table">
                        <thead>
                        <tr>
                            <th className="checkbox-cell">
                                <input
                                    type="checkbox"
                                    checked={selectedItems.size === headerCells.length && headerCells.length > 0}
                                    onChange={handleSelectAll}
                                    className="item-checkbox"
                                />
                            </th>
                            <th>Оригинальное название</th>
                            <th>Подкатегория</th>
                            <th>Категория</th>
                            <th>Статус</th>
                        </tr>
                        </thead>
                        <tbody>
                        {headerCells.map(cell => (
                            <tr
                                key={cell.id.toString()}
                                className={selectedItems.has(cell.id) ? 'selected' : ''}
                            >
                                <td className="checkbox-cell">
                                    <input
                                        type="checkbox"
                                        checked={selectedItems.has(cell.id)}
                                        onChange={() => handleSelectItem(cell.id)}
                                        className="item-checkbox"
                                    />
                                </td>
                                <td>{cell.originalName}</td>
                                <td>
                                    {cell.subcategoryName ? cell.subcategoryName : '—'}
                                </td>
                                <td>
                                    <span className={`category-badge category-${cell.category}`}>
                                        {cell.category}
                                    </span>
                                </td>
                                <td>
                                    <span className={`status-badge ${cell.isProcessing ? 'status-processed' : 'status-ignored'}`}>
                                        {cell.isProcessing ? 'Обрабатывается' : 'Игнорируется'}
                                    </span>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            );
        }

        return null;
    };

    if (loading) {
        return (
            <div className="configuration-unit-manager">
                <div className="configuration-unit-manager-loading">
                    <p>Загрузка...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="configuration-unit-manager">
                <div className="configuration-unit-manager-error">
                    <p>Ошибка: {error}</p>
                    <button onClick={() => window.location.reload()}>
                        Повторить
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="configuration-unit-manager">
            <div className="table-header">
                <div className="action-buttons">
                    <button
                        className="action-button add-button"
                        onClick={() => handleAction('add')}
                        title="Добавить"
                    >
                        Добавить
                    </button>
                    <button
                        className={`action-button update-button ${isUpdateDisabled ? 'disabled' : ''}`}
                        onClick={() => handleAction('update')}
                        disabled={isUpdateDisabled}
                        title={isUpdateDisabled ? 'Выберите один элемент для редактирования' : 'Редактировать'}
                    >
                        Обновить
                    </button>
                    <button
                        className={`action-button delete-button ${isDeleteDisabled ? 'disabled' : ''}`}
                        onClick={() => handleAction('delete')}
                        disabled={isDeleteDisabled}
                        title={isDeleteDisabled ? 'Выберите элементы для удаления' : 'Удалить'}
                    >
                        Удалить
                    </button>
                </div>
            </div>

            {renderTable()}
            {renderModal()}
        </div>
    );
}