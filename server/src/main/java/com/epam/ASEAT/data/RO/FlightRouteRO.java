package com.epam.ASEAT.data.RO;

import lombok.Data;

@Data
public class FlightRouteRO {
    private String airline;
    private String sourceAirport;
    private String destinationAirport;
    private String codeShare;
    private int stops;
    private String equipment;  // Optional field
}