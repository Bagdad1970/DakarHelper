import Modal from "../Modal.tsx";
import {useEffect, useState} from "react";
import type {Subcategory} from "../../../types/subcategory/Subcategory.ts";
import {Category} from "../../../types/Category.ts";
import type SubcategoryManager from "../../../api/SubcategoryManager.ts";

interface EditSubcategoryModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    selectedId: bigint;
    manager: SubcategoryManager;
}

export function EditSubcategoryModal({ isOpen, onClose, onSuccess, selectedId, manager }: EditSubcategoryModalProps) {
    const [formData, setFormData] = useState<Subcategory | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchSubcategory = async () => {
            if (!selectedId || !isOpen) return;

            try {
                setLoading(true);
                const subcategory = await manager.findById(selectedId);
                setFormData(subcategory);
                setError(null);
            } catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load subcategory');
            } finally {
                setLoading(false);
            }
        };

        fetchSubcategory();
    }, [selectedId, isOpen, manager]);

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
            title="Редактирование подкатегории"
        >
            <div className="modal-content">
                <div className="form-fields">
                    <div className="form-field">
                        <label>Название:</label>
                        <input
                            type="text"
                            value={formData?.name || ''}
                            onChange={(e) => handleInputChange('name', e.target.value)}
                            placeholder="Введите название"
                        />
                    </div>
                    <div className="form-field">
                        <label>Категория:</label>
                        <select
                            value={formData.category}
                            onChange={(e) => handleInputChange('category', e.target.value)}
                        >
                            {Object.values(Category).map(cat => (
                                <option key={cat} value={cat}>{cat}</option>
                            ))}
                        </select>
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