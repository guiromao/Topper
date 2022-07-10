package co.topper.domain.service;

import co.topper.domain.data.dto.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);

    UserDto updateLastLogin(String userId);

}
