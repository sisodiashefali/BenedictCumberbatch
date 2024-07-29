package com.android.benedictcumberbatchapp

import com.android.benedictcumberbatchapp.data.network.ApiService
import com.android.benedictcumberbatchapp.utils.Constant
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.mockito.Mockito;


class MovieListApiTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var movieApi: ApiService

    @Before
    fun setUp() {
        //initialise mock web server
        mockWebServer = MockWebServer()
        // base URL of local web server, define root base url, API will use local webserver
        //API setup pointing to local webserver
        movieApi = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(ApiService::class.java)
    }

    @Test
    fun testGetList() = runBlocking {

        //response set
        //check if response is correct
        val mockResponse = MockResponse()
        mockResponse.setBody("{}") // return empty object
        mockWebServer.enqueue(mockResponse)

        //Request Response{protocol=http/1.1, code=200, message=OK, url=http://127.0.0.1:58721/movie?page=1&api_key=209af8a7c9a11e0419da762d88faa7f1&with_people=71580}
        val response = movieApi.getAllMovies(1, Constant.API_KEY, Constant.PEOPLE_ID)
        mockWebServer.takeRequest()
        Assert.assertEquals(true, response.body()!!.results.isNullOrEmpty())
    }

    @Test
    fun testGetList_returnMovieList() = runBlocking {

        //set response
        //check if response is correct
        val mockResponse = MockResponse()
        val content =
            Helper.readFileResource("/response.json")
        mockResponse.setResponseCode(200)
        mockResponse.setBody(content)
        mockWebServer.enqueue(mockResponse)

        //Request Response{protocol=http/1.1, code=200, message=OK, url=http://127.0.0.1:58721/movie?page=1&api_key=209af8a7c9a11e0419da762d88faa7f1&with_people=71580}
        val response = movieApi.getAllMovies(1, Constant.API_KEY, Constant.PEOPLE_ID)
        mockWebServer.takeRequest()
        Assert.assertEquals(false, response.body()!!.results.isEmpty())
        Assert.assertEquals(2, response.body()!!.results.size)

    }

    @Test
    fun testGetList_returnError() = runBlocking {

        //set response
        //check if response is correct
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(404)
        mockResponse.setBody("Something went wrong")
        mockWebServer.enqueue(mockResponse)

        //Request Response{protocol=http/1.1, code=200, message=OK, url=http://127.0.0.1:58721/movie?page=1&api_key=209af8a7c9a11e0419da762d88faa7f1&with_people=71580}
        val response = movieApi.getAllMovies(1, Constant.API_KEY, Constant.PEOPLE_ID)
        mockWebServer.takeRequest()
        Assert.assertEquals(false, response.isSuccessful)
        Assert.assertEquals(404, response.code())

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()

    }
}