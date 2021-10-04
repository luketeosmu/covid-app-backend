package net.codejava.CodeJavaApp.restrictions;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionsRepository extends JpaRepository<Restrictions, Long>{
    // additional derived queries specified here will be implemented by Spring Data JPA
    // start the derived query with "findBy", then reference the entity attributes you want to filter
    List<Restrictions> findByRestrictionsId(Long id);
    Optional<Restrictions> findByIdAndBusinessId(Long id, Long business_id);
}