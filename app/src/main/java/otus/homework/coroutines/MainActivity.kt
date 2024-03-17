package otus.homework.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()

    private lateinit var catsViewModel: CatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        catsViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[CatsViewModel::class.java]
        catsViewModel.catsService = diContainer.service
        view.viewModel = catsViewModel

        catsViewModel.catsLiveData.observe(this) { cat ->
            view.populate(cat)
        }

        catsViewModel.fetchCat()
    }
}