# Avaamo Java Bot SDK

#### Download and add library
Avaamo Java Bot SDK is a single jar file. You can download it here

The java sdk requires the following 3 libraries as dependencies.

* javaee-api-7.0.jar [ Download ](https://github.com/jbt/markdown-editor)
* javax.json-1.0.4.jar
* tyrus-standalone-client-1.12.jar

#### Receiving Messages

Import Avaamo java package.
```java
import com.avaamo.java.Avaamo;

```
Initialize the library with your BOT UUID and Access Token.

```java
avaamo = new Avaamo(<YOUR-BOT-UUID>, <YOUR-BOT-ACCESS-TOKEN>);

avaamo.addEventHandler(new Avaamo.EventHandler() {
public void handleEvent(String message) {
try{
if (message.contains("phx_reply")){
System.out.println("==> response: "+ message );

}else if (message.contains("read_ack")){
System.out.println("==> read_ack: "+message);
}
else{
System.out.println("==> message: "+message);
}
}catch (Exception error){
System.err.println("Error "+ error.getMessage());
}

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
// message is the JSON string
avaamo.sendMessage(message)
```
![image](image.jpg)
