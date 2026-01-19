package lu.isd.isd_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;
import lu.isd.isd_api.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    @Autowired
    private UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long userId,
            @RequestBody AdminUpdateUserRequest request) {

        userService.adminUpdateUser(userId, request);
        return ResponseEntity.noContent().build();
    }
}