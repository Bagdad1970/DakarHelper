import Modal from "./Modal.tsx";
import type VendorManager from "../../api/VendorManager.ts";
import type SubcategoryManager from "../../api/SubcategoryManager.ts";
import type HeaderCellManager from "../../api/HeaderCellManager.ts";

interface DeleteModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    selectedIds: bigint[];
    manager: VendorManager | SubcategoryManager | HeaderCellManager;
}

export function DeleteModal({ isOpen, onClose, onSuccess, selectedIds, manager }: DeleteModalProps) {
    const handleDelete = async () => {
        await Promise.all(selectedIds.map(id => manager.deleteById(id)));
        onSuccess();
        onClose();
    }

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Подтверждение удаления"
        >
            <div className="modal-content">
                <p>Вы уверены, что хотите удалить выбранные элементы?</p>
                <div className="modal-actions">
                    <button onClick={onClose} className="modal-button cancel">Отмена</button>
                    <button onClick={handleDelete} className="modal-button confirm delete">Удалить</button>
                </div>
            </div>
        </Modal>
    );

}