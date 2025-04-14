package com.petstore.api.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom TestNG listener for reporting and logging
 */
@Slf4j
public class TestListener implements ITestListener {
    private static ExtentReports extentReports;
    private final Map<String, ExtentTest> testMap = new HashMap<>();
    
    /**
     * Initialize ExtentReports instance
     */
    private static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            ExtentSparkReporter reporter = new ExtentSparkReporter("test-output/reports/TestReport.html");
            extentReports.attachReporter(reporter);
        }
        return extentReports;
    }
    
    @Override
    public void onStart(ITestContext context) {
        log.info("Test suite started: {}", context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        log.info("Test suite finished: {}", context.getName());
        if (extentReports != null) {
            extentReports.flush();
        }
    }
    
    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getName());
        
        String testId = getTestId(result);
        ExtentTest test = getInstance().createTest(result.getName(), result.getMethod().getDescription());
        testMap.put(testId, test);
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {}", result.getName());
        
        String testId = getTestId(result);
        ExtentTest test = testMap.get(testId);
        if (test != null) {
            test.log(Status.PASS, "Test passed");
        }
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getName());
        log.error("Failure cause: {}", result.getThrowable().getMessage());
        
        String testId = getTestId(result);
        ExtentTest test = testMap.get(testId);
        if (test != null) {
            test.log(Status.FAIL, result.getThrowable());
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", result.getName());
        
        String testId = getTestId(result);
        ExtentTest test = testMap.get(testId);
        if (test != null) {
            test.log(Status.SKIP, "Test skipped");
            test.log(Status.SKIP, result.getThrowable());
        }
    }
    
    /**
     * Generate a unique test ID for tracking in the map
     */
    private String getTestId(ITestResult result) {
        return result.getTestClass().getName() + "." + result.getName();
    }
} 