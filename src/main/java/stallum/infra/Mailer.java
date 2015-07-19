package stallum.infra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.google.common.io.Resources;

public class Mailer {
	
	private Logger log = Logger.getLogger(Mailer.class);
	
	private String html;
	private String assunto;
	private Locale locale = new Locale("pt", "BR");
	
	public Mailer() {
	}
	
	public Mailer(Locale locale) {
		if (locale != null)
			this.locale = locale;
	}
	
	public void setLocale(Locale locale) {
		if (locale != null)
			this.locale = locale;
	}
	
	public void enviarEmail(String nomeTemplate, String[] parametros, String[] destinatarios) throws MessagingException {
		enviarEmail(nomeTemplate, parametros, destinatarios, null);
	}
	
	public void enviarEmail(String nomeTemplate, String[] parametros, String[] destinatarios, String[] responderPara) throws MessagingException {
		
		try {
			html = lerTemplate(nomeTemplate);
			for (int i = 0; i < parametros.length; i++)
				html = html.replace("{" + i + "}", parametros[i]);
			
			enviarEmail(destinatarios, responderPara);
		} catch (IOException e) {
			log.warn(e.getMessage());
			throw new MessagingException(e.getMessage());
		}
	}
	
	private void enviarEmail(String[] recipients, String[] replyTo) throws IOException, MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", Util.getConfig("config.smtp.host"));
        props.put("mail.smtp.port", Util.getConfig("config.smtp.port"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl", "true");  
        props.put("mail.smtp.ehlo", "true"); 
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        if (Boolean.valueOf(Util.getConfig("config.smtp.tls"))) {
	        props.put("mail.smtp.starttls.enable","true");
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.put("mail.smtp.socketFactory.port", Util.getConfig("config.smtp.port"));
        }

        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);

        session.setDebug(true);

        Message msg = new MimeMessage(session);
        msg.setHeader("Content-Transfer-Encoding",  "8bit");

        InternetAddress addressFrom = new InternetAddress(Util.getConfig("config.smtp.sender"));
        msg.setFrom(addressFrom);
        
        if (replyTo != null && replyTo.length > 0) {
        	Address[] addrs = new Address[replyTo.length];
        	for (int i = 0; i < replyTo.length; i++)
        		addrs[i] = new InternetAddress(replyTo[i]);
        	msg.setReplyTo(addrs);
        }

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        String conteudo = lerTemplate("templateEmail");
        conteudo = conteudo.toString().replace("{conteudo}", html);
       
        msg.setSubject(assunto);
        msg.setContent(conteudo, "text/html; charset=UTF-8");
        
        log.info(conteudo);
        
        if (Boolean.valueOf(Util.getConfig("config.emailAtivo")))
        	Transport.send(msg);

    }
	
	public String lerTemplate(String nomeTemplate) throws IOException {
		
		URL urlTemplate = null;
		try {
			urlTemplate = Resources.getResource("email/" + nomeTemplate + "-" + locale.getLanguage() + "_" + locale.getCountry() + ".html");
		} catch (IllegalArgumentException e) {
			log.warn(e.getMessage());
			urlTemplate = Resources.getResource("email/" + nomeTemplate + ".html");
		}

		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder conteudo = new StringBuilder();
		
		try {
			
			fr = new FileReader(urlTemplate.getFile());
			br = new BufferedReader(fr);
	        
	        String line = br.readLine();
	        while (line != null) {
	        	if (line.startsWith("<assunto>"))
	        		assunto = line.replace("<assunto>", "").replace("</assunto>", "");
	        	else
	        		conteudo.append(line);
	        	line = br.readLine();
	        }
	        
		} catch (Exception e) {
			log.warn(e.getMessage());
		} finally {
			if (br != null)
				br.close();
			if (fr != null)
				fr.close();
		}
		
        return conteudo.toString();
		
	}

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = Util.getConfig("config.smtp.user");
            String password = Util.getConfig("config.smtp.pass");
            return new PasswordAuthentication(username, password);
        }
    }

}
