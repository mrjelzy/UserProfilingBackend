package com.hai913i.models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nom;
	private double prix;
	private LocalDate dateExpiration;
	
	public Product() {}
	
	public Product(String nom, double prix, LocalDate dateExpiration)
	{
		super();
		this.nom = nom;
		this.prix = prix;
		this.dateExpiration = dateExpiration;
	}
	public long getId()
	{
		return id;
	}
	public String getNom()
	{
		return nom;
	}
	public void setNom(String nom)
	{
		this.nom = nom;
	}
	public double getPrix()
	{
		return prix;
	}
	public void setPrix(double prix)
	{
		this.prix = prix;
	}
	public LocalDate getDateExpiration()
	{
		return dateExpiration;
	}
	public void setDateExpiration(LocalDate dateExpiration)
	{
		this.dateExpiration = dateExpiration;
	}

	@Override
	public String toString()
	{
		return "Product [id=" + id + ", nom=" + nom + ", prix=" + prix + ", dateExpiration=" + dateExpiration + "]";
	}
	
	
}
