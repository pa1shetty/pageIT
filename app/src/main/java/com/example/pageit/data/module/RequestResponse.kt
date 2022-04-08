package com.example.pageit.data.module

data class RequestResponse(
    var status: StatusEnum = StatusEnum.NOT_REQUESTED,
    var errorCode: ErrorCode = ErrorCode.DEFAULT,
    var errorMessage: String = ""

)

enum class StatusEnum {
    NOT_REQUESTED,
    LOADING,
    SUCCESS,
    FAILED
}

enum class ErrorCode {
    DEFAULT,
    NO_INTERNET
}