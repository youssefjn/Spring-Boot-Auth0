package spring.boot.yj.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.boot.yj.entities.Address;

public interface AddressRepository extends JpaRepository<Address,Long> {
    
    List<Address> findByUser_Id(Long id);

}
