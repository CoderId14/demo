package com.example.demo.Repository;

import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepo<T, ID> extends JpaRepository<T, ID> {
    T findByIdMandatory(ID id) throws ResourceNotFoundException;

    void clear();

    void detach(T entity);
}
