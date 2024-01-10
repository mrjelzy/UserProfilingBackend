package com.hai913i.logs;

public class EntryLog
{
	String userId;
	String type;
	String productId;
	
	public EntryLog(String userId, String type, String productId)
	{
		super();
		this.userId = userId;
		this.type = type;
		this.productId = productId;
	}

	@Override
	public String toString()
	{
		return "type=" + type + ", productId=" + productId;
	}
	
	
}
