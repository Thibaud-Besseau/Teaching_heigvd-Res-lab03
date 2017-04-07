import config.ConfigurationManager;
import model.prank.Prank;
import model.prank.PrankGenerator;
import smtp.SmtpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MailRobot {

	public static void main(String[] args) {

		//connect localhost server on 2525 port
		SmtpClient smtp = new SmtpClient("localhost", 2525);
		try {

			//use all informations contain in configuration files
			ConfigurationManager manager = new ConfigurationManager();
			PrankGenerator prankGenerator = new PrankGenerator(manager);
			//start prank
			List<Prank> pranks = new ArrayList<>(prankGenerator.generatePranks());
			for(Prank prank: pranks){
				smtp.sendMessage(prank.generateMailMessage());
			}

		} catch (IOException ex) {
			System.out.println("ERREUR " + ex);
		}
	}
}