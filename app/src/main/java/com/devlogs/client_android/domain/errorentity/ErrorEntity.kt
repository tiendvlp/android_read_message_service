package com.devlogs.client_android.domain.errorentity

sealed class ErrorEntity : Exception() {
    class NetworkErrorEntity : ErrorEntity ()
    class InvalidJwtErrorEntity : ErrorEntity ()
    class BadRequestErrorEntity : ErrorEntity ()

}