package com.example.mockproject.common

import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object Constant {

    var idImage: Int = 1
    var stateInternet: Boolean = false
    var id: String = ""
    var email: String? = "sang.pham.intern@ntq-solution.com.vn"
    var userName: String? = "Phạm Xuân Sang"
    var avt: String? = "sang"
    var familyName: String? = "Phạm Xuân"
    var givenName: String? = "Sang"
    var dateOfBirth: String = "01/01/2001"
    var skype: String = "Sang"
    var phone: String = "123456789"
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().build()
    var isOperation = true
    var idCaptainWhenClickGroup: String? = ""
    var idGroupWhenClickGroup: String = ""
    const val showDetailGroup = false
    const val timeToSlideViewFlipper = 4000
    const val requestCode = 111
    const val timeToSplashScreen = 3500
    const val oneHundredMiliSecond = 100
    const val oneHundredMiliSecondLong: Long = 100
    const val joned = "JOIN"
    const val unjoned = "UNJOIN"
    const val wait = "WAIT"
    var date = ""
    val SELECT_IMAGE = 1
    var description: String = ""
    var imageBanner :String=""
    var imageAvatar :String=""

    const val DIALOG_FRAGMENT = 100001
}