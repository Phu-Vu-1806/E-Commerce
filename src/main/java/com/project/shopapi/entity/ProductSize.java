package com.project.shopapi.entity;

import com.project.shopapi.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "size")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSize extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;

    @Column(name = "size")
    private int size;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToMany(mappedBy = "sizes")
    private List<Product> products = new ArrayList<>();

    public ProductSize(int size) {
        this.size = size;
    }
}
