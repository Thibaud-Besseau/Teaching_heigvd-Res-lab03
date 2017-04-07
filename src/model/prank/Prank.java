package model.prank;

import model.mail.Message;
import model.mail.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thibaud Besseau on 06-Apr-17.
 */
public class Prank
{
	private Person victimSender;
	private List<Person> victimsRecipients = new ArrayList<>();
	private List<Person> witnessesRecipients = new ArrayList<>();
	private String message;

	public Message generateMailMessage()
	{
		//create a empty message
		Message msg = new Message();
	    //add the body
		msg.setBody(message + "\r\n" + victimSender.getLastName() + " "+victimSender.getFirstName());

        //store all the emails address to prank
		int i = 0;
		String[] sendTo = new String[victimsRecipients.size()];
		for (Person victim : victimsRecipients)
		{
			sendTo[i++] = victim.getAddress() + " ";
		}
		msg.setTo(sendTo);

		//store all the emails address in copy for the prank
		i = 0;
		String[] cc = new String[witnessesRecipients.size()];
		for (Person witness : witnessesRecipients)
		{
			cc[i++] = witness.getAddress() + " ";
		}
		msg.setCc(cc);

		msg.setFrom(victimSender.getAddress());
		return msg;
	}


	public Person getVictimSender()
	{
		return victimSender;
	}

	public void setVictimSender(Person victimSender)
	{
		this.victimSender = victimSender;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void addVictimRecipients(List<Person> victims)
	{
		victimsRecipients.addAll(victims);
	}

	public void addWitnessRecipients(List<Person> witness)
	{
		witnessesRecipients.addAll(witness);
	}

	public List<Person> getVictimRecipients()
	{
		return new ArrayList<>(victimsRecipients);
	}

	public List<Person> getWitnessRecipients()
	{
		return new ArrayList<>(witnessesRecipients);
	}


}