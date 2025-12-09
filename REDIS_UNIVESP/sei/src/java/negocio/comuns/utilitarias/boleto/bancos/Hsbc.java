/*
 * Esta biblioteca e um software livre, que pode ser redistribuido e/ou
 * modificado sob os termos da LicenÃƒÂ§a Publica Geral Reduzida GNU,
 * conforme publicada pela Free Software Foundation, versao 2.1 da licenca.
 *
 * Esta biblioteca e distribuida na experanca de ser util aos seus usuarios,
 * porem NAO TEM NENHUMA GARANTIA, EXPLICITAS OU IMPLICITAS, COMERCIAIS OU
 * DE ATENDIMENTO A UMA DETERMINADA FINALIDADE.
 * Veja a Licenca Publica Geral Reduzida GNU para maiores detalhes.
 * A licenca se encontra no arquivo lgpl-br.txt
 */

package negocio.comuns.utilitarias.boleto.bancos;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;

/**
 * Classe para gerar o boleto do unibanco
 * 
 * @author Fabio Souza
 */
import java.io.Serializable; public class Hsbc implements BoletoBanco, Serializable {

	JBoletoBean boleto;

	/**
	 * Metdodo responsavel por resgatar o numero do banco, coloque no return o codigo do seu banco
	 */
	public String getNumero() {
		return "399";
	}

	public String getCnr() {
		return "2";
	}

	public String getApp() {
		return "2";
	}

	public int getDataJuliano2() {

		String[] data = boleto.getDataVencimento().split("/");

		int day = Integer.parseInt(data[0]);
		String ano = data[2].substring(3);

		Calendar cal = GregorianCalendar.getInstance();
		int diaDoAno = cal.get(cal.DAY_OF_YEAR) + (day - cal.get(cal.DAY_OF_MONTH));

		String retorno = String.valueOf(diaDoAno) + String.valueOf(ano);

		return Integer.parseInt(retorno);
	}

	/**
	 * Método que pega a data de vencimento do boleto, 
	 * e a transforma no formato Juliano, de 4 casas. 
	 * 3 primeiras casas - Número correspondente ao dia naquele ano. 
	 * Ex.: 01 de Janeiro -> 001 
	 *      11 de Fevereiro -> 042 
	 * 4ª casa - Número correspondente ao ano. 
	 * Ex.: 2001 - 2011 -> Número 1 
	 *      2002 - 2012 -> Número 2
	 * 
	 * @return String Data vencimento formato Juliano.
	 * @author Murillo Parreira
	 */
	public String getDataJuliano() {
		try {
			Date dataVencimento = Uteis.getData(boleto.getDataVencimento(), "dd/MM/yyyy");
			Calendar cal = GregorianCalendar.getInstance();
			cal.setTime(dataVencimento);
			String anoFormatoJuliano = String.valueOf(cal.get(cal.YEAR)).substring(3);
			String diaAnoFormatoJuliano = String.valueOf(cal.get(cal.DAY_OF_YEAR));
			if (cal.get(cal.DAY_OF_YEAR) < 100) {
				if (cal.get(cal.DAY_OF_YEAR) < 10) {
					diaAnoFormatoJuliano = "00" + diaAnoFormatoJuliano;
				} else {
					diaAnoFormatoJuliano = "0" + diaAnoFormatoJuliano;
				}
			}
			String dataFormatoJuliano = diaAnoFormatoJuliano + anoFormatoJuliano;
			return dataFormatoJuliano;
		} catch (Exception e) {
			return "0000";
		}
	}

	/**
	 * MÃƒÂ©todo particular do hsbc
	 */
	public String getCampoLivre() {
		return getNumero() + boleto.getMoeda() + getCampo4() + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getCodCliente() + boleto.getNossoNumero() + getDataJuliano() + getApp();
	}

	/**
	 * Classe construtura, recebe como parametro a classe jboletobean
	 */
	public Hsbc(JBoletoBean boleto) {
		this.boleto = boleto;
	}

	/**
	 * Metodo que monta o primeiro campo do codigo de barras Este campo como os demais e feito a partir do da documentacao do banco A documentacao do banco tem a estrutura de como monta cada campo,
	 * depois disso ÃƒÂ© sÃƒÂ³ concatenar os campos como no exemplo abaixo.
	 */
	private String getCampo1() {
		String campo = getNumero() + boleto.getMoeda();
                String codCliente = "";
                if (!boleto.getCodCliente().equals("") && boleto.getCodCliente().length() >= 5) {
                    codCliente = boleto.getCodCliente().substring(0, 5);
                }
                campo = campo + codCliente;

		return boleto.getDigitoCampo(campo, 2);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo2() {
		String campo = boleto.getCodCliente().substring(5) + boleto.getNossoNumero().substring(0, 8);

		return boleto.getDigitoCampo(campo, 1);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo3() {
		String campo = boleto.getNossoNumero().substring(8) + getDataJuliano() + getApp();

		return boleto.getDigitoCampo(campo, 1);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo4() {
		String campo = getNumero() + String.valueOf(boleto.getMoeda()) + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getCodCliente() + boleto.getNossoNumero() + getDataJuliano()
				+ getApp();

		return boleto.getDigitoCodigoBarras(campo);
	}

	/**
	 * ver documentacao do banco para saber qual a ordem deste campo
	 */
	private String getCampo5() {
		String campo = boleto.getFatorVencimento() + boleto.getValorTitulo();
		return campo;
	}

	/**
	 * Metodo para monta o desenho do codigo de barras A ordem destes campos tambem variam de banco para banco, entao e so olhar na documentacao do seu banco qual a ordem correta
	 */
	public String getCodigoBarras() {
		return getNumero() + String.valueOf(boleto.getMoeda()) + getCampo4() + boleto.getFatorVencimento() + boleto.getValorTitulo() + boleto.getCodCliente() + boleto.getNossoNumero()
				+ getDataJuliano() + getApp();
	}

	/**
	 * Metodo que concatena os campo para formar a linha digitavel E necessario tambem olhar a documentacao do banco para saber a ordem correta a linha digitavel
	 */
	public String getLinhaDigitavel() {
		return getCampo1().substring(0, 5) + "." + getCampo1().substring(5) + "  " + getCampo2().substring(0, 5) + "." + getCampo2().substring(5) + "  " + getCampo3().substring(0, 5) + "."
				+ getCampo3().substring(5) + "  " + getCampo4() + "  " + getCampo5();
	}

	/**
	 * Recupera a carteira no padrao especificado pelo banco
	 * 
	 * @author Gladyston Batista/Eac Software
	 */
	public String getCarteiraFormatted() {
		return boleto.getCarteira();
	}

	/**
	 * Recupera a agencia / codigo cedente no padrao especificado pelo banco
	 * 
	 * @author Gladyston Batista/Eac Software
	 */
	public String getAgenciaCodCedenteFormatted() {
		return boleto.getAgencia() + " / " + boleto.getContaCorrente() + "-" + boleto.getDvContaCorrente();
	}

	/**
	 * Recupera o nossoNumero no padrao especificado pelo banco
	 * 
	 * @author Gladyston Batista/Eac Software
	 */
	public String getNossoNumeroFormatted() {
		return String.valueOf(Long.parseLong(boleto.getNossoNumero()));
	}
}