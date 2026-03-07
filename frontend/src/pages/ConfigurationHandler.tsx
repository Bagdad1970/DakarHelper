import PageNav from "../components/PageNav.tsx";
import { ConfigurationUnit } from "../types/ConfigurationUnit.ts";
import { useState } from "react";
import "../assets/styles/configuration-handler/ConfigurationHandler.css";
import ConfigurationUnitManager from "../components/configuration-handler/ConfigurationUnitManager.tsx";
import {SynchronizeButton} from "../components/configuration-handler/SynchronizeButton.tsx";

export default function ConfigurationHandler() {

    const [confUnit, setConfUnit] = useState<ConfigurationUnit>(ConfigurationUnit.VENDOR);

    const handleConfUnitSelection = (configurationUnit: ConfigurationUnit) => {
        setConfUnit(configurationUnit);
    }

    return (
        <div className="configuration-handler">
            <PageNav />

            <div className="configuration-handler-container">
                <div className="configuration-unit-button-list">
                    <button
                        className={`configuration-unit-button-item ${confUnit === ConfigurationUnit.VENDOR ? 'active' : ''}`}
                        onClick={() => handleConfUnitSelection(ConfigurationUnit.VENDOR)}
                    >
                        Поставщики
                    </button>
                    <button
                        className={`configuration-unit-button-item ${confUnit === ConfigurationUnit.SUBCATEGORY ? 'active' : ''}`}
                        onClick={() => handleConfUnitSelection(ConfigurationUnit.SUBCATEGORY)}
                    >
                        Подкатегории
                    </button>
                    <button
                        className={`configuration-unit-button-item ${confUnit === ConfigurationUnit.HEADER_CELL ? 'active' : ''}`}
                        onClick={() => handleConfUnitSelection(ConfigurationUnit.HEADER_CELL)}
                    >
                        Заголовочные ячейки
                    </button>
                </div>

                <ConfigurationUnitManager confUnit={confUnit} />

                <SynchronizeButton />
            </div>
        </div>
    );
}