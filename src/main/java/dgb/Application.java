package dgb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class Application {

	private static Integer port;
	private static ArrayList<String> knownNames = null;

	public static String secondary_host, this_host;
	public static ConfigurableApplicationContext ctx;

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception
	{
		Application.ctx = SpringApplication.run(Application.class, args);
		Application.port = Integer.parseInt(ctx.getEnvironment().getProperty("local.server.port"));

		// TODO: use a real java command line arg parser
		for (String arg : args) {
			String[] parts = arg.split("=");
			if (parts.length == 2) {
				if (parts[0].equals("secondary_host")) {
					Application.secondary_host = parts[1];
				}
				else if (parts[0].equals("this_host")) {
					Application.this_host = parts[1];
				}
			}
		}
		if (Application.secondary_host != null) {
			System.out.println("Detected secondary host as " + Application.secondary_host);
		} else {
			throw new Exception("You must specify secondary_host as an argument. Example: secondary_host=\"127.0.0.1:8090\"");
		}
		if (Application.this_host != null) {
			System.out.println("Detected this host as " + Application.this_host);
		} else {
			throw new Exception("You must specify this_host as an argument. Example: this_host=\"127.0.0.1:8080\"");
		}

		System.out.println("Running on " + Application.port);
	}

}
