package com.hai913i;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    public static void main(String[] args) {
        SpringApplication.run(UserProfilingBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	scanner = new Scanner(System.in);
    	
    	clearJsonFile("logs/user-trace.json");

        // Initialisation des données
        initData();

        System.out.println("[1] - Manipuler manuellement");
        System.out.println("[2] - Lancer les scenarios");

        if (scanner.nextLine().equals("1"))
        {
        	startCli();
        }
        else
        {
        	startSimulation();
        }
        
        System.out.println("");
        
        UserProfileBuilder.profileBuilder();
        
        System.exit(0);
    }

    private void initData() {
    	System.out.println("");
    	
        // Créez une nouvelle instance de Product
        List<Product> products = Arrays.asList(
            new Product("Chocolat", 3.50, LocalDate.of(2023, 12, 31)),
            new Product("Bonbons", 2.00, LocalDate.of(2023, 10, 15)),
            new Product("Biscuits", 4.25, LocalDate.of(2023, 11, 20)),
            new Product("Gâteau", 5.75, LocalDate.of(2023, 9, 10))
        );
        
        User newUser = new User("Pepe", "pepe@mail.com", LocalDate.of(2000, 12, 31), "pepe");
        
        // Sauvegardez le Product et l'User dans la base de données H2
        userRepository.save(newUser);
        productRepository.saveAll(products);

        // Pour vérifier, imprimez le Product et l'User sauvegardé dans la console
        System.out.println("Produit sauvegardé : " + products.toString());
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
	
	public void startSimulation()
	{
		scenario_1();
		scenario_2();
		scenario_3();
		scenario_4();
		scenario_5();
		
		System.out.println("");
        System.out.println("============================");
        System.out.println("");
		
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void scenario_1()
	{
		userService.addUser("john.doe@email.com", "10/12/2000", "John", "mdp");
		userService.connectUser("john.doe@email.com", "mdp");
		
		productService.addProduct("Cafe", 3.0, "12/12/2024");
		productService.addProduct("Riz", 5.0, "12/12/2024");
		
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
	}
	
	private void scenario_2()
	{
		userService.addUser("anne.doe@email.com", "10/12/2000", "Anne", "mdp");
		userService.connectUser("anne.doe@email.com", "mdp");
		
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
		
		productService.addProduct("Soupe", 2.0, "12/02/2024");
		
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
	}
	
	private void scenario_3()
	{
		userService.addUser("clow.doe@email.com", "09/08/1996", "Clow", "mdp");
		userService.connectUser("clow.doe@email.com", "mdp");
		
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
	}

	private void scenario_4()
	{
	    userService.addUser("lucie.bernard@email.com", "05/04/1997", "Lucie", "mdp789");
	    userService.connectUser("lucie.bernard@email.com", "mdp789");

	    productService.addProduct("Chocolat", 4.0, "20/12/2024");
	    productService.addProduct("Lait", 2.0, "01/01/2025");

		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
	}

	private void scenario_5()
	{
	    userService.addUser("simon.petit@email.com", "11/11/1999", "Simon", "motdepasse");
	    userService.connectUser("simon.petit@email.com", "motdepasse");

	    productService.addProduct("Vin", 12.0, "31/12/2024");
	    productService.addProduct("Fromage", 5.0, "15/02/2025");

		productService.getProduct(productRepository.findAll().stream().findFirst().orElse(null).getId());
	    productService.updateProduct(productRepository.findByNom("Fromage").orElse(null).getId(), "Fromage", 6.0, "15/02/2025");
	    productService.deleteProduct(productRepository.findByNom("Vin").orElse(null).getId());
	}
}
