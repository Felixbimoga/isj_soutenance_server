package com.EasayHelp.EasayHelp.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configuration pour éviter les problèmes
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(context -> {
                    // N'accède pas aux propriétés si c'est une collection
                    return !(context.getSource() instanceof Collection<?>);
                })
                .setAmbiguityIgnored(true);

        return modelMapper;
    }
}
