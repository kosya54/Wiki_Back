package com.kosenko.wikirest.parsers;

import com.kosenko.wikirest.entity.article.elements.Heading;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public interface WordParser {
    List<Heading> parse(File file) throws IOException, InvalidFormatException;
}
