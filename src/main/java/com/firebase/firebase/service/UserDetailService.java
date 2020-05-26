package com.firebase.firebase.service;

import com.firebase.firebase.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author rodot
 */
@Service("userDetailService")
public class UserDetailService implements UserDetailsService {

    @Autowired
    private FirebaseServiceUser fireuser;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Person usuario;
            usuario = fireuser.BuscarUserName(username);

            List<GrantedAuthority> authorities = new ArrayList<>();

            if (usuario.getAdmin() == true) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            String encodedPassword = passwordEncoder.encode(usuario.getPassword());

            return new User(usuario.getName(), encodedPassword, usuario.getEnable(), true, true, true, authorities);

        } catch (InterruptedException ex) {
            Logger.getLogger(UserDetailService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(UserDetailService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }
}
