import React from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';

const ProductForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Form submission logic will be implemented in future tasks
    console.log(isEdit ? 'Update product:' : 'Create product:', id);
    navigate('/products');
  };

  const handleCancel = () => {
    navigate(-1); // Go back to previous page
  };

  return (
    <div className="product-form">
      <div className="page-header">
        <div className="breadcrumb">
          <Link to="/products">Products</Link>
          <span> / </span>
          {isEdit ? (
            <>
              <Link to={`/products/${id}`}>Product Details</Link>
              <span> / </span>
              <span>Edit</span>
            </>
          ) : (
            <span>New Product</span>
          )}
        </div>
      </div>

      <div className="form-container">
        <h1>{isEdit ? 'Edit Product' : 'Create New Product'}</h1>

        <form onSubmit={handleSubmit} className="product-form-fields">
          <div className="form-group">
            <label htmlFor="name">Product Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              required
              placeholder="Enter product name"
            />
          </div>

          <div className="form-group">
            <label htmlFor="sku">SKU *</label>
            <input
              type="text"
              id="sku"
              name="sku"
              required
              placeholder="Enter product SKU"
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              rows={4}
              placeholder="Enter product description"
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="price">Price *</label>
              <input
                type="number"
                id="price"
                name="price"
                required
                min="0"
                step="0.01"
                placeholder="0.00"
              />
            </div>

            <div className="form-group">
              <label htmlFor="category">Category</label>
              <select id="category" name="category">
                <option value="">Select a category</option>
                {/* Categories will be loaded in future tasks */}
              </select>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="inventoryQuantity">Initial Inventory</label>
              <input
                type="number"
                id="inventoryQuantity"
                name="inventoryQuantity"
                min="0"
                defaultValue="0"
              />
            </div>

            <div className="form-group">
              <label htmlFor="lowStockThreshold">Low Stock Threshold</label>
              <input
                type="number"
                id="lowStockThreshold"
                name="lowStockThreshold"
                min="0"
                defaultValue="5"
              />
            </div>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn-secondary"
              onClick={handleCancel}
            >
              Cancel
            </button>
            <button type="submit" className="btn-primary">
              {isEdit ? 'Update Product' : 'Create Product'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProductForm;
