package com.inventory.api.config;

import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.model.Supplier;
import com.inventory.api.repository.CategoryRepository;
import com.inventory.api.repository.ProductRepository;
import com.inventory.api.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            loadSampleData();
        }
    }

private void loadSampleData() {
        log.info("Loading sample data...");

        // Create categories
        Category electronics = Category.builder()
                .name("Electronics")
                .description("Electronic devices and components")
                .build();

        Category clothing = Category.builder()
                .name("Clothing")
                .description("Apparel and fashion items")
                .build();

        Category books = Category.builder()
                .name("Books")
                .description("Books and educational materials")
                .build();

        Category homeGarden = Category.builder()
                .name("Home & Garden")
                .description("Home improvement and gardening supplies")
                .build();

        Category sports = Category.builder()
                .name("Sports")
                .description("Sports equipment and accessories")
                .build();

        categoryRepository.save(electronics);
        categoryRepository.save(clothing);
        categoryRepository.save(books);
        categoryRepository.save(homeGarden);
        categoryRepository.save(sports);

        // Create suppliers
        Supplier techCorp = Supplier.builder()
                .name("TechCorp Solutions")
                .contactPerson("Alice Johnson")
                .email("alice@techcorp.com")
                .phone("+1-555-0101")
                .address("123 Tech Street")
                .city("San Francisco")
                .country("USA")
                .postalCode("94105")
                .notes("Leading technology supplier")
                .active(true)
                .build();

        Supplier fashionForward = Supplier.builder()
                .name("Fashion Forward Inc")
                .contactPerson("Bob Smith")
                .email("bob@fashionforward.com")
                .phone("+1-555-0102")
                .address("456 Fashion Ave")
                .city("New York")
                .country("USA")
                .postalCode("10001")
                .notes("Premium fashion supplier")
                .active(true)
                .build();

        Supplier bookWorld = Supplier.builder()
                .name("BookWorld Distributors")
                .contactPerson("Carol Davis")
                .email("carol@bookworld.com")
                .phone("+1-555-0103")
                .address("789 Library Lane")
                .city("Chicago")
                .country("USA")
                .postalCode("60601")
                .notes("Educational and general books")
                .active(true)
                .build();

        Supplier homeStyle = Supplier.builder()
                .name("HomeStyle Suppliers")
                .contactPerson("David Wilson")
                .email("david@homestyle.com")
                .phone("+1-555-0104")
                .address("321 Home Blvd")
                .city("Los Angeles")
                .country("USA")
                .postalCode("90210")
                .notes("Home and garden supplies")
                .active(true)
                .build();

        Supplier sportZone = Supplier.builder()
                .name("SportZone International")
                .contactPerson("Eva Martinez")
                .email("eva@sportzone.com")
                .phone("+1-555-0105")
                .address("654 Sports Way")
                .city("Miami")
                .country("USA")
                .postalCode("33101")
                .notes("Sports equipment and gear")
                .active(true)
                .build();

        Supplier globalElectronics = Supplier.builder()
                .name("Global Electronics Ltd")
                .contactPerson("Frank Chen")
                .email("frank@globalelectronics.com")
                .phone("+86-21-1234-5678")
                .address("888 Innovation Road")
                .city("Shanghai")
                .country("China")
                .postalCode("200000")
                .notes("International electronics supplier")
                .active(true)
                .build();

        Supplier europeanTextiles = Supplier.builder()
                .name("European Textiles")
                .contactPerson("Grace Mueller")
                .email("grace@eurotextiles.com")
                .phone("+49-30-9876-5432")
                .address("777 Textile Street")
                .city("Berlin")
                .country("Germany")
                .postalCode("10115")
                .notes("High-quality European textiles")
                .active(true)
                .build();

        supplierRepository.save(techCorp);
        supplierRepository.save(fashionForward);
        supplierRepository.save(bookWorld);
        supplierRepository.save(homeStyle);
        supplierRepository.save(sportZone);
        supplierRepository.save(globalElectronics);
        supplierRepository.save(europeanTextiles);

        // Create products
        Product smartphone = Product.builder()
                .name("Smartphone Pro")
                .description("Latest smartphone with advanced features")
                .price(new BigDecimal("899.99"))
                .inventoryQuantity(50)
                .sku("PHONE-001")
                .category(electronics)
                .supplier(techCorp)
                .lowStockThreshold(10)
                .build();

        Product headphones = Product.builder()
                .name("Wireless Headphones")
                .description("Premium noise-cancelling headphones")
                .price(new BigDecimal("299.99"))
                .inventoryQuantity(25)
                .sku("AUDIO-001")
                .category(electronics)
                .supplier(techCorp)
                .lowStockThreshold(5)
                .build();

        Product laptop = Product.builder()
                .name("Laptop Computer")
                .description("High-performance laptop for professionals")
                .price(new BigDecimal("1299.99"))
                .inventoryQuantity(15)
                .sku("COMP-001")
                .category(electronics)
                .supplier(globalElectronics)
                .lowStockThreshold(5)
                .build();

        Product tshirt = Product.builder()
                .name("Designer T-Shirt")
                .description("Premium cotton t-shirt with designer logo")
                .price(new BigDecimal("49.99"))
                .inventoryQuantity(100)
                .sku("SHIRT-001")
                .category(clothing)
                .supplier(fashionForward)
                .lowStockThreshold(20)
                .build();

        Product jeans = Product.builder()
                .name("Jeans Classic")
                .description("Classic fit denim jeans")
                .price(new BigDecimal("79.99"))
                .inventoryQuantity(75)
                .sku("JEANS-001")
                .category(clothing)
                .supplier(europeanTextiles)
                .lowStockThreshold(15)
                .build();

        Product book = Product.builder()
                .name("Programming Guide")
                .description("Complete guide to modern programming")
                .price(new BigDecimal("59.99"))
                .inventoryQuantity(30)
                .sku("BOOK-001")
                .category(books)
                .supplier(bookWorld)
                .lowStockThreshold(10)
                .build();

        Product gardenTools = Product.builder()
                .name("Garden Tools Set")
                .description("Complete set of essential garden tools")
                .price(new BigDecimal("149.99"))
                .inventoryQuantity(20)
                .sku("GARDEN-001")
                .category(homeGarden)
                .supplier(homeStyle)
                .lowStockThreshold(5)
                .build();

        Product tennisRacket = Product.builder()
                .name("Tennis Racket")
                .description("Professional grade tennis racket")
                .price(new BigDecimal("199.99"))
                .inventoryQuantity(12)
                .sku("TENNIS-001")
                .category(sports)
                .supplier(sportZone)
                .lowStockThreshold(5)
                .build();

        Product tablet = Product.builder()
                .name("Tablet Device")
                .description("Lightweight tablet for entertainment and work")
                .price(new BigDecimal("399.99"))
                .inventoryQuantity(8)
                .sku("TABLET-001")
                .category(electronics)
                .supplier(globalElectronics)
                .lowStockThreshold(10)
                .build();

        Product runningShoes = Product.builder()
                .name("Running Shoes")
                .description("High-performance running shoes")
                .price(new BigDecimal("129.99"))
                .inventoryQuantity(3)
                .sku("SHOES-001")
                .category(sports)
                .supplier(sportZone)
                .lowStockThreshold(10)
                .build();

        productRepository.save(smartphone);
        productRepository.save(headphones);
        productRepository.save(laptop);
        productRepository.save(tshirt);
        productRepository.save(jeans);
        productRepository.save(book);
        productRepository.save(gardenTools);
        productRepository.save(tennisRacket);
        productRepository.save(tablet);
        productRepository.save(runningShoes);

        log.info("Sample data loaded successfully!");
        log.info("Created {} categories", categoryRepository.count());
        log.info("Created {} suppliers", supplierRepository.count());
        log.info("Created {} products", productRepository.count());
    }
}