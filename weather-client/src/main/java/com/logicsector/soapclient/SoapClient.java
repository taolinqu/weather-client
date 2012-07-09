package com.logicsector.soapclient;
 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.cdyne.ws.weatherws.Forecast;
import com.cdyne.ws.weatherws.ForecastReturn;
import com.cdyne.ws.weatherws.POP;
import com.cdyne.ws.weatherws.Temp;
import com.cdyne.ws.weatherws.Weather;
import com.cdyne.ws.weatherws.WeatherSoap;
 
public class SoapClient {
    private static final Logger           LOGGER      = LoggerFactory.getLogger(SoapClient.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, MMMM d yyyy");
 
    public static void main(String[] args) {
        try {
            LOGGER.debug("Creating weather service instance (Note: Weather = Service subclass)...");
            
            long start = new Date().getTime();
            // Get a reference to the SOAP service interface.
            Weather weatherService = new Weather();
            WeatherSoap weatherSoap = weatherService.getWeatherSoap();
            // An alternate way to get the SOAP service interface; includes logging interceptors.
            // JaxWsProxyFactoryBean factory = new org.apache.cxf.jaxws.JaxWsProxyFactoryBean();
            // factory.setServiceClass(WeatherSoap.class);
            // factory.setAddress("http://ws.cdyne.com/WeatherWS/Weather.asmx");
            // factory.getInInterceptors().add(new org.apache.cxf.interceptor.LoggingInInterceptor());
            // factory.getOutInterceptors().add(new org.apache.cxf.interceptor.LoggingOutInterceptor());
            // WeatherSoap weatherSoap = (WeatherSoap) factory.create();
            long end = new Date().getTime();
            LOGGER.debug("...Done! weatherService instance: {}", weatherService);
            LOGGER.debug("Time required to initialize weather service interface: {} seconds", (end - start) / 1000f);
 
            // Send a SOAP weather request for zip code 94025 (Menlo Park, CA, USA).
            LOGGER.debug("weatherSoap instance: {}", weatherSoap);
            start = new Date().getTime();
            ForecastReturn forecastReturn = weatherSoap.getCityForecastByZIP("94025");
            end = new Date().getTime();
            LOGGER.debug("Time required to invoke 'getCityForecastByZIP': {} seconds", (end - start) / 1000f);
            LOGGER.debug("forecastReturn: {}", forecastReturn);
            LOGGER.debug("forecastReturn city: {}", forecastReturn.getCity());
            LOGGER.debug("forecastReturn state: {}", forecastReturn.getState());
            LOGGER.debug("forecastReturn result: {}", forecastReturn.getForecastResult());
            LOGGER.debug("forecastReturn response text: {}", forecastReturn.getResponseText());
            LOGGER.debug("");
            List<Forecast> forecasts = forecastReturn.getForecastResult().getForecast();
            for (Forecast forecast : forecasts) {
                LOGGER.debug("  forecast date: {}", DATE_FORMAT.format(forecast.getDate().toGregorianCalendar().getTime()));
                LOGGER.debug("  forecast description: {}", forecast.getDesciption());
                Temp temps = forecast.getTemperatures();
                LOGGER.debug("  forecast temperature high: {}", temps.getDaytimeHigh());
                LOGGER.debug("  forecast temperature low: {}", temps.getMorningLow());
                POP pop = forecast.getProbabilityOfPrecipiation();
                LOGGER.debug("  forecast precipitation day: {}%", pop.getDaytime());
                LOGGER.debug("  forecast precipitation night: {}%", pop.getNighttime());
                LOGGER.debug("");
            }
            LOGGER.debug("Program complete, exiting");
        }
        catch (Exception e) {
            LOGGER.error("An exception occurred, exiting", e);
        }
    }
 
}
