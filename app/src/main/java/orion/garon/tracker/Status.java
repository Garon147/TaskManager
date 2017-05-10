package orion.garon.tracker;

import java.util.ArrayList;
import java.util.List;

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

    public static List<String> getAllStates() {

        List<String> items = new ArrayList<>();
        items.add(NEW.toString());
        items.add(INPROGRESS.toString());
        items.add(DONE.toString());

        return items;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
