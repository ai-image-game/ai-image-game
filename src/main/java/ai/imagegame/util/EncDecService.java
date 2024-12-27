package ai.imagegame.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncDecService {
    private final SecretKeySpec secretKeySpec;
    private final ObjectMapper objectMapper;

    public EncDecService(@Value("${secret.key}") String secretKey, ObjectMapper objectMapper) {
        // AES 16바이트 키 생성
        byte[] decodedKey = Arrays.copyOf(secretKey.getBytes(StandardCharsets.UTF_8), 16);
        this.secretKeySpec = new SecretKeySpec(decodedKey, "AES");
        this.objectMapper = objectMapper;
    }

    public <T> String encrypt(T data) throws Exception {
        // 객체를 JSON 문자열로 변환
        String jsonData = objectMapper.writeValueAsString(data);

        // Cipher 설정
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        // 데이터 암호화
        byte[] encryptedBytes = cipher.doFinal(jsonData.getBytes(StandardCharsets.UTF_8));

        // Base64로 인코딩 후 반환
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public <T> T    decrypt(String encryptedData, Class<T> clazz) throws Exception {
        // Cipher 설정
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        // Base64 디코딩 후 데이터 복호화
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

        // JSON 문자열로 변환된 결과를 객체로 반환
        String result = new String(decryptedBytes, StandardCharsets.UTF_8);
        return objectMapper.readValue(result, clazz);
    }
}
