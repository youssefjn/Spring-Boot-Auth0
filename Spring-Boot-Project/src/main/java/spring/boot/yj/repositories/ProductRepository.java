package spring.boot.yj.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
