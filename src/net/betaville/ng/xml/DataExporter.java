package net.betaville.ng.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import edu.poly.bxmc.betaville.jme.map.GPSCoordinate;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ModeledDesign;

// TODO: Re-add AudibleDesign, VideoDesign and Bookmark

/**
 * Writes objects to XML
 * @author Skye Book
 */
public class DataExporter{
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(DataExporter.class);
	
	public static Element export(ILocation location){
		Element coordinate = new Element("coordinate");
		coordinate.setAttribute("type", location.getClass().getName());
		if(location instanceof UTMCoordinate){
			coordinate.setAttribute("easting", Integer.toString(((UTMCoordinate)location).getEasting()));
			coordinate.setAttribute("northing", Integer.toString(((UTMCoordinate)location).getNorthing()));
			coordinate.setAttribute("eastingCentimeters", Short.toString(((UTMCoordinate)location).getEastingCentimeters()));
			coordinate.setAttribute("northingCentimeters", Short.toString(((UTMCoordinate)location).getNorthingCentimeters()));
			coordinate.setAttribute("lonZone", Integer.toString(((UTMCoordinate)location).getLonZone()));
			coordinate.setAttribute("latZone", Character.toString(((UTMCoordinate)location).getLatZone()));
			coordinate.setAttribute("altitude", Float.toString(((UTMCoordinate)location).getAltitude()));
		}
		else if(location instanceof GPSCoordinate){
			coordinate.setAttribute("latitude", Double.toString(((GPSCoordinate)location).getLatitude()));
			coordinate.setAttribute("longitude", Double.toString(((GPSCoordinate)location).getLongitude()));
			coordinate.setAttribute("altitude", Double.toString(((GPSCoordinate)location).getAltitude()));
		}
		return coordinate;
	}
	
	public static Element exportDesigns(List<Design> designs){
		Element e = new Element("designs");
		for(Design design : designs){
			e.addContent(export(design));
		}
		return e;
	}
	
	public static Element export(Design design){
		Element d = new Element(design.getClass().getName());
		d.setAttribute("name", design.getName());
		d.setAttribute("classification", design.getClassification().toString());
		d.setAttribute("id", Integer.toString(design.getID()));
		d.setAttribute("sourceID", Integer.toString(design.getSourceID()));
		if(design.getAddress()!=null) d.setAttribute("address", design.getAddress());
		d.setAttribute("cityID", Integer.toString(design.getCityID()));
		d.setAttribute("user", design.getUser());
		if(design.getDescription()!=null) d.setAttribute("description", design.getDescription());
		d.setAttribute("filepath", design.getFilepath());
		if(design.getURL()!=null) d.setAttribute("url", design.getURL());
		d.setAttribute("dateAdded", design.getDateAdded());
		d.setAttribute("isPublic", Boolean.toString(design.isPublic()));
		
		// construct designs to remove string
		StringBuilder dtr = new StringBuilder();
		for(int toRemove : design.getDesignsToRemove()){
			dtr.append(toRemove+";");
		}
		d.setAttribute("designsToRemove", dtr.toString());
		
		// construct user fave string
		StringBuilder favedBy = new StringBuilder();
		for(String userWhoFaved : design.getFavedBy()){
			favedBy.append(userWhoFaved+";");
		}
		d.setAttribute("favedBy", favedBy.toString());
		
		if(design.getProposalPermission()!=null) d.setAttribute("proposalPermission", design.getProposalPermission().toString());
		
		if(design instanceof ModeledDesign){
			d.setAttribute("rotX", Float.toString(((ModeledDesign)design).getRotationX()));
			d.setAttribute("rotY", Float.toString(((ModeledDesign)design).getRotationY()));
			d.setAttribute("rotZ", Float.toString(((ModeledDesign)design).getRotationZ()));
			d.setAttribute("textured", Boolean.toString(((ModeledDesign)design).isTextured()));
		}
//		else if(design instanceof AudibleDesign){
//		}
//		else if(design instanceof VideoDesign){
//		}
		else if(design instanceof EmptyDesign){
			d.setAttribute("length", Integer.toString(((EmptyDesign)design).getLength()));
			d.setAttribute("width", Integer.toString(((EmptyDesign)design).getWidth()));
		}
		
		d.addContent(export(design.getCoordinate()));
		
		return d;
	}
	
	public static Element exportComments(List<Comment> comments){
		Element e = new Element("comments");
		for(Comment comment : comments){
			e.addContent(export(comment));
		}
		return e;
	}
	
	public static Element export(Comment comment){
		Element e = new Element(comment.getClass().getName());
		e.setAttribute("id", Integer.toString(comment.getID()));
		e.setText(comment.getComment());
		e.setAttribute("user", comment.getUser());
		e.setAttribute("designID", Integer.toString(comment.getDesignID()));
		e.setAttribute("repliesToCommentID", Integer.toString(comment.repliesTo()));
		e.setAttribute("date", comment.getDate());
		return e;
	}
	
	public static void write(Element element, File f) throws IOException{
		Document dom = new Document(element);
		
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		FileWriter writer = new FileWriter(f);
		outputter.output(dom, writer);
		writer.close();
	}
}
