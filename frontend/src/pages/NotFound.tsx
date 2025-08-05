import React from 'react';
import { Link } from 'react-router-dom';

const NotFound: React.FC = () => {
  return (
    <div className="not-found">
      <div className="not-found-content fade-in">
        <h1 className="bounce">404</h1>
        <h2 className="slide-in-left">Page Not Found</h2>
        <p className="slide-in-right">
          The page you're looking for doesn't exist or has been moved.
        </p>
        <div className="flex gap-4 justify-center scale-in">
          <Link to="/" className="btn btn-primary hover-scale">
            🏠 Go to Dashboard
          </Link>
          <Link to="/products" className="btn btn-secondary hover-scale">
            📦 View Products
          </Link>
        </div>
      </div>
    </div>
  );
};

export default NotFound;
