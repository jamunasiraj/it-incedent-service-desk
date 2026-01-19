package lu.isd.isd_api.dto;

import lu.isd.isd_api.entity.RoleName;

// EDIT: Public owner DTO for ticket responses (no ids / sensitive fields) (2026-01-19)
public class OwnerPublicDto {

    private final String username;
    private final RoleName role;

    public OwnerPublicDto(String username, RoleName role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public RoleName getRole() {
        return role;
    }
}
