package lu.isd.isd_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lu.isd.isd_api.entity.RoleName;

public class AdminUpdateUserRequest {

    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
    private String password; // optional, so no @NotBlank here

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
