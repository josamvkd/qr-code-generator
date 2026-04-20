package com.josamtechie.controller;

import com.josamtechie.model.QrReadResponse;
import com.josamtechie.model.UserDetails;
import com.josamtechie.service.QrCodeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/qr")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * API 1: Generate QR Code
     * POST /api/qr/generate?width=300&height=300
     * Body: UserDetails JSON
     * Returns: PNG image
     */
    @PostMapping(
            value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> generateQrCode(
            @RequestBody UserDetails userDetails,
            @RequestParam(defaultValue = "300") int width,
            @RequestParam(defaultValue = "300") int height) throws Exception {

        byte[] qrImage = qrCodeService.generateQrCode(userDetails, width, height);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrImage.length);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"");

        return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
    }

    /**
     * API 2: Read / Decode QR Code Image
     * POST /api/qr/read
     * Body: multipart/form-data with key "file"
     * Returns: JSON with decoded text and UserDetails
     */
    @PostMapping(
            value = "/read",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<QrReadResponse> readQrCode(
            @RequestParam("file") MultipartFile file) throws Exception {

        QrReadResponse response = qrCodeService.readQrCode(file);
        return ResponseEntity.ok(response);
    }
}
