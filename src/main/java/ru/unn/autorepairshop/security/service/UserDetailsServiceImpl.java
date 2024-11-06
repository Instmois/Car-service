package ru.unn.autorepairshop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.exceptions.UserException;
import ru.unn.autorepairshop.repository.UserRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByAuthData_Email(username)
                .orElseThrow(UserException.CODE.NO_SUCH_USER_EMAIL::get);

        System.out.println(new SimpleGrantedAuthority(user.getAuthData().getRole().toString()));

        return new org.springframework.security.core.userdetails.User(
                user.getAuthData().getEmail(),
                user.getAuthData().getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getAuthData().getRole().toString()))
        );
    }
}
