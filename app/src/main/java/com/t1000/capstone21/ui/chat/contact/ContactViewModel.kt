package com.t1000.capstone21.ui.chat.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.t1000.capstone21.Repo
import com.t1000.capstone21.models.User
import com.t1000.capstone21.models.Video

class ContactViewModel : ViewModel() {
    private val repo = Repo.getInstance()

    fun fetchFollow(userId: String) : LiveData<User> = liveData {
        emit(repo.fetchUserById(userId))
    }
}