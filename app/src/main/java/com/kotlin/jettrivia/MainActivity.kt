package com.kotlin.jettrivia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kotlin.jettrivia.screens.MainScreenViewModel
import com.kotlin.jettrivia.screens.MyTrivia
import com.kotlin.jettrivia.ui.theme.JetTriviaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                val jetViewModel:MainScreenViewModel by viewModels()
                MyTrivia(viewModel = jetViewModel)
            }

        }
    }
}
@Composable
fun MyApp(context:@Composable ()->Unit){
    JetTriviaTheme {
       context()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {

    }
}