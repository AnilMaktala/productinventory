import React from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const handleEdit = () => {
    navigate(`/products/${id}/edit`);
  };

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      // Delete logic will be implemented in future tasks
      console.log('Delete product:', id);
      navigate('/products');
    }
  };

  return (
    <div className="product-detail">
      <div className="page-header">
        <div className="breadcrumb">
          <Link to="/products">Products</Link>
          <span> / </span>
          <span>Product Details</span>
        </div>
        <div className="actions">
          <button type="button" className="btn-secondary" onClick={handleEdit}>
            Edit Product
          </button>
          <button type="button" className="btn-danger" onClick={handleDelete}>
            Delete Product
          </button>
        </div>
      </div>

      <div className="product-detail-content">
        <div className="product-info">
          <h1>Product Details</h1>
          <p>Product ID: {id}</p>
          <p>Product details will be loaded and displayed in future tasks.</p>
        </div>

        <div className="inventory-section">
          <h2>Inventory Management</h2>
          <div className="inventory-controls">
            <button type="button" className="btn-secondary">
              -
            </button>
            <span className="inventory-count">Loading...</span>
            <button type="button" className="btn-secondary">
              +
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
