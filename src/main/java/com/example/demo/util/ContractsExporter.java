package com.example.demo.util;

import com.example.demo.domain.Contract;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Objects;

@Service
public class ContractsExporter {
    public File createFile(List<Contract> contracts, String exportType) {
        if (Objects.equals(exportType, "PDF")) {
            File file = new File("/tmp/tmp.pdf");
            try {
                file.createNewFile();
                Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/tmp/tmp.pdf"));
                document.open();
                Anchor anchorTarget = new Anchor("First page");
                anchorTarget.setName("BackToTop");
                PdfPTable t = new PdfPTable(7);
                t.setSpacingBefore(25);
                t.setSpacingAfter(25);
                PdfPCell c1 = new PdfPCell(new Phrase("id"));
                t.addCell(c1);
                PdfPCell c2 = new PdfPCell(new Phrase("contractor name"));
                t.addCell(c2);
                PdfPCell c3 = new PdfPCell(new Phrase("contract date"));
                t.addCell(c3);
                PdfPCell c4 = new PdfPCell(new Phrase("start date"));
                t.addCell(c4);
                PdfPCell c5 = new PdfPCell(new Phrase("end date"));
                t.addCell(c5);
                PdfPCell c6 = new PdfPCell(new Phrase("expected total cost"));
                t.addCell(c6);
                PdfPCell c7 = new PdfPCell(new Phrase("calculated total cost"));
                t.addCell(c7);

                for (Contract contract : contracts) {
                    t.addCell(contract.getId().toString());
                    t.addCell(contract.getContractorName());
                    t.addCell(contract.getContractDate().toString());
                    t.addCell(contract.getBeginDate().toString());
                    t.addCell(contract.getEndDate().toString());
                    t.addCell(contract.getExpectedTotalCost().toString());
                    t.addCell(contract.getCalculatedTotalCost().toString());
                }


                document.add(t);

                document.close();
                return file;
            } catch (IOException | DocumentException e) {
            }
        } else {
            if (Objects.equals(exportType, "CSV")) {
                File file = new File("/tmp/tmp.csv");
                try {
                    file.createNewFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/tmp.csv"));
                    writer.write("id;contractor name;contract date;start date;end date;expected total cost;calculated total cost;\n");
                    for (Contract contract : contracts) {
                        writer.write(contract.getId() + ";" + contract.getContractorName() + ";" + contract.getContractDate() + ";" + contract.getBeginDate() + ";" + contract.getEndDate() + ";" + contract.getExpectedTotalCost() + ";" + contract.getCalculatedTotalCost() + ";" + "\n");
                    }
                    writer.close();
                    return file;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
