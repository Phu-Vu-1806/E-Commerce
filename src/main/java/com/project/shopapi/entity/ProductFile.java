package com.project.shopapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.shopapi.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "product_file")
@Data
@NoArgsConstructor
public class ProductFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @JsonIgnore
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public ProductFile(String name, String type, byte[] data, Product product, User user) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.product = product;
        this.user = user;
    }
}
