package com.yikster.aws.resources.core.model;

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

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESOURCE")
    @SequenceGenerator(sequenceName = "SEQ_RESOURCE", allocationSize = 1, name = "SEQ_RESOURCE")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CAT_ID")
    ResourceCategory resourceCategory;



    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", resourceCategory=" + resourceCategory +
                ", region='" + region + '\'' +
                ", status='" + status + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", type='" + type + '\'' +
                ", imageId='" + imageId + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", subnetId='" + subnetId + '\'' +
                ", name='" + name + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", relatedResources=" + relatedResources +
                ", ip='" + ip + '\'' +
                '}';
    }

    String region;
    String status;
    String resourceId;
    String type;
    String imageId;
    String vpcId;
    String subnetId;
    String name;
    String endpoint;
    Date createdAt;
    Integer relatedResources;
    String ip;


    public Resource() {

    }

    public Resource(String name, Long id, ResourceCategory resourceCategory) {
        this.name = name;
        this.id = id;
        this.resourceCategory = resourceCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResourceCategory getResourceCategory() {
        return resourceCategory;
    }

    public void setResourceCategory(ResourceCategory resourceCategory) {
        this.resourceCategory = resourceCategory;
    }

    public String getRegion() {
        return region;
    }

    public Resource setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(Integer relatedResources) {
        this.relatedResources = relatedResources;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



}
