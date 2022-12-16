package com.ty.fabrico.fabrico_springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ty.fabrico.fabrico_springboot.dao.CustomerDao;
import com.ty.fabrico.fabrico_springboot.dto.Customer;
import com.ty.fabrico.fabrico_springboot.exception.NoSuchIdFoundException;
import com.ty.fabrico.fabrico_springboot.exception.NoSuchUsernameFoundException;
import com.ty.fabrico.fabrico_springboot.exception.PasswordIncorrectException;
import com.ty.fabrico.fabrico_springboot.util.ResponseStructure;

@Service
public class CustomerService {

	@Autowired
	private CustomerDao customerDao;
	
	public ResponseEntity<ResponseStructure<Customer>> saveCustomer(Customer customer) {
		ResponseStructure<Customer> responseStructure = new ResponseStructure<Customer>();
		ResponseEntity<ResponseStructure<Customer>> responseEntity;
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Saved");
		responseStructure.setData(customerDao.saveCustomer(customer));
		return responseEntity = new ResponseEntity<ResponseStructure<Customer>>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<Customer>> updateCustomer(Customer customer, int customerId) {
		ResponseStructure<Customer> responseStructure = new ResponseStructure<Customer>();
		ResponseEntity<ResponseStructure<Customer>> responseEntity;
		Optional<Customer> optional = customerDao.getCustomerById(customerId);
		if (optional.isPresent()) {
			customer.setCustomerId(customerId);
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Updated");
			responseStructure.setData(customerDao.updateCustomer(customer));
			return responseEntity = new ResponseEntity<ResponseStructure<Customer>>(responseStructure, HttpStatus.OK);
		} else
			throw new NoSuchIdFoundException("No Id Found to Update");
	}

	public ResponseEntity<ResponseStructure<Customer>> getCustomerById(int customerId) {
		ResponseStructure<Customer> responseStructure = new ResponseStructure<Customer>();
		ResponseEntity<ResponseStructure<Customer>> responseEntity;
		Optional<Customer> optional = customerDao.getCustomerById(customerId);
		if (optional.isPresent()) {
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Found");
			responseStructure.setData(optional.get());
			return responseEntity = new ResponseEntity<ResponseStructure<Customer>>(responseStructure, HttpStatus.OK);
		} else
			throw new NoSuchIdFoundException("No such Id is Found");

		}
	

	public ResponseEntity<ResponseStructure<Customer>> customerLogin(Customer customer) {

		ResponseStructure<Customer> responseStructure = new ResponseStructure<Customer>();
		ResponseEntity<ResponseStructure<Customer>> responseEntity;
		Customer customer1 = customerDao.getCustomerByEmail(customer.getEmail());
		if (customer1 != null) {
			if (customer1.getPassword().equals(customer.getPassword())) {
				responseStructure.setStatus(HttpStatus.OK.value());
				responseStructure.setMessage("Login Successful as Customer");
				responseStructure.setData(customer1);
				return responseEntity = new ResponseEntity<ResponseStructure<Customer>>(responseStructure,
						HttpStatus.OK);
			} else
				throw new PasswordIncorrectException("Invalid Password");
		} else
			throw new NoSuchUsernameFoundException("Email Not Found");

	}

	public ResponseEntity<ResponseStructure<Customer>> deleteCustomer(int customerId) {
		ResponseStructure<Customer> responseStructure = new ResponseStructure<Customer>();
		ResponseEntity<ResponseStructure<Customer>> responseEntity;
		Optional<Customer> optional = customerDao.getCustomerById(customerId);
		if (optional.isPresent()) {
			customerDao.deleteCustomer(optional.get());
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Deleted");
			responseStructure.setData(optional.get());
			return responseEntity = new ResponseEntity<ResponseStructure<Customer>>(responseStructure, HttpStatus.OK);
		} else
			throw new NoSuchIdFoundException("No Id found to Delete");
	}
}