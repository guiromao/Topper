package co.topper.domain.data.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Stream;

public enum Role implements GrantedAuthority {

    ADMIN(1),
    USER(2);

    private final int id;

    Role(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Role fromName(String name) {
        return Stream.of(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Role fromId(int id) {
        return Stream.of(Role.values())
                .filter(role -> role.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String getAuthority() {
        return name();
    }

}
