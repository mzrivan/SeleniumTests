package ru.netology.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppOrderTest {
    private WebDriver driver;

    @BeforeAll
    //static void setUpAll() {System.setProperty("webdriver.chrome.driver", "./driver/win/chromedriver.exe");}
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver", "./driver/linux/chromedriver.exe");
    }
    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void PositiveTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий Суворов");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement] span")).click();
        driver.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @ParameterizedTest
    @CsvSource({"Vasiliy",
                "1111111",
                "@#$%!",
            //"Василий" //Верный вариант для првоерки теста

    })
    void WrongName(String name) {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement] span")).click();
        driver.findElement(By.cssSelector("button")).click();
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id=name].input_invalid"));
        assertNotNull(elements.get(0)); //Тест пройден, если найден спан с сообщением о неверном значении поля
           }

    @ParameterizedTest
    @CsvSource({
            "+7123456789",
            "+712345678910",
            "-71234567891",
            "+712345678",
            "+7123456789112",
            "+7123456789A",
            "71234567891",
            "712345678912",
            "+7123456789Ы",
            //"+71234567891" //Верный вариант для проверки теста
    })
    void WrongPhone(String phone) {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[data-test-id=agreement] span")).click();
        driver.findElement(By.cssSelector("button")).click();
        List<WebElement> elements = driver.findElements(By.cssSelector("[data-test-id=phone].input_invalid"));
        assertNotNull(elements.get(0)); //Тест пройден, если найден спан с сообщением о неверном значении поля
    }
}

