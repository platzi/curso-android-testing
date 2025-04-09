package com.juandgaines.testground.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun navigation_fromProfileToDetail_worksCorrectly() {
        // Arrange
        val testPlace = Place(
            id = "1",
            name = "Test Place",
            coordinates = Coordinates(1.0, 1.0)
        )
        val testProfile = Profile(
            user = User("test-user", "Test User"),
            places = listOf(testPlace)
        )

        // Act
        composeRule.setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "profile"
                ) {
                    composable("profile") {
                        ProfileScreen(
                            state = ProfileState(profile = testProfile),
                            onPlaceClick = { place ->
                                navController.navigate("detail/${place.id}")
                            }
                        )
                    }
                    composable("detail/{placeId}") { backStackEntry ->
                        val placeId = backStackEntry.arguments?.getString("placeId")
                        val place = testProfile.places.find { it.id == placeId }
                        if (place != null) {
                            DetailPlaceScreen(
                                place = place,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }

        // Assert - Verify initial screen
        composeRule.onNodeWithText("Welcome Test User!").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Place card: Test Place").assertIsDisplayed()

        // Act - Navigate to detail
        composeRule.onNodeWithContentDescription("Place card: Test Place").performClick()
        composeRule.waitForIdle()

        // Assert - Verify detail screen
        composeRule.onNodeWithText("Place Details").assertIsDisplayed()
        composeRule.onNodeWithText("Test Place").assertIsDisplayed()
        composeRule.onNodeWithText("Latitude").assertIsDisplayed()

        // Act - Navigate back
        composeRule.onNodeWithContentDescription("Navigate back").performClick()
        composeRule.waitForIdle()
        // Assert - Verify back on profile screen
        composeRule.onNodeWithText("Welcome Test User!").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Place card: Test Place").assertIsDisplayed()
    }
}



// -------- INTERFAZ BASE PARA LOS TEST DOUBLES -------

interface ShoppingCartCache {
    fun saveCart(items: List<Producto>)
    fun loadCart(): List<Producto>
    fun clearCart()
}

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double
)

// -------- 1. 🫥 DUMMY --------
class ShoppingCartCacheDummy : ShoppingCartCache {
    override fun saveCart(items: List<Producto>) = Unit
    override fun loadCart(): List<Producto> = emptyList()
    override fun clearCart() = Unit
}

// Uso típico: se pasa como parámetro, pero no se usa. Solo cumple con la firma.

// -------- 2. 📦 STUB --------
class ShoppingCartCacheStub : ShoppingCartCache {
    override fun saveCart(items: List<Producto>) = Unit
    override fun loadCart(): List<Producto> = listOf(
        Producto(1, "Helado", 5.0),
        Producto(2, "Manzana", 2.0)
    )
    override fun clearCart() = Unit
}

// Uso típico: quieres que el método devuelva datos fijos y predecibles.

// -------- 3. 🧱 FAKE --------
class ShoppingCartCacheFake : ShoppingCartCache {
    private val items = mutableListOf<Producto>()

    override fun saveCart(items: List<Producto>) {
        this.items.clear()
        this.items.addAll(items)
    }

    override fun loadCart(): List<Producto> = items.toList()

    override fun clearCart() {
        items.clear()
    }
}

// Uso típico: tests realistas con lógica funcional pero sin acceso a red o BD.

// -------- 4. 🪞 SPY --------
// Requiere biblioteca: mockk o Mockito
// Dependencia gradle (mockk): testImplementation "io.mockk:mockk:1.13.7"

fun ejemploSpy() {

    val shoppingCartSpy = spyk(ShoppingCartCacheFake())

    shoppingCartSpy.loadCart() // Acción a verificar

    verify { shoppingCartSpy.loadCart() } // Verifica que se llamó
}

// -------- 5. 🎭 MOCK --------
fun ejemploMock() {

    val shoppingCartMock = mockk<ShoppingCartCache>()

    io.mockk.every { shoppingCartMock.loadCart() } returns listOf(
        Producto(1, "Helado", 5.0)
    )

    val carrito = shoppingCartMock.loadCart()

    verify { shoppingCartMock.loadCart() }
}

/*
 * ✅ CONCLUSIÓN:
 * - Fakes son los más recomendados para tests unitarios.
 * - Mocks y Spies permiten verificar interacciones, pero agregan complejidad.
 * - Los Dummy y Stub son útiles para cubrir casos básicos o dependencias simples.
 *
 * 🧠 Siempre elige el tipo de Test Double según el objetivo del test y el nivel de la pirámide.
 */
