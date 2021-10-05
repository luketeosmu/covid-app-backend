package net.codejava.CodeJavaApp.Business;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository businessRepository;

//    private List<Business> businesses = new ArrayList<>();

    public List<Business> getAllBusinesses(){
        return businessRepository.findAll();
    }

    public Business addBusiness(@RequestBody Business business){
        if(businessRepository.findById(business.getBusiness_id()).isPresent()){
            return businessRepository.save(business);
        }
        else{
            return null;
        }
    }

    public Business updateBusiness(@PathVariable(value = "businessId") Long businessId, @RequestBody Business newBusiness){
        return businessRepository.findById(businessId).map(restriction -> {restriction.setDescription(newBusiness.getDescription());
            return businessRepository.save(restriction);
        }).orElse(null);
    }

    public void deleteBusiness(@PathVariable Long businessId){
        try{
            businessRepository.deleteById(businessId);
        }catch(EmptyResultDataAccessException e) {
            throw new BusinessNotFoundException(businessId);
        }
    }

}