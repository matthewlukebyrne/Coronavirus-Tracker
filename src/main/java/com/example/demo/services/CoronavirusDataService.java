package com.example.demo.services;


import com.example.demo.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


//The services annotation "@Service" makes this a Spring service
@Service
public class CoronavirusDataService {

    // Fetching the data with a url to the CSV file
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/10-04-2020.csv";

    // New Arraylist
    private List<LocationStats> allStats = new ArrayList<>();

    // Creation of a getting for allStats to then allow the data to be used in Home controller
    public List<LocationStats> getAllStats() {
        return allStats;
    }

    //  Creating a new HTTP Client to fetch the data
    // Post Construct tells spring to initiate the service
    @PostConstruct

    // Spring schedules the services and method to be reran depending on what time you want
    //    https://crontab.guru/ for 5 minutes
    //    @Scheduled(cron = "* * * * * *")
    public void fetchVirusData() throws IOException, InterruptedException {

        // Populate all stats with the new stats
        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        // Take the body and return the text as a string
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Takes the string data and then using a CSV library added into the pom file in the root
        // https://commons.apache.org/proper/commons-csv/

        // THIS CAN BE SUBSTITUTED TO JSON PARSE

        // Then creation of a StringReader called "csvBodyReader" to read the httpResponse.body above
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        // Parsing using the open source value
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        // The data is now parsed and then a for loop is now used to iterate the data
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province_State"));
            locationStat.setCountry(record.get("Country_Region"));
            locationStat.setDeaths(record.get("Deaths"));
            locationStat.setRecovered(record.get("Recovered"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get("Confirmed")));

            System.out.println(locationStat);
            newStats.add(locationStat);

        }
        this.allStats = newStats;
    }
}
