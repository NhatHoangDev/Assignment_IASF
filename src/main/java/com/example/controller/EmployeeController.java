package com.example.controller;

import com.example.models.Employee;
import com.example.models.ResponseObject;
import com.example.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/Employees")
public class EmployeeController {


    @Autowired
    private IEmployeeRepository repository;

    @GetMapping("")
    List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id) {
        Optional<Employee> foundEmployee = repository.findById(id);
        return foundEmployee.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query employee successfully", foundEmployee)
        ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("false", "Cannot find employee with id: " + id, "")
                );
    }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertEmployee(@RequestBody Employee newEmployee) {
        List<Employee> foundEmployees = repository.findByName(newEmployee.getName().trim());
        if (foundEmployees.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("falied", "Employee name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert employee successfully", repository.save(newEmployee))
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setEmail(newEmployee.getEmail());
                    employee.setLocation(newEmployee.getLocation());
                    return repository.save(employee);
                }).orElseGet(() -> {
                    //newProduct.setId(id);
                    return repository.save(newEmployee);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "update employee successfully", updatedEmployee)
        );
        //return new ResponseEntity<>(new ResponseObject("ok", "update", updatedProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteEmployee(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Ok", "delete employee successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find employee to delete", "")
        );
    }
}
