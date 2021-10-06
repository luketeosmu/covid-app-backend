package net.codejava.CodeJavaApp.restrictions;

import javax.persistence.*;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Restrictions {
    private  @Id @GeneratedValue (strategy = GenerationType.IDENTITY) Long id;
    
    //applies to indoor/outdoor/all 
    // @NotNull(message = "Attraction setting should not be empty")
    @Size(max = 7, message = "Attraction setting should be Indoor/Outdoor/All only")
    private String locationSetting;
    // @NotNull(message = "Category should not be empty")
    private String category;
    // @NotNull
    private String description;

    public Restrictions(String locationSetting, String category,String description) {
        this.locationSetting = locationSetting;
        this.category = category;
        this.description = description;
    }
    public Restrictions(Long id ,String locationSetting, String category,String description) {
        this.id = id;
        this.locationSetting = locationSetting;
        this.category = category;
        this.description = description;
    }

    public Restrictions() {
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
    @Override
    public String toString() {
        return "Restrictions [category=" + category + ", description=" + description + ", id=" + id
                + ", locationSetting=" + locationSetting + "]";
    }
    
    
    // 

    
}
