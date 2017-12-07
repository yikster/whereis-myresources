package com.yikster.aws.resources.core.utils;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Vpc;
import com.amazonaws.services.elasticloadbalancing.model.LoadBalancerDescription;
import com.amazonaws.services.elasticloadbalancingv2.model.LoadBalancer;
import com.amazonaws.services.rds.model.DBInstance;
import com.amazonaws.services.rds.model.Subnet;
import com.amazonaws.services.redshift.model.Cluster;
import com.amazonaws.services.s3.model.Bucket;
import com.yikster.aws.resources.core.model.AWSEndpoint;
import com.yikster.aws.resources.core.model.AWSTag;
import com.yikster.aws.resources.core.model.Resource;
import com.yikster.aws.resources.core.model.ResourceCategory;

import java.util.List;

public class AWSResourceUtils {
    public static Resource copyToResource(Instance instance, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(6, "EC2"));
        resource.setName(getNameFromTag(instance.getTags()));
        resource.setCreatedAt(instance.getLaunchTime());
        resource.setResourceId(instance.getInstanceId());
        resource.setType(instance.getInstanceType());
        resource.setImageId(instance.getImageId());
        resource.setEndpoint(instance.getPublicDnsName());
        resource.setVpcId(instance.getVpcId());
        resource.setStatus(instance.getState().getName());
        resource.setIp(instance.getPublicIpAddress() + "/" + instance.getPrivateIpAddress());
        resource.setRegion(region.getName());
        return resource;
    }
    public static Resource copyToResource(Bucket bucket, String region) {
        Resource resource = new Resource();
        resource.setCreatedAt(bucket.getCreationDate());
        resource.setEndpoint(bucket.getName());
        resource.setRegion(region);
        resource.setResourceId(bucket.getName());
        resource.setName(bucket.getName());
        resource.setResourceCategory(new ResourceCategory(1, "S3"));
        return resource;
    }

    public static Resource copyToResource(TableDescription tableDescription, Regions region) {
        Resource resource = new Resource();
        resource.setCreatedAt(tableDescription.getCreationDateTime());
        resource.setEndpoint(tableDescription.getTableArn());
        resource.setName(tableDescription.getTableName());
        resource.setRegion(region.getName());
        resource.setResourceId(region.getName());
        resource.setResourceCategory(new ResourceCategory(2, "DDB"));
        resource.setStatus(tableDescription.getTableStatus());
        resource.setType(getTypeFromDDBTable(tableDescription));
        return resource;
    }

    /**
     * Constructor for VP
     *
     * @param vpc
     * @param region
     */
    public static Resource copyToResource(Vpc vpc, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(3, "VPC"));
        resource.setName(getNameFromTag(vpc.getTags()));
        resource.setResourceId(vpc.getVpcId());
        resource.setVpcId(vpc.getVpcId());
        resource.setIp(vpc.getCidrBlock());
        resource.setStatus(vpc.getState());
        resource.setType(vpc.getIsDefault() ? "Default" : "");
        resource.setRegion(region.getName());
        return resource;
    }

    /**
     * Constructor for RDS
     *
     * @param dbInstance
     * @param region
     */
    public static Resource copyToResource(DBInstance dbInstance, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(4, "RDS"));
        resource.setName(dbInstance.getDBName());
        resource.setCreatedAt(dbInstance.getInstanceCreateTime());
        resource.setResourceId(dbInstance.getDbiResourceId());
        resource.setType(dbInstance.getDBInstanceClass());
        resource.setImageId(dbInstance.getEngine() + "/v" + dbInstance.getEngineVersion());
        resource.setEndpoint(dbInstance.getEndpoint().getAddress() + ":" + dbInstance.getEndpoint().getPort());
        resource.setVpcId(dbInstance.getDBSubnetGroup().getVpcId());
        resource.setSubnetId(subnetsToString(dbInstance.getDBSubnetGroup().getSubnets()));
        resource.setStatus(dbInstance.getDBInstanceStatus());
        resource.setRegion(region.getName());
        return resource;
    }

    /**
     * Constructor for Redshift
     *
     * @param cluster
     */
    public static Resource copyToResource(Cluster cluster, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(5, "REDSHIFT"));
        resource.setName(getNameFromTag(cluster.getTags()));
        resource.setCreatedAt(cluster.getClusterCreateTime());
        resource.setResourceId(cluster.getClusterIdentifier());
        resource.setType(cluster.getNodeType());
        resource.setImageId("N/A");
        resource.setEndpoint(AWSEndpoint.getEndpointUrl(cluster.getEndpoint()));
        resource.setVpcId(cluster.getVpcId());
        resource.setRegion(region.getName());
        //subnetId
        resource.setStatus(cluster.getClusterStatus());
        resource.setRelatedResources(cluster.getNumberOfNodes());

        return resource;
    }


    public static Resource copyToResource(LoadBalancerDescription elb, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(7, "ELB"));
        resource.setName(elb.getLoadBalancerName());
        resource.setCreatedAt(elb.getCreatedTime());
        resource.setResourceId(elb.getLoadBalancerName());
        resource.setType("CLB");
        resource.setEndpoint(elb.getDNSName());
        resource.setVpcId(elb.getVPCId());
        resource.setSubnetId(subnetsToString(elb.getSubnets()));
        resource.setRegion(region.getName());
        return resource;
    }

    public static Resource copyToResource(LoadBalancer elbv2, Regions region) {
        Resource resource = new Resource();
        resource.setResourceCategory(new ResourceCategory(8, "ALB"));
        resource.setName(elbv2.getLoadBalancerName());
        resource.setCreatedAt(elbv2.getCreatedTime());
        resource.setResourceId(elbv2.getLoadBalancerArn());
        resource.setType(elbv2.getType());
        resource.setEndpoint(elbv2.getDNSName());
        resource.setVpcId(elbv2.getVpcId());
        resource.setStatus(elbv2.getState().toString());
        resource.setRegion(region.getName());
        return resource;
    }

    private static String subnetsToString(List subnets) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < subnets.size(); i++) {
            if (i > 0) sb.append(",");

            if (subnets.get(i) instanceof Subnet)
                sb.append(((Subnet) subnets.get(i)).getSubnetIdentifier());
            else if (subnets.get(i) instanceof com.amazonaws.services.ec2.model.Subnet)
                sb.append(((com.amazonaws.services.ec2.model.Subnet) subnets.get(i)).getSubnetId());
        }
        return sb.toString();
    }

    public static String getEndpointUrl(Object o) {
        return AWSEndpoint.getEndpointUrl(o);
    }

    public static String getNameFromTag(List tags) {


        for (int i = 0; i < tags.size(); i++) {
            AWSTag tag = AWSTag.GetAWSTag(tags.get(i));
            if ("Name".equals(tag.getKey()))
                return tag.getValue();

        }
        return "NoName";
    }

    public static String getTypeFromDDBTable(TableDescription tableDescription) {
        return String.format("%d WCU, %d RCU", tableDescription.getProvisionedThroughput().getWriteCapacityUnits()
                , tableDescription.getProvisionedThroughput().getReadCapacityUnits());
    }
}
