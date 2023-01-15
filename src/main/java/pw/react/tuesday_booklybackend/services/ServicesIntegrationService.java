package pw.react.tuesday_booklybackend.services;

import org.springframework.http.HttpHeaders;
import pw.react.tuesday_booklybackend.utils.CompanionService;

public class ServicesIntegrationService {
    private final String booklyUser;
    private final String booklyPassword;

    private final String parklyUrl;
    private final String carlyUrl;
    private final String flatlyUrl;

    public ServicesIntegrationService(String booklyUser, String booklyPassword, String parklyUrl, String carlyUrl, String flatlyUrl) {
        this.booklyUser = booklyUser;
        this.booklyPassword = booklyPassword;

        this.parklyUrl = parklyUrl;
        this.carlyUrl = carlyUrl;
        this.flatlyUrl = flatlyUrl;
    }

    private String getParklyToken() {
        // TODO: Implement!
        return null;
    }

    private String getCarlyToken() {
        // TODO: Implement!
        return null;
    }

    private String getFlatlyToken() {
        // TODO: Implement!
        return null;
    }

    public String getUrl(CompanionService service) {
        return switch (service) {
            case Parkly -> parklyUrl;
            case Carly -> carlyUrl;
            case Flatly -> flatlyUrl;
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
