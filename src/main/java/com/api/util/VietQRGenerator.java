package com.api.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.MultiFormatWriter;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;

public class VietQRGenerator {

    // Tạo TLV format: ID + length + value
    private static String buildTLV(String id, String value) {
        return id + String.format("%02d", value.length()) + value;
    }

 public static String generateVietQrPayload(String binCode, String accountNumber, long amount) {
    String gui = buildTLV("00", "A000000727");
    String bank = buildTLV("01", binCode);
    String acc = buildTLV("02", accountNumber);
    String merchantInfo = buildTLV("38", gui + bank + acc);
    String currency = buildTLV("53", "704");
    String money = buildTLV("54", String.valueOf(amount));
    String country = buildTLV("58", "VN");

    // Tổng hợp
    String payloadWithoutCRC = buildTLV("00", "01")        // Payload Format Indicator
                              + buildTLV("01", "12")        // Point of Initiation
                              + merchantInfo
                              + currency
                              + money
                              + country
                              + "6304";                     // CRC Placeholder

    String crc = getCRC(payloadWithoutCRC);
    return payloadWithoutCRC + crc;
}


    // Tính CRC16-CCITT theo chuẩn EMVCo
    private static String getCRC(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        int crc = 0xFFFF;

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0)
                    crc = (crc << 1) ^ 0x1021;
                else
                    crc <<= 1;
            }
            crc &= 0xFFFF;
        }

        return String.format("%04X", crc);
    }

    public static byte[] generateQRImage(String payload) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(payload, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", stream);
        return stream.toByteArray();
    }
}
