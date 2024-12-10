package com.moudjane.microservice_product.controller;

import com.moudjane.microservice_product.model.Product;
import com.moudjane.microservice_product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Add a new product", description = "Creates a new product and returns the created product.")
    @ApiResponse(responseCode = "201", description = "Product created successfully",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product) {
        String generatedId = (product.id() == null || product.id().isBlank())
                ? UUID.randomUUID().toString()
                : product.id();

        Product newProduct = new Product(
                generatedId,
                product.name(),
                product.category(),
                product.price(),
                product.quantity()
        );
        return productService.addProduct(newProduct);
    }

    @Operation(summary = "Update a product", description = "Updates the product identified by the given ID.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable String id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @Operation(summary = "Delete a product", description = "Deletes the product identified by the given ID.")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }

    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @Operation(summary = "Get product by ID", description = "Retrieves a product by its ID.")
    @ApiResponse(responseCode = "200", description = "Product retrieved successfully",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @Operation(summary = "Search products", description = "Searches for products based on category, price, or name.")
    @ApiResponse(responseCode = "200", description = "Products matching the search criteria retrieved successfully",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) String name) {

        if (category != null) {
            return productService.searchByCategory(category);
        } else if (price != null) {
            return productService.searchByPrice(price);
        } else if (name != null) {
            return productService.searchByName(name);
        } else {
            throw new IllegalArgumentException("At least one search parameter is required");
        }
    }
}