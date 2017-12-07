package api.controller;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.yikster.aws.resources.core.utils.AWSClient;
import com.yikster.aws.resources.core.model.Resource;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AWSClientTest extends BaseTest {

    @Before
    public void prepare() {
        AmazonS3 client = AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider())
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();

        client.listBuckets().forEach(bucket->{
            System.out.println(bucket.toString());
        });


    }
    @Test
    public void testGetAllResources() throws Exception {
        AWSClient awsClient = new AWSClient();
        List<Resource> resources = awsClient.getAllResources();
        printResources(resources);
    }

    @Test
    public void testGetEC2withToken() throws Exception {
        AWSClient awsClient = new AWSClient();
        List<Resource> resources = awsClient.getEC2s(Regions.AP_NORTHEAST_2);
        printResources(resources);
        System.out.println(resources.size());
    }




    public void printResources(List<Resource> resources) {
        resources.forEach(resource -> {
            System.out.println(resource.toString());
        });
    }




}
