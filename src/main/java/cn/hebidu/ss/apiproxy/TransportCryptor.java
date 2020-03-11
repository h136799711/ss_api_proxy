package cn.hebidu.ss.apiproxy;

import java.util.Map;

public interface TransportCryptor {

    String decryptTransmissionData(String encrypt, String key);

    String encryptTransmissionData(Map<String, String> param, String key);

    boolean verify_sign(Map<String, String> param, String sign);

    String sign(Map<String, String> param);

    Map<String, String> decryptData(String data);

    String encryptData(Map<String, String> data);
}
