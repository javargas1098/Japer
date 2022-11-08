package com.planb.jasper.controllers;

import com.planb.jasper.dto.JasperDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VistaController {


    @GetMapping
    public String getProductList(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat) throws FileNotFoundException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
        File file = ResourceUtils.getFile("classpath:jasper1.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasper);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\jasper.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\jasper.pdf");
        }

        return "report generated in path : " + path;

    }
}
