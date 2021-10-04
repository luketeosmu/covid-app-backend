package net.codejava.CodeJavaApp.Restrictions;

import java.util.List;

public interface RestrictionsService {
    List<Restrictions> listRestrictions();
    Restrictions getRestriction(Long restriction_id);
    Restrictions addRestriction(Restrictions restriction);
    Restrictions updateRestriction(Long restriction_id, Restrictions restriction);
    void deleteRestriction(Long restriction_id);
}
