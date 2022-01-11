package TestRunner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)

@CucumberOptions(
        plugin = {"pretty","html:target/cucumber-reports.html",
                "json:target/cucumber/cucumber.json",
                "util.MyTestListener"
        }
        ,features= {"src/test/features"}
        ,glue = {"stepDefinitions"}
)


public class CucumberRunner {
}
