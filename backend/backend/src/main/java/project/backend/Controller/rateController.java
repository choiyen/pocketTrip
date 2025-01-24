package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.DTO.ResponseDTO;
import project.backend.Service.RateService;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/rate")
public class rateController {

    @Autowired
    private RateService rateService;

    private ResponseDTO responseDTO = new ResponseDTO<>();


    @GetMapping
    public ResponseEntity<?> RatePost()
    {
        try
        {


            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {return null;}
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session){
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            String rate = rateService.getObject();
            List list = new ArrayList<>();
            list.add(rate);
            return ResponseEntity.ok().body(responseDTO.Response("success","환율 전송 완료", list));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(responseDTO.Response("error", e.getMessage()));
        }

    }
}
