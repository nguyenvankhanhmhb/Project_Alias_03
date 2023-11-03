package com.example.ProjectAlias.Entity;

import javax.persistence.*;
import java.util.List;
@Entity(name = "size")
public class SizeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "size")
    private List<ProductEntity> product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
