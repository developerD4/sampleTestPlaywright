package com.winvinaya.playwrite.test;

import com.microsoft.playwright.*;
import com.winvinaya.playwrite.utils.CsvReportWriter;

import org.testng.Assert;
import org.testng.annotations.*;
import com.winvinaya.playwrite.utils.SendAttachment;

import java.util.*;

public class WinVinayaTests {

	Playwright playwright;
	Browser browser;
	BrowserContext context;
	Page page;
	CsvReportWriter csvReportWriter;

	// Define browser/app and environment
	private final String BROWSER_APP = "Chromium";
	private final String ENVIRONMENT = "Winvinaya-InfoSystems-website";
	private final String URL = "https://winvinaya.com/";

	// List to store test results
	private final List<TestResult> results = new ArrayList<>();

	@BeforeClass
	public void setup() throws Exception {
		playwright = Playwright.create();
		browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
		context = browser.newContext();
		page = context.newPage();

		// Initialize CsvReportWriter (header will be written)
		csvReportWriter = new CsvReportWriter("target/TestResults.csv", BROWSER_APP, ENVIRONMENT);
	}

	@AfterClass
	public void teardown() throws Exception {
		if (page != null) page.close();
		if (context != null) context.close();
		if (browser != null) browser.close();
		if (playwright != null) playwright.close();

		// Sort results by TCID before writing CSV
		results.sort(Comparator.comparing(r -> r.tcID));

		// Write sorted results to CSV
		for (TestResult r : results) {
			csvReportWriter.writeRow(r.tcID, r.description, r.result, r.duration);
		}

		if (csvReportWriter != null) csvReportWriter.close();
		
//		SendAttachment mail =new SendAttachment();
//		mail.sendmail();
	}

	@Test (groups= {"PlayWright"}, enabled= true,description="", priority=1)
	public void HomePage() {
		runTest("TC_01", "Verify the infosystem Home Page is opening or not", () -> {
			page.navigate(URL);
			Locator header = page.locator("(//h1)[1]");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in Home Page: " + headerText);
			
			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && header.isVisible() && "Empowering Innovation, Inclusion, and Impact".equals(headerText), 
					"Title should match 'WinVinaya InfoSystems' and header should be 'Empowering Innovation, Inclusion, and Impact'");
		});
	}

	@Test(groups= {"PlayWright"}, enabled= true,description="", priority=2)
	public void AboutPage() {
		runTest("TC_02", "Verify the About Page is opening or not", () -> {
			page.navigate(URL);
			page.click("text=About");

			Locator header = page.locator("h1");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in About Page: " + headerText);

			// Assert URL contains "about" AND header text equals "Who We Are"
			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && page.url().contains("about") && header.isVisible() && "Who We Are".equals(headerText), 
					"URL should contain 'about' and header should be 'Who We Are'");
		});
	}


	@Test(groups= {"PlayWright"}, enabled= true,description="", priority=3)
	public void NewslettersPage() {
		runTest("TC_03", "Verify the Newsletters Page is opening or not", () -> {
			page.navigate(URL);
			page.click("text=Newsletters");

			Locator header = page.locator("(//h2)[1]");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in Newsletters Page: " + headerText);

			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && page.url().contains("newsletters") && header.isVisible() && "Newsletter".equals(headerText), 
					"URL should contain 'newsletters' and header should be 'Newsletter'");
		});
	}
	
	@Test(groups= {"PlayWright"}, enabled= true,description="", priority=4)
	public void BlogsPage() {
		runTest("TC_04", "Verify the Blogs Page is opening or not", () -> {
			page.navigate(URL);
			page.click("text=Blogs");

			Locator header = page.locator("h1");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in Blogs Page: " + headerText);

			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && page.url().contains("blogs") && header.isVisible() && "Our Blog".equals(headerText), 
					"URL should contain 'blogs' and header should be 'Our Blog'");
		});
	}
	
	@Test(groups= {"PlayWright"}, enabled= true,description="", priority=5)
	public void eBooksPage() {
		runTest("TC_05", "Verify the eBooks Page is opening or not", () -> {
			page.navigate(URL);
			page.click("text=Accessible eBooks");

			Locator header = page.locator("(//h1)[1]");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in eBooks Page: " + headerText);

			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && page.url().contains("ebooks") && header.isVisible() && "Explore Our Accessible eBooks".equals(headerText), 
					"URL should contain 'eBooks' and header should be 'Explore Our Accessible eBooks'");
		});
	}
	
	@Test(groups= {"PlayWright"}, enabled= true,description="", priority=6)
	public void ContactUsPage() {
		runTest("TC_06", "Verify the Contact Us Page is opening or not", () -> {
			page.navigate(URL);
			page.click("text=Contact Us");

			Locator header = page.locator("(//h2)[1]");
			String headerText = header.textContent(); // textContent() in Java Playwright returns String

			System.out.println("Header text in Contact Us Page: " + headerText);

			Assert.assertTrue(page.title().equals("WinVinaya InfoSystems") && page.url().contains("contact") && header.isVisible() && "Contact Us".equals(headerText), 
					"URL should contain 'contact' and header should be 'Contact Us'");
		});
	}

	// Helper method to run tests and store results
	private void runTest(String tcNo, String description, TestAction action) {
		long start = System.currentTimeMillis();
		String result = "FAILED";

		try {
			action.run();
			result = "PASSED";
		} catch (AssertionError | Exception e) {
			System.out.println(e);
		} finally {
			int durationSec = (int) ((System.currentTimeMillis() - start) / 1000);
			results.add(new TestResult(tcNo, description, result, durationSec));
		}
	}

	// Functional interface for lambda
	@FunctionalInterface
	interface TestAction {
		void run() throws Exception;
	}

	// Class to hold test result info
	static class TestResult {
		String tcID;
		String description;
		String result;
		int duration;

		public TestResult(String tcID, String description, String result, int duration) {
			this.tcID = tcID;
			this.description = description;
			this.result = result;
			this.duration = duration;
		}
	}
}
