package com.ty.fabrico.fabrico_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ty.fabrico.fabrico_springboot.dto.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{

	public Customer getCustomerByEmail(String email);
	
	@Query(value =  "SELECT c from Customer c WHERE c.cart.cartId=?1")
	Customer getCustomerByCartId(int cartid);
}








