package com.seiberl.jumpreader.ui.jumpimport.models.exceptions

class VolumeAccessException : IllegalStateException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}