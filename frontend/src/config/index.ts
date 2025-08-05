// API Configuration
export const API_BASE_URL =
  process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

export const API_ENDPOINTS = {
  PRODUCTS: '/api/products',
  CATEGORIES: '/api/categories',
} as const;

// Application Configuration
export const APP_CONFIG = {
  DEFAULT_PAGE_SIZE: 20,
  MAX_PAGE_SIZE: 100,
  DEBOUNCE_DELAY: 300,
  LOW_STOCK_THRESHOLD: 5,
} as const;

// Environment
export const isDevelopment = process.env.NODE_ENV === 'development';
export const isProduction = process.env.NODE_ENV === 'production';
