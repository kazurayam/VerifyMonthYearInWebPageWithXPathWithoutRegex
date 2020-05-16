Verifying Today'S Date in Web page using XPath without Regex in Katalon Studio
============

This project is a small [Katalon Studio](https://www.katalon.com/katalon-studio/) project for demonstration purpose. You can download the zip from the [Releases] page, unzip it, and open it using your local Katalon Studio.

This project was developed using Katalon Studio version 7.2.1.

# Problem to solve

There is a web page http://demoaut-mimic.kazurayam.com/
![demoaut-mimic](./docs/images/demoaut-mimic.png)

A datetime string `2020/5/16` is displayed in the page. I want to verify the dateime is TODAY. The datetime displayed yesterday was different from today. Tomorrow, it will be different. The datetime value moves.
 
I do not want to edit my test script to cope with the moving datetime value, Written once, and the test should pass everyday.

# Solution proposed

1. The script needs the datetime value of TODAY. I will use [`java.time.LocalDate`](https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html) class to get it.
2. The `LocalDate` class provides `plusDays(int n)` and other convenient methods. We can calculate the date of Tomorrow (`today.plusDays(1)`) or Yesterday (`today.plusDays(-1)`) just easily.
3. Datetime value displayed in a web page is formatted in various ways, often tailored according to Locale. I need to format the today's date to cope exactly with the format in the web page. I will use [`java.time.format.DateTimeFormatter`](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html) to format the instance of `java.time.LocalDate` into  a String.
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

## How the XPath is coded

You can find the definition of the TestObject [span_datetime](Object Repository/span_datetime.rs) where a XPath is written like this:

```
//span[contains(normalize-space(.),'${ptn}')]
```

The placeholder `${ptn}` will be interplated by `findTestObject()` method call with the today's date like `2020/5/16`.

Please note that the XPath uses `contains()` function. I think that XPath `contains()` function is useful and satisfies most of the cases where people tend to think they need magical `matches()` method.














