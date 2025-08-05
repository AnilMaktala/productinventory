import React from "react";
import { render, screen } from "@testing-library/react";
import App from "./App";

test("renders inventory management system title", () => {
  render(<App />);
  const titleElement = screen.getByText(/Product Inventory Management System/i);
  expect(titleElement).toBeInTheDocument();
});

test("renders welcome message", () => {
  render(<App />);
  const welcomeElement = screen.getByText(
    /Welcome to the Inventory Management Dashboard/i,
  );
  expect(welcomeElement).toBeInTheDocument();
});

test("renders coming soon message", () => {
  render(<App />);
  const comingSoonElement = screen.getByText(/Dashboard Coming Soon/i);
  expect(comingSoonElement).toBeInTheDocument();
});
