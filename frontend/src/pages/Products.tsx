import React from 'react';
import { Link } from 'react-router-dom';

const Products: React.FC = () => {
  return (
    <div className="products fade-in">
      <div className="page-header slide-in-left">
        <h1>Products</h1>
        <Link to="/products/new" className="btn btn-primary hover-scale">
          <span>+</span>
          Add Product
        </Link>
      </div>
      <div className="products-content">
        <div className="search-filters slide-in-right">
          <div className="search-input">
            <input
              type="text"
              placeholder="Search products..."
              className="form-input"
            />
          </div>
          <select
            className="form-select filter-select"
            aria-label="Filter by category"
          >
            <option value="">All Categories</option>
            <option value="electronics">Electronics</option>
            <option value="clothing">Clothing</option>
            <option value="books">Books</option>
          </select>
        </div>
        <div className="products-list">
          <div className="alert alert-info scale-in">
            <strong>Coming Soon:</strong> Product list will be implemented in
            future tasks.
          </div>
          <div className="sample-product-links">
            <p>Sample navigation (will be replaced with actual data):</p>
            <div className="stagger-children">
              <Link to="/products/1" className="product-link hover-lift">
                📱 Sample Product 1 - iPhone 15 Pro
                <span className="badge badge-success">In Stock</span>
              </Link>
              <Link to="/products/2" className="product-link hover-lift">
                💻 Sample Product 2 - MacBook Air M2
                <span className="badge badge-warning">Low Stock</span>
              </Link>
              <Link to="/products/3" className="product-link hover-lift">
                🎧 Sample Product 3 - AirPods Pro
                <span className="badge badge-error">Out of Stock</span>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Products;
