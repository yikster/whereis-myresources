package com.yikster.aws.resources.core.utils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancing;
import com.amazonaws.services.elasticloadbalancing.AmazonElasticLoadBalancingClientBuilder;
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersRequest;
import com.amazonaws.services.elasticloadbalancingv2.model.DescribeLoadBalancersResult;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest;
import com.amazonaws.services.rds.model.DescribeDBInstancesResult;
import com.amazonaws.services.redshift.AmazonRedshift;
import com.amazonaws.services.redshift.AmazonRedshiftClientBuilder;
import com.amazonaws.services.redshift.model.Cluster;
import com.amazonaws.services.redshift.model.DescribeClustersRequest;
import com.amazonaws.services.redshift.model.DescribeClustersResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.yikster.aws.resources.core.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class AWSClient{

    Logger logger = LoggerFactory.getLogger(AWSClient.class);

    public AWSCredentialsProvider getCredentialsProvider() {
        ProfileCredentialsProvider profileCredentialsProvider = new ProfileCredentialsProvider("default");
        return profileCredentialsProvider;
    }

    public List<Resource> getELB(Regions region, String nextMarker) {
        logger.info("Getting {} ELB list...", region.getName());

        List<Resource> resources = new ArrayList<>();
        com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancing client = com.amazonaws.services.elasticloadbalancingv2.AmazonElasticLoadBalancingClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        DescribeLoadBalancersRequest req = new DescribeLoadBalancersRequest();
        req.withMarker(nextMarker);

        try {
            DescribeLoadBalancersResult result = client.describeLoadBalancers(req);
            result.getLoadBalancers().forEach(elbv2 -> {
                resources.add(AWSResourceUtils.copyToResource(elbv2, region));
            });
            logger.info("{} ELBs are found", resources.size());
            if (!StringUtils.isEmpty(result.getNextMarker())) {
                resources.addAll(getELB(region, result.getNextMarker()));
            }
        } catch (Exception e) {
            logger.error("Error in getting ELBs", e);
        }
        return resources;

    }
    public List<Resource> getELB(Regions region) {
        return getELB(region, null);
    }



    public List<Resource> getCLB(Regions region, String nextMarker) {
        logger.info("Getting {} CLB list...", region.getName());
        List<Resource> resources = new ArrayList<>();

        AmazonElasticLoadBalancing client = AmazonElasticLoadBalancingClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();


        com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersRequest request = new com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersRequest();
        request.withMarker(nextMarker);

        try {
            com.amazonaws.services.elasticloadbalancing.model.DescribeLoadBalancersResult result = client.describeLoadBalancers(request);
            result.getLoadBalancerDescriptions().forEach(elb -> {
                resources.add(AWSResourceUtils.copyToResource(elb, region));
            });
            logger.info("{} CLBs are found", resources.size());
            if (!StringUtils.isEmpty(result.getNextMarker()))
                resources.addAll(getCLB(region, nextMarker));
        } catch (Exception e) {
            logger.error("Error in Getting CLBs", e);
        }

        return resources;
    }
    public List<Resource> getCLB(Regions region) {
        return getCLB(region, null);
    }

    public List<Resource> getEC2s(Regions region, String nextToken) {
        logger.info("Getting {} EC2 list...(nextToken: {})", region.getName(), nextToken);
        List<Resource> resources = new ArrayList<>();
        AmazonEC2 client = AmazonEC2ClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        if(!StringUtils.isEmpty(nextToken))
            request.withNextToken(nextToken);
        try {
            DescribeInstancesResult result = client.describeInstances(request);

            result.getReservations().forEach(reservation -> {
                List<Instance> instances = reservation.getInstances();
                for (int i = 0; i < instances.size(); i++) {
                    resources.add(AWSResourceUtils.copyToResource(instances.get(i), region));
                }
            });

            logger.info("{} EC2 are found", resources.size());
            if (!StringUtils.isEmpty(result.getNextToken()))
                resources.addAll(getEC2s(region, result.getNextToken()));
        } catch (Exception e) {
            logger.error("Error in getting EC2s");
        }

        return resources;
    }
    public List<Resource> getEC2s(Regions region) {
        return getEC2s(region, null);
    }

    public List<Resource> getRedshift(Regions region) {
        return getRedshift(region, null);
    }
    public List<Resource> getRedshift(Regions region, String nextMarker) {
        logger.info("Getting {} Redshift cluster list...", region.getName());
        List<Resource> resources = new ArrayList<>();
        AmazonRedshift client = AmazonRedshiftClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        DescribeClustersRequest request = new DescribeClustersRequest();
        request.setMarker(nextMarker);

        try {
            DescribeClustersResult result = client.describeClusters(request);
            List<Cluster> clusters = result.getClusters();
            clusters.forEach(cluster -> {
                resources.add(AWSResourceUtils.copyToResource(cluster, region));
            });

            logger.info("{} redshift clusters are found", resources.size());

            if (!StringUtils.isEmpty(result.getMarker()))
                resources.addAll(getRedshift(region, result.getMarker()));
        } catch(Exception e) {
            logger.error("Error in getting Redshifts");
        }
        return resources;
    }

    public List<Resource> getBucketList(Regions region) {
        logger.info("Getting {} Bucket list...", region.getName());

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        List<Bucket> result = new ArrayList<>();
        try {
            result = amazonS3.listBuckets();
            logger.info("{} buckets are found", result.size());
        } catch(Exception e) {
            logger.error("Error in getting buckets", e);
        }
        return bucketsToResources(result, region.getName());
    }

    public List<Resource> bucketsToResources(List<Bucket> buckets, String region) {
        List<Resource> resources = new ArrayList<>();
        for(int i=0;i<buckets.size();i++) {
            resources.add(AWSResourceUtils.copyToResource(buckets.get(i), region));
        }
        return resources;
    }


    public List<Resource> getDynamodbTables(Regions region) {
        logger.info("Getting {} DynamoDB Table list...", region.getName());
        List<Resource> resources = new ArrayList<>();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        try {
            ListTablesResult result = client.listTables();

            List<String> tables = result.getTableNames();

            tables.forEach(table -> {
                TableDescription tableDescription = client.describeTable(table).getTable();
                resources.add(AWSResourceUtils.copyToResource(tableDescription, region));
            });
            logger.info("{} DynamoDB Tables are found", resources.size());
        }catch (Exception e) {
            logger.error("Errors in getting dynamodb tables", e);
        }
        return resources;
    }

    public List<Resource> getVPC(Regions region) {
        logger.info("Getting {} VPC list...", region.getName());
        List<Resource> resources = new ArrayList<>();
        AmazonEC2 client = AmazonEC2ClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();

        try {
            DescribeVpcsResult result = client.describeVpcs();
            List<Vpc> vpcs = result.getVpcs();
            for (int i = 0; i < vpcs.size(); i++) {
                resources.add(AWSResourceUtils.copyToResource(vpcs.get(i), region));
            }
            logger.info("{} VPCs are found", resources.size());
        } catch (Exception e) {
            logger.error("Error in getting vpcs", e);
        }
        return resources;
    }


    public List<Resource> getRDS(Regions region) {
        return getRDS(region, null);
    }
    public List<Resource> getRDS(Regions region, String marker) {
        logger.info("Getting {} RDS list...", region.getName());
        List<Resource> resources = new ArrayList<>();
        AmazonRDS client = AmazonRDSClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withRegion(region)
                .build();
        DescribeDBInstancesRequest request = new DescribeDBInstancesRequest();
        request.withMarker(marker);
        try {
            DescribeDBInstancesResult result = client.describeDBInstances(request);

            for (int i = 0; i < result.getDBInstances().size(); i++) {
                resources.add(AWSResourceUtils.copyToResource(result.getDBInstances().get(i), region));
            }
            logger.info("{} RDSs are found", resources.size());
            if (!StringUtils.isEmpty(result.getMarker())) {
                resources.addAll(getRDS(region, result.getMarker()));
            }
        } catch (Exception e) {
            logger.error("Error in getting RDSs", e);
        }
        return resources;
    }

    public List<Resource> getAllResources() {
        AWSClient s3Client = new AWSClient();
        List<Resource> resources = new ArrayList<>();
        for(Regions region : Regions.values()) {
            if(Regions.GovCloud == region || Regions.CN_NORTH_1 == region)
                continue;
            resources.addAll(s3Client.getELB(region));
            resources.addAll(s3Client.getCLB(region));
            resources.addAll(s3Client.getEC2s(region));
            resources.addAll(s3Client.getRedshift(region));
            resources.addAll(s3Client.getBucketList(region));
            resources.addAll(s3Client.getDynamodbTables(region));
            resources.addAll(s3Client.getRDS(region));
            resources.addAll(s3Client.getVPC(region));



        }
        return resources;
    }

}
