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
    private RestrictionsService restrictionSvc;

    @GetMapping("/restrictions")
    public List<Restrictions> getAllRestrictions() {
        return restrictionSvc.getAllRestrictions();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restrictions")
    public Restrictions addRestrictions(@Valid @RequestBody Restrictions restriction) {
        return restrictionSvc.addRestrictions(restriction);
    }


    @PutMapping("/restrictions/{restrictionId}")
    public Restrictions updateRestrictions(
                                 @PathVariable (value = "restrictionId") Long restrictionId,
                                 @Valid @RequestBody Restrictions newRestriction) {

        return restrictionSvc.updateRestrictions(restrictionId,newRestriction);
    }

    @DeleteMapping("/restrictions/{id}")
    public void deleteRestriction(@PathVariable Long id){
        restrictionSvc.deleteRestrictions(id);
    }
}
