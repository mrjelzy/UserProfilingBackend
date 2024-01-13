package com.hai913i.logs;

import java.util.HashMap;
import java.util.Map;

public class UserProfile
{
	private String userId;
    private Map<String, EntryLog> actions; // Clé : Timestamp, Valeur : Log
    private int nbRead;
    private int nbWrite;
    
    public UserProfile()
    {
    	this.nbRead = 0;
        this.nbWrite = 0;
    }

    public UserProfile(String userId) {
        this.userId = userId;
        this.actions = new HashMap<>();
        this.nbRead = 0;
        this.nbWrite = 0;
    }

    public void updateProfileWithLogEntry(String timestamp, String type, String productID)
    {
    	actions.put(timestamp, new EntryLog(userId, type, productID));
    	
    	if(type.equals("read"))
    	{
    		nbRead += 1;
    	}
    	else if (type.equals("write"))
    	{
    		nbWrite += 1;
    	}
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

	public int getNbRead()
	{
		return nbRead;
	}

	public void setNbRead(int nbRead)
	{
		this.nbRead = nbRead;
	}

	public int getNbWrite()
	{
		return nbWrite;
	}

	public void setNbWrite(int nbWrite)
	{
		this.nbWrite = nbWrite;
	}
	
}
