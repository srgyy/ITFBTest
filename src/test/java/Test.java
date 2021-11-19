import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class Test {
    @org.junit.Test
    public void test() {
        System.setProperty("webdriver.chrome.driver", "C:\\tools\\chromedriver\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        WebElement element;
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://yandex.ru");

//      1. Перейти на https://market.yandex.ru

        driver.findElement(By.xpath("//div[text()='Маркет']")).click();

//      2. Нажать на «Каталог»

        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.findElement(By.xpath("//div[@data-tid='5a689c45']//button")).click();

//      3. Навести курсор на «Зоотовары, в блоке «Для кошек» нажать на «Лакомства»

        Actions actions = new Actions(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Зоотовары']")));
        actions.moveToElement(driver.findElement(By.xpath("//span[text()='Зоотовары']"))).perform();
        driver.findElement(By.xpath("//a[text()='Для кошек']/../..//a[text()='Лакомства']")).click();

//      4. Установить фильтры:
        wait.until(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("#glpricefrom"))
        ).sendKeys("50");
        driver.findElement(By.cssSelector("#glpriceto")).sendKeys("150");
        driver.findElement(By.xpath("//span[text()='Доставка курьером']")).click();
        driver.findElement(By.xpath("//label[@for='7893318_12686323']")).click();

//      5. Перейти в первый товар в списке, нажать на кнопку «Сравнить»

        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath("//button[@class='tzQlI _1e9zv']"))));
        element = driver.findElement(By.xpath("//div[@data-zone-name='snippetList']" +
                "//article[@data-autotest-id='product-snippet'][1]//a[@class='_2f75n _24Q6d cia-cs']"));
        String productName1 = element.getAttribute("title");
        element.click();

        tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(2));
        driver.findElement(By.cssSelector("._1FODI")).click();


//6.    Вернуться на предыдущую страницу

        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "4");
        driver.switchTo().window(tabs.get(1));

//7.     В фильтре снять галочку с производителя «» и установить производителя «Мнямс»

        driver.findElement(By.xpath("//label[@for='7893318_12686323']")).click();
        driver.findElement(By.xpath("//label[@for='7893318_10739158']")).click();

//      8. Перейти во второй товар в списке, нажать на кнопку «Сравнить»

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-zone-name='snippetList']" +
                "//article[@data-autotest-id='product-snippet'][2]//*[contains(text(),'Мнямс')]")));
        element = driver.findElement(By.xpath("//div[@data-zone-name='snippetList']" +
                "//article[@data-autotest-id='product-snippet'][2]//a[@class='_2f75n _24Q6d cia-cs']"));
        String productName2 = element.getAttribute("title");
        element.click();

        tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(3));
        driver.findElement(By.cssSelector("._1FODI")).click();


//      9. Перейти в «Сравнение», проверить, что имена товаров в сравнении соответствуют именам товаров, добавленных ...

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Сравнить']"))).click();

        Assert.assertEquals(productName1,
                driver.findElement(By.xpath("(//a[@class='_2f75n PzFNv cia-cs'])[1]")).getText());
        Assert.assertEquals(productName2,
                driver.findElement(By.xpath("(//a[@class='_2f75n PzFNv cia-cs'])[2]")).getText());

        String str1 = driver.findElement(By.xpath("(//a[@class='_2f75n PzFNv cia-cs'])[1]")).getText();
        String str2 = driver.findElement(By.xpath("(//a[@class='_2f75n PzFNv cia-cs'])[2]")).getText();

//      10. Проверить, что сумма стоимостей товаров не превышает 300 руб

        int priceProduct1 = Integer.parseInt(driver.findElement(
                By.xpath("(//div[@class='_3NaXx _1ri69']//span[@data-autotest-currency='₽'])[1]"))
                .getAttribute("data-autotest-value"));
        int priceProduct2 = Integer.parseInt(driver.findElement(
                By.xpath("(//div[@class='_3NaXx _1ri69']//span[@data-autotest-currency='₽'])[2]"))
                .getAttribute("data-autotest-value"));

        Assert.assertTrue(priceProduct1 + priceProduct2 <= 300);

//      11. Удалить товар производителя «Dreamies» из сравнения и проверить, что товар производителя «Dreamies» не ...

        actions.moveToElement(driver.findElement(By.xpath("(//img[@data-tid='26287868'])[1]"))).perform();
        driver.findElement(By.xpath("(//div[@class='_2bqiO'])[1]")).click();

        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.xpath("//a[contains(text(),'Dreamies')]"))));

        String singleProduct = driver.findElement(By.xpath("//a[@class='_2f75n PzFNv cia-cs']")).getText();
        Assert.assertNotEquals(singleProduct, productName1);


//      12. Нажать на «Удалить список» (значок мусорного бака), проверить, что товары не отображаются

        driver.findElement(By.xpath("//button[@class='_1KpjX _1KU3s']")).click();
        element = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//img[@class='ZuZUp']")));
        Assert.assertNotNull(element);

        driver.quit();
    }
}
