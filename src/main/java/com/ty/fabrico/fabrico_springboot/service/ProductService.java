package com.ty.fabrico.fabrico_springboot.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ty.fabrico.fabrico_springboot.dao.CustomerDao;
import com.ty.fabrico.fabrico_springboot.dao.ProductDao;
import com.ty.fabrico.fabrico_springboot.dao.WeaverDao;
import com.ty.fabrico.fabrico_springboot.dto.Cart;
import com.ty.fabrico.fabrico_springboot.dto.Customer;
import com.ty.fabrico.fabrico_springboot.dto.Product;
import com.ty.fabrico.fabrico_springboot.dto.Weaver;
import com.ty.fabrico.fabrico_springboot.exception.NoSuchIdFoundException;
import com.ty.fabrico.fabrico_springboot.util.ResponseStructure;

@Service
public class ProductService {

	@Autowired
	ProductDao productDao;

	@Autowired
	WeaverDao weaverDao;
	
	@Autowired
	CustomerDao customerDao;
	
	@Autowired
	CartService cartService;

	public ResponseEntity<ResponseStructure<Product>> saveProductForWeaver(Product product, int weaverid) {
		ResponseStructure<Product> responseStructure = new ResponseStructure<Product>();
		ResponseEntity<ResponseStructure<Product>> responseEntity;
		Optional<Weaver> optional = weaverDao.getWeaverById(weaverid);
		Weaver weaver;
		if (optional.isPresent()) {
			weaver = optional.get();
		} else {
			throw new NoSuchIdFoundException("No Such Id Found For Weaver");
		}
		if (weaver != null) {
			List<Product> products = weaver.getProduct();
			products.add(product);
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("Product Saved To Weaver");
			responseStructure.setData(productDao.saveProduct(product));
			weaverDao.updateWeaver(weaver);
		}
		return responseEntity = new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.CREATED);
	}
	
	public ResponseEntity<ResponseStructure<Product>> saveProductForCustomer(Product product, int customerid) {
		ResponseStructure<Product> responseStructure = new ResponseStructure<Product>();
		ResponseEntity<ResponseStructure<Product>> responseEntity;
		Optional<Customer> optional =customerDao.getCustomerById(customerid);
		Customer customer;
		if (optional.isPresent()) {
			customer = optional.get();
		} else {
			throw new NoSuchIdFoundException("No Such Id Found For Customer");
		}
		if (customer != null) {
			Cart cart=customer.getCart();
			List<Product> products = cart.getProduct();
			products.add(product);
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("Product Saved To Cart");
			responseStructure.setData(productDao.saveProduct(product));
			cartService.updateCart(cart, cart.getCartId());
			customerDao.updateCustomer(customer);
		}
		return responseEntity = new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.CREATED);
	}

	public ResponseEntity<ResponseStructure<Product>> getProductById(int productid) {
		ResponseStructure<Product> responseStructure = new ResponseStructure<Product>();
		ResponseEntity<ResponseStructure<Product>> responseEntity;
		Optional<Product> optional = productDao.getProductById(productid);
		if (optional.isPresent()) {
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Product Found");
			responseStructure.setData(optional.get());
			return responseEntity = new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
		} else
			throw new NoSuchIdFoundException("Product Id Not Found");
	}

	public ResponseEntity<ResponseStructure<Product>> deleteProduct(int productid) {
		ResponseStructure<Product> responseStructure = new ResponseStructure<Product>();
		ResponseEntity<ResponseStructure<Product>> responseEntity;
		Optional<Product> optional = productDao.getProductById(productid);
		if (optional.isPresent()) {
			productDao.deleteProduct(optional.get());
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Deleted");
			responseStructure.setData(optional.get());
			return responseEntity = new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
		} else
			throw new NoSuchIdFoundException("No Such Id Found Unable To Delete");
	}

	public ResponseEntity<ResponseStructure<Product>> updateProduct(Product product, int productid) {
		Optional<Product> optional = productDao.getProductById(productid);
		Product product2;
		ResponseStructure<Product> responseStructure = new ResponseStructure<Product>();
		ResponseEntity<ResponseStructure<Product>> responseEntity;
		if(optional.isPresent()) {
			product2=optional.get();
		}else {
			product2=null;
		}
		if (product2 != null) {
			product.setPId(productid);
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("Updated");
			responseStructure.setData(productDao.updateProduct(product));
			return responseEntity = new ResponseEntity<ResponseStructure<Product>>(responseStructure, HttpStatus.OK);
		}else
			throw new NoSuchIdFoundException("No Such Id Found Unable To Update");
	}

}
