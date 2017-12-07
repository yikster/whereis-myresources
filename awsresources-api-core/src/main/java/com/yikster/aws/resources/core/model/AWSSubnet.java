package com.yikster.aws.resources.core.model;

import com.amazonaws.services.ec2.model.Subnet;

import java.util.List;

public class AWSSubnet {
    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    String subnetId;

    public AWSSubnet() {}

    public static String getSubnetIds(List list) {
        StringBuffer sb = new StringBuffer();

        for(int i=0;i<list.size();i++) {
            if(list.get(i) instanceof Subnet) {
                if(i>0) sb.append(",");
                sb.append((new AWSSubnet((Subnet) list.get(i)).getSubnetId()));
            }
        }

        return sb.toString();
    }

    public AWSSubnet(Subnet subnet) {
        this.subnetId = subnet.getSubnetId();
    }


    public AWSSubnet(com.amazonaws.services.rds.model.Subnet subnet) {
        this.subnetId = subnet.getSubnetIdentifier();
    }

    public AWSSubnet(com.amazonaws.services.redshift.model.Subnet subnet) {
        this.subnetId = subnet.getSubnetIdentifier();
    }

    public AWSSubnet(com.amazonaws.services.elasticache.model.Subnet subnet) {
        this.subnetId = subnet.getSubnetIdentifier();
    }
}
