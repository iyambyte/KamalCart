package com.kamalbyte.KamalCart.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kamalbyte.KamalCart.dto.ProductDto;
import com.kamalbyte.KamalCart.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllProducts(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		return ResponseEntity.ok(productService.getAllProducts(page, size));
	}

	@GetMapping("/search")
	public ResponseEntity<List<ProductDto>> searchProducts(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Double ratings) {
		List<ProductDto> products = productService.searchProducts(category, minPrice, maxPrice, keyword, ratings)
				.stream()
				.map(productService::convertToDto)
				.collect(Collectors.toList());
		return ResponseEntity.ok(products);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.convertToDto(productService.getProductById(id)));
	}

}
