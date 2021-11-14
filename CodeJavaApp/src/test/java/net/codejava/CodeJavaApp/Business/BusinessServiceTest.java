package net.codejava.CodeJavaApp.Business;
import net.codejava.CodeJavaApp.Business.Business;
import net.codejava.CodeJavaApp.Business.BusinessExistsException;
import net.codejava.CodeJavaApp.Business.BusinessNotFoundException;
import net.codejava.CodeJavaApp.Business.BusinessRepository;
import net.codejava.CodeJavaApp.Business.BusinessService;
import net.codejava.CodeJavaApp.user.User;
import net.codejava.CodeJavaApp.user.UserRepository;
import net.codejava.CodeJavaApp.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BusinessServiceTest {
    @Mock
    private BusinessRepository businesses;

    @Mock
    private UserRepository users;

    @InjectMocks
    private BusinessService businessService;

    @InjectMocks
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    

    @Test
    public void addBusiness_NewBusiness_ReturnSavedBusiness() throws Exception {
        // Arrange
        User user = new User("test@gmail.com", "Test1234", "tes", "tes", "ROLE_USER");
        Business business = new Business("Biz", "Category", 'I', 20L);

        // Mock the operation
        when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(businesses.save(any(Business.class))).thenReturn(business);

        // Act
        Business savedBusiness = businessService.addBusiness(1L, business);

        // Assert
        assertNotNull(savedBusiness);
        assertEquals(savedBusiness.getBusinessName(), business.getBusinessName());

        // verify
        verify(businesses).save(business);
        verify(businesses).findByBusinessName(business.getBusinessName());
    }

    @Test
    public void addBusiness_SameBusinessName_ReturnBusinessExistsException() {
            // arrange ***
            User user = new User("email@gmail.com", "Strongpassword", "john", "Luke", "ROLE_USER");
            Business business = new Business("Biz", "Category", 'I', 20L);
            Business newBusiness = new Business("Biz", "Cat", 'O', 20L);

            // mock the "save" operation
            when(users.findById(any(Long.class))).thenReturn(Optional.of(user));
            when(businesses.findByBusinessName(any(String.class))).thenReturn(Optional.of(business));

            // act ***
            Business savedBusiness = businessService.addBusiness(1L, newBusiness);
            // assert ***
            assertNull(savedBusiness);

            // verify
            verify(businesses).findByBusinessName(business.getBusinessName());
            verify(users).findById(1L);
    }

    @Test
    public void getBusiness_Success_ReturnBusiness() {
        User user = new User(20L, "test@gmail.com", "password", "firstName", "lastName");
        Business business = new Business(1L, "Biz", "Category", 'I', 20L, user);
        user.getBusinesses().add(business);
        when(businesses.findByBusinessIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(business));
        Business getBusiness = businessService.getBusinessByBusinessIdAndUserId(business.getBusinessId(), user.getId());
        assertNotNull(getBusiness);
        verify(businesses).findByBusinessIdAndUserId(business.getBusinessId(), user.getId());
    }

    @Test
    public void getAllBusiness_ValidUserId_ReturnList() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Business business = new Business(1L, "Biz", "Category", 'I', 20L, user);
        user.getBusinesses().add(business);
        ArrayList<Business> list = new ArrayList<>();
        list.add(business); 
        // act
        when(businesses.findByUserId(anyLong())).thenReturn(list);
        List<Business> returnBusinesses = businessService.getAllBusinesses(user.getId());
        // assert
        assert(returnBusinesses.size() == 1);
        verify(businesses).findByUserId(user.getId());
    }

    @Test
    public void getAllBusiness_InvalidUserId_ReturnNull() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Business business = new Business(1L, "Biz", "Category", 'I', 20L, user);
        user.getBusinesses().add(business);
        // act
        when(businesses.findByUserId(anyLong())).thenReturn(null);
        List<Business> returnBusinesses = businessService.getAllBusinesses(user.getId()+1);
        // assert
        assertNull(returnBusinesses);
        verify(businesses).findByUserId(user.getId()+1);
    }


    @Test
    public void getBusiness_InvalidBusinessId_ReturnNull() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Business business = new Business(1L, "Biz", "Category", 'I', 20L, user);
        user.getBusinesses().add(business);
        // act
        when(businesses.findByBusinessIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        Business returnBusiness = businessService.getBusinessByBusinessIdAndUserId(20L, user.getId());
        // assert
        assertNull(returnBusiness);
        verify(businesses).findByBusinessIdAndUserId(20L, user.getId());
    }

    @Test
    public void updateBusiness_BusinessValid_ReturnUpdatedBusiness() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Business business = new Business(1L, "Biz", "Category", 'I', 20L, user);
        Business newBusiness = new Business("Biz2", "Category2", 'O', 20L);

        // Mock
        when(businesses.save(any(Business.class))).thenReturn(business);
        // when(users.existsById(any(Long.class))).thenReturn(true);
        when(businesses.findByBusinessIdAndUserId(any(Long.class), any(Long.class))).thenReturn(Optional.of(business));
        // act

        Business savedNewBusiness = businessService.updateBusiness(1L, business.getBusinessId(), newBusiness);
        // assert
        assertNotNull(savedNewBusiness);
        verify(businesses).save(business);
        verify(businesses).findByBusinessIdAndUserId(user.getId(), business.getBusinessId());
    }

    // //
    @Test
    public void updateBusinessName_BusinessIDInvalid_ReturnNull() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Business newBusiness = new Business("Biz2", "Category2", 'O', 20L);
        // mock

        // when(businesses.save(any(Business.class))).thenReturn(business);
        when(businesses.findByBusinessIdAndUserId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        // when(users.existsById(any(Long.class))).thenReturn(true);
        // act
        Business updatedBusiness = businessService.updateBusiness(user.getId(), 20L, newBusiness);

        // assert
        verify(businesses).findByBusinessIdAndUserId(20L,user.getId());
        assertNull(updatedBusiness);

    }
    ////
}