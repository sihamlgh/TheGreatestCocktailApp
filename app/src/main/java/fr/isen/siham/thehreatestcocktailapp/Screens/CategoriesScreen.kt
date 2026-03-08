package fr.isen.siham.thehreatestcocktailapp.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import fr.isen.siham.thehreatestcocktailapp.Network.DrinkModel
import fr.isen.siham.thehreatestcocktailapp.Network.Drinks
import fr.isen.siham.thehreatestcocktailapp.Network.NetworkManagement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.navigation.NavController
import android.net.Uri
@Composable
fun CategoriesScreen(
    modifier: Modifier,
    navController: NavController
) {

    val categoriesState = remember {
        mutableStateOf<List<DrinkModel>>(emptyList())
    }

    LaunchedEffect(Unit) {

        NetworkManagement.api.getListCategory().enqueue(object : Callback<Drinks> {

            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                categoriesState.value = response.body()?.drinks ?: emptyList()
            }

            override fun onFailure(call: Call<Drinks>, t: Throwable) {
                Log.e("API", t.message ?: "error")
            }
        })
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFFff9a9e), Color(0xFFfad0c4))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(categoriesState.value) { category ->

            val name = category.category ?: ""

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = Color(0xFF9C27B0).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable {
                        navController.navigate("drinks/${Uri.encode(name)}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}