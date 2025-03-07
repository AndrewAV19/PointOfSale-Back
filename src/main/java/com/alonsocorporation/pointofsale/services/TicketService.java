package com.alonsocorporation.pointofsale.services;

import com.alonsocorporation.pointofsale.dto.response.SaleProductDTO;
import com.alonsocorporation.pointofsale.dto.response.SalesDTO;
import com.alonsocorporation.pointofsale.entities.DataPointOfSale;
import com.alonsocorporation.pointofsale.repositories.DataPointOfSaleRepository;
import org.springframework.stereotype.Service;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class TicketService {

    private final DataPointOfSaleRepository dataPointOfSaleRepository;

    public TicketService(DataPointOfSaleRepository dataPointOfSaleRepository) {
        this.dataPointOfSaleRepository = dataPointOfSaleRepository;
    }

    public void printTicket(SalesDTO sale) {
        // Obtener los datos del negocio
        DataPointOfSale businessData = dataPointOfSaleRepository.findById(1L) // Suponiendo que el ID del negocio es 1
                .orElseThrow(() -> new RuntimeException("Business data not found"));
    
        // Verificar si se debe imprimir el ticket
        if (!businessData.getPrintTicket()) {
            System.out.println("========================================");
            System.out.println("La impresión de tickets está desactivada.");
            System.out.println("========================================");
            return; // Salir del método si no se debe imprimir el ticket
        }
    
        // Calcular el cambio
        double change = sale.getAmount() - sale.getTotal();
    
        // Formatear la fecha
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy, hh:mm a", new Locale("es", "ES"));
        String formattedDate = sale.getCreatedAt().format(dateFormatter);
    
        // Formatear el ticket
        StringBuilder ticket = new StringBuilder();
        ticket.append("========================================\n");
        ticket.append("           ").append(businessData.getName()).append("\n");
        ticket.append("========================================\n");
        ticket.append("Dirección: ").append(businessData.getAddress()).append("\n");
        ticket.append("Teléfono: ").append(businessData.getPhone()).append("\n");
        ticket.append("========================================\n");
        ticket.append("Ticket de Venta #").append(sale.getId()).append("\n");
        ticket.append("Fecha: ").append(formattedDate).append("\n");
        ticket.append("Cliente: ").append(sale.getClient() != null ? sale.getClient().getName() : "N/A").append("\n");
        ticket.append("----------------------------------------\n");
        ticket.append("Productos:\n");
    
        for (SaleProductDTO product : sale.getSaleProducts()) {
            ticket.append(String.format(" - %-20s x %-3d $%.2f\n",
                    product.getProduct().getName(),
                    product.getQuantity(),
                    product.getProduct().getPrice() * product.getQuantity()));
        }
    
        ticket.append("----------------------------------------\n");
        ticket.append(String.format("Total: $%.2f\n", sale.getTotal()));
        ticket.append(String.format("Monto recibido: $%.2f\n", sale.getAmount()));
        ticket.append(String.format("Cambio: $%.2f\n", change));
        ticket.append("========================================\n");
        ticket.append("¡Gracias por su compra!\n");
        ticket.append("========================================\n");
    
        System.out.println(ticket.toString());
        // Imprimir el ticket en la impresora
        printToPrinter(ticket.toString());
    }

    private void printToPrinter(String ticketContent) {
        try {
            // Obtener la impresora predeterminada
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService == null) {
                // Si no hay impresora, registrar un mensaje de advertencia y salir
                System.out.println("Advertencia: No se encontró ninguna impresora configurada.");
                return; // Salir del método sin lanzar una excepción
            }
    
            // Convertir el contenido del ticket a bytes (formato de texto plano)
            byte[] bytes = ticketContent.getBytes("UTF-8");
    
            // Crear un documento de impresión
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            SimpleDoc doc = new SimpleDoc(new ByteArrayInputStream(bytes), flavor, null);
    
            // Configurar atributos de impresión (por ejemplo, número de copias)
            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(new Copies(1));
    
            // Crear un trabajo de impresión y enviar el documento a la impresora
            DocPrintJob printJob = printService.createPrintJob();
            printJob.print(doc, attributes);
    
            System.out.println("Ticket enviado a la impresora correctamente.");
        } catch (PrintException | UnsupportedEncodingException e) {
            // Registrar el error sin lanzar una excepción
            System.err.println("Error al imprimir el ticket: " + e.getMessage());
        }
    }
}