package com.minhagrana

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.minhagrana.ui.presentation.entries.AnnualBalanceScreen
import com.minhagrana.ui.presentation.entries.EntriesScreen
import com.minhagrana.ui.presentation.entries.EntryScreen
import com.minhagrana.ui.presentation.home.HomeScreen
import com.minhagrana.ui.presentation.newentry.NewEntryScreen
import com.minhagrana.ui.theme.AppTheme
import kotlinx.serialization.Serializable
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.compose_multiplatform
import minhagrana.composeapp.generated.resources.ic_add
import minhagrana.composeapp.generated.resources.ic_home
import minhagrana.composeapp.generated.resources.ic_report
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
    AppTheme {
        Surface(
            modifier =
                Modifier
                    .imePadding()
                    .background(MaterialTheme.colorScheme.background),
        ) {
            val navController = rememberNavController()
            BottomNavigationBar(navController)
        }
    }
}

@Composable
fun BottomNavigationBar(rootNavController: NavHostController) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavigationItems =
        listOf(
            BottomNavigationItem(
                label = "Home",
                route = HomeRoute.Home,
                customIconRes = Res.drawable.ic_home,
            ),
            BottomNavigationItem(
                label = "New Entry",
                route = NewEntryRoute.NewEntry,
                customIconRes = Res.drawable.ic_add,
            ),
            BottomNavigationItem(
                label = "Entries",
                route = EntriesRoute.Entries,
                customIconRes = Res.drawable.ic_report,
            ),
        )

    val showBottomNav =
        currentDestination?.hasRoute(HomeRoute.Home::class) == true ||
            currentDestination?.hasRoute(EntriesRoute.Entries::class) == true ||
            currentDestination?.hasRoute(NewEntryRoute.NewEntry::class) == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    bottomNavigationItems.forEachIndexed { index, navigationItem ->
                        val isSelected =
                            currentDestination?.hierarchy?.any {
                                it.hasRoute(navigationItem.route::class)
                            } == true
                        NavigationBarItem(
                            modifier = Modifier.padding(top = 16.dp),
                            colors =
                                NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                            selected = isSelected,
                            icon = {
                                Icon(
                                    painter = painterResource(navigationItem.customIconRes),
                                    contentDescription = navigationItem.label,
                                    tint =
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.secondary
                                        },
                                )
                            },
                            onClick = {
                                navController.navigate(navigationItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = HomeRoute.Root,
            ) {
                homeNavGraph(navController = navController)
                newEntryNavGraph(navController = navController)
                entriesNavGraph(navController = navController)
            }
        }
    }
}

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation<HomeRoute.Root>(
        startDestination = HomeRoute.Home,
    ) {
        composable<HomeRoute.Home> {
            HomeScreen()
        }
    }
}

fun NavGraphBuilder.newEntryNavGraph(navController: NavHostController) {
    navigation<NewEntryRoute.Root>(
        startDestination = NewEntryRoute.NewEntry,
    ) {
        composable<NewEntryRoute.NewEntry> {
            NewEntryScreen(
                onEntrySaved = {
                    navController.navigate(EntriesRoute.Entries) {
                        popUpTo(EntriesRoute.Entries) { inclusive = true }
                    }
                },
                onCancel = {
                    navController.navigate(EntriesRoute.Entries) {
                        popUpTo(EntriesRoute.Entries) { inclusive = true }
                    }
                },
            )
        }
    }
}

fun NavGraphBuilder.entriesNavGraph(navController: NavHostController) {
    navigation<EntriesRoute.Root>(
        startDestination = EntriesRoute.Entries,
    ) {
        composable<EntriesRoute.Entries> {
            EntriesScreen(
                onEntriesByYearSelected = {
                    navController.navigate(EntriesRoute.AnnualEntries)
                },
                onEntrySelected = { entry ->
                    navController.navigate(EntriesRoute.EditEntry) {
                        popUpTo(EntriesRoute.Entries) { inclusive = false }
                    }
                },
            )
        }
        composable<EntriesRoute.EditEntry> {
            EntryScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                onSaveEntrySelected = {
                    navController.navigate(EntriesRoute.Entries) {
                        popUpTo(EntriesRoute.Entries) { inclusive = true }
                    }
                },
            )
        }
        composable<EntriesRoute.AnnualEntries> {
            AnnualBalanceScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                onMonthSelected = { month ->
                    navController.navigate(EntriesRoute.Entries) {
                        popUpTo(EntriesRoute.Entries) { inclusive = false }
                    }
                },
            )
        }
    }
}

data class BottomNavigationItem<T : Any>(
    val label: String = "",
    val route: T,
    val customIconRes: org.jetbrains.compose.resources.DrawableResource = Res.drawable.compose_multiplatform,
    val useCustomIcon: Boolean = false,
)

@Serializable
sealed class HomeRoute {
    @Serializable
    data object Root : HomeRoute()

    @Serializable
    data object Home : HomeRoute()
}

@Serializable
sealed class NewEntryRoute {
    @Serializable
    data object Root : NewEntryRoute()

    @Serializable
    data object NewEntry : NewEntryRoute()
}

@Serializable
sealed class EntriesRoute {
    @Serializable
    data object Root : EntriesRoute()

    @Serializable
    data object Entries : EntriesRoute()

    @Serializable
    data object EditEntry : EntriesRoute()

    @Serializable
    data object AnnualEntries : EntriesRoute()
}
