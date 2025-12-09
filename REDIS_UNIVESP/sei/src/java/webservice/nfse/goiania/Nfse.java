package webservice.nfse.goiania;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.Assinador;
import webservice.nfse.uberlandia.ReqConsultaLoteRPS;
import webservice.nfse.uberlandia.ReqEnvioLoteRPS;



public class Nfse {

	private static final Logger LOG = LoggerFactory.getLogger(Nfse.class);
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	private final XStream xstream;
	private boolean enableValidation;
	private final GerarNfse gerarNfse;
	private ReqEnvioLoteRPS reqEnvioLoteRPS;
	private ReqConsultaLoteRPS reqConsultaLoteRPS;

	protected Nfse() {
		xstream = xstream();
		enableValidation = true;
		gerarNfse = new GerarNfse();
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
	
	public String asGerarNFSEXML(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, String Id) throws Exception {
		String xml = XML_HEADER	+ xstream.toXML(gerarNfse);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSe(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML,"");
		if(enableValidation) {
			try {
				Nfse.validator().from(xml).validate(notaFiscalSaidaVO.getWebServicesNFSEEnum());
			} catch (Exception e) {
				throw new NfseException(e);
			}
		}
		return xml;
	}
	
	public String asGerarNFSEXMLUberlandia(String Id, NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws Exception {
		String xml = xstream.toXML(reqEnvioLoteRPS);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSeDemaisCidades(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML);
		return xml;
	}
	
	public String asGerarNFSEXMLConsultaUberlandia(String Id, NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws Exception {
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
	
	public String asGerarNFSERioDeJaneiroXML(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, String Id) throws Exception {
		String xml = XML_HEADER	+ xstream.toXML(gerarNfse);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		xml = Assinador.assinarNFSe(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML,"");
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
}