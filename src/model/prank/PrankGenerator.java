package model.prank;

import config.ConfigurationManager;
import model.mail.Group;
import model.mail.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by thibaud Besseau on 06-Apr-17.
 */
public class PrankGenerator
{
	private ConfigurationManager configurationManager;
	private static final Logger LOG = Logger.getLogger(PrankGenerator.class.getName());

	public PrankGenerator(ConfigurationManager configurationManager)
	{
		this.configurationManager = configurationManager;
	}


	public List<Prank> generatePranks()
	{

		List<Prank> listOfPranks = new ArrayList<>();

		//store all parameters form configmanager
		List<String> messages = configurationManager.getMessages();
		int numberOfGroups = configurationManager.getNumberOfGroups();
		int numberOfVictims = configurationManager.getVictims().size();
		int messageToChoose = 0;                                    //to choice message of Prank

		//test if we have enought victims
		if ((numberOfVictims / numberOfGroups) < 3)
		{
			numberOfGroups = numberOfVictims / 3;
			LOG.warning("There are not enought vicitims for this number of group");
		}

		//create a group of victims
		List<Group> groups = generateGroups(configurationManager.getVictims(), numberOfGroups);
		for (Group group : groups)
		{
			//we create a new Prank
			Prank prank = new Prank();
			//obtain the list of victim
			List<Person> victims = group.getMembers();
			Collections.shuffle(victims);
			Person sender = victims.remove(0);
			//the first victim is the sender
			prank.setVictimSender(sender);
			prank.addVictimRecipients(victims);
			//put in cc the addresses set in the config file
			prank.addWitnessRecipients(configurationManager.getWithnessesToCC());

			//randomly choose the message to send
			String message = messages.get(messageToChoose);
			messageToChoose = (messageToChoose + 1) % messages.size();

			//add the result in the prank list
			prank.setMessage(message);
			listOfPranks.add(prank);
		}
		return listOfPranks;

	}

	public List<Group> generateGroups(List<Person> victims, int numberOfGroups)
	{

		List<Person> listOfVictims = new ArrayList(victims);
		Collections.shuffle(listOfVictims);
		List<Group> groups = new ArrayList<>();

		//create a list of group
		for (int i = 0; i < numberOfGroups; ++i)
		{
			Group group = new Group();
			groups.add(group);
		}
		int numberGroup = 0;
		Group group;
		while (listOfVictims.size() > 0)
		{
			group = groups.get(numberGroup);
			//choose the next group
			numberGroup = (numberGroup + 1) % groups.size();
			Person victim = listOfVictims.remove(0);
			//add victim in a group
			group.addMember(victim);
		}
		return groups;
	}
}
