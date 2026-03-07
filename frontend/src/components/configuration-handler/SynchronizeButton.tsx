import "../../assets/styles/configuration-handler/SynchronizeButton.css"
import {useState} from "react";
import ExcelParserManager from "../../api/ExcelParserManager.ts";

export const SynchronizeButton = () => {
    const [isClicked, setIsClicked] = useState(false);

    const excelParserManager = new ExcelParserManager();

    const handleSynchronize = () => {
        setIsClicked(true);
        excelParserManager.parse()
            .then(() => setIsClicked(false));
    }

    return (
        <div className="synchronize-button-container">
            <button className="synchronize-button"
                    onClick={handleSynchronize}
                    disabled={isClicked}
            >
                Синхронизировать файлы
            </button>
        </div>
    );
};