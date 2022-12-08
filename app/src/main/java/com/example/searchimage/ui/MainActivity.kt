package com.example.searchimage.ui

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.searchimage.R
import com.example.searchimage.model.dto.Item
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
                composable(MyScreen.DetailScreen.name) {
                    DetailScreen()
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
    fun DetailScreen() {
        viewModel.downloadProgressLiveData.postValue(0)
        val detailItem = viewModel.detailItemLiveData.observeAsState()
        Column {
            GlideImage(
                imageModel = detailItem.value?.thumbnail,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
            )
            Column {
                detailItem.value?.title?.let { Text(it) }
                Text("${detailItem.value?.sizeWidth} X ${detailItem.value?.sizeHeight}")
            }
            Button(onClick = {
                detailItem.value?.let { viewModel.downloadImage(it) }
            }) {
                Text(text = "download")
            }
            DownloadProgress(detailItem.value?.link)
        }

    }

    @Composable
    fun DownloadProgress(imgUrl: String?) {
        val downloadProgress = viewModel.downloadProgressLiveData.observeAsState()
        if (downloadProgress.value != null && imgUrl == viewModel.detailItemLiveData.value?.link) {
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = downloadProgress.value!!.toFloat(),
                    backgroundColor = Color.LightGray,
                    color = Color.Blue
                )
            }
        }
    }

    @Composable
    fun MainContent(navController: NavController) {
        viewModel.initBookmarkList()
        val searchText = viewModel.searchText.observeAsState()
        Column {
            SearchBar()
            if (searchText.value.isNullOrEmpty()) {
                BookmarkList(navController)
            } else {
                ImageList(navController)
            }
        }
    }

    @Preview
    @Composable
    fun SearchBar() {
        val inputValue = remember { viewModel.inputText }

        Row {
            TextField(
                value = inputValue.value,
                onValueChange = { inputValue.value = it },
                maxLines = 1,
                shape = CircleShape,
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(0.8f)
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
            SearchIconView()
        }
    }

    @Composable
    fun TrailingIconView() {
        IconButton(onClick = {
            viewModel.inputText.value = TextFieldValue("")
            viewModel.searchText.value = viewModel.inputText.value.text
        }) {
            Icon(
                imageVector = Icons.Filled.Clear,
                contentDescription = ""
            )
        }
    }

    @Composable
    fun SearchIconView() {
        IconButton(onClick = {
            viewModel.searchText.value = viewModel.inputText.value.text
        }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = ""
            )
        }
    }

    @Composable
    fun BookmarkList(navController: NavController) {
        val bookmarkLinkList = viewModel.bookmarkListLiveData.observeAsState().value ?: emptyList()
        Log.d("searchTest", "bookmarkList: $bookmarkLinkList")
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(bookmarkLinkList.size) { idx ->
                    ImageItem(navController, bookmarkLinkList[idx].convertToItem())
                }
            })
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
                .width(200.dp)
                .height(200.dp)
                .clickable {
                    viewModel.detailItemLiveData.postValue(item)
                    navController.navigate(MyScreen.DetailScreen.name)
                }
        )
        Box(contentAlignment = Alignment.TopEnd) {
            Icon(
                painter = painterResource(
                    id = if (viewModel.bookmarkListLiveData.value?.map { it.link }?.contains(item.link) == true) {
                        R.drawable.ic_baseline_star_24
                    } else {
                        R.drawable.ic_baseline_star_border_24
                    }
                ),
                contentDescription = "bookmark",
                tint = Color.Yellow,
                modifier = Modifier.clickable {
                    Log.d("searchTest", "bookmarkList: ${viewModel.bookmarkListLiveData.value}")
                    if (viewModel.bookmarkListLiveData.value?.map { it.link }?.contains(item.link) == true) {
                        viewModel.deleteBookmark(item)
                    } else {
                        viewModel.addBookmark(item)
                    }
                },
            )
        }
    }
}
