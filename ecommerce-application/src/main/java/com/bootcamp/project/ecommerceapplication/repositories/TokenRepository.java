package com.bootcamp.project.ecommerceapplication.repositories;

import com.bootcamp.project.ecommerceapplication.domain.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<ConfirmationToken,Long> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);

    public void deleteById(int id);
}
