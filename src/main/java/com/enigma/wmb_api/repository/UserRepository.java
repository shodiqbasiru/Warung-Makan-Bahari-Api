package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, String> {
}
