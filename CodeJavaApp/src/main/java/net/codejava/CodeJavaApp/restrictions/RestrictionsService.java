package net.codejava.CodeJavaApp.restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;


@Service
public class RestrictionsService {
    @Autowired
    private RestrictionsRepository restrictionsRepository;

//    private List<Restrictions> Restrictionses = new ArrayList<>();

    public List<Restrictions> getAllRestrictions(){
        return restrictionsRepository.findAll();
    }

    public Restrictions addRestrictions(Restrictions restrictions){
        if(!restrictionsRepository.findById(restrictions.getId()).isPresent())
                return restrictionsRepository.save(restrictions);
            else
                return null;
    }

    public Restrictions updateRestrictions(Long RestrictionsId, Restrictions newRestrictions){
        return restrictionsRepository.findById(RestrictionsId).map(restriction -> {restriction.setDescription(newRestrictions.getDescription());
            return restrictionsRepository.save(restriction);
        }).orElse(null);
    }

    public void deleteRestrictions(Long RestrictionsId){
        try{
            restrictionsRepository.deleteById(RestrictionsId);
        }catch(EmptyResultDataAccessException e) {
            throw new RestrictionNotFoundException(RestrictionsId);
        }
    }
}
