package br.com.lucasladeira.dto

//Corpo da resposta ao enviar o arquivo
class UploadFileResponseDTO (
    var fileName : String = "",
    var fileDownloadUri : String = "",
    var fileType : String = "",
    var fileSize: Long = 0
)