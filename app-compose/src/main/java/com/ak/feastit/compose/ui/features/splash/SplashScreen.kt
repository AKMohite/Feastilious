package com.ak.feastit.compose.ui.features.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ak.feastit.compose.R
import com.ak.feastit.ui.splash.SplashViewModel
import com.ak.feastit.ui.splash.SplashViewModel.SplashState

@Composable
internal fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.splashState.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        when (state) {
            SplashState.NavigateToOnBoarding -> onNavigateToOnboarding()
            SplashState.NavigateToHome -> onNavigateToHome()
            SplashState.Empty -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayLarge
        )
    }
}
