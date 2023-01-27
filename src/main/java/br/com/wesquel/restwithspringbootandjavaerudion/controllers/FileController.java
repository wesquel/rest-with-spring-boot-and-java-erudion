package br.com.wesquel.restwithspringbootandjavaerudion.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v1.UploadFileResponseVO;
import br.com.wesquel.restwithspringbootandjavaerudion.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "File Endpoint!")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {
    
    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponseVO uploadFile(@RequestParam("file") MultipartFile file) {

        logger.info("Storing file to disk");
        var filename = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/api/file/v1/downloadFile/")
            .path(filename)
            .toUriString();
        ;

        return new UploadFileResponseVO(
            filename, 
            fileDownloadUri, 
            file.getContentType(), 
            file.getSize()
        );

    }

    @PostMapping("/uploadMultipleFile")
    public List<UploadFileResponseVO> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {

        logger.info("Storing files to disk");

        return Arrays.asList(files)
            .stream()
            .map(file -> this.uploadFile(file))
            .collect(Collectors.toList())
        ;
    }

}