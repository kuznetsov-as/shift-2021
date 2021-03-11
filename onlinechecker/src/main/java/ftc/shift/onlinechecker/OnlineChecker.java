package ftc.shift.onlinechecker;

import ftc.shift.onlinechecker.exception.LicenceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OnlineChecker {
    public static boolean isLicenceAvailable(String url, String licence) throws IOException, InterruptedException, LicenceException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url + "/licences/check"))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(licence))
                .build();

        var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return true;
        } else {
            throw new LicenceException(response.body());
        }
    }
}
