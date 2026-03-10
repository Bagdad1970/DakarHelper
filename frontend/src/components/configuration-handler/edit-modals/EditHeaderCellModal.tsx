import Modal from "../Modal.tsx";
import {useEffect, useState} from "react";
import {Category} from "../../../types/Category.ts";
import type HeaderCellManager from "../../../api/HeaderCellManager.ts";
import type {HeaderCellUpdateRequest} from "../../../types/headercell/HeaderCellUpdateRequest.ts";
import type {Subcategory} from "../../../types/subcategory/Subcategory.ts";
import SubcategoryManager from "../../../api/SubcategoryManager.ts";

interface EditHeaderCellModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    selectedId: bigint;
    manager: HeaderCellManager;
}

export function EditHeaderCellModal({ isOpen, onClose, onSuccess, selectedId, manager }: EditHeaderCellModalProps) {
    const [formData, setFormData] = useState<HeaderCellUpdateRequest | null>({
        id: selectedId,
        subcategoryId: null,
        originalName: "",
        category: null,
        isProcessing: true,
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [subcategories, setSubcategories] = useState<Subcategory[]>([]);
    const subcategoryManager = new SubcategoryManager();

    useEffect(() => {
        const fetchHeaderCell = async () => {
            if (!selectedId || !isOpen) return;

            try {
                setLoading(true);
                const headerCell = await manager.findById(selectedId);
                setFormData({
                    ...headerCell,
                    category: null
                });
                setError(null);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load HeaderCell');
            } finally {
                setLoading(false);
            }
        };

        fetchHeaderCell();
        console.log(formData);
    }, [])

    useEffect(() => {
        const loadSubcategories = async () => {
            try {
                setLoading(true);
                const data = await subcategoryManager.findAll() ?? [];

                setSubcategories(data);
                setError(null);
            }
            catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load data');
            }
            finally {
                setLoading(false);
            }
        };

        loadSubcategories()
    }, []);

    const handleInputChange = (field: string, value) => {
        if (formData) {
            setFormData({
                ...formData,
                [field]: value
            });
        }
    };

    const handleUpdate = async () => {
        if (formData) {
            await manager.update(formData);
            onClose();
            onSuccess();
        }
    }

    if (!isOpen) return null;

    if (loading) return <div>Loading...</div>;

    if (error) return <div>Error: {error}</div>;

    if (!formData) return <div>No data</div>;

    return (

        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Редактирование ячейки заголовка"
        >
            <div className="modal-content">
                <div className="form-fields">
                    <div className="form-field">
                        <label>Оригинальное название:</label>
                        <input
                            type="text"
                            value={formData.originalName ?? ''}
                            onChange={(e) => handleInputChange('originalName', e.target.value)}
                            placeholder="Например: розница"
                        />
                    </div>
                    <div className="form-field">
                        <label>Подкатегория:</label>
                        <select
                            value={formData.subcategoryId ?? ""}
                            onChange={(e) => {
                                handleInputChange('subcategoryId', e.target.value || null);
                            }}
                        >
                            <option value="">Без подкатегории</option>
                            {subcategories.map(subcategory => (
                                <option key={subcategory.id} value={subcategory.id.toString()}>
                                    {subcategory.name} ({Category[subcategory.category]})
                                </option>
                            ))}
                        </select>
                    </div>

                    {!formData.subcategoryId &&
                        <div className="form-field">
                            <label>Категория:</label>
                            <select
                                value={formData.category ?? ""}
                                onChange={(e) => {
                                    handleInputChange('category', e.target.value || null);
                                }}
                            >
                                <option value="">Не выбрано</option>
                                {Object.keys(Category)
                                    .map(category => (
                                        <option key={category} value={category}>
                                            {category}
                                        </option>
                                    ))
                                }
                            </select>
                        </div>
                    }

                    <div className="form-field checkbox">
                        <label>
                            <input
                                type="checkbox"
                                checked={formData?.isProcessing || false}
                                onChange={(e) => handleInputChange('isProcessing', e.target.checked)}
                            />
                            Обрабатывается
                        </label>
                    </div>
                </div>

                <div className="modal-actions">
                    <button onClick={onClose} className="modal-button cancel">Отмена</button>
                    <button onClick={handleUpdate} className="modal-button confirm">
                        Сохранить
                    </button>
                </div>
            </div>
        </Modal>
    );
}