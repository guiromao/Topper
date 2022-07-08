package co.topper.domain.service;

import co.topper.domain.data.converter.UserConverter;
import co.topper.domain.data.dto.UserDto;
import co.topper.domain.data.entity.UserEntity;
import co.topper.domain.data.repository.UserRepository;
import co.topper.domain.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        UserEntity user;

        if (Objects.isNull(userDto.getUserId())
                || !userRepository.existsById(userDto.getUserId())) {
            user = UserEntity.create(userDto.getUsername(), userDto.getPassword());
        } else {
            user = userRepository.findById(userDto.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(userDto.getUserId()));
        }

        return userConverter.toDto(userRepository.save(user));
    }
}
