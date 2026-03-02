import type VendorManager from "../../../api/VendorManager.ts";
import {useState} from "react";
import Modal from "../Modal.tsx";
import type {VendorCreateRequest} from "../../../types/vendor/VendorCreateRequest.ts";

interface CreateVendorModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    manager: VendorManager;
}

export function CreateVendorModal({ isOpen, onClose, onSuccess, manager }: CreateVendorModalProps) {
    const [formData, setFormData] = useState<VendorCreateRequest>({
        title: ""
    });

    const handleInputChange = (field: string, value: any) => {
        setFormData((prev: VendorCreateRequest) => ({
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
            title="Добавление поставщика"
        >
            <div className="modal-content">
                <div className="form-fields">
                    <div className="form-field">
                        <label>Название поставщика:</label>
                        <input
                            type="text"
                            value={formData.title}
                            onChange={(e) => handleInputChange('title', e.target.value)}
                            placeholder="Название"
                        />
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