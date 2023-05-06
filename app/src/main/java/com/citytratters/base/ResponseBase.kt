package com.citytratters.base

data class ResponseBase<T>(
    val ErrorMessage: String?,
    val Data: T?,
    val Status: Int
)