package smtp;

import model.mail.Message;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by thibaud Besseau on 06-Apr-17.
 */
public class SmtpClient implements ISmtpClient
{
	public SmtpClient(String serverAddress, int smtpServerPort)
	{
		this.serverAddress = serverAddress;
		this.serverPort = smtpServerPort;
	}

	public void sendMessage(Message message) throws IOException
	{
		LOG.info("Sending message");
		//server connexion
		Socket socket = new Socket(serverAddress, serverPort);

		//input and output for the client
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

		String line = reader.readLine();
		LOG.info("Server " + line);


		//start the server connexion
		writer.printf("EHLO " + serverAddress + " \r\n");
		writer.flush();
		LOG.info("EHLO" + serverAddress);

		//reading the server answer
		line = reader.readLine();
		System.out.println("Server : " + line);

		//if the serveur communication doens't start correctly
		if (!line.startsWith("250"))
		{
			LOG.info("Error server line:" + line);
			throw new IOException("Error server line : " + line);
		}

		//wait the end of 250 param
		while (line.startsWith("250-"))
		{
			line = reader.readLine();
			LOG.info("Server : " + line);
		}

		//send sender infomation to the server
		writer.printf("MAIL FROM:" + message.getFrom() + "\r\n");
		writer.flush();
		LOG.info("MAIL FROM:" + message.getFrom());

		//reading the server answer
		line = reader.readLine();
		LOG.info("Server : " + line);

		//send to
		for (String to : message.getTo())
		{
			writer.printf("RCPT TO:" + to + "\r\n");
			writer.flush();
			LOG.info("RCPT TO:" + to);

			//reading the server answer
			line = reader.readLine();
			LOG.info("Server : " + line);
		}

		//send cc
		for (String cc : message.getCc())
		{
			writer.printf("RCPT TO:" + cc + "\r\n");
			writer.flush();
			LOG.info("RCPT TO:" + cc);

			//reading the server answer
			line = reader.readLine();
			LOG.info("Server : " + line);
		}

		//send Bcc
		for (String bcc : message.getBcc())
		{
			writer.printf("RCPT TO:" + bcc + "\r\n");
			writer.flush();
			LOG.info("RCPT TO:" + bcc);

			//reading the server answer
			line = reader.readLine();
			LOG.info("Server : " + line);
		}

		//send build email
		writer.printf("DATA \r\n");
		writer.flush();
		LOG.info("DATA");

		//reading the server answer
		line = reader.readLine();
		LOG.info("Server : " + line);

		writer.printf("From: " + message.getFrom() + "\r\n");
		LOG.info("From: " + message.getFrom());

		writer.printf("To: " + message.getTo()[0]);
		for (int i = 1; i < message.getTo().length; ++i)
		{
			writer.printf("," + message.getTo()[i]);
		}
		writer.printf("\r\n");

		writer.printf("Cc: " + message.getCc()[0]);
		for (int i = 1; i < message.getCc().length; ++i)
		{
			writer.printf("," + message.getCc()[i]);
		}
		writer.printf("\r\n");
		writer.printf(message.getBody());

		//close the email
		writer.printf("\r\n");
		writer.printf(".\r\n");
		LOG.info(".");
		writer.flush();

		//reading the server answer
		line = reader.readLine();
		LOG.info("Server : " + line);

		//close the communication with the serveur
		writer.printf("quit \r\n");
		writer.flush();
		LOG.info("quit \r\n");

		writer.close();
		reader.close();
		socket.close();
	}

	private static final Logger LOG = Logger.getLogger(SmtpClient.class.getName());
	private String serverAddress;
	private int serverPort;

	private PrintWriter writer;
	private BufferedReader reader;
}
