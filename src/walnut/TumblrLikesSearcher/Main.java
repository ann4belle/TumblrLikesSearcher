package walnut.TumblrLikesSearcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CancellationException;

import com.github.scribejava.apis.TumblrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

public class Main {

	static String request_url = "https://www.tumblr.com/oauth/request_token";
	static String access_url = "https://www.tumblr.com/oauth/access_token";
	static String auth_url = "https://www.tumblr.com/oauth/authorize";

	public static void main(String[] args) throws Exception {
		ServerSocket listener = new ServerSocket(new Random().nextInt((65535 - 8000) + 1) + 8000);
		OAuth10aService authserv = new ServiceBuilder("INSERT API KEY HERE YOU IDIOT")
				.apiSecret("INSERT API SECRET HERE DIPSHIT")
				.callback("http://localhost:" + Integer.toString(listener.getLocalPort())).build(TumblrApi.instance());
		final OAuth1RequestToken requestToken = authserv.getRequestToken();
		System.out.println(authserv.getAuthorizationUrl(requestToken));
		Socket s = listener.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		PrintWriter out = new PrintWriter(s.getOutputStream());
		String line = in.readLine();
		out.print("HTTP/1.1 200 \r\n");
		out.print("Content-Type: text/plain\r\n");
		out.print("Connection: close\r\n");
		out.print("\r\n");
		if (!line.contains("oauth_verifier"))
		{
			
			throw new CancellationException(
					"You seem to have denied me access to your account. Without access, I can't read your likes.");
		}
	}
}
