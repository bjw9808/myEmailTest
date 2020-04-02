import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailImapFetch {

    private static Log logger = LogFactory.getLog(EmailImapFetch.class);

    public static void main(String[] args) throws Exception {
        EmailImapFetch.databaseOperate("mail", "", "292215");
        logger.debug("The IMAP catcher plug-in unit start.");
        logger.debug("Args params: " + Arrays.toString(args));

        // 传入参数：4个
        String host = args[0]; //目标邮件服务器，例：imap.139.com
        String port = args[1]; // 端口，例：993
        String username = args[2]; //邮箱的用户名，例：xxxxx@139.com
        String password = args[3]; //邮箱密码，例：xxxxxx

        Properties props = new Properties();
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.port", port);
        props.setProperty("mail.store.protocol","imap");
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", port);
        props.setProperty("mail.imap.auth.login.disable", "true");
        logger.debug("Properties paras: " + props);

        Session session = Session.getDefaultInstance(props,null);
        session.setDebug(false);
        IMAPFolder folder= null;
        IMAPStore store=null;
        store=(IMAPStore)session.getStore("imap");
        store.connect(host,Integer.parseInt(port),username,password);
        Folder defaultFolder = store.getDefaultFolder();
        Folder[] allFolder = defaultFolder.list();
        for (Folder value : allFolder) {
            logger.debug("Mail Folder : " + value.getFullName());
            folder = (IMAPFolder)store.getFolder(value.getFullName());
            folder.open(Folder.READ_ONLY);
            int size = folder.getMessageCount();
            logger.debug("The Folder counts："+size);
            Message[] mess=folder.getMessages();
            int count = 1;
            int retry_count = 0;
            for (Message msg: mess) {
                while (retry_count <= 20) {
                    try {
                        if (count == size) {
                            break;
                        }
                        logger.debug("This is NO." + count + " mail, " + "Subject: " + mess[count].getSubject());
                        break;
                    }
                    catch (Exception e) {
                        retry_count = retry_count + 1;
                        logger.debug("get mail error, error mail NO." + count +" ,retry count is: " + retry_count + "error msg: " + e);
                    }
                }
                retry_count = 0;
                count ++;
            }
        }
    }

    public static void databaseOperate(String DB_NAME, String JDBC_USER, String JDBC_PASSWORD) throws Exception {
        String JDBC_URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?serverTimezone=UTC";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery("SELECT id, grade, name, gender FROM students WHERE gender=\'M\'")) {
                    while (rs.next()) {
                        long id = rs.getLong(1); // 注意：索引从1开始
                        long grade = rs.getLong(2);
                        String name = rs.getString(3);
                        String gender = rs.getString(4);
                        System.out.println(id);
                        System.out.println(gender);
                        System.out.println(name);
                        System.out.println(grade);
                    }
                }
            }
        }
    }
}
