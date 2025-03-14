package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.dto.response.ProductDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QRCodeLabelService {

    public ResponseEntity<byte[]> generateLabel(ProductDTO product) {
        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("Producto: " + product.getName(), bold));
            document.add(new Paragraph("Precio: $" + product.getPrice()));
            document.add(new Paragraph("Código de Barras: " + product.getBarCode()));

            byte[] qrBytes = Base64.getDecoder().decode(product.getQrCode());
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(100, 100);
            document.add(qrImage);

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "etiqueta_" + product.getId() + ".pdf");

            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al generar la etiqueta", e);
        }
    }
}
