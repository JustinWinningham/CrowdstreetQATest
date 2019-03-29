package com.crowdstreet.selenium.webdriver.test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Created - 3/28/2019
 * Author: Justin Winningham
 * About:
 * This test case automates the registration process at "staging.crowdstreet.com" through 4 screens
 * XPaths are used in cases where no other selector is available
 * Test is fragile due to the re-captcha - 50% fail rate from that step alone. Usually presents itself if the re-captcha loads too slowly?
 * If this occurs, manually click the re-captcha and the test will proceed as normal
 */


public class QuestionTwo {
	
	WebDriver driver;
	
	public void invokeBrowser() {
		try {
			System.setProperty("webdriver.chrome.driver", "C:\\Selenium_maybe\\chromedriver_win32_2.37\\chromedriver.exe");
			driver = new ChromeDriver();
			
			// Remove all cookies to ensure a fresh start - ensure test is re-runable
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			// Allows page to load without Java outrunning the browser
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			// Webpage we want to load at start
			driver.get("https://staging.crowdstreet.com/");
			
			} catch (Exception e ) {
				e.printStackTrace();
			}
	}
	
	public void clickJoin() {
		driver.findElement(By.partialLinkText("JOIN")).click();
	}
	
	public void clickSignUp() {
		driver.findElement(By.className("sign-up-button")).click();
	}

	public void proceed() {
		driver.findElement(By.xpath("//*[contains(concat( \" \", @class, \" \" ), concat( \" \", \"btn-block\", \" \" ))]")).click();
	}
	
	public void skipPreferences() {
		driver.findElement(By.partialLinkText("Skip for now")).click();
	}
	
/*
 * email: String - Email you want to register with
 * fName: String - First name
 * lName: String - Last name
 * pWord: String - Password
 */
	public void register(String email, String fName, String lName, String pWord) {
		
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		List<WebElement> myList = driver.findElements(By.className("_inputElement_dgbt3_418"));
		myList.get(0).sendKeys(email);
		myList.get(1).sendKeys(fName);
		myList.get(2).sendKeys(lName);
		myList.get(3).sendKeys(pWord);
		myList.get(4).sendKeys(pWord);
		// We must scroll down to the re-captcha on a 1920x1080 16:9 screen
		jse.executeScript("document.getElementById('content-well').scrollTop += 250", "");
		driver.findElement(By.tagName("iframe")).click();
	}
	
	/*
	 * address: String - User address
	 * city: String - City
	 * postal: String - Postal code
	 * country: String - Country of residence
	 * phone: String - Primary phone number (remember this is a string, not an int)
	 * referredBy: String - Who this user was referred by, use "" if not referred by anybody
	 * accreditedInvestor: Boolean - True if the user is an accredited investor, false otherwise
	 */
	public void setupProfile(String address, String city, String postal, String country, String state, String phone, String referredBy, Boolean accreditedInvestor) {
		
		// For some reason putting this as a class variable breaks my ability to scroll down
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		
		driver.findElement(By.id("address")).sendKeys(address);
		driver.findElement(By.id("city")).sendKeys(city);
		driver.findElement(By.id("postal-code")).sendKeys(postal);
		driver.findElement(By.id("primary-phone")).sendKeys(phone);
		driver.findElement(By.id("referred-by")).sendKeys(referredBy);
		
		// Still need to specify radio button selection (no)
		
		List<WebElement> L1 = driver.findElements(By.name("entity-accreditation-options"));
		
		L1.get(1).click();
		
		// Dropdowns are not required to proceed in the registration process, so we will not test for them here.
		// We do need to agree to the TOS though.
		
		//jse.executeScript("document.getElementById('content-well').scrollTop += 250", "");
		jse.executeScript("window.scrollBy(0,1000)");
		
		List<WebElement> L2 = driver.findElements(By.xpath("//input[@type='checkbox']"));
		
		L2.get(0).click();
		L2.get(1).click();
		
		}
	
	
	public static void main(String[] args) {

		// We will use these random numbers to allow re-usability on tests that may not allow duplicate data (such as emails)
		Random rand = new Random();
		int rando = rand.nextInt(10000);
		String sRando = Integer.toString(rando);
		String testEmail = "test" + sRando + "@gmail.com";
		
		QuestionTwo myObj = new QuestionTwo();
		myObj.invokeBrowser();
		myObj.clickJoin();
		myObj.register(testEmail , "testFirstName", "testLastName", "Corndog1#");
		
		// Wait until the captcha finishes its animation and reveals the clickable element.
		while(!myObj.driver.findElement(By.className("sign-up-button")).isEnabled())
		{
		}
		myObj.clickSignUp();
		
		// At this point, we switch to the "Setup Profile" Page
		myObj.setupProfile("123 Busy Street", "Busytown", "98765", "United States", "California", "000-000-0000", "", false);
		myObj.proceed();
		myObj.skipPreferences();
	}

}
