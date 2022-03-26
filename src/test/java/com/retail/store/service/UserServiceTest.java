package com.retail.store.service;

import com.retail.store.entity.UserMaster;
import com.retail.store.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void shouldFindUserByIdWhenValidIdIsPassed() {
        UserMaster expectedUserMaster = new UserMaster();
        expectedUserMaster.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUserMaster));

        UserMaster actualUserMaster = userService.findUserById(1L);

        assertThat(actualUserMaster, is(expectedUserMaster));
    }

    @Test
    public void shouldInvokeFindUserByIdWhenInvalidIdIsPassedThenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.findUserById(1L);

            fail("This is not expected!");
        } catch (RuntimeException runtimeException) {
            assertThat(runtimeException.getMessage(), is("User not found"));
        }
    }
}