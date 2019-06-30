package semantic;

public enum AttributeType {

    VOID, INT, POINTER;

    public static AttributeType getTypeByName(String name) {
        for (AttributeType token : AttributeType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
