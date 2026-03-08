package fr.isen.siham.thehreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.siham.thehreatestcocktailapp.Screens.CategoriesScreen
import fr.isen.siham.thehreatestcocktailapp.Screens.DetailCocktailScreen
import fr.isen.siham.thehreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBarItemDefaults
import fr.isen.siham.thehreatestcocktailapp.Screens.DrinksScreen
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.platform.LocalContext
import fr.isen.siham.thehreatestcocktailapp.FavoriteScreen
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import android.app.Activity
import fr.isen.siham.thehreatestcocktailapp.Screens.SearchResultScreen

enum class NavigationItem(
    val title: String,
    val icon: ImageVector,
    val route: String
){
    Home( title = "Home", Icons.Default.Home, route = "home"),
    List( title = "Categories", Icons.Default.Menu, route = "list"),
    Fav( title = "Favoris", Icons.Default.Favorite, route = "fav")
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val startNavigationItem = NavigationItem.Home
                val currentNavigationItem = remember { mutableStateOf(startNavigationItem) }
               val currentDrinkId = remember { mutableStateOf<String?>(null) }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(snackbarHostState, null)
                    },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    },
                    bottomBar = {
                        NavigationBar( containerColor = Color(0xFFF06292)) {
                            NavigationItem.values().forEach { navigationItem ->
                                NavigationBarItem(
                                    selected = currentNavigationItem.value == navigationItem,
                                    onClick = {
                                        navController.navigate(navigationItem.route)
                                        currentNavigationItem.value = navigationItem
                                    },
                                    label = {
                                        Text(navigationItem.title)
                                    },
                                    icon = {
                                        Icon(navigationItem.icon, contentDescription = "")
                                    },
                                    colors = NavigationBarItemDefaults.colors(

                                        selectedIconColor = Color.White,
                                        selectedTextColor = Color.White,

                                        unselectedIconColor = Color.White.copy(0.5f),
                                        unselectedTextColor = Color.White.copy(0.5f),

                                        indicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                 ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startNavigationItem.route
                    ) {
                        NavigationItem.values().forEach { navigationItem ->
                            composable(navigationItem.route) {
                                when (navigationItem) {
                                    NavigationItem.Home -> DetailCocktailScreen(
                                        modifier = Modifier.padding(innerPadding),
                                        drinkID = currentDrinkId.value ?: "",
                                        onDrinkLoaded = { id ->
                                            currentDrinkId.value = id
                                        },
                                        navController = navController
                                    )
                                    NavigationItem.List ->
                                        CategoriesScreen(
                                            Modifier.padding(innerPadding),
                                            navController
                                        )
                                    NavigationItem.Fav -> {FavoriteScreen(Modifier.padding(innerPadding))}

                                }
                            }
                        }

                        composable("drinks/{category}") { backStackEntry ->

                            val category = backStackEntry.arguments?.getString("category") ?: ""

                            DrinksScreen(
                                modifier = Modifier.padding(innerPadding),
                                category = category
                            )
                        }
                        composable("search/{query}") { backStackEntry ->

                            val query = backStackEntry.arguments?.getString("query") ?: ""

                            SearchResultScreen(
                                modifier = Modifier.padding(innerPadding),
                                searchText = query
                            )
                        }
                    }
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(snackbarHostState: SnackbarHostState, drinkID: String? = null) {

    CenterAlignedTopAppBar(
        title = { Text("Random", color = Color.White) },

        navigationIcon = {
            if (drinkID != null) {
                val activity = (LocalContext.current as? Activity)

                IconButton(onClick = {
                    activity?.finish()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White
                    )
                }
            }
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFF06292)
        ),

        actions = {
            val added = "Ajouté aux favoris"
            val removed = "Retiré des favoris"
            val snackbarScope = rememberCoroutineScope()

            val context = LocalContext.current
            val sharedPreferences = SharedPreferencesHelper(context)
            val drinkList: ArrayList<String> = sharedPreferences.getFavoriteList()
            val isFav = remember { mutableStateOf(getFavoriteStatusForID(drinkID, drinkList)) }

            IconToggleButton(
                checked = isFav.value,
                onCheckedChange = {
                    isFav.value = !isFav.value
                    snackbarScope.launch {
                        snackbarHostState.showSnackbar(if (isFav.value) added else removed)
                    }

                    if (drinkID != null) {
                        updateFavoriteList(
                            drinkID,
                            isFav.value,
                            sharedPreferences,
                            drinkList
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = if (isFav.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "fav"
                )
            }
        }
    )
}

fun getFavoriteStatusForID(drinkID: String?, list: ArrayList<String>): Boolean {
    for (id: String in list) {
        if (drinkID == id) {
            return true
        }
    }
    return false
}
fun updateFavoriteList(drinkID: String, shouldBeAdded: Boolean, sharedPreferencesHelper: SharedPreferencesHelper, list: ArrayList<String>) {
    if (shouldBeAdded) {
        list.add(drinkID)
    } else {
        list.remove(drinkID)
    }
    sharedPreferencesHelper.saveFavoriteList(list)
}







































































































