package com.hai913i.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hai913i.GlobalData;
import com.hai913i.models.User;
import com.hai913i.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService
{
	@Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GlobalData globalData;

    public String connectUser(String email, String password) {
	    Optional<User> user = userRepository.findByEmailAndPassword(email, password);
	    if(user.isPresent())
	    {
	    	globalData.setActualUserId(user.get().getId());
	    	// LOGS
	    	// logger.trace("User " + user.get().getId() + " connecté");
	    	
	        System.out.println("Connexion réussie pour : " + user.get().getName());
	        return user.get().getName();
	    } else {
	        // L'utilisateur n'existe pas ou le mot de passe est incorrect
	        System.out.println("Identifiants incorrects !");
	    }
	    
	    return null;
	}
    
    public Boolean addUser(String email, String birthday, String name, String password) {

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
        
        // LOGS
        // logger.trace("Nouvel utilisateur créé : " + newUser.getId());

        // Imprimez un message de confirmation.
        System.out.println("Nouvel utilisateur créé avec succès : " + newUser.toString());
        return true;
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