import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ProxySaKesiranjem extends Thread{
	Socket klijentSoket;
	static String kesFolder="Cached/";
	static ArrayList<KesiraniObjekat> listaKesiranihObjekata = new ArrayList<KesiraniObjekat>();
	static Object lock = new Object();
	
	public ProxySaKesiranjem(Socket klijentSoket) {
		this.klijentSoket=klijentSoket;
		start();
	}
	
	public static void ucitajKesSaDiska() {
		int brojFajla = 0;
		System.out.println("Ucitavam kes: ");
		while(true) {
			File fajl = new File(kesFolder + brojFajla + ".txt");
			if (!fajl.exists())
				break;
			KesiraniObjekat ko = new KesiraniObjekat(brojFajla);
			listaKesiranihObjekata.add(ko);
			System.out.println(brojFajla +": "+ko.getHost() + ko.getUrl());
			brojFajla++;
		}
	}
	
	public static void main(String[] args) throws Exception{
		try {
			if (args.length>0) {
				kesFolder = args[0];
				if (!kesFolder.endsWith("/")) {
					kesFolder = kesFolder + "/";
				}
			}
			ucitajKesSaDiska();
			ServerSocket serverSoket  = new ServerSocket(8080);
			System.out.println("Proxy poceo sa radom na portu 8080");
			while(true) {
				new ProxySaKesiranjem(serverSoket.accept());
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			BufferedReader ulazniTokOdKlijenta = new BufferedReader(new InputStreamReader(klijentSoket.getInputStream()));
			PrintStream izlazniTokKaKlijentu = new PrintStream(klijentSoket.getOutputStream());
			String linijaTeksta = ulazniTokOdKlijenta.readLine();
			String[] zahtev = linijaTeksta.split(" ");
			if (zahtev.length!=3) {
				System.out.println("Lose zaglavlje: "+linijaTeksta);
				klijentSoket.close();
				return;
			}
			String zaglavlje = "";
			String host = null;
			int port = 80;
			while(true) {
				linijaTeksta = ulazniTokOdKlijenta.readLine();
				if (linijaTeksta.equals(""))
					break;
				if(linijaTeksta.startsWith("Host"))
					host=linijaTeksta.substring(linijaTeksta.indexOf(' ')+1);
				if (host.indexOf(':')>0) {
					String[] tmp = host.split(":");
					host = tmp[0];
					port = Integer.parseInt(tmp[1]);
				}
				if (!linijaTeksta.startsWith("Proxy") && (!linijaTeksta.startsWith("Pragma")) && (!linijaTeksta.startsWith("Cache")))
					zaglavlje=zaglavlje+linijaTeksta+'\n';
			}
			if(zahtev[1].startsWith("http://"))
				zahtev[1]=zahtev[1].substring(7 +host.length());
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
