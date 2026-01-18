package lu.isd.isd_api.dto.response;

import lu.isd.isd_api.entity.RoleName;

public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private RoleName role;

    public UserResponseDto(Long id, String username, String email, RoleName role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public RoleName getRole() {
        return role;
    }
}
