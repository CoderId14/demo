package com.example.demo.config;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

public class VnpayConfig  {
  public static String vnp_IpAddr = "0.0.0.0.0.1";
  public static String orderType = "billpayment";
  public static String vnp_Command = "pay";
  public static String vnp_Version = "2.1.0";
  public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
  public static String vnp_Returnurl = "http://localhost:8081/api/user/v1/save-coin";
  public static String vnp_TmnCode = "OKVQCIMD";
  public static String vnp_HashSecret = "AVKGBRLEQDXSXICCGAFSBYQNVVZZGICP";
  public static String vnp_apiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

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
