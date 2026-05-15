package com.novacart.store.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.novacart.store.config.DataInitializer;
import com.novacart.store.entity.Category;
import com.novacart.store.entity.Product;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.ProductRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${novacart.security.jwt-secret}")
    private String jwtSecret;

    @Test
    void loginReturnsBearerTokenForDefaultAdmin() throws Exception {
        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@novacart.local",
                                  "password": "NovaCartAdmin123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token", not(emptyString())))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.email").value(DataInitializer.DEFAULT_ADMIN_EMAIL))
                .andExpect(jsonPath("$.data.passwordHash").doesNotExist());
    }

    @Test
    void loginRejectsInvalidCredentialsWithUnauthorizedResponse() throws Exception {
        mockMvc.perform(post("/api/admin/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "admin@novacart.local",
                                  "password": "wrong-password"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email address or password."));
    }

    @Test
    void publicProductListingReturnsSeededProducts() throws Exception {
        mockMvc.perform(get("/api/public/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name", not(emptyString())));
    }

    @Test
    void checkoutCreatesOrderAndDeductsStock() throws Exception {
        Product product = saveProduct("Controller Checkout Tray", 5, "22.00", true);

        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Morgan Lee",
                                  "customerEmail": "morgan@example.com",
                                  "shippingAddress": "12 Market Street",
                                  "city": "Auckland",
                                  "postalCode": "1010",
                                  "country": "New Zealand",
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.items[0].quantity").value(2))
                .andExpect(jsonPath("$.data.totalAmount").value(44.00));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(3);
    }

    @Test
    void checkoutRejectsInsufficientStockWithoutDeductingInventory() throws Exception {
        Product product = saveProduct("Controller Low Stock Tray", 1, "22.00", true);

        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "Morgan Lee",
                                  "customerEmail": "morgan@example.com",
                                  "shippingAddress": "12 Market Street",
                                  "city": "Auckland",
                                  "postalCode": "1010",
                                  "country": "New Zealand",
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": 2
                                    }
                                  ]
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Insufficient stock for Controller Low Stock Tray."));

        Product unchangedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(unchangedProduct.getStockQuantity()).isEqualTo(1);
    }

    @Test
    void checkoutRejectsValidationErrorsWithFieldMessages() throws Exception {
        mockMvc.perform(post("/api/public/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerName": "",
                                  "customerEmail": "not-an-email",
                                  "shippingAddress": "",
                                  "city": "",
                                  "postalCode": "",
                                  "country": "",
                                  "items": []
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed. Please review the highlighted fields."))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void adminRouteRequiresBearerToken() throws Exception {
        mockMvc.perform(get("/api/admin/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Authentication is required."));
    }

    @Test
    void adminRouteRejectsInvalidBearerToken() throws Exception {
        mockMvc.perform(get("/api/admin/products")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Authentication token is invalid."));
    }

    @Test
    void adminRouteRejectsExpiredBearerToken() throws Exception {
        mockMvc.perform(get("/api/admin/products")
                        .header("Authorization", "Bearer " + expiredToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Authentication token has expired."));
    }

    private Product saveProduct(String name, int stockQuantity, String price, boolean active) {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        Category category = categoryRepository.save(new Category(
                name + " Category",
                "test-category-" + suffix,
                "Category for API controller tests.",
                true
        ));

        return productRepository.save(new Product(
                name,
                "test-product-" + suffix,
                "Product used by API controller tests.",
                new BigDecimal(price),
                stockQuantity,
                "https://example.com/test-product.jpg",
                active,
                category
        ));
    }

    private String expiredToken() {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(DataInitializer.DEFAULT_ADMIN_EMAIL)
                .issuedAt(Date.from(now.minus(3, ChronoUnit.HOURS)))
                .expiration(Date.from(now.minus(2, ChronoUnit.HOURS)))
                .signWith(secretKey)
                .compact();
    }
}
