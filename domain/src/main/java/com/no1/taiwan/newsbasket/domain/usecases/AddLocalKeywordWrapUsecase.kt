package com.no1.taiwan.newsbasket.domain.usecases

import com.no1.taiwan.newsbasket.domain.BaseUsecase.RequestValues
import com.no1.taiwan.newsbasket.domain.DeferredWrapUsecase
import com.no1.taiwan.newsbasket.domain.parameters.params.KeywordsParams
import com.no1.taiwan.newsbasket.domain.repositories.DataRepository
import com.no1.taiwan.newsbasket.domain.usecases.AddLocalKeywordWrapUsecase.Request
import kotlinx.coroutines.CoroutineScope

class AddLocalKeywordWrapUsecase(
    private val repository: DataRepository,
    override var requestValues: Request? = null
) : DeferredWrapUsecase<Boolean, Request>() {
    override fun CoroutineScope.acquireCase() = attachParameter {
        repository.addKeyword(it.parameters, this)
    }

    class Request(val parameters: KeywordsParams = KeywordsParams()) : RequestValues
}
