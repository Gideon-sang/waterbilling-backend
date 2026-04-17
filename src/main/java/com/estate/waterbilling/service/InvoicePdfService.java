package com.estate.waterbilling.service;


import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class InvoicePdfService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillRepository billRepository;

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font TOTAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);

    public byte[] generateInvoicePdf(Integer memberId) throws Exception {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        List<Bill> bills = billRepository.findByMemberId(memberId);

        if (bills.isEmpty()) {
            throw new RuntimeException("No bills found for member ID: " + memberId);
        }

        // Calculate totals
        double totalPaid = bills.stream()
                .filter(Bill::isPaid)
                .mapToDouble(Bill::getAmount)
                .sum();

        double totalUnpaid = bills.stream()
                .filter(b -> !b.isPaid())
                .mapToDouble(Bill::getAmount)
                .sum();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        document.open();

        // ✅ Estate header
        Paragraph estateTitle = new Paragraph("ESTATE WATER BILLING SYSTEM", TITLE_FONT);
        estateTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(estateTitle);

        Paragraph subtitle = new Paragraph("Water Bill Invoice", 
                new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(Chunk.NEWLINE);

        // ✅ Horizontal line
        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);

        // ✅ Invoice details + member details side by side
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);

        // Left — member details
        PdfPCell memberCell = new PdfPCell();
        memberCell.setBorder(Rectangle.NO_BORDER);
        memberCell.addElement(new Paragraph("BILLED TO:", BOLD_FONT));
        memberCell.addElement(new Paragraph(member.getName(), BOLD_FONT));
        memberCell.addElement(new Paragraph("House No: " + member.getHouseNumber(), NORMAL_FONT));
        memberCell.addElement(new Paragraph("Meter No: " + member.getMeterNumber(), NORMAL_FONT));
        memberCell.addElement(new Paragraph("Phone: " + member.getPhone(), NORMAL_FONT));
        infoTable.addCell(memberCell);

        // Right — invoice details
        PdfPCell invoiceCell = new PdfPCell();
        invoiceCell.setBorder(Rectangle.NO_BORDER);
        invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        String invoiceNumber = "INV-" + LocalDate.now().getYear()
                + "-" + String.format("%02d", LocalDate.now().getMonthValue())
                + "-" + String.format("%03d", memberId);
        invoiceCell.addElement(new Paragraph("Invoice No: " + invoiceNumber, BOLD_FONT));
        invoiceCell.addElement(new Paragraph("Date: " + LocalDate.now(), NORMAL_FONT));
        invoiceCell.addElement(new Paragraph("Due Date: " + LocalDate.now().plusDays(30), NORMAL_FONT));
        invoiceCell.addElement(new Paragraph("Status: " + 
                (member.getInArrears() ? "IN ARREARS" : "UP TO DATE"), 
                member.getInArrears() ? 
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED) : 
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 150, 0))));
        infoTable.addCell(invoiceCell);

        document.add(infoTable);
        document.add(Chunk.NEWLINE);

        // ✅ Bills table
        PdfPTable billTable = new PdfPTable(5);
        billTable.setWidthPercentage(100);
        billTable.setWidths(new float[]{1.5f, 2f, 1.5f, 1.5f, 1.5f});

        // Table headers
        String[] headers = {"Bill Date", "Units Used", "Amount (KES)", "Arrears (KES)", "Status"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(new BaseColor(41, 128, 185));
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            billTable.addCell(cell);
        }

        // Table rows
        for (Bill bill : bills) {
            billTable.addCell(createCell(bill.getBillDate().toString(), false));
            billTable.addCell(createCell(String.valueOf(bill.getUnitsUsed()), false));
            billTable.addCell(createCell(String.format("%.2f", bill.getAmount()), false));
            billTable.addCell(createCell(String.format("%.2f", bill.getArrears()), false));

            PdfPCell statusCell = new PdfPCell(new Phrase(
                    bill.isPaid() ? "PAID" : "UNPAID",
                    bill.isPaid() ?
                    new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0, 150, 0)) :
                    new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.RED)));
            statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            statusCell.setPadding(6);
            billTable.addCell(statusCell);
        }

        document.add(billTable);
        document.add(Chunk.NEWLINE);

        // ✅ Summary totals
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(40);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        addSummaryRow(summaryTable, "Total Paid:", String.format("KES %.2f", totalPaid), false);
        addSummaryRow(summaryTable, "Total Unpaid:", String.format("KES %.2f", totalUnpaid), false);
        addSummaryRow(summaryTable, "AMOUNT DUE:", String.format("KES %.2f", totalUnpaid), true);

        document.add(summaryTable);
        document.add(Chunk.NEWLINE);

        // ✅ Footer
        document.add(new Chunk(line));
        Paragraph footer = new Paragraph(
                "Thank you for your prompt payment. For inquiries contact the estate office.",
                new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
        return baos.toByteArray();
    }

    private PdfPCell createCell(String text, boolean bold) {
        PdfPCell cell = new PdfPCell(new Phrase(text, bold ? BOLD_FONT : NORMAL_FONT));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }

    private void addSummaryRow(PdfPTable table, String label, String value, boolean isTotal) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, isTotal ? TOTAL_FONT : BOLD_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(4);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, isTotal ? TOTAL_FONT : NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(4);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }
}