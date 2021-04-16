package org.labs.app;

public enum PositionInDB {
    MANAGER (1, "Менеджер"),
    RYADOVOI (2, "Рядовой"),
    ADMIN (3, "Админ");

    private final int id;
    private final String position;

    PositionInDB(int id, String position) {
        this.id = id;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }
}
