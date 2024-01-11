package com.hai913i;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hai913i.logs.UserProfileBuilder;
import com.hai913i.models.Product;
import com.hai913i.models.User;
import com.hai913i.repositories.ProductRepository;
import com.hai913i.repositories.UserRepository;
import com.hai913i.services.ProductService;
import com.hai913i.services.UserService;

@SpringBootApplication
public class UserProfilingBackendApplication implements CommandLineRunner{
	
	@Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    private Scanner scanner;
    
    Logger logger = LoggerFactory.getLogger(UserProfilingBackendApplication.class);
    
    Long actualUserId = null;

    public static void main(String[] args) {
        SpringApplication.run(UserProfilingBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	scanner = new Scanner(System.in);
    	// userService = new UserService(userRepository);
    	// productService = new ProductService(productRepository);
    	
    	clearJsonFile("logs/user-trace.json");

        // Initialisation des données
        initData();

        // Logique CLI
        startCli();
        
        UserProfileBuilder.profileBuilder();
        
        System.exit(0);
    }

    private void initData() {
    	System.out.println("");
    	
        // Créez une nouvelle instance de Product
        Product newProduct = new Product("Chocolat", 3.50, LocalDate.of(2023, 12, 31));
        User newUser = new User("Pepe", "cacamail", LocalDate.of(2000, 12, 31), "caca");
        
        // Sauvegardez le Product et l'User dans la base de données H2
        userRepository.save(newUser);
        productRepository.save(newProduct);

        // Pour vérifier, imprimez le Product et l'User sauvegardé dans la console
        System.out.println("Produit sauvegardé : " + newProduct.toString());
        System.out.println("User sauvegardé : " + newUser.toString());
        
        System.out.println("");
        System.out.println("============================");
        System.out.println("");
    }

    private void startCli() {
        String choice;
        String nameActualUser = null;
        
        do
        {
	        System.out.println("Veuillez vous identifier ou créer un compte pour manipuler les produits");
	        System.out.println("[1] - Connexion");
	        System.out.println("[2] - Inscription");
	        System.out.println("[x] - Quitter");
	        choice = scanner.nextLine();
        
        	if (choice.equals("1"))
            {
            	System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String mdp = scanner.nextLine();
                
                nameActualUser = userService.connectUser(email, mdp);
                
                System.out.println("");
                System.out.println("============================");
                System.out.println("");
                
                if(nameActualUser != null)
                {
                	manipulerProduit(nameActualUser);
                }
            } 
            else if (choice.equals("2")) {
                String email, birthday, name, password;
                do
                {
                    System.out.print("Email : ");
                    email = scanner.nextLine();
                    System.out.print("Anniversaire (dd/MM/yyyy) : ");
                    birthday = scanner.nextLine();
                    System.out.print("Nom :");
                    name = scanner.nextLine();
                    System.out.print("Mot de passe : ");
                    password = scanner.nextLine();
                } while (!userService.addUser(email, birthday, name, password));
                
                System.out.println("");
                System.out.println("============================");
                System.out.println("");
            }
        } while (!choice.equals("x"));
        
        System.out.println("");
        System.out.println("============================");
        System.out.println("");
        
        scanner.close();
    }
    
    private void manipulerProduit(String nameActualUser)
	{
    	String choice_2;
    	
    	do
        {
	        System.out.println("Bienvenue " + nameActualUser + ",");
	        System.out.println("[1] - Afficher les produits");
	        System.out.println("[2] - Récupérer un produit par son ID");
	        System.out.println("[3] - Ajouter un nouveau produit");
	        System.out.println("[4] - Supprimer un produit par son ID");
	        System.out.println("[5] - Mettre à jour les informations d'un produit");
	        System.out.println("[x] - Déconnexion");
	        choice_2 = scanner.nextLine();
        
        	if (choice_2.equals("1"))
            {
        		System.out.println("");
                System.out.println("============================");
                System.out.println("");
        		
        		for (Product p : productRepository.findAll())
        		{
        			System.out.println(p.toString());
        		}
            }
        	if (choice_2.equals("2"))
        	{
        		System.out.print("Entrer l'Id : ");
    	        String myId = scanner.nextLine();
    	        
    	        Product p = productService.getProduct(Long.parseLong(myId));
    	        
    	        if (p != null)
    	        {
    	        	// Affichez les détails du produit si trouvé
    		        System.out.println("Détails du produit : " + p.toString());
    	        }
    	        
        	}
        	if (choice_2.equals("3"))
        	{
        		System.out.print("Nom du produit : ");
    	        String name = scanner.nextLine();
    	        
    	        System.out.print("Prix du produit : ");
    	        Double prix = Double.parseDouble(scanner.nextLine());
    	        
    	        System.out.print("Date d'expiration (dd/MM/yyyy) : ");
    	        String expireDate = scanner.nextLine();
    	        
    	        productService.addProduct(name, prix, expireDate);
        	}
        	if (choice_2.equals("4"))
        	{
        		System.out.print("ID du produit à supprimer : ");
    	        Long id = Long.parseLong(scanner.nextLine());
    	        
    	        productService.deleteProduct(id);
        	}
        	if (choice_2.equals("5"))
        	{
        		System.out.print("ID du produit à modifier (laisser vide pour ne pas modifier) : ");
    	        Long id = Long.parseLong(scanner.nextLine());
    	        
    	        Product p = productService.getProduct(id);
    	        
    	        if (p != null)
    	        {
    	        	System.out.println(p.toString());
        	        
        	        System.out.print("Nom du produit : ");
        	        String name = scanner.nextLine();
        	        if (name.equals(""))
        	        {
        	        	name = null;
        	        }
        	        
        	        System.out.print("Prix du produit : ");
        	        Double prix;
        	        String prixTxt = scanner.nextLine();
        	        if (prixTxt.equals(""))
        	        {
        	        	prix = null;
        	        }
        	        else
        	        {
        	        	prix = Double.parseDouble(prixTxt);
        	        }
        	        
        	        System.out.print("Date d'expiration (dd/MM/yyyy) : ");
        	        String expireDate = scanner.nextLine();
        	        if (expireDate.equals(""))
        	        {
        	        	expireDate = null;
        	        }
        	        
        	        System.out.println("Produit avant mis à jour : " + p.toString());
        	        productService.updateProduct(id, name, prix, expireDate);
    	        }
    	        
        	}
        	
        	System.out.println("");
	        System.out.println("============================");
	        System.out.println("");
        } while (!choice_2.equals("x"));
	}
	
	public static void clearJsonFile(String filePath) {
        try (FileWriter fileWriter = new FileWriter(filePath, false)) {
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
