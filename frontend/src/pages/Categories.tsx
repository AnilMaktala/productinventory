import React from 'react';
import { Link } from 'react-router-dom';

const Categories: React.FC = () => {
  return (
    <div className="categories fade-in">
      <div className="page-header slide-in-left">
        <h1>Categories</h1>
        <Link to="/categories/new" className="btn btn-primary hover-scale">
          <span>+</span>
          Add Category
        </Link>
      </div>
      <div className="categories-content">
        <div className="categories-grid">
          <div className="alert alert-info scale-in">
            <strong>Coming Soon:</strong> Categories list will be implemented in
            future tasks.
          </div>
          <div className="sample-category-links">
            <p>Sample navigation (will be replaced with actual data):</p>
            <div className="grid-auto-fit stagger-children">
              <Link to="/categories/1" className="category-link hover-lift">
                <div className="flex items-center gap-4">
                  <div className="text-2xl">📱</div>
                  <div>
                    <h4 className="font-semibold mb-1">Electronics</h4>
                    <p className="text-sm text-secondary-600 mb-0">
                      Phones, laptops, accessories
                    </p>
                    <span className="badge badge-primary">45 products</span>
                  </div>
                </div>
              </Link>
              <Link to="/categories/2" className="category-link hover-lift">
                <div className="flex items-center gap-4">
                  <div className="text-2xl">👕</div>
                  <div>
                    <h4 className="font-semibold mb-1">Clothing</h4>
                    <p className="text-sm text-secondary-600 mb-0">
                      Shirts, pants, accessories
                    </p>
                    <span className="badge badge-secondary">23 products</span>
                  </div>
                </div>
              </Link>
              <Link to="/categories/3" className="category-link hover-lift">
                <div className="flex items-center gap-4">
                  <div className="text-2xl">📚</div>
                  <div>
                    <h4 className="font-semibold mb-1">Books</h4>
                    <p className="text-sm text-secondary-600 mb-0">
                      Fiction, non-fiction, textbooks
                    </p>
                    <span className="badge badge-success">67 products</span>
                  </div>
                </div>
              </Link>
              <Link to="/categories/4" className="category-link hover-lift">
                <div className="flex items-center gap-4">
                  <div className="text-2xl">🏠</div>
                  <div>
                    <h4 className="font-semibold mb-1">Home & Garden</h4>
                    <p className="text-sm text-secondary-600 mb-0">
                      Furniture, decor, tools
                    </p>
                    <span className="badge badge-warning">12 products</span>
                  </div>
                </div>
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Categories;
