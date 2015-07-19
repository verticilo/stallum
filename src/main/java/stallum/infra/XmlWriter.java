package stallum.infra;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class XmlWriter {

	private String nomeArquivo;
	private BufferedWriter out;
	private TransformerHandler hd;
	private AttributesImpl atts;
	
	public XmlWriter(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public void abre() throws IOException, TransformerConfigurationException, SAXException {
		
		atts = new AttributesImpl();
		FileWriter fw = new FileWriter(Util.getConfig("config.dir.relatorios") + System.getProperty("file.separator") + nomeArquivo + ".xml");
		out = new BufferedWriter(fw);
		StreamResult streamResult = new StreamResult(out);
		SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		hd = tf.newTransformerHandler();
		Transformer serializer = hd.getTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes"); // teste
		hd.setResult(streamResult);
		hd.startDocument();
		
		hd.startElement("", "", "XML", atts);
		
	}
	
	public void fecha() throws SAXException, IOException {

		hd.endElement("", "", "XML");
		hd.endDocument();
		out.flush();
		out.close();
		
	}
	
	public void novoElemento(String tag, String valor) throws SAXException {
		try {
		hd.startElement("", "", tag, atts);
		hd.characters(valor.toCharArray(), 0, valor.length());
		hd.endElement("", "", tag);
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	
	}
	
	public void abreTag(String tag) throws SAXException {
		hd.startElement("", "", tag, atts);
	}
	
	public void fechaTag(String tag) throws SAXException {
		hd.endElement("", "", tag);
	}
	
	
}
