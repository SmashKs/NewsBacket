package com.no1.taiwan.newsbasket.data.datas.mappers

import com.no1.taiwan.newsbasket.data.datas.DataNewsMapper
import com.no1.taiwan.newsbasket.data.datas.NewsData
import com.no1.taiwan.newsbasket.domain.models.NewsModel

/**
 * A transforming mapping between [NewsData] and [NewsModel]. The different layers have
 * their own data objects, the objects should transform and fit each layers.
 */
class NewsMapper : DataNewsMapper {
    override fun toModelFrom(data: NewsData) = data.run {
        NewsModel(id,
                  author,
                  content,
                  country,
                  created_at,
                  description,
                  published_at,
                  title,
                  updated_at,
                  url,
                  urlToImage)
    }

    override fun toDataFrom(model: NewsModel) = model.run {
        NewsData(id,
                 author,
                 content,
                 country,
                 created_at,
                 description,
                 published_at,
                 title,
                 updated_at,
                 url,
                 urlToImage)
    }
}
