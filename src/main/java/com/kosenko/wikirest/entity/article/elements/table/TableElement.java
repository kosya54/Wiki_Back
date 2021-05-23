package com.kosenko.wikirest.entity.article.elements.table;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_elements")
public class TableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @JsonView(Views.FullArticle.class)
    private int colCount = 0;

    @JsonView(Views.FullArticle.class)
    private int rowCount = 0;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_element_id")
    @JsonView(Views.FullArticle.class)
    private final List<Row> rows;

    public TableElement() {
        rows = new ArrayList<>();
    }

    public List<Row> getRows() {
        return rows;
    }

    public void addRow(Row row) {
        rows.add(row);
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int col) {
        this.colCount = col;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int row) {
        this.rowCount = row;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Col: ").append(colCount);
        stringBuilder.append(System.lineSeparator());

        stringBuilder.append("Row: ").append(rowCount);
        stringBuilder.append(System.lineSeparator());

        rows.forEach(row -> {
            stringBuilder.append("|");
            row.getCells().forEach(cell -> {
                stringBuilder.append(cell).append("|");
            });
            stringBuilder.append(System.lineSeparator());
        });

        return stringBuilder.toString();
    }
}
