package net.betaville.ng.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author Skye Book
 *
 */
public abstract class XMLReader{
	private SAXBuilder builder;
	protected Document dom;
	protected Element rootElement;

	/**
	 * Reads an XML file.
	 * @throws JDOMException 
	 * @throws IOException 
	 */
	public XMLReader(){
		builder = new SAXBuilder();
	}
	
	public void loadFile(File xmlFile) throws JDOMException, IOException{
		dom = builder.build(xmlFile);
		rootElement = dom.getRootElement();
	}
	
	public void loadFile(URL xmlFile) throws JDOMException, IOException{
		dom = builder.build(xmlFile);
		rootElement = dom.getRootElement();
	}
	
	public void loadFile(InputStream xmlFile) throws JDOMException, IOException{
		dom = builder.build(xmlFile);
		rootElement = dom.getRootElement();
	}
	
	public void loadStream(InputStream is) throws JDOMException, IOException{
		dom = builder.build(is);
		rootElement = dom.getRootElement();
	}
	
	public abstract void parse() throws Exception;

}
