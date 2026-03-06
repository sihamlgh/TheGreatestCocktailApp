package fr.isen.siham.thehreatestcocktailapp.Network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Drinks (
    val drinks: List<DrinkModel>

    ):  Serializable

class DrinkModel(
    @SerializedName("strIngredient1") val ingredient1: String? = null,
    @SerializedName("strIngredient2") val ingredient2: String? = null,
    @SerializedName("strIngredient3") val ingredient3: String? = null,
    @SerializedName("strMeasure1") val measure1: String? = null,
    @SerializedName("strMeasure2") val measure2: String? = null,
    @SerializedName("strMeasure3") val measure3: String? = null,
    @SerializedName("strCategory")
    val category: String? = null,
    @SerializedName("strDrink")
    val name: String? = null,
    @SerializedName("strInstructions") val instructions: String? = null,
    @SerializedName("strDrinkThumb")
    val imageURL: String? = null,
    @SerializedName("strAlcoholic") val alcoholic: String? = null,
    @SerializedName("idDrink")
    val idDrink: String? = null,
): Serializable