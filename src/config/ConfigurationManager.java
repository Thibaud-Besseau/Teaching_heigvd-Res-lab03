package config;

import model.mail.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by thibaud besseau on 07.04.2017.
 */
public class ConfigurationManager
{
	public ConfigurationManager() throws IOException
	{

		//load the file with all the param to run the program
		victims = LoadAddressesFromFile("./victims.utf8");
		messages = LoadMessagesFromFile("./messages.utf8");
		LoadProperties("./config.properties");
	}

	public List<Person> getVictims()
	{
		return new ArrayList<>(victims);
	}

	public List<Person> getWithnessesToCC()
	{
		return new ArrayList<>(withnessesToCC);
	}

	public List<String> getMessages()
	{
		return new ArrayList<>(messages);
	}

	public int getNumberOfGroups()
	{
		return numberOfGroups;
	}

	private void LoadProperties(String filename) throws IOException
	{

		FileInputStream fis = new FileInputStream(filename);
		Properties properties = new Properties();
		properties.load(fis);
		this.withnessesToCC = new ArrayList<>();
		String witnesses = properties.getProperty("withnessesToCC");
		String[] witnessesAddresses = witnesses.split(",");
		for (String address : witnessesAddresses)
		{
			this.withnessesToCC.add(new Person(address));
		}
		numberOfGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));
	}

	private List<Person> LoadAddressesFromFile(String filename) throws IOException
	{
		List<Person> result = new ArrayList<>();
		FileInputStream fis = new FileInputStream(filename);
		InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
		BufferedReader reader = new BufferedReader(isr);

		try
		{
			String address = reader.readLine();
			//for each address the program create a person
			while (address != null)
			{
				result.add(new Person(address));
				address = reader.readLine();
			}
		} catch (IOException ex)
		{
			//error cannot open the file
			LOG.log(Level.SEVERE, null, ex);
		}

		return result;
	}

	private List<String> LoadMessagesFromFile(String filename) throws IOException
	{

		List<String> result = new ArrayList<>();
		FileInputStream fis = new FileInputStream(filename);

		//test if we can open correctly
		try
		{
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader reader = new BufferedReader(isr);
			//test if we can read
			try
			{
				String line = reader.readLine();
				while (line != null)
				{
					StringBuilder body = new StringBuilder();

					//read a message . The signal for the end of a message is ==
					while ((line != null) && (!line.equals("==")))
					{
						body.append(line);
						body.append("\r\n");
						line = reader.readLine();
					}
					result.add(body.toString());
					line = reader.readLine();
				}

			} catch (IOException ex)
			{
				LOG.log(Level.SEVERE, null, ex);
			}

		} catch (IOException ex)
		{
			LOG.log(Level.SEVERE, null, ex);
		}
		return result;
	}

	private static final Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());

	private final List<Person> victims;   //receivers of pranks
	private final List<String> messages;  //messages to send
	private int numberOfGroups;           //number of group receiver
	private List<Person> withnessesToCC;  //receiver withnesses of pranks
}
