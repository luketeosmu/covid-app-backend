package net.codejava.CodeJavaApp.restrictions;
import java.util.List;

import javax.validation.Valid;
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
    private RestrictionsRepository restrictions; 

    @GetMapping("/restrictions")
    public List<Restrictions> getAllRestrictions() {
        return restrictions.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restrictions")
    public Restrictions addRestrictions(@Valid @RequestBody Restrictions restriction) {
            if(restrictions.findById(restriction.getId()).isPresent())
                return restrictions.save(restriction);
            else
                return null;
    }


    @PutMapping("/restrictions/{restrictionId}")
    public Restrictions updateRestrictions(
                                 @PathVariable (value = "restrictionId") Long restrictionId,
                                 @Valid @RequestBody Restrictions newRestriction) {

        return restrictions.findById(restrictionId).map(restriction -> {restriction.setDescription(newRestriction.getDescription());
            return restrictions.save(restriction);
        }).orElse(null);
    }

    @DeleteMapping("/restrictions/{id}")
    public void deleteRestriction(@PathVariable Long id){
        try{
            restrictions.deleteById(id);
         }catch(EmptyResultDataAccessException e) {
            throw new RestrictionNotFoundException(id);
         }
    }
}
