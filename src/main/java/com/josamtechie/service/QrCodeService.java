package com.josamtechie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.josamtechie.model.QrReadResponse;
import com.josamtechie.model.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

@Service
public class QrCodeService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generates a QR code image (PNG) from UserDetails object.
     *
     * @param userDetails the user details to encode
     * @param width       QR code image width in pixels
     * @param height      QR code image height in pixels
     * @return byte array of the PNG image
     */
    public byte[] generateQrCode(UserDetails userDetails, int width, int height) throws Exception {
        // Serialize UserDetails to JSON string
        String jsonContent = objectMapper.writeValueAsString(userDetails);

        // Configure QR code hints
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);

        // Generate QR code bit matrix
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(jsonContent, BarcodeFormat.QR_CODE, width, height, hints);

        // Convert BitMatrix to PNG byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Reads and decodes a QR code image uploaded as MultipartFile.
     *
     * @param file the uploaded QR code image file
     * @return QrReadResponse containing decoded text and parsed UserDetails
     */
    public QrReadResponse readQrCode(MultipartFile file) throws Exception {
        // Read image from uploaded file
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        if (bufferedImage == null) {
            throw new IllegalArgumentException("Invalid image file. Could not read the uploaded image.");
        }

        // Convert image to luminance source for ZXing
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

        // Configure decode hints
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        // Decode QR code
        MultiFormatReader reader = new MultiFormatReader();
        Result result = reader.decode(binaryBitmap, hints);

        String decodedText = result.getText();

        // Try to parse decoded text back into UserDetails
        UserDetails userDetails = null;
        try {
            userDetails = objectMapper.readValue(decodedText, UserDetails.class);
        } catch (Exception e) {
            // Decoded text is plain string, not JSON — return as-is
        }

        return new QrReadResponse("SUCCESS", decodedText, userDetails);
    }
}
