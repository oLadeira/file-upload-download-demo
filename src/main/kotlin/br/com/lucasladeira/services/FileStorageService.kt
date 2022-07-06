package br.com.lucasladeira.services

import br.com.lucasladeira.config.FileStorageConfig
import br.com.lucasladeira.exceptions.FileStorageException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService(
    @Autowired
    fileStorageConfig: FileStorageConfig
) {

    private lateinit var fileStorageLocation: Path

    //Ao instanciar a classe...
    init {

        //
        fileStorageLocation = Paths.get(fileStorageConfig.uploadDir).toAbsolutePath().normalize()
        try {
            //Tenta criar o diretorio caso nao exista
            Files.createDirectories(fileStorageLocation)
        }catch (ex: Exception){
            throw FileStorageException("Não foi possível criar o diretório onde o arquivo será armazenado!", ex)
        }
    }

    fun storageFile(file: MultipartFile):String{

        //Obtem o nome do arquivo
        val fileName = StringUtils.cleanPath(file.originalFilename!!)

        //Tenta Salvar
        return try {

            //Verifica se o nome do arquivo não possui ".."
            if (fileName.contains("..")){
                throw FileStorageException("O nome do seu arquivo possui uma sequência de caracteres inválida!")
            }

            //Definindo o local de armazenamento, pegando o local da pasta + o nome do arquivo
            var targetLocation = fileStorageLocation.resolve(fileName)

            //executa a copia, passando o bytearray do arquivo, o target, e o padrao de copia (neste caso coloquei para substituir caso ja exista)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        }catch (ex: Exception){
            throw FileStorageException("Não foi possível armazenar o arquivo ${fileName}!", ex)
        }
    }
}