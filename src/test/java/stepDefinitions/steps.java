package stepDefinitions;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageObject.AccountRegistrationPage;
import pageObject.HomePage;
import pageObject.LoginPage;
import pageObject.MyAccountPage;
import utilities.DataReader;

public class steps {
	  WebDriver driver;
	     HomePage hp;
	     LoginPage lp;
	     MyAccountPage macc;
	     AccountRegistrationPage arp;

	     Logger logger; //for logging
	     public ResourceBundle rb;
	     String br; //to store browser name

	     List<HashMap<String, String>> datamap; //Data driven
	     
	     
	     
	     //Account Registration
	     @When("click on Register")
	     public void click_on_register() {
	    	 hp= new HomePage(driver);
	    	 hp.clickRegister();
	         
	     }

	     @When("user enters following details")
	     public void user_enters_following_details(DataTable dataTable) throws InterruptedException {
	    	 
	    	 Map<String, String> reg  = dataTable.asMap(String.class, String.class);
	    	 arp= new AccountRegistrationPage(driver);
	    	 arp.setFirstName(reg.get("FirstName"));
	    	 Thread.sleep(1000);
	    	 arp.setLastName(reg.get("LastName"));
	    	 Thread.sleep(1000);
	    	 arp.setEmail(reg.get("E-mail"));
	    	 Thread.sleep(1000);
	    	 arp.setTelephone(reg.get("Telephone"));
	    	 Thread.sleep(1000);
	    	 arp.setPassword(reg.get("password"));
	    	 Thread.sleep(1000);
	    	 arp.setConfirmPassword(reg.get("cnfPassword"));
	    	// arp.setPrivacyPolicy();
	    	 
	    	
	     }
	     @When("check the privacy policy")
	     public void check_the_privacy_policy() {
	    	  arp.setPrivacyPolicy();
	     }

	     @When("click continue")
	     public void click_continue() {
	    	 arp.clickContinue();
	      
	     }

	     @Then("user Navigate to sucessfull account  created page")
	     public void user_navigate_to_sucessfull_account_created_page() {
	    	String cnfMsg= arp.getConfirmationMsg();
	    	if(cnfMsg.equals("Your Account Has Been Created!")){
	    		
	    		logger.info("Account has been sucessfully created");
	    		Assert.assertTrue(true);
	    	}
	    	else {
	    		
	    		logger.warn("Account creation has Failed");
	    		Assert.assertTrue(false);
	    	}
	      
	     }



	    @Before
	    public void setup() throws IOException
	    {
	        //for logging
	        logger= LogManager.getLogger(this.getClass());
	        
	        //Reading config.properties (for browser)
	        rb=ResourceBundle.getBundle("config");
			br=rb.getString("browser");
			System.out.println("browser "+br);
	    }

	    @After
	    public void tearDown(Scenario scenario) {
	        System.out.println("Scenario status ======>"+scenario.getStatus());
	        if(scenario.isFailed()) {
	        	
	        	TakesScreenshot ts=(TakesScreenshot) driver;
	        	byte[] screenshot=ts.getScreenshotAs(OutputType.BYTES);
	        	scenario.attach(screenshot, "image/png",scenario.getName());
	        	            
	        }
	       driver.quit();
	    }
	
	@Given("User Launch browser")
	public void user_launch_browser() {
		if(br.equals("chrome")) {
			driver= new ChromeDriver();
			
		}
		if(br.equals("edge")) {
			driver= new EdgeDriver();
		}
		if(br.equals("firefox")) {
			driver= new FirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	    
	}

	@Given("opens URL {string}")
	public void opens_url(String url) {
		driver.get(url);
		driver.manage().window().maximize();
	   
	}

	@When("User navigate to MyAccount menu")
	public void user_navigate_to_my_account_menu() {
	   hp= new HomePage(driver);
	   hp.clickMyAccount();
	   logger.info("clicked on My Account in Home page");
	}

	@When("click on Login")
	public void click_on_login() {
	    hp.clickLogin();
	    logger.info("click My lofin in Home page");
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_email_as_and_password_as(String email, String pwd) {
		lp= new LoginPage(driver);
		lp.setEmail(email);
		lp.setPassword(pwd);
	
	    
	}

	@When("Click on Login button")
	public void click_on_login_button() {
		lp.clickLogin();
	}

	@Then("User navigates to MyAccount Page")
	public void user_navigates_to_my_account_page() {
	   macc=new MyAccountPage(driver);
	  boolean targetPage= macc.msgConfirmation();
	  if(targetPage) {
		  logger.info("login success");
		  Assert.assertTrue(true);
	  }
	  else {
		  logger.info("login failure");
		  Assert.assertTrue(false);
		  
	  }
	}	


	 @Then("check User navigates to MyAccount Page by passing Email and Password with excel row {string}")
	    public void check_user_navigates_to_my_account_page_by_passing_email_and_password_with_excel_data(String rows)
	    {
	        datamap=DataReader.data(System.getProperty("user.dir")+"\\test-data\\Opencart_LoginData.xlsx", "Sheet1");

	        int index=Integer.parseInt(rows)-1;
	        String email= datamap.get(index).get("username");
	        String pwd= datamap.get(index).get("password");
	        String exp_res= datamap.get(index).get("res");

	        lp=new LoginPage(driver);
	        lp.setEmail(email);
	        lp.setPassword(pwd);

	        lp.clickLogin();
	        macc=new MyAccountPage(driver);
	        try
	        {
	            boolean targetpage=macc.msgConfirmation();
	            System.out.println("target page: "+ targetpage);
	            if(exp_res.equals("Valid"))
	            {
	                if(targetpage==true)
	                {
	                    MyAccountPage myaccpage=new MyAccountPage(driver);
	                    myaccpage.clickLogout();
	                    Assert.assertTrue(true);
	                }
	                else
	                {
	                    Assert.assertTrue(false);
	                }
	            }

	            if(exp_res.equals("Invalid"))
	            {
	                if(targetpage==true)
	                {
	                    macc.clickLogout();
	                    Assert.assertTrue(false);
	                }
	                else
	                {
	                    Assert.assertTrue(true);
	                }
	            }


	        }
	        catch(Exception e)
	        {

	            Assert.assertTrue(false);
	        }
	        driver.close();

}
	}
	 
