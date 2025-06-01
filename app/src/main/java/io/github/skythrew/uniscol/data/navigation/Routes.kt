package io.github.skythrew.uniscol.data.navigation

import kotlinx.serialization.Serializable

interface Route {
    val name: String
    val route: Any
}

data class HomeRoute(
    override val name: String = "home",
    override val route: Any = Routes.Home
) : Route

data class SettingsRoute(
    override val name: String = "settings",
    override val route: Any = Routes.Settings
) : Route

data class RestaurantRoute(
    override val name: String = "restaurant",
    override val route: Any = Routes.Restaurant
) : Route

/*data class AccountSettingsRoute(
    override val name: String = "account_settings",
    override val route: Any = Routes.AccountSettings
) : Route


data class InterfaceSettingsRoute(
    override val name: String = "interface_settings",
    override val route: Any = Routes.InterfaceSettings
) : Route


data class TurboselfLoginRoute(
    override val name: String = "turboself_login",
    override val route: Any = Routes.TurboselfLogin
) : Route


data class EdificeLoginRoute(
    override val name: String = "edifice_login",
    override val route: Any = Routes.EdificeLogin
) : Route*/

data class MailboxRoute(
    override val name: String = "mailbox",
    override val route: Any = Routes.Mailbox
) : Route


object Routes {
    @Serializable
    object Home
    @Serializable
    object Settings
    @Serializable
    object Restaurant
    @Serializable
    object AccountSettings
    @Serializable
    object InterfaceSettings
    @Serializable
    object TurboselfLogin
    @Serializable
    object EdificeLogin
    @Serializable
    object Mailbox

    @Serializable
    data class Message(val id: String, val title: String)
}

val BASE_ROUTES = listOf(HomeRoute(), RestaurantRoute(), MailboxRoute(), SettingsRoute())