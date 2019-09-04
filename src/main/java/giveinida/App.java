package giveinida;
import java.awt.*;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
public class App
{
    public static void main( String[] args ) throws IOException,AWTException
    {
        String currentUsersHomeDir = System.getProperty("user.dir");
        String homePage="https://en.wikipedia.org/wiki/Selenium";
        System.setProperty("webdriver.chrome.driver",currentUsersHomeDir+ "\\Drivers\\chromedriver.exe");
        WebDriver dr=new ChromeDriver();
        dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        dr.manage().window().fullscreen();
        dr.get(homePage);
        String url = "";
        WebElement ExternalLinks=dr.findElement(By.xpath("//h2[*[contains(text(),'External links')]]"));
        HttpURLConnection huc = null;
        int respCode = 200;
        if(ExternalLinks.isDisplayed()){
            List<WebElement> ExternalLinksCheck=dr.findElements(By.xpath("//h2[span[@id='External_links']]/following-sibling::ul/li/a"));
                Iterator<WebElement> it = ExternalLinksCheck.iterator();
                while(it.hasNext()) {
                    url = it.next().getAttribute("href");
                    System.out.println(url);
                    if (url == null || url.isEmpty()) {
                        System.out.println("URL is either not configured for anchor tag or it is empty");
                        continue;
                    } else {
                        try {
                            huc = (HttpURLConnection) (new URL(url).openConnection());
                            huc.setRequestMethod("HEAD");
                            huc.connect();
                            respCode = huc.getResponseCode();
                            if (respCode >= 400) {
                                System.out.println(url + " is a broken link");
                            } else {
                                System.out.println(url + " is a valid link");
                            }

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        WebElement o2Element = dr.findElement(By.xpath("//*[@title='O, Oxygen']/a"));
        o2Element.click();
        WebElement o2FeathuredPage=dr.findElement(By.xpath("//h1[contains(text(),'Oxygen')]"));
        if(o2FeathuredPage.isDisplayed()){
            System.out.println("its a featured article");
        }else{
            System.out.println("its  not a featured article");
        }
        ExternalLinks=dr.findElement(By.xpath("//h2[*[contains(text(),'External links')]]"));
        File scrShot = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrShot, new File(currentUsersHomeDir + File.separator +"Screenshots//WebElementScreenShot.png"));
        WebElement elementProperties =dr.findElement(By.xpath("//table[caption/span[contains(text(),'Oxygen')]]"));
        List<WebElement> noOfReferenceLinks=dr.findElements(By.xpath("//h2[span[text()='References']]/following-sibling::div/ol/li"));
        System.out.println("Number of Reference Links are"+noOfReferenceLinks.size());

        WebElement SerachField=dr.findElement(By.xpath("//input[@name='search']"));
        SerachField.sendKeys("pluto");

        List<WebElement> SuggestionResults=dr.findElements(By.xpath("//div[@class='suggestions-results']/a"));
        if(SuggestionResults.get(2).getAttribute("title").equalsIgnoreCase("Plutonium")){
            System.out.println("Plutonium is 2nd suggestion");
        }else{
        System.out.println("Plutonium is  not 2nd suggestion");
    }
    dr.quit();
    }
}
