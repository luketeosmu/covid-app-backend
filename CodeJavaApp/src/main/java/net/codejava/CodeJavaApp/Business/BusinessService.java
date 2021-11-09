package net.codejava.CodeJavaApp.Business;

import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import net.codejava.CodeJavaApp.user.*;
import net.codejava.CodeJavaApp.user.UserNotFoundException;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository users;
//    private List<Business> businesses = new ArrayList<>();

    public List<Business> getAllBusinesses(Long userId){
        return businessRepository.findByUserId(userId);
    }

    /**
     * to find the specific users/num/businesses/num 
     * @param businessId
     * @param userId
     * @return
     */
    public Business getBusinessByBusinessIdAndUserId(Long businessId, Long userId){
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            return business2;
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));
    }

    /**
     * check for user existence before checking for duplicated business name 
     * @param userId
     * @param business
     * @return saved business/ null 
     */
    public Business addBusiness(Long userId, Business business){
        return users.findById(userId).map(user2-> {
            //if user exist then set user and check for duplicated business name 
            business.setUser(user2);
            Business check = businessRepository.findByBusinessName(business.getBusinessName()).map(business2 ->{
                return business2;
            }).orElse(null);
            if(check != null){
                throw new BusinessExistsException(business.getBusinessName()); 
            }else{
                return businessRepository.save(business);
            }
        }).orElseThrow(()-> new UserNotFoundException(userId));


    }
    /**
     * takes in userid , business id and the new business 
     * only updates the part where things will possibly change 
     * @param userId
     * @param businessId
     * @param newBusiness
     * @return usernotfound / businessnotfound / business that is updated
     */
    public Business updateBusiness(Long userId, Long businessId, Business newBusiness){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            business2.setCategory(newBusiness.getCategory());
            business2.setOutdoorIndoor(newBusiness.getOutdoorIndoor());
            business2.setCapacity(newBusiness.getCapacity());
            business2.setBusinessName(newBusiness.getBusinessName());
            return businessRepository.save(business2);
        }).orElseThrow(()->new BusinessNotFoundException(businessId));

    }

    public ResponseEntity<?> deleteBusiness(Long userId, Long businessId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            businessRepository.delete(business2);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));
    }

}