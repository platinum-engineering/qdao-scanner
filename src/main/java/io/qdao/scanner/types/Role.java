package io.qdao.scanner.types;

public enum Role {
    ROLE_GUEST(Role.ROLE_GUEST_STR),
    ROLE_CLIENT(Role.ROLE_CLIENT_STR),
    ROLE_MANAGER(Role.ROLE_MANAGER_STR),
    ROLE_DEVELOPER(Role.ROLE_DEVELOPER_STR);

    public static final String ROLE_GUEST_STR = "ROLE_GUEST";
    public static final String ROLE_CLIENT_STR = "ROLE_CLIENT";
    public static final String ROLE_MANAGER_STR = "ROLE_MANAGER";
    public static final String ROLE_DEVELOPER_STR = "ROLE_DEVELOPER";

    public final String value;

    Role(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean contains(String role) {
        final Role[] roles = Role.values();
        for (Role r : roles) {
            if (r.name().equals(role) || r.value.equals(role)) {
                return true;
            }
        }

        return false;

    }

}
