package com.yikster.aws.resources.core.model;

import com.amazonaws.services.redshift.model.Endpoint;

public class AWSEndpoint {
    String address;
    Integer port;
    public static String getEndpointUrl(Object o) {

        if(o instanceof Endpoint) {
            return (new AWSEndpoint((Endpoint) o)).toUrlString();
        } if( o instanceof com.amazonaws.services.rds.model.Endpoint) {
            return (new AWSEndpoint((com.amazonaws.services.rds.model.Endpoint) o)).toUrlString();
        }
        else {
            return "N/A";
        }

    }

    public AWSEndpoint() {

    }

    public AWSEndpoint(com.amazonaws.services.rds.model.Endpoint o) {
        this.address = o.getAddress();
        this.port = o.getPort();
    }

    public AWSEndpoint(Endpoint o) {
        this.address = o.getAddress();
        this.port = o.getPort();
    }

    public String toUrlString() {
        return String.format("%s:%d", address, port);
    }
}
