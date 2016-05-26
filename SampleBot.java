package com.avaamo.java.test;

import java.io.File;
import com.avaamo.java.Avaamo;
import com.avaamo.java.attachments.CardAttachment;
import com.avaamo.java.attachments.FileAttachment;
import com.avaamo.java.attachments.Image;
import com.avaamo.java.models.IncomingMessageModel;
import com.avaamo.java.models.ReadAckModel;

public class SampleBot {

	static Avaamo avaamo;

	static String bot_uuid = "2aa5c062-4aba-40e5-9200-e853ca5ed4da";
	static String access_token = "eXatcc2s1pZUty7vD48BzvZQtmIJ8jP7";

	public static void main(String[] args) throws Exception {

		/**  
		 * 1. open Avaamo websocket
		 */
		avaamo = new Avaamo(bot_uuid, access_token);


		/**
		 *  2. add a real time message listener
		 */

		avaamo.addEventHandler(new Avaamo.MessageHandler() {
			@Override
			public void handleMessage(IncomingMessageModel message) {
				try{
					String content = message.message.content;
					System.out.println("\n==> "+message.user.firstName+": "+ content);					
					String cuuid = message.conversation.uuid;
					

					
					switch(content.toUpperCase()){
						case "IMAGE":
							// Image example
							Image image = new Image(new File("test_image.jpg"));
							image.setCaption("This is the image caption");
							avaamo.sendImage(image, cuuid);
							break;
							
						case "FILE":
							// File example
							FileAttachment fileAttachment = new FileAttachment(new File("TestFile.txt"));
							avaamo.sendFileAttachment(fileAttachment, cuuid);
							break;
						case "CARD":
							
							// Card example
							CardAttachment cardAttachment = new CardAttachment();
							cardAttachment.setTitle("Card Title");
							cardAttachment.setDescription("Card Description. This has minimal rich text capabilities as well. For example <b>Bold</b> <i>Italics</i>");
							cardAttachment.setURL("http://www.avaamo.com");
							cardAttachment.addLink(new CardAttachment.WebpageCardLink("Web URL", "http://www.avaamo.com"));
							cardAttachment.addLink(new CardAttachment.SendMessageDeeplink("Post a Message", "Sample Action"));
							cardAttachment.addLink(new CardAttachment.SendFormToConversationDeeplink("Open a Form", "63c906c3-553e-9680-c273-28d1e54da050", "Say Yes", null));
							cardAttachment.setShowcaseImage(new File("test_image.jpg"));
							avaamo.sendCardAttachment(cardAttachment, cuuid);
							break;
						case "SAMPLE ACTION":
							// Sample action response
							avaamo.sendMessage("Sample action response.", cuuid);
							break;
							
							
						default:
							// Default welcome message
							avaamo.sendMessage("Awesome. It works!. \nType one of the following to see them in action. \nimage \nfile \ncard", cuuid);
							break;
					}
					
					

					
				}catch (Exception error){
					error.printStackTrace();
					System.err.println("Error processing the message."+ error.getMessage());
				}

			}
			
			@Override
			public void handleReadAck(ReadAckModel readAckModel) {
				System.out.println("Incoming read ack for message uuid : " + readAckModel.read_ack.message_uuid );
			}
		});


		// Keep this process running
		Thread.sleep(60000000);
	}

}
