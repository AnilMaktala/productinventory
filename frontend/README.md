# Product Inventory Management System - Frontend

A React TypeScript frontend for the Product Inventory Management System.

## Features

- **Product Management**: Create, view, update, and delete products
- **Inventory Tracking**: Monitor and update inventory levels
- **Category Management**: Organize products into categories
- **Search & Filtering**: Find products by various criteria
- **Responsive Design**: Works on desktop, tablet, and mobile devices

## Technology Stack

- **React 19** with **TypeScript** for type safety
- **Create React App** for project setup and build tools
- **ESLint** and **Prettier** for code quality and formatting
- **CSS3** with responsive design principles

## Project Structure

```
src/
├── components/          # Reusable UI components
├── pages/              # Page components for different routes
├── services/           # API service functions
├── store/              # State management (Redux/Context)
├── types/              # TypeScript type definitions
├── utils/              # Utility functions
├── hooks/              # Custom React hooks
└── config/             # Configuration files
```

## Getting Started

### Prerequisites

- Node.js (version 16 or higher)
- npm or yarn
- Backend API running on http://localhost:8080

### Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Copy environment configuration:
   ```bash
   cp .env.example .env
   ```

4. Update environment variables in `.env` if needed

### Available Scripts

#### `npm start`
Runs the app in development mode.
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

#### `npm test`
Launches the test runner in interactive watch mode.

#### `npm run build`
Builds the app for production to the `build` folder.

#### `npm run lint`
Runs ESLint to check for code quality issues.

#### `npm run format`
Formats code using Prettier.

## Development Guidelines

### Code Style
- Use TypeScript for all new files
- Follow ESLint and Prettier configurations
- Use functional components with hooks
- Implement proper error handling
- Write meaningful component and function names

### Component Structure
- Keep components small and focused
- Use TypeScript interfaces for props
- Implement proper loading and error states
- Make components responsive

### API Integration
- Use the service layer for API calls
- Handle errors gracefully
- Implement proper loading states
- Use TypeScript types for API responses

## Environment Variables

- `REACT_APP_API_BASE_URL`: Backend API base URL (default: http://localhost:8080)
- `REACT_APP_DEFAULT_PAGE_SIZE`: Default pagination size (default: 20)
- `REACT_APP_MAX_PAGE_SIZE`: Maximum pagination size (default: 100)
- `REACT_APP_LOW_STOCK_THRESHOLD`: Low stock threshold (default: 5)

## API Integration

The frontend communicates with the Spring Boot backend API:

- **Products API**: `/api/products`
- **Categories API**: `/api/categories`
- **Inventory API**: `/api/products/{id}/inventory`

## Contributing

1. Follow the established code style and conventions
2. Write tests for new components and functions
3. Update documentation as needed
4. Ensure responsive design principles are followed

## Deployment

1. Build the production version:
   ```bash
   npm run build
   ```

2. Deploy the `build` folder to your web server or CDN

3. Configure environment variables for production

## Learn More

- [React Documentation](https://reactjs.org/)
- [TypeScript Documentation](https://www.typescriptlang.org/)
- [Create React App Documentation](https://facebook.github.io/create-react-app/docs/getting-started)