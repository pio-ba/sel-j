package com.selj.tests;

import com.selj.pages.LoginPage;
import com.selj.pages.FavoritesPage;
import com.selj.pages.AccountMenuPage;
import com.selj.pages.ProductsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductsTest extends BaseTest {

    private static final String LOW_TO_HIGH = "Low to High (Price)";
    private static final String HIGH_TO_LOW = "High to Low (Price)";

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Product can be added and removed from favorites")
    @Epic("E-commerce")
    @Feature("Favorites")
    @Story("Add and remove favorite")
    @Severity(SeverityLevel.CRITICAL)
    public void addAndRemoveFavoriteTest(String username, String password) {
        FavoritesPage favorites = openFavorites(username, password);
        String productName = favorites.getFavoriteProductName(0);

        favorites.toggleFavorite(0);

        assertThat(favorites.isFavorite(0))
                .as("%s should be marked as favorite", productName)
                .isTrue();
        assertThat(favorites.getFavoriteProductNames())
                .as("Favorite product should be present in favorites state")
                .contains(productName);

        favorites.toggleFavorite(0);

        assertThat(favorites.isFavorite(0))
                .as("%s should be removed from favorites", productName)
                .isFalse();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Favorite remains selected after page reload")
    @Epic("E-commerce")
    @Feature("Favorites")
    @Story("Favorite persistence after reload")
    @Severity(SeverityLevel.NORMAL)
    public void favoritePersistsAfterReloadTest(String username, String password) {
        FavoritesPage favorites = openFavorites(username, password);
        String productName = favorites.getFavoriteProductName(0);

        if (favorites.isFavorite(0)) {
            favorites.toggleFavorite(0);
        }
        favorites.toggleFavorite(0);
        favorites.reload();

        assertThat(favorites.isFavorite(0))
                .as("Favorite state for %s should persist after reload", productName)
                .isTrue();

        favorites.toggleFavorite(0);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Favorite remains selected after logout and login")
    @Epic("E-commerce")
    @Feature("Favorites")
    @Story("Favorite persistence after login")
    @Severity(SeverityLevel.NORMAL)
    public void favoritePersistsAfterLoginTest(String username, String password) {
        FavoritesPage favorites = openFavorites(username, password);
        String productName = favorites.getFavoriteProductName(0);

        if (favorites.isFavorite(0)) {
            favorites.toggleFavorite(0);
        }
        favorites.toggleFavorite(0);

        AccountMenuPage accountMenu = accountMenuPageProvider.get();
        LoginPage loginPage = accountMenu.logout();
        loginPage.loginAndGetDashboard(username, password);
        favorites = favoritesPageProvider.get();

        assertThat(favorites.isFavorite(0))
                .as("Favorite state for %s should persist after login", productName)
                .isTrue();

        favorites.toggleFavorite(0);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Products are sorted from lowest to highest price")
    @Epic("E-commerce")
    @Feature("Sorting")
    @Story("Sort by price")
    @Severity(SeverityLevel.CRITICAL)
    public void sortProductsLowToHighTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .sortBy(LOW_TO_HIGH);

        assertProductPricesAreSorted(products.getProductPrices(), false);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Products are sorted from highest to lowest price")
    @Epic("E-commerce")
    @Feature("Sorting")
    @Story("Sort by price")
    @Severity(SeverityLevel.CRITICAL)
    public void sortProductsHighToLowTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .sortBy(HIGH_TO_LOW);

        assertProductPricesAreSorted(products.getProductPrices(), true);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Verify availability of sorting options")
    @Epic("E-commerce")
    @Feature("Sorting")
    @Story("Supported sorting options")
    @Severity(SeverityLevel.MINOR)
    public void sortingOptionsTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        assertThat(products.getSortOptions())
                .contains(LOW_TO_HIGH, HIGH_TO_LOW);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Newest arrivals sorting is not available on the current product page")
    @Epic("E-commerce")
    @Feature("Sorting")
    @Story("Newest arrivals")
    @Severity(SeverityLevel.MINOR)
    public void newestArrivalsOptionIsNotAvailableTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        assertThat(products.getSortOptions().stream()
                .anyMatch(option -> option.toLowerCase().contains("new")))
                .as("Newest arrivals sorting should not be reported as available")
                .isFalse();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Verify category, price range and brand filters are not displayed")
    @Epic("E-commerce")
    @Feature("Filtering")
    @Story("Product filters")
    @Severity(SeverityLevel.MINOR)
    public void productFiltersAreNotAvailableTest(String username, String password) {
        ProductsPage products = openProducts(username, password);
        String visibleText = products.getVisibleText().toLowerCase();

        assertThat(visibleText)
                .doesNotContain("category")
                .doesNotContain("price range")
                .doesNotContain("brand");
    }

    private ProductsPage openProducts(String username, String password) {
        LoginPage loginPage = loginPage();
        ProductsPage products = loginPage.loginAndGetDashboard(username, password);

        assertThat(products.getPageTitle())
                .as("Products page should open after login")
                .contains("Products");

        return products;
    }

    private FavoritesPage openFavorites(String username, String password) {
        openProducts(username, password);
        return favoritesPageProvider.get();
    }

    private void assertProductPricesAreSorted(List<BigDecimal> prices, boolean descending) {
        assertThat(prices)
                .as("At least two products are required to verify sorting")
                .hasSizeGreaterThanOrEqualTo(2);

        for (int i = 1; i < prices.size(); i++) {
            if (descending) {
                assertThat(prices.get(i - 1))
                        .as("Price at index %d should be greater than or equal to price at index %d", i - 1, i)
                        .isGreaterThanOrEqualTo(prices.get(i));
            } else {
                assertThat(prices.get(i - 1))
                        .as("Price at index %d should be less than or equal to price at index %d", i - 1, i)
                        .isLessThanOrEqualTo(prices.get(i));
            }
        }
    }
}
