package edu.poly.bxmc.betaville.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * Basic implementation of a tree commenting system.  Only allows
 * for a single level of indentation/quotation functionality at
 * this point in time.
 * @author Skye Book
 */
public class CommentTree {
	private Vector<Comment> comments;
	private ArrayList<ArrayList<Comment>> commentSets;

	/**
	 * Creates a new <code>CommentTree</code> and automatically
	 * sorts it.
	 * @param commentLoad The comments to add, must be ordered from oldest
	 * at index 0 to newest.
	 */
	@SuppressWarnings("unchecked")
	public CommentTree(Vector<Comment> commentLoad){
		comments=(Vector<Comment>) commentLoad.clone();
		commentSets = new ArrayList<ArrayList<Comment>>();
		sortComments();
	}
	
	private void sortComments(){
		// SET UP INDENTATION
		Iterator<Comment> it = comments.iterator();
		while(it.hasNext()){
			Comment c = it.next();
			// check for comment replying to the root
			if(c.repliesTo()==0){
				c.setIndentLevel(0);
			}
			// or find the comment which this comment is replying to
			else{
				Iterator<Comment> it2 = comments.iterator();
				while(it2.hasNext()){
					Comment c2 = it2.next();
					if(c2.getID()==c.repliesTo()){
						// Set the indent level and drop from the iterator
						c.setIndentLevel(c2.getIndentLevel()+1);
						break;
					}
				}
			}
		}
		
		// SET UP ORDER
		for(int i=0; i<comments.size(); i++){
			if(comments.get(i).getIndentLevel()>0){
				for(int j=0; j<i; j++){
					if(comments.get(i).repliesTo()==comments.get(j).getID()){
						findRoot(comments.get(j).getID()).add(comments.get(i));
					}
				}
			}
			else{
				ArrayList<Comment> set = new ArrayList<Comment>();
				set.add(comments.get(i));
				commentSets.add(set);
			}
		}
	}
	
	private ArrayList<Comment> findRoot(int rootID){
		for(int i=0; i<commentSets.size(); i++){
			if(commentSets.get(i).get(0).getID()==rootID){
				return commentSets.get(i);
			}
		}
		return null;
	}
	
	/**
	 * This needs to be re-thought and re-written.
	 * @param commentSet
	 * @experimental
	 */
	public void loadComments(Vector<Comment> commentSet){
		
	}
	
	/**
	 * creates an ordered <code>Vector</code> of comments which can be
	 * read and displayed in a coherent order.
	 * @see Comment#getID()
	 * @see Comment#getIndentLevel()
	 * @return
	 */
	public Vector<Comment> printToVector(){
		Vector<Comment> orderedComments = new Vector<Comment>();
		for(int i=0; i<commentSets.size(); i++){
			for(int j=0; j<commentSets.get(i).size(); j++){
				orderedComments.add(commentSets.get(i).get(j));
			}
		}
		return orderedComments;
	}
	
	/**
	 * creates an ordered <code>Vector</code> of comments which can be
	 * read and displayed in a coherent order.
	 * @see Comment#getID()
	 * @see Comment#getIndentLevel()
	 * @return
	 */
	public ArrayList<Comment> printToArrayList(){
		ArrayList<Comment> orderedComments = new ArrayList<Comment>();
		for(int i=0; i<commentSets.size(); i++){
			for(int j=0; j<commentSets.get(i).size(); j++){
				orderedComments.add(commentSets.get(i).get(j));
			}
		}
		return orderedComments;
	}
	
	public void clear(){
		Iterator<ArrayList<Comment>> it = commentSets.iterator();
		while(it.hasNext()){
			it.next().clear();
		}
		commentSets.clear();
	}

}
