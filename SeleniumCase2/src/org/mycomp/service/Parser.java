package org.mycomp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mycomp.model.Good;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Parser {
	 private static final String SEPARATOR = System.getProperty("file.separator");
	 
	 private static final String USER_DIR = System.getProperty("user.dir");
	 
	 private static final String DRIVER_PATH = USER_DIR + SEPARATOR + "lib" + SEPARATOR + "chromedriver.exe";
	 	 
	 private static final String BASE_URL = "https://rozetka.com.ua/";

	 public static WebDriver getWebDriver() {		 	  
		 System.setProperty("webdriver.chrome.driver", DRIVER_PATH);	  
		 WebDriver driver = new ChromeDriver();	  	  
		 return driver;
	 }
	 
	 public List<Good> selectListGoods(String nameOfType, String nameOfCategory, String nameOfSubcategory, String nameOfSort, int pageCount) {
		 List<Good> goodsList = new ArrayList();
		 
		 WebDriver driver = getWebDriver();		  
		 Timer.waitSeconds(5);
		 driver.get(BASE_URL);
		 Timer.waitSeconds(5);
		 
		 WebElement type = driver.findElement(By.linkText(nameOfType)); 
		 String typeLink = type.getAttribute("href");
		 driver.get(typeLink);		 
		 Timer.waitSeconds(5);
		 
		 WebElement category = driver.findElement(By.linkText(nameOfCategory)); 
		 String categoryLink = category.getAttribute("href");
		 driver.get(categoryLink);
		 Timer.waitSeconds(5);
		 
		 WebElement subcategory = driver.findElement(By.linkText(nameOfSubcategory));
		 String subcategoryLink = subcategory.getAttribute("href");
		 driver.get(subcategoryLink);
		 Timer.waitSeconds(5);
		 
		 WebElement sortsBlock = driver.findElement(By.id("title_page"));
		 List<WebElement> listSorts = sortsBlock.findElements(By.className("sub-cat-l-i"));
		 
		 String sortLink = null;
		 for (WebElement sortParameters : listSorts) {			 
				WebElement tagLink = sortParameters.findElement(By.tagName("a"));
				String sortLinkText = tagLink.getText();
				if (sortLinkText.contains(nameOfSort)) {
					sortLink = tagLink.getAttribute("href");
					break;
				}				
		 }
		 		 
		 int pageNumber = 1;
		 String linkNextPage = null;
		 
		 while (pageNumber <= pageCount) {			 
			 if (pageNumber == 1) {
				 driver.get(sortLink);
			 }
			 else driver.get(linkNextPage);
		 
			 Timer.waitSeconds(5);
		 
			 WebElement goodsBlock = driver.findElement(By.id("catalog_goods_block"));	
			 List<WebElement> listGoods = goodsBlock.findElements(By.className("g-i-tile-i-box-desc"));
		 
			 for (WebElement goodParameters : listGoods) {			
				 WebElement priceElement = goodParameters.findElement(By.className("g-price-uah"));
				 String goodPriceText = priceElement.getText();			            
				 goodPriceText = goodPriceText.replaceAll("\\D+", "");			
				 int goodPrice = Integer.parseInt(goodPriceText);
				 
				 if (goodPrice >= 100 && goodPrice <= 300) {			
					 WebElement goodNameBlock = goodParameters.findElement(By.className("g-i-tile-i-title"));
					 WebElement tagLink = goodNameBlock.findElement(By.tagName("a"));
					 String goodName = tagLink.getText();	
					 Good good = new Good();
					 good.setGoodName(goodName);
					 good.setGoodPrice(goodPrice);
					 goodsList.add(good);			
				 }
			 }
			 //Page navigator
			 linkNextPage = null;
			 pageNumber++;
			 String idNextPage = "page" + pageNumber;		 
			 WebElement nextPage = driver.findElement(By.id(idNextPage));
			 WebElement buttonNextPage = nextPage.findElement(By.tagName("a"));
			 linkNextPage = buttonNextPage.getAttribute("href");
		 		 
			 if (linkNextPage == null) {
				 System.out.println("Page " + idNextPage + " is not found");
				 break;
			 }
		 }		 
		 driver.quit();		 		 
		 return goodsList;
	 }
}
