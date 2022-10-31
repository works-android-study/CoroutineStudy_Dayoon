package com.example.searchimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchimage.data.Item
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: SearchImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SearchImageApp()
        }
    }

    @Composable
    fun SearchImageApp(
        navController: NavController = rememberNavController()
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = MyScreen.valueOf(
            backStackEntry?.destination?.route ?: MyScreen.HomeScreen.name
        )
        Scaffold (
            topBar = {
                SearchAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController as NavHostController,
                startDestination = MyScreen.HomeScreen.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(MyScreen.HomeScreen.name) {
                    HomeScreen(navController)
                }
                composable(
                    "${MyScreen.DetailScreen.name}/{item}",
                    arguments = listOf(
                        navArgument("item") {
                            type = NavType.ParcelableType(Item::class.java)
                        }
                    )
                ) {
                    val item = backStackEntry?.arguments?.getParcelable<Item>("item")
                    if (item != null) {
                        DetailScreen(navController, item)
                    }
                }
            }
        }
    }

    @Composable
    fun SearchAppBar(
        currentScreen: MyScreen,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        TopAppBar(
            title = { Text(currentScreen.name) },
            modifier = modifier,
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun HomeScreen(navController: NavController) {
        MainContent(navController)
    }

    @Composable
    fun DetailScreen(navController: NavController, item: Item) {
        Row {
            GlideImage(
                imageModel = item.thumbnail
            )
            Column {
                item.title?.let { Text(it) }
                Text("${item.sizeWidth} X ${item.sizeHeight}")
            }
        }

    }

    @Composable
    fun MainContent(navController: NavController) {
        Column {
            SearchBar()
            ImageList(navController)
        }
    }

    @Preview
    @Composable
    fun SearchBar() {
        val inputValue = remember { viewModel.inputText }

        TextField(
            value = inputValue.value,
            onValueChange = { inputValue.value = it },
            maxLines = 1,
            shape = CircleShape,
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, CircleShape),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (inputValue.value.text.isNotBlank()) {
                    TrailingIconView()
                }
            }
        )
    }

    @Preview
    @Composable
    fun TrailingIconView() {
        IconButton(onClick = {
            viewModel.searchText.value = viewModel.inputText.value.text
            viewModel.inputText.value = TextFieldValue("")
        }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = ""
            )
        }
    }

    @Composable
    fun ImageList(navController: NavController) {
        val imageLinkList = viewModel.flow.collectAsLazyPagingItems()
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(imageLinkList.itemCount) { idx ->
                    imageLinkList[idx]?.let {
                        ImageItem(navController, it)
                    }
                }
            })
    }

    @Composable
    fun ImageItem(navController: NavController, item: Item) {
        GlideImage(
            imageModel = item.link,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("${MyScreen.DetailScreen.name}/$item")
                }
        )
    }
}
