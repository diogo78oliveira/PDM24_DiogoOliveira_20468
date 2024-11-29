package com.ag_apps.newsapp.core.data

import com.ag_apps.newsapp.core.data.local.ArticlesDao
import com.ag_apps.newsapp.core.data.remote.NewsListDto
import com.ag_apps.newsapp.core.domain.NewsList
import com.ag_apps.newsapp.core.domain.NewsRepository
import com.ag_apps.newsapp.core.domain.NewsResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NewsRepositoryImpl(
    private val httpClient: HttpClient,
    private val dao: ArticlesDao
) : NewsRepository {

    private val tag = "NewsRepository: "
    private val baseUrl = "https://newsdata.io/api/1/latest"
    private val apiKey = "pub_607817cd547a06c7ae7dfe7beeb6307d7a61d"

    private suspend fun getLocalNews(nextPage: String?): NewsList {
        val localNews = dao.getArticleList()
        println(tag + "getLocalNews: " + localNews.size + " nextPage: " + nextPage)

        val newsList = NewsList(
            nextPage = nextPage,
            articles = localNews.map { it.toArticle() }
        )

        return newsList
    }

    private suspend fun getRemoteNews(nextPage: String?): NewsList {
        val newsListDto: NewsListDto = httpClient.get(baseUrl) {
            parameter("apikey", apiKey)
            parameter("language", "en")
            if (nextPage != null) parameter("page", nextPage)
        }.body()

        println(tag + "getRemoteNews: " + newsListDto.results?.size + " nextPage: " + nextPage)

        return newsListDto.toNewsList()
    }

    override suspend fun getNews(): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(null)
            } catch (e: Exception) {
                e.printStackTrace()
                println(tag + "getNews remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.clearDatabase()
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })
                emit(NewsResult.Success(getLocalNews(remoteNewsList.nextPage)))
                return@flow
            }

            val localNewsList = getLocalNews(null)
            if (localNewsList.articles.isNotEmpty()) {
                emit(NewsResult.Success(localNewsList))
                return@flow
            }

            emit(NewsResult.Error("No Data"))

        }
    }

    override suspend fun paginate(nextPage: String): Flow<NewsResult<NewsList>> {
        return flow {
            val remoteNewsList = try {
                getRemoteNews(nextPage)
            } catch (e: Exception) {
                e.printStackTrace()
                println(tag + "paginate remote exception: " + e.message)
                null
            }

            remoteNewsList?.let {
                dao.upsertArticleList(remoteNewsList.articles.map { it.toArticleEntity() })

                emit(NewsResult.Success(remoteNewsList))
                return@flow
            }
        }
    }
















}