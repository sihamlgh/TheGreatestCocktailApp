package fr.isen.siham.thehreatestcocktailapp.Screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.siham.thehreatestcocktailapp.DetailCocktailActivity
import fr.isen.siham.thehreatestcocktailapp.Network.DrinkModel
import fr.isen.siham.thehreatestcocktailapp.Network.Drinks
import fr.isen.siham.thehreatestcocktailapp.Network.NetworkManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import android.app.Activity

@Composable
fun DrinksScreen(modifier: Modifier, category: String) {

    val drinks = remember { mutableStateOf<List<DrinkModel>>(listOf()) }

    LaunchedEffect(Unit) {
        val call: Call<Drinks> =
            NetworkManagement.api.getDrinkByCategory(category.replace(" ", "_"))

        call.enqueue(object : Callback<Drinks> {

            override fun onResponse(p0: Call<Drinks?>, p1: Response<Drinks?>) {
                drinks.value = p1.body()?.drinks ?: listOf()
            }

            override fun onFailure(p: Call<Drinks?>, p1: Throwable) {
                Log.e("error", p1.message.toString())
            }
        })
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFff9a9e), Color(0xFFfad0c4))
                )
            )
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(category, color = Color.White, fontSize = 40.sp)
            }

            items(drinks.value) { drink ->

                val context = LocalContext.current

                Button(
                    onClick = {
                        val intent = Intent(context, DetailCocktailActivity::class.java)
                        intent.putExtra("drinkID", drink.idDrink)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.3f),
                        contentColor = Color.White
                    )
                ) {
                    Text(drink.name ?: "", fontSize = 24.sp)
                }
            }
        }
    }
}