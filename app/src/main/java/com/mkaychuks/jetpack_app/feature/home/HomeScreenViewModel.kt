package com.mkaychuks.jetpack_app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mkaychuks.jetpack_app.common.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor() : ViewModel() {
    private var _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Idle)
    val state = _state.asStateFlow()

    private var _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    init {
        viewModelScope.launch {
            getAllUsersExceptLoggedInUser()
        }
    }

    private fun getAllUsersExceptLoggedInUser() {
        _state.value = HomeScreenState.Loading
        val currentUserUid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            firestore.collection("users")
                .whereNotEqualTo("uid", currentUserUid)
                .addSnapshotListener{ snapshots, e ->
                    if (e != null){
                        _state.value = HomeScreenState.Error
                    }
                    if(snapshots != null && !snapshots.isEmpty){
                        val userList = snapshots.documents.map { document -> document.toObject(User::class.java) ?: User() }
                        _state.value = HomeScreenState.Success
                        _userList.value = userList
                    }
                }
        }
    }

    fun signOutUser(){
        auth.signOut()
    }
}


sealed class HomeScreenState {
    data object Idle : HomeScreenState()
    data object Loading : HomeScreenState()
    data object Success : HomeScreenState()
    data object Error : HomeScreenState()
}