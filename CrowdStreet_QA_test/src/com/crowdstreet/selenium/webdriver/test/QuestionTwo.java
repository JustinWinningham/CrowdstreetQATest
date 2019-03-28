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
 * This test case automates the registration process at "test.crowdstreet.com" through 4 screens.
 * This file also contains the "register" function to be utilized by other tests.
 * The test is currently incomplete due to a bug in the test website which is blocking completion after the registration step.
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
			driver.get("https://test.crowdstreet.com/");
			
			} catch (Exception e ) {
				e.printStackTrace();
			}
	}
	
	public void clickJoin() {
		driver.findElement(By.partialLinkText("JOIN")).click();
	}
	
	public void clickSignUp( ) {
		driver.findElement(By.linkText("SIGN UP")).click();
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
		driver.findElement(By.className("checkbox_id")).click();
	}
	
	
	public static void main(String[] args) {

		// We will use these random numbers to allow re-usability on tests that may not allow duplicate data (such as emails)
		Random rand = new Random();
		int rando = rand.nextInt(1000);
		String sRando = Integer.toString(rando);
		String testEmail = "test" + sRando + "@gmail.com";
		
		QuestionTwo myObj = new QuestionTwo();
		myObj.invokeBrowser();
		myObj.clickJoin();
		myObj.register(testEmail , "testFirstName", "testLastName", "Corndog1#");
		myObj.clickSignUp();
		
		// At this point the test fails - as soon as the test clicks "SIGN UP" on the screen, we get an error.
		// I am unable proceed with automation until this is fixed or a temporary workaround is found / created.
		
	}

}
