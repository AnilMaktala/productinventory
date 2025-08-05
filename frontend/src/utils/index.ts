// Format currency values
export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

// Format dates
export const formatDate = (dateString: string): string => {
  return new Date(dateString).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });
};

// Debounce function for search inputs
export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  delay: number
): ((...args: Parameters<T>) => void) => {
  let timeoutId: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => func(...args), delay);
  };
};

// Check if product is low stock
export const isLowStock = (
  quantity: number,
  threshold: number = 5
): boolean => {
  return quantity <= threshold;
};

// Generate SKU (simple implementation)
export const generateSKU = (name: string, category?: string): string => {
  const namePrefix = name.substring(0, 3).toUpperCase();
  const categoryPrefix = category
    ? category.substring(0, 2).toUpperCase()
    : 'GN';
  const timestamp = Date.now().toString().slice(-4);
  return `${categoryPrefix}-${namePrefix}-${timestamp}`;
};

// Validate email format
export const isValidEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Capitalize first letter
export const capitalize = (str: string): string => {
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};
