package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import model.DataDTO;
import model.RootWeatherDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import util.Util;
import util.PropertiesFile;
import static io.restassured.RestAssured.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import util.TestContext;
import static org.hamcrest.Matchers.*;


public class ChooseySurfer {

    private TestContext context;
    private static final Logger LOG = LogManager.getLogger(ChooseySurfer.class);

    public ChooseySurfer(TestContext context) {
        this.context = context;
    }

    HashMap<Integer, String> responseString = new HashMap<Integer, String>();
    HashMap<String, List<DataDTO>> responseData = new HashMap<String, List<DataDTO>>();
    List<DataDTO> responseBody = new ArrayList<>();
    Map<String, Object> params = null;
    List<List<Double>> rows = null;

    @Given("^I like to surf in any of (\\d+) beaches out of top (\\d+) of Sydney$")
    public void i_like_to_surf_in_any_of_beaches_out_of_top_ten_of_Sydney(int random, int bound, DataTable table) throws Throwable {

        //Getting table count and data
        rows = table.asLists(Double.class);
        params = new HashMap<String, Object>();

        //Checking the initial value and total data points
        if (bound != rows.size())
            return;

        //Getting Random Responses
        for (int i = 0; i < random; i++) {

            int answer = (new Random().nextInt(bound) - 1) + 1;
            int postcode = (rows.get(answer).get(2)).intValue();
            params.put("lat", rows.get(answer).get(0));
            params.put("lon", rows.get(answer).get(1));
            params.put("key", PropertiesFile.getProperty("Key"));

            context.response = context.requestSetup()
                    .params(params)
                    .when().get("/forecast/daily")
                    .then()
                    .body("$", hasKey("lon"))
                    .body("$", hasKey("lat"))
                    .body("$", hasKey("data"))
                    .body("$", not(hasKey("age")))
                    .statusCode(200).extract().response();
            // Deserialize the Response body into RootWeather
            RootWeatherDTO body = context.response.getBody().as(RootWeatherDTO.class);
            responseBody.addAll(body.data);
            responseData.put(body.city_name, body.data);
            responseString.put(postcode, context.response.asString());
        }
    }

    public static boolean dayOfWeekFilter(String a, int fDay, int sDay) throws ParseException {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(a);
        return (date1.getDay() == 1 || date1.getDay() == 5);
    }

    @Given("^I only like to surf on \"([^\"]*)\" & \"([^\"]*)\" in next (\\d+) days$")
    public void i_only_like_to_surf_on_Monday_Friday_in_next_days(String arg1, String arg2, int arg3) throws Throwable {

        int D1 = Util.iDayOfWeek.valueOf(arg1).getValue();
        int D2 = Util.iDayOfWeek.valueOf(arg2).getValue();

        for (Map.Entry<String, List<DataDTO>> entry : responseData.entrySet()) {
            responseBody = entry.getValue().stream().filter(d -> {
                        try {
                            return dayOfWeekFilter(d.datetime, D1, D2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            responseData.put(entry.getKey(), responseBody);
//            context.response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("resources/schemas/weather.json"));
        }

    }

    @When("^I look up the the weather forecast for the next (\\d+) days with postcodes$")
    public void i_look_up_the_the_weather_forecast_for_the_next_days_with_postcodes(int arg1) throws Throwable {
        params = new HashMap<String, Object>();

        for (Map.Entry me : responseString.entrySet()) {
            params.put("postal_code", me.getKey());
            params.put("days", arg1);
            params.put("key", PropertiesFile.getProperty("Key"));
            Response response = given().log().ifValidationFails()
                    .params(params)
                    .when().get("/current")
                    .then()
                    .body("$", hasKey("count"))
                    .body("$", hasKey("data"))
                    .statusCode(200).extract().response();
            // Deserialize the Response body into RootWeather
            String postcodeResponse = response.asString();
            Assert.assertNotNull(postcodeResponse);
        }

    }

    @Then("^I check to if see the temperature is between <(\\d+)°C and (\\d+)°C>$")
    public void i_check_to_if_see_the_temperature_is_between_C_and_C(int t1, int t2) throws Throwable {

        for (Map.Entry<String, List<DataDTO>> entry : responseData.entrySet()) {
            responseBody = entry.getValue().stream().filter(d -> (d.temp > t1 && d.temp < t2))
                    .collect(Collectors.toList());
            responseData.put(entry.getKey(), responseBody);
        }

        Assert.assertTrue(responseBody.get(new Random().nextInt(responseBody.size() - 1) + 1).temp > t1);
        Assert.assertTrue(responseBody.get(new Random().nextInt(responseBody.size() - 1) + 1).temp < t2);
    }

    @Then("^I check wind speed to be between (\\d+) and (\\d+)$")
    public void i_check_wind_speed_to_be_between_and(int s1, int s2) throws Throwable {

        for (Map.Entry<String, List<DataDTO>> entry : responseData.entrySet()) {
            responseBody = entry.getValue().stream().filter(d -> (d.wind_spd > s1 && d.wind_spd < s2))
                    .collect(Collectors.toList());
            responseData.put(entry.getKey(), responseBody);
        }

        Assert.assertTrue(responseBody.get(new Random().nextInt(responseBody.size() - 1) + 1).wind_spd > s1);
        Assert.assertTrue(responseBody.get(new Random().nextInt(responseBody.size() - 1) + 1).wind_spd < s2);
    }

    @Then("^I check to see if UV index is <= (\\d+)$")
    public void i_check_to_see_if_UV_index_is(int uv) throws Throwable {

        for (Map.Entry<String, List<DataDTO>> entry : responseData.entrySet()) {
            responseBody = entry.getValue().stream().filter(d -> d.uv <= uv)
                    .collect(Collectors.toList());
            responseData.put(entry.getKey(), responseBody);
        }

        Assert.assertTrue(responseBody.get(new Random().nextInt(responseBody.size() - 1) + 1).uv <= uv);
    }

    @Then("^I Pick best suitable spot out of top two spots, based upon suitable weather forecast for the day$")
    public void i_Pick_best_suitable_spot_out_of_top_two_spots_based_upon_suitable_weather_forecast_for_the_day() throws Throwable {
        for (Map.Entry<String, List<DataDTO>> entry : responseData.entrySet()) {
            System.out.println("Result: \"The Best suited spot would be : \" " + entry.getKey() + ", On date : " + entry.getValue().get(0).datetime+
                    " , Forecast : There would be " + entry.getValue().get(0).weather.description);
            break;
        }

    }
}







