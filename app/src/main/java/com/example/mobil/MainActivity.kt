package com.example.mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobil.databinding.ActivityMainBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding : ActivityMainBinding
    private lateinit var firebaseAuth : FirebaseAuth
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()

        mainBinding.switchSignInButtonSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        mainBinding.signUpButton.setOnClickListener {
            val email = mainBinding.email.text.toString()
            val pass = mainBinding.password.text.toString()
            val confirmPass = mainBinding.repeatPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val user = hashMapOf(
                                "userID" to firebaseAuth.currentUser?.uid,
                                "email" to email
                            )
                            database.collection("Users").add(user)

                            val intent = Intent(this, FlashcardContainer::class.java)
                            startActivity(intent)
                        }
                        else {
                            Toast.makeText(this, "Email is not correct or already exists", Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords are not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()

            }
        }
    }
}


