package com.yikster.aws.resources.core.dao;

import com.yikster.aws.resources.core.model.Resource;
import com.yikster.aws.resources.core.model.ResourceCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ResourceRepository extends PagingAndSortingRepository<Resource, Long> {
    Resource findByName(String name);

    Resource findByResourceCategoryAndResourceIdAndRegion(ResourceCategory resourceCategory, String resourceId, String region);
}
