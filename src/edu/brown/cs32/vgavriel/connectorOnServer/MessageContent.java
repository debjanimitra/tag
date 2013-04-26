package edu.brown.cs32.vgavriel.connectorOnServer;

import java.io.Serializable;

/**
 * enum for specifying the content of a message
 * USER: instance of User class
 * DATA: instance of Data class
 * NOTIFICATION: instance of Notification class
 * USERID: a String containing the user id
 * @author vgavriel
 *
 */
public enum MessageContent implements Serializable {

	USER, DATA, NOTIFICATION, USERID, DONE, NEWUSERID, ERRORHANDSHAKE_UNKNOWNUSER, ERRORHANDSHAKE_NONUNIQUEUSER, ERRORHANDSHAKE_NOUSER, ERROR_RECEIVE_TAGALREADYEXISTS, ERROR_RECEIVE_INVALIDDATA, ERRORHANDSHAKE_MULTIPLELOGINS, DONE_BUTNOTPERSISTENT

	
}
