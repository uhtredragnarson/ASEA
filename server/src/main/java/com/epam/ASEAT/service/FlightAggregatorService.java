package com.epam.ASEAT.service;

import com.epam.ASEAT.data.RO.FlightRouteRO;

import com.epam.ASEAT.integration.providers.Provider1Client;
import com.epam.ASEAT.integration.providers.Provider2Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FlightAggregatorService {

    private static final Logger logger = LoggerFactory.getLogger(FlightAggregatorService.class);

    @Autowired
    private Provider1Client provider1Client;

    @Autowired
    private Provider2Client provider2Client;

    /**
     * Public method to get aggregated flight routes from both providers.
     */
    public List<FlightRouteRO> getAggregatedFlightRoutes() {
        List<FlightRouteRO> provider1Routes = fetchRoutesFromProvider1();
        List<FlightRouteRO> provider2Routes = fetchRoutesFromProvider2();

        List<FlightRouteRO> aggregatedRoutes = new ArrayList<>();
        aggregatedRoutes.addAll(provider1Routes);
        aggregatedRoutes.addAll(provider2Routes);

        return aggregatedRoutes;
    }

    /**
     * Fetches flight routes from Provider1 and throws exceptions for the global handler to manage.
     */
    private List<FlightRouteRO> fetchRoutesFromProvider1() {
        List<FlightRouteRO> provider1Routes = provider1Client.getFlightRoutes();
        if (provider1Routes == null) {
            logger.warn("Provider1 returned null for flight routes.");
            return Collections.emptyList();
        }
        return provider1Routes;
    }

    /**
     * Fetches flight routes from Provider2 and throws exceptions for the global handler to manage.
     */
    private List<FlightRouteRO> fetchRoutesFromProvider2() {
        List<FlightRouteRO> provider2Routes = provider2Client.getFlightRoutes();
        if (provider2Routes == null) {
            logger.warn("Provider2 returned null for flight routes.");
            return Collections.emptyList();
        }
        return provider2Routes;
    }
}