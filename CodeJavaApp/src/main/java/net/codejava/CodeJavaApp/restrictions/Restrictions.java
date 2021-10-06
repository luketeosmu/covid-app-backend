package net.codejava.CodeJavaApp.Restrictions;

// import net.codejava.CodeJavaApp.Business.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restrictions")
public class Restrictions {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long restriction_id;

    //applies to indoor/outdoor/all
    @NotNull(message = "Attraction setting should not be empty")
    @Size(max = 7, message = "Attraction setting should be Indoor/Outdoor/Both only")
    private String locationSetting;
    @NotNull(message = "Category should not be empty")
    private String category;
    @NotNull
    private String description;

    // @ManyToMany
    // // the column "book_id" will be in the auto-generated table "review"
    // // nullable = false: add not-null constraint to the database column "book_id"
    // @JoinTable(name = "business_restrictions",
    //         joinColumns = @JoinColumn(name= "business_id"),
    //         inverseJoinColumns =
    //         @JoinColumn(name = "restriction_id"))
    // private Set<Business> business = new HashSet<Business>();

    public Restrictions() {
    }

    public Restrictions(String locationSetting, String category, String description) {
//        this.restriction_id = restriction_id;
        this.locationSetting = locationSetting;
        this.category = category;
        this.description = description;
    }

    public Long getId() {
        return restriction_id;
    }

    public void setId(Long restriction_id) {
        this.restriction_id = restriction_id;
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

    //


}
