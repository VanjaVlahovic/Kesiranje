import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KesiraniObjekat {
	String host;
	String url;
	String odgovor="";
	
	public KesiraniObjekat(String url, String host) {
		this.url=url;
		this.host=host;
	}
	public KesiraniObjekat(int n) {
		load(n);
	}
	public void dodajLinijuOdgovora(String s) {
		odgovor += s+ '\n';
	}
	public void save (int n) {
		
		try {
			FileWriter fajlUpis = new FileWriter(ProxySaKesiranjem.kesFolder + n + ".txt");
			fajlUpis.write(url + '\n');
			fajlUpis.write(host + '\n');
			fajlUpis.write(odgovor + '\n');
			fajlUpis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void load (int n) {
		try {
			BufferedReader ulazniTokIzFajla = new BufferedReader(new FileReader(ProxySaKesiranjem.kesFolder + n + ".txt"));
			url = ulazniTokIzFajla.readLine();
			host = ulazniTokIzFajla.readLine();
			String s;
			while(true) {
				s=ulazniTokIzFajla.readLine();
				if(s==null || s.equals(""))
					break;
				dodajLinijuOdgovora(s);
			}
			ulazniTokIzFajla.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof KesiraniObjekat) {
			KesiraniObjekat kesiraniObjekat = (KesiraniObjekat) arg0;
			return kesiraniObjekat.host.equals(host) && kesiraniObjekat.url.equals(url);
		}
		return false;
	}
	public String getHost() {
		return host;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getOdgovor() {
		return odgovor;
	}
	
	
}
