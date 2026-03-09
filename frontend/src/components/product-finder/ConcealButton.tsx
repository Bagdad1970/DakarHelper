import '../../assets/styles/product-finder/ConcealButton.css';
import openedEye from '../../assets/images/opened-eye.png';
import closedEye from '../../assets/images/closed-eye.png';
import { useState } from 'react';

interface ConcealButtonProps {
    onConcealClick: () => void;
}

export function ConcealButton({ onConcealClick }: ConcealButtonProps) {
    const [isOpen, setIsOpen] = useState(true);

    const handleClick = () => {
        setIsOpen(!isOpen);
        onConcealClick();
    };

    return (
        <button
            className="conceal-columns-button"
            title={isOpen ? "Скрыть колонки" : "Показать колонки"}
            onClick={handleClick}
        >
            <img
                src={isOpen ? openedEye : closedEye}
                alt={isOpen ? "Скрыть колонки" : "Показать колонки"}
                className="conceal-columns-button-image"
            />
        </button>
    );
}