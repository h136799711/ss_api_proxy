package cn.hebidu.ss.apiproxy;

import com.github.peterasasi.cm.core.exception.CryptException;
import com.github.peterasasi.cm.core.toolkits.Base64Utils;
import com.github.peterasasi.cm.core.toolkits.DesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@SpringBootTest
class ApiProxyApplicationTests {

//	@Test
//	void contextLoads() throws Exception {
//		String decrypt = null;
//		try {
//			String encrypt = Base64Utils.encode(DesUtils.encrypt("888999", "cb0bfaf5"));
//			System.out.println(encrypt);
//			decrypt = DesCbcUtils.decrypt(Base64Utils.decodeBuffer("6f3Y3ZEUezU="), "cb0bfaf5");
//			System.out.println(decrypt);
//			Assertions.assertEquals("888999", decrypt);
//		} catch (CryptException e) {
//			e.printStackTrace();
//		}
//	}


}
