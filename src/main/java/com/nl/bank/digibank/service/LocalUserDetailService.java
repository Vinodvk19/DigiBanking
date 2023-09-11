package com.nl.bank.digibank.service;

import com.nl.bank.digibank.dto.LocalUser;
import com.nl.bank.digibank.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocalUserDetailService implements UserDetailsService {
 
    private final CustomerService customerService;
 
    @Override
    @Transactional
    public LocalUser loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerService.findUserByUsername(username);
        if (!customerOptional.isPresent()) {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
        return createLocalUser(customerOptional.get());
    }
 
    /**
     * @param customer The user entity
     * @return LocalUser The spring user object
     */
    private LocalUser createLocalUser(Customer customer) {
        return new LocalUser(customer.getUsername(), customer.getPassword(), customer.isEnabled(), true, true, true, List.of(new SimpleGrantedAuthority("CUSTOMER")));
    }
}