package lu.isd.isd_api.dto;

import lu.isd.isd_api.entity.RoleName;

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
