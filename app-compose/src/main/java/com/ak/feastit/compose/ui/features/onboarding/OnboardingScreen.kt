package com.ak.feastit.compose.ui.features.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ak.feastit.compose.R
import com.ak.feastit.ui.onboarding.OnBoardingViewModel
import kotlinx.coroutines.launch

@Composable
internal fun OnboardingScreen(
    onNavigateToHome: () -> Unit,
    viewModel: OnBoardingViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.state.collect { state ->
            if (state is OnBoardingViewModel.OnBoardingState.NavigateToHome) {
                onNavigateToHome()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextButton(
            onClick = { viewModel.finishOnBoarding() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = stringResource(id = R.string.skip))
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPage(page = page)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Page Indicators
            Row {
                repeat(3) { index ->
                    val color = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    }
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp),
                        shape = MaterialTheme.shapes.small,
                        color = color
                    ) {}
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        viewModel.finishOnBoarding()
                    }
                }
            ) {
                Text(
                    text = if (pagerState.currentPage == 2) {
                        stringResource(id = R.string.get_started)
                    } else {
                        stringResource(id = R.string.next)
                    }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(page: Int) {
    val titleRes = when (page) {
        0 -> R.string.onboarding_title_1
        1 -> R.string.onboarding_title_2
        else -> R.string.onboarding_title_3
    }
    val subtitleRes = when (page) {
        0 -> R.string.onboarding_sub_title_1
        1 -> R.string.onboarding_sub_title_2
        else -> R.string.onboarding_sub_title_3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = titleRes),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = subtitleRes),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
