import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';

interface ProtectedRouteProps {
  children: React.ReactNode;
  isAuthenticated?: boolean;
  requiredRole?: string;
  userRole?: string;
  requiresAuth?: boolean;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  children,
  isAuthenticated = true, // For now, we'll assume all routes are accessible
  requiredRole,
  userRole = 'admin', // Default role for development
  requiresAuth = true,
}) => {
  const location = useLocation();

  // Skip authentication check if not required
  if (!requiresAuth) {
    return <>{children}</>;
  }

  // Check authentication
  if (!isAuthenticated) {
    // Redirect to login page with return url
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Check role-based access
  if (requiredRole && userRole !== requiredRole) {
    // Redirect to unauthorized page or dashboard
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
