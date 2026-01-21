package lu.isd.isd_api.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    // Soft-delete flag
    @Column(nullable = false)
    private boolean deleted = false;

    /**
     * One User can own many Tickets (One-to-Many relationship)
     * "ownedTickets" holds tickets where this user is the owner
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ticket> ownedTickets = new HashSet<>();

    /**
     * Many Users can be assigned to many Tickets (Many-to-Many relationship)
     * "assignedTickets" holds tickets where this user is assigned as participant
     */
    @ManyToMany(mappedBy = "assignees", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Ticket> assignedTickets = new HashSet<>();

    public User() {
    }

    public User(Long id, String username, String password, String email, RoleName role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Ticket> getOwnedTickets() {
        return ownedTickets;
    }

    public void setOwnedTickets(Set<Ticket> ownedTickets) {
        this.ownedTickets = ownedTickets;
    }

    public Set<Ticket> getAssignedTickets() {
        return assignedTickets;
    }

    public void setAssignedTickets(Set<Ticket> assignedTickets) {
        this.assignedTickets = assignedTickets;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    // helper
    public void addOwnedTicket(Ticket ticket) {
        if (ticket != null) {
            ownedTickets.add(ticket);
            ticket.setOwner(this);
        }
    }

    public void addAssignedTicket(Ticket ticket) {
        if (ticket != null) {
            assignedTickets.add(ticket);
            ticket.getAssignees().add(this);
        }
    }
}
