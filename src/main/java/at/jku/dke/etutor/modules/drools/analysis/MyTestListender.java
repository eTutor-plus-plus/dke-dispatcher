package at.jku.dke.etutor.modules.drools.analysis;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;

public class MyTestListender implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        // Hier haben Sie Zugriff auf das erfolgreiche Testergebnis
        // iTestResult.getTestName() und iTestResult.getMethod().getMethodName() können verwendet werden, um auf Testinformationen zuzugreifen
        System.out.println(">>> Test " + iTestResult.getName() + " erfolgreich durchgeführt.");
        System.out.println(Arrays.toString(iTestResult.getParameters()));
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        // Hier haben Sie Zugriff auf das fehlgeschlagene Testergebnis
        // iTestResult.getTestName() und iTestResult.getMethod().getMethodName() können verwendet werden, um auf Testinformationen zuzugreifen
        System.out.println(">>> Test " + iTestResult.getName() + " ist fehlgeschlagen.");
        System.out.println(Arrays.toString(iTestResult.getParameters()));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        // Hier haben Sie Zugriff auf das übersprungene Testergebnis
        // iTestResult.getTestName() und iTestResult.getMethod().getMethodName() können verwendet werden, um auf Testinformationen zuzugreifen
        System.out.println(">>> Test " + iTestResult.getName() + " wurde übersprungen.");
        System.out.println(Arrays.toString(iTestResult.getParameters()));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        // Implementierung entsprechend den Anforderungen
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        // Implementierung entsprechend den Anforderungen
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        // Implementierung entsprechend den Anforderungen
    }
}
