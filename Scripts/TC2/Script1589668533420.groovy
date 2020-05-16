import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.ConditionType

import com.kazurayam.ksbackyard.HighlightElement
import com.kazurayam.ksbackyard.KazTestObjectFactory

// Highlight element of my interest
HighlightElement.pandemic()

// open my favorite test fixture page
WebUI.openBrowser("http://demoaut-mimic.kazurayam.com")

// test if a span element with yyyy/M/dd of Today is displayed
String patternToday = CustomKeywords.'com.kazurayam.ksbackyard.DateTimePattern.today'()

TestObject tObjToday = 
	KazTestObjectFactory.createTestObject(
		'tObj',
		// Be sure NOT to use double quotes "..." for the template string.
		// You MUST use single quotes '...' as you want to pass the placeholder ${ptn} as is
		'//span[contains(normalize-space(.),\'${ptn}\')]',
		["ptn": patternToday])

WebUI.verifyElementPresent(tObjToday, 5)

WebUI.delay(5)
WebUI.closeBrowser()
