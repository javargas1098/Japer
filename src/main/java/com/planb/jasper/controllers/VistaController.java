package com.planb.jasper.controllers;

import com.planb.jasper.dto.JasperDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class VistaController {


    @GetMapping
    public String getProductList(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat) throws FileNotFoundException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
        File file = ResourceUtils.getFile("classpath:jasper7.jrxml");
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

    @GetMapping("/merged")
    public String getProductListMerged(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat) throws FileNotFoundException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
        File file = ResourceUtils.getFile("classpath:jasper2.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        jasper.stream().forEach(data->{
            List<JasperDTO> jasperN= new LinkedList<>();
            jasperN.add(data);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasperN);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Java Techie");
            JasperPrint jasperPrint = null;
            try {
                jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

                jasperPrintList.add(jasperPrint);
            } catch (JRException e) {
                e.printStackTrace();
            }
        });
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path +  "/output.pdf"));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setCreatingBatchModeBookmarks(true); //add this so your bookmarks work, you may set other parameters
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }

        return "report generated in path : " + path;

    }
}
