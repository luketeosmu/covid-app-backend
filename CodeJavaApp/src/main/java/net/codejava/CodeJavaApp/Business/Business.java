package net.codejava.CodeJavaApp.Business;

import javax.persistence.*;

@Entity
public class Business {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessId;

    // @Column(nullable = false, unique = true, length = 200)
    private String businessName;

    // @Column(nullable = false, length = 200)
    private String description;

    // @Column(length = 200)
    private String location;

    // @Column(unique = true, length = 45)
    private String email;

    // @Column(unique = true)
    private Long mobileNum;

    // @Column(nullable = false)
    private Character outdoorIndoor;

    // @Column(nullable = false)
    private Long capacity;




    
    public Business(String businessName, String description, String location, String email, Long mobileNum,
            Character outdoorIndoor, Long capacity) {
        this.businessName = businessName;
        this.description = description;
        this.location = location;
        this.email = email;
        this.mobileNum = mobileNum;
        this.outdoorIndoor = outdoorIndoor;
        this.capacity = capacity;
    }

    public Business(Long businessId, String businessName, String description, String location, String email,
            Long mobileNum, Character outdoorIndoor, Long capacity) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.description = description;
        this.location = location;
        this.email = email;
        this.mobileNum = mobileNum;
        this.outdoorIndoor = outdoorIndoor;
        this.capacity = capacity;
    }

    public Business() {
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    public Long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Long mobileNum) {
        this.mobileNum = mobileNum;
    }

    public void setOutdoorIndoor(Character outdoorIndoor) {
        this.outdoorIndoor = outdoorIndoor;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public char getOutdoorIndoor() {
        return outdoorIndoor;
    }

    public void setOutdoorIndoor(char outdoorIndoor) {
        this.outdoorIndoor = outdoorIndoor;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }
}
