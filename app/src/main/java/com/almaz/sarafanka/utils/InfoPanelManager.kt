package com.almaz.sarafanka.utils

object InfoPanelManager {
    val errorMainState = TemporaryLiveData("")
    val successMainState = TemporaryLiveData("")
/*
    fun getErrorInfo(error: Throwable, action: String = ""): String =
        if (error is )
            getHttpErrorInfo(error, action)
        else error.message.toString()

    private fun getHttpErrorInfo(error: HttpException, action: String): String =
        when (error.code()) {
            422 -> "Неправильные данные"
            404 -> {
                if (action == "code")
                    "Такой код не существует"
                else
                    unknown(error)
            }
            else -> unknown(error)
        }

    private fun unknown(error: HttpException): String {
        val body = error.response()?.errorBody()?.string().toString()
        return if (body.isNotEmpty())
            body
        else "Неизвестная ошибка - " + error.code()
    }

    private var currError : Job? = null

    fun showErrorOnMain(error: String, coroutineScope: CoroutineScope) {
        if (currError?.isActive == true)
            currError?.cancel()
        currError = coroutineScope.launch {
            if (errorMainState.value?.isNotEmpty() == true) {
                errorMainState.postValue("")
                delay(150)
            }
            errorMainState.postValue(error)
            delay(3000)
            if (errorMainState.value == error)
                errorMainState.postValue("")
        }
    }*/
}
