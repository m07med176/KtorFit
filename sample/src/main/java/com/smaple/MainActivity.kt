package com.smaple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ktorfit.Result
import com.ktorfit.asResult
import com.smaple.data.models.Product
import com.smaple.data.proxy.KtorFitBuilder
import com.smaple.data.proxy.KtorFitBuilder.create
import com.smaple.data.proxy.ServiceApiKtor
import com.smaple.ui.theme.KtorfitTheme
import kotlinx.coroutines.flow.flowOf

data class UIState<T>(
    val idle:Boolean = true,
    val loading:Boolean = false,
    val data:T?=null,
    val error:String?=null
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KtorfitTheme {

                var state by remember { mutableStateOf(UIState<Product>()) }

//                LaunchedEffect(true){
////                    val retro = RetrofitInstance.api.getAllProducts().body()?.products
////                    state = state.copy(data = retro)
//
//                    val proxy = KtorFitBuilder.createHttpClient().create(ServiceApiKtor::class.java)
//                    val data  = proxy.getSingleProduct(1)
//
//                    flowOf(data).asResult().collect{ result->
//                        state = when(result){
//                            is Result.Error -> state.copy(error = result.exception.message, idle = false, loading = false)
//                            is Result.Idle -> state.copy( idle = true, loading = false)
//                            is Result.Loading -> state.copy( idle = false, loading = true)
//                            is Result.Success -> state.copy( idle = false, loading = false, data = result.data)
//                        }
//                    }
//
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    Text(text = state.data.toString(), modifier =  Modifier.padding(20.dp))
//                    LazyColumn{
//                        items(state.data?: emptyList()){
//                        }
//                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KtorfitTheme {
        Greeting("Android")
    }
}
