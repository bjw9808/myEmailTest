import java.util.Date;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class EmailImapFetch {
    public static void main(String[] args) throws Exception {
        System.out.println("My mail application");
        String host = "imap.139.com";
        String password = "";
        String username = "15102957670@139.com";

        Properties props = new Properties();
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.port","993");
        props.setProperty("mail.store.protocol","imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", "993");
        props.setProperty("mail.imap.auth.login.disable", "true");

        Session session = Session.getDefaultInstance(props,null);
        session.setDebug(false);
        IMAPFolder folder= null;
        IMAPStore store=null;
        store=(IMAPStore)session.getStore("imap");
        store.connect(host,993,username,password);
        Folder defaultFolder = store.getDefaultFolder();
        Folder[] allFolder = defaultFolder.list();
        for (Folder value : allFolder) {
            System.out.println("文件夹 = " + value.getFullName());
            folder = (IMAPFolder)store.getFolder(value.getFullName());
            folder.open(Folder.READ_ONLY);
            int size = folder.getMessageCount();
            System.out.println("数目： "+size);
            Message[] mess=folder.getMessages();
            int count = 1;
            int retry_count = 0;
            for (Message msg: mess) {
                Thread.sleep(1000);
                while (retry_count <= 20) {
                    try {
                        if (count == size) {
                            break;
                        }
                        System.out.println("This is NO." + count + ":");
                        String from = mess[count].getFrom()[0].toString();
                        String subject = mess[count].getSubject();
                        Date date = mess[count].getSentDate();
                        System.out.println("Subject: " + subject);
                        count ++;
                        break;
                    }
                    catch (Exception e) {
                        Thread.sleep(2000);
                        retry_count = retry_count + 1;
                        System.out.println(e);
                        System.out.println("get mail error, error mail NO." + count +" ,retry count is: " + retry_count);
                    }
                }
                retry_count = 0;
            }
        }
    }
}
