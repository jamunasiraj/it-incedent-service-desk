package lu.isd.isd_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.exception.ResourceNotFoundException;
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
            @Valid @RequestBody AdminUpdateUserRequest request) {

        userService.adminUpdateUser(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OwnerDto>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OwnerDto> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            OwnerDto dto = new OwnerDto(user.getId(), user.getUsername(), user.getRole());
            return ResponseEntity.ok(dto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}