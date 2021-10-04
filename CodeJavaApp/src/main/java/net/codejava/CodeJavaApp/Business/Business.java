package net.codejava.CodeJavaApp.Business;

import javax.persistence.*;

@Entity
@Table(name = "business")
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long business_id;

    @Column(nullable = false, unique = true, length = 200)
    private String business_name;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(length = 200)
    private String location;

    @Column(unique = true, length = 45)
    private String email;

    @Column(unique = true)
    private Long mobile_num;

    @Column(nullable = false)
    private Character outdoor_indoor;

    @Column(nullable = false)
    private Long capacity;

    public Long getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(Long business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getMobile_num() {
        return mobile_num;
    }

    public void setMobile_num(Long mobile_num) {
        this.mobile_num = mobile_num;
    }

    public void setOutdoor_indoor(Character outdoor_indoor) {
        this.outdoor_indoor = outdoor_indoor;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public char getOutdoor_indoor() {
        return outdoor_indoor;
    }

    public void setOutdoor_indoor(char outdoor_indoor) {
        this.outdoor_indoor = outdoor_indoor;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}
