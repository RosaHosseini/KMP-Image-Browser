package com.rosahosseini.findr.ui.component.pullrefresh

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.rosahosseini.findr.ui.extensions.doOncePerInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun RefreshableBox(
    state: PullToRefreshState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.nestedScroll(state.nestedScrollConnection)) {
        content()
        PullRefreshIndicator(state = state, modifier = Modifier.align(Alignment.TopCenter))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullRefreshIndicator(
    state: PullToRefreshState,
    modifier: Modifier = Modifier
) {
    PullToRefreshContainer(
        state = state,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.onPrimary,
        contentColor = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberPullToRefreshState(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    enabled: Boolean = true
): PullToRefreshState {
    val state = rememberPullToRefreshState(enabled = { enabled })
    var isConsumed by remember { mutableStateOf(true) }

    state.isRefreshing.doOncePerInstance {
        isConsumed = false
        if (state.isRefreshing) onRefresh()
    }

    // Don't change it to launch effect because in fast changes compose skip running that
    if (!refreshing && !isConsumed) {
        state.endRefresh()
        isConsumed = true
    }
    return state
}
