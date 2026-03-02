import Modal from "../Modal.tsx";
import type VendorManager from "../../../api/VendorManager.ts";
import {useEffect, useState} from "react";
import type {VendorUpdateRequest} from "../../../types/vendor/VendorUpdateRequest.ts";

interface EditVendorModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    selectedId: bigint;
    manager: VendorManager;
}

export function EditVendorModal({ isOpen, onClose, onSuccess, selectedId, manager }: EditVendorModalProps) {
    const [formData, setFormData] = useState<VendorUpdateRequest>({
        id: selectedId,
        title: ""
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchVendor = async () => {
            if (!selectedId || !isOpen) return;

            try {
                setLoading(true);
                const vendor = await manager.findById(selectedId);
                setFormData(vendor);
                setError(null);
            }
            catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load vendor');
            }
            finally {
                setLoading(false);
            }
        };

        fetchVendor();
    }, [selectedId, isOpen, manager]);

    const handleInputChange = (field: string, value: any) => {
        setFormData({
            ...formData,
            [field]: value
        });
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
            title="Редактирование поставщика"
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
                    <button onClick={handleUpdate} className="modal-button confirm">
                        Сохранить
                    </button>
                </div>
            </div>
        </Modal>
    );
}