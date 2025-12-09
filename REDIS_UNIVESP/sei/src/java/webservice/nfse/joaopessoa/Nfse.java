package webservice.nfse.joaopessoa;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;



public class Nfse {

	private static final Logger LOG = LoggerFactory.getLogger(Nfse.class);
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	private final XStream xstream;
	private boolean enableValidation;
	private final GerarNfse gerarNfse;

	private final EnviarLoteRpsEnvio enviarLoteRpsEnvio;

	protected Nfse() {
		xstream = xstream();
		enableValidation = true;
		gerarNfse = new GerarNfse();
		enviarLoteRpsEnvio = new EnviarLoteRpsEnvio();
	}
	
	private XStream xstream() {
		XStream xstream = new XStream();
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.registerConverter(new EmptyConverter());
		
		xstream.autodetectAnnotations(true);
		return xstream;
	}

	public static Nfse nfse() {
		return new Nfse();
	}
	
	public String asGerarNFSEXML(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, String Id) throws Exception {
		
		String xml = XML_HEADER	+ xstream.toXML(gerarNfse);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSe(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "nfse:Rps", nomeArquivoXML,"nfse:");
		if(enableValidation) {
			try {
				Nfse.validator().from(xml).validate(notaFiscalSaidaVO.getWebServicesNFSEEnum());
			} catch (Exception e) {
				throw new NfseException(e);
			}
		}
		return xml;
	}
	
	public String asGerarNFSERioDeJaneiroXML(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, String Id) throws Exception {
		String xml = XML_HEADER	+ xstream.toXML(gerarNfse);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSe(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML,"nfse:");
		if(enableValidation) {
			try {
				Nfse.validator().from(xml).validate(notaFiscalSaidaVO.getWebServicesNFSEEnum());
			} catch (Exception e) {
				throw new NfseException(e);
			}
		}
		return xml;
	}
	
	public static RpsBuilder rps() {
		return new RpsBuilder();
	}

	public static LoteNfseBuilder loteNfse() {
		return new LoteNfseBuilder();
	}

	public static NfseValidator validator() {
		return new NfseValidator();
	}

	public Nfse disableValidation() {
		this.enableValidation = false;
		return this;
	}

	public static ServicoBuilder servico() {
		return new ServicoBuilder();
	}

	public static PrestadorBuilder prestador() {
		return new PrestadorBuilder();
	}

	public static TomadorBuilder tomador() {
		return new TomadorBuilder();
	}

	public static EnderecoBuilder endereco() {
		return new EnderecoBuilder();
	}
	
	public Nfse withRps(Rps rps) {
		gerarNfse.setRps(rps);
		return this;
	}
	public Nfse withLoteRps(LoteRps loteRps) {
		enviarLoteRpsEnvio.setLoteRps(loteRps);
		return this;
	}
}