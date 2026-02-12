package com.minhagrana

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.navigation.toRoute
import com.minhagrana.ui.presentation.entries.AnnualBalanceScreen
import com.minhagrana.ui.presentation.entries.EntriesScreen
import com.minhagrana.ui.presentation.entries.EntryScreen
import com.minhagrana.ui.presentation.home.HomeScreen
import kotlinx.serialization.Serializable
import minhagrana.composeapp.generated.resources.Res
import minhagrana.composeapp.generated.resources.ic_data
import org.jetbrains.compose.resources.painterResource

@Composable
fun App() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}

@Composable
fun BottomNavigationBar(rootNavController: NavHostController) {
    var navigationSelectedItem by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavigationItems =
        listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Default.Home,
                route = HomeRoute.Home,
            ),
            BottomNavigationItem(
                label = "Entries",
                icon = Icons.Default.Home,
                route = EntriesRoute.Entries(),
                useCustomIcon = true,
                customIconRes = Res.drawable.ic_data,
            ),
            BottomNavigationItem(
                label = "",
                icon = Icons.Default.Add,
                route = EntriesRoute.Entries(showBottomEntryItem = true),
            ),
        )

    val showBottomNav =
        bottomNavigationItems.any {
            currentDestination?.hasRoute(it.route::class) == true
        }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNav,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    bottomNavigationItems.forEachIndexed { index, navigationItem ->
                        NavigationBarItem(
                            modifier = Modifier.padding(top = 16.dp),
                            colors =
                                NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.onPrimary,
                                ),
                            selected =
                                currentDestination?.hierarchy?.any {
                                    it.hasRoute(navigationItem.route::class)
                                } == true,
                            icon = {
                                if (navigationItem.useCustomIcon && navigationItem.customIconRes != null) {
                                    Icon(
                                        painter = painterResource(navigationItem.customIconRes),
                                        contentDescription = navigationItem.label,
                                        tint =
                                            if (navigationSelectedItem == index) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.secondary
                                            },
                                    )
                                } else {
                                    Icon(
                                        imageVector = navigationItem.icon,
                                        contentDescription = navigationItem.label,
                                        tint =
                                            if (navigationSelectedItem == index) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.secondary
                                            },
                                    )
                                }
                            },
                            onClick = {
                                if (currentDestination?.route != navigationItem.route.toString()) {
                                    navigationSelectedItem = index
                                    navController.navigate(navigationItem.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
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

fun NavGraphBuilder.entriesNavGraph(navController: NavHostController) {
    navigation<EntriesRoute.Root>(
        startDestination = EntriesRoute.Entries(),
    ) {
        composable<EntriesRoute.Entries> {
            val args = it.toRoute<EntriesRoute.Entries>()
            EntriesScreen(
                showBottomSheetNav = args.showBottomEntryItem,
                onEntriesByYearSelected = {
                    navController.navigate(EntriesRoute.AnnualEntries)
                },
                onEntrySelected = {
                    navController.navigate(EntriesRoute.EditEntry) {
                        popUpTo(EntriesRoute.Entries(false)) { inclusive = false }
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
                    navController.navigate(EntriesRoute.Entries(false)) {
                        popUpTo(EntriesRoute.Entries(false)) { inclusive = true }
                    }
                },
            )
        }
        composable<EntriesRoute.AnnualEntries> {
            AnnualBalanceScreen(
                navigateUp = {
                    navController.navigateUp()
                },
                onMonthSelected = {
                    navController.navigate(EntriesRoute.Entries(false)) {
                        popUpTo(EntriesRoute.Entries(false)) { inclusive = false }
                    }
                },
            )
        }
    }
}

data class BottomNavigationItem<T : Any>(
    val label: String = "",
    val icon: ImageVector = Icons.Default.Home,
    val route: T,
    val useCustomIcon: Boolean = false,
    val customIconRes: org.jetbrains.compose.resources.DrawableResource? = null,
)

// Navigation Routes

@Serializable
sealed class HomeRoute {
    @Serializable
    data object Root : HomeRoute()

    @Serializable
    data object Home : HomeRoute()
}

@Serializable
sealed class EntriesRoute {
    @Serializable
    data object Root : EntriesRoute()

    @Serializable
    data class Entries(
        val showBottomEntryItem: Boolean = false,
    ) : EntriesRoute()

    @Serializable
    data object EditEntry : EntriesRoute()

    @Serializable
    data object AnnualEntries : EntriesRoute()
}
