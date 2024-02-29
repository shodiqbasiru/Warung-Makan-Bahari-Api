package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,String>, JpaSpecificationExecutor<Menu> {
}
