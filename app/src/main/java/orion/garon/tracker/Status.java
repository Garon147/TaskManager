package orion.garon.tracker;

/**
 * Created by VKI on 30.04.2017.
 */

public enum Status {

    NEW("New"),
    INPROGRESS("In Progress"),
    DONE("Done");

    private String stringValue;

    private Status(String stringVal) {
        stringValue = stringVal;
    }


    @Override
    public String toString() {
        return stringValue;
    }
}
