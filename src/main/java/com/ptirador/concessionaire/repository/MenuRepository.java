package com.ptirador.concessionaire.repository;

import com.ptirador.concessionaire.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {

    @Query(value = "{ 'typeId' : ?0, 'parentId' : ?2 }")
    List<Menu> getMenusByParent(Integer typeId, Integer roleId, String parentId);
}
