package com.hai913i.logs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserProfileBuilder
{
	public static void profileBuilder() throws FileNotFoundException, IOException
	{
        ObjectMapper mapper = new ObjectMapper();
        String logFilePath = "logs/user-trace.json";
        Map<String, UserProfile> userProfiles = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(logFilePath)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                JsonNode logEntry = mapper.readTree(line);

                // Vérifiez si le niveau de log est TRACE
                if ("TRACE".equals(logEntry.get("level").asText()))
                {
                    String message = logEntry.get("message").asText();
                    String timestamp = logEntry.get("@timestamp").asText();
                    String userId = extractUserId(message);
                    String type = extractType(message);
                    String productId = extractProductId(message);

                    // Mise à jour ou création du profil utilisateur
                    UserProfile userProfile = userProfiles.getOrDefault(userId, new UserProfile(userId));
                    userProfile.updateProfileWithLogEntry(timestamp, type, productId);
                    userProfiles.put(userId, userProfile);
                }
            }
        }

        for (Map.Entry<String, UserProfile> entry : userProfiles.entrySet())
        {
        	System.out.println("ID utilisateur : " + entry.getKey());
        	
        	for (Map.Entry<String, EntryLog> log : entry.getValue().getActions().entrySet())
        	{
        		System.out.println("- " + log.getKey() + " -> " + log.getValue().toString());
        	}       	
        }
    }

    private static String extractProductId(String message)
	{
    	String[] parts = message.split(":");
	    
	    if (parts.length > 2)
	    {
	        return parts[2]; // L'ID du produit est le troisieme élément
	    }
	    return ""; //  Retourne une chaîne vide si l'ID du produit n'est pas trouvé
	}

	private static String extractType(String message)
	{
    	String[] parts = message.split(":");
	    
	    if (parts.length > 1)
	    {
	        return parts[1]; // Le type d'action est le deuxieme élément
	    }
	    return ""; // Retourne une chaîne vide si l'action n'est pas trouvé
	}

	// Méthode pour extraire l'ID utilisateur à partir du message log
	private static String extractUserId(String logMessage)
	{
	    String[] parts = logMessage.split(":");
	    
	    if (parts.length > 0)
	    {
	        return parts[0]; // L'ID de l'utilisateur est le premier élément
	    }
	    return ""; // Retourne une chaîne vide si l'ID de l'utilisateur n'est pas trouvé
	}

}
