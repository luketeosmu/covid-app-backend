package net.codejava.CodeJavaApp.restrictions;

import java.util.*;

public interface RestrictionService {
    List<Restrictions> getAllRestrictions();
    Restrictions getRestriction(Long restrictionId);
    Restrictions addRestrictions(Restrictions restrictions);
    Restrictions updateRestrictions(Long RestrictionsId, Restrictions newRestrictions);
    void deleteRestrictions(Long RestrictionsId);
}
