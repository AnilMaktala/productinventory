// Product types
export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  inventoryQuantity: number;
  sku: string;
  categoryId?: number;
  categoryName?: string;
  lowStock: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface ProductDTO {
  name: string;
  description: string;
  price: number;
  inventoryQuantity: number;
  sku: string;
  categoryId?: number;
}

// Category types
export interface Category {
  id: number;
  name: string;
  description: string;
  productCount?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface CategoryDTO {
  name: string;
  description: string;
}

// API Response types
export interface ApiResponse<T> {
  data: T;
  message?: string;
  timestamp: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}

// Error types
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  details?: string[];
  path: string;
}

// Search and filter types
export interface ProductSearchParams {
  name?: string;
  category?: string;
  minPrice?: number;
  maxPrice?: number;
  inStock?: boolean;
  page?: number;
  size?: number;
  sort?: string;
}

// Inventory update types
export interface InventoryUpdateDTO {
  quantity: number;
}
