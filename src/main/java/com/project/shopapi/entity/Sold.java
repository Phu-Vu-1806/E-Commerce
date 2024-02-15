package com.project.shopapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "sold")
@Data
@NoArgsConstructor
public class Sold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Sold(int quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }
}
