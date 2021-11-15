package net.codejava.CodeJavaApp.Business;

import java.util.*;

import javax.validation.constraints.Null;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

import net.codejava.CodeJavaApp.user.*;
import net.codejava.CodeJavaApp.user.UserNotFoundException;
import net.codejava.CodeJavaApp.user.*;

@Service
public class BusinessService {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository users;
    /**
     * to get a list of user's businesses 
     * @param userId
     * @return List of businesses under this userId
     */

    public List<Business> getAllBusinesses(Long userId){
        if(users.findById(userId) == null){
            return null; 
        }
        return businessRepository.findByUserId(userId);
    }

    /**
     * to find the specific users/num/businesses/num 
     * @param businessId
     * @param userId
     * @return business or throw BusinessNotFoundException
     */
    public Business getBusinessByBusinessIdAndUserId(Long businessId, Long userId){
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            return business2;
        }).orElse(null);
    }

    /**
     * added logic to check for exisiting business name 
     * make sure no duplicate for business name
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
                return null; 
            }else{
                return businessRepository.save(business);
            }
        }).orElse(null);


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
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            setDetails(newBusiness, business2);
            return businessRepository.save(business2);
        }).orElse(null);

    }

    private void setDetails(Business newBusiness, Business business2) {
        business2.setCategory(newBusiness.getCategory());
        business2.setOutdoorIndoor(newBusiness.getOutdoorIndoor());
        business2.setCapacity(newBusiness.getCapacity());
        business2.setBusinessName(newBusiness.getBusinessName());
    }

    /**
     * delete business given businessId
     * @param userId
     * @param businessId
     * @return ResponseEntity
     */
    public ResponseEntity<?> deleteBusiness(Long userId, Long businessId){
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            businessRepository.delete(business2);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));
    }

}