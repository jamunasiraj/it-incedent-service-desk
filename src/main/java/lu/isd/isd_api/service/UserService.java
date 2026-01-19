package lu.isd.isd_api.service;

import java.util.List;

import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;

public interface UserService {

    void adminUpdateUser(Long userId, AdminUpdateUserRequest request);

    List<OwnerDto> getAllUsers();

    void deleteUser(Long userId);
}
