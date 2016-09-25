package uk.org.willmott.mediasyncer.model;

/**
 * Created by tomwi on 25/09/2016.
 */
public enum StorageType {
    SMB("SMB"),
    INTERNAL("Internal Storage"),
    EXTERNAL("External Storage");

    private final String name;

    private StorageType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : name.equals(otherName);
    }


    @Override
    public String toString() {
        return this.name;
    }
}
