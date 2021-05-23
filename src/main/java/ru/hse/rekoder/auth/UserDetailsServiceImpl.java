package ru.hse.rekoder.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.hse.rekoder.model.User;
import ru.hse.rekoder.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(new User.UserCompositeKey(username))
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("User \"" + username + "\" not found"));
    }
}
