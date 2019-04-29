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

	public static String secondary_host;
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
			}
		}
		if (Application.secondary_host != null) {
			System.out.println("Detected secondary host as " + Application.secondary_host);
		} else {
			throw new Exception("You must specify secondary_host as an argument. Example: secondary_host=\"127.0.0.1:8090\"");
		}

		System.out.println("Running on " + Application.port);
	}


	/**
	 * This might be used if we allowed the user to specify a generic secondary server
	 * hostname and we needed name resolution.
	 * @return This instance's host ip and port.
	 */
	public static ArrayList<String> getKnownHostnames () {
		if (Application.knownNames != null) {
			return Application.knownNames;
		}
		HashSet<String> names = new HashSet<String>();
		ArrayList<String> knownNames = new ArrayList<String>();

		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			String hostname = addr.getHostName();

			if (hostname != "") {
				names.add(hostname);
			}
			String canonicalHostname = addr.getCanonicalHostName();
			if (canonicalHostname != "") {
				names.add(canonicalHostname);
			}
			for (InetAddress a : InetAddress.getAllByName(canonicalHostname != "" ? canonicalHostname : hostname)) {
				names.add(a.getHostAddress());
			}

			addr = InetAddress.getLoopbackAddress();
			hostname = addr.getCanonicalHostName();
			if (hostname != "") {
				names.add(hostname);
			}
			hostname = addr.getHostName();
			if (hostname != "") {
				names.add(hostname);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String n : names) {
			knownNames.add(n);
		}
		Application.knownNames = knownNames;
		return knownNames;
	}


	/**
	 * Given a host name and a port, see if it maps to this server instance.
	 * This is not needed if we are hardcoding the primary/secondary flag.
	 * @param host
	 * @param port
	 * @return boolean
	 */
	public static boolean isThisServer(String host, Integer port) {
		NetworkInterface ni = null;
		InetAddress addr = null;

		try {
			addr = InetAddress.getByName(host);
			ni = NetworkInterface.getByInetAddress(addr);
		} catch (SocketException e) {
			System.err.println("Unable to getByInetAddress");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.err.println("Unable to get InetAddress.getByName: " + host);
			e.printStackTrace();
		}

		if (ni != null) {
			//System.out.println(ni.getDisplayName());
			if (port.equals(Application.port)) {
				return true;
			}
		}
		return false;
	}

}
