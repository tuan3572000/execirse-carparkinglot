package com.exercise.carparking.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CSVFileReader {

    public static List<String[]> read(String resourceLocation) {
        LinkedList<String[]> data = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile(resourceLocation)))) {
            String line;
            while ((line =  br.readLine()) != null) {
                String[] columns = line.split(",");
                data.add(columns);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        // skip header line
        data.removeFirst();
        return data;
    }
}
