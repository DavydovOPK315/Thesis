package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "image")
@Data
public class Image {

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
}