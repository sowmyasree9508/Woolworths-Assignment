package steps;


import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.cucumber.java.Before;

public class Hooks {
    private static final Logger LOG = LogManager.getLogger(Hooks.class);

    @Before
    public void testStart(Scenario scenario) {
        LOG.info("*****************************************************************************************");
        LOG.info("	Scenario: "+scenario.getName());
        LOG.info("*****************************************************************************************");
    }
}
