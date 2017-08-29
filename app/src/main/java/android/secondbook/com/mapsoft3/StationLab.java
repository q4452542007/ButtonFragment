package android.secondbook.com.mapsoft3;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StationLab {
    private static StationLab sStationLab;

    private ArrayList<Station> mStations;

    public static StationLab get(Context context) {
        if (sStationLab == null) {
            sStationLab = new StationLab(context);
        }
        return sStationLab;
    }

    private StationLab(Context context) {
        mStations = new ArrayList<>();
    }

    public void addStation(Station s) {
        mStations.add(s);
    }

    public List<Station> getStations() {
        return mStations;
    }

    public Station getStation(UUID id) {
        for (Station station : mStations) {
            if (station.getId().equals(id)) {
                return station;
            }
        }
        return null;
    }
}
