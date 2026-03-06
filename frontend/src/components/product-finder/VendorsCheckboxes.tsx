import {type ChangeEvent, useEffect, useState} from "react";
import "../../assets/styles/product-finder/VendorsCheckboxes.css";
import type {Vendor} from "../../types/vendor/Vendor.ts";
import VendorManager from "../../api/VendorManager.ts";

interface VendorsCheckboxesProps {
    onCheckboxChange: (vendorId: bigint, checked: boolean) => void;
    selectedIds: Set<bigint>
}

export function VendorsCheckboxes({ onCheckboxChange, selectedIds }: VendorsCheckboxesProps) {
    const [vendors, setVendors] = useState<Vendor[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const vendorManager = new VendorManager();

    useEffect(() => {
        const loadVendors = async () => {
            try {
                setLoading(true);
                const data = await vendorManager.findAll();
                setVendors(data);
                setError(null);
            }
            catch (err) {
                setError(err instanceof Error ? err.message : 'Failed to load vendors');
            }
            finally {
                setLoading(false);
            }
        };

        loadVendors();
    }, []);

    const handleSelectAll = (e: ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        if (selectedIds.size == vendors.length) {
            vendors.forEach(vendor => {
                onCheckboxChange(vendor.id, false);
            });
        }
        else {
            vendors.forEach(vendor => {
                onCheckboxChange(vendor.id, true);
            });
        }
    }

    if (loading) {
        return (
            <div className="data-loading">
                <div className="spinner"></div>
                <p>Loading vendors...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="data-error">
                <p>Error: {error}</p>
                <button onClick={() => window.location.reload()}>
                    Retry
                </button>
            </div>
        );
    }

    return (
        <div className="vendors-checkboxes">
            <div className="vendors-header">
                <h4>Поставщики</h4>
            </div>

            {vendors.length === 0 && (
                <div className="data-empty">
                    <p>No vendors found</p>
                </div>
            )}

            {vendors.length !== 0 && (
                <div className="vendors-list">
                    {vendors.map(vendor => (
                        <div key={vendor.id} className="vendor-item">
                            <input
                                type="checkbox"
                                checked={selectedIds.has(vendor.id)}
                                onChange={(e) => onCheckboxChange(vendor.id, e.target.checked)}
                                className="vendor-checkbox"
                            />
                            <label
                                htmlFor={`vendor-${vendor.id}`}
                                className="vendor-label"
                            >
                                {vendor.title}
                            </label>
                        </div>
                    ))}
                </div>
            )}

            <div className="select-all-vendors">
                <button className="select-all-vendors-button" onClick={handleSelectAll}>Выбрать все</button>
            </div>
        </div>
    );

}