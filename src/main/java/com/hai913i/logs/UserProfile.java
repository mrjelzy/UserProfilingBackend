package com.hai913i.logs;

import java.util.HashMap;
import java.util.Map;

public class UserProfile
{
	private String userId;
    private Map<String, EntryLog> actions; // Clé : Timestamp, Valeur : Log

    public UserProfile(String userId) {
        this.userId = userId;
        this.actions = new HashMap<>();
    }

    public void updateProfileWithLogEntry(String timestamp, String type, String productID)
    {
    	actions.put(timestamp, new EntryLog(userId, type, productID));
    }
    
    public String toString()
	{
		return "User : " + userId + "\br" + printActions();
	}

	private String printActions()
	{
		String buff = "";
		
		for (Map.Entry<String, EntryLog> entry : actions.entrySet())
		{
			buff += entry.getKey() + " : " + entry.getValue().toString() + "\br";
		}
		return buff;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Map<String, EntryLog> getActions()
	{
		return actions;
	}

	public void setActions(Map<String, EntryLog> actions)
	{
		this.actions = actions;
	}
	
	
}
