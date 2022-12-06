package spring.boot.yj.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.boot.yj.entities.Product;
import spring.boot.yj.service.ProductService;


@RestController
@RequestMapping("/product")
public class ProductController {

	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	@GetMapping ("/all")
	public ResponseEntity<List<Product>> getAllProducts() {
		try {
			List<Product> products = new ArrayList<Product>();
			products = productService.getAllProducts();
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}	
			return new ResponseEntity<>(products, HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable ("id") Long id ){
		try {
			Product product = productService.getProductById(id);
			return new ResponseEntity<Product>(product, HttpStatus.OK);

		}catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}


}
