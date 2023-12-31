package com.exercise.carparking.application.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CSVFileReader {

    public static List<String[]> read(String resourcePath) {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        LinkedList<String[]> data = new LinkedList<>();
        try (CSVParser parser = CSVParser.parse(resource.getInputStream(), Charset.defaultCharset(), CSVFormat.DEFAULT)) {
            for (CSVRecord record : parser) {
                data.add(record.toList().toArray(new String[]{}));
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        // ignore the header row
        data.removeFirst();
        return data;
    }
}
