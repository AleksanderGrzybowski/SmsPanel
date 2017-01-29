package panel;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This was copied straight from provider website.
 * WEBAPI
 * @author Łukasz Tarnogórski <lukasz at serwersms.pl>
 */
public class SerwerSMS {
    
    public String nadawca = "", //nazwa nadawcy wiadomosci (tylko full)
            wiadomosc = "", //tresc wiadomosci
            data=""; // planowana daty wysylki wiadomosci
    
    public int PL = 0, //wiadomosc z polskimi znakami (tylko full)
            flash = 0, //sms typu flash
            speed = 0;  //sms typu speed (tylko full)
    
    private StringBuilder param;
    
    /**
     * @param  login
     * @param  haslo
     * @param  test - tryb testowy
     * @throws Exception
     */
    public SerwerSMS(String login, String haslo, int test) throws Exception {
        this.param = new StringBuilder();
        
        if (login.isEmpty() || haslo.isEmpty())
            throw new Exception("Nie podano danych do logowania");
        
        this.param.append("login=").append(login);
        this.param.append("&haslo=").append(haslo);
        this.param.append("&test=").append((test == 1 ? 1 : 0));
    }
    
    /**
     * SPRAWDZ ILOSC DOSTEPNYCH WIADOMOSCI
     *
     * @param typ_konta - dodatkowe atrybuty
     * @throws IOException
     */
    public void sprawdz_sms(int typ_konta) throws IOException {
        this.param.append("&akcja=ilosc_sms");
        this.param.append("&pokaz_typ_konta=").append(typ_konta == 1 ? 1 : 0);
        
        wyslij(this.param);
    }
    
    /**
     * DODAJ NOWA NAZWE NADAWCY SMS
     *
     * @param nadawca - alfanumeryczna nazwa nadawcy maks. 11 znakow
     * @throws Exception
     * @throws IOException
     */
    public void dodaj_nadawce(String nadawca) throws Exception, IOException {
        
        if (nadawca.length() > 11) {
            throw new Exception("Nazwa nadawcy jest za długa");
        }
        
        Matcher m = Pattern.compile("^[a-zA-Z0-9\\.\\,\\-]+$").matcher(nadawca);
        if (!m.find()) {
            throw new Exception("Niedozwolone znaki w nazwie nadawcy");
        }
        
        this.param.append("&akcja=nazwa_nadawcy");
        this.param.append("&operacja=dodanie");
        this.param.append("&nazwa=").append(nadawca);
        
        wyslij(this.param);
    }
    
    /**
     * POBIERZ NAZWY NADAWCOW SMS
     *
     * @param pre - pobiera predefiniowane nazwy nadawcow
     * @throws IOException
     */
    public void nadawcy_lista(int pre) throws IOException {
        
        this.param.append("&akcja=nazwa_nadawcy");
        this.param.append("&operacja=lista");
        this.param.append("&predefiniowane=").append((pre == 1 ? 1: 0));
        
        wyslij(this.param);
    }
    
    /**
     *  POBIERZ PRZYCHODZACE SMS
     *
     * @param numer - numer lub numery telefonow
     * @param typ [1|2|3|4|5]
     *                                      1 - wiadomosci SMS
     *                                      2 - wiadomosci SMS z ND
     *                                      3 - wiadomosci SMS z NDI
     *                                      4 - wiadomosci PREMIUM SMS
     *                                      5 - MMS
     * @param odczyt - oznacz wiadomosc jako przeczytana
     * @param powiazane - dodatkowy atrybyt (tylko sms eco)
     * @throws IOException
     */
    public void pobierz_sms(String[] numer, int typ, int odczyt, int powiazane) throws IOException {
        
        if (numer.length > 0) {
            this.param.append("&numer=").append(numery(numer));
        }
        
        this.param.append("&akcja=sprawdz_odpowiedzi");
        this.param.append("&typ=").append(typ);
        this.param.append("&odczyt=").append((odczyt == 1 ? 1: 0));
        this.param.append("&pokaz_powiazanie=").append((powiazane == 1 ? 1: 0));
        
        wyslij(this.param);
    }
    
    /**
     * POBIERZ RAPORT DORZECZEN
     * wiadomosci SMS, MMS oraz VOICE
     *
     * @access public
     * @param sms_id - tablica identyfikatorow wiadomosci zwracane podczas wysylki
     * @param stan [1|2|3|4]
     *                                        0 - wiadomosci oczekujace na raport
     *                                        1 - wiadomosci doreczone
     *                                        2 - wiadomosci wyslane
     *                                        3 - wiadomosci niewyslane
     *                                        4 - wiadomosci zaplanowane
     * @throws IOException
     */
    public void sms_raport(String[] sms_id, int stan) throws IOException {
        String s;
        
        switch (stan) {
            case 0:
                s = "oczekiwanie";
                break;
            case 1:
                s = "doreczone";
                break;
            case 2:
                s = "wyslane";
                break;
            case 3:
                s = "niewyslane";
                break;
            case 4:
                s = "zaplanowane";
                break;
            default:
                s = "";
                break;
        }
        
        this.param.append("&akcja=sprawdz_sms");
        this.param.append("&stan=").append(s);
        
        if (sms_id.length > 0) {
            StringBuilder id = new StringBuilder();
            for (int a = 0; a < sms_id.length; a++) {
                id.append(",").append(sms_id[a]);
            }
            this.param.append("&smsid=").append(id.toString().substring(1));
        }
        
        wyslij(this.param);
    }
    
    /**
     * USUN ZAPLANOWANE WYSYLKI
     *
     * @param sms_id - identyfikatory wybranych wiadomosci
     * @throws IOException
     */
    public void usun_zaplanowane(String[] sms_id) throws IOException {
        if (sms_id.length > 0) {
            StringBuilder id = new StringBuilder();
            for (int a = 0; a < sms_id.length; a++) {
                id.append(",").append(sms_id[a]);
            }
            this.param.append("&akcja=usun_zaplanowane");
            this.param.append("&smsid=").append(id.toString().substring(1));
            wyslij(this.param);
        }
    }
    
    /**
     * WYSLIJ WIADOMOSC GLOSOWA
     * SMS VOICE
     *
     * @access public
     * @param odbiorcy - numer lub kilka numerow odbiorcow wiadomosci
     * @param id_pliku - identyfikator pliku
     * @throws Exception
     * @throws IOException
     */
    public void voice(String[] odbiorcy, String id_pliku) throws Exception, IOException {
        
        if (odbiorcy.length == 0) {
            throw new Exception("Nie podano odbiorców wiadomości");
        }
        if ("".equals(this.wiadomosc) && "".equals(id_pliku)) {
            throw new Exception("Nie podano treści wiadomości");
        }
        if (this.wiadomosc.length() > 160) {
            throw new Exception("Treść wiadomości jest za długa");
        }
        
        if (!this.data.isEmpty()) {
            this.param.append("&data_wysylki=").append(sprawdz_date(this.data));
        }
        
        this.param.append("&akcja=wyslij_sms");
        this.param.append("&glosowy=1");
        this.param.append("&numer=").append(numery(odbiorcy));
        
        
        if (id_pliku.isEmpty()) {
            this.param.append("&wiadomosc=").append(tresc(this.wiadomosc, 0));
        } else {
            this.param.append("&plikwav=").append(id_pliku);
        }
        
        wyslij(this.param);
    }
    
    /**
     * WYSLIJ WIADOMOSC MULTIMEDIALNA
     * MMS
     *
     * @access public
     * @param odbiorcy - numer lub kilka numerow odbiorcow wiadomosci
     * @param id_pliku - identyfikator pliku
     * @throws Exception
     * @throws IOException
     */
    public void mms(String[] odbiorcy, String id_pliku) throws Exception, IOException {
        
        if (odbiorcy.length == 0) {
            throw new Exception("Nie podano odbiorców wiadomości");
        }
        if (id_pliku.isEmpty()) {
            throw new Exception("Nie podano pliku do wysłania");
        }
        if (this.wiadomosc.length() > 40) {
            throw new Exception("Tytuł wiadomości jest za długi");
        }
        
        if (!this.data.isEmpty()) {
            this.param.append("&data_wysylki=").append(sprawdz_date(this.data));
        }
        
        this.param.append("&akcja=wyslij_sms");
        this.param.append("&mms=1");
        this.param.append("&plikmms=").append(id_pliku);
        this.param.append("&numer=").append(numery(odbiorcy));
        this.param.append("&wiadomosc=").append(tresc(this.wiadomosc, 0));
        
        wyslij(this.param);
    }
    
    /**
     *  WYSLIJ WIADOMOSC SMS
     *
     * @access public
     * @param odbiorcy - numer lub kilka numerow odbiorcow wiadomosci
     * @throws Exception
     * @throws IOException
     */
    public void sms(String[] odbiorcy) throws Exception, IOException {
        
        if(odbiorcy.length == 0) throw new Exception("Nie podano odbiorców wiadomości");
       if(this.wiadomosc.isEmpty()) throw new Exception("Nie podano treści wiadomości");
        
        if((this.PL == 1 && this.wiadomosc.length() > 268) || (this.PL == 0 && this.wiadomosc.length() > 612)) {
            throw new Exception ("Treść wiadomości jest za długa");
        }
        
        if(!this.nadawca.isEmpty()) {
            if(this.speed == 1) this.param.append("&speed=1");
            if(this.PL == 1) this.param.append("&pl=").append(this.PL);
            this.param.append("&nadawca=").append(this.nadawca);
        }
        
        if (!this.data.isEmpty()) {
            this.param.append("&data_wysylki=").append(sprawdz_date(this.data));
        }
        
        this.param.append("&wiadomosc=").append(tresc(this.wiadomosc, this.PL));
        this.param.append("&flash=").append(this.flash == 1 ? 1: 0);
        this.param.append("&akcja=wyslij_sms");
        this.param.append("&numer=").append(numery(odbiorcy));
        
        wyslij(this.param);
    }
    
    /**
     *  TRESC WIADOMOSCI
     *
     * @access private
     * @static
     * @param  tresc
     * @param  PL
     * @return String
     */
    static private String tresc(String tresc, int PL) {
        try {
            if(PL == 0) {
                String[] WE = {"ą", "Ą", "ę", "Ę", "ó", "Ó", "ś", "Ś", "ł", "Ł", "ż", "Ż", "ź", "Ź", "ć", "Ć", "ń", "Ń"};
                String[] WY = {"a", "A", "e", "E", "o", "O", "s", "S", "l", "L", "z", "Z", "z", "Z", "c", "C", "n", "N"};
                
                for(int a =0; a < WE.length; a++) tresc = tresc.replace(WE[a], WY[a]);
            }
            
            tresc = URLEncoder.encode(tresc, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        }
        
        return tresc.trim();
    }
    
    /**
     *  POPRAW NUMERY
     *
     * @access private
     * @static
     * @param numer
     * @return String
     */
    static private String numery(String[] numer) {
        StringBuilder nr = new StringBuilder();
        
        for (int a = 0; a < numer.length; a++) {
            numer[a] = numer[a].replaceAll("[^0-9]", "");
            
            if (numer[a].length() == 9 && !numer[a].startsWith("48")) {
                numer[a] = "48".concat(numer[a]);
            }
            if (numer[a].length() == 10 && numer[a].startsWith("0")) {
                numer[a] = "48".concat(numer[a].substring(1));
            }
            
            nr.append(",").append(numer[a]);
        }
        
        return nr.toString().substring(1);
    }
    
    /**
     *  PARSOWANIE DATY
     *
     * @param data
     * @return string
     * @throws Exception
     */
    static private String sprawdz_date(String data) throws Exception {
        String data_r = null;
        
        try {
            
            SimpleDateFormat f_data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date _data = f_data.parse(data);
            data_r = f_data.format(_data);
            
        } catch (ParseException ex) {
            throw new Exception("Nieprawidłowy format daty");
        }
        
        return data_r;
    }
    
    /**
     *  WYSLIJ ZAPYTANIE I POBIERZ ODPOWIEDZ
     *
     * @access private
     * @static
     * @param dane
     * @throws IOException
     */
    static private void wyslij(StringBuilder dane) throws IOException {
        
        URLConnection polacz =  new URL("https://api1.serwersms.pl/zdalnie/").openConnection();
        polacz.setReadTimeout(15000);
        polacz.setDoOutput(true);
        
        OutputStreamWriter wyslij = new OutputStreamWriter(polacz.getOutputStream());
        wyslij.write(dane.toString());
        wyslij.close();
        
        BufferedReader odpowiedz = new BufferedReader(new InputStreamReader(polacz.getInputStream()));
        String xml;
        while ((xml = odpowiedz.readLine()) != null) {
            System.out.println(xml);
        }
        
        odpowiedz.close();
        
        System.out.println("\n"+dane.toString()+"\n"+polacz.getReadTimeout()+" "+polacz.getConnectTimeout());
    }
}

