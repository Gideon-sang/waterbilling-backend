package com.estate.waterbilling.service;

import com.estate.waterbilling.model.Bill;
import com.estate.waterbilling.model.Member;
import com.estate.waterbilling.repository.BillRepository;
import com.estate.waterbilling.repository.MemberRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportPdfService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BillRepository billRepository;

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font TOTAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.DARK_GRAY);

    public byte[] generateArrearsReportPdf() throws Exception {

        // ✅ Get all members with arrears
        List<Member> membersWithArrears = memberRepository.findByInArrears(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        document.open();

        // ✅ Title
        Paragraph title = new Paragraph("ESTATE WATER BILLING SYSTEM", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("Arrears Report — " + LocalDate.now(),
                new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.GRAY));
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);

        document.add(Chunk.NEWLINE);

        LineSeparator line = new LineSeparator();
        line.setLineColor(BaseColor.LIGHT_GRAY);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);

        // ✅ Summary
        Paragraph summary = new Paragraph(
                "Total Members in Arrears: " + membersWithArrears.size(), BOLD_FONT);
        document.add(summary);
        document.add(Chunk.NEWLINE);

        if (membersWithArrears.isEmpty()) {
            Paragraph noArrears = new Paragraph(
                    "No members are currently in arrears.", NORMAL_FONT);
            noArrears.setAlignment(Element.ALIGN_CENTER);
            document.add(noArrears);
        } else {

            // ✅ Arrears table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2f, 1.5f, 1.5f, 2f});

            // Table headers
            String[] headers = {"House No", "Name", "Meter No", "Phone", "Total Arrears (KES)"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
                cell.setBackgroundColor(new BaseColor(192, 57, 43)); // ✅ Red for arrears
                cell.setPadding(8);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            // Table rows
            double grandTotal = 0.0;
            for (Member member : membersWithArrears) {

                // Calculate total unpaid for this member
                List<Bill> unpaidBills = billRepository.findByMemberAndPaidFalse(member);
                double totalArrears = unpaidBills.stream()
                        .mapToDouble(Bill::getAmount)
                        .sum();
                grandTotal += totalArrears;

                table.addCell(createCell(member.getHouseNumber(), false));
                table.addCell(createCell(member.getName(), false));
                table.addCell(createCell(member.getMeterNumber(), false));
                table.addCell(createCell(member.getPhone(), false));
                table.addCell(createCell(String.format("%.2f", totalArrears), false));
            }

            document.add(table);
            document.add(Chunk.NEWLINE);

            // ✅ Grand total
            PdfPTable totalTable = new PdfPTable(2);
            totalTable.setWidthPercentage(40);
            totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell totalLabel = new PdfPCell(new Phrase("GRAND TOTAL ARREARS:", TOTAL_FONT));
            totalLabel.setBorder(Rectangle.NO_BORDER);
            totalLabel.setPadding(4);
            totalTable.addCell(totalLabel);

            PdfPCell totalValue = new PdfPCell(
                    new Phrase(String.format("KES %.2f", grandTotal), TOTAL_FONT));
            totalValue.setBorder(Rectangle.NO_BORDER);
            totalValue.setPadding(4);
            totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalTable.addCell(totalValue);

            document.add(totalTable);
        }

        document.add(Chunk.NEWLINE);

        // ✅ Footer
        document.add(new Chunk(line));
        Paragraph footer = new Paragraph(
                "Generated on " + LocalDate.now() + " — Estate Water Billing System",
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
}
