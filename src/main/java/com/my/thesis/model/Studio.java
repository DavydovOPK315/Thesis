package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "main_studios")
@Data
public class Studio extends BaseEntity implements Serializable {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @OneToMany
    @JoinColumn(name = "studio_id")
    List<Product> products;

    @Override
    public String toString() {
        return "Studio[" +
                "name='" + name +
                ']';
    }
}
