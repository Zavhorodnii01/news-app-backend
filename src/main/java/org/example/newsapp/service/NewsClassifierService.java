package org.example.newsapp.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import org.example.newsapp.controller.dto.ArticleDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

// Service class responsible for classifying news articles into local or global scope
@Service
public class NewsClassifierService {

    // Logger for logging information and errors
    private static final Logger log = LoggerFactory.getLogger(NewsClassifierService.class);

    // OpenAI service for generating chat completions
    private final OpenAiService openAiService;
    // Object mapper for parsing JSON strings
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Constructor for dependency injection of OpenAiService
    public NewsClassifierService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    // Method to classify an article based on its title and content into local or global news
    public ClassificationResult classify(ArticleDto articleDto) {
        String prompt = """
        You are a precise and strict news classifier with these rules:
        
        CLASSIFICATION CRITERIA:
        1. LOCAL NEWS (must meet ALL):
           - The news is primarily about events in a SPECIFIC U.S. city.
           - The city is essential to understanding the story.
           - The city must be a real U.S. city from the fixed city list.
           - The city must be followed by the FULL state name (no abbreviations).
           - Output JSON must be: {"scope":"local", "city":"CityName, StateName"}
        
        2. GLOBAL NEWS (any of these):
           - No U.S. city is central to the story.
           - Mentions of cities only incidental (birthplace, HQs, institutions).
           - Output JSON must be: {"scope":"global", "city":null}
        
        ABSOLUTE RULES:
        - NEVER invent or hallucinate cities or states.
        - Always use full state names after the city, no abbreviations allowed.
        - Mentions like "born in [CITY]", "based in [CITY]", "headquartered in [CITY]" count as global.
        - Institution names do NOT count as locations.
        - The output must be strictly one of these two JSON objects and nothing else.
        
        RESPONSE FORMAT (ONLY JSON):
        {
          "scope": "local"|"global",
          "city": "CityName, StateName"|null
        }
        
        Examples of valid output:
        {"scope":"local", "city":"Chicago, Illinois"}
        {"scope":"global", "city":null}
        
        Invalid examples:
        {"scope":"local", "city":"Chicago"}          // Missing state name
        {"scope":"local", "city":"Chicago, IL"}      // State abbreviation not allowed
        {"scope":"mixed", "city":null}               // Scope must be "local" or "global"
        {"scope":"local", "city":"Toronto, Ontario"} // Non-US city
        
        Article to classify:
        Title: %s
        Content: %s
""".formatted(articleDto.getTitle(), articleDto.getContent());

        // Building a chat completion request with the specified prompt
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o-mini")
                .messages(List.of(new ChatMessage(ChatMessageRole.USER.value(), prompt)))
                .temperature(0.2)
                .build();

        // Creating a chat completion result using the OpenAI service
        ChatCompletionResult result = openAiService.createChatCompletion(request);
        // Extracting and trimming the raw response message
        String rawResponse = result.getChoices().get(0).getMessage().getContent().trim();

        // Cleaning the JSON response to remove code block markers and trim whitespace
        String cleanedJson = cleanJson(rawResponse);

        // Parsing the cleaned JSON into a ClassificationResult object
        return parseJson(cleanedJson);
    }

    // Method to clean the JSON response by removing code block markers and trimming whitespace
    private String cleanJson(String rawJson) {
        return rawJson
                .replaceAll("(?s)^```json\\s*", "")
                .replaceAll("(?s)^```\\s*", "")
                .replaceAll("(?s)\\s*```$", "")
                .trim();
    }

    // Method to parse the JSON response into a ClassificationResult object
    private ClassificationResult parseJson(String json) {
        try {
            // Reading the JSON string into a ClassificationResponse object
            ClassificationResponse response = objectMapper.readValue(json, ClassificationResponse.class);

            // Handling cases where the scope field is missing from the response
            if (response.scope == null) {
                log.warn("No scope field found in response JSON. Defaulting to global.");
                return new ClassificationResult(true, null, null);
            }

            // Determining if the news is global based on the scope field
            boolean isGlobal = response.scope.equalsIgnoreCase("global");

            // Handling cases where the scope is local but the city field is missing or invalid
            if (!isGlobal) {
                if (response.city == null || response.city.isBlank()) {
                    log.warn("Local scope but city is null or blank. Forcing global.");
                    return new ClassificationResult(true, null, null);
                }

                // Splitting the city field to extract city and state names
                String[] parts = response.city.split(",\\s*");
                if (parts.length != 2) {
                    log.warn("City field does not contain valid 'City, State' format: '{}'. Forcing global.", response.city);
                    return new ClassificationResult(true, null, null);
                }

                // Trimming whitespace from city and state names
                String cityName = parts[0].trim();
                String stateName = parts[1].trim();

                // Returning a ClassificationResult object for local news
                return new ClassificationResult(false, cityName, stateName);
            }

            // Returning a ClassificationResult object for global news
            return new ClassificationResult(true, null, null);

        } catch (Exception e) {
            // Logging errors that occur during JSON parsing
            log.error("Failed to parse classification JSON: '{}'", json, e);
            return new ClassificationResult(true, null, null);
        }
    }

    // Inner class representing the response from the news classification process
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ClassificationResponse {
        public String scope;
        public String city;
    }

    // Class representing the result of the news classification process
    public static class ClassificationResult {
        public final boolean isGlobal;
        public final String cityName;
        public final String stateName;

        // Constructor to initialize the ClassificationResult object
        public ClassificationResult(boolean isGlobal, String cityName, String stateName) {
            this.isGlobal = isGlobal;
            this.cityName = cityName;
            this.stateName = stateName;
        }
    }
}
