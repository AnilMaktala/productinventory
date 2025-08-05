import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import ProtectedRoute from './components/ProtectedRoute';
import {
  Dashboard,
  Products,
  ProductDetail,
  ProductForm,
  Categories,
  CategoryDetail,
  CategoryForm,
  NotFound,
} from './pages';
import './App.css';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          {/* Dashboard - Main route */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Dashboard />
              </ProtectedRoute>
            }
          />

          {/* Product routes */}
          <Route
            path="/products"
            element={
              <ProtectedRoute>
                <Products />
              </ProtectedRoute>
            }
          />

          <Route
            path="/products/new"
            element={
              <ProtectedRoute>
                <ProductForm />
              </ProtectedRoute>
            }
          />

          <Route
            path="/products/:id"
            element={
              <ProtectedRoute>
                <ProductDetail />
              </ProtectedRoute>
            }
          />

          <Route
            path="/products/:id/edit"
            element={
              <ProtectedRoute>
                <ProductForm />
              </ProtectedRoute>
            }
          />

          {/* Category routes */}
          <Route
            path="/categories"
            element={
              <ProtectedRoute>
                <Categories />
              </ProtectedRoute>
            }
          />

          <Route
            path="/categories/new"
            element={
              <ProtectedRoute>
                <CategoryForm />
              </ProtectedRoute>
            }
          />

          <Route
            path="/categories/:id"
            element={
              <ProtectedRoute>
                <CategoryDetail />
              </ProtectedRoute>
            }
          />

          <Route
            path="/categories/:id/edit"
            element={
              <ProtectedRoute>
                <CategoryForm />
              </ProtectedRoute>
            }
          />

          {/* Error routes */}
          <Route
            path="/unauthorized"
            element={
              <div className="error-page">
                <h1>Unauthorized</h1>
                <p>You don't have permission to access this page.</p>
              </div>
            }
          />

          {/* Catch-all route for 404 */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
