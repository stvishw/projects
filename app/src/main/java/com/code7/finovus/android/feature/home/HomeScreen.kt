package com.code7.finovus.android.feature.home
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.code7.finovus.android.data.model.ExpenseEntity
import com.code7.finovus.android.ui.theme.Zinc
import com.code7.finovus.android.widget.ExpenseTextView
import com.code7.finovus.android.UserViewModel
import com.code7.finovus.android.ui.theme.Green
import com.code7.finovus.android.ui.theme.LightGrey
import com.code7.finovus.android.ui.theme.Red
import com.code7.finovus.android.ui.theme.Typography
import com.code7.finovus.android.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.code7.finovus.android.R
import com.code7.finovus.android.base.HomeNavigationEvent
import com.code7.finovus.android.base.NavigationEvent
import java.util.Calendar

@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userName = currentUser?.displayName ?: "Guest" // Fetch current user's name
    val snackbarHostState = remember { SnackbarHostState() }
    val userViewModel: UserViewModel = viewModel() // Initialize UserViewModel here



// greeting logic start here
    // Greeting based on the time of day
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
//        in 22..12 -> "Good Morning"
//        in 12..17 -> "Good Afternoon"
//        else -> "Good Evening"
        in 0..5 -> "Good Night"         // Late-night hours (12:00 AM to 5:59 AM)
        in 6..11 -> "Good Morning"      // Morning hours (6:00 AM to 11:59 AM)
        in 12..16 -> "Good Afternoon"   // Afternoon hours (12:00 PM to 4:59 PM)
        in 17..20 -> "Good Evening"     // Evening hours (5:00 PM to 8:59 PM)
        else -> "Good Night"            // Night hours (9:00 PM to 11:59 PM)
    }

// ends here

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                NavigationEvent.NavigateBack -> navController.popBackStack()
                HomeNavigationEvent.NavigateToSeeAll -> {
                    navController.navigate("/all_transactions")
                }

                HomeNavigationEvent.NavigateToAddIncome -> {
                    navController.navigate("/add_income")
                }

                HomeNavigationEvent.NavigateToAddExpense -> {
                    navController.navigate("/add_exp")
                }

                else -> {}
            }
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar, add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = null,
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    ExpenseTextView(
                        //text = "Howdy!!",
                        text = "$greeting",
                        style = Typography.titleLarge,

                        //style = MaterialTheme.typography.headlineSmall,
                        // style = Typography.bodyMedium,
                        //color = Color.White
                        color = Color.Black
                    )
                    ExpenseTextView(
                        text = userName, // Display the user's nam
                        style = Typography.titleLarge,
                        color = Color.Black
                    )

                }

//                Image(
//                    painter = painterResource(id = R.drawable.ic_notification),
//                    contentDescription = null,
//                    modifier = Modifier.align(Alignment.CenterEnd)
//                )
                // Add Dots Menu with Logout
                //DotsMenu(navController = navController,userViewModel = userViewModel, modifier = Modifier.align(Alignment.CenterEnd))
                // Add Dots Menu with Logout
                DotsMenu(
                    navController = navController,
                    userViewModel = userViewModel, // Pass the ViewModel
                    modifier = Modifier.align(Alignment.CenterEnd)
                )



            }

            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)
            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance = balance, income = income, expense = expense
            )
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list) {
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }, list = state.value, onSeeAllClicked = {
                    viewModel.onEvent(HomeUiEvent.OnSeeAllClicked)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(add) {
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }, contentAlignment = Alignment.BottomEnd
            ) {
                MultiFloatingActionButton(modifier = Modifier, {
                    viewModel.onEvent(HomeUiEvent.OnAddExpenseClicked)
                }, {
                    viewModel.onEvent(HomeUiEvent.OnAddIncomeClicked)
                })
            }
        }
    }
}

@Composable
fun DotsMenu(
    navController: NavController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.clickable { expanded = !expanded }) {
        Image(
            painter = painterResource(id = R.drawable.ic_logout),
            contentDescription = "Menu",
            modifier = Modifier.size(24.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { ExpenseTextView("Logout", style = Typography.bodyMedium) },
                onClick = {
                    expanded = false
                    userViewModel.logout {
                        navController.navigate("/login") {
                            popUpTo(0) // Clear navigation stack
                        }
                    }
                }
            )
        }
    }
}




@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    onAddExpenseClicked: () -> Unit,
    onAddIncomeClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Secondary FABs
            AnimatedVisibility(visible = expanded) {
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = Zinc, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddIncomeClicked.invoke()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_positive),
                            contentDescription = "Add Income",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(color = Zinc, shape = RoundedCornerShape(12.dp))
                            .clickable {
                                onAddExpenseClicked.invoke()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_negative),
                            contentDescription = "Add Expense",
                            tint = Color.Black
                        )
                    }
                }
            }
            // Main FAB
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    // .background(color = Blue)
                    .clickable {
                        expanded = !expanded
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_action),
                    contentDescription = "small floating action button",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

@Composable
fun CardItem(
    modifier: Modifier,
    balance: String, income: String, expense: String
) {
    // State to control the visibility of the balance
    var isBalanceVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            // .background(color = Blue)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                ExpenseTextView(
                    text = "Total Balance",
                    style = Typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))

                // Display balance or 'xxxx' based on visibility state
                ExpenseTextView(
                    text = if (isBalanceVisible) balance else "XXXX.XX INR",
                    style = Typography.headlineLarge,
                    color = Color.Black,
                )
            }

            // Eye icon for toggling balance visibility
            Image(
                painter = painterResource(id = R.drawable.ic_eye), // Use your eye icon here
                contentDescription = "Toggle Balance Visibility",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable {
                        // Toggle the visibility when clicked
                        isBalanceVisible = !isBalanceVisible
                    }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                title = "Budget",
                amount = income,
                imaget = R.drawable.ic_positive
            )
            Spacer(modifier = Modifier.size(8.dp))
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                title = "Money Spent",
                amount = expense,
                imaget = R.drawable.ic_negative
            )
        }

    }
}


// changes ends here

@Composable
fun TransactionList(
    modifier: Modifier,
    list: List<ExpenseEntity>,
    title: String = "Recent Transactions",
    onSeeAllClicked: () -> Unit
) {
    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        item {
            Column {
                Box(modifier = modifier.fillMaxWidth()) {
                    ExpenseTextView(
                        text = title,
                        style = Typography.titleLarge,
                    )
                    if (title == "Recent Transactions") {
                        ExpenseTextView(
                            text = "See all",
                            style = Typography.bodyMedium,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickable {
                                    onSeeAllClicked.invoke()
                                }
                        )
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
        items(items = list,
            key = { item -> item.id ?: 0 }) { item ->
            val icon = Utils.getItemIcon(item)
            val amount = if (item.type == "Income") item.amount else item.amount * -1

            TransactionItem(
                title = item.title,
                amount = Utils.formatCurrency(amount),
                icon = icon,
                date = Utils.formatStringDateToMonthDayYear(item.date),
                color = if (item.type == "Income") Green else Red,
                Modifier
            )
        }
    }
}

@Composable
fun TransactionItem(
    title: String,
    amount: String,
    icon: Int,
    date: String,
    color: Color,
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(51.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                ExpenseTextView(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(6.dp))
                ExpenseTextView(text = date, fontSize = 13.sp, color = LightGrey)
            }
        }
        ExpenseTextView(
            text = amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterEnd),
            color = color
        )
    }
}

@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, imaget: Int) {
    Column(modifier = modifier) {
        Row {

            Image(
                painter = painterResource(id = imaget),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            ExpenseTextView(text = title, style = Typography.bodyLarge, color = Color.Black)
        }
        Spacer(modifier = Modifier.size(4.dp))
        ExpenseTextView(text = amount, style = Typography.titleLarge, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}


