package co.topper.domain.service;

import co.topper.configuration.RedisConfiguration;
import co.topper.domain.data.converter.UserConverter;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import co.topper.domain.exception.UserAlreadyExistingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

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
    public UserDto getUser(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(userId, UserEntity.class));

        // Hide password before sending the DTO
        user = user.withPassword(null);

        return userConverter.toDto(user);
    }

    private UserEntity fetchUser(UserDto userDto) {
        UserEntity user;

        if (Objects.isNull(userDto.getUserId())) {
            validate(userDto);
            user = UserEntity.create(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
        } else {
            user = userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(userDto.getUserId(), UserEntity.class));
        }

        return user;
    }

    private Update updateOf(UserEntity user) {
        final UpdateBuilder updateBuilder = UpdateBuilder.create()
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .setTrackVotes(user.getTrackVotes());

        return updateBuilder.build()
                .orElseThrow(() -> new RuntimeException("Error creating UserEntity Update"));
    }

    private void validate(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistingException("Email", dto.getEmail());
        }
    }

}
