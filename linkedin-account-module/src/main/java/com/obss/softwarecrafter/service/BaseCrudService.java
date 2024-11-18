package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.jpa.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public abstract class BaseCrudService<T extends BaseEntity, R extends JpaRepository<T, UUID>> {
    protected abstract String getEntityName();
    protected abstract R getRepository();

    public T create(T entity){
        return getRepository().save(entity);
    }

    public T getById(UUID id){
        return getRepository().findById(id).orElseThrow();
    }

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public void deleteById(UUID id){
        getRepository().deleteById(id);
    }

    public void deleteByIdList(List<UUID> idList) {
        idList.forEach(this::deleteById);
    }

    public T update(T entity){
        if(!getRepository().existsById(entity.getId())){
            throw new NoSuchElementException("No value present");
        }
        return getRepository().save(entity);
    }
}
