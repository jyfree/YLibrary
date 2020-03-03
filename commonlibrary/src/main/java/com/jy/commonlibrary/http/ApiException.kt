package com.jy.commonlibrary.http


class ApiException(code: Int, message: String) : Exception(message, Throwable(code.toString()))