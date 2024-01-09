package com.hai913i.repositories;

import com.hai913i.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>
{
    // Ici, vous pouvez définir des méthodes de recherche personnalisées si nécessaire
	// Optional<Product> findByid(long id);
}