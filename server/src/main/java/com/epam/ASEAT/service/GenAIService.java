package com.epam.ASEAT.service;

import com.epam.ASEAT.data.DepartureDestinationDTO;
import com.epam.ASEAT.data.RO.FlightRouteRO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class GenAIService {

    @Autowired
    private OpenAiApi openAiApi;

    @Autowired
    private FlightAggregatorService flightAggregatorService;

    @Value("${genai.prompt}")
    private String prompt;

    /**
     * Original method to get a general answer from the OpenAI model.
     */
    public String getAnswer(String userInput, List<FlightRouteRO> flightRoutes) {
        assert userInput != null && flightRoutes != null && !flightRoutes.isEmpty();


        List<FlightRouteRO> filteredFlightRoutes = getFilteredFlightRoutesByDestinations(userInput,flightRoutes);

        String flightDataSummary = filteredFlightRoutes.stream()
                .map(route -> String.format("Airline: %s, From: %s, To: %s, Stops: %d, Equipment: %s",
                        route.getAirline(),
                        route.getSourceAirport(),
                        route.getDestinationAirport(),
                        route.getStops(),
                        route.getEquipment() == null ? "N/A" : route.getEquipment()))
                .collect(Collectors.joining("; "));

        // Format the prompt with flight data and user input
        String formattedPrompt = String.format(prompt, flightDataSummary, userInput);

        // Return the result of the OpenAI API call
        return openAiApi.getTextCompletion(formattedPrompt);
    }

    /**
     * New method to narrow down search based on user input to return only destination names.
     * Then, filter the flight routes based on those destinations.
     */
    public List<FlightRouteRO> getFilteredFlightRoutesByDestinations(String userInput, List<FlightRouteRO> flightRoutes) {
        assert userInput != null && flightRoutes != null && !flightRoutes.isEmpty();

        // Step 1: Ask GPT to return departures and destinations in JSON format
        String promptForDestinations = "Extract the names of departure and destination cities or airports mentioned in the following query: \""
                + userInput +
                "\". You have to return a JSON nothing else, if the question has no information about flights return the given JSON format keep the departures and destinations empty!" +
                ". Only return the result in JSON format as follows: {\"departures\": [\"departure1\", \"departure2\"], \"destinations\": [\"destination1\", \"destination2\"]}.";

        // Step 2: Get the GPT response
        String gptResponse = openAiApi.getTextCompletion(promptForDestinations);

        // Step 3: Parse the GPT response as JSON
        ObjectMapper objectMapper = new ObjectMapper();
        DepartureDestinationDTO dto;
        try {
            dto = objectMapper.readValue(gptResponse, DepartureDestinationDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse GPT response: " + gptResponse, e);
        }

        // Step 4: Filter flightRoutes based on departures and destinations
        List<String> departures = dto.getDepartures();
        List<String> destinations = dto.getDestinations();

        return flightRoutes.stream()
                .filter(route -> departures.contains(route.getSourceAirport()) && destinations.contains(route.getDestinationAirport()))
                .collect(Collectors.toList());
    }
}
