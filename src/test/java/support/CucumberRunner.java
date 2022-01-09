package support;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import java.io.File;


@RunWith(Cucumber.class)

@CucumberOptions(
        plugin = {"pretty","html:target/cucumber-reports.html",
                "json:target/cucumber/cucumber.json",
                "util.MyTestListener"
        }
        ,features= {"src/test/features"}
        ,glue = {"steps"}
)


public class CucumberRunner {
}
