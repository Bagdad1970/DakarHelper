package io.github.bagdad.dakarhelperservice.controller;

import io.github.bagdad.dakarhelperservice.service.interfaces.ExcelParserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/parser")
@RestController
public class ExcelParserController {

    private final ExcelParserService excelParserService;

    ExcelParserController(ExcelParserService excelParserService) {
        this.excelParserService = excelParserService;
    }

    @PostMapping("/parse-excel")
    public void parseExcelFiles() {
        excelParserService.runExcelParser();
    }

}
