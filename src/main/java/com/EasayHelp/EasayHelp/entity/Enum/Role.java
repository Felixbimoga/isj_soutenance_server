package com.EasayHelp.EasayHelp.entity.Enum;

public enum Role {

    USER(1, "USER"),
    ADMIN(2, "ADMIN"),
    TECHNICIEN(3, "TECHNICIEN");

    private final String name;
    private final Integer id;

    Role(Integer id, String name) {
        this.name = name;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Role findById(Integer id) {
        for (Role role : Role.values()) {
            if (role.getId().equals(id)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Aucun rôle trouvé pour l'id : " + id);
    }
}
