package com.selj.tests;

import com.selj.pages.LoginPage;
import com.selj.pages.ProductsPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasketTest extends BaseTest {

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Basket count is empty after login")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Empty basket")
    @Severity(SeverityLevel.NORMAL)
    public void basketCountIsEmptyAfterLoginTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        assertThat(products.getBasketCount())
                .as("Empty basket should not display a product count")
                .isZero();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Basket has no selected products after login")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Empty basket")
    @Severity(SeverityLevel.NORMAL)
    public void basketHasNoProductsAfterLoginTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        assertThat(products.getBasketProductNames())
                .as("Empty basket should not contain products")
                .isEmpty();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Adding a product increments the basket count")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Add products")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductIncrementsBasketCountTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        products.addToBasket(0);

        assertThat(products.getBasketCount())
                .as("Basket count should increase after adding a product")
                .isEqualTo(1);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Adding a product marks it as selected")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Add products")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductMarksProductAsSelectedTest(String username, String password) {
        ProductsPage products = openProducts(username, password);
        String productName = products.getProductNames().get(0);

        products.addToBasket(0);

        assertThat(products.isInBasket(0))
                .as("%s should be marked as in the basket", productName)
                .isTrue();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Adding a product adds it to the basket")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Add products")
    @Severity(SeverityLevel.CRITICAL)
    public void addingProductAddsProductToBasketTest(String username, String password) {
        ProductsPage products = openProducts(username, password);
        String productName = products.getProductNames().get(0);

        products.addToBasket(0);

        assertThat(products.getBasketProductNames())
                .as("Basket should contain the selected product")
                .containsExactly(productName);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Adding multiple products updates the basket count")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Add products")
    @Severity(SeverityLevel.CRITICAL)
    public void addingMultipleProductsUpdatesBasketCountTest(String username, String password) {
        ProductsPage products = openProducts(username, password);

        products.addToBasket(0);
        products.addToBasket(1);

        assertThat(products.getBasketCount())
                .as("Basket count should include both added products")
                .isEqualTo(2);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Adding multiple products adds all selected products")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Add products")
    @Severity(SeverityLevel.CRITICAL)
    public void addingMultipleProductsAddsAllProductsTest(String username, String password) {
        ProductsPage products = openProducts(username, password);
        String firstProduct = products.getProductNames().get(0);
        String secondProduct = products.getProductNames().get(1);

        products.addToBasket(0);
        products.addToBasket(1);

        assertThat(products.getBasketProductNames())
                .as("Basket should contain both selected products")
                .containsExactlyInAnyOrder(firstProduct, secondProduct);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing a product decrements the basket count")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingProductDecrementsBasketCountTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .addToBasket(0)
                .addToBasket(1);

        products.removeFromBasket(0);

        assertThat(products.getBasketCount())
                .as("Basket count should decrease after removing a product")
                .isEqualTo(1);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing a product unmarks it as selected")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingProductUnmarksProductTest(String username, String password) {
        ProductsPage products = openProducts(username, password).addToBasket(0);

        products.removeFromBasket(0);

        assertThat(products.isInBasket(0))
                .as("Removed product should no longer be marked as in the basket")
                .isFalse();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing a product leaves other products in the basket")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingProductLeavesOtherProductsTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .addToBasket(0)
                .addToBasket(1);

        products.removeFromBasket(0);

        assertThat(products.isInBasket(1))
                .as("Other products should remain in the basket")
                .isTrue();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing a product removes only that product")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingProductRemovesOnlySelectedProductTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .addToBasket(0)
                .addToBasket(1);
        String secondProduct = products.getProductNames().get(1);

        products.removeFromBasket(0);

        assertThat(products.getBasketProductNames())
                .as("Only the remaining product should be in the basket")
                .containsExactly(secondProduct);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing the last product returns the count to zero")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingLastProductReturnsCountToZeroTest(String username, String password) {
        ProductsPage products = openProducts(username, password).addToBasket(0);

        products.removeFromBasket(0);

        assertThat(products.getBasketCount())
                .as("Basket count should return to zero when its last product is removed")
                .isZero();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Removing the last product empties the basket")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Remove products")
    @Severity(SeverityLevel.CRITICAL)
    public void removingLastProductEmptiesBasketTest(String username, String password) {
        ProductsPage products = openProducts(username, password).addToBasket(0);

        products.removeFromBasket(0);

        assertThat(products.getBasketProductNames())
                .as("Basket should be empty after removing its last product")
                .isEmpty();
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Basket count persists after reloading")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Basket persistence")
    @Severity(SeverityLevel.NORMAL)
    public void basketCountPersistsAfterReloadTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .addToBasket(0)
                .addToBasket(2);

        products.reload();

        assertThat(products.getBasketCount())
                .as("Basket count should persist after reloading the products page")
                .isEqualTo(2);
    }

    @Test(dataProvider = "productsLogin", dataProviderClass = TestData.class,
            description = "Basket contents persist after reloading")
    @Epic("E-commerce")
    @Feature("Basket")
    @Story("Basket persistence")
    @Severity(SeverityLevel.NORMAL)
    public void basketContentsPersistAfterReloadTest(String username, String password) {
        ProductsPage products = openProducts(username, password)
                .addToBasket(0)
                .addToBasket(2);
        String firstProduct = products.getProductNames().get(0);
        String thirdProduct = products.getProductNames().get(2);

        products.reload();

        assertThat(products.getBasketProductNames())
                .as("Basket contents should persist after reloading the products page")
                .containsExactlyInAnyOrder(firstProduct, thirdProduct);
    }

    private ProductsPage openProducts(String username, String password) {
        LoginPage loginPage = loginPage();
        ProductsPage products = loginPage.loginAndGetDashboard(username, password);

        assertThat(products.getPageTitle())
                .as("Products page should open after login")
                .contains("Products");

        return products;
    }
}
