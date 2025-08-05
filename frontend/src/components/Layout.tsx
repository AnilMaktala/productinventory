import { ReactNode } from 'react';
import { Link, useLocation } from 'react-router-dom';
import './Layout.css';

interface LayoutProps {
  children: ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const location = useLocation();

  const isActive = (path: string) => {
    return (
      location.pathname === path || location.pathname.startsWith(path + '/')
    );
  };

  return (
    <div className="layout">
      <header className="header">
        <div className="header-content">
          <Link to="/" className="logo">
            <h1>Product Inventory</h1>
          </Link>
          <nav className="nav">
            <Link
              to="/"
              className={`nav-link ${isActive('/') && location.pathname === '/' ? 'active' : ''}`}
            >
              Dashboard
            </Link>
            <Link
              to="/products"
              className={`nav-link ${isActive('/products') ? 'active' : ''}`}
            >
              Products
            </Link>
            <Link
              to="/categories"
              className={`nav-link ${isActive('/categories') ? 'active' : ''}`}
            >
              Categories
            </Link>
          </nav>
        </div>
      </header>
      <main className="main-content">{children}</main>
    </div>
  );
};

export default Layout;
