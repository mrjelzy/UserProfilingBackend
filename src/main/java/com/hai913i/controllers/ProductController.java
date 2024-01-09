package com.hai913i.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai913i.models.Product;
import com.hai913i.repositories.ProductRepository;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
    
    @RequestMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @RequestMapping("/products.html")
	public String showAllProducts()
	{
		String result = "<h1>Liste des produits</h1> <ul>";
		
		for (Product u : productRepository.findAll())
		{
			result += "<li>" + u.toString() + "</li>";
		}
		
		return result + "</ul>";
	}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}