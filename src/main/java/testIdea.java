import com.sun.mail.pop3.POP3SSLStore;

import javax.mail.*;
import java.util.Properties;

public class testIdea {
    public static void main(String[] args) throws Exception{
        String host = args[0];
        String port = args[1];
        String username = args[2];
        String password = args[3];

        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3"); // 协议名称
        props.setProperty("mail.pop3.host", host);// POP3主机名
        props.setProperty("mail.pop3.port", port); // 端口号
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", String.valueOf(port));

        URLName url = new URLName("pop3", host, Integer.parseInt(port), "", username, password);
        Session session = Session.getInstance(props, null);

        Store store = new POP3SSLStore(session, url);
        store.connect();

        Folder folder = store.getFolder("INBOX");

        folder.open(Folder.READ_ONLY);

        System.out.println("Total messages: " + folder.getMessageCount());
        System.out.println("New messages: " + folder.getNewMessageCount());
        System.out.println("Unread messages: " + folder.getUnreadMessageCount());
        System.out.println("Deleted messages: " + folder.getDeletedMessageCount());

        Message[] messages = folder.getMessages();
        int count = 0;
        for (Message message : messages) {
            System.out.println("this is NO." + count + " mail start.");
            // 打印每一封邮件:
            while (true) {
                try {
                    System.out.println(message.getSubject());
                    Thread.sleep(1000);
                    break;
                }
                catch (Exception e) {
                    store.close();
                    store.connect();
                    folder.open(Folder.READ_ONLY);
                    Thread.sleep(2000);
                    System.out.println(e);
                }
            }

            System.out.println("this is NO." + count + " mail end.");
            count ++;
        }
        store.close();
    }
}
