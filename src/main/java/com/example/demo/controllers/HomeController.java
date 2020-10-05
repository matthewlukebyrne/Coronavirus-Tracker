package com.example.demo.controllers;

import com.example.demo.models.LocationStats;
import com.example.demo.services.CoronavirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


// Sets the Spring Controller to be set as a Rest Controller or normal Controller
@Controller
public class HomeController {


    // Gets the mapping of the url of HTML to show and display data to the screen
    // The mapping comes from home.html


    // The concept of the model in Spring
    // Sample Add attribute allows you to add model data to then be shown in Thymeleaf on HTML


    // The from the service that was created for fetching Coronavirus details we can now render the data using Thmyeleaf
    // template engine

    @Autowired
    CoronavirusDataService coronavirusDataService;



    // Taking the list of objects converting it to a string and then mapping the records
    @GetMapping("/")
    public String home(Model model){
        List<LocationStats> allStats = coronavirusDataService.getAllStats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);


        // Generate HTML
        return "home";
    }
}
