package com.shose.shoseshop.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigCloudinary {
    @Bean
    public Cloudinary configKey(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dhypw7pdk");
        config.put("api_key", "272844889973159");
        config.put("api_secret", "D8IYchySe31npbUZrna7UVNfs48");
        return new Cloudinary(config);
    }
}
