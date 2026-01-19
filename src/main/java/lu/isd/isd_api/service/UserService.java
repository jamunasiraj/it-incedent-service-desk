package lu.isd.isd_api.service;

import java.util.List;
import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;
import lu.isd.isd_api.entity.User;

public interface UserService {

    User getUserById(Long userId);

    User getUserByUsername(String username);

    void adminUpdateUser(Long userId, AdminUpdateUserRequest request);

    List<OwnerDto> getAllUsers();

    void deleteUser(Long userId);

}
