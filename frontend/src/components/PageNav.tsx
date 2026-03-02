import { NavLink } from 'react-router-dom';
import '../assets/styles/PageNav.css';

export default function PageNav() {
    return (
        <div className="page-nav">
            <div className="logo-nav">
                <img src="src/assets/images/logo.png" alt="Dakar Helper logo" />
            </div>
            <nav>
                <NavLink
                    to="/"
                    className={({ isActive }) => isActive ? 'active' : ''}
                    end
                >
                    Поиск
                </NavLink>
                <NavLink
                    to="/rules"
                    className={({ isActive }) => isActive ? 'active' : ''}
                >
                    Правила обработки
                </NavLink>
                <NavLink
                    to="/profile"
                    className={({ isActive }) => isActive ? 'active' : ''}
                >
                    Профиль
                </NavLink>
            </nav>
        </div>
    );
}