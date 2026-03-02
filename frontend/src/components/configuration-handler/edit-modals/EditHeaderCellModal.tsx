import Modal from "../Modal.tsx";
import {useEffect, useState} from "react";
import {Category} from "../../../types/Category.ts";
import type {HeaderCell} from "../../../types/headercell/HeaderCell.ts";
import type HeaderCellManager from "../../../api/HeaderCellManager.ts";

interface EditHeaderCellModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    selectedId: bigint;
    manager: HeaderCellManager;
}

export function EditHeaderCellModal({ isOpen, onClose, onSuccess, selectedId, manager }: EditHeaderCellModalProps) {
    const [formData, setFormData] = useState<HeaderCell | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchHeaderCell = async () => {
            if (!selectedId || !isOpen) return;

            try {
                setLoading(true);
                const headerCell = await manager.findById(selectedId);
                setFormData(headerCell);
                setError(null);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load HeaderCell');
            } finally {
                setLoading(false);
            }
        };

        fetchHeaderCell();
    }, [selectedId, isOpen, manager])

    const handleInputChange = (field: string, value: any) => {
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
                            value={formData.originalName || ''}
                            onChange={(e) => handleInputChange('originalName', e.target.value)}
                            placeholder="Введите название"
                        />
                    </div>
                    <div className="form-field">
                        <label>ID подкатегории:</label>
                        <input
                            type="number"
                            value={formData.subcategoryId ?? ""}
                            onChange={(e) => handleInputChange('subcategoryId', e.target.value ? BigInt(e.target.value) : null)}
                            placeholder="Введите ID подкатегории"
                        />
                    </div>
                    <div className="form-field">
                        <label>Категория:</label>
                        <select
                            value={formData?.category || Category.NAME}
                            onChange={(e) => handleInputChange('category', e.target.value)}
                        >
                            {Object.values(Category).map(cat => (
                                <option key={cat} value={cat}>{cat}</option>
                            ))}
                        </select>
                    </div>
                    <div className="form-field checkbox">
                        <label>
                            <input
                                type="checkbox"
                                checked={formData?.isProcessing || false}
                                onChange={(e) => handleInputChange('isProcessed', e.target.checked)}
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