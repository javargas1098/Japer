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
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VistaController {


    @GetMapping
    public String getProductList(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat, @RequestParam int type) throws FileNotFoundException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
        File file = null;
        switch (type) {
            case 1:
                file = ResourceUtils.getFile("classpath:jasper1.jrxml");
                break;
            case 2:
                file = ResourceUtils.getFile("classpath:troquel2.jrxml");
                break;
            case 3:
                file = ResourceUtils.getFile("classpath:troquel1.jrxml");
                break;
            case 4:
                file = ResourceUtils.getFile("classpath:nuevoProductoCorto.jrxml");
                break;
            case 5:
                file = ResourceUtils.getFile("classpath:nuevoProductoLargo.jrxml");
                break;
            case 6:
                file = ResourceUtils.getFile("classpath:bajoPrecioCorto.jrxml");
                break;
            case 7:
                file = ResourceUtils.getFile("classpath:bajoPrecioLargo.jrxml");
                break;
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasper);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\" + uuidAsString + ".pdf");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\" + uuidAsString + ".pdf");
        }

        return "report generated in path : " + path;

    }

    @GetMapping("/merged")
    public String getProductListMerged(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat) throws FileNotFoundException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
        File file = ResourceUtils.getFile("classpath:jasper2.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        List<JasperPrint> jasperPrintList = new ArrayList<>();
        jasper.stream().forEach(data -> {
            List<JasperDTO> jasperN = new LinkedList<>();
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
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path + "/output.pdf"));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setCreatingBatchModeBookmarks(true); //add this so your bookmarks work, you may set other parameters
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        }

        return "report generated in path : " + path;

    }
}
