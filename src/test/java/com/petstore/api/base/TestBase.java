package com.petstore.api.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.petstore.api.client.PetClient;
import com.petstore.api.client.StoreClient;
import com.petstore.api.client.UserClient;
import com.petstore.api.config.ApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base class for all test classes
 */
@Slf4j
public class TestBase {
    protected static ExtentReports extentReports;
    protected ExtentTest test;
    protected PetClient petClient;
    protected StoreClient storeClient;
    protected UserClient userClient;
    protected ApiConfig config;
    
    @BeforeSuite
    public void setupSuite() {
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
    
    @BeforeMethod
    public void setupMethod(Method method) {
        log.info("Starting test: {}", method.getName());
        
        // Initialize clients
        petClient = new PetClient();
        storeClient = new StoreClient();
        userClient = new UserClient();
        
        // Create test in extent reports
        test = extentReports.createTest(method.getName());
    }
    
    @AfterMethod
    public void teardownMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("Test failed: {}", result.getName());
            test.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.warn("Test skipped: {}", result.getName());
            test.skip(result.getThrowable());
        } else {
            log.info("Test passed: {}", result.getName());
            test.pass("Test passed");
        }
    }
    
    @AfterSuite
    public void teardownSuite() {
        log.info("Tearing down test suite");
        if (extentReports != null) {
            extentReports.flush();
        }
    }
} 