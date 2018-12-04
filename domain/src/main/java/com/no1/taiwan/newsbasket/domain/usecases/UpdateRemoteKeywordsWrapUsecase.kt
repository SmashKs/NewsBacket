package com.no1.taiwan.newsbasket.domain.usecases

import com.no1.taiwan.newsbasket.domain.BaseUsecase.RequestValues
import com.no1.taiwan.newsbasket.domain.DeferredWrapUsecase
import com.no1.taiwan.newsbasket.domain.parameters.fields.KeywordsFields
import com.no1.taiwan.newsbasket.domain.repositories.DataRepository
import com.no1.taiwan.newsbasket.domain.usecases.UpdateRemoteKeywordsWrapUsecase.Request
import com.no1.taiwan.newsbasket.ext.const.takeIfDefault
import kotlin.coroutines.CoroutineContext

class UpdateRemoteKeywordsWrapUsecase(
    private val repository: DataRepository,
    override var requestValues: Request? = null
) : DeferredWrapUsecase<Boolean, Request>() {
    override fun acquireCase(parentJob: CoroutineContext) = attachParameter {
        val firebaseToken = repository.fetchFirebaseToken(parentJob)
        val token = repository.fetchToken(parentJob)
        val keywords = repository.fetchKeywords(parentJob)
        val parameter = KeywordsFields(
            it.parameters.firebaseToken.takeIfDefault() ?: firebaseToken.await(),
            it.parameters.token.takeIfDefault() ?: token.await(),
            it.parameters.keywords.takeIfDefault() ?: keywords.await().joinToString(","))

        repository.updateKeywords(parameter, parentJob)
    }

    class Request(val parameters: KeywordsFields = KeywordsFields()) : RequestValues
}