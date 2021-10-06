package net.codejava.CodeJavaApp.Restrictions;

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

        // mock the "save" operation
        when(restrictions.save(any(Restrictions.class))).thenReturn(restriction);

        // act ***
        Restrictions savedRestriction = restrictionsService.addRestriction(restriction);

        // assert ***
        assertNotNull(savedRestriction);
        verify(restrictions).save(restriction);
    }

    @Test   //this one got problem ah sial.....
    public void updateRestriction(){
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        Long restrictionId = 1L;
        Restrictions newRestriction = new Restrictions("Indoor", "Category", "2 pax");
        when(restrictionsService.addRestriction(restriction)).thenReturn(restriction);
        restrictionsService.addRestriction(restriction);
        Restrictions updatedRestriction = restrictionsService.updateRestriction(restrictionId, newRestriction);

        assertNotNull(updatedRestriction);
        // verify(books).save(book);
        verify(restrictionsService).updateRestriction(restrictionId, newRestriction);
    }

    @Test
    public void updateRestriction_NotFound_ReturnNull(){
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        Long restrictionId = 1L;
        when(restrictions.findById(restrictionId)).thenReturn(Optional.empty());

        Restrictions updatedRestriction = restrictionsService.updateRestriction(restrictionId, restriction);

        assertNull(updatedRestriction);
    }

}
