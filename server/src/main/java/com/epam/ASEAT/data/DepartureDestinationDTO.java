package com.epam.ASEAT.data;

import java.util.List;

public class DepartureDestinationDTO {
    private List<String> departures;
    private List<String> destinations;

    // Getters and setters
    public List<String> getDepartures() {
        return departures;
    }

    public void setDepartures(List<String> departures) {
        this.departures = departures;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
