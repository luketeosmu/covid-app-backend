package net.codejava.CodeJavaApp.restrictions;
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

@RestController
public class RestrictionsController {
    @Autowired 
    private RestrictionsServiceImpl restrictionSvc;

    /**
     * list all restrictions
     * @return list of restrictions 
     */
    @GetMapping("/restrictions")
    public List<Restrictions> getAllRestrictions() {
        return restrictionSvc.getAllRestrictions();
    }


    /**
     * get the specific restriction with the id 
     * @param restrictionId
     * @return restriction 
     */
    @GetMapping("/restrictions/{restrictionId}")
    public Restrictions getRestriction(@PathVariable(value = "restrictionId") Long restrictionId) {

        Restrictions ret = restrictionSvc.getRestriction(restrictionId);
        if(ret == null){
            throw new RestrictionNotFoundException(restrictionId);
        }
        return ret;
    }

    /**
     * add new restriction 
     * @param restriction
     * @return added Restriction
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restrictions")
    public Restrictions addRestrictions(@Valid @RequestBody Restrictions restriction) {
        return restrictionSvc.addRestrictions(restriction);
    }

    /**
     * update restriction details 
     * @param restriction
     * @return updated Restriction
     */
    @PutMapping("/restrictions/{restrictionId}")
    public Restrictions updateRestrictions(
                                 @PathVariable (value = "restrictionId") Long restrictionId,
                                 @Valid @RequestBody Restrictions newRestriction) {
        Restrictions restriction = restrictionSvc.updateRestrictions(restrictionId, newRestriction);
        if(restriction == null){
            throw new RestrictionNotFoundException(restrictionId);
        }
        return restriction;
    }

    /**
     * delete restiction given id
     * @param id
     */
    @DeleteMapping("/restrictions/{id}")
    public void deleteRestriction(@PathVariable Long id){
        try{
            restrictionSvc.deleteRestrictions(id);
         }catch(EmptyResultDataAccessException e) {
            throw new RestrictionNotFoundException(id);
         }
    }
}
