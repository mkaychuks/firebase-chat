package com.mkaychuks.jetpack_app.feature.auth.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow<SignInState>(SignInState.Idle)
    val state = _state.asStateFlow()


    fun signIn(email: String, password: String){
        _state.value = SignInState.Loading
        // firebase signIn
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    task.result.user?.let {
                        _state.value = SignInState.Success
                        return@addOnCompleteListener
                    }
                    _state.value = SignInState.Error
                } else{
                    _state.value = SignInState.Error
                }
            }
    }

}



sealed class SignInState{
    data object Idle: SignInState()
    data object Loading: SignInState()
    data object Success: SignInState()
    data object Error: SignInState()
}