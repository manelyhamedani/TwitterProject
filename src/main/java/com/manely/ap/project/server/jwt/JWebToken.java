package com.manely.ap.project.server.jwt;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JWebToken {

   private static final String JWT_HEADER = new Gson().toJson(new Header("HS256", "JWT"));
   private static final String ALGORITHM = SignatureAlgorithm.HS256.getValue();
   private static final String SECRET_KEY = "mfsKfoTfuPorteLksTiwW24slKl9skDsj03lsLLhdKmaNua777sldGbo";
   private static final long EXP_IN = 10 * 24 * 60 * 60 * 1000; // 10 days in milliseconds

   private JWebToken() {

   }

   public static String generate(String sub, int id, String aud) {
       long currentTime = System.currentTimeMillis();
       Payload payload = new Payload(sub, id, aud, currentTime, currentTime + EXP_IN);
       String jwtPayload = new Gson().toJson(payload);

       String encodedPayload = encode(jwtPayload.getBytes(StandardCharsets.UTF_8));
       String encodedHeader = encode(JWT_HEADER.getBytes(StandardCharsets.UTF_8));
       try{
           String signature = hmacSha256((encodedHeader + "." + encodedPayload).getBytes(StandardCharsets.UTF_8));
           return encodedHeader + "." + encodedPayload + "." + signature;
       }
       catch (NoSuchAlgorithmException | InvalidKeyException e) {
           throw new JwtException("Encoding Exception!", e);
       }
   }

   public static boolean isValid(String jwt) {
       if (jwt == null)
           return false;
       String[] parts = jwt.split("\\.");
       if (parts.length != 3) {
           return false;
       }
       try {
           String signature = hmacSha256((parts[0] + "." + parts[1]).getBytes(StandardCharsets.UTF_8));
           if (!signature.equals(parts[2])) {
               return false;
           }
           Payload payload = getPayload(jwt);
           long exp = payload.getExp();
           long currentTime = System.currentTimeMillis();
           if (currentTime > exp) {
               return false;
           }
       }
       catch (NoSuchAlgorithmException | InvalidKeyException e) {
           throw new JwtException("Decoding Exception!", e);
       }
       return true;
   }

   public static Payload getPayload(String jwt) {
       String[] parts = jwt.split("\\.");
       if (parts.length != 3) {
           throw new JwtException("Invalid Token!");
       }
       return new Gson().fromJson(decode(parts[1]), Payload.class);
   }

   private static String encode(byte[] data) {
       return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
   }

   private static String decode(String encodedStr) {
       return new String(Base64.getUrlDecoder().decode(encodedStr));
   }

   private static String hmacSha256(byte[] data) throws NoSuchAlgorithmException, InvalidKeyException{
       Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
       SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
       hmacSHA256.init(secretKey);
       byte[] result = hmacSHA256.doFinal(data);

       return encode(result);
   }

}
