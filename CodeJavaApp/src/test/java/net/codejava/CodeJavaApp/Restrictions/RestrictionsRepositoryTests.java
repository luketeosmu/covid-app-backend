package net.codejava.CodeJavaApp.Restrictions;

import static org.assertj.core.api.Assertions.assertThat;

import net.codejava.CodeJavaApp.Restrictions.Restrictions;
import net.codejava.CodeJavaApp.Restrictions.RestrictionsRepository;
import net.codejava.CodeJavaApp.Restrictions.RestrictionsServiceImpl;
import net.codejava.CodeJavaApp.Restrictions.*;
import net.codejava.CodeJavaApp.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class RestrictionsRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestrictionsRepository restrictions;

    @Test
    public void testCreateRestriction() {
        Restrictions restriction = new Restrictions();
        restriction.setDescription("10 pax");
        restriction.setCategory("dining");
        restriction.setLocationSetting("indoor");
        Restrictions savedRestriction = restrictions.save(restriction);
        Restrictions existRestriction = entityManager.find(Restrictions.class, savedRestriction.getId());

        assertThat(restriction.getDescription()).isEqualTo(existRestriction.getDescription());

    }

    @Test
    public void serviceUpdateRestriction() {
        RestrictionsServiceImpl restrictionsService = new RestrictionsServiceImpl(restrictions);
        Restrictions updated = restrictionsService.updateRestriction(1L, new Restrictions("Indoor", "Category", "5 pax"));
        assertThat(updated).isNotNull();
    }

    @Test
    public void serviceAddRestriction() {
        RestrictionsServiceImpl restrictionsService = new RestrictionsServiceImpl(restrictions);
        Restrictions newRestriction = restrictionsService.addRestriction(new Restrictions("Indoor", "Category", "1000 pax"));
        assertThat(newRestriction).isNotNull();
    }
}
