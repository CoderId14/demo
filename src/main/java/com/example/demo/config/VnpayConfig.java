package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Configuration
@PropertySource("classpath:application-local.yaml")
public class VnpayConfig  {
  @Value("${vnp.vnp_IpAddr}")
  public String vnp_IpAddr;
  @Value("${vnp.orderType}")
  public String orderType;
  @Value("${vnp.vnp_Command}")
  public String vnp_Command;
  @Value("${vnp.vnp_Version}")
  public String vnp_Version;
  @Value("${vnp.vnp_PayUrl}")
  public String vnp_PayUrl;
  @Value("${vnp.vnp_Returnurl}")
  public String vnp_Returnurl;
  @Value("${vnp.vnp_TmnCode}")
  public String vnp_TmnCode;
  @Value("${vnp.vnp_HashSecret}")
  public String vnp_HashSecret;
  @Value("${vnp.vnp_apiUrl}")
  public String vnp_apiUrl;

  //  key = secret. amount. dateTime. sku
  public static String getRandomNumber(Long userId) throws Exception {
    return UUID.randomUUID()+userId.toString();
  }
  public static String getIpAddress(HttpServletRequest request) {
    String ipAdress;
    try {
      ipAdress = request.getHeader("X-FORWARDED-FOR");
      if (ipAdress == null) {
        ipAdress = request.getLocalAddr();
      }
    } catch (Exception e) {
      ipAdress = "Invalid IP:" + e.getMessage();
    }
    return ipAdress;
  }
  public static String hmacSHA512(final String key, final String data) {
    try {

      if (key == null || data == null) {
        throw new NullPointerException();
      }
      final Mac hmac512 = Mac.getInstance("HmacSHA512");
      byte[] hmacKeyBytes = key.getBytes();
      final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
      hmac512.init(secretKey);
      byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
      byte[] result = hmac512.doFinal(dataBytes);
      StringBuilder sb = new StringBuilder(2 * result.length);
      for (byte b : result) {
        sb.append(String.format("%02x", b & 0xff));
      }
      return sb.toString();

    } catch (Exception ex) {
      return "";
    }
  }
}
