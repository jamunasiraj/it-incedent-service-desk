package lu.isd.isd_api.dto.request;

import lu.isd.isd_api.entity.RoleName;

public class AdminUpdateUserRequest {

    private String username;
    private String email;
    private String password; // optional
    private RoleName role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // ADMIN sets raw password → encoded in service
    public void setPassword(String password) {
        this.password = password;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }
}
