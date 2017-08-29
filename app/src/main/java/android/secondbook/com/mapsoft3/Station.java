package android.secondbook.com.mapsoft3;

import java.util.Date;
import java.util.UUID;

public class Station {

    private UUID mId;
    private String mName;

    public Station() {
        this(UUID.randomUUID());
    }

    public Station(UUID id) {
        mId = id;
    }
    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
