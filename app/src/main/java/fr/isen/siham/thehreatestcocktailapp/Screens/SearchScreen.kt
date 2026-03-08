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
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun SearchResultScreen(modifier: Modifier, searchText: String) {

    val drinks = remember { mutableStateOf<List<DrinkModel>>(emptyList()) }
    val context = LocalContext.current

    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            NetworkManagement.api.searchCocktail(searchText)
                .enqueue(object : Callback<Drinks> {
                    override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                        drinks.value = response.body()?.drinks ?: emptyList()
                    }

                    override fun onFailure(call: Call<Drinks>, t: Throwable) {
                        Log.e("search", t.message.toString())
                    }
                })
        } else {
            drinks.value = emptyList()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFff9a9e), Color(0xFFfad0c4))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(drinks.value) { drink: DrinkModel ->
            Button(
                onClick = {
                    val intent = Intent(context, DetailCocktailActivity::class.java)
                    intent.putExtra("drinkID", drink.idDrink ?: "")
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = 0.3f),
                    contentColor = Color.White
                )
            ) {
                Text(text = drink.name ?: "", fontSize = 22.sp)
            }
        }
    }
}