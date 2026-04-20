package com.josamtechie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrReadResponse {
    private String status;
    private String decodedText;
    private UserDetails userDetails;
}
