package com.example.cookmate.utils

private const val INTERNET_CONNECTION_ERROR = "No internet connection"
private const val OTHER_ERROR = "Something went wrong"

private const val ACTION_OPEN_SETTINGS = "Open settings"
private const val ACTION_OK = "OK"

fun handleError(errorType: ErrorType): Pair<String, String> {
    val errorMessage: String
    val actionLabel: String

    when (errorType) {
        ErrorType.NO_INTERNET_CONNECTION -> {
            errorMessage = INTERNET_CONNECTION_ERROR
            actionLabel = ACTION_OPEN_SETTINGS
        }
        ErrorType.OTHER -> {
            errorMessage = OTHER_ERROR
            actionLabel = ACTION_OK
        }
    }

    return Pair(errorMessage, actionLabel)
}
