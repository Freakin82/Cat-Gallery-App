package com.paranjal.catgallery.features.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.paranjal.catgallery.features.data.CatImage
import com.paranjal.catgallery.service.CatApiService
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CatViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CatViewModel
    private val catApiServiceMock = mockk<CatApiService>()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = CatViewModel(catApiServiceMock)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fetchCatImages success`() = runBlocking {
        coEvery { catApiServiceMock.getCatImages() } returns listOf(
            CatImage("1", "https://example.com/cat1.jpg", 500, 300),
            CatImage("2", "https://example.com/cat2.jpg", 600, 400)
        )
        viewModel.fetchCatImages()

        assert(viewModel.catImages.value?.isNotEmpty() == true)
        assert(viewModel.isLoading.value == false)
        assert(viewModel.isError.value == false)
    }

    @Test
    fun `fetchCatImages error`() {
        coEvery { catApiServiceMock.getCatImages() } throws Exception("Network error")

        viewModel.fetchCatImages()

        assert(viewModel.catImages.value == null)
        assert(viewModel.isLoading.value == false)
        assert(viewModel.isError.value == true)
    }
}