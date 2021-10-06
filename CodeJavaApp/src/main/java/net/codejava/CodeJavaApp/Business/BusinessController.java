package net.codejava.CodeJavaApp.Business;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import net.codejava.CodeJavaApp.user.*;

@RestController
public class BusinessController {
    
    private BusinessService businessService;

    private UserRepository users;

    @Autowired
    public BusinessController(BusinessService bs, UserRepository users){
        this.businessService = bs;
        this.users = users;
    }

    @GetMapping("/users/{userid}/businesses")
    public List<Business> getBusinessByUserId(@PathVariable (value = "userid") Long userId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessService.getAllBusinesses(userId);
    }

    @GetMapping("/users/{userid}/businesses/{businessid}")
    public Business getBusinessByUserId(@PathVariable (value = "userid") Long userId, @PathVariable (value = "businessid") Long businessId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessService.getBusinessByBusinessIdAndUserId( businessId,userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userid}/businesses")
    public Business addBusinesses(@PathVariable (value = "userid") Long userId,@RequestBody @Valid Business business){
        return businessService.addBusiness(userId, business);
    }

    @PutMapping("/users/{userid}/businesses/{businessId}")
    public Business updateBusinesses(@PathVariable ( value = "userid") Long userId ,
            @PathVariable (value = "businessId") Long businessId, @RequestBody Business newBusiness) {
        return businessService.updateBusiness(userId,businessId, newBusiness);
    }

    @DeleteMapping("/users/{userid}/businesses/{businessId}")
    public ResponseEntity<?> deleteBusiness(@PathVariable Long userId ,@PathVariable Long businessId){
        return businessService.deleteBusiness(userId ,businessId);
    }
}
