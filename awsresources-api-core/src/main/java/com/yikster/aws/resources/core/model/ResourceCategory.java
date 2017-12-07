package com.yikster.aws.resources.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ResourceCategory {
    @Id
    @GeneratedValue
    Integer id;

    @Column(nullable = false)
    String name;

    public ResourceCategory() {
    }

    @Override
    public String toString() {
        return "ResourceCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public ResourceCategory(Integer id) {
        this.id = id;
    }

    public ResourceCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
