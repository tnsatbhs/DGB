package dgb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class Application {

	public static ConfigurableApplicationContext ctx;

	private static Integer port;

	public static void main(String[] args) throws Exception
	{
		Application.ctx = SpringApplication.run(Application.class, args);
		Application.port = Integer.parseInt(ctx.getEnvironment().getProperty("local.server.port"));

		// This needs to go in a unit test...
		System.out.println(Application.isThisServer("127.0.0.1", 8080));
		System.out.println(Application.isThisServer("127.0.0.1", 8090));
		System.out.println(Application.isThisServer("192.168.1.38", 8080));
		System.out.println(Application.isThisServer("192.168.1.38", 8090));
	}


	/**
	 * Given a host name and a port, see if it maps to this server instance.
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
