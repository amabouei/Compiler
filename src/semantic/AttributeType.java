package semantic;

public enum AttributeType {

    VOID, INT, POINTER,ARRAY;

    public static AttributeType getTypeByName(String name) {
        for (AttributeType token : AttributeType.values()) {
            if (token.name().equals(name.toUpperCase()))
                return token;
        }
        return null;
    }
}
