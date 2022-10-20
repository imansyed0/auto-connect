import java.util.*;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

public class LinkedinRequestGeneral {

	WebDriver driver;
	String baseUrl;
	JavascriptExecutor js;

	@BeforeClass
	public void beforeClass() {
		driver = new ChromeDriver();
		baseUrl = "https://www.linkedin.com/";
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		js = (JavascriptExecutor) driver;
		driver.get(baseUrl);
	}

	@Test
	public void test() throws InterruptedException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your first name");
		String firstName = sc.next();
		System.out.println("Enter your username for login");
		String userName = sc.next();
		System.out.println("Enter your password for login");
		String passwordText = sc.next();
		

		WebElement username = driver.findElement(By.xpath("//input[@autocomplete='username']"));
		username.click();
		username.sendKeys(userName);

		WebElement password = driver.findElement(By.xpath("//input[@autocomplete='current-password']"));
		password.click();
		password.sendKeys(passwordText);
		password.sendKeys(Keys.ENTER);
		
		System.out.println("Enter the name of the company whose employees you want to get in touch with");
		String company = sc.next();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//input[@placeholder='Search']"))));
		searchBox.click();

		String companyName = company;
		searchBox.sendKeys(companyName);
		searchBox.sendKeys(Keys.ENTER);

		WebElement peopleFilterButton = driver.findElement(By.xpath("//button[@aria-label='People']"));
		peopleFilterButton.click();

		Thread.sleep(1000);
		WebElement chatArrow = driver.findElement(By.xpath("//li-icon[@type='chevron-down-icon']"));
		chatArrow.click();
		
		connectToPeople(firstName);

		js.executeScript("window.scrollBy(0, 1400);");
		Thread.sleep(1000);
		List<WebElement> intialPages = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//ul[contains(@class,'artdeco-pagination__pages')]/li/button")))) ;
		
		int noOfPages = intialPages.size();
		
		for(int i=0;i<noOfPages;i++)
		{
			if(i==3)
				break;
			List<WebElement> pages = wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//ul[contains(@class,'artdeco-pagination__pages')]/li/button")))) ;
			pages.get(i+1).click();
			Thread.sleep(1000);
			connectToPeople(firstName);
			js.executeScript("window.scrollBy(0, 1400);");
			Thread.sleep(1000);
		}
	}

	public void connectToPeople(String firstname) throws InterruptedException {
		
		Thread.sleep(1000);
		js.executeScript("window.scrollBy(0, 1400);");
		Thread.sleep(1000);
		js.executeScript("window.scrollBy(0, -1400);");
		
		String parentHandle = driver.getWindowHandle();

		List<WebElement> peoples = driver.findElements(By.xpath("//ul[contains(@class,'reusable-search__entity-result-list')]/li//span[contains(@class,'entity-result__title-text')]/a"));
		for (WebElement people : peoples) {
			Actions newwin = new Actions(driver);
			newwin.keyDown(Keys.SHIFT).click(people).keyUp(Keys.SHIFT).build().perform();
			Thread.sleep(3000);
			Set<String> handles = driver.getWindowHandles();
			for (String handle : handles) {
				if (!handle.equals(parentHandle)) {
					driver.switchTo().window(handle);
					Thread.sleep(3000);

					String name = driver.findElement(By.xpath("//h1[contains(@class,'text-heading-xlarge')]"))
							.getText();
					int spaceIndex = name.indexOf(' ');
					if (spaceIndex != -1) {
						name = name.substring(0, spaceIndex);
					}

					List<WebElement> connectButton = driver.findElements(
							By.xpath("//div[@class='pvs-profile-actions ']/button/span[text()='Connect']"));

					if (!connectButton.isEmpty()) {
						WebElement connect = connectButton.get(0);
						Thread.sleep(1000);
						connect.click();
						Thread.sleep(1000);
					} else {
						WebElement moreButton = driver.findElement(By.xpath(
								"//div[@class='pvs-profile-actions ']//button[@aria-label='More actions']/span[text()='More']"));
						moreButton.click();
						Thread.sleep(1000);

						WebElement connect = driver.findElement(By.xpath(
								"//div[@class='pvs-profile-actions ']//div[@class='artdeco-dropdown__content-inner']/ul/li/div/span[text()='Connect']"));
						connect.click();
						Thread.sleep(1000);

						WebElement connectConfirm = driver.findElement(By.xpath("//button[@aria-label='Connect']"));
						connectConfirm.click();
						Thread.sleep(1000);
					}

					WebElement addANote = driver.findElement(By.xpath("//button[@aria-label='Add a note']"));
					addANote.click();

					WebElement messageArea = driver.findElement(By.xpath("//textarea[@name='message']"));
					String jobLink = "";
					String message = "Hey " + name + ",\nHope you're doing good. I am "+ firstname+". Please accept my connection request\n";
					messageArea.sendKeys(message);

					WebElement sendButton = driver.findElement(By.xpath("//button[@aria-label='Send now']"));
					sendButton.click();

					Thread.sleep(1000);
					driver.close();
					break;
				}
			}
			driver.switchTo().window(parentHandle);
		}
	}

	@AfterClass
	public void afterClass() throws InterruptedException {
		Thread.sleep(1000);
		driver.quit();
	}

}
