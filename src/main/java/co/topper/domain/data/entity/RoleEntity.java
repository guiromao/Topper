package co.topper.domain.data.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public class RoleEntity implements GrantedAuthority {

    private final Role role;

    public RoleEntity(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "role=" + role +
                '}';
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getAuthority() {
        return role.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleEntity that = (RoleEntity) o;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(role);
    }

}
