package org.example.newsapp.config;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//Configuration class for setting up OpenAI service in the application.
@Configuration
public class OpenAiConfig {


    //API key for accessing OpenAI services, injected from application properties.
    @Value("${openai.api.key}")
    private String apiKey;


     //Bean definition for OpenAiService, initializing it with the API key.
    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(apiKey);
    }
}
