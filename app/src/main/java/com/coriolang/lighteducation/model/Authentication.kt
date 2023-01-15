package com.coriolang.lighteducation.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Authentication {

    private val auth: FirebaseAuth = Firebase.auth

    private val _signedIn = MutableStateFlow(isSignedIn())
    val signedIn = _signedIn.asStateFlow()

    private val _exception = MutableStateFlow("")
    val exception = _exception.asStateFlow()

    private fun isSignedIn(): Boolean =
        auth.currentUser != null

    fun updateException(message: String) {
        _exception.update { message }
        _exception.update { "" }
    }

    suspend fun createUser(displayName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { createUser ->
                if (createUser.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")

                    val user = auth.currentUser
                    val displayNameUpdate = userProfileChangeRequest {
                        this.displayName = displayName
                    }

                    user!!.updateProfile(displayNameUpdate)
                        .addOnCompleteListener { addDisplayName ->
                            if (addDisplayName.isSuccessful) {
                                Log.d(TAG, "updateProfile:success")
                            } else {
                                Log.d(TAG, "updateProfile:failure", addDisplayName.exception)

                                // message about exception
                                _exception.update {
                                    addDisplayName.exception?.message!!
                                }
                            }
                        }

                    // update ui state
                    _signedIn.update { true }
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", createUser.exception)

                    // message about exception
                    _exception.update {
                        createUser.exception?.message!!
                    }
                }
            }
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")

                    // update ui state
                    _signedIn.update { true }
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)

                    // message about exception
                    _exception.update {
                        task.exception?.message!!
                    }
                }
            }
    }

    suspend fun signOut() {
        auth.signOut()
        _signedIn.update { false }
    }

    companion object {
        const val TAG = "Authentication"
    }
}