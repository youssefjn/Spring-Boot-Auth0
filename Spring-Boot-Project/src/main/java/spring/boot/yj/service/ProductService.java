package spring.boot.yj.service;

import java.util.List;
import org.springframework.stereotype.Service;
import spring.boot.yj.entities.Product;
import spring.boot.yj.repositories.ProductRepository;


@Service
public class ProductService {

	private ProductRepository productRepository;


	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;

	}

	public List<Product> getAllProducts(){
		return productRepository.findAll();
	}

	public Product getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(()-> 
		new IllegalStateException("Product with id "+ id + " does not exist"));
		return product;
	}

}
