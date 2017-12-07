package com.yikster.aws.resources.api.controller;

import com.yikster.aws.resources.core.utils.AWSClient;
import com.yikster.aws.resources.core.dao.ResourceCategoryRepository;
import com.yikster.aws.resources.core.dao.ResourceRepository;
import com.yikster.aws.resources.core.model.Resource;
import com.yikster.aws.resources.core.model.ResourceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by TDERVILY on 01/03/2017.
 */
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceCategoryRepository resourceCategoryRepository;

    @Autowired
    AWSClient awsClient;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Resource resource) {
        return saveResource(resource);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Resource resource) {
        if (resourceRepository.findOne(resource.getId()) != null) {
            return saveResource(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<Page> list(Pageable pageable) {
        return new ResponseEntity<>(this.resourceRepository.findAll(pageable), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/{resourceId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getById(@PathVariable Long resourceId) {
        return  new ResponseEntity<>(resourceRepository.findOne(resourceId), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "/{resourceId}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long resourceId) {
        resourceRepository.delete(resourceId);
        return ResponseEntity.ok().build();
    }


    @CrossOrigin
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public ResponseEntity sync() {
        List<Resource> list = awsClient.getAllResources();
        list.forEach(resource->{
            System.out.println(resource.toString());
        });
        int updated = 0;
        int inserted = 0;
        for(int i=0; i<list.size(); i++) {
            Resource resource = list.get(i);
            Resource existRecord = resourceRepository.findByResourceCategoryAndResourceIdAndRegion(
                    resource.getResourceCategory(), resource.getResourceId(), resource.getRegion());
            if( null != existRecord) {
                resource.setId(existRecord.getId());
                updated ++;
            } else {
                inserted ++;

            }
            resourceRepository.save(resource);
        }
        String body = String.format("{ updated:%d, inserted:%d }", updated, inserted);
        //resourceRepository.save(list);
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<?> saveResource(Resource resource) {
        System.out.println("Call for save");
        System.out.println(resource.toString());
        System.out.println(resource.getResourceCategory().toString());
        // Fetch pet category from id

        ResourceCategory resourceCategory =  resourceCategoryRepository.findByName(resource.getResourceCategory().getName());

        if (resourceCategory == null)
            return ResponseEntity.noContent().build();

        // Save pet
        resource.setResourceCategory(resourceCategory);
        return new ResponseEntity<>(resourceRepository.save(resource), HttpStatus.OK);
    }
}
