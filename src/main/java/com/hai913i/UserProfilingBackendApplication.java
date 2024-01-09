package com.hai913i;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hai913i.models.Product;
import com.hai913i.models.User;
import com.hai913i.repositories.ProductRepository;
import com.hai913i.repositories.UserRepository;

@SpringBootApplication
public class UserProfilingBackendApplication implements CommandLineRunner{
	
	@Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private Scanner scanner;

    public static void main(String[] args) {
        SpringApplication.run(UserProfilingBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    	scanner = new Scanner(System.in);

        // Initialisation des données
        initData();

        // Logique CLI
        startCli();
    }

    private void initData() {
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
                
                nameActualUser = connectUser(email, mdp);
                
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
                } while (!addUser(email, birthday, name, password));
                
                System.out.println("");
                System.out.println("============================");
                System.out.println("");
            }
        } while (!choice.equals("x"));
        
        scanner.close();
        System.exit(0);
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
    	        
    	        Product p = getProduct(Long.parseLong(myId));
    	        
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
    	        
    	        addProduct(name, prix, expireDate);
        	}
        	if (choice_2.equals("4"))
        	{
        		System.out.print("ID du produit à supprimer : ");
    	        Long id = Long.parseLong(scanner.nextLine());
    	        
    	        deleteProduct(id);
        	}
        	if (choice_2.equals("5"))
        	{
        		System.out.print("ID du produit à modifier (laisser vide pour ne pas modifier) : ");
    	        Long id = Long.parseLong(scanner.nextLine());
    	        
    	        Product p = getProduct(id);
    	        
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
        	        updateProduct(id, name, prix, expireDate);
    	        }
    	        
        	}
        	
        	System.out.println("");
	        System.out.println("============================");
	        System.out.println("");
        } while (!choice_2.equals("x"));
	}
    
    ////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// USER /////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

	private Boolean addUser(String email, String birthday, String name, String password) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            System.out.println("Un compte avec cet email existe déjà !");
            return false;
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            System.out.println("Le format de la date d'anniversaire est incorrect. Utilisez dd/MM/yyyy.");
            return false;
        }

        // Créez une nouvelle instance de User avec les données fournies.
        User newUser = new User(name, email, birthDate, password); // Assurez-vous que le constructeur de User est correctement défini.

        // Sauvegardez le nouvel utilisateur dans la base de données.
        userRepository.save(newUser);

        // Imprimez un message de confirmation.
        System.out.println("Nouvel utilisateur créé avec succès : " + newUser.toString());
        return true;
    }

	public String connectUser(String email, String password) {
	    Optional<User> user = userRepository.findByEmailAndPassword(email, password);
	    if(user.isPresent()) {
	        // L'utilisateur existe
	        System.out.println("Connexion réussie pour : " + user.get().getName());
	        return user.get().getName();
	    } else {
	        // L'utilisateur n'existe pas ou le mot de passe est incorrect
	        System.out.println("Identifiants incorrects !");
	    }
	    
	    return null;
	}

    ////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// PRODUCTS ///////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
	
	private Boolean addProduct(String nom, Double prix, String expire)
	{
		try {
	        LocalDate expireDate = LocalDate.parse(expire, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

	        // Vérifiez si un produit avec le même ID existe déjà
	        Optional<Product> existingProduct = productRepository.findById(expireDate.toEpochDay()); // Exemple d'ID basé sur la date, à adapter selon votre logique d'ID
	        if (existingProduct.isPresent()) {
	            throw new Exception("Un produit avec cet ID existe déjà !");
	        }

	        // Créez une nouvelle instance de Product
	        Product newProduct = new Product(nom, prix, expireDate);

	        // Sauvegardez le nouveau produit dans la base de données
	        productRepository.save(newProduct);

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
	
	private void deleteProduct(Long id) {
	    try {
	        Product p = productRepository.findById(id)
	                         .orElseThrow(() -> new NoSuchElementException("Aucun produit avec cet ID (" + id + ") trouvé."));
	        productRepository.deleteById(id);
	        System.out.println(p.toString() + " supprimé !");
	    } catch (NoSuchElementException e) {
	        System.out.println(e.getMessage());
	    }
	}
	
	private Product getProduct(Long id) {
		Product product = null;
	    try {
	        product = productRepository.findById(id)
	                          .orElseThrow(() -> new NoSuchElementException("Aucun produit avec cet ID (" + id + ") trouvé."));
	        
	    } catch (NoSuchElementException e) {
	        // Gérez le cas où aucun produit avec l'ID donné n'est trouvé
	        System.out.println(e.getMessage());
	    }
	    
	    return product;
	}
	
	private void updateProduct(Long id, String nom, Double prix, String expire)
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

	        // Affichage d'une confirmation
	        System.out.println("Produit apres mis à jour : " + product.toString());
	    } catch (DateTimeParseException e) {
	        System.out.println("Le format de la date d'expiration est incorrect. Utilisez dd/MM/yyyy.");
	    } catch (NoSuchElementException e) {
	        System.out.println(e.getMessage());
	    }
	}

}
