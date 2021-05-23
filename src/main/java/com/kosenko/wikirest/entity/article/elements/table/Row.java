package com.kosenko.wikirest.entity.article.elements.table;

import com.fasterxml.jackson.annotation.JsonView;
import com.kosenko.wikirest.entity.Views;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "row_elements")
public class Row {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullArticle.class)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "row_id")
    @JsonView(Views.FullArticle.class)
    private final List<Cell> cells;

    @JsonView(Views.FullArticle.class)
    private boolean isTableHead = false;

    public Row() {
        cells = new ArrayList<>();
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public boolean isTableHead() {
        return isTableHead;
    }

    public void setTableHead(boolean tableHead) {
        isTableHead = tableHead;
    }
}
