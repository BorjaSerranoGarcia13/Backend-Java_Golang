package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findProductsByName(String name);

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findByIds(@Param("ids") List<Integer> ids);

    Optional<Product> findByReference(@Param("reference") String reference);

    @Query("SELECT p FROM Product p WHERE p.reference LIKE %:reference%")
    List<Product> findProductsByReference(@Param("reference") String reference);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) = LOWER(:name) OR p.reference = :reference")
    Optional<Product> findByNameOrReference(String name, String reference);

    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
    void markAsDeleted(@Param("id") Integer id);

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id = :id OR p.name = :name")
    List<Product> findByIdOrName(@Param("id") Integer id, @Param("name") String name);

}
