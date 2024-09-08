package com.epam.ASEAT.integration.providers;

import com.epam.ASEAT.data.RO.FlightRouteRO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "provider2", url = "${provider1.api.url}")
public interface Provider2Client {
    @GetMapping("/flights2")
    List<FlightRouteRO> getFlightRoutes();
}