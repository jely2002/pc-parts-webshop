package com.jelleglebbeek.pcparts.product;

import com.jelleglebbeek.pcparts.exceptions.BadRequestException;
import com.jelleglebbeek.pcparts.product.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

    private final ProductService productService;
    private final S3Service s3Service;

    @Autowired
    public ProductController(ProductService productService, S3Service s3Service) {
        this.productService = productService;
        this.s3Service = s3Service;
    }

    @GetMapping()
    public Iterable<Product> getAllProducts() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public Iterable<Product> getProduct(@PathVariable String id) {
        String[] stringIDs = id.split(",");
        if (stringIDs.length > 1) {
            List<UUID> ids = Arrays.stream(stringIDs).map(UUID::fromString).collect(Collectors.toList());
            return this.productService.findMultiple(ids);
        } else {
            UUID uuid = UUID.fromString(stringIDs[0]);
            return Collections.singletonList(this.productService.findOne(uuid));
        }
    }

    @GetMapping("/category/{category}")
    public Iterable<Product> getProductByCategory(@PathVariable String category) {
        return this.productService.findByCategory(category);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping()
    public Product createProduct(@RequestBody Product product) {
        return this.productService.create(product);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/image")
    public void addImage(@RequestParam MultipartFile file, @PathVariable UUID id) {
        Product addImage = this.productService.findOne(id);
        try {
            String publicUrl = this.s3Service.upload(file.getBytes(), addImage.getId());
            addImage.setImageUrl(publicUrl);
            this.productService.update(id, addImage);
        } catch (IOException e) {
            throw new BadRequestException("This image file is not valid.");
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable UUID id, @RequestBody Product product) {
        return this.productService.update(id, product);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable UUID id) {
        this.s3Service.remove(id);
        this.productService.delete(id);
    }

}
