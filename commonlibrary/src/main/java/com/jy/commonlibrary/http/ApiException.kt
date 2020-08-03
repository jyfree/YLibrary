package com.jy.commonlibrary.http


class ApiException(val code: Int, message: String) : Exception(message, Throwable(code.toString()))