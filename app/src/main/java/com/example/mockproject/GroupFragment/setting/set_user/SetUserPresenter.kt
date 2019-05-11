package com.example.mockproject.GroupFragment.setting.set_user

class SetUserPresenter(
        var setUserView: SetUserView,
        val setUserInteractor: SetUserInteractor){

    fun onShowDirectory(){
        setUserView.showDirector()
    }

}