/**
 * 
 */
package com.zhuyanbin.app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * @author flavor
 *
 */
public class XmlLoaderTest extends TestCase {

	private XmlLoader classRelection;
	private String xmlFile = "src/test/resource/config.xml";
	protected void setUp() throws Exception 
	{
		super.setUp();
		classRelection = new XmlLoader();
	}

	protected void tearDown() throws Exception 
	{
		classRelection = null;
		super.tearDown();
	}

	public void testXmlLoaderInit() 
	{
		classRelection = new XmlLoader();
		assertTrue(classRelection instanceof XmlLoader);
		
		classRelection = new XmlLoader(xmlFile);
		assertTrue(classRelection instanceof XmlLoader);
		
		classRelection = new XmlLoader(null);
		assertTrue(classRelection instanceof XmlLoader);
		
		classRelection = new XmlLoader("sdfasdfasdf/asdfasdf.xml");
		assertTrue(classRelection instanceof XmlLoader);
	}

	public void testGetDocumentAfterLoad() 
	{
		assertNull(classRelection.getDocument());
		// load success
		try
		{
			classRelection.load(xmlFile);
			assertTrue(classRelection.getDocument() instanceof Document);
		}
		catch (IOException e) 
		{
			fail(e.getMessage());
		}
		catch (IllegalArgumentException e) 
		{
			fail(e.getMessage());
		}
		catch (ParserConfigurationException e) 
		{
			fail(e.getMessage());
		}
		catch (SAXException e) 
		{
			fail(e.getMessage());
		}
		
		classRelection = new XmlLoader(xmlFile);
		try
		{
			classRelection.load();
			assertTrue(classRelection.getDocument() instanceof Document);
		}
		catch (IOException e) 
		{
			fail(e.getMessage());
		}
		catch (IllegalArgumentException e) 
		{
			fail(e.getMessage());
		}
		catch (ParserConfigurationException e) 
		{
			fail(e.getMessage());
		}
		catch (SAXException e) 
		{
			fail(e.getMessage());
		}
		
		// not such file
		classRelection = new XmlLoader();
		try
		{
			classRelection.load("sdfasdfsdf/sdfasdf.xml");
			assertTrue(classRelection.getDocument() instanceof Document);
		}
		catch (IOException e) 
		{
			assertTrue(e instanceof IOException);
		}
		catch (IllegalArgumentException e) 
		{
			fail(e.getMessage());
		}
		catch (ParserConfigurationException e) 
		{
			fail(e.getMessage());
		}
		catch (SAXException e) 
		{
			fail(e.getMessage());
		}
		
		// null
		classRelection = new XmlLoader();
		try
		{
			classRelection.load();
			assertTrue(classRelection.getDocument() instanceof Document);
		}
		catch (IOException e) 
		{
			fail(e.getMessage());
		}
		catch (IllegalArgumentException e) 
		{
			assertTrue(e instanceof IllegalArgumentException);
		}
		catch (ParserConfigurationException e) 
		{
			fail(e.getMessage());
		}
		catch (SAXException e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testXmlFileIsMutable()
	{
		assertNull(classRelection.getXmlFilePath());
		classRelection.setXmlFilePath(xmlFile);
		assertEquals(xmlFile, classRelection.getXmlFilePath());
	}
}
