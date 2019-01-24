package com.no1.taiwan.newsbasket.domain.usecases.keyword

import com.no1.taiwan.newsbasket.domain.BaseUsecase.RequestValues
import com.no1.taiwan.newsbasket.domain.DeferredUsecase
import com.no1.taiwan.newsbasket.domain.parameters.fields.KeywordsFields
import com.no1.taiwan.newsbasket.domain.repositories.DataRepository
import com.no1.taiwan.newsbasket.domain.usecases.keyword.UpdateRemoteKeywordsRespCase.Request
import com.no1.taiwan.newsbasket.ext.const.takeUnlessDefault

class UpdateRemoteKeywordsRespCase(
    private val repository: DataRepository,
    override var requestValues: Request? = null
) : DeferredUsecase<Boolean, Request>() {
    override suspend fun acquireCase() = attachParameter {
        val firebaseToken = repository.fetchFirebaseToken()
        val token = repository.fetchToken()
        val keywords = repository.fetchKeywords()

        val parameter = KeywordsFields(
            it.parameters.token.takeUnlessDefault() ?: token,
            it.parameters.firebaseToken.takeUnlessDefault() ?: firebaseToken,
            it.parameters.keywords.takeUnlessDefault() ?: keywords.joinToString(","),
            it.parameters.removeKeyword)

        repository.updateKeywords(parameter)
    }

    class Request(val parameters: KeywordsFields = KeywordsFields()) : RequestValues
}
