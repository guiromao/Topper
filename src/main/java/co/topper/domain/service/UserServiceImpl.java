package co.topper.domain.service;

import co.topper.domain.data.converter.UserConverter;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.entity.UserEntity.UpdateBuilder;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserEntity user = fetchUser(userDto);

        return userConverter.toDto(userRepository.updateUser(user.getId(), updateOf(user)));
    }

    private UserEntity fetchUser(UserDto userDto) {
        UserEntity user;

        if (Objects.isNull(userDto.getUserId()) ||
                !userRepository.existsById(userDto.getUserId())) {
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
                .setLastLogin(Instant.now());

        return updateBuilder.build()
                .orElseThrow(() -> new RuntimeException("Error creating UserEntity Update"));
    }

}
