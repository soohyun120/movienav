package com.movienav.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public Map<Long, Long> MovieIdToTmId() {
        Map<Long, Long> movieIdToTmId = new HashMap<>();

        File csv = new File("C:\\Users\\soohy\\moviedata\\links.csv");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new FileReader(csv)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line = "";
        boolean skipFirstLine = true;
        while (true) {
            try {
                assert br != null;
                if ((line = br.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }

            String[] token = line.split(",");

            if (token.length > 2) {
                Long movieId = Long.parseLong(token[0]);
                Long tmId = Long.parseLong(token[2]);

                movieIdToTmId.put(movieId, tmId);
            }
        }

        return movieIdToTmId;
    }
}
