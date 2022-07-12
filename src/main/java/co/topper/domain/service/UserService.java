package co.topper.domain.service;

import co.topper.domain.data.dto.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto getUser(String userId);

    UserDto updateLastLogin(String userId);

}
