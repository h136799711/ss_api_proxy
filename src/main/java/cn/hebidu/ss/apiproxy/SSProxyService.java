package cn.hebidu.ss.apiproxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peterasasi.cm.core.toolkits.Base64Utils;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SSProxyService {
    private ObjectMapper objectMapper;
    private String apiUrl;
    private String clientId;
    private String clientSecret;
    private TransportCryptor transportCryptor;

    public SSProxyService() {
        objectMapper = new ObjectMapper();
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public SSProxyService(String apiUrl, String clientId, String clientSecret) {
        this.apiUrl = apiUrl;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public SSProxyService setTransportCryptor(TransportCryptor transportCryptor) {
        this.transportCryptor = transportCryptor;
        return this;
    }

    public String request(Map<String, String> data) throws IOException {

        if (StringUtils.isEmpty(this.clientId)) {
            return "param-need client_id";
        }
        if (StringUtils.isEmpty(data.getOrDefault("type", ""))) {
            return "param-need type";
        }

        if (StringUtils.isEmpty(data.getOrDefault("api_ver", ""))) {
            return "param-need api_ver";
        }

        String notifyId = String.valueOf(System.currentTimeMillis() / 1000);
        String alg = data.getOrDefault("alg", "");
        String type = data.getOrDefault("type", "");
        String apiVer = data.getOrDefault("api_ver", "100");
        if (StringUtils.isEmpty(apiVer)) apiVer = "100";

        data.remove("alg");
        data.remove("type");
        data.remove("api_ver");

        String encryptData = this.transportCryptor.encryptData(data);

        Map<String, String> params = new HashMap<>();
        params.put("client_secret", this.clientSecret);
        params.put("api_ver", apiVer);
        params.put("notify_id", notifyId);
        params.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("data", encryptData);
        params.put("type", type);
        params.put("alg", alg);

        params.put("sign", this.transportCryptor.sign(params));
        params.put("client_id", this.clientId);

        params.remove("client_secret");

        encryptData = this.transportCryptor.encryptTransmissionData(params, clientSecret);

        Form form = Form.form()
                .add("client_id", clientId)
                .add("alg", alg)
                .add("app_version", "1000")
                .add("app_type", "test")
                .add("itboye", Base64Utils.encode(encryptData));

        System.out.println("Request Url = " + apiUrl);
        form.build().forEach(pair -> {
            System.out.println("params" + pair.getName() + " value = " + pair.getValue());
        });

        String resp = Request.Post(URI.create(apiUrl))
                .useExpectContinue()
                .bodyForm(form.build())
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Content-type", "application/json")
                .connectTimeout(5000)
                .execute().returnContent().asString(Charset.forName("utf-8"));
        return this.parseResp(resp);
    }

    @SuppressWarnings("unchecked")
    private String parseResp(String resp) {
        try {
            Map<String, String> respMap = objectMapper.readValue(resp, HashMap.class);
            String data = respMap.getOrDefault("data", "");
            if (StringUtils.isEmpty(data)) return  "解析data为空";
            return objectMapper.writeValueAsString(this.transportCryptor.decryptData(data));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "无法解析" + resp;
        }

    }
}
