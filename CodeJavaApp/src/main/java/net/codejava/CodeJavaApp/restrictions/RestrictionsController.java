package net.codejava.CodeJavaApp.Restrictions;
import java.util.List;

import javax.validation.Valid;

import net.codejava.CodeJavaApp.Restrictions.RestrictionNotFoundException;
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
    private RestrictionsServiceImpl restrictionsService;

    public RestrictionsController(RestrictionsServiceImpl restrictionsService) {
        this.restrictionsService = restrictionsService;
    }

    @GetMapping("/restrictions")
    public List<Restrictions> getRestrictions() {
        return restrictionsService.listRestrictions();
    }

    @GetMapping("/restrictions/{id}")
    public Restrictions getRestriction(Long restrictionId) {
        Restrictions restriction = restrictionsService.getRestriction(restrictionId);
        if(restriction == null) throw new RestrictionNotFoundException(restrictionId);
        return restriction;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/restrictions")
    public Restrictions addRestrictions(@Valid @RequestBody Restrictions restriction) {
        Restrictions savedRestriction = restrictionsService.addRestriction(restriction);
        return savedRestriction;
    }


    @PutMapping("/restrictions/{restrictionId}")
    public Restrictions updateRestrictions(
            @PathVariable (value = "restrictionId") Long restrictionId,
            @Valid @RequestBody Restrictions newRestriction) {
            Restrictions updatedRestriction = restrictionsService.updateRestriction(restrictionId, newRestriction);
            if(updatedRestriction == null) throw new RestrictionNotFoundException(restrictionId);
            return updatedRestriction;
    }

    @DeleteMapping("/restrictions/{id}")
    public void deleteRestriction(@PathVariable Long id){
        try{
            restrictionsService.deleteRestriction(id);
        }catch(EmptyResultDataAccessException e) {
            throw new RestrictionNotFoundException(id);
        }
    }
}

