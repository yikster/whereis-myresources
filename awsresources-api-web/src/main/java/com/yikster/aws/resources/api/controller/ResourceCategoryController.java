package com.yikster.aws.resources.api.controller;

import com.yikster.aws.resources.core.dao.ResourceCategoryRepository;
import com.yikster.aws.resources.core.model.ResourceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by TDERVILY on 06/03/2017.
 */
@RestController
@RequestMapping("/resourcecategory")
public class ResourceCategoryController {

    @Autowired
    private ResourceCategoryRepository resourceCategoryRepository;

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<Iterable<ResourceCategory>> list() {
            System.out.println("HERE");
            resourceCategoryRepository.findAll().forEach(resourceCategory -> {
                System.out.println(resourceCategory.getName());
            });
        return new ResponseEntity<>(resourceCategoryRepository.findAll(), HttpStatus.OK);
    }
}
