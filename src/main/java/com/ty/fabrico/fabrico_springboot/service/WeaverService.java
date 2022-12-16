package com.ty.fabrico.fabrico_springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ty.fabrico.fabrico_springboot.dao.WeaverDao;
import com.ty.fabrico.fabrico_springboot.dto.Weaver;
import com.ty.fabrico.fabrico_springboot.exception.NoSuchIdFoundException;
import com.ty.fabrico.fabrico_springboot.exception.NoSuchUsernameFoundException;
import com.ty.fabrico.fabrico_springboot.exception.PasswordIncorrectException;
import com.ty.fabrico.fabrico_springboot.util.ResponseStructure;

@Service
public class WeaverService {

	@Autowired
	private WeaverDao weaverDao;

	public ResponseEntity<ResponseStructure<Weaver>> saveWeaver(Weaver weaver) {
		ResponseStructure<Weaver> responseStructure = new ResponseStructure<Weaver>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Saved");
		responseStructure.setData(weaverDao.saveWeaver(weaver));
		ResponseEntity<ResponseStructure<Weaver>> responseEntity = new ResponseEntity<ResponseStructure<Weaver>>(
				responseStructure, HttpStatus.CREATED);
		return responseEntity;
	}

	public ResponseEntity<ResponseStructure<Weaver>> getWeaverById(int weaverid) {
		Optional<Weaver> optional = weaverDao.getWeaverById(weaverid);
		ResponseStructure<Weaver> responseStructure = new ResponseStructure<Weaver>();
		ResponseEntity<ResponseStructure<Weaver>> responseEntity = new ResponseEntity<ResponseStructure<Weaver>>(
				responseStructure, HttpStatus.OK);
		if (optional.isPresent()) {
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Found");
			responseStructure.setData(optional.get());
			return responseEntity;
		} else {
			throw new NoSuchIdFoundException("No Such Id Found");
		}
	}

	public ResponseEntity<ResponseStructure<Weaver>> updateWeaver(Weaver weaver, int weaverid) {
		ResponseStructure<Weaver> responseStructure = new ResponseStructure<Weaver>();
		ResponseEntity<ResponseStructure<Weaver>> responseEntity = new ResponseEntity<ResponseStructure<Weaver>>(
				responseStructure, HttpStatus.OK);
		Optional<Weaver> optional = weaverDao.getWeaverById(weaverid);
		if (optional.isPresent()) {
			weaver.setWeaverid(weaverid);
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Updated");
			responseStructure.setData(weaverDao.updateWeaver(weaver));
			return responseEntity;
		} else {
			throw new NoSuchIdFoundException("Unable to Update No Such Id Found");
		}
	}
	
	public ResponseEntity<ResponseStructure<Weaver>> deleteWeaver(int weaverid) {
		ResponseStructure<Weaver> responseStructure = new ResponseStructure<Weaver>();
		ResponseEntity<ResponseStructure<Weaver>> responseEntity = new ResponseEntity<ResponseStructure<Weaver>>(
				responseStructure, HttpStatus.OK);
		Optional<Weaver> optional = weaverDao.getWeaverById(weaverid);
		if(optional.isPresent()) {
			weaverDao.deleteWeaver(optional.get());
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Deleted");
			responseStructure.setData(optional.get());
			return responseEntity;
		} else {
			throw new NoSuchIdFoundException("No Such Id Found Unable to Delete");
		}	
	}
	
	public ResponseEntity<ResponseStructure<Weaver>> weaverLogin(Weaver weaver){
		ResponseStructure<Weaver> responseStructure = new ResponseStructure<Weaver>();
		ResponseEntity<ResponseStructure<Weaver>> responseEntity = new ResponseEntity<ResponseStructure<Weaver>>(
				responseStructure, HttpStatus.OK);
		Weaver weaver2=weaverDao.getWeaverByName(weaver.getUsername());
		if(weaver2!=null) {
			if(weaver.getPassword().equals(weaver2.getPassword())) {
				responseStructure.setStatus(HttpStatus.OK.value());
				responseStructure.setMessage("Login Successfull");
				responseStructure.setData(weaver2);
				return responseEntity;
			}else {
				throw new PasswordIncorrectException();
			}
		}else {
			throw new NoSuchUsernameFoundException();
		}
	}

}
