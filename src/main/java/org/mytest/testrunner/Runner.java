package org.mytest.testrunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "features/mytest.feature",
        glue = "org.mytest.stepdefinations",
        dryRun = false,
        monochrome = true,
        plugin = {"pretty","html:target/test-output.html"}
)
public class Runner {

}
