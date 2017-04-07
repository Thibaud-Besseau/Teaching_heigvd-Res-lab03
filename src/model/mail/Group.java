package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiabud Besseau on 06-Apr-17.
 */
public class Group
{
	private final List<Person> members = new ArrayList<>();

	public void addMember(Person person)
	{
		members.add(person);
	}

	public List<Person> getMembers()
	{
		return new ArrayList<>(members);
	}
}
