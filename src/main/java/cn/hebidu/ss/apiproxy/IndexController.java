package cn.hebidu.ss.apiproxy;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class IndexController {

    private SSProxyService ssProxyService;

    public IndexController(SSProxyService ssProxyService, TransportCryptor transportCryptor) {
        this.ssProxyService = ssProxyService;
        this.ssProxyService.setTransportCryptor(transportCryptor);
    }

    @PostMapping("public/read")
    public String readJson(@RequestParam(value="file_url") String url) throws InterruptedException, IOException {
        return EntityUtils.toString(Request
                .Get(url)
                .execute().returnResponse().getEntity(), "utf-8");
    }

    @PostMapping("public/api")
    public String index(HttpServletRequest request) {
//        String alg = request.getParameter("alg");
        String apiUrl = request.getParameter("api_url");
        String clientId = request.getParameter("client_id");
        String clientSecret = request.getParameter("client_secret");
        String apiType = request.getParameter("api_type");
//        String apiVer = request.getParameter("api_ver");

        Map<String, String> params = ofParams(request.getParameterMap());

        params.put("type", apiType);

        params.remove("client_id");
        params.remove("client_secret");
        params.remove("api_url");

        this.ssProxyService.setApiUrl(apiUrl);
        this.ssProxyService.setClientId(clientId);
        this.ssProxyService.setClientSecret(clientSecret);
        params.forEach((k, v) -> log.debug(k + " = " + v));

        try {
            return this.ssProxyService.request(params);
        } catch (IOException e) {
            e.printStackTrace();
            return  e.getMessage();
        }
    }

    private Map<String, String> ofParams(Map<String, String[]> map) {
        Map<String, String> params = new HashMap<>();
        map.forEach((k, v) -> params.put(k, v[0]));
        return params;
    }
}
