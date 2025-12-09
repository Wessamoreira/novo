package webservice.boletoonline.bradesco;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import webservice.boletoonline.bradesco.classes.RegistroEntradaBoleto;
import webservice.boletoonline.bradesco.classes.RegistroRetornoBoleto;
import webservice.boletoonline.bradesco.ws.WSRegistroBoleto;

public class BradescoBoletoOnline extends BBRConfig  {

	public ContaReceberVO contareceberVO;
	public ControleRemessaContaReceberVO crcrVO;
	public ConfiguracaoGeralSistemaVO config;
	public ConfiguracaoFinanceiroVO configFin;
	private KeyStore rep;
	private KeyStore repCadeiaCert;
	
	public BradescoBoletoOnline(ContaReceberVO contareceberVO, ControleRemessaContaReceberVO crcrVO, ConfiguracaoGeralSistemaVO config, ConfiguracaoFinanceiroVO confiFin) {
		setContareceberVO(contareceberVO);
		setCrcrVO(crcrVO);
		setConfig(config);
		setConfigFin(configFin);
	}
	
	public RegistroRetornoBoleto enviarBoletoRemessaOnlineBradesco () throws Exception {
		WSRegistroBoleto ws = new WSRegistroBoleto();
		RegistroEntradaBoleto re = new RegistroEntradaBoleto(getContareceberVO(), getCrcrVO(), getConfigFin());
		RegistroRetornoBoleto regRetorno = ws.enviarBoleto(this, re);
		if (!getContareceberVO().getContaCorrenteVO().getPermitirEmissaoBoletoRemessaOnlineRejeita()) {
			if (!regRetorno.getCdErro().equals("")) {
				return regRetorno; 
			}
		}
		return regRetorno;		
	}

	@Override
	public KeyStore getCertificadoKeyStore() throws KeyStoreException {
		try {
			//Security.addProvider(new BouncyCastleProvider());
	    	String nomeCertificado = getContareceberVO().getContaCorrenteVO().getArquivoCertificadoVO().getNome();
	    	
	    	String caminhoCertificado = getConfig().getLocalUploadArquivoFixo() + File.separator + getContareceberVO().getContaCorrenteVO().getArquivoCertificadoVO().getPastaBaseArquivo() + File.separator + nomeCertificado;						
	    	//caminhoCertificado = "C:\\Users\\THYAGO JAYME DINIZ\\Desktop\\Certificados\\Certificado Digital - Goiania - ok\\IPOG  INSTITUTO DE POSGRADUACAO E GRADUACAO LTD04688977000102.pfx";
	    	//System.out.println("CAMINHO CERTIFICADO => " + caminhoCertificado);
	    	rep = getKeyStore();
			InputStream fs = new FileInputStream(caminhoCertificado);
			rep.load(fs, contareceberVO.getContaCorrenteVO().getSenhaCertificado().toCharArray());
			return rep;
		} catch (Exception e) {
			return null;
		}
	}

	public KeyStore getKeyStore() throws Exception {
		return KeyStore.getInstance(KeyStore.getDefaultType());
	}
	
	@Override
	public String getCertificadoSenha() {
		return getContareceberVO().getContaCorrenteVO().getSenhaCertificado();
	}

	@Override
	public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
		try {
			//Security.addProvider(new BouncyCastleProvider());
			repCadeiaCert = getKeyStore();
			InputStream fs = new FileInputStream(contareceberVO.getContaCorrenteVO().getCaminhoUnidadeCertificadora());
			repCadeiaCert.load(fs, contareceberVO.getContaCorrenteVO().getSenhaUnidadeCertificadora().toCharArray());
			return repCadeiaCert;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getCadeiaCertificadosSenha() {
		return getContareceberVO().getContaCorrenteVO().getSenhaUnidadeCertificadora();
	}

	public ContaReceberVO getContareceberVO() {
		if (contareceberVO == null) {
			contareceberVO = new ContaReceberVO();
		}
		return contareceberVO;
	}

	public void setContareceberVO(ContaReceberVO contareceberVO) {
		this.contareceberVO = contareceberVO;
	}

	public ConfiguracaoGeralSistemaVO getConfig() {
		if (config == null) {
			config = new ConfiguracaoGeralSistemaVO();
		}
		return config;
	}

	public void setConfig(ConfiguracaoGeralSistemaVO config) {
		this.config = config;
	}

	public ControleRemessaContaReceberVO getCrcrVO() {
		if (crcrVO == null) {
			crcrVO = new ControleRemessaContaReceberVO();
		}
		return crcrVO;
	}

	public void setCrcrVO(ControleRemessaContaReceberVO crcrVO) {
		this.crcrVO = crcrVO;
	}

	public ConfiguracaoFinanceiroVO getConfigFin() {
		if (configFin == null) {
			configFin = new ConfiguracaoFinanceiroVO();
		}
		return configFin;
	}

	public void setConfigFin(ConfiguracaoFinanceiroVO configFin) {
		this.configFin = configFin;
	}

}
