package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.MTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<MTable,String> {
}
