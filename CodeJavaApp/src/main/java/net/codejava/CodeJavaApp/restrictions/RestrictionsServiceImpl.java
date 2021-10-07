package net.codejava.CodeJavaApp.restrictions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.*;


@Service
public class RestrictionsServiceImpl implements RestrictionService{
    @Autowired
    private RestrictionsRepository restrictionsRepository;

//    private List<Restrictions> Restrictionses = new ArrayList<>();
    @Override
    public List<Restrictions> getAllRestrictions(){
        return restrictionsRepository.findAll();
    }
    @Override
    public Restrictions getRestriction(Long restrictionId){
        return restrictionsRepository.findById(restrictionId).orElse(null);
    }
    @Override
    public Restrictions addRestrictions(Restrictions restrictions){
        return restrictionsRepository.save(restrictions);
    }
    @Override
    public Restrictions updateRestrictions(Long RestrictionsId, Restrictions newRestriction){
        return restrictionsRepository.findById(RestrictionsId).map(restriction -> {restriction.setDescription(newRestriction.getDescription());
            return restrictionsRepository.save(restriction);
        }).orElse(null);
    }

    /*
        @Override
    public Book updateBook(Long id, Book newBookInfo){
        return books.findById(id).map(book -> {book.setTitle(newBookInfo.getTitle());
            return books.save(book);
        }).orElse(null);

    }
    */
    @Override
    public void deleteRestrictions(Long RestrictionsId){
        restrictionsRepository.deleteById(RestrictionsId);
    }
}
