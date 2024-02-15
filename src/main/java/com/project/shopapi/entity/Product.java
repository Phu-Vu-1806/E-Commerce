package com.project.shopapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shopapi.entity.base.BaseEntity;
import com.project.shopapi.model.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Column(name = "description")
    private String description;

    @Column(name = "detail")
    private String detail;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private float price;

    @Column(name = "count_buy")
    private int count_buy;

    @Column(name = "active")
    private int active;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User user;

//    @Column(name = "created_at")
//    private Date created_at;

    @OneToMany(mappedBy = "product")
    private List<ProductFile> productFiles = new ArrayList<ProductFile>();

    @ManyToOne
    @JoinColumn(name = "category_at")
    private Category category;

    @ManyToMany
    @JoinTable(name = "product_size", joinColumns = @JoinColumn(name = "product_id",referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "size_id", referencedColumnName = "id"))
    private List<ProductSize> sizes;

    public ProductDto toDto(){
        return ProductDto.builder()
//                .id(id)
                .name(name)
                .description(description)
                .detail(detail)
                .price(price)
                .build();
    }
}
