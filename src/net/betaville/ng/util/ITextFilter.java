package net.betaville.ng.util;

/**
 * Adds the ability to filter content when adding comment, creating proposals,
 * or any action that requires user input.
 * @author Skye Book
 *
 */
public interface ITextFilter {
	boolean isClean(String text);
}