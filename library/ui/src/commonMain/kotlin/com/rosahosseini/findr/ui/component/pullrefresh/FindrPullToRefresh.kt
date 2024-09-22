package com.rosahosseini.findr.ui.component.pullrefresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

object FindrPullToRefresh {
    /**
     * [PullToRefreshBox] is a container that expects a scrollable layout as content and adds gesture
     * support for manually refreshing when the user swipes downward at the beginning of the content. By
     * default, it uses [FindrPullToRefresh.Indicator] as the refresh indicator.
     * *
     * View models can be used as source as truth as shown in
     * A custom state implementation can be initialized like this
     * Scaling behavior can be implemented like this
     * @param isRefreshing whether a refresh is occurring
     * @param onRefresh callback invoked when the user gesture crosses the threshold, thereby requesting
     *   a refresh.
     * @param modifier the [Modifier] to be applied to this container
     * @param state the state that keeps track of distance pulled
     * @param contentAlignment The default alignment inside the Box.
     * @param indicator the indicator that will be drawn on top of the content when the user begins a
     *   pull or a refresh is occurring
     * @param content the content of the pull refresh container, typically a scrollable layout such as
     *   [LazyColumn] or a layout using [Modifier.verticalScroll]
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PullToRefreshBox(
        isRefreshing: Boolean,
        onRefresh: () -> Unit,
        modifier: Modifier = Modifier,
        state: PullToRefreshState = rememberPullToRefreshState(),
        contentAlignment: Alignment = Alignment.TopStart,
        indicator: @Composable BoxScope.() -> Unit = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = state,
                isRefreshing = isRefreshing
            )
        },
        content: @Composable BoxScope.() -> Unit
    ) {
        androidx.compose.material3.pulltorefresh.PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
            state = state,
            contentAlignment = contentAlignment,
            indicator = indicator,
            content = content
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Indicator(
        state: PullToRefreshState,
        isRefreshing: Boolean,
        modifier: Modifier = Modifier
    ) {
        PullToRefreshDefaults.Indicator(
            state = state,
            modifier = modifier,
            containerColor = MaterialTheme.colorScheme.onPrimary,
            color = MaterialTheme.colorScheme.primary,
            isRefreshing = isRefreshing
        )
    }
}
