package net.betaville.ng.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Skye Book
 *
 */
public abstract class XMLWriter {
	protected Document dom;
	protected Element rootElement;
	private XMLOutputter outputter;
	private FileWriter writer;

	/**
	 * @throws IOException 
	 * 
	 */
	public XMLWriter(String rootElementName, File file) throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
		rootElement = new Element(rootElementName);
		dom = new Document(rootElement);
		
		outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		writer = new FileWriter(file);
	}
	
	public void writeData() throws IOException{
		outputter.output(dom, writer);
		writer.close();
	}
}
