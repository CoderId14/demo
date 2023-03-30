package com.example.demo.Repository;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepo<T, ID> {
    @PersistenceContext
    public EntityManager em;
    public JPAQueryFactory queryFactory;

    public BaseImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public T findByIdMandatory(ID id) throws ResourceNotFoundException {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Entity [%s]", getDomainClass().getSimpleName()), "id", id));
    }
    @Override
    public void clear() {
        em.clear();
    }
    @Override
    public void detach(T entity) {
        em.detach(entity);
    }
}
