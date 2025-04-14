package com.petstore.api.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG Cucumber Runner
 * To run specific tags, use: 
 * mvn clean test -Dcucumber.filter.tags="@tagname"
 * For example:
 * mvn clean test -Dcucumber.filter.tags="@pet"
 * mvn clean test -Dcucumber.filter.tags="@smoke"
 * mvn clean test -Dcucumber.filter.tags="@pet and @smoke"
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.petstore.api.stepdefs"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty.html",
                "json:target/cucumber-reports/CucumberTestReport.json",
                "com.petstore.api.listeners.CustomCucumberListener",
                "rerun:target/cucumber-reports/rerun.txt"
        },
        monochrome = true,
        dryRun = false
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 