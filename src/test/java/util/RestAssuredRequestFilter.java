package util;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestAssuredRequestFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        LOG.info("-----------------------------------------------------------------------------------------");
//        LOG.info(" Request Method => " + requestSpec.getMethod() +
//                "\n Request URI => " + requestSpec.getURI() +
//                "\n Request Header =>\n" + requestSpec.getHeaders() +
//                "\n Request Body => " + requestSpec.getBody() +
//                "\n\n Response Status => "+ response.getStatusLine() +
//                "\n Response Header =>\n"+ response.getHeaders() +
//                "\n Response Body => " + response.getBody().prettyPrint());
        LOG.info("-----------------------------------------------------------------------------------------");

        System.out.println(" Request Method => " + requestSpec.getMethod() +
                "\n Request URI => " + requestSpec.getURI() +
                "\n Request Header =>\n" + requestSpec.getHeaders() +
                "\n Request Body => " + requestSpec.getBody() +
                "\n\n Response Status => "+ response.getStatusLine() +
                "\n Response Header =>\n"+ response.getHeaders());
        String responseFormattedDate = response.getHeader("Date").
                replace(" ","").replace(":","_").replace(",","");
        new OutputFileWriter("responseBody_"+responseFormattedDate,
                "json",response.body().prettyPrint());
        return response;
    }
}
