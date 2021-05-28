package com.app.googleloginlogout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
    }
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    val firebaseAuth= FirebaseAuth.getInstance()

    val gso =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_S
                IGN_IN)

            .requestIdToken(getString(R.string.default_web_client_id)
            )
            .requestEmail()
            .build()
    mGoogleSignInClient=GoogleSignIn.getClient(this,gso)
    firebaseAuth= FirebaseAuth.getInstance()

    private fun signInGoogle(){
        val
                signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode,
            data)
        if(requestCode==Req_Code){
            val task:Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask:
                             Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount?
                    =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){

            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show
            ()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential=
            GoogleAuthProvider.getCredential(account.idToken,null)

        firebaseAuth.signInWithCredential(credential).addOnComple
        teListener {task->
            if(task.isSuccessful) {

                SavedPreference.setEmail(this,account.email.toString())
                SavedPreference.setUsername(this,account.displayName.toSt
                        ring())
                val intent = Intent(this,
                    MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this,
                MainActivity::class.java))
            finish()
        }
    }

    object SavedPreference {
        const val EMAIL= "email"
        const val USERNAME="username"
        private fun getSharedPreference(ctx: Context?):
                SharedPreferences? {return
            PreferenceManager.getDefaultSharedPreferences(ctx)
        }
        private fun editor(context: Context, const:String,
                           string: String){
            getSharedPreference(
                context
            )?.edit()?.putString(const,string)?.apply()
        }
        fun getEmail(context: Context)= getSharedPreference(
            context
        )?.getString(EMAIL,"")
        fun setEmail(context: Context, email: String){
            editor(
                context,
                EMAIL,
                email
            )
        }
        fun setUsername(context: Context, username:String){
            editor(
                context,
                USERNAME,
                username
            )
        }
        fun getUsername(context: Context) =
            getSharedPreference(
                context
            )?.getString(USERNAME,"")
    }

    Signin.setOnClickListener{ view: View? -> signInGoogle()
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this,
                MainActivity::class.java))
            finish()
        }
    }
}