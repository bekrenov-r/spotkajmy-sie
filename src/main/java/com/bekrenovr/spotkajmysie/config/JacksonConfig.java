package com.bekrenovr.spotkajmysie.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.serializerByType(LocalTime.class, new LocalTimeSerializer());
        return builder;
    }

    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
        @Override
        public void serialize(LocalTime time, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(time.format(FORMATTER));
        }
    }
}
