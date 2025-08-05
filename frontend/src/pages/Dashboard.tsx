import React from 'react';
import { Link } from 'react-router-dom';

const Dashboard: React.FC = () => {
  return (
    <div className="dashboard fade-in">
      <div className="page-header slide-in-left">
        <h1>Dashboard</h1>
        <div className="flex gap-4">
          <Link
            to="/products/new"
            className="btn btn-primary btn-sm hover-scale"
          >
            + Add Product
          </Link>
          <Link
            to="/categories/new"
            className="btn btn-secondary btn-sm hover-scale"
          >
            + Add Category
          </Link>
        </div>
      </div>
      <div className="dashboard-content">
        <div className="stats-grid stagger-children">
          <div className="stat-card hover-lift">
            <h3>Total Products</h3>
            <p>247</p>
            <div className="text-sm text-secondary mt-2">
              <span className="text-success">+12</span> from last month
            </div>
          </div>
          <div className="stat-card hover-lift">
            <h3>Low Stock Items</h3>
            <p>8</p>
            <div className="text-sm text-warning mt-2">Requires attention</div>
          </div>
          <div className="stat-card hover-lift">
            <h3>Categories</h3>
            <p>15</p>
            <div className="text-sm text-secondary mt-2">Well organized</div>
          </div>
          <div className="stat-card hover-lift">
            <h3>Total Value</h3>
            <p>$124,580</p>
            <div className="text-sm text-success mt-2">
              <span className="text-success">+5.2%</span> from last month
            </div>
          </div>
        </div>
        <div className="recent-activity slide-in-right">
          <h2>Recent Activity</h2>
          <div className="alert alert-info mb-6">
            <strong>Coming Soon:</strong> Activity feed will be implemented in
            future tasks.
          </div>
          <div className="space-y-4 stagger-children">
            <div className="card hover-lift">
              <div className="card-body">
                <div className="flex justify-between items-start">
                  <div>
                    <h4 className="font-semibold text-secondary-900 mb-2">
                      Sample Activity
                    </h4>
                    <p className="text-secondary-600 mb-0">
                      Product "iPhone 15 Pro" was updated
                    </p>
                  </div>
                  <span className="badge badge-primary">2 hours ago</span>
                </div>
              </div>
            </div>
            <div className="card hover-lift">
              <div className="card-body">
                <div className="flex justify-between items-start">
                  <div>
                    <h4 className="font-semibold text-secondary-900 mb-2">
                      Low Stock Alert
                    </h4>
                    <p className="text-secondary-600 mb-0">
                      MacBook Air M2 is running low on stock
                    </p>
                  </div>
                  <span className="badge badge-warning">5 hours ago</span>
                </div>
              </div>
            </div>
            <div className="card hover-lift">
              <div className="card-body">
                <div className="flex justify-between items-start">
                  <div>
                    <h4 className="font-semibold text-secondary-900 mb-2">
                      New Category
                    </h4>
                    <p className="text-secondary-600 mb-0">
                      Category "Smart Home" was created
                    </p>
                  </div>
                  <span className="badge badge-success">1 day ago</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
