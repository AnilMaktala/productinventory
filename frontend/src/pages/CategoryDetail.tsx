import React from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';

const CategoryDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const handleEdit = () => {
    navigate(`/categories/${id}/edit`);
  };

  const handleDelete = () => {
    if (
      window.confirm(
        'Are you sure you want to delete this category? This will also affect associated products.'
      )
    ) {
      // Delete logic will be implemented in future tasks
      console.log('Delete category:', id);
      navigate('/categories');
    }
  };

  return (
    <div className="category-detail">
      <div className="page-header">
        <div className="breadcrumb">
          <Link to="/categories">Categories</Link>
          <span> / </span>
          <span>Category Details</span>
        </div>
        <div className="actions">
          <button type="button" className="btn-secondary" onClick={handleEdit}>
            Edit Category
          </button>
          <button type="button" className="btn-danger" onClick={handleDelete}>
            Delete Category
          </button>
        </div>
      </div>

      <div className="category-detail-content">
        <div className="category-info">
          <h1>Category Details</h1>
          <p>Category ID: {id}</p>
          <p>Category details will be loaded and displayed in future tasks.</p>
        </div>

        <div className="products-in-category">
          <h2>Products in this Category</h2>
          <div className="products-grid">
            <p>Products in this category will be displayed in future tasks.</p>
          </div>
          <Link to={`/products/new?category=${id}`} className="btn-primary">
            Add Product to Category
          </Link>
        </div>
      </div>
    </div>
  );
};

export default CategoryDetail;
