package app.sheepfold.android.router

import android.content.Context

enum class InternetAccessState {
    Enabled,
    Disabled
}

object InternetControlRepository {
    private const val prefsName = "sheepfold_state"
    private const val internetStateKey = "internet_state"

    fun readInternetState(context: Context): InternetAccessState {
        val value = context.applicationContext
            .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .getString(internetStateKey, InternetAccessState.Enabled.name)

        return runCatching { InternetAccessState.valueOf(value.orEmpty()) }
            .getOrDefault(InternetAccessState.Enabled)
    }

    fun setInternetState(context: Context, state: InternetAccessState) {
        context.applicationContext
            .getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            .edit()
            .putString(internetStateKey, state.name)
            .apply()
    }
}
