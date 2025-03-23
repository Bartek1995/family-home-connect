package com.familyhomeconnect

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.familyhomeconnect.auth.GoogleAuthUiClient
import com.familyhomeconnect.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    val authClient: GoogleAuthUiClient
        get() = googleAuthUiClient
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Nasłuchiwacz zmian stanu autoryzacji, który wywołuje updateUserDrawerHeader
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            updateUserDrawerHeader()
        }

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(binding.appBarMain.fab).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        googleAuthUiClient = GoogleAuthUiClient(
            context = this,
            credentialManager = CredentialManager.create(this),
            webClientId = getString(R.string.default_web_client_id)
        )

        if (!googleAuthUiClient.isSignedIn()) {
            googleAuthUiClient.signIn(
                onSuccess = { email ->
                    Snackbar.make(binding.root, "Zalogowano jako: $email", Snackbar.LENGTH_LONG).show()
                    invalidateOptionsMenu()
                    updateUserDrawerHeader()
                },
                onError = { error ->
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                }
            )
        } else {
            Snackbar.make(
                binding.root,
                "Już zalogowano jako: ${googleAuthUiClient.getSignedInUserEmail()}",
                Snackbar.LENGTH_LONG
            ).show()
            updateUserDrawerHeader()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // obsługa kliknięcia ustawień
                true
            }
            R.id.action_login -> {
                googleAuthUiClient.signIn(
                    onSuccess = { email ->
                        Snackbar.make(binding.root, "Zalogowano jako: $email", Snackbar.LENGTH_LONG).show()
                        invalidateOptionsMenu()
                        updateUserDrawerHeader()
                    },
                    onError = { error ->
                        Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                    }
                )
                true
            }
            R.id.action_logout -> {
                googleAuthUiClient.signOut()
                Snackbar.make(binding.root, "Wylogowano", Snackbar.LENGTH_LONG).show()
                invalidateOptionsMenu()
                updateUserDrawerHeader()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val isLoggedIn = googleAuthUiClient.isSignedIn()
        menu.findItem(R.id.action_login)?.isVisible = !isLoggedIn
        menu.findItem(R.id.action_logout)?.isVisible = isLoggedIn
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateUserDrawerHeader() {
        val navView: NavigationView = binding.navView
        val headerView = navView.getHeaderView(0)
        val emailTextView = headerView.findViewById<TextView>(R.id.textViewEmail)
        val fullNameTextView = headerView.findViewById<TextView>(R.id.textViewFullName)
        val imageView = headerView.findViewById<ImageView>(R.id.imageView)

        val currentUser = googleAuthUiClient.getCurrentUser()
        emailTextView.text = currentUser?.email ?: "Nie zalogowano"
        fullNameTextView.text = currentUser?.displayName ?: ""

        val photoUrl = currentUser?.photoUrl
        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .into(imageView)
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}
