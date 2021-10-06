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

    public Business addBusiness(Business business){
        Business check = businessRepository.findByBusinessName(business.getBusinessName()).map(business2 ->{
            return business2;
        }).orElse(null);

        if(check != null){
            throw new BusinessExistsException(business.getBusinessName()); 
        }else{
            return businessRepository.save(business);
        }
    }


    public Business updateBusiness(Long businessId, Business newBusiness){
        return businessRepository.findById(businessId).map(restriction -> {restriction.setDescription(newBusiness.getDescription());
            return businessRepository.save(restriction);
        }).orElse(null);
    }

    public void deleteBusiness(Long businessId){
        try{
            businessRepository.deleteById(businessId);
        }catch(EmptyResultDataAccessException e) {
            throw new BusinessNotFoundException(businessId);
        }
    }

}
