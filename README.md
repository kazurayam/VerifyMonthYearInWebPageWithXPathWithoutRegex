Verifying today's Year/Month/Day in a Web page using XPath in Katalon Studio
============

This project is a small [Katalon Studio](https://www.katalon.com/katalon-studio/) project for demonstration purpose. You can download the zip from the [Releases](https://github.com/kazurayam/VerifyMonthYearInWebPageWithXPathWithoutRegex/releases) page, unzip it, and open it using your local Katalon Studio.

This project was developed using Katalon Studio version 7.2.1.

# Problem to solve

There is a web page http://demoaut-mimic.kazurayam.com/
![demoaut-mimic](./docs/images/demoaut-mimic.png)

A date string `2020/5/16` is displayed in the page. I want to verify the date is TODAY. The value displayed yesterday was different from today. Tomorrow, it will be different from today. The date value displayed in the page moves.
 
I do not want to edit my test script to cope with the moving date value displayed, Written once, the test should pass everyday.

# Solution proposed

1. The script needs the datetime value of TODAY. I will use [`java.time.LocalDate`](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html) class to get it.
2. The `LocalDate` class provides `plusDays(int n)` and other convenient methods. We can calculate the date of Tomorrow (`today.plusDays(1)`) or Yesterday (`today.plusDays(-1)`) just easily.
3. Datetime value displayed in a web page is formatted in various ways. Often date display is tailored according to Locale. E.g, in Japan, I may see 令和2年5月16日. I need to format the today's date to cope exactly with the format applied in the web page. I will use [`java.time.format.DateTimeFormatter`](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html) to format the instance of `java.time.LocalDate` into  a String.
4. I will create a TestObject, which is a container of CSS/XPath selector to be passed to WebDriver, with parameter which will be interpolated with the today's date when the test script is executed.
5. I will use a XPath with `contains()` function call. I do not need `match()` function here.

# Description

## How to run the demo

You can open "Test Cases/TC1" and run with any browser.

You will see the test passes.


## Video
 
You can the [video](https://drive.google.com/open?id=1AZ6lZWJZ4OfqieIoeGxXlKfiQlsD6RSa). You will notice that the `<span>yyyy/M/dd hh:dd:ss</span>` element is addressed and highlighted with red borderline.


## Test Case script

See [Test Cases/TC1](Scripts/TC1/Script1589596395268.groovy).

Essentially, the following code fragment will be interesting.

```
String patternToday = CustomKeywords.'com.kazurayam.ksbackyard.DateTimePattern.today'()
TestObject tObjToday = findTestObject("span_datetime", ["ptn": patternToday])
WebUI.verifyElementPresent(tObjToday, 5)
```

1. The today's date as string (e.g., `2020/5/16`) is resolved calling a custom keyword.
2. The today's date is passed to `findTestObject(name, params)` method call. The TestObject obtained by this call will be aware of today' date.
3. The `WebUI.verifyElementPresent()` keyword will do what it should do.

## How to generate the pattern string of "Today's date"

I wrote a custome keyword where a few lines of Groovy code generates a String of Today's date. 

[Custom keyword](./Keywords/com/kazurayam/ksbackyard.DateTimePattern.groovy)
```
public static String today() {
    // Todays date
    LocalDate today = LocalDate.now(ZoneOffset.UTC)
    // Todays Day as 2 gigits. e.g, "16"
    String dayToday = today.format(DateTimeFormatter.ofPattern("dd", Locale.JAPAN))
    // Todays Month as 1-2 digits. E.g, '5' as May
    String monthToday = today.format(DateTimeFormatter.ofPattern("M", Locale.JAPAN))
    // Todays Year as 4 digits. E.g, '2020'
    String yearToday = today.format(DateTimeFormatter.ofPattern("yyyy", Locale.JAPAN))
    // construct the pattern to check elements with
    String patternToday = "${yearToday}/${monthToday}/${dayToday}"
    return patternToday
}
```

You might be surprised that the method is this lengthy and complex. In fact, this is the core part of this demo project. Generating an appropriate pattern string as Today's date is most important when you are going to verify date in a web page because every web page has it's own format of date and you, as a tester, have to adopt to those variations of date formats.


## How the XPath is coded

You can find the definition of the TestObject [span_datetime](Object Repository/span_datetime.rs) where a XPath is written like this:

```
//span[contains(normalize-space(.),'${ptn}')]
```

The placeholder `${ptn}` will be interplated by `findTestObject()` method call with the today's date like `2020/5/16`.

Please note that the XPath uses `contains()` function. I think that XPath `contains()` function is useful and satisfies most of the cases where people tend to think they need magical `matches()` method.



----

# One more problem to solve: how to create a TestObject by script

In the TC1, the following one line created a TestObject.

```
TestObject tObjToday = findTestObject("span_datetime", ["ptn": patternToday])
```

The source of the TestObject was named "span_datetime", was prepeared in the "Object Repsitory" with a template String
```
//span[contains(normalize-space(.),'${ptn}')]
```

The template was processed by `findTestObject()` method and the resulting TestObject has the xpath:
```
//span[contains(normalize-space(.),'2020/5/16')]
```

Please note that the placeholder `${ptn}` was interpolated to a string `2020/5/16`. This interpolation was done by `findTestObject()` method.

However, sometimes we do not like to create entries in the "Object Repository". Escpecially when we need a lot of TestObjects --- creating them manually is cumbersome. We may prefer creating TestObjects by script --- single line per a TestObject.

How can I create a TestObject by script while performing interpolation of placeholders in the template string with values wanted? I want to write a custom keyword that can do it.

## Solution 


I made one more demo testcase script [`Test Cases/TC2`](Scripts/TC2/Script1589668533420.groovy)

```
TestObject tObjToday = 
	KazTestObjectFactory.createTestObject(
		'tObj',
		'//span[contains(normalize-space(.),\'${ptn}\')]',
		["ptn": patternToday])


```

This one line creates an instance of TestObject dynamically.

The 2nd argument is the string as template from which a XPath expression is derived. Please note that it contains a placeholder `${ptn}`.

The 3rd argument is a Map object, which contains key=value pairs. The key `ptn` is associated with value of `2020/5/16` here.

<blockquote>Be sure NOT to use double quotes "..." for the template string. You MUST use single quotes '...' as you want to pass the placeholder ${ptn} as is.</blockquote>
		


I made a custom keyword [`com.kazurayam.ksbackyard.KazTestObjecFactory`](./Keywords/com/kazurayam/ksbackyard/KazTestObjectFactory.groovy). Its `createTestObject` method does the magic.

```

public static TestObject createTestObject(String id, String template, Map params) {
		...
		// interpolate placeholders in the template with values supplied in the params
		String expr = new GroovyShell(new Binding(params)).evaluate('\"' + template + '\"')
		...
		TestObject tObj = new TestObject(id)
		tObj.addProperty("xpath", ConditionType.EQUALS, expr)
		return tObj

```

Here I used `groovy.lang.GroovyShell` and `groovy.lang.Binding` classes. For more informatin of these classes, see [Integrating Groovy in a Java application](http://docs.groovy-lang.org/latest/html/documentation/guide-integrating.html)

