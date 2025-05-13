package io.github.skythrew.uniscol.data

import androidx.room.TypeConverter
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature.entries

class Converters {
    @TypeConverter
    fun restaurantFeaturefromString(feature: String): RestaurantAccountFeature {
        return entries.first {it.name == feature}
    }

    @TypeConverter
    fun restaurantFeatureToString(feature: RestaurantAccountFeature): String {
        return feature.name
    }

    @TypeConverter
    fun restaurantFeaturesfromString(features: String): List<RestaurantAccountFeature> {
        return features.split(',').map { feature -> entries.first {it.name == feature} }
    }

    @TypeConverter
    fun restaurantFeatureToString(features: List<RestaurantAccountFeature>): String {
        return features.joinToString(",") { it.name }
    }
}