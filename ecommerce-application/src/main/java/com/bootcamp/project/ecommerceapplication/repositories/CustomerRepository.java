package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.Customer;
import com.bootcamp.project.ecommerceapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {


    List<Customer> findAll();


//    @Query(value = "select contact from customer where user_id = id ",nativeQuery = true)
//    String findContact(@Param("id") long id);

    Customer findById(long id);


}
