package com.yikster.aws.resources.core.dao;

import com.yikster.aws.resources.core.model.ResourceCategory;
import org.springframework.data.repository.CrudRepository;

public interface ResourceCategoryRepository extends CrudRepository<ResourceCategory, Long> {
    ResourceCategory findByName(String name);
}
