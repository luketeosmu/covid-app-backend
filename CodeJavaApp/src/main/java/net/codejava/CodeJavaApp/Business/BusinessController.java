package net.codejava.CodeJavaApp.Business;

import java.util.List;

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

@RestController
public class BusinessController {
    private BusinessService businessService;

    public BusinessController(BusinessService bs){
        this.businessService = bs;
    }

    @GetMapping("/businesses")
    public List<Business> getBusiness(){
        return businessService.getAllBusinesses();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/businesses")
    public Business addBusinesses(@RequestBody Business business){

        return businessService.addBusiness(business);
    }

    @PutMapping("/businesses/{businessId}")
    public Business updateBusinesses(@PathVariable (value = "businessId") Long businessId, @RequestBody Business newBusiness) {
        return businessService.updateBusiness(businessId, newBusiness);
    }

    @DeleteMapping("/businesses/{id}")
    public void deleteBusiness(@PathVariable Long businessId){
        businessService.deleteBusiness(businessId);
    }
}
