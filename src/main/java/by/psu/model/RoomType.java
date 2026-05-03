package by.psu.model;

public enum RoomType {
    SINGLE(1, "Single Room", "A room for one person"),
    DOUBLE(2, "Double Room", "A room with one double bed"),
    TWIN(2, "Twin Room", "A room with two single beds"),
    SUITE(4, "Suite", "A luxury room with separate living area"),
    FAMILY(4, "Family Room", "A room for families with multiple beds");

    private final int capacity;
    private final String displayName;
    private final String description;

    RoomType(int capacity, String displayName, String description) {
        this.capacity = capacity;
        this.displayName = displayName;
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canAccommodate(int guests) {
        return guests <= capacity;
    }

    @Override
    public String toString() {
        return String.format("%s (max %d guests)", displayName, capacity);
    }
}