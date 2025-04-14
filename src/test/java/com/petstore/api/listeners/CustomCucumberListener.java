package com.petstore.api.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomCucumberListener implements ConcurrentEventListener {
    private static ExtentReports extentReports;
    private final Map<String, ExtentTest> featureMap = new HashMap<>();
    private final Map<String, ExtentTest> scenarioMap = new HashMap<>();
    private final ThreadLocal<ExtentTest> scenarioThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<ExtentTest> stepThreadLocal = new ThreadLocal<>();
    private static final String REPORT_PATH = "test-output/reports/PetStoreAPIReport_" + 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".html";
    
    // Initialize ExtentReports instance
    private static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = new ExtentReports();
            ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_PATH);
            
            // Configure the reporter
            reporter.config().setDocumentTitle("PetStore API Test Report");
            reporter.config().setReportName("PetStore API Test Report");
            reporter.config().setTheme(Theme.STANDARD);
            reporter.config().setEncoding("utf-8");
            reporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
            
            // Set the view order for the report tabs
            reporter.viewConfigurer()
                   .viewOrder()
                   .as(new ViewName[] {
                      ViewName.DASHBOARD,
                      ViewName.TEST,
                      ViewName.CATEGORY,
                      ViewName.AUTHOR,
                      ViewName.DEVICE,
                      ViewName.EXCEPTION
                   })
                   .apply();
            
            // Attach the reporter and set system info
            extentReports.attachReporter(reporter);
            extentReports.setSystemInfo("Environment", "Test");
            extentReports.setSystemInfo("API Base URL", "https://petstore.swagger.io/v2");
            extentReports.setSystemInfo("Version", "1.0");
            extentReports.setSystemInfo("Framework", "REST Assured + Cucumber");
        }
        return extentReports;
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Register all event handlers
        publisher.registerHandlerFor(TestRunStarted.class, this::handleTestRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
        publisher.registerHandlerFor(TestSourceRead.class, this::handleTestSourceRead);
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private void handleTestRunStarted(TestRunStarted event) {
        log.info("Test Run Started");
    }

    private void handleTestRunFinished(TestRunFinished event) {
        log.info("Test Run Finished");
        // Flush the report at the end of the test run
        getInstance().flush();
        log.info("Generated Extent Report at: {}", REPORT_PATH);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        // This is where we can parse the feature file if needed
        // Currently not needed for basic reporting
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        String featureFilePath = event.getTestCase().getUri().toString();
        String featureName = featureFilePath.substring(featureFilePath.lastIndexOf('/') + 1);
        
        // Extract tags and add them to the feature name
        String tags = event.getTestCase().getTags().toString();
        String scenarioName = event.getTestCase().getName();
        
        // Create feature node if it doesn't exist
        ExtentTest feature = featureMap.get(featureName);
        if (feature == null) {
            // Parse the feature name to make it more readable
            String cleanFeatureName = featureName.replace("_", " ")
                                                .replace(".feature", "")
                                                .trim();
            
            // Convert first character to uppercase
            cleanFeatureName = cleanFeatureName.substring(0, 1).toUpperCase() + 
                              cleanFeatureName.substring(1);
            
            feature = getInstance().createTest(cleanFeatureName);
            featureMap.put(featureName, feature);
        }
        
        // Create scenario node with better formatting
        ExtentTest scenario = feature.createNode(scenarioName);
        
        // Add tags as labels if they exist
        if (!tags.isEmpty() && !tags.equals("[]")) {
            // Remove brackets and split by comma
            String[] tagArray = tags.substring(1, tags.length() - 1).split(", ");
            for (String tag : tagArray) {
                // Map tag colors based on tag name
                ExtentColor tagColor = getTagColor(tag);
                scenario.assignCategory(tag);
                // Add colored label for visual representation
                scenario.info(MarkupHelper.createLabel(tag, tagColor));
            }
        }
        
        scenarioMap.put(event.getTestCase().getId().toString(), scenario);
        scenarioThreadLocal.set(scenario);
        
        log.info("Scenario started: {}", scenarioName);
    }
    
    // Helper method to assign colors to different tags
    private ExtentColor getTagColor(String tag) {
        tag = tag.toLowerCase();
        if (tag.contains("smoke")) {
            return ExtentColor.GREEN;
        } else if (tag.contains("regression")) {
            return ExtentColor.BLUE;
        } else if (tag.contains("create")) {
            return ExtentColor.LIME;
        } else if (tag.contains("read")) {
            return ExtentColor.INDIGO;
        } else if (tag.contains("update")) {
            return ExtentColor.ORANGE;
        } else if (tag.contains("delete")) {
            return ExtentColor.RED;
        } else if (tag.contains("pet")) {
            return ExtentColor.CYAN;
        } else if (tag.contains("store")) {
            return ExtentColor.BROWN;
        } else if (tag.contains("user")) {
            return ExtentColor.PURPLE;
        } else if (tag.contains("auth")) {
            return ExtentColor.YELLOW;
        }
        return ExtentColor.GREY;
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        String scenarioName = event.getTestCase().getName();
        Status status = Status.PASS;
        
        // Reset the step thread local
        stepThreadLocal.remove();
        
        if (event.getResult().getStatus() == io.cucumber.plugin.event.Status.FAILED) {
            status = Status.FAIL;
            ExtentTest scenario = scenarioMap.get(event.getTestCase().getId().toString());
            
            // Create a more visually appealing error section
            String errorMessage = event.getResult().getError().getMessage();
            String stackTrace = event.getResult().getError().toString();
            
            // Add error details with better formatting
            scenario.log(Status.FAIL, MarkupHelper.createLabel("SCENARIO FAILED", ExtentColor.RED));
            scenario.log(Status.FAIL, "Error Message: " + (errorMessage != null ? errorMessage : "No message available"));
            scenario.log(Status.FAIL, "Stack Trace:");
            scenario.log(Status.FAIL, MarkupHelper.createCodeBlock(stackTrace));
        } else if (event.getResult().getStatus() == io.cucumber.plugin.event.Status.SKIPPED) {
            status = Status.SKIP;
            ExtentTest scenario = scenarioMap.get(event.getTestCase().getId().toString());
            scenario.log(Status.SKIP, MarkupHelper.createLabel("SCENARIO SKIPPED", ExtentColor.AMBER));
        } else {
            ExtentTest scenario = scenarioMap.get(event.getTestCase().getId().toString());
            scenario.log(Status.PASS, MarkupHelper.createLabel("SCENARIO PASSED", ExtentColor.GREEN));
        }
        
        log.info("Scenario finished: {} with status: {}", scenarioName, status);
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getText();
            String keyword = pickleStep.getStep().getKeyword().trim();
            
            log.info("Step started: {} {}", keyword, stepText);
            
            ExtentTest scenario = scenarioThreadLocal.get();
            if (scenario != null) {
                try {
                    // Create step with Gherkin keyword and better formatting
                    ExtentTest step = scenario.createNode(new GherkinKeyword(keyword), stepText);
                    
                    // Apply different styling based on keyword
                    if (keyword.equalsIgnoreCase("Given")) {
                        step.info(MarkupHelper.createLabel("ARRANGE", ExtentColor.BLUE));
                    } else if (keyword.equalsIgnoreCase("When")) {
                        step.info(MarkupHelper.createLabel("ACT", ExtentColor.GREEN));
                    } else if (keyword.equalsIgnoreCase("Then") || keyword.equalsIgnoreCase("And")) {
                        step.info(MarkupHelper.createLabel("ASSERT", ExtentColor.ORANGE));
                    }
                    
                    stepThreadLocal.set(step);
                } catch (ClassNotFoundException e) {
                    scenario.info(keyword + " " + stepText);
                }
            }
        } else if (event.getTestStep() instanceof HookTestStep) {
            // Hook steps
            String hookName = event.getTestStep().getId().toString();
            log.info("Hook started: {}", hookName);
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStep = (PickleStepTestStep) event.getTestStep();
            String stepText = pickleStep.getStep().getText();
            String keyword = pickleStep.getStep().getKeyword().trim();
            
            ExtentTest step = stepThreadLocal.get();
            
            switch (event.getResult().getStatus()) {
                case PASSED:
                    if (step != null) {
                        Markup passedLabel = MarkupHelper.createLabel("PASSED", ExtentColor.GREEN);
                        step.log(Status.PASS, passedLabel);
                        
                        // Add duration info
                        long durationMs = event.getResult().getDuration().toMillis();
                        step.log(Status.INFO, "Duration: " + formatDuration(durationMs));
                    }
                    log.info("Step passed: {} {}", keyword, stepText);
                    break;
                    
                case FAILED:
                    if (step != null) {
                        Markup failedLabel = MarkupHelper.createLabel("FAILED", ExtentColor.RED);
                        step.log(Status.FAIL, failedLabel);
                        
                        // Add detailed error information
                        String errorMessage = event.getResult().getError().getMessage();
                        String errorClass = event.getResult().getError().getClass().getName();
                        
                        step.log(Status.FAIL, "Error Type: " + errorClass);
                        step.log(Status.FAIL, "Error Message: " + errorMessage);
                        step.log(Status.FAIL, MarkupHelper.createCodeBlock(
                            event.getResult().getError().toString()));
                        
                        // Add duration info
                        long durationMs = event.getResult().getDuration().toMillis();
                        step.log(Status.INFO, "Duration: " + formatDuration(durationMs));
                    }
                    log.error("Step failed: {} {}", keyword, stepText);
                    log.error("Failure cause: {}", event.getResult().getError().getMessage());
                    break;
                    
                case SKIPPED:
                    if (step != null) {
                        Markup skippedLabel = MarkupHelper.createLabel("SKIPPED", ExtentColor.YELLOW);
                        step.log(Status.SKIP, skippedLabel);
                    }
                    log.warn("Step skipped: {} {}", keyword, stepText);
                    break;
                    
                default:
                    if (step != null) {
                        step.log(Status.INFO, "Status: " + event.getResult().getStatus());
                    }
                    break;
            }
            
            // Set the scenario thread local back
            scenarioThreadLocal.set(scenarioMap.get(
                event.getTestCase().getId().toString()));
        }
    }
    
    // Helper method to format duration
    private String formatDuration(long durationMs) {
        if (durationMs < 1000) {
            return durationMs + " ms";
        } else {
            double seconds = durationMs / 1000.0;
            return String.format("%.2f sec", seconds);
        }
    }
}