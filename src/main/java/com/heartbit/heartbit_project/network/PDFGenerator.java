package com.heartbit.heartbit_project.network;

import com.heartbit.heartbit_project.db.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PDFGenerator {
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static final BaseColor HEADER_COLOR = new BaseColor(169, 169, 169); // Light gray for headers
    private static final BaseColor ALTERNATE_ROW_COLOR = new BaseColor(211, 211, 211); // Light gray for alternating rows
    private static final BaseColor LINE_COLOR = new BaseColor(201, 114, 114); // Same color as "Bit"
    private User user;
    private Disease disease;
    private ArrayList<EmergencyReading> emergencyReadings;
    private Bpm bpmCollection;

    public PDFGenerator(User user, Disease disease, ArrayList<EmergencyReading> emergencyReadings, Bpm bpm){
        this.user = user;
        this.disease = disease;
        this.emergencyReadings = emergencyReadings;
        this.bpmCollection = bpm;
    }

    public void promptAndGeneratePDF(String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar PDF");
        chooser.setSelectedFile(new File(defaultFileName));
        int userSelection = chooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }
            generatePDF(path);
        } else {
            System.out.println("Operação cancelada pelo utilizador.");
        }
    }

    public void generatePDF(String fileName) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            addHeader(document);
            addHorizontalLine(document,10);
            addUserAndHeartInformation(document, user);
            addHorizontalLine(document,4);
            addDiseasesSection(document);
            addHorizontalLine(document,4);
            addEmergencyLogsSection(document);
            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addHeader(Document document) throws DocumentException {
        // Header with logo text and generation info
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 1});
        headerTable.setSpacingAfter(10);

        // Logo section with "Heart" in black and "Bit" in red
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph logoText = new Paragraph();
        logoText.add(new Chunk("Heart", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK)));
        logoText.add(new Chunk("Bit", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, LINE_COLOR)));
        logoCell.addElement(logoText);

        // Generation info
        PdfPCell infoCell = new PdfPCell();
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph nameInfo = new Paragraph("Name: " + user.getName(), new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        nameInfo.setAlignment(Element.ALIGN_RIGHT);

        Paragraph generationInfo = new Paragraph("Generated at: " +
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL));
        generationInfo.setAlignment(Element.ALIGN_RIGHT);

        infoCell.addElement(nameInfo);
        infoCell.addElement(generationInfo);

        headerTable.addCell(logoCell);
        headerTable.addCell(infoCell);

        document.add(headerTable);
    }

    private void addHorizontalLine(Document document, int lineWidth) throws DocumentException {
        LineSeparator line = new LineSeparator();
        line.setLineColor(LINE_COLOR);
        line.setLineWidth(lineWidth);
        document.add(line);
        document.add(new Paragraph("\n"));
    }

    private void addUserAndHeartInformation(Document document, User user) throws DocumentException {
        // Create a table with two columns for User Info and Heart Info
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{1, 1});
        infoTable.setSpacingAfter(20);

        // User Information Column
        PdfPCell userInfoCell = new PdfPCell();
        userInfoCell.setBorder(Rectangle.NO_BORDER);
        userInfoCell.setPaddingRight(20);

        Paragraph userTitle = new Paragraph("User Information", SECTION_FONT);
        userTitle.setSpacingAfter(10);
        userInfoCell.addElement(userTitle);

        Paragraph userInfo = new Paragraph();
        userInfo.add(new Chunk("Name: ", HEADER_FONT));
        userInfo.add(new Chunk(user.getName() + "\n", NORMAL_FONT));
        userInfo.add(new Chunk("Email: ", HEADER_FONT));
        userInfo.add(new Chunk(user.getEmail() + "\n", NORMAL_FONT));
        userInfo.add(new Chunk("Emergency: ", HEADER_FONT));
        userInfo.add(new Chunk(user.getEmergencyPhoneNumber(), NORMAL_FONT));

        userInfoCell.addElement(userInfo);

        // Heart Information Column
        PdfPCell heartInfoCell = new PdfPCell();
        heartInfoCell.setBorder(Rectangle.NO_BORDER);
        heartInfoCell.setPaddingLeft(20);

        Paragraph heartTitle = new Paragraph("Heart Information", SECTION_FONT);
        heartTitle.setSpacingAfter(10);
        heartInfoCell.addElement(heartTitle);

        Paragraph heartInfo = new Paragraph();
        heartInfo.add(new Chunk("Max BPM: ", HEADER_FONT));
        heartInfo.add(new Chunk(String.valueOf(bpmCollection.getMaxBPM()) + "\n", NORMAL_FONT));
        heartInfo.add(new Chunk("Min BPM: ", HEADER_FONT));
        heartInfo.add(new Chunk(String.valueOf(bpmCollection.getMinBPM()) + "\n", NORMAL_FONT));
        heartInfo.add(new Chunk("Avg BPM: ", HEADER_FONT));
        heartInfo.add(new Chunk(String.valueOf(bpmCollection.getAvgBPM()), NORMAL_FONT));

        heartInfoCell.addElement(heartInfo);

        infoTable.addCell(userInfoCell);
        infoTable.addCell(heartInfoCell);

        document.add(infoTable);
    }

    private void addDiseasesSection(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Diseases registered", SECTION_FONT);
        title.setSpacingBefore(10);
        title.setSpacingAfter(15);
        document.add(title);

        if (disease == null) {
            Paragraph empty = new Paragraph("<Empty>", NORMAL_FONT);
            empty.setSpacingBefore(10);
            document.add(empty);
        } else {
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 4});
            table.setSpacingAfter(20);

            // Header
            PdfPCell headerNr = new PdfPCell(new Phrase("#Nr", HEADER_FONT));
            headerNr.setBackgroundColor(HEADER_COLOR);
            headerNr.setPadding(8);
            headerNr.setBorderColor(BaseColor.WHITE);
            table.addCell(headerNr);

            PdfPCell headerName = new PdfPCell(new Phrase("Name", HEADER_FONT));
            headerName.setBackgroundColor(HEADER_COLOR);
            headerName.setPadding(8);
            headerName.setBorderColor(BaseColor.WHITE);
            table.addCell(headerName);

            // Data rows
            for (int i = 0; i < disease.getDiseases().size(); i++) {
                BaseColor rowColor = i % 2 == 0 ? BaseColor.WHITE : ALTERNATE_ROW_COLOR;

                PdfPCell cellNr = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL_FONT));
                cellNr.setPadding(8);
                cellNr.setBackgroundColor(rowColor);
                cellNr.setBorderColor(BaseColor.WHITE);
                table.addCell(cellNr);

                PdfPCell cellName = new PdfPCell(new Phrase(disease.getDiseases().get(i), NORMAL_FONT));
                cellName.setPadding(8);
                cellName.setBackgroundColor(rowColor);
                cellName.setBorderColor(BaseColor.WHITE);
                table.addCell(cellName);
            }

            document.add(table);
        }
    }

    private void addEmergencyLogsSection(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Emergency Logs", SECTION_FONT);
        title.setSpacingBefore(10);
        title.setSpacingAfter(15);
        document.add(title);

        if (emergencyReadings == null) {
            Paragraph empty = new Paragraph("<Empty>", NORMAL_FONT);
            empty.setSpacingBefore(10);
            document.add(empty);
        } else {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 3, 2, 3});

            // Header
            PdfPCell headerNr = new PdfPCell(new Phrase("#Nr", HEADER_FONT));
            headerNr.setBackgroundColor(HEADER_COLOR);
            headerNr.setPadding(8);
            headerNr.setBorderColor(BaseColor.WHITE);
            table.addCell(headerNr);

            PdfPCell headerIssued = new PdfPCell(new Phrase("Emergency Issued", HEADER_FONT));
            headerIssued.setBackgroundColor(HEADER_COLOR);
            headerIssued.setPadding(8);
            headerIssued.setBorderColor(BaseColor.WHITE);
            table.addCell(headerIssued);

            PdfPCell headerBPM = new PdfPCell(new Phrase("BPM", HEADER_FONT));
            headerBPM.setBackgroundColor(HEADER_COLOR);
            headerBPM.setPadding(8);
            headerBPM.setBorderColor(BaseColor.WHITE);
            table.addCell(headerBPM);

            PdfPCell headerDateTime = new PdfPCell(new Phrase("Date/Time", HEADER_FONT));
            headerDateTime.setBackgroundColor(HEADER_COLOR);
            headerDateTime.setPadding(8);
            headerDateTime.setBorderColor(BaseColor.WHITE);
            table.addCell(headerDateTime);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Data rows
            for (int i = 0; i < emergencyReadings.size(); i++) {
                EmergencyReading log = emergencyReadings.get(i);
                BaseColor rowColor = i % 2 == 0 ? BaseColor.WHITE : ALTERNATE_ROW_COLOR;

                PdfPCell cellNr = new PdfPCell(new Phrase(String.valueOf(i + 1), NORMAL_FONT));
                cellNr.setPadding(8);
                cellNr.setBackgroundColor(rowColor);
                cellNr.setBorderColor(BaseColor.WHITE);
                table.addCell(cellNr);

                PdfPCell cellIssued = new PdfPCell(new Phrase(log.getHas_contacted() > 0 ? "Yes" : "No", NORMAL_FONT));
                cellIssued.setPadding(8);
                cellIssued.setBackgroundColor(rowColor);
                cellIssued.setBorderColor(BaseColor.WHITE);
                table.addCell(cellIssued);

                PdfPCell cellBPM = new PdfPCell(new Phrase(String.valueOf(log.getBpmValue()), NORMAL_FONT));
                cellBPM.setPadding(8);
                cellBPM.setBackgroundColor(rowColor);
                cellBPM.setBorderColor(BaseColor.WHITE);
                table.addCell(cellBPM);
                String dateTimeStr = log.getDateTime()
                        .toLocalDateTime()   // Timestamp → LocalDateTime
                        .format(fmt);
                PdfPCell cellDateTime =  new PdfPCell(new Phrase(dateTimeStr, NORMAL_FONT));
                cellDateTime.setPadding(8);
                cellDateTime.setBackgroundColor(rowColor);
                cellDateTime.setBorderColor(BaseColor.WHITE);
                table.addCell(cellDateTime);
            }

            document.add(table);
        }
    }
}