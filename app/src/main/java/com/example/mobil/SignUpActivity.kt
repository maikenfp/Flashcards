package com.example.mobil


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobil.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivitySignupBinding
    private var firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var database : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        //Switch between log in and create user
        mainBinding.switchSignInButtonSignIn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Sign up button
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

    //Check if a user is already logged in
    override fun onStart() {
        super.onStart()

        if(firebaseAuth.currentUser != null){
            val intent = Intent(this, FlashcardContainer::class.java)
            startActivity(intent)
        }
    }
}