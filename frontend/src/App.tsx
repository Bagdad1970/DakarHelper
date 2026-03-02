import React from 'react';
import ConfigurationHandler from './pages/ConfigurationHandler.tsx';
import './assets/styles/App.css';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import ProductFinder from "./pages/ProductFinder.tsx";

const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<ProductFinder />} />
                <Route path="/rules" element={<ConfigurationHandler />} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;