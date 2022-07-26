package co.topper.domain.service;

import co.topper.domain.data.converter.UserConverter;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter,
                           BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        UserEntity user = fetchUser(userDto).withPassword(encodedPassword);

        return userConverter.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUser(String emailId) {
        UserEntity user = userRepository.findById(emailId)
                .orElseThrow(() -> new ResourceNotFoundException(emailId, UserEntity.class));

        // Hide password before sending the DTO
        user = user.withPassword(null);

        return userConverter.toDto(user);
    }

    private UserEntity fetchUser(UserDto userDto) {
        UserEntity user;

        if (!userRepository.existsById(userDto.getEmailId())) {
            user = UserEntity.create(userDto.getEmailId(), userDto.getUsername(), userDto.getPassword());
        } else {
            user = userRepository.findById(userDto.getEmailId())
                    .orElseThrow(() -> new ResourceNotFoundException(userDto.getEmailId(), UserEntity.class));
        }

        return user;
    }

}
