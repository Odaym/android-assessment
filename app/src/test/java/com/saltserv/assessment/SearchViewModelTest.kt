package com.saltserv.assessment

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import com.nhaarman.mockito_kotlin.whenever
import com.saltserv.assessment.network.ApiService
import com.saltserv.assessment.responses.ArtistItem
import com.saltserv.assessment.responses.Artists
import com.saltserv.assessment.responses.ExternalUrls
import com.saltserv.assessment.responses.SearchResponse
import com.saltserv.assessment.testutils.TestBaseViewModelDependencies
import com.saltserv.assessment.ui.search.SearchViewModel
import com.saltserv.assessment.util.NetworkAvailabilityChecker
import com.saltserv.assessment.util.OpenArtistDetailScreen
import com.saltserv.assessment.util.ShowErrorDialog
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SearchViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val apiService: ApiService = mock()

    private val networkChecker: NetworkAvailabilityChecker = mock()

    private val tested by lazy {
        SearchViewModel(
            dependencies = TestBaseViewModelDependencies(),
            apiService = apiService,
            networkChecker = networkChecker
        )
    }

    @Test
    fun `does call search endpoint when query is not empty`() {
        givenIsConnected()

        givenSearchReturnsArtists()

        tested.onSearchQueryChanged(SEARCH_TERM)

        assertThat(tested.searchQuery).isEqualTo(SEARCH_TERM)
        assertThat(tested.emptyViewVisible.value).isEqualTo(false)

        verify(apiService).search(SEARCH_TERM)

        verifyDoFinallyBlock()
    }

    @Test
    fun `does load items when search succeeds`() {
        givenIsConnected()

        givenSearchReturnsArtists()

        tested.onSearchQueryChanged(SEARCH_TERM)

        assertThat(tested.noResultsVisible.value).isEqualTo(false)
        assertThat(tested.listItems.value).isEqualTo(searchResponse.artists.items)
    }

    @Test
    fun `does show no-results view when no results are found`(){
        givenIsConnected()

        givenSearchReturnsEmptyResults ()

        tested.onSearchQueryChanged(SEARCH_TERM)

        assertThat(tested.noResultsVisible.value).isEqualTo(true)
        assertThat(tested.listItems.value).isEqualTo(emptyList<ArtistItem>())
    }

    @Test
    fun `does not call search endpoint when query is empty`() {
        givenIsConnected()

        tested.onSearchQueryChanged("")

        assertThat(tested.emptyViewVisible.value).isEqualTo(true)
        assertThat(tested.listItems.value).isEqualTo(emptyList<ArtistItem>())

        verifyZeroInteractions(apiService)
    }

    @Test
    fun `does not call search endpoint when not connected`() {
        givenIsNotConnected()

        tested.onSearchQueryChanged(SEARCH_TERM)

        verifyZeroInteractions(apiService)

        verifyDoFinallyBlock()
    }

    @Test
    fun `does not show results when search term is invalid`() {
        givenIsConnected()

        givenSearchThrowsException()

        tested.onSearchQueryChanged(WRONG_SEARCH_TERM)

        verify(apiService).search(WRONG_SEARCH_TERM)

        assertThat(tested.emptyViewVisible.value).isEqualTo(true)
        assertThat(tested.listItems.value).isEqualTo(emptyList<ArtistItem>())
    }

    @Test
    fun `does show error message when searching and not connected`() {
        givenIsNotConnected()

        val tester = tested.commands.test()

        tested.onSearchQueryChanged(SEARCH_TERM)

        verifyZeroInteractions(apiService)

        verifyDoFinallyBlock()

        tester.assertValue(ShowErrorDialog("${R.string.spotify_search_failed}"))
    }

    @Test
    fun `does open Artist Detail screen when list item is clicked`() {
        val tester = tested.commands.test()

        tested.onListItemClicked(artist)

        tester.assertValue(OpenArtistDetailScreen(artist))
    }

    private fun givenSearchReturnsArtists() {
        whenever((apiService).search(SEARCH_TERM)).thenReturn(Single.just(searchResponse))
    }

    private fun givenSearchReturnsEmptyResults(){
        whenever((apiService).search(SEARCH_TERM)).thenReturn(Single.just(emptySearchResponse))
    }

    private fun givenSearchThrowsException() {
        whenever((apiService).search(WRONG_SEARCH_TERM)).thenReturn(Single.error(Exception()))
    }

    private fun givenIsConnected() {
        whenever((networkChecker).isConnectedToInternet).thenReturn(true)
    }

    private fun givenIsNotConnected() {
        whenever((networkChecker).isConnectedToInternet).thenReturn(false)
    }

    private fun verifyDoFinallyBlock() {
        assertThat(tested.emptyViewVisible.value).isEqualTo(false)
        assertThat(tested.isProgressVisible.value).isEqualTo(false)
    }

    private companion object {
        val SEARCH_TERM = "Any Artist"
        val WRONG_SEARCH_TERM = "(*!@"
    }

    private val artist = ArtistItem(
        ExternalUrls("https://spotify.com/"),
        listOf(),
        "",
        "1iweioajdoj1239",
        listOf(),
        "",
        2,
        "",
        ""
    )

    private val searchResponse = SearchResponse(
        Artists(
            "href", listOf(artist), 4, "next aritst", 3, "prev", 59
        )
    )

    private val emptySearchResponse = SearchResponse(
        Artists(
            "href", listOf(), 4, "next aritst", 3, "prev", 59
        )
    )
}
