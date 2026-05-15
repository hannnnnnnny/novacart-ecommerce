package com.novacart.store.config;

import com.novacart.store.entity.AdminUser;
import com.novacart.store.entity.Category;
import com.novacart.store.entity.Product;
import com.novacart.store.entity.ProductStatus;
import com.novacart.store.repository.AdminUserRepository;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    public static final String DEFAULT_ADMIN_EMAIL = "admin@novacart.local";
    public static final String DEFAULT_ADMIN_PASSWORD = "NovaCartAdmin123!";

    @Bean
    CommandLineRunner seedAdminUser(
            AdminUserRepository adminUserRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!adminUserRepository.existsByEmailIgnoreCase(DEFAULT_ADMIN_EMAIL)) {
                AdminUser adminUser = new AdminUser(
                        DEFAULT_ADMIN_EMAIL,
                        passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD),
                        "ADMIN",
                        true
                );
                adminUserRepository.save(adminUser);
            }

            Map<String, Category> categories = seedCategories(categoryRepository);
            seedProducts(productRepository, categories);
        };
    }

    private Map<String, Category> seedCategories(CategoryRepository categoryRepository) {
        return Map.of(
                "home-goods", seedCategory(
                        categoryRepository,
                        "Home Goods",
                        "home-goods",
                        "Well-made essentials for calm, practical living."
                ),
                "studio-supplies", seedCategory(
                        categoryRepository,
                        "Studio Supplies",
                        "studio-supplies",
                        "Tools and materials for organized creative work."
                ),
                "kitchen-table", seedCategory(
                        categoryRepository,
                        "Kitchen & Table",
                        "kitchen-table",
                        "Durable preparation, serving, and storage pieces for everyday meals."
                ),
                "daily-carry", seedCategory(
                        categoryRepository,
                        "Daily Carry",
                        "daily-carry",
                        "Reliable bags, pouches, and travel pieces for errands and short trips."
                ),
                "wellness-basics", seedCategory(
                        categoryRepository,
                        "Wellness Basics",
                        "wellness-basics",
                        "Small routine upgrades for calmer mornings, evenings, and work breaks."
                ),
                "gift-sets", seedCategory(
                        categoryRepository,
                        "Gift Sets",
                        "gift-sets",
                        "Thoughtful bundles for useful, memorable gifting."
                ),
                "outdoor-living", seedCategory(
                        categoryRepository,
                        "Outdoor Living",
                        "outdoor-living",
                        "Portable goods for garden projects, picnics, and weekend resets."
                ),
                "pantry-storage", seedCategory(
                        categoryRepository,
                        "Pantry Storage",
                        "pantry-storage",
                        "Clear, stackable storage pieces that make home inventory easier to manage."
                )
        );
    }

    private void seedProducts(ProductRepository productRepository, Map<String, Category> categories) {
        List<SeedProduct> products = List.of(
                new SeedProduct("bamboo-desk-organizer", "Bamboo Desk Organizer", "studio-supplies", "Northline Goods", "A compact organizer with layered compartments for pens, notes, and small workspace tools.", "39.00", "48.00", 28, 6, "https://images.unsplash.com/photo-1586953208448-b95a79798f07?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("workspace", "bamboo", "organization")),
                new SeedProduct("linen-market-tote", "Linen Market Tote", "daily-carry", "Hearth & Loom", "A durable everyday tote with reinforced handles and a clean natural texture.", "32.00", null, 42, 8, "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("carry", "linen", "errands")),
                new SeedProduct("ceramic-pouring-pitcher", "Ceramic Pouring Pitcher", "kitchen-table", "Vale Studio", "A hand-finished pitcher for table service, flowers, or a quiet kitchen shelf.", "48.00", "58.00", 16, 4, "https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("ceramic", "table", "serving")),
                new SeedProduct("cotton-kitchen-cloth-set", "Cotton Kitchen Cloth Set", "kitchen-table", "Hearth & Loom", "A set of four absorbent cotton cloths with a soft weave and muted color palette.", "24.00", null, 64, 12, "https://images.unsplash.com/photo-1582735689369-4fe89db7114c?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("cotton", "kitchen", "washable")),
                new SeedProduct("notebook-planning-kit", "Notebook Planning Kit", "studio-supplies", "Bright Shelf", "A practical kit with two lay-flat notebooks, page markers, and a slim archival pen.", "29.00", "36.00", 35, 8, "https://images.unsplash.com/photo-1517842645767-c639042777db?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("planning", "paper", "desk")),
                new SeedProduct("morning-ritual-gift-box", "Morning Ritual Gift Box", "gift-sets", "Kind Table", "A curated box with a ceramic cup, cotton cloth, and a small planning notebook.", "72.00", "86.00", 12, 3, "https://images.unsplash.com/photo-1512909006721-3d6018887383?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("gift", "morning", "bundle")),
                new SeedProduct("acacia-serving-board", "Acacia Serving Board", "kitchen-table", "Northline Goods", "A slim acacia board sized for bread, fruit, and small shared plates.", "44.00", null, 21, 5, "https://images.unsplash.com/photo-1542838132-92c53300491e?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("wood", "serving", "kitchen")),
                new SeedProduct("stoneware-breakfast-bowl", "Stoneware Breakfast Bowl", "kitchen-table", "Vale Studio", "A low-profile stoneware bowl with a satin glaze and comfortable daily weight.", "26.00", null, 38, 8, "https://images.unsplash.com/photo-1516916759473-600c07bc12d4?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("stoneware", "table", "daily")),
                new SeedProduct("glass-pantry-canister-set", "Glass Pantry Canister Set", "pantry-storage", "Clearhouse", "Three stackable glass canisters with snug bamboo lids for dry pantry staples.", "54.00", "68.00", 18, 5, "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("pantry", "glass", "storage")),
                new SeedProduct("fold-flat-laundry-basket", "Fold Flat Laundry Basket", "home-goods", "Hearth & Loom", "A structured cotton basket that folds down for tight closets and small laundry rooms.", "46.00", null, 14, 4, "https://images.unsplash.com/photo-1604335399105-a0c585fd81a1?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("laundry", "cotton", "home")),
                new SeedProduct("brass-wall-hook-pair", "Brass Wall Hook Pair", "home-goods", "Northline Goods", "Two brushed brass hooks for entryways, towels, or light kitchen tools.", "34.00", null, 9, 5, "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("entryway", "brass", "hardware")),
                new SeedProduct("recycled-wool-throw", "Recycled Wool Throw", "home-goods", "Hearth & Loom", "A soft recycled wool throw with a warm handfeel and a tidy blanket stitch edge.", "88.00", "104.00", 7, 4, "https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("wool", "living-room", "textile")),
                new SeedProduct("desk-cable-tray", "Desk Cable Tray", "studio-supplies", "Bright Shelf", "A powder-coated under-desk tray that keeps power strips and loose cables off the floor.", "42.00", null, 25, 6, "https://images.unsplash.com/photo-1497366754035-f200968a6e72?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("desk", "cable", "office")),
                new SeedProduct("archival-marker-set", "Archival Marker Set", "studio-supplies", "Bright Shelf", "Six archival markers with reliable black pigment for labels, notes, and sketches.", "18.00", null, 53, 12, "https://images.unsplash.com/photo-1513364776144-60967b0f800f?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("markers", "labels", "studio")),
                new SeedProduct("cork-pinboard-tiles", "Cork Pinboard Tiles", "studio-supplies", "Northline Goods", "A set of modular cork tiles for mood boards, project notes, and compact workspaces.", "36.00", "45.00", 19, 6, "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("cork", "planning", "wall")),
                new SeedProduct("aluminum-pencil-cup", "Aluminum Pencil Cup", "studio-supplies", "Bright Shelf", "A brushed aluminum cup with a weighted base and soft felt underside.", "22.00", null, 41, 10, "https://images.unsplash.com/photo-1527236438218-d82077ae1f85?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("desk", "aluminum", "tools")),
                new SeedProduct("canvas-tool-roll", "Canvas Tool Roll", "daily-carry", "Field Day Works", "A waxed canvas roll for small tools, art supplies, charging cables, or repair kits.", "38.00", null, 17, 5, "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("canvas", "carry", "tools")),
                new SeedProduct("modular-weekly-planner", "Modular Weekly Planner", "studio-supplies", "Bright Shelf", "A reusable weekly planner board with removable cards for tasks, meals, and errands.", "52.00", "64.00", 11, 4, "https://images.unsplash.com/photo-1499750310107-5fef28a66643?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("planning", "reusable", "workflow")),
                new SeedProduct("insulated-market-cooler", "Insulated Market Cooler", "daily-carry", "Field Day Works", "A soft-sided cooler that keeps groceries, picnic food, and short-trip drinks protected.", "68.00", null, 13, 4, "https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("cooler", "market", "travel")),
                new SeedProduct("weatherproof-picnic-blanket", "Weatherproof Picnic Blanket", "outdoor-living", "Field Day Works", "A roll-up picnic blanket with a soft top layer and water-resistant base.", "76.00", "92.00", 10, 4, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("picnic", "outdoor", "weekend")),
                new SeedProduct("herb-garden-starter-tray", "Herb Garden Starter Tray", "outdoor-living", "Greenhouse Lane", "A compact tray with labeled inserts for starting basil, parsley, thyme, and mint.", "31.00", null, 26, 7, "https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("garden", "herbs", "starter")),
                new SeedProduct("compact-compost-caddy", "Compact Compost Caddy", "outdoor-living", "Greenhouse Lane", "A countertop caddy with a charcoal filter lid and removable inner bucket.", "58.00", null, 0, 5, "https://images.unsplash.com/photo-1592419044706-39796d40f98c?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.DRAFT, List.of("compost", "kitchen", "garden")),
                new SeedProduct("cedar-shoe-care-kit", "Cedar Shoe Care Kit", "daily-carry", "Northline Goods", "A compact care kit with cedar brush, cotton cloth, and neutral conditioning cream.", "49.00", null, 3, 4, "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("care", "cedar", "travel")),
                new SeedProduct("travel-laundry-pouch-set", "Travel Laundry Pouch Set", "daily-carry", "Field Day Works", "Three breathable pouches for separating clean, worn, and delicate travel garments.", "27.00", null, 34, 8, "https://images.unsplash.com/photo-1488646953014-85cb44e25828?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("travel", "laundry", "packing")),
                new SeedProduct("refillable-soap-dispenser", "Refillable Soap Dispenser", "wellness-basics", "Clearhouse", "A weighted glass dispenser with a stainless pump for kitchen or bath counters.", "33.00", "40.00", 22, 6, "https://images.unsplash.com/photo-1607006483224-9f9f34a2e9cc?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("bath", "refillable", "glass")),
                new SeedProduct("sleep-notes-bedside-pad", "Sleep Notes Bedside Pad", "wellness-basics", "Bright Shelf", "A compact tear-off pad for evening reflections, morning reminders, and bedside notes.", "16.00", null, 46, 10, "https://images.unsplash.com/photo-1493836512294-502baa1986e2?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("wellness", "notes", "routine")),
                new SeedProduct("calm-desk-candle", "Calm Desk Candle", "wellness-basics", "Kind Table", "A clean-burning candle with cedar, citrus, and soft herbal notes for short work breaks.", "28.00", null, 15, 5, "https://images.unsplash.com/photo-1602874801006-e26c7655474d?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("candle", "desk", "break")),
                new SeedProduct("host-kitchen-gift-crate", "Host Kitchen Gift Crate", "gift-sets", "Kind Table", "A ready-to-gift crate with cloths, a serving board, markers, and pantry labels.", "98.00", "118.00", 8, 3, "https://images.unsplash.com/photo-1513885535751-8b9238bd345a?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("gift", "kitchen", "host")),
                new SeedProduct("new-home-essentials-bundle", "New Home Essentials Bundle", "gift-sets", "Kind Table", "A practical welcome bundle with hooks, cloths, storage labels, and a glass dispenser.", "112.00", "136.00", 6, 3, "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?auto=format&fit=crop&w=1200&q=80", true, ProductStatus.ACTIVE, List.of("gift", "home", "bundle")),
                new SeedProduct("care-package-stationery-set", "Care Package Stationery Set", "gift-sets", "Bright Shelf", "A stationery bundle with cards, envelopes, markers, and a small planning pad.", "43.00", null, 20, 6, "https://images.unsplash.com/photo-1519682577862-22b62b24e493?auto=format&fit=crop&w=1200&q=80", false, ProductStatus.ACTIVE, List.of("gift", "stationery", "cards"))
        );

        products.forEach(product -> seedProduct(productRepository, product, categories.get(product.categorySlug())));
    }

    private Category seedCategory(
            CategoryRepository categoryRepository,
            String name,
            String slug,
            String description
    ) {
        return categoryRepository.findBySlug(slug)
                .orElseGet(() -> categoryRepository.save(new Category(name, slug, description, true)));
    }

    private void seedProduct(ProductRepository productRepository, SeedProduct seed, Category category) {
        productRepository.findBySlug(seed.slug()).ifPresentOrElse(
                product -> applySeedProduct(product, seed, category),
                () -> productRepository.save(new Product(
                        seed.name(),
                        seed.slug(),
                        sku(seed.slug()),
                        seed.brand(),
                        seed.description(),
                        new BigDecimal(seed.price()),
                        seed.compareAtPrice() == null ? null : new BigDecimal(seed.compareAtPrice()),
                        seed.stockQuantity(),
                        seed.lowStockThreshold(),
                        seed.imageUrl(),
                        List.of(seed.imageUrl()),
                        seed.tags(),
                        seed.featured(),
                        seed.status(),
                        seed.status() == ProductStatus.ACTIVE,
                        category
                ))
        );
    }

    private void applySeedProduct(Product product, SeedProduct seed, Category category) {
        product.setName(seed.name());
        product.setSku(sku(seed.slug()));
        product.setBrand(seed.brand());
        product.setDescription(seed.description());
        product.setPrice(new BigDecimal(seed.price()));
        product.setCompareAtPrice(seed.compareAtPrice() == null ? null : new BigDecimal(seed.compareAtPrice()));
        product.setLowStockThreshold(seed.lowStockThreshold());
        product.setImageUrl(seed.imageUrl());
        product.setImageGallery(List.of(seed.imageUrl()));
        product.setTags(seed.tags());
        product.setFeatured(seed.featured());
        product.setStatus(seed.status());
        product.setActive(seed.status() == ProductStatus.ACTIVE);
        product.setCategory(category);
    }

    private String sku(String slug) {
        return "NC-" + slug.toUpperCase().replaceAll("[^A-Z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private record SeedProduct(
            String slug,
            String name,
            String categorySlug,
            String brand,
            String description,
            String price,
            String compareAtPrice,
            int stockQuantity,
            int lowStockThreshold,
            String imageUrl,
            boolean featured,
            ProductStatus status,
            List<String> tags
    ) {
    }
}
