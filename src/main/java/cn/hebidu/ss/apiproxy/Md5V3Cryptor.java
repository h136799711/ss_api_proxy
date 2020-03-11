package cn.hebidu.ss.apiproxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.peterasasi.cm.core.exception.CryptException;
import com.github.peterasasi.cm.core.toolkits.Base64Utils;
import com.github.peterasasi.cm.core.toolkits.DesUtils;
import com.github.peterasasi.cm.core.toolkits.Md5Utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Md5V3Cryptor implements TransportCryptor {

    private ObjectMapper objectMapper;

    public Md5V3Cryptor() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String decryptTransmissionData(String encrypt, String key) {

//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.readValue(encrypt)
        return null;
    }

    @Override
    public String encryptTransmissionData(Map<String, String> param, String key) {
        try {
            String jsonStr = objectMapper.writeValueAsString(param);
            return DesUtils.encrypt(jsonStr, key.substring(0, 8));
        } catch (JsonProcessingException | CryptException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public boolean verify_sign(Map<String, String> param, String sign) {
        return sign(param).equalsIgnoreCase(sign);
    }

    @Override
    public String sign(Map<String, String> param) {

        String stringBuilder = param.getOrDefault("time", "") +
                param.getOrDefault("type", "") +
                param.getOrDefault("notify_id", "") +
                param.getOrDefault("client_secret", "") +
                param.getOrDefault("data", "") +
                param.getOrDefault("time", "");
        try {
            return Md5Utils.encodeLowercase(stringBuilder);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> decryptData(String data) {
        String json = Base64Utils.decode(Base64Utils.decode(data));
        try {
            return objectMapper.readValue(json, HashMap.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public String encryptData(Map<String, String> data)  {
        try {
            String str = objectMapper.writeValueAsString(data);
            return Base64Utils.encode(Base64Utils.encode(str));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
