import {ConfigurationUnit} from "../../types/ConfigurationUnit.ts";
import type VendorManager from "../../api/VendorManager.ts";
import type SubcategoryManager from "../../api/SubcategoryManager.ts";
import type HeaderCellManager from "../../api/HeaderCellManager.ts";
import {EditVendorModal} from "./edit-modals/EditVendorModal.tsx";
import {EditHeaderCellModal} from "./edit-modals/EditHeaderCellModal.tsx";
import {EditSubcategoryModal} from "./edit-modals/EditSubcategoryModal.tsx";

interface EditModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    confUnit: ConfigurationUnit;
    selectedId: bigint;
    manager: VendorManager | SubcategoryManager | HeaderCellManager;
}

export function EditModal({ isOpen, onClose, onSuccess, confUnit, selectedId, manager }: EditModalProps) {

    if (confUnit === ConfigurationUnit.VENDOR) {
        return (
            <EditVendorModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                selectedId={selectedId}
                manager={manager as VendorManager}
            />
        );
    }
    else if (confUnit === ConfigurationUnit.SUBCATEGORY) {
        return (
            <EditSubcategoryModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                selectedId={selectedId}
                manager={manager as SubcategoryManager}
            />
        );
    }
    else if (confUnit === ConfigurationUnit.HEADER_CELL) {
        return (
            <EditHeaderCellModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                selectedId={selectedId}
                manager={manager as HeaderCellManager}
            />
        );
    }
}