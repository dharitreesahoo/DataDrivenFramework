package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.jcommander.Parameter;

public class dataProviderSample {
	WebDriver driver;
	String ScenarioName;
	
	@Parameters("ScenarioName")
	public dataProviderSample(String ScenarioName)
	{
		this.ScenarioName =ScenarioName;
	}
	
	@Parameter
	@Test(priority=1,dataProvider = "userData")
	public void test1(Map<String,String> mapData) {
		System.setProperty("webdriver.chrome.driver", "D:\\ChromeDrivers\\ChromeDriver_84\\chromedriver.exe"); 
		driver = new ChromeDriver();
		driver.get("http://demo.automationtesting.in/Register.html");
		driver.findElement(By.xpath("//input[@placeholder='First Name']")).sendKeys(mapData.get("Name"));
	}
	@Parameter
	@Test(priority=2,dataProvider = "addressData")
	public void test2(Map<String,String> mapData) {
		driver.findElement(By.xpath("//textarea[@class='form-control ng-pristine ng-untouched ng-valid']")).sendKeys(mapData.get("addresses"));
		driver.close();
	
	}
	

	@DataProvider(name = "userData")
	public Object[][] dataProviderMethod() throws Exception {
		String strSheetName = "UserData";
		String filePath = System.getProperty("user.dir") + "/resources/DataSheet.xlsx";
		Object[][] userData = getData(filePath, strSheetName ,ScenarioName);
		return userData;
	}
	
	
	@DataProvider(name = "addressData")
	public Object[][] dataProviderMethod2() throws Exception {
		String strSheetName = "Address";
		String filePath = System.getProperty("user.dir") + "/resources/DataSheet.xlsx";
		Object[][] userData = getData(filePath, strSheetName,ScenarioName);
		return userData;
	}

	//keep this code in excel solution
	public Object[][] getData(String filePath, String strSheetName,String ScenarioNameInTestNG) throws Exception {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheet(strSheetName);

		int rowCount = sheet.getLastRowNum();
		int scenarioRowCount=0;
		
		for (int i = 0; i < rowCount; i++) {
			String scenarioName = sheet.getRow(i).getCell(0).toString();
			System.out.println(scenarioName);
			if(scenarioName.equalsIgnoreCase(ScenarioNameInTestNG))
			{
				scenarioRowCount= scenarioRowCount+1;
			}
		}
		
		int colCount = sheet.getRow(0).getLastCellNum();
		// define a map and define an object array
		Object[][] obj = new Object[scenarioRowCount][1];

		for (int i = 0; i < scenarioRowCount; i++) {
			Map<Object, Object> dataMap = new HashMap<Object, Object>();
			for (int j = 1; j < colCount; j++) {
				dataMap.put(sheet.getRow(0).getCell(j).toString(), sheet.getRow(i + 1).getCell(j).toString());
			}
			obj[i][0] = dataMap;
		}
		return obj;
	}

}
