package com.my.thesis.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "main_products")
@Data
public class Product extends BaseEntity implements Serializable {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 45, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Column(name = "description")
    private String description;

    @Column(name = "count")
    private Long count;

    @Column(name = "price")
    private Double price;

    @Column(name = "year")
    private Long year;

   //foreign key
    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;

    @ManyToOne
    @JoinColumn(name = "os_id")
    private Os os;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image  image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "main_product_category",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    private List<Category> categories;

    @OneToMany(mappedBy = "product")
    private List<CheckoutOrderHasProduct> checkoutOrderHasProducts;

    @Override
    public String toString() {
        return "Product[" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", price=" + price +
                ", year=" + year +
                ", studio=" + studio +
                ", os=" + os +
                ", image=" + image +
                ']';
    }
}
