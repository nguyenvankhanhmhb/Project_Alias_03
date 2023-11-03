package com.example.ProjectAlias.Repository;

import com.example.ProjectAlias.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<ProductEntity, Integer> {

    List<ProductEntity> findByTitleContainingIgnoreCase(String keyword);
    List<ProductEntity> findByCategory(int id);


}
