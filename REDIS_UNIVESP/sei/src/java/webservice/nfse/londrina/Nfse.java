package webservice.nfse.londrina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;



public class Nfse {

	private static final Logger LOG = LoggerFactory.getLogger(Nfse.class);
	//private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
	private final XStream xstream;
	private boolean enableValidation;
	private GerarNota gerarNota;

	protected Nfse() {
		xstream = xstream();
		enableValidation = true;
		gerarNota = new GerarNota();
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
	
	public String asGerarNFSEXML(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws Exception {
		String xml = xstream.toXML(gerarNota);
		LOG.debug("Validation enabled? {}", enableValidation);
		LOG.debug("XML:\n{}", xml);
		//String nomeArquivoXML = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue() + File.separator + Uteis.getMontarCodigoBarra(notaFiscalSaidaVO.getNumero().toString(), 14) + ".xml";		
		//xml = Assinador.assinarNFSeDemaisCidades(notaFiscalSaidaVO, configuracaoRespositoriArquivo, xml, "EN", nomeArquivoXML);
		return xml.replace("__", "_");
	}
	
//	public String asGerarNFSEXMLConsultaUberlandia(String Id, NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo) throws Exception {
//		String xml = xstream.toXML(reqConsultaLoteRPS);
//		LOG.debug("Validation enabled? {}", enableValidation);
//		LOG.debug("XML:\n{}", xml);
//		return xml;
//	}
	
	public Nfse withGerarNota(GerarNota gerarNota) {
		this.gerarNota = gerarNota;
		return this;
	}
	
//	public Nfse withReqConsultaRPS(ReqConsultaLoteRPS reqConsultaLoteRPS) {
//		this.reqConsultaLoteRPS = reqConsultaLoteRPS;
//		return this;
//	}

	public Nfse disableValidation() {
		this.enableValidation = false;
		return this;
	}
}