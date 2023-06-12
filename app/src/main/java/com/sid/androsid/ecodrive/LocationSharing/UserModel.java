package com.sid.androsid.ecodrive.LocationSharing;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sid on 2019-02-28.
 */

public class UserModel {

    private LatLng source,destination;
    private String usernameLoc;

    public LatLng getSource() {
        return source;
    }

    public void setSource(LatLng source) {
        this.source = source;
    }

    public LatLng getDestination() {
        return destination;
    }

    public void setDestination(LatLng destination) {
        this.destination = destination;
    }

    public String getUsernameLoc() {
        return usernameLoc;
    }

    public void setUsernameLoc(String usernameLoc) {
        this.usernameLoc = usernameLoc;
    }
}
