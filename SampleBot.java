
import java.io.File;

import com.avaamo.java.Avaamo;
import com.avaamo.java.attachments.CardAttachment;
import com.avaamo.java.attachments.FileAttachment;
import com.avaamo.java.attachments.Image;
import com.avaamo.java.models.Activity;
import com.avaamo.java.models.FormAttachmentResponse;
import com.avaamo.java.models.IncomingMessageModel;
import com.avaamo.java.models.IncomingMessageModel.FileAttachmentResponse;
import com.avaamo.java.models.ReadAckModel;
import com.avaamo.java.models.form.FormResponse;
import com.avaamo.java.models.form.Question;
import com.avaamo.java.models.form.ReplyData.CheckListData;
import com.avaamo.java.models.form.ReplyData.FileData;
import com.avaamo.java.models.form.ReplyData.ImageGroupData;
import com.avaamo.java.models.form.ReplyData.LocationData;
import com.avaamo.java.models.form.ReplyData.OptionData;

public class SampleBot {

	static Avaamo avaamo;

	static String bot_uuid = "0f34af86-3310-4b0d-aeac-3b9e7eab84f2";
	static String access_token = "m1Mvdhj4HF3awFQ2InVTpSJ1tGy5efba";
	
//	static String bot_uuid = "1b1fa0ef-4f01-47d8-9959-d62a5fac2593";
//	static String access_token = "tVq-9ceBf0laP7S0CThH9jnc4QR6PQzG";

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
					
					avaamo.readMessage(message);
					
					if(message.message.attachment instanceof FileAttachmentResponse){
						FileAttachmentResponse fileAttachmentResponse = (FileAttachmentResponse) message.message.attachment;
						avaamo.downloadAttachment(fileAttachmentResponse.uuid, new File("download"));
					}
					
					else if(message.message.attachment instanceof FormAttachmentResponse){
						System.out.println("Incoming form from user: ");
						FormAttachmentResponse formAttachmentResponse = (FormAttachmentResponse) message.message.attachment;
						System.out.println("Downloading form...");
						FormResponse formResponse = formAttachmentResponse.downloadResponse();
						System.out.println("Downloading form complete.");
						for(Question question : formResponse.questions){
							System.out.println(question.title);
							System.out.println(question.reply.getAnswer());
							switch (question.questionType) {
							case AUTO_CHECKIN:
							case LOCATION:
								if(question.reply.replyData instanceof LocationData){
									LocationData locationData = (LocationData) question.reply.replyData;
									System.out.println("Location details: " + locationData.lat + " :: " + locationData.lng);
								}
								break;
							case CHECKLIST:
								if(question.reply.replyData instanceof CheckListData){
									CheckListData checkListData = (CheckListData) question.reply.replyData;
									System.out.println("selected option uuids");
									for(String optionUUID : checkListData.option_uuid){
										System.out.println(optionUUID);
									}
								}
								break;
							case FILE:
							case IMAGE:
							case SIGNATURE:
								if(question.reply.replyData instanceof FileData){
									FileData fileData = (FileData) question.reply.replyData;
									fileData.assetInfo[0].downloadAttachment(new File(fileData.assetInfo[0].file_name));
								}
								break;
							case IMAGE_GROUP:
								if(question.reply.replyData instanceof ImageGroupData){
									ImageGroupData imageGroupData = (ImageGroupData) question.reply.replyData;
									System.out.println("Number of images attached: " + imageGroupData.fileNames.length);
								}
								break;
							
							case PICKLIST:
							case POLL:
								if(question.reply.replyData instanceof OptionData){
									OptionData optionData = (OptionData) question.reply.replyData;
									System.out.println("selected option uuid : "+ optionData.option_uuid);
									
								}
								break;
							}
							System.out.println();
						}
						
						
					}

					if(content != null){
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
					}
					else{
						avaamo.sendMessage("Awesome. It works!. \nType one of the following to see them in action. \nimage \nfile \ncard", cuuid);
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
			
			@Override
			public void handleActivity(Activity activity) {
				System.out.println("Incoming activity from user : " + activity.user.firstName + " " + activity.user.lastName );
			}
		});


		// Keep this process running
		Thread.sleep(60000000);
	}

}
