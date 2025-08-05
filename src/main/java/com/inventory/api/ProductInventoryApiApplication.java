package com.inventory.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductInventoryApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductInventoryApiApplication.class, args);
    }
}