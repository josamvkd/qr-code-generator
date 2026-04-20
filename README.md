# QR Code Generator & Reader — Spring Boot 4

A lightweight REST API application built with **Java 21** and **Spring Boot 4** 
that generates QR codes from user bank account details and decodes QR code 
images back into structured data using the **Google ZXing** library.

## Features
- Generate QR codes from user details (name, mobile, account type & number)
- Decode/read uploaded QR code images back into JSON
- Configurable QR image size via query parameters
- High error correction (Level H) for reliable scanning
- Clean layered architecture — Model → Service → Controller

## Tech Stack
- Java 21
- Spring Boot 4
- Google ZXing 3.5.3
- Lombok
- Maven

## API Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/qr/generate` | Generate QR code PNG from user details |
| POST | `/api/qr/read` | Decode a QR code image and return JSON |

## Quick Start
\`\`\`bash
git clone https://github.com/your-username/qr-code-generator.git
cd qr-code-app
mvn spring-boot:run
\`\`\`
Server starts at: `http://localhost:8080`
