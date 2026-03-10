import Modal from "../Modal.tsx";
import {useState} from "react";
import {Category} from "../../../types/Category.ts";
import type SubcategoryManager from "../../../api/SubcategoryManager.ts";
import type {SubcategoryCreateRequest} from "../../../types/subcategory/SubcategoryCreateRequest.ts";

interface CreateSubcategoryModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    manager: SubcategoryManager;
}

export function CreateSubcategoryModal({ isOpen, onClose, onSuccess, manager }: CreateSubcategoryModalProps) {
    const [formData, setFormData] = useState<SubcategoryCreateRequest>({
        name: "",
        category: Category.NAME
    });

    const handleInputChange = (field: string, value: any) => {
        setFormData((prev: SubcategoryCreateRequest) => ({
            ...prev,
            [field]: value
        }));
    };

    const handleCreate = async () => {
        await manager.create(formData);
        onClose();
        onSuccess();
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Добавление подкатегории"
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
                            {Object.keys(Category).map(category => (
                                <option key={category} value={category}>
                                    {category}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="modal-actions">
                    <button onClick={onClose} className="modal-button cancel">Отмена</button>
                    <button onClick={handleCreate} className="modal-button confirm">
                        Добавить
                    </button>
                </div>
            </div>
        </Modal>
    );
}