import React from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';

const CategoryForm: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Form submission logic will be implemented in future tasks
    console.log(isEdit ? 'Update category:' : 'Create category:', id);
    navigate('/categories');
  };

  const handleCancel = () => {
    navigate(-1); // Go back to previous page
  };

  return (
    <div className="category-form">
      <div className="page-header">
        <div className="breadcrumb">
          <Link to="/categories">Categories</Link>
          <span> / </span>
          {isEdit ? (
            <>
              <Link to={`/categories/${id}`}>Category Details</Link>
              <span> / </span>
              <span>Edit</span>
            </>
          ) : (
            <span>New Category</span>
          )}
        </div>
      </div>

      <div className="form-container">
        <h1>{isEdit ? 'Edit Category' : 'Create New Category'}</h1>

        <form onSubmit={handleSubmit} className="category-form-fields">
          <div className="form-group">
            <label htmlFor="name">Category Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              required
              placeholder="Enter category name"
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description</label>
            <textarea
              id="description"
              name="description"
              rows={4}
              placeholder="Enter category description"
            />
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
              {isEdit ? 'Update Category' : 'Create Category'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CategoryForm;
