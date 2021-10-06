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


    public Business getBusinessByBusinessIdAndUserId(Long businessId, Long userId){
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            return business2;
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));
    }


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

    public Business updateBusiness(Long userId, Long businessId, Business newBusiness){
        // return businessRepository.findById(businessId).map(restriction -> {restriction.setDescription(newBusiness.getDescription());
        //     return businessRepository.save(restriction);
        // }).orElse(null);

        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            business2.setDescription(newBusiness.getDescription());
            business2.setLocation(newBusiness.getLocation());
            business2.setEmail(newBusiness.getEmail());
            business2.setMobileNum(newBusiness.getMobileNum());
            business2.setOutdoorIndoor(newBusiness.getOutdoorIndoor());
            return businessRepository.save(business2);
        }).orElseThrow(()->new BusinessNotFoundException(businessId));

    }

    public ResponseEntity<?> deleteBusiness(Long userId, Long businessId){
        // try{
        //     businessRepository.deleteById(businessId);
        // }catch(EmptyResultDataAccessException e) {
        //     throw new BusinessNotFoundException(businessId);
        // }

        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return businessRepository.findByBusinessIdAndUserId(businessId,userId).map(business2 ->{
            businessRepository.delete(business2);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));
    }

}

/*
if(!books.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        return reviews.findByIdAndBookId(reviewId, bookId).map(review -> {
            reviews.delete(review);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ReviewNotFoundException(reviewId));
    } */
