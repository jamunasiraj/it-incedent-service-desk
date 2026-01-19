package lu.isd.isd_api.service;

import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;

public interface UserService {

    void adminUpdateUser(Long userId, AdminUpdateUserRequest request);
}
