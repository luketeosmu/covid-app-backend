package net.codejava.CodeJavaApp.Restrictions;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestrictionsServiceImpl implements RestrictionsService{

    private RestrictionsRepository restrictions;

    public RestrictionsServiceImpl(RestrictionsRepository restrictions) {
        this.restrictions = restrictions;
    }

    @Override
    public List<Restrictions> listRestrictions() {
        return restrictions.findAll();
    }

    @Override
    public Restrictions getRestriction(Long restriction_id) {
        return restrictions.findById(restriction_id).orElse(null);
    }

    @Override
    public Restrictions addRestriction(Restrictions restriction) {
        return restrictions.save(restriction);
    }

    @Override
    public Restrictions updateRestriction(Long restriction_id, Restrictions newRestrictionInfo) {
        return restrictions.findById(restriction_id).map(restriction ->{restriction.setDescription(newRestrictionInfo.getDescription());
            return restrictions.save(restriction);
        }).orElse(null);
    }

    @Override
    public void deleteRestriction(Long restriction_id) {
        restrictions.deleteById(restriction_id);
    }
}
