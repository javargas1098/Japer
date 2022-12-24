package com.planb.jasper.controllers;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.planb.jasper.configs.PdfGenerator;
import com.planb.jasper.dto.JasperDTO;
import com.planb.jasper.dto.ResponseDTO;
import lombok.var;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VistaController {


    @Value("azure-blob://planbflejesstorage/Refrigerados4cm.jrxml")
    private Resource blobFile;

    @Value("azure-blob://planbflejesstorage/Secos6cm.jrxml")
    private Resource blobFile2;

    @GetMapping
    public ResponseEntity<ResponseDTO> getProductList(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat, @RequestParam String jasperPath) throws IOException, JRException {
        String path = "E:\\USER\\Projects\\PLANB\\jasper\\e-Commerce-Angular11-Springboot-PostgreSQL\\jasper";
//        System.out.println(this.blobFile.getFile());
//        BlobContainerClient blobContainerClient =
//                storageClient.getBlobContainerClient(containerName);
//        BlobClient blobClient = blobContainerClient.getBlobClient(blobItem.getName());
//        System.out.println(ResourceUtils.getFile(StreamUtils.copyToString(
//                this.blobFile.getInputStream(),
//                Charset.defaultCharset())));
        File file = ResourceUtils.getFile(jasperPath);
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jasper);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint,  uuidAsString + ".pdf");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, uuidAsString + ".pdf");
        }
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setPath(path + uuidAsString + ".pdf");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);


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
    @GetMapping("/gerarPdf")
    public ResponseEntity<?> geraPDF(@RequestBody List<JasperDTO> jasper, @RequestParam String reportFormat, @RequestParam String jasperPath){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfGenerator pdf = new PdfGenerator();
        byte[] bytes= pdf.generateJasperReportPDF( blobFile, outputStream, jasper);
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        String name= uuidAsString + ".pdf";
        return  ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_PDF).header("Content-Disposition", "filename=\"" + name + "\"").body(bytes);

    }

    @GetMapping("/readBlobFile")
    public String readBlobFile() throws IOException {
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
    }

}
