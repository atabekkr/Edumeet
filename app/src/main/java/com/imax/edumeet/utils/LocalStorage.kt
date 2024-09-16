package com.imax.edumeet.utils

import android.content.SharedPreferences

class LocalStorage(preference: SharedPreferences) {

    var isLogin by BooleanPreference(preference, false)

    var token by StringPreference(preference)

    var group by StringPreference(preference)

    var studentId by StringPreference(preference)

}