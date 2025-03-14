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
            
            float width = 280f;
            float height = 120f;
            Document document = new Document(new Rectangle(width, height));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            Font bold = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
            Font regular = new Font(Font.FontFamily.HELVETICA, 7);

            Paragraph productInfo = new Paragraph();

            if (product.getDiscount() != null && product.getDiscount() > 0) {
                productInfo.add(new Phrase("Oferta!!!" + "\n", bold));
            }

            productInfo.add(new Phrase(product.getName() + "\n", bold));

            productInfo.add(new Phrase("Precio: $" + product.getPrice() + "\n", regular));

            if (product.getDiscount() != null && product.getDiscount() > 0) {
                productInfo.add(new Phrase("Descuento: " + product.getDiscount() + "%\n", regular));

                double precioConDescuento = product.getPrice() * (1 - (product.getDiscount() / 100));
                productInfo.add(new Phrase("Precio con descuento: $" + String.format("%.2f", precioConDescuento) + "\n", regular));
            }

            productInfo.add(new Phrase("Código de Barras: " + product.getBarCode(), regular));

            PdfPCell productCell = new PdfPCell(productInfo);
            productCell.setBorder(Rectangle.NO_BORDER);

            productCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            if (product.getDiscount() != null && product.getDiscount() > 0) {
                productCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            } else {
                productCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            }

            table.addCell(productCell);

            byte[] qrBytes = Base64.getDecoder().decode(product.getQrCode());
            Image qrImage = Image.getInstance(qrBytes);
            qrImage.scaleToFit(50, 50);

            PdfPCell qrCell = new PdfPCell(qrImage, true);
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(qrCell);

            document.add(table);

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