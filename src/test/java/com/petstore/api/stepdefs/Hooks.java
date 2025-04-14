package com.petstore.api.stepdefs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.petstore.api.config.ApiConfig;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cucumber hooks for setup and teardown
 */
@Slf4j
public class Hooks {
    private static ExtentReports extentReports;
    private static ApiConfig config;
    private ExtentTest test;

    @BeforeAll
    public static void beforeAll() {
        log.info("Setting up test suite");
        config = ApiConfig.getInstance();
        
        // Setup Extent Reports
        String reportDir = config.getProperty("report.output.dir", "test-output/reports");
        new File(reportDir).mkdirs();
        
        String reportName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = reportDir + "/PetStoreAPIReport_" + reportName + ".html";
        
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle(config.getProperty("report.title", "PetStore API Test Report"));
        sparkReporter.config().setReportName(config.getProperty("report.name", "PetStore API Test Report"));
        
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Environment", "Test");
        extentReports.setSystemInfo("API Base URL", config.getBaseUrl());
    }

    @Before
    public void before(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        
        // Create test in extent reports
        test = extentReports.createTest(scenario.getName());
    }

    @After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            log.error("Scenario failed: {}", scenario.getName());
            test.fail(scenario.getName() + " - FAILED");
        } else {
            log.info("Scenario passed: {}", scenario.getName());
            test.pass(scenario.getName() + " - PASSED");
        }
    }

    @AfterAll
    public static void afterAll() {
        log.info("Tearing down test suite");
        if (extentReports != null) {
            extentReports.flush();
        }
    }
} 