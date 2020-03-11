package cn.hebidu.ss.apiproxy;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class DesCbcUtils {

    /**
     * Encryption tool
     */
    private Cipher encryptCipher;

    /**
     * Decryption tool
     */
    private Cipher decryptCipher;

    /**
     * Convert a byte array to a string representing a hexadecimal value, such as: byte[]{8,18} converted to: 0813 , and public static
     * byte[] hexStr2ByteArr(String strIn) mutually reversible conversion process
     *
     * @param arrB byte array to be converted
     * @return converted string
     * @throws Exception This method does not handle any exceptions, all exceptions are thrown
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // Each byte can be represented by two characters, so the length of the string is twice the length of the array.
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // Convert negative numbers to positive numbers
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // The number less than 0F needs to be 0 in front.
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * Converts a string representing a 16-value value to a byte array, and public static String
     * byteArr2HexStr(byte[] arrB) mutually reversible conversion process
     *
     * @param strIn string to be converted
     * @return converted byte array
     * @throws Exception This method does not handle any exceptions, all exceptions are thrown
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // two characters represent one byte, so the length of the byte array is the length of the string divided by 2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * Generate a key from the specified string. The length of the byte array required for the key is 8 bits. When the number is less than 8 bits, it is followed by 0. If it exceeds 8 bits, only the first 8 bits are taken.
     *
     * @param arrBTmp constitutes a byte array of the string
     * @return generated key
     * @throws java.lang.Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // Create an empty 8-bit byte array (default is 0)
        byte[] arrB = new byte[8];
        //Convert the original byte array to 8 bits
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // Generate a key
        Key key = new SecretKeySpec(arrB, "DES");
        return key;
    }

    public static byte[] encrypt(String strIn, String secretKey) throws Exception {
        Key key = getKey(secretKey.getBytes("utf-8"));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return (cipher.doFinal(strIn.getBytes("utf-8")));
    }

    public static String decrypt(byte[] strBytes, String secretKey) throws Exception {
//        int padLen = strBytes.length() % 8;

//        byte[] bytes = new byte[strIn.length() + (8 - padLen)];
//        for (int i = 0; i < bytes.length; i++) {
//            if (i < strBytes.length) {
//                bytes[i] = strBytes[i];
//            } else {
//                bytes[i] = '\0';
//            }
//        }
        Key key = getKey(secretKey.getBytes("utf-8"));
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] param = cipher.doFinal(strBytes);
        return new String(param, "utf-8");
    }
}
