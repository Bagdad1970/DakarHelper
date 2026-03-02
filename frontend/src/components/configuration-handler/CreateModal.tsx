import {ConfigurationUnit} from "../../types/ConfigurationUnit.ts";
import type VendorManager from "../../api/VendorManager.ts";
import type SubcategoryManager from "../../api/SubcategoryManager.ts";
import type HeaderCellManager from "../../api/HeaderCellManager.ts";
import {CreateSubcategoryModal} from "./create-modals/CreateSubcategoryModal.tsx";
import {CreateVendorModal} from "./create-modals/CreateVendorModal.tsx";
import {CreateHeaderCellModal} from "./create-modals/CreateHeaderCellModal.tsx";

interface CreateModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
    confUnit: ConfigurationUnit;
    manager: VendorManager | SubcategoryManager | HeaderCellManager;
}

export function CreateModal({ isOpen, onClose, onSuccess, confUnit, manager}: CreateModalProps ) {
    if (confUnit === ConfigurationUnit.VENDOR) {
        return (
            <CreateVendorModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                manager={manager as VendorManager}
            />
        );
    }
    else if (confUnit === ConfigurationUnit.SUBCATEGORY) {
        return (
            <CreateSubcategoryModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                manager={manager as SubcategoryManager}
            />
        );
    }
    else if (confUnit === ConfigurationUnit.HEADER_CELL) {
        return (
            <CreateHeaderCellModal
                isOpen={isOpen}
                onClose={onClose}
                onSuccess={onSuccess}
                manager={manager as HeaderCellManager}
            />
        );
    }
}