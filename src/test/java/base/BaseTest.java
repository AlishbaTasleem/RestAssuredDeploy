package base;

import io.restassured.RestAssured;
import org.example.ConfigReader;
import org.testng.annotations.BeforeSuite;

public class BaseTest {
    protected ConfigReader config;

    @BeforeSuite
    public void setup(){
        config = new ConfigReader();
        RestAssured.baseURI = config.getBaseURI();
    }
}
