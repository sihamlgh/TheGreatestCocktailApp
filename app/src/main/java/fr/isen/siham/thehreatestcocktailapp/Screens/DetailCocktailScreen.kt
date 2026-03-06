package fr.isen.siham.thehreatestcocktailapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.siham.thehreatestcocktailapp.Network.DrinkModel
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response
import fr.isen.siham.thehreatestcocktailapp.Network.Drinks
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import fr.isen.siham.thehreatestcocktailapp.Network.NetworkManagement
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults


data class Ingredient(val ingredient: String, val measure: String)

@Composable
fun DetailCocktailScreen(
    modifier: Modifier,
    drinkID: String,
    onDrinkLoaded: (String) -> Unit
) {

    val drink: MutableState<DrinkModel> = remember {
        mutableStateOf(value = DrinkModel())
    }

    val ingredients = listOf(
        Ingredient(drink.value.ingredient1 ?: "", drink.value.measure1 ?: ""),
        Ingredient(drink.value.ingredient2 ?: "", drink.value.measure2 ?: ""),
        Ingredient(drink.value.ingredient3 ?: "", drink.value.measure3 ?: "")
    ).filter { it.ingredient.isNotBlank() }

    LaunchedEffect(drinkID) {
        val call =
            if (drinkID.isEmpty())
                NetworkManagement.api.getRandomCocktail()
            else
                NetworkManagement.api.getCocktailById(drinkID)

        call.enqueue(object : Callback<Drinks> {

            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                drink.value = response.body()?.drinks?.first() ?: DrinkModel()
                drink.value.idDrink?.let { onDrinkLoaded(it) }
            }

            override fun onFailure(call: Call<Drinks>, t: Throwable) {
                Log.e("error", t.message.toString())
            }
        })
    }

    Column(
        modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFFff9a9e), Color(0xFFfad0c4))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AsyncImage(
            model = drink.value.imageURL,
            contentDescription = drink.value.name ?: "",
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Row(Modifier.wrapContentHeight()) {
            Box(
                Modifier
                    .background(
                        color = Color.DarkGray.copy(0.7f),
                        shape = RoundedCornerShape(25.dp)
                    )
            )
        }
        Text(
            text = drink.value.name ?: "",
            color = Color.White,
            fontSize = 35.sp
        )

        Card(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(45.dp),
            shape = RoundedCornerShape(16.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color.DarkGray.copy(alpha = 0.6f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = drink.value.category ?: "",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = Color(0xFFFFF0F6).copy(alpha = 0.3f)
            )
        ) {
            Column(Modifier.padding(16.dp)) {

                Text(
                    text = "Ingredients",
                    color = Color.White,
                    fontSize = 28.sp
                )

                ingredients.forEach { (ingredient, measure) ->
                    Row(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(vertical = 6.dp)
                    ) {
                        Text(
                            text = ingredient,
                            color = Color.White,
                            fontSize = 20.sp
                        )

                        Spacer(Modifier.weight(1f))

                        Text(
                            text = measure,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color(0xFF9C27B0).copy(alpha = 0.3f)
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            text = "Recette",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = drink.value.instructions ?: "",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                    val alcoolText = when (drink.value.alcoholic) {
                        "Alcoholic" -> "Alcoolisé"
                        "Non alcoholic" -> "Sans alcool"
                        else -> "Optionnel"
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFC1E3)
                        )
                    ) {
                        Text(
                            text = alcoolText,
                            modifier = Modifier.padding(8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}