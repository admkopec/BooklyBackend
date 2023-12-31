package pw.react.tuesday_booklybackend.services;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import pw.react.tuesday_booklybackend.security.models.JwtRequest;
import pw.react.tuesday_booklybackend.security.models.JwtResponse;
import pw.react.tuesday_booklybackend.utils.CompanionService;
import pw.react.tuesday_booklybackend.web.UserCreationDto;

import java.util.Date;

public class ServicesIntegrationService {
    private final String booklyUser;
    private final String booklyPassword;

    private final String parklyUrl;
    private final String carlyUrl;
    private final String flatlyUrl;

    private Date parklyTokenGenerationDate = new Date(0);
    private Date carlyTokenGenerationDate  = new Date(0);
    private Date flatlyTokenGenerationDate = new Date(0);

    private String parklyToken;
    private String carlyToken;
    private String flatlyToken;

    public ServicesIntegrationService(String booklyUser, String booklyPassword, String parklyUrl, String carlyUrl, String flatlyUrl) {
        this.booklyUser = booklyUser;
        this.booklyPassword = booklyPassword;

        this.parklyUrl = parklyUrl;
        this.carlyUrl = carlyUrl;
        this.flatlyUrl = flatlyUrl;
    }

    private String generateToken(String serviceUrl, String booklyUser, String booklyPassword) {
        // Create a new authenticate request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity(serviceUrl + "/authenticate", new JwtRequest(booklyUser, booklyPassword), JwtResponse.class);
        if (response.getStatusCode() == HttpStatus.OK && response.hasBody()) {
            return response.getBody().jwttoken();
        } else {
            // If we cannot get a proper JWT, try creating an account
            ResponseEntity<String> creationResponse = restTemplate.postForEntity(serviceUrl + "/logic/api/users", new UserCreationDto("Bookly", booklyUser, booklyPassword), String.class);
            if (creationResponse.getStatusCode() != HttpStatus.OK) {
                // If we cannot create an account, fail
                return "";
            }
            return generateToken(serviceUrl, booklyUser, booklyPassword);
        }
    }

    private String getParklyToken() {
        // If the token is older than 10 hours, regenerate
        //if ((new Date().getTime() - parklyTokenGenerationDate.getTime()) > 10*60*1000) {
            parklyToken = generateToken(parklyUrl+"/logic/api", booklyUser, booklyPassword);
        //    parklyTokenGenerationDate = new Date();
        //}
        return parklyToken;
    }

    private String getCarlyToken() {
        // If the token is older than 10 hours, regenerate
        //if ((new Date().getTime() - carlyTokenGenerationDate.getTime()) > 10*60*1000) {
            carlyToken = generateToken(carlyUrl, "string", "string");
        //    carlyTokenGenerationDate = new Date();
        //}
        return carlyToken;
    }

    private String getFlatlyToken() {
        // If the token is older than 10 hours, regenerate
        //if ((new Date().getTime() - flatlyTokenGenerationDate.getTime()) > 10*60*1000) {
            flatlyToken = generateToken(flatlyUrl, booklyUser, booklyPassword);
        //    flatlyTokenGenerationDate = new Date();
        //}
        return flatlyToken;
    }

    public String getUrl(CompanionService service) {
        return switch (service) {
            case Parkly -> parklyUrl;
            case Carly -> carlyUrl;
            case Flatly -> flatlyUrl;
        };
    }

    public String getOfferUrl(CompanionService service) {
        return switch (service) {
            case Parkly -> parklyUrl + "/logic/api/parkings";
            case Carly -> carlyUrl + "/logic/api/cars";
            case Flatly -> flatlyUrl + "/logic/api/offers";
        };
    }

    public String getToken(CompanionService service) {
        return switch (service) {
            case Parkly -> this.getParklyToken();
            case Carly -> this.getCarlyToken();
            case Flatly -> this.getFlatlyToken();
        };
    }

    public HttpHeaders getAuthorizationHeaders(CompanionService service) {
        String encodedAuth = "Bearer " + this.getToken(service);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", encodedAuth);
        return headers;
    }
}
