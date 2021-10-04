package net.codejava.CodeJavaApp;

import net.codejava.CodeJavaApp.Restrictions.Restrictions;
import net.codejava.CodeJavaApp.Restrictions.RestrictionsRepository;
import net.codejava.CodeJavaApp.Restrictions.RestrictionsServiceImpl;
//import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestrictionsServiceTest {
    @Mock
    private RestrictionsRepository restrictions;

    @InjectMocks
    private RestrictionsServiceImpl restrictionsService;

    @Test
    public void addRestriction_NewDescription_ReturnSavedRestriction(){
        // arrange ***
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        // mock the "findbytitle" operation
        when(restrictions.findByDescription(any(String.class))).thenReturn(new ArrayList<Restrictions>());
        // mock the "save" operation
        when(restrictions.save(any(Restrictions.class))).thenReturn(restriction);

        // act ***
        Restrictions savedRestriction = restrictionsService.addRestriction(restriction);

        // assert ***
        assertNotNull(savedRestriction);
        verify(restrictions).findByDescription(restriction.getDescription());
        verify(restrictions).save(restriction);
    }

    @Test
    public void updateRestriction_NotFound_ReturnNull(){
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        Long restrictionId = 10L;
        when(restrictions.findById(restrictionId)).thenReturn(Optional.empty());

        Restrictions updatedRestriction = restrictionsService.updateRestriction(restrictionId, restriction);

        assertNull(updatedRestriction);
        verify(restrictions).findById(restrictionId);
    }

    @Test
    public void addRestriction_SameDescription_ReturnNull() {
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        List<Restrictions> sameDescription = new ArrayList<Restrictions>();
        sameDescription.add(new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings"));
        when(restrictions.findByDescription(restriction.getDescription())).thenReturn(sameDescription);
        Restrictions savedRestriction = restrictionsService.addRestriction(restriction);
        assertNull(savedRestriction);
        verify(restrictions).findByDescription(restriction.getDescription());
    }
}
