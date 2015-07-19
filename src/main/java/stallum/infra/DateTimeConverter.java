package stallum.infra;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import br.com.caelum.vraptor.Convert;
import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.converter.ConversionError;

@Convert(Date.class)  
public class DateTimeConverter implements Converter<Date> {  
  
	public Date convert(String value, Class<? extends Date> type, ResourceBundle bundle) {
    	
    	if (value == null || value.trim().isEmpty()) {
			return null;
		}

		SimpleDateFormat fmt;
		if (value.trim().length() > 10) {			
			fmt = new SimpleDateFormat(bundle.getString("formato.dataHora"));
		} else if (value.trim().length() > 5) {
			fmt = new SimpleDateFormat(bundle.getString("formato.data"));
		} else {
			fmt = new SimpleDateFormat(bundle.getString("formato.hora"));
		}
		
		try {
			Date data = fmt.parse(value);
			return data;
		} catch (ParseException e) {
			throw new ConversionError(MessageFormat.format(bundle.getString("invalida"), value));
		}
		
    }  
}  