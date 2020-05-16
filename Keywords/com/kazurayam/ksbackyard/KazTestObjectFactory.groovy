package com.kazurayam.ksbackyard

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject

import groovy.lang.GroovyShell
import groovy.lang.Binding

public class KazTestObjectFactory {

	/**
	 * Create a TestObject dynamically while interpolating placeholders in the template string
	 * with name=value pairs specified.
	 *
	 * What's GroovyShell?
	 * See http://docs.groovy-lang.org/latest/html/documentation/guide-integrating.html
	 *
	 * @param template e.g., a String like '//span[contains(normalize-space(.),'${ptn}')]'
	 * @param params   e.g., a Map like    ['ptn':'2020/5/16']
	 * @return
	 */
	public static TestObject createTestObject(String id, String template, Map params) {
		Objects.requireNonNull(id, "id must not be null")
		Objects.requireNonNull(template, "template must not be null")
		Objects.requireNonNull(params, "params must not be null")
		// interpolate placeholders in the template with values supplied in the params
		String expr = new GroovyShell(new Binding(params)).evaluate('\"' + template + '\"')
		assert expr != null
		println "expr=${expr}"
		TestObject tObj = new TestObject(id)
		tObj.addProperty("xpath", ConditionType.EQUALS, expr)
		return tObj
	}
}
