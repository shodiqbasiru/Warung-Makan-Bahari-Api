package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill,String> {
}
