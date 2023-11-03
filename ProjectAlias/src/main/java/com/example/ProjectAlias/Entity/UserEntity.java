package com.example.ProjectAlias.Entity;

import javax.persistence.*;
import java.util.List;

@Entity(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String frist_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name ="postalcode")
    private int postalcode;

    @Column(name = "country")
    private String country;

    @Column(name="phone")
    private int phone ;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private RoleEntity role;

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> review;

    public UserEntity(int id, String email, String password, String frist_name,
                      String last_name, String address, String city, int postalcode, String country,
                      int phone, RoleEntity role, List<ReviewEntity> review) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.frist_name = frist_name;
        this.last_name = last_name;
        this.address = address;
        this.city = city;
        this.postalcode = postalcode;
        this.country = country;
        this.phone = phone;
        this.role = role;
        this.review = review;
    }

    public List<ReviewEntity> getReview() {
        return review;
    }

    public void setReview(List<ReviewEntity> review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrist_name() {
        return frist_name;
    }

    public void setFrist_name(String frist_name) {
        this.frist_name = frist_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode = postalcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }
}
