package net.codejava.CodeJavaApp.restrictions;

import net.codejava.CodeJavaApp.Business.Business;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Restrictions {
    private  @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;
    
    //applies to indoor/outdoor/all 
    @NotNull(message = "Attraction setting should not be empty")
    @Size(max = 7, message = "Attraction setting should be Indoor/Outdoor/All only")
    private String locationSetting;
    @NotNull(message = "Category should not be empty")
    private String category;
    @NotNull
    private String description;
    
    @ManyToMany
    // the column "book_id" will be in the auto-generated table "review"
    // nullable = false: add not-null constraint to the database column "book_id"
    @JoinTable(name = "business_restrictions", 
        joinColumns = @JoinColumn(name= "business_id"),
        inverseJoinColumns = 
        @JoinColumn(name = "restriction_id"))
    private Set<Business> business = new HashSet<>();
    
    public Restrictions() {
    }

    public Restrictions(Long id, String locationSetting, String category) {
        this.id = id;
        this.locationSetting = locationSetting;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationSetting() {
        return locationSetting;
    }

    public void setLocationSetting(String locationSetting) {
        this.locationSetting = locationSetting;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    // 

    
}