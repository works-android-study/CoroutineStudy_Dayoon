package com.example.searchimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var inputValue: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue())
    private val viewModel: SearchImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Column {
                SearchBar()
                ImageList()
            }
        }
    }

    @Preview
    @Composable
    fun SearchBar() {
        inputValue = remember { mutableStateOf(TextFieldValue("")) }

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
            viewModel.getSearchImage(inputValue.value.text)
            inputValue.value = TextFieldValue("")
        }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = ""
            )
        }
    }

    @Preview
    @Composable
    fun ImageList() {
        val imageLinkList = viewModel.imageLinkList
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
            items(imageLinkList) { link ->
                if (link != null) {
                    ImageItem(link)
                }
            }
        })
    }

    @Composable
    fun ImageItem(link: String) {
        GlideImage(
            imageModel = link,
            modifier = Modifier.size(128.dp)
        )
    }
}
