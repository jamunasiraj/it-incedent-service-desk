package lu.isd.isd_api.dto;

import lu.isd.isd_api.entity.RoleName;

public class OwnerDto {

    private Long id;
    private String username;
    private RoleName role;

    public OwnerDto(Long id, String username, RoleName role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public RoleName getRole() {
        return role;
    }
}
