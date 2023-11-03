package com.example.ProjectAlias.Entity;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_category;

    @NotBlank(message = "Trường này không được phép bỏ trống")
    @Length(min = 3, max = 100, message = "Phải nhập tên Brand")
    private String name;


    public void Category(){}

    public void Category(int id_category, String name) {
        this.id_category = id_category;
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    private List<ProductEntity> product;

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductEntity> product) {
        this.product = product;
    }

    public void setId(int categoryId) {
    }
}
