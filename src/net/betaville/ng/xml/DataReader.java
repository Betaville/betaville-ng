package net.betaville.ng.xml;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ModeledDesign;
import edu.poly.bxmc.betaville.model.Design.Classification;


// TODO: Re-add AudibleDesign and VideoDesign

/**
 * Reads objects from XML
 * @author Skye Book
 *
 */
public class DataReader {
	
	public static List<Design> readDesigns(String xml) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		return readDesigns(sb.build(new StringReader(xml)).getRootElement());
	}
	
	public static List<Design> readDesigns(Element e){
		List<Design> designs = new ArrayList<Design>();
		if(e.getName().equals("designs")){
			for(int i=0; i<e.getChildren().size(); i++){
				designs.add(readDesign((Element)e.getChildren().get(i)));
			}
		}
		return designs;
	}

	
	public static Design readDesign(String xml) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		return readDesign(sb.build(new StringReader(xml)).getRootElement());
	}
	
	public static Design readDesign(Element e){
		String name = e.getAttributeValue("name");
		UTMCoordinate coordinate = (UTMCoordinate)readCoordinate(e.getChild("coordinate"));
		Classification classification = Classification.valueOf(e.getAttributeValue("classification"));
		int id = Integer.parseInt(e.getAttributeValue("id"));
		int sourceID = Integer.parseInt(e.getAttributeValue("sourceID"));
		String address = e.getAttributeValue("address");
		int cityID = Integer.parseInt(e.getAttributeValue("cityID"));
		String user = e.getAttributeValue("user");
		String description = e.getAttributeValue("description");
		String filepath = e.getAttributeValue("filepath");
		String url = e.getAttributeValue("url");
		String dateAdded = e.getAttributeValue("dateAdded");
		boolean isPublic = Boolean.parseBoolean(e.getAttributeValue("isPublic"));
		
		Design d;
		
		if(e.getName().equals(ModeledDesign.class.getName())){
			int rotX = Integer.parseInt(e.getAttributeValue("rotX"));
			int rotY = Integer.parseInt(e.getAttributeValue("rotY"));
			int rotZ = Integer.parseInt(e.getAttributeValue("rotZ"));
			boolean textured = Boolean.parseBoolean(e.getAttributeValue("textured"));
			d = new ModeledDesign(name, coordinate, address, cityID, user, description, filepath,
					url, isPublic, rotX, rotY, rotZ, textured);
		}
//		else if(e.getName().equals(AudibleDesign.class.getName())){
//		}
//		else if(e.getName().equals(VideoDesign.class.getName())){
//		}
		else if(e.getName().equals(EmptyDesign.class.getName())){
			int length = Integer.parseInt(e.getAttributeValue("length"));
			int width = Integer.parseInt(e.getAttributeValue("width"));
			d = new EmptyDesign(coordinate, address, cityID, user, description,
					url, isPublic, length, width);
		}
		else{
			/* If we've arrived here the type is either incorrect (typo)
			 * or unsupported (subclass that isn't registered here)
			 */
			return null;
		}
		
		d.setID(id);
		d.setSourceID(sourceID);
		d.setClassification(classification);
		d.setDateAdded(dateAdded);
		return d;
	}
	
	private static ILocation readCoordinate(Element e){
		if(e.getName().equals("coordinate")){
			ILocation c = null;
			if(e.getAttributeValue("type").equals(UTMCoordinate.class.getName())){
				c = new UTMCoordinate(Integer.parseInt(e.getAttributeValue("easting")),
						Integer.parseInt(e.getAttributeValue("northing")),
						Short.parseShort(e.getAttributeValue("eastingCentimeters")),
						Short.parseShort(e.getAttributeValue("northingCentimeters")),
						Integer.parseInt(e.getAttributeValue("lonZone")),
						e.getAttributeValue("latZone").charAt(0),
						Integer.parseInt(e.getAttributeValue("altitude")));
			}
			else{
				// do GPS
			}
			
			
			return c;
		}
		else return null;
	}
	
	public static List<Comment> readComments(String xml) throws JDOMException, IOException{
		SAXBuilder sb = new SAXBuilder();
		return readComments(sb.build(new StringReader(xml)).getRootElement());
	}
	
	public static List<Comment> readComments(Element e){
		if(e.getName().equals("comments")){
			List<Comment> comments = new ArrayList<Comment>();
			for(int i=0; i<e.getChildren().size(); i++){
				comments.add(readComment((Element)e.getChildren().get(i)));
			}
			return comments;
		}
		else return null;
	}
	
	public static Comment readComment(Element e){
		if(e.getName().equals(Comment.class.getName())){
			int id = Integer.parseInt(e.getAttributeValue("id"));
			String comment = e.getText();
			String user = e.getAttributeValue("user");
			int designID = Integer.parseInt(e.getAttributeValue("designID"));
			int repliesTo = Integer.parseInt(e.getAttributeValue("repliesToCommentID"));
			String date = e.getAttributeValue("date");
			return new Comment(id, designID, user, comment, repliesTo, date);
		}
		else return null;
	}
}
