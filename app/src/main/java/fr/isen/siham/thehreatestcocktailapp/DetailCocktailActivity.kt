package fr.isen.siham.thehreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.siham.thehreatestcocktailapp.Screens.DetailCocktailScreen
import fr.isen.siham.thehreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import androidx.compose.runtime.remember
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import fr.isen.siham.thehreatestcocktailapp.TopAppBar

class DetailCocktailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkID = intent.getStringExtra("drinkID") ?: ""
        enableEdgeToEdge()
        setContent {
            TheGreatestCocktailAppTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                    TopAppBar(snackbarHostState, drinkID)
                },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    }
                ) { innerPadding ->

                    DetailCocktailScreen(
                        modifier = Modifier.padding(innerPadding),
                        drinkID = drinkID,
                        onDrinkLoaded = { }
                    )
                }
            }
        }
    }
}
