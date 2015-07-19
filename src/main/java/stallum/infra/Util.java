package stallum.infra;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.validator.routines.UrlValidator;

public class Util {

	private static ResourceBundle messageBundle;
	private static ResourceBundle configBundle;

	static {
		messageBundle = ResourceBundle.getBundle("messages");
		configBundle = ResourceBundle.getBundle("config");
	}

	public static String encripta(String senha) {

		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(senha.getBytes());
			byte[] hash = md.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
			senha = hexString.toString();
		} catch (Exception nsae) {
			nsae.printStackTrace();
		}
		return senha;
	}

	public static boolean isVazioOuNulo(Object obj) {
		return obj == null || obj.toString().equals("");
	}

	public static long diferencaDias(Date dataInicial, Date dataFinal) {
		return (diaCompleto(dataFinal).getTime() - diaCompleto(dataInicial).getTime()) / (1000 * 60 * 60 * 24);
	}
	
	public static long diferencaAnos(Date dataInicial, Date dataFinal) {
		return diferencaDias(dataInicial, dataFinal) / 365;
	}

	public static BigDecimal diferencaHoras(Date horaEntrada, Date horaSaida) {
		return BigDecimal.valueOf((horaSaida.getTime() - horaEntrada.getTime()) / (1000.00 * 60 * 60)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static BigDecimal horasEmDecimal(Date hora) {
		return BigDecimal.valueOf((hora.getTime() - new Date(10800000).getTime()) / (1000.00 * 60 * 60)).setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public static Date diaCompleto(Date data) {
		if (data == null)
			return null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);

		// Seta a hora para 23:59:59 para considerar o dia completo
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		return cal.getTime();

	}
	
	public static Date diaSemHora(Date data) {
		if (data == null)
			return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		// Seta a hora para 23:59:59 para considerar o dia completo
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
		
	}

	public static Date getDataMenosDias(Date data, int dias) {
		if (data == null)
			return null;
		
		long dataInMillis = data.getTime();
		long diasInMillis = new Long(dias) * (1000 * 60 * 60 * 24);
		
		return diaCompleto(new Date(dataInMillis - diasInMillis));
		
	}

	public static Date getDataMaisDias(Date data, int dias) {
		if (data == null)
			return null;

		long dataInMillis = data.getTime();
		long diasInMillis = new Long(dias) * 1000 * 60 * 60 * 24;

		return diaCompleto(new Date(dataInMillis + diasInMillis));

	}
	
	public static String getMessage(String key) {
		return messageBundle.getString(key);
	}

	public static boolean isEmailValido(String email) {
		return !isVazioOuNulo(email) && (email.contains("@") && email.indexOf("@") < email.lastIndexOf("."));
	}

	public static boolean isCpfCnpjValido(String cpfCnpj) {
		return isCpfValido(cpfCnpj) || isCnpjValido(cpfCnpj);
	}

	public static boolean isUrlValida(String url) {
		UrlValidator urlValidator = UrlValidator.getInstance();
		return urlValidator.isValid(url);
	}

	public static String getConfig(String key) {
		return configBundle.getString(key);
	}

	public static boolean isCpfValido(String cpf) {

		cpf = cpf.replaceAll("\\s*[./-]*", "");
		
		if (cpf.length() != 11)
			return false;

		String cpf_calc = cpf.substring(0, 9);
		
		Integer primDig, segDig;
		int soma = 0, peso = 10;
		for (int i = 0; i < cpf_calc.length(); i++)
			soma += Integer.parseInt(cpf_calc.substring(i, i + 1)) * peso--;

		if (soma % 11 == 0 | soma % 11 == 1)
			primDig = new Integer(0);
		else
			primDig = new Integer(11 - (soma % 11));

		soma = 0;
		peso = 11;
		for (int i = 0; i < cpf_calc.length(); i++)
			soma += Integer.parseInt(cpf_calc.substring(i, i + 1)) * peso--;

		soma += primDig.intValue() * 2;
		if (soma % 11 == 0 | soma % 11 == 1)
			segDig = new Integer(0);
		else
			segDig = new Integer(11 - (soma % 11));

		String digVerif = (primDig.toString() + segDig.toString());

		return digVerif.equals(cpf.substring(9, 11));
	}

	public static boolean isCnpjValido(String cnpj) {

		cnpj = cnpj.replaceAll("\\s*[./-]*", "");
		
		if (cnpj.length() != 14)
			return false;

		int soma = 0, dig;
		String cnpj_calc = cnpj.substring(0, 12);

		if (cnpj.length() != 14)
			return false;

		char[] chr_cnpj = cnpj.toCharArray();

		/* Primeira parte */
		for (int i = 0; i < 4; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9)
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);

		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		/* Segunda parte */
		soma = 0;
		for (int i = 0; i < 5; i++)
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9)
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
		for (int i = 0; i < 8; i++)
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9)
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
		dig = 11 - (soma % 11);
		cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);

		return cnpj.equals(cnpj_calc);
	}

	public static String dataToString(Date data) {
		if (data == null)
			return null;
		
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return df.format(data);
	}
	
	public static Date stringToData(String strData) {
		
		strData = strData.replaceAll("/", "-");
		
		String[] data = strData.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.valueOf(data[data.length - 1]));
		cal.set(Calendar.MONTH, Integer.valueOf(data[data.length - 2]) - 1);
		if (data.length > 2)
			cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(data[0]));
		else
			cal.set(Calendar.DAY_OF_MONTH, 1);
		
		return diaSemHora(cal.getTime());
	}
	
	public static String valorToString(BigDecimal valor) {
		return valorToString(valor, 2);
	}
	
	public static String valorToString(BigDecimal valor, int dec) {
		
		if (valor == null)
			valor = BigDecimal.ZERO;
		
		NumberFormat df = DecimalFormat.getInstance();
		df.setMaximumFractionDigits(dec);
		df.setMinimumFractionDigits(dec);
		return df.format(valor);
	}

	public static String decimalToHora(BigDecimal decimal) {
		if (decimal == null)
			return null;
		
		boolean neg = false;
		if (decimal.compareTo(BigDecimal.ZERO) < 0) {
			neg = true;
			decimal = decimal.multiply(BigDecimal.valueOf(-1));
		}
		
		double dec = decimal.doubleValue() - decimal.intValue();
		long min = Math.round(60.0 * dec);
		
		String ret = (neg ? "-" : "") + decimal.intValue() + ":" + (min < 10 ? "0" : "") + min;
		
		return ret;
	}

	public static BigDecimal stringToHora(String strHoraExtra) {

		if (isVazioOuNulo(strHoraExtra))
			return null;
		
		boolean neg = strHoraExtra.startsWith("-");
		strHoraExtra = strHoraExtra.replace("-", "").replace("+", "");

//		if (!strHoraExtra.matches("(?:[01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d"))
//			throw new IllegalArgumentException();
		
		String[] aStr = strHoraExtra.split(":");
		
		Integer hor = Integer.valueOf(aStr[0]);
		Integer min = Integer.valueOf(aStr[1]);
		
		BigDecimal ret = BigDecimal.valueOf(hor).add(BigDecimal.valueOf(min / 60.0));

		if (neg)
			ret = ret.multiply(BigDecimal.valueOf(-1.0));
		
		return ret;
	}

	public static Date primeiroDiaMes(String mesDe) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.valueOf(mesDe.split("/")[0]) - 1);
		cal.set(Calendar.YEAR, Integer.valueOf(mesDe.split("/")[1]));
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static Date ultimoDiaMes(String mesDe) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Integer.valueOf(mesDe.split("/")[0]) - 1);
		cal.set(Calendar.YEAR, Integer.valueOf(mesDe.split("/")[1]));
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static int diferencaDiasUteis(Date dataDe, Date dataAte) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataDe);
		int dif = 0;
		
		while (cal.getTime().compareTo(dataAte) <= 0) {
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
				dif++;
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return dif;
	}

}
