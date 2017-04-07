package smtp;

/**
 * Created by thiba on 06-Apr-17.
 */
import model.mail.Message;
import java.io.IOException;
import java.net.InetAddress;

public interface ISmtpClient {

    public void sendMessage(Message message) throws IOException;

}