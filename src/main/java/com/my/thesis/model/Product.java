package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "main_products")
@Data
public class Product extends BaseEntity{

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 45, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Column(name = "description")
    private String description;

//    @NotEmpty(message = "Count should not be empty")
    @Column(name = "count")
    private Long count;

//    @NotEmpty(message = "Price should not be empty")
    @Column(name = "price")
    private Double price;

//    @NotEmpty(message = "Year should not be empty")
    @Column(name = "year")
    private Long year;

   //foreign key
    @ManyToOne
    @JoinColumn(name = "studio_id", insertable = false, updatable = false)
    private Studio studio;

    @ManyToOne
    @JoinColumn(name = "os_id", insertable = false, updatable = false)
    private Os os;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image  image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "main_product_category",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categories;
}
