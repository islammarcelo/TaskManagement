package com.example.banquemisr.challenge05.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.banquemisr.challenge05.exceptions.InvalidDateFormatException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.modelmapper.Converter;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Date Converter
        Converter<String, LocalDate> dateConverter =
            new Converter<String, LocalDate>() {
                public LocalDate convert(MappingContext<String, LocalDate> context) {
                    try {
                        return LocalDate.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception ex) {
                        throw new InvalidDateFormatException(
                            "Invalid date format: " + context.getSource() + ". Expected format is yyyy-MM-dd.");
                    }
                }
            };

        modelMapper.addConverter(dateConverter);

        return modelMapper;
    }
}
