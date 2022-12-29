package com.example.repository;

import com.example.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
    //void updateProduct(Long id, Product newProduct);
    @Query("SELECT u FROM Employee u WHERE u.name = :name")
    List<Employee>  findByName(@Param("name") String name);
}
