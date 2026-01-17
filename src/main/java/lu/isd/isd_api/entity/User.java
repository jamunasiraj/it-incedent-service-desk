package lu.isd.isd_api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    public User() {
    }

    public User(Long id, String username, String password, String email, RoleName role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    // @Override
    // public boolean equals(Object o) {
    // if (this == o)
    // return true;
    // if (o == null || getClass() != o.getClass())
    // return false;
    // User user = (User) o;
    // return id != null && id.equals(user.id);
    // }

    // @Override
    // public int hashCode() {
    // return 31;
    // }

    // @Override
    // public String toString() {
    // return "User{" +
    // "id=" + id +
    // ", username='" + username + '\'' +
    // ", email='" + email + '\'' +
    // ", role=" + role +
    // '}';
    // }

}
