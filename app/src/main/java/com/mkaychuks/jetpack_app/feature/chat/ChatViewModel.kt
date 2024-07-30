package com.mkaychuks.jetpack_app.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mkaychuks.jetpack_app.common.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    private var _chatMessages = MutableStateFlow<List<Message>>(emptyList())
    val chatMessages = _chatMessages.asStateFlow()
    val currentUserUid = auth.currentUser?.uid


//    init {
//        viewModelScope.launch {
//            getMessages()
//        }
//    }


    // function to create a message
    fun sendMessage(receiverID: String, message: String) {
        val currentUserUid = auth.currentUser?.uid ?: return
        val currentUserEmail = auth.currentUser?.email ?: return
        val timestamp = Timestamp.now()

        // construct the message
        val newMessage = Message(
            senderEmail = currentUserEmail,
            senderID = currentUserUid,
            message = message,
            timestamp = timestamp,
            receiverID = receiverID
        )

        // construct a unique chatroom Id
        val ids = mutableListOf<String>(receiverID, currentUserUid)
        ids.sort()
        val chatRoomID = ids.joinToString("_")

        // creating the firestore collection
        viewModelScope.launch {
            firestore.collection("chat_rooms").document(chatRoomID).collection("messages")
                .add(newMessage)
        }
    }


    // get the messages from firebase
    fun getMessages(receiverUserID: String) {
        val currentUserUid = auth.currentUser?.uid ?: return

        // construct a unique chatroom Id
        val ids = mutableListOf<String>(receiverUserID, currentUserUid)
        ids.sort()
        val chatRoomID = ids.joinToString("_")

        // Query the firestore database
        viewModelScope.launch {
            firestore.collection("chat_rooms").document(chatRoomID).collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshots, error ->
                    if (snapshots != null && !snapshots.isEmpty) {
                        val messages = snapshots.documents.map { document ->
                            document.toObject(Message::class.java) ?: Message()
                        }
                        _chatMessages.value = messages
                    }
                }
        }
    }

}