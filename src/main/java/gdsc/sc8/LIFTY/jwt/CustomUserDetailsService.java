package gdsc.sc8.LIFTY.jwt;

import gdsc.sc8.LIFTY.infrastructure.UserRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다."));
    }

    private UserDetails createUserDetails(gdsc.sc8.LIFTY.domain.User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority().toString());
        return new User(user.getEmail(), user.getPassword(), Collections.singleton(authority));
    }
}