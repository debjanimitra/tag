package edu.brown.cs32.vgavriel.connectorOnServer;

/**
 * enum for specifying the content of a message
 * USER: instance of User class
 * DATA: instance of Data class
 * NOTIFICATION: instance of Notification class
 * USERID: a String containing the user id
 * @author vgavriel
 *
 */
public enum MessageContent {
	USER, DATA, NOTIFICATION, USERID, DONE, NEWUSERID
}
