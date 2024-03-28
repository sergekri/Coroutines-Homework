package otus.homework.coroutines

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import com.squareup.picasso.Picasso
import otus.homework.coroutines.CatsViewModel.Result

class CatsView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ICatsView {

    private val viewModel by lazy {
        findViewTreeViewModelStoreOwner()?.let { ViewModelProvider(it).get<CatsViewModel>() }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        findViewById<Button>(R.id.button).setOnClickListener {
            viewModel?.fetchCat()
        }
    }

    override fun populate(result: Result) {
        when (result) {
            is Result.Success<*> -> {
                val data = result.data as CatContainer
                findViewById<TextView>(R.id.fact_textView).text = result.data.fact
                Picasso
                    .get()
                    .load(data.imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(findViewById<ImageView>(R.id.cat_imageView))
            }

            is Result.Error -> {
                Toast.makeText(context, R.string.exception_socket_timeout, Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun showToast(messageResId: Int) {
        Toast.makeText(context, messageResId, Toast.LENGTH_LONG).show()
    }

    override fun showToast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

interface ICatsView {

    fun populate(result: Result)

    fun showToast(messageResId: Int)

    fun showToast(message: String?)
}