package br.com.lucasladeira.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FileStorageConfig(

    //Diretorio da pasta onde vou armazenar os arquivos, definido no application.yml
    @Value("\${file.upload-dir}")
    var uploadDir: String
)