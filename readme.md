# Avaamo Java Bot SDK

Before start developing a bot you should first create a bot in the Avaamo Dashboard.
Follow the steps in this [Getting Started](https://github.com/avaamo/java/wiki) page to create a bot in the dashboard.

#### Download and add library
Avaamo Java Bot SDK is a single jar file, avaamo.jar.

The java sdk requires the following 3 libraries as dependencies.

* javaee-api-7.0.jar 
* javax.json-1.0.4.jar
* tyrus-standalone-client-1.12.jar

[ Download ](https://github.com/avaamo/java/blob/master/avaamo_java_1.0.zip?raw=true) SDK + Dependencies

#### Sample Bot

This [file](https://github.com/avaamo/java/blob/master/SampleBot.java?raw=true) has the full example referred in this page.

#### Receiving Messages

Import Avaamo java package.
```java
import com.avaamo.java.Avaamo;

```
Initialize the library with your BOT UUID and Access Token.

```java
avaamo = new Avaamo(<YOUR-BOT-UUID>, <YOUR-BOT-ACCESS-TOKEN>);

avaamo.addEventHandler(new Avaamo.MessageHandler() {
	@Override
	public void handleMessage(IncomingMessageModel message) {
		try{
			String content = message.message.content;
			System.out.println("\n==> "+message.user.firstName+": "+ content);					
			
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

```
#### Sending Messages

```java
// message is the JSON string
avaamo.sendMessage(message)
```

#### Sending an image

```java
Image image = new Image(new File("test_image.jpg"));
image.setCaption("This is the image caption");
avaamo.sendImage(image, cuuid);
```
![image](screenshots/image.png)

#### Sending a file

```java
FileAttachment fileAttachment = new FileAttachment(new File("TestFile.txt"));
avaamo.sendFileAttachment(fileAttachment, cuuid);
```
![image](screenshots/file.png)

#### Sending a card

```java
CardAttachment cardAttachment = new CardAttachment();
cardAttachment.setTitle("Card Title");
cardAttachment.setDescription("Card Description. This has minimal rich text capabilities as well. For example <b>Bold</b> <i>Italics</i>");
cardAttachment.addLink(new CardAttachment.WebpageCardLink("Web URL", "http://www.avaamo.com"));
cardAttachment.addLink(new CardAttachment.SendMessageDeeplink("Post a Message", "Sample Action"));
cardAttachment.addLink(new CardAttachment.SendFormToConversationDeeplink("Open a Form", "63c906c3-553e-9680-c273-28d1e54da050", "Say Yes", null));
cardAttachment.setShowcaseImage(new File("test_image.jpg"));
avaamo.sendCardAttachment(cardAttachment, cuuid);
```
![image](screenshots/card.png)

