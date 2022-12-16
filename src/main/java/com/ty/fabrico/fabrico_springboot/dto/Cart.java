package com.ty.fabrico.fabrico_springboot.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cartId;
	@NotNull
	private String cartName;
	@NotNull
	private long phone;
	@NotNull
	private double totalcost;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<Product> product;

}
