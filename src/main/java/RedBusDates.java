import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RedBusDates {
    WebDriver driver;
    Boolean isCurrentMonth = true;
    public static By Holiday_count = By.cssSelector(".holiday_count");
    public static By Date_picker = By.cssSelector("#onwardCal");

    public static By  nextbtn = By.cssSelector("div[class^='DayNavigator__CalendarHeader'] div:nth-child(3)");
    public static By  selectedMonth = By.cssSelector("div[class^='DayNavigator__CalendarHeader'] div:nth-child(2)");
    public static By weekendDate = By.cssSelector("div[class^='DayTiles__CalendarDaysBlock'] span[class*='bwoYtA']");
    public static By today = By.cssSelector("div[class^='DayTiles__CalendarDaysBlock'] span[class*='fgdqFw']");
    public List<String> getWeekendDates(String date){
        List<String> weekends = new ArrayList<String>();
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-notifications");
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        driver.get("https://www.redbus.in/");
        driver.findElement(Date_picker).click();
        printHolidays(date);
        return getWeekends();
    }

    public List<String> getWeekends(){
        List<String> dates = new ArrayList<String>();
        List<WebElement> elements = driver.findElements(weekendDate);

        if (isTodayWeekend() && isCurrentMonth) {
            dates.add(driver.findElement(today).getText());
        }

        for (int i = 0; i < elements.size(); i++) {
            dates.add(elements.get(i).getText());
        }
        return dates;
    }

    public boolean isTodayWeekend()
    {
        LocalDate today = LocalDate.now();
        DayOfWeek day = DayOfWeek.of(today.get(ChronoField.DAY_OF_WEEK));
        return day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY;
    }
    public void printHolidays(String date){
        String Holidays = driver.findElement(selectedMonth).getText();
        System.out.println(Holidays);
        String tillDate = Holidays.substring(0,8);

            while (!date.equalsIgnoreCase(tillDate)) {
                isCurrentMonth = false;
                driver.findElement(nextbtn).click();
                Holidays = driver.findElement(selectedMonth).getText();
                System.out.println(Holidays);
                tillDate = Holidays.substring(0,8);
            }
    }

    public static void main(String[] args) throws ParseException {
        RedBusDates dates = new RedBusDates();
        List<String> weekends= dates.getWeekendDates("Mar 2024");
        System.out.println(weekends);
    }
}
