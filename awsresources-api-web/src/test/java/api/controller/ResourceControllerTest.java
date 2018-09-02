package api.controller;

import com.yikster.aws.resources.api.Application;
import com.yikster.aws.resources.core.dao.ResourceCategoryRepository;
import com.yikster.aws.resources.core.dao.ResourceRepository;
import com.yikster.aws.resources.core.model.Resource;
import com.yikster.aws.resources.core.model.ResourceCategory;
import net.minidev.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by TDERVILY on 02/03/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class
ResourceControllerTest extends BaseTest {

    private Resource resource;

    @Autowired
    ResourceCategoryRepository resourceCategoryRepository;
    @Autowired
    ResourceRepository resourceRepository;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        ResourceCategory elbCategory = new ResourceCategory(2, "ELB");
        resourceCategoryRepository.save(elbCategory);
        resourceRepository.save(new Resource("Labrador chocolate", 2L, elbCategory));
        resourceRepository.save(new Resource("Golden retriever", 3L, elbCategory));
        resource = resourceRepository.findById(3L).get();
    }

    @Test
    public void testSync() throws Exception {
        mockMvc.perform(post("/resource/sync")
                .contentType(contentType))
                .andExpect(status().isOk());

    }
    @Test
    public void testCRUD() throws Exception {
        test1GetResourceDetail();
//        test2AddResource();
        test3UpdateResource();
        test4getAll();
        test5DeleteResource();
    }

    private void test1GetResourceDetail() throws Exception {
        mockMvc.perform(get("/resource/" + resource.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(resource.getId().intValue())))
                .andExpect(jsonPath("$.name", is(resource.getName())))
                .andExpect(jsonPath("$.resourceCategory.id", is(resource.getResourceCategory().getId().intValue())));
    }

    private void test2AddResource() throws Exception {
        mockMvc.perform(post("/resource")
                .content(json(new Resource("red dog", 10L, new ResourceCategory(2, "ELB"))))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(10)));
    }

    private void test3UpdateResource() throws Exception {
        Resource resource = resourceRepository.findById(1L).get();
        Resource resourceUpdate = new Resource(resource.getName() + "updated", resource.getId(), resource.getResourceCategory());
        resourceUpdate.setId(resource.getId());

        mockMvc.perform(put("/resource")
                .content(json(resourceUpdate))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(resource.getName() + "updated")));
    }

    private void test4getAll() throws Exception {
        mockMvc.perform(get("/resource/list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", isA(JSONArray.class)));
    }

    private void test5DeleteResource() throws Exception {
        mockMvc.perform(delete("/resource/" + resource.getId())
                .contentType(contentType))
                .andExpect(status().isOk());
    }

}
