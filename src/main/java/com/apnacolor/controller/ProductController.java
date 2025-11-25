package com.apnacolor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.apnacolor.entity.Product;
import com.apnacolor.repository.ProductRepository;
import com.apnacolor.services.ProductService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
//@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/all")
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}/image")
	public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
		Optional<Product> productOpt = productService.getProductById(id);
		if (productOpt.isPresent() && productOpt.get().getImage() != null) {
			byte[] image = productOpt.get().getImage();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG); // or MediaType.IMAGE_PNG depending on image
			return new ResponseEntity<>(image, headers, HttpStatus.OK);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Product> addProduct(@RequestPart("product") Product product,
			@RequestPart("image") MultipartFile imageFile) {

		if(product.getQuantity()<= 0) {
			product.setQuantity(1);
		}
		try {
			product.setImage(imageFile.getBytes());
			Product savedProduct = productService.addProduct(product);
			return ResponseEntity.ok(savedProduct);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
		Optional<Product> optionalProduct = productService.getProductById(id);

		if (optionalProduct.isPresent()) {
			Product existingProduct = optionalProduct.get();

			existingProduct.setName(updatedProduct.getName());
			existingProduct.setBrand(updatedProduct.getBrand());
			existingProduct.setType(updatedProduct.getType());
			existingProduct.setColor(updatedProduct.getColor());
			existingProduct.setPrice(updatedProduct.getPrice());
			existingProduct.setQuantity(updatedProduct.getQuantity());
			existingProduct.setDescription(updatedProduct.getDescription());
			existingProduct.setImage(updatedProduct.getImage());

			Product savedProduct = productService.saveProduct(existingProduct);
			return ResponseEntity.ok(savedProduct);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete Product
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
		boolean deleted = productService.deleteProductById(id);
		if (deleted) {
			return ResponseEntity.ok("Product deleted successfully.");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/search")
	public List<Product> searchProducts(@RequestParam("q") String query) {
		return productService.searchProducts(query);
	}

}
