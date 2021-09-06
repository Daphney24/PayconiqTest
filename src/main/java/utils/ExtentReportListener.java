package utils;

import java.io.File;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReportListener  implements ITestListener{

	protected static ExtentReports reports;
	protected static ExtentTest test;

	private static String resultpath = getResultPath();


	public static void deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					System.out.println(files[i].getName());
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
	}

	private static String getResultPath() {

		resultpath = "test";
		if (!new File(resultpath).isDirectory()) {
			new File(resultpath);
		}
		return resultpath;
	}

	public void onTestStart(ITestResult result) {
		test = reports.startTest(result.getMethod().getMethodName());
		test.log(LogStatus.INFO, result.getMethod().getMethodName());
	}

	public void onTestSuccess(ITestResult result) {
		test.log(LogStatus.PASS, "Test is pass");

	}

	public void onTestFailure(ITestResult result) {
		test.log(LogStatus.FAIL, "Test is fail");

	}

	public void onTestSkipped(ITestResult result) {
		test.log(LogStatus.SKIP, "Test is skipped");

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onStart(ITestContext context) {
		String reportLocation = "test-output/Report/" + resultpath + "/";
		System.out.println("ReportLocation :: "+reportLocation);
		reports = new ExtentReports(reportLocation + "ExtentReport.html");
		test = reports.startTest("");
	}

	public void onFinish(ITestContext context) {
		reports.endTest(test);
		reports.flush();
	}

}