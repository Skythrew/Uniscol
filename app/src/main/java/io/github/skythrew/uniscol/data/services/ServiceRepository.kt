package io.github.skythrew.uniscol.data.services

import io.github.skythrew.uniscol.R

class ServiceRepository {
    fun getIconForService(service: Services): Int {
        return when (service) {
            Services.Turboself -> R.drawable.turboself
            Services.Edifice -> R.drawable.logo_neo
        }
    }
}