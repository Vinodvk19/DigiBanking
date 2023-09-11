package com.nl.bank.digibank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nl.bank.digibank.controller.CustomerController;
import com.nl.bank.digibank.dto.CustomerRegisterResponse;
import com.nl.bank.digibank.dto.CustomerRegistration;
import com.nl.bank.digibank.exception.UserAlreadyExistAuthenticationException;
import com.nl.bank.digibank.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testRegisterCustomer_Success() throws Exception {
        CustomerRegistration registrationDTO = new CustomerRegistration();
        CustomerRegisterResponse customerRegisterResponseDTO = new CustomerRegisterResponse("testuser", "password");
        registrationDTO.setUsername("testuser");
        customerRegisterResponseDTO.setPassword("password");
        registrationDTO.setFullName("Test User");
        registrationDTO.setMobileNumber("1234567890");
        registrationDTO.setCountry("NL");
        registrationDTO.setDateOfBirth(LocalDate.of(2010, 1, 1));

        CustomerRegisterResponse registeredCustomer = new CustomerRegisterResponse("testuser","password");
        /*registeredCustomer.setUsername("testuser");
        registeredCustomer.setPassword("password");*/

        when(customerService.registerCustomer(any())).thenReturn(registeredCustomer);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        CustomerRegisterResponse responseDTO = new ObjectMapper().readValue(responseContent, CustomerRegisterResponse.class);

        // Verify that the response contains the registered username and password
        assertEquals("testuser", responseDTO.getUsername());
        assertEquals("password", responseDTO.getPassword());

        // Verify that the service method was called with the correct DTO
        verify(customerService, times(1)).registerCustomer(registrationDTO);
    }

    @Test
    public void testRegisterCustomer_Failure() throws Exception {
        CustomerRegistration registrationDTO = new CustomerRegistration();
        // Set invalid data to simulate a failure
        registrationDTO.setUsername("invalidUsername");

        when(customerService.registerCustomer(any())).thenThrow(new UserAlreadyExistAuthenticationException("User already exists"));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationDTO))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration failed: User already exists"));

        // Verify that the service method was called with the correct DTO
        verify(customerService, times(1)).registerCustomer(registrationDTO);
    }
}
