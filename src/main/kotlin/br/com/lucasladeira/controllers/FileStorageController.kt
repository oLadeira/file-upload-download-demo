package br.com.lucasladeira.controllers

import br.com.lucasladeira.dto.UploadFileResponseDTO
import br.com.lucasladeira.services.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/files")
class FileStorageController {

    private val logger = Logger.getLogger(FileStorageController::class.java.name)

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @PostMapping("/upload-file")
    fun uploadFile(@RequestParam("file")file: MultipartFile): UploadFileResponseDTO{
        val fileName = fileStorageService.storageFile(file)
        val fileDownloadUri = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/api/files/upload-file")
            .path(fileName)
            .toUriString()

        return UploadFileResponseDTO(fileName, fileDownloadUri, file.contentType!!, file.size)
    }

    @PostMapping("/upload-files")
    fun uploadMultipleFiles(@RequestParam("files")files: List<MultipartFile>): List<UploadFileResponseDTO>{
        var uploadFilesResponse = arrayListOf<UploadFileResponseDTO>()
        for (file in files){
            var uploadFileResponse = uploadFile(file)
            uploadFilesResponse.add(uploadFileResponse)
        }
        return uploadFilesResponse
    }

    @GetMapping("/download-file/{fileName:.+}")
    fun downloadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource>{
        val resource = fileStorageService.loadFileAsResource(fileName)
        var contentType = ""
        try {
            contentType = request.servletContext.getMimeType(resource.file.absolutePath)
        }catch (ex: Exception){
            logger.info("Tipo de arquivo n??o determinado!")
        }
        if (contentType.isBlank()){
            contentType = "application/octet-stream"
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, """attachment; filename="${resource.filename}"""")
            .body(resource)
    }
}