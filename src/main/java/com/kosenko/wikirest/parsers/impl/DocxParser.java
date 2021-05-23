package com.kosenko.wikirest.parsers.impl;

import com.kosenko.wikirest.entity.article.elements.Heading;
import com.kosenko.wikirest.entity.article.elements.Numbering;
import com.kosenko.wikirest.entity.article.elements.Paragraph;
import com.kosenko.wikirest.entity.article.elements.table.Cell;
import com.kosenko.wikirest.entity.article.elements.table.Row;
import com.kosenko.wikirest.entity.article.elements.table.TableElement;
import com.kosenko.wikirest.parsers.WordParser;
import com.kosenko.wikirest.service.StorageService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

@Component
public class DocxParser implements WordParser {
    private final StorageService storageService;

    @Autowired
    public DocxParser(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<Heading> parse(File file) throws IOException, InvalidFormatException {
        List<Heading> headings = new ArrayList<>();
        Map<String, XWPFPictureData> images = new HashMap<>();

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            XWPFDocument docxDocument = new XWPFDocument(OPCPackage.open(fileInputStream));
            XWPFStyles docxDocumentStyles = docxDocument.getStyles();

            List<IBodyElement> docElements = docxDocument.getBodyElements();
            docElements.forEach(element -> {
                //Выборка параграфов
                if (element instanceof XWPFParagraph) {
                    XWPFParagraph docxDocumentParagraph = (XWPFParagraph) element;

                    //Выборка заголовка
                    XWPFStyle style = docxDocumentStyles.getStyle(docxDocumentParagraph.getStyleID());
                    if (style != null && style.getName().startsWith("heading")) {
                        Heading heading = new Heading();
                        heading.setContent(docxDocumentParagraph.getParagraphText());

                        headings.add(heading);

                        return;
                    }

                    //Выборка списка
                    if (isNumbering(docxDocumentParagraph)) {
                        int level = docxDocumentParagraph.getNumIlvl().intValue();
                        String text = docxDocumentParagraph.getParagraphText();

                        Numbering numbering = new Numbering(level, text);

                        if (!docxDocumentParagraph.getNumFmt().equals("decimal")) {
                            numbering.setDecimal(false);
                        }

                        getLastParagraph(headings).addNumbering(numbering);

                        return;
                    }

                    //Выборка картинки
                    if (hasImage(docxDocumentParagraph)) {
                        docxDocumentParagraph.getRuns().forEach(run -> run.getEmbeddedPictures().forEach(picture -> {
                            String fileName = UUID.randomUUID().toString() + "." + picture.getPictureData().getFileName();

                            images.put(fileName, picture.getPictureData());

                            getLastParagraph(headings).addImageName(fileName);
                        }));

                        return;
                    }

                    //Выборка текста
                    if (!docxDocumentParagraph.isEmpty() && !docxDocumentParagraph.getParagraphText().equals("")) {
                        Paragraph paragraph = new Paragraph();
                        paragraph.setContent(docxDocumentParagraph.getParagraphText());

                        if (headings.size() > 0) {
                            headings.get(headings.size() - 1).addParagraph(paragraph);
                        }
                    }
                }

                //Выборка таблиц
                if (element instanceof XWPFTable) {
                    XWPFTable docTable = (XWPFTable) element;

                    TableElement table = new TableElement();
                    docTable.getRows().forEach(docRow -> {
                        int docTableCol = docRow.getTableCells().size();
                        if (docTableCol > table.getColCount()) {
                            table.setColCount(docTableCol);
                        }

                        Row row = new Row();
                        docRow.getTableCells().forEach(docCell -> row.addCell(new Cell(docCell.getText())));

                        table.addRow(row);
                    });

                    table.getRows().get(0).setTableHead(true);
                    table.setRowCount(docTable.getNumberOfRows());

                    getLastParagraph(headings).addTableElement(table);
                }
            });
        }

        uploadImages(images);

        return headings;
    }

    private static Paragraph getLastParagraph(List<Heading> headings) {
        if (headings.isEmpty()) {
            throw new NullPointerException("Заголовки пустые");
        }

        List<Paragraph> paragraphs = headings.get(headings.size() - 1).getParagraphs();
        if (paragraphs.isEmpty()) {
            return new Paragraph();
        }

        return paragraphs.get(paragraphs.size() - 1);
    }

    private static boolean isNumbering(XWPFParagraph paragraph) {
        return paragraph.getNumFmt() != null;
    }

    private static boolean hasImage(XWPFParagraph paragraph) {
        List<XWPFRun> runs = paragraph.getRuns();
        for (XWPFRun run : runs) {
            if (!run.getEmbeddedPictures().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private void uploadImages(Map<String, XWPFPictureData> images) {
        Map<String, BufferedImage> imagesForUpload = new HashMap<>();
        images.forEach((fileName, image) -> {
            try {
                imagesForUpload.put(fileName, ImageIO.read(new ByteArrayInputStream(image.getData())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        storageService.upload(imagesForUpload);
    }
}
