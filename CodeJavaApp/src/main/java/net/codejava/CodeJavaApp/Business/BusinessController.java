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
import org.springframework.web.bind.annotation.CrossOrigin;

import net.codejava.CodeJavaApp.user.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class BusinessController {

    private BusinessService businessService;

    private UserRepository users;

    @Autowired
    public BusinessController(BusinessService bs, UserRepository users) {
        this.businessService = bs;
        this.users = users;
    }

    /**
     * gets all businesses under the user
     * @param userId
     * @return list of businesses
     */
    @GetMapping("/users/{userid}/businesses")
    public List<Business> getBusinessByUserId(@PathVariable(value = "userid") Long userId) {
        if (!users.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return businessService.getAllBusinesses(userId);
    }


    /**
     * get specific business given id and userid
     * @param userId
     * @param businessId
     * @return business 
     */
    @GetMapping("/users/{userid}/businesses/{businessid}")
    public Business getBusinessByBusinessIdAndUserId(@PathVariable(value = "userid") Long userId,
            @PathVariable(value = "businessid") Long businessId) {
        if (!users.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Business business = businessService.getBusinessByBusinessIdAndUserId(businessId, userId);
        if (business == null) {
            throw new BusinessNotFoundException(businessId);
        }
        return business;
    }

    /**
     * add new business 
     * @param userId
     * @param business
     * @return business
     */
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/{userid}/businesses")
    public Business addBusinesses(@PathVariable(value = "userid") Long userId, @RequestBody @Valid Business business) {
        if (!users.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Business addedBusiness = businessService.addBusiness(userId, business);
        if (addedBusiness == null) {
            throw new BusinessExistsException(business.getBusinessName());
        }
        return addedBusiness;
    }

    /**
     * update business with new details 
     * @param userId
     * @param businessId
     * @param newBusiness
     * @return Business
     */
    @PutMapping("/users/{userid}/businesses/{businessId}")
    public Business updateBusinesses(@PathVariable(value = "userid") Long userId,
            @PathVariable(value = "businessId") Long businessId, @RequestBody Business newBusiness) {
        if (!users.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        Business updatedBusiness = businessService.updateBusiness(userId, businessId, newBusiness);
        if (updatedBusiness == null) {
            throw new BusinessNotFoundException(businessId);
        }
        return updatedBusiness;
    }
    /**
     * delete business given userid and businessid
     * @param userid
     * @param businessId
     * @return ResponseEntity 
     */
    @DeleteMapping("/users/{userid}/businesses/{businessId}")
    public ResponseEntity<?> deleteBusiness(@PathVariable Long userid, @PathVariable Long businessId) {
        if (!users.existsById(userid)) {
            throw new UserNotFoundException(userid);
        }
        return businessService.deleteBusiness(userid, businessId);
    }
}
