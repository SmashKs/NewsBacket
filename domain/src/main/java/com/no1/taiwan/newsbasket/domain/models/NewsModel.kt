package com.no1.taiwan.newsbasket.domain.models

import com.no1.taiwan.newsbasket.ext.const.DEFAULT_LONG
import com.no1.taiwan.newsbasket.ext.const.DEFAULT_STR
import com.no1.taiwan.newsbasket.ext.const.UniqueId

/**
 * Model object in domain layer to be a bridge object.
 */
data class NewsModel(
    var id: UniqueId = DEFAULT_LONG,
    val author: String? = DEFAULT_STR,
    val content: String? = DEFAULT_STR,
    val country: String = DEFAULT_STR,
    val createdAt: String = DEFAULT_STR,
    val description: String? = DEFAULT_STR,
    val publishedAt: String = DEFAULT_STR,
    val title: String = DEFAULT_STR,
    val updatedAt: String = DEFAULT_STR,
    val url: String = DEFAULT_STR,
    val urlToImage: String? = DEFAULT_STR
) : Model
