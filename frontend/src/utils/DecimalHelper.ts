import Decimal from "decimal.js";

export const formatPrice = (decimal: Decimal): string => {
    const rounded = decimal.toFixed(2);
    const [integerPart, decimalPart] = rounded.split('.');
    const localizedIntegerPart = parseInt(integerPart).toLocaleString('ru-RU');
    return localizedIntegerPart + "," + decimalPart;
}