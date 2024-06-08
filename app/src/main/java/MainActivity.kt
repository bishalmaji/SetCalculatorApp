package com.bishal.setcalculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bishal.setcalculatorapp.viewmodel.CalculatorViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.text.style.TextOverflow
import com.bishal.setcalculatorapp.ui.theme.SetCalculatorAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetCalculatorAppTheme {
                CalculatorApp(viewModel)
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel) {
    val isDarkTheme = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    var maxHeight by remember { mutableStateOf(0) }
    val isInputValid by remember(
        viewModel.textFieldOne,
        viewModel.textFieldTwo,
        viewModel.textFieldThree
    ) {
        mutableStateOf(
            viewModel.textFieldOne.text.any { it.isDigit() } &&
                    viewModel.textFieldTwo.text.any { it.isDigit() } &&
                    viewModel.textFieldThree.text.any { it.isDigit() }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Set Operations Calculator",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NumberInputField(
                    value = viewModel.textFieldOne,
                    onValueChange = { newValue -> viewModel.textFieldOne = newValue },
                    label = "Enter comma separated numbers",
                    hint = "SET 1"
                )
                NumberInputField(
                    value = viewModel.textFieldTwo,
                    onValueChange = { newValue -> viewModel.textFieldTwo = newValue },
                    label = "Enter comma separated numbers",
                    hint = "SET 2"

                )
                NumberInputField(
                    value = viewModel.textFieldThree,
                    onValueChange = { newValue -> viewModel.textFieldThree = newValue },
                    label = "Enter comma separated numbers",
                    hint = "SET 3"

                )

                Button(
                    onClick = { viewModel.calculateNumbers() },
                    enabled = isInputValid,
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("CALCULATE", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.Gray)

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Input Result",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            color = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ResultCard(
                maxHeight = maxHeight,
                label = "Intersection",
                result = viewModel.resultIntersection,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        maxHeight = maxOf(maxHeight, coordinates.size.height)
                    }
            )
            VerticalDivider()
            ResultCard(
                maxHeight = maxHeight,
                label = "Union",
                result = viewModel.resultUnion,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        maxHeight = maxOf(maxHeight, coordinates.size.height)
                    }
            )
            VerticalDivider()
            ResultCard(
                maxHeight = maxHeight,

                label = "Highest",
                result = viewModel.resultHighest,
                isDarkTheme = isDarkTheme,
                modifier = Modifier
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        maxHeight = maxOf(maxHeight, coordinates.size.height)
                    }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LaunchedEffect(maxHeight) {
            // Trigger recomposition when maxHeight changes
        }
    }
}

@Composable
fun ResultCard(
    maxHeight: Int,
    label: String,
    result: String,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .heightIn(min = with(LocalDensity.current) { maxHeight.toDp() }) // Apply the max height to all cards
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = result,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = if (isDarkTheme) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VerticalDivider() {
    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberInputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    hint: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(hint) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SetCalculatorAppTheme {
        CalculatorApp(CalculatorViewModel())
    }
}