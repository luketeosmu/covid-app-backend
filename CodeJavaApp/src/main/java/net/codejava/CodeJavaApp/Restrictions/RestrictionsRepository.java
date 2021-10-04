package net.codejava.CodeJavaApp.Restrictions;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrictionsRepository extends JpaRepository<Restrictions, Long>{
    // additional derived queries specified here will be implemented by Spring Data JPA
    // start the derived query with "findBy", then reference the entity attributes you want to filter
//    List<Restrictions> findByRestrictionsId(Long id);
    List<Restrictions> findByDescription(String description);
//    Optional<Restrictions> findByIdAndBusinessId(Long id, Long business_id);
}