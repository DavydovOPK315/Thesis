package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Arrays;

@Entity
@Table(name = "image")
@Data
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty
    @Lob
    byte[] content;

    @NotEmpty
    String name;

    @OneToOne(mappedBy = "image")
    private Product product;

    @Override
    public String toString() {
        return "Image[" +
                "id=" + id +
                ", content=" + Arrays.toString(content) +
                ", name='" + name + '\'' +
                ']';
    }
}