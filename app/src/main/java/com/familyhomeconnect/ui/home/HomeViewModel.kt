import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _isLoggedIn.value = firebaseAuth.currentUser != null
    }

    init {
        auth.addAuthStateListener(authStateListener)
    }

    override fun onCleared() {
        auth.removeAuthStateListener(authStateListener)
        super.onCleared()
    }
}
