package io.github.skythrew.uniscol.data

import androidx.room.TypeConverter
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature
import io.github.skythrew.uniscol.data.accounts.restaurant.RestaurantAccountFeature.entries
import io.github.skythrew.uniscol.data.navigation.BASE_ROUTES
import io.github.skythrew.uniscol.data.navigation.Route
import io.github.skythrew.uniscol.data.navigation.TabIcon

class Converters {
    @TypeConverter
    fun restaurantFeaturefromString(feature: String): RestaurantAccountFeature {
        return entries.first { it.name == feature }
    }

    @TypeConverter
    fun restaurantFeatureToString(feature: RestaurantAccountFeature): String {
        return feature.name
    }

    @TypeConverter
    fun restaurantFeaturesfromString(features: String): List<RestaurantAccountFeature> {
        return features.split(',').map { feature -> entries.first { it.name == feature } }
    }

    @TypeConverter
    fun restaurantFeatureToString(features: List<RestaurantAccountFeature>): String {
        return features.joinToString(",") { it.name }
    }

    @TypeConverter
    fun tabIconFromString(icon: String): TabIcon {
        return TabIcon.valueOf(icon)
    }

    @TypeConverter
    fun tabIconToString(icon: TabIcon): String {
        return icon.name
    }

    @TypeConverter
    fun routeFromString(route: String): Route {
        return BASE_ROUTES.first { it.name == route }
    }

    @TypeConverter
    fun routeToString(route: Route): String {
        return route.name
    }
}