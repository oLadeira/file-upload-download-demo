package br.com.lucasladeira.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class MyFileNotFoundException: RuntimeException {

    constructor(ex: String): super(ex)
    constructor(ex: String, cause: Throwable): super(ex, cause)

}