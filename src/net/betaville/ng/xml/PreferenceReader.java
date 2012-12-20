package net.betaville.ng.xml;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * @author Skye Book
 *
 */
public class PreferenceReader extends XMLReader {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PreferenceReader.class);
	
	private boolean xmlLoaded=false;

	/**
	 * 
	 * @param xmlFile
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public PreferenceReader(File xmlFile) throws JDOMException, IOException{
		super();
		loadFile(xmlFile);
		xmlLoaded=true;
	}

	/* 
	 * @see edu.poly.bxmc.betaville.xml.XMLReader#parse()
	 */
	@Override
	public void parse(){
		parseImpl(rootElement);
	}
	
	@SuppressWarnings("rawtypes")
	private void parseImpl(Element top){
		String currentFullName="";
		List children = top.getChildren();
		Iterator it = children.iterator();
		while(it.hasNext()){
			Element current = (Element)it.next();
			currentFullName = current.getName();
			
			if(current.getChildren().size()==0){
				Element parent = current.getParentElement();
				currentFullName = parent.getName()+"."+currentFullName;
				while((parent = parent.getParentElement())!=null){
					currentFullName = parent.getName()+"."+currentFullName;
				}
				
				System.setProperty(currentFullName, current.getValue());
			}
			else parseImpl(current);
		}
	}
	
	public boolean isXMLLoaded(){
		return xmlLoaded;
	}
	
}
