package net.codejava.CodeJavaApp.employee;

import java.util.Date;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    @Id 
    private Long id; 

    private String name; 

    private boolean vaxStatus; 
    
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date fetDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Employee(Long id , String name, boolean vaxStatus) {
        this.id = id;
        this.name = name;
        this.vaxStatus = vaxStatus;
    } 



    
    
}
