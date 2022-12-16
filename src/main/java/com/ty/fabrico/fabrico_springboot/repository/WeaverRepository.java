package com.ty.fabrico.fabrico_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ty.fabrico.fabrico_springboot.dto.Weaver;

public interface WeaverRepository extends JpaRepository<Weaver, Integer>{
	
	public Weaver getWeaverByUsername(String username);

}
