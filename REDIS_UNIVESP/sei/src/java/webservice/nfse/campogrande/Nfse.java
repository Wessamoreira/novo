package webservice.nfse.campogrande;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;
import webservice.nfse.belohorizonte.NfseValidator;



public class Nfse {

	private static final Logger LOG = LoggerFactory.getLogger(Nfse.class);
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	private final XStream xstream;
	private boolean enableValidation;
	private ReqEnvioLoteRPS reqEnvioLoteRPS;
	private ReqConsultaLoteRPS reqConsultaLoteRPS;

	protected Nfse() {
		xstream = xstream();
		enableValidation = true;
		reqEnvioLoteRPS = new ReqEnvioLoteRPS();
		reqConsultaLoteRPS = new ReqConsultaLoteRPS();
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
	
	public String asGerarNFSEXML(String Id, NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws Exception {
		String xml = xstream.toXML(reqEnvioLoteRPS);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSeDemaisCidades(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML);
//		if (enableValidation) {
//			try {
//				Nfse.validator().from(xml).validate(notaFiscalSaidaVO.getWebServicesNFSEEnum());
//			} catch (Exception e) {
//				throw new NfseException(e);
//			}
//		}
		return xml;
	}
	
	public static NfseValidator validator() {
		return new NfseValidator();
	}
	
	public String asGerarNFSEXMLConsulta() throws Exception {
		String xml = xstream.toXML(reqConsultaLoteRPS);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		return xml;
	}
	
	public Nfse withReqRPS(ReqEnvioLoteRPS reqEnvioLoteRPS) {
		this.reqEnvioLoteRPS = reqEnvioLoteRPS;
		return this;
	}
	
	public Nfse withReqConsultaRPS(ReqConsultaLoteRPS reqConsultaLoteRPS) {
		this.reqConsultaLoteRPS = reqConsultaLoteRPS;
		return this;
	}

	public Nfse disableValidation() {
		this.enableValidation = false;
		return this;
	}
}