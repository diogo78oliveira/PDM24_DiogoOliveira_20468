package com.example.appnoticias20core.data

import com.example.appnoticias20.core.data.local.ArticleEntity
import com.example.appnoticias20.core.data.remote.ArticleDto
import com.example.appnoticias20.core.data.remote.NewsListDto
import com.example.appnoticias20.core.domain.Article
import com.example.appnoticias20.core.domain.NewsList



fun NewsListDto.toNewsList(): NewsList {
    return NewsList(
        nextPage = nextPage,
        articles = results?.map { it.toArticle() } ?: emptyList(),
    )
}

fun ArticleDto.toArticle(): Article {
    return Article(
        articleId = article_id ?: "",
        title = title ?: "",
        description = description ?: "",
        pubDate = pubDate ?: "",
        sourceName = source_name ?: "",
        imageUrl = image_url ?: "",
        content = "$article_id: As of 10:30 a.m. ET Friday — more than a week after landfall — nearly 735,000 electricity accounts were still without power across five states, according to Poweroutage.us: South Carolina (273,913), North Carolina (230,448), Georgia (203,111), Florida (13,794), and Virginia (13,191).\n"

    )
}

fun ArticleEntity.toArticle(): Article {
    return Article(
        articleId = articleId,
        title = title,
        description = description,
        pubDate = pubDate,
        sourceName = sourceName,
        imageUrl = imageUrl,
        content = content
    )
}

fun Article.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        articleId = articleId,
        title = title,
        description = description,
        pubDate = pubDate,
        sourceName = sourceName,
        imageUrl = imageUrl,
        content = content
    )
}