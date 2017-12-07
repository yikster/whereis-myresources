package com.yikster.aws.resources.core.model;

import com.amazonaws.services.ec2.model.Tag;

public class AWSTag {
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String value;

    public AWSTag() {

    }
    public static AWSTag GetAWSTag(Object o) {
        if(o instanceof Tag)
            return new AWSTag((Tag)o);
        else if(o instanceof com.amazonaws.services.redshift.model.Tag)
            return new AWSTag((com.amazonaws.services.redshift.model.Tag)o);
        else {
            // Undefined Tag Class
            return new AWSTag();
        }

    }
    public AWSTag(Tag tag) {
        this.key =tag.getKey();
        this.value = tag.getValue();

    }

    public AWSTag(com.amazonaws.services.redshift.model.Tag tag) {
        this.key =tag.getKey();
        this.value = tag.getValue();
    }
}
