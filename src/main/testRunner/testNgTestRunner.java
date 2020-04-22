package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/main/java/features", glue = { "stepDefinations" },
plugin = { "pretty", "html:target/cucumber-reports" })
public class testNgTestRunner extends AbstractTestNGCucumberTests {
}
