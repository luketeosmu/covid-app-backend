package net.codejava.CodeJavaApp.Business;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.codejava.CodeJavaApp.user.*;
import java.util.*;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>{
    Optional<Business> findByBusinessName(String businessName);
    List<Business> findByUserId(Long userId);
    Optional<Business> findByBusinessIdAndUserId(Long id, Long userid);
}