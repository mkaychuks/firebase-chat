package com.mkaychuks.jetpack_app.feature.auth.signup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mkaychuks.jetpack_app.common.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(): ViewModel() {
    private var _state = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val state = _state.asStateFlow()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun createUser(name: String, email: String, password: String){
        _state.value = SignUpState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    task.result.user?.let { user ->
                        val newUser = User(email = email, uid = user.uid, fullName = name)
                        firestore.collection("users").document(user.uid).set(newUser)
                            .addOnCompleteListener{firestoreTask ->
                                if(firestoreTask.isSuccessful){
                                    _state.value = SignUpState.Success
                                }
                            }
                        return@addOnCompleteListener
                    }
                    _state.value = SignUpState.Error
                } else {
                    _state.value = SignUpState.Error
                }
            }
    }

}


sealed class SignUpState{
    data object Idle: SignUpState()
    data object Loading: SignUpState()
    data object Success: SignUpState()
    data object Error: SignUpState()
}