package com.planb.jasper.configs;

import com.planb.jasper.dto.JasperDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfGenerator {

    public byte[]  generateJasperReportPDF(String jasperPath, ByteArrayOutputStream outputStream,  List<JasperDTO> jasper) {
        JRPdfExporter exporter = new JRPdfExporter();
        try {
            File file = ResourceUtils.getFile(jasperPath);
            InputStream jrxmlInput = new FileInputStream(file);
            //this.getClass().getClassLoader().getResource("data.jrxml").openStream();
            JasperDesign design = JRXmlLoader.load(jrxmlInput);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            //System.out.println("Report compiled");

            //JasperReport jasperReport = JasperCompileManager.compileReport(reportLocation);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasper);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("createdBy", "Java Techie");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            exporter.exportReport();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in generate Report..."+e);
        } finally {
        }
        return outputStream.toByteArray();
    }
}
