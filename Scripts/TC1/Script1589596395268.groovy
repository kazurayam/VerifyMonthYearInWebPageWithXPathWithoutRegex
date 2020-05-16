import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

// Highlight element of my interest
CustomKeywords.'com.kazurayam.ksbackyard.HighlightElement.pandemic'()

// open my favorite test fixture page
WebUI.openBrowser("http://demoaut-mimic.kazurayam.com")

// test if a span element with yyyy/M/dd of Today is displayed
String patternToday = CustomKeywords.'com.kazurayam.ksbackyard.DateTimePattern.today'()
println "patternToday is ${patternToday}"
TestObject tObjToday = findTestObject("span_datetime", ["ptn": patternToday])
WebUI.verifyElementPresent(tObjToday, 5)

// test if a span element with yyyy/M/dd of Today+30days is NOT displayed
String patternFuture = CustomKeywords.'com.kazurayam.ksbackyard.DateTimePattern.todayPlusDays'(30)
println "patternFuture is ${patternFuture}"
TestObject tObjFuture = findTestObject("span_datetime", ["ptn": patternFuture])
WebUI.verifyElementNotPresent(tObjFuture, 5)

WebUI.delay(3)
WebUI.closeBrowser()

