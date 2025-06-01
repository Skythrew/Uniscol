package io.github.skythrew.uniscol.presentation.settings.accounts.edifice

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.skythrew.edificekt.EdificeClient
import io.github.skythrew.uniscol.R
import io.github.skythrew.uniscol.data.accounts.Account
import io.github.skythrew.uniscol.data.accounts.AccountRepository
import io.github.skythrew.uniscol.data.navigation.MailboxRoute
import io.github.skythrew.uniscol.data.navigation.TabRepository
import io.github.skythrew.uniscol.data.services.Services
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import javax.inject.Inject

data class EdificeInstance(
    val displayName: String,
    val url: String,
    val clientId: String,
    val clientSecret: String,
    val logo: Int
)

@HiltViewModel
class EdificeLoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tabsRepository: TabRepository
) : ViewModel() {
    val instances = mapOf(
        "NEO" to EdificeInstance(
            displayName = "NEO",
            url = "https://neoconnect.edifice.io",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_neo
        ),
        "La Charente" to EdificeInstance(
            displayName = "La Charente",
            url = "https://mon-ent16.lacharente.fr",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_charente
        ),
        "Colibri" to EdificeInstance(
            displayName = "Colibri",
            url = "https://colibri.ac-martinique.fr",
            clientId = "app-e",
            clientSecret = "w8k6saDuwaEz4pjxLQC7jEVC",
            logo = R.drawable.logo_colibri
        ),
        "ENT04" to EdificeInstance(
            displayName = "ENT04",
            url = "https://ent04.fr",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_ent04
        ),
        "LEIA" to EdificeInstance(
            displayName = "LEIA",
            url = "https://ent.leia.corsica",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_leia
        ),
        "EduProvence" to EdificeInstance(
            displayName = "EduProvence",
            url = "https://www.eduprovence.fr",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_eduprovence
        ),
        "ENT Hauts-de-France" to EdificeInstance(
            displayName = "ENT Hauts-de-France",
            url = "https://enthdf.fr",
            clientId = "app-e",
            clientSecret = "aY54xWeNHSSTuAbQSgg4ekav",
            logo = R.drawable.logo_hdf
        ),
        "ENT Hautes-Alpes" to EdificeInstance(
            displayName = "ENT Hautes-Alpes",
            url = "https://ent.colleges05.fr",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_hautes_alpes
        ),
        "ENT Mayotte" to EdificeInstance(
            displayName = "ENT Mayotte",
            url = "https://mayotte.edifice.io",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_mayotte
        ),
        "MonCollège" to EdificeInstance(
            displayName = "MonCollège",
            url = "https://www.moncollege-ent.essonne.fr",
            clientId = "app-e",
            clientSecret = "Kd2XTR6hYCU87TD75xGCmhSm",
            logo = R.drawable.logo_moncollege
        ),
        "Natirua" to EdificeInstance(
            displayName = "Natirua",
            url = "https://nati.pf",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_natirua
        ),
        "Seine-et-Yvelines Numérique" to EdificeInstance(
            displayName = "Seine-et-Yvelines Numérique",
            url = "https://ecole.sy-numerique.fr",
            clientId = "app-e",
            clientSecret = "se2NQD7U4mAkYa5wJP6C3UWy",
            logo = R.drawable.logo_syn_2d
        ),
        "ENT Var" to EdificeInstance(
            displayName = "ENT Var",
            url = "https://moncollege-ent.var.fr",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_var
        ),
        "Wilapa by NEO" to EdificeInstance(
            displayName = "Wilapa by NEO",
            url = "https://wilapa-guyane.com",
            clientId = "app-e",
            clientSecret = "yTFxAPupNnKb9VcKwA6E5DA3",
            logo = R.drawable.logo_wilapa
        ),
        "Édifice Formation" to EdificeInstance(
            displayName = "Édifice Formation",
            url = "https://formationneo.edifice.io",
            clientId = "app-e",
            clientSecret = "Qk7gKQMguhcQa6aXgVf9spHh",
            logo = R.drawable.logo_edifice
        )
    )

    private val _showWebview = MutableStateFlow(false)
    val showWebview: StateFlow<Boolean> = _showWebview

    private val _selectedInstance: MutableStateFlow<EdificeInstance?> = MutableStateFlow(null)
    val selectedInstance: StateFlow<EdificeInstance?> = _selectedInstance

    var _loginError = MutableStateFlow(false)
    val loginError = _loginError

    var _isLogging = MutableStateFlow(false)
    val isLogging = _isLogging

    fun webView(context: Context): WebView {
        return WebView(context).apply {
            settings.cacheMode = LOAD_NO_CACHE
            settings.javaScriptEnabled = true
            settings.userAgentString = "X-APP=mobile"
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    view?.loadUrl("javascript:(function f() { Android.getToken(document.head.innerHTML.match(\"{type:\\\"(.*)\\\"\")[1] , unescapeToken); })()")
                }
            }

            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.setSupportZoom(true)

            addJavascriptInterface(object {
                @JavascriptInterface
                fun getToken(type: String, token: String) {
                    when (type) {
                        "SAML" -> loginSaml(token)
                        else -> Log.d("DEBUG", "Unsupported token type")
                    }

                    _showWebview.value = false
                }
            }, "Android")
        }
    }

    fun loginSaml(saml: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedInstance = _selectedInstance.value!!

            val client = EdificeClient(selectedInstance.url)

            _isLogging.value = true

            try {
                val login = client.getTokensBySaml(
                    clientId = selectedInstance.clientId,
                    clientSecret = selectedInstance.clientSecret,
                    saml = saml,
                    scope = listOf(
                        "infra",
                        "actualites",
                        "blog",
                        "conversation",
                        "directory",
                        "homeworks",
                        "schoolbook",
                        "timeline",
                        "userinfo",
                        "workspace",
                        "portal",
                        "cas",
                        "sso",
                        "zimbra",
                        "presences",
                        "incidents",
                        "competences",
                        "diary",
                        "viescolaire",
                        "edt",
                        "support"
                    )
                )

                client.loginByOauth2Token(
                    clientId = selectedInstance.clientId,
                    clientSecret = selectedInstance.clientSecret,
                    accessTokenParam = login.accessToken,
                    refreshTokenParam = login.refreshToken
                )

                val userinfo = client.userInfo!!

                accountRepository.insertAccount(
                    Account(
                        service = Services.Edifice,
                        label = userinfo.firstName + " " + userinfo.lastName,
                        username = userinfo.username,
                        password = null,
                        accessToken = login.accessToken,
                        accessTokenExpiration = Clock.System.now()
                            .toEpochMilliseconds() + login.expiration * 1000,
                        refreshToken = login.refreshToken,
                        refreshTokenExpiration = null,
                        supportCanteen = false,
                        supportConversation = true,
                        clientId = selectedInstance.clientId,
                        clientSecret = selectedInstance.clientSecret,
                        instance = selectedInstance.url
                    )
                )

                _loginError.value = false
            } catch (e: Exception) {
                _loginError.value = true
                Log.e("ERR", e.message.toString())
            }

            tabsRepository.enableDestination(MailboxRoute().name)

            _isLogging.value = false
        }
    }

    fun selectInstance(instance: String) {
        _selectedInstance.value = instances[instance]
        _showWebview.value = true
    }
}