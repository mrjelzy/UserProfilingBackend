package com.hai913i.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai913i.GlobalData;
import com.hai913i.UserProfilingBackendApplication;
import com.hai913i.models.Product;
import com.hai913i.repositories.ProductRepository;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductService
{
	@Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private GlobalData globalData;
    
    Logger logger = LoggerFactory.getLogger(UserProfilingBackendApplication.class);
    
    public Boolean addProduct(String nom, Double prix, String expire)
	{
		try {
	        LocalDate expireDate = LocalDate.parse(expire, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	        // Vérifiez si un produit avec le même nom existe déjà
	        Optional<Product> existingProduct = productRepository.findByNom(nom);
	        if (existingProduct.isPresent()) {
	            throw new Exception("Un produit avec ce nom existe déjà !");
	        }

	        // Créez une nouvelle instance de Product
	        Product newProduct = new Product(nom, prix, expireDate);

	        // Sauvegardez le nouveau produit dans la base de données
	        Long generatedId = productRepository.save(newProduct).getId();
	        
	        // LOGS
	        logger.trace(globalData.getActualUserId() + ":write:" + generatedId + ":Nouveau produit ajoute");

	        // Imprimez un message de confirmation
	        System.out.println("Nouveau produit ajouté : " + newProduct.toString());
	        return true;
	    } catch (DateTimeParseException e) {
	        System.out.println("Le format de la date d'expiration est incorrect. Utilisez dd/MM/yyyy.");
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
		
		return false;
    }
    
    public void deleteProduct(Long id) {
	    try {
	        Product p = productRepository.findById(id)
	                         .orElseThrow(() -> new NoSuchElementException("Aucun produit avec cet ID (" + id + ") trouvé."));
	        productRepository.deleteById(id);
	        
	        // LOGS
	        logger.trace(globalData.getActualUserId() + ":write:" + id + ":Produit supprime");

	        System.out.println(p.toString() + " supprimé !");
	    } catch (NoSuchElementException e) {
	        System.out.println(e.getMessage());
	    }
	}
    
    public Product getProduct(Long id) {
		Product product = null;
	    try {
	        product = productRepository.findById(id)
	                          .orElseThrow(() -> new NoSuchElementException("Aucun produit avec cet ID (" + id + ") trouvé."));
	        
	    } catch (NoSuchElementException e) {
	        // Gérez le cas où aucun produit avec l'ID donné n'est trouvé
	        System.out.println(e.getMessage());
	    }
	    
	    // LOGS
        logger.trace(globalData.getActualUserId() + ":read:" + id + ":Recherche de produit");
	    
	    return product;
	}
    
    public void updateProduct(Long id, String nom, Double prix, String expire)
	{
		try {
	        // Recherche du produit par son ID
	        Product product = productRepository.findById(id)
	                          .orElseThrow(() -> new NoSuchElementException("Aucun produit avec cet ID (" + id + ") trouvé."));

	        // Mise à jour des informations du produit
	        if (nom != null && !nom.isEmpty()) {
	            product.setNom(nom);
	        }
	        if (prix != null) {
	            product.setPrix(prix);
	        }
	        if (expire != null && !expire.isEmpty()) {
	            LocalDate expireDate = LocalDate.parse(expire, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            product.setDateExpiration(expireDate);
	        }

	        // Sauvegarde du produit mis à jour
	        productRepository.save(product);
	        
	        // LOGS
	        logger.trace(globalData.getActualUserId() + ":write:" + id + ":Produit mis a jour");

	        // Affichage d'une confirmation
	        System.out.println("Produit apres mis à jour : " + product.toString());
	    } catch (DateTimeParseException e) {
	        System.out.println("Le format de la date d'expiration est incorrect. Utilisez dd/MM/yyyy.");
	    } catch (NoSuchElementException e) {
	        System.out.println(e.getMessage());
	    }
	}
    
    @PostConstruct
    public void init() {
        if (globalData == null) {
        	System.out.println("GlobalData n'est pas injecté !");
        } else {
        	System.out.println("GlobalData est injecté avec succès.");
        }
    }
    
}