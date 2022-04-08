package com.example.pageit.utils

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
class GeneralException(message: String) : Exception(message)
