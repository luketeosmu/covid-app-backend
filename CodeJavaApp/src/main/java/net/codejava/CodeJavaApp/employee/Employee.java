package net.codejava.CodeJavaApp.employee;

import java.util.Date;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import net.codejava.CodeJavaApp.user.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
public class Employee {
    @Id @NotNull(message = "Please enter your work ID")
    private Long id; 

    private String name; 

    @NotNull(message = "Enter your vaccination status ")
    private boolean vaxStatus; 
    

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fetDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Employee(Long id , String name, boolean vaxStatus) {
        this.id = id;
        this.name = name;
        this.vaxStatus = vaxStatus;
    } 

}
