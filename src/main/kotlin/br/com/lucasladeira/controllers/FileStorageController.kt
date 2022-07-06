package br.com.lucasladeira.controllers

import br.com.lucasladeira.dto.UploadFileResponseDTO
import br.com.lucasladeira.services.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.logging.Logger

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
}