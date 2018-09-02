import com.amazonaws.regions.Regions;
import com.yikster.aws.resources.core.AWSResourceApplication;
import com.yikster.aws.resources.core.dao.ResourceCategoryRepository;
import com.yikster.aws.resources.core.dao.ResourceRepository;
import com.yikster.aws.resources.core.model.Resource;
import com.yikster.aws.resources.core.model.ResourceCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AWSResourceApplication.class)
@DataJpaTest
public class ResourceRepositoryTest {
    Logger logger = LoggerFactory.getLogger(ResourceRepositoryTest.class);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private ResourceCategoryRepository resourceCategoryRepository;

    public static final String ELB_TEST = "testElb";

    @Test
    public void getOneResource() throws Exception {
        assertNotNull(resourceRepository.findAll());
        resourceRepository.findAll().forEach(resource->{
            Resource one = resourceRepository.findById(resource.getId()).get();
            logger.debug(one.toString());
            System.out.println(one.toString());

            assertNotNull(one);
        });

    }

    @Test
    public void createResource() throws Exception {
        Optional<ResourceCategory> elb =
                StreamSupport.stream(resourceCategoryRepository.findAll().spliterator(), false).
                        filter(resourceCategory -> resourceCategory.getName().equals("ELB")).findFirst();
        assertNotNull(elb.get());
        Resource resource = new Resource();
        resource.setName("ELB_NAME");
        resource.setResourceId("ELB_RID");
        resource.setRegion(Regions.AP_NORTHEAST_2.toString());
        resource.setResourceCategory(elb.get());
        entityManager.persist(resource);
        assertNotNull(resourceRepository.findByResourceCategoryAndResourceIdAndRegion(resource.getResourceCategory(), resource.getResourceId(), resource.getRegion()));

    }

    @Test
    public void updateResource() throws Exception {
        Resource one = resourceRepository.findById(1L).get();
        assertNotNull(one);
        one.setName(one.getName() + "updated");
        resourceRepository.save(one);
        Resource updated = resourceRepository.findById(1L).get();
        assertEquals(one.getName(), updated.getName());
    }

    @Test
    public void deleteResource() throws Exception {
        resourceRepository.deleteById(1L);
        assertFalse(resourceRepository.findById(1L).isPresent());
    }

    @Test
    public void getByPage() throws Exception {
        Page<Resource> pageOne = resourceRepository.findAll(new PageRequest(1, 1));
        assertThat(pageOne.getTotalElements()).isGreaterThan(0);
    }
}
