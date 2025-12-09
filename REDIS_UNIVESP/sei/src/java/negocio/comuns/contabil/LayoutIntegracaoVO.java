package negocio.comuns.contabil;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.contabil.enumeradores.TipoLayoutIntegracaoEnum;
import negocio.comuns.contabil.enumeradores.TipoLayoutPlanoContaEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class LayoutIntegracaoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2266847434039158778L;
	private Integer codigo;
	private String descricao;
	private TipoLayoutIntegracaoEnum tipoLayoutIntegracao;
	private TipoLayoutPlanoContaEnum tipoLayoutPlanoContaEnum;
	private String delimitadorTxt;
	private boolean terminaInstrucaoComDelimitador = false;
	private String valorPrefixo;
	private String valorSufixo;
	private List<LayoutIntegracaoTagVO> listaLayoutIntegracaoTag;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String gerarApresentacaoLayout() {
		if (Uteis.isAtributoPreenchido(getTipoLayoutIntegracao()) && getTipoLayoutIntegracao().isXml()) {
			return gerarXmlApresentacaoLayout();
		} else if (Uteis.isAtributoPreenchido(getTipoLayoutIntegracao()) && getTipoLayoutIntegracao().isTxt()) {
			return gerarTxtApresentacaoLayout();
		}
		return "";
	}

	public String gerarTxtApresentacaoLayout()  {
		try {
			if (Uteis.isAtributoPreenchido(getListaLayoutIntegracaoTag())) {
				Collections.sort(getListaLayoutIntegracaoTag());
				StringBuilder sb = new StringBuilder("");
				for (LayoutIntegracaoTagVO lixtag : getListaLayoutIntegracaoTag()) {					
					if (lixtag.getTipoTagEnum().isTagRoot()) {
						continue;
					}else if (lixtag.getTipoTagEnum().isTagList()) {
						sb.append(System.lineSeparator()).append(lixtag.getTag()).append("-> ");
						continue;
					}
					sb.append(lixtag.getTag());
					sb.append(getDelimitadorTxt());
				}
				return sb.toString();
			}
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}

		return "";
	}

	public String gerarXmlApresentacaoLayout() {
		try {
			if (Uteis.isAtributoPreenchido(getListaLayoutIntegracaoTag())) {
				Collections.sort(getListaLayoutIntegracaoTag());
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();
				Element rootElement = null;
				for (LayoutIntegracaoTagVO lixtag : getListaLayoutIntegracaoTag()) {
					if (lixtag.getTipoTagEnum().isTagRoot()) {
						rootElement = doc.createElement(lixtag.getTag());
						Attr attr = doc.createAttribute("id");
						attr.setValue(lixtag.getNivel().replaceAll("\\.", ""));
						rootElement.setAttributeNode(attr);
						rootElement.setIdAttributeNode(attr, true);
						doc.appendChild(rootElement);
					} else {
						preencherTipoTagXml(doc, rootElement, lixtag);
					}
				}
				Transformer trans = TransformerFactory.newInstance().newTransformer();
				StringWriter writer = new StringWriter();
				trans.setOutputProperty(OutputKeys.INDENT, "yes");
				trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
				trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				trans.transform(new DOMSource(doc), new StreamResult(writer));
				return writer.toString().replaceAll(" id=\".*?\"", "");
			}
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}

		return "";

	}

	public void preencherTipoTagXml(Document doc, Element rootElement, LayoutIntegracaoTagVO lixtag)  {
		Element element = doc.createElement(lixtag.getTag());
		Attr attr = doc.createAttribute("id");
		attr.setValue(lixtag.getNivel().replaceAll("\\.", ""));
		element.setAttributeNode(attr);
		element.setIdAttributeNode(attr, true);
		if (lixtag.getTipoTagEnum().isFixo()) {
			if (lixtag.getCampo().length() > lixtag.getTamanhoTag()) {
				element.appendChild(doc.createTextNode(lixtag.getCampo().substring(0, lixtag.getTamanhoTag())));
			} else {
				element.appendChild(doc.createTextNode(lixtag.getCampo()));
			}
		} else if (lixtag.getTipoTagEnum().isCampo()) {
			element.appendChild(doc.createTextNode(lixtag.getCampo()));
		}
		if (Uteis.isAtributoPreenchido(lixtag.getTagMae().getTag())) {
			Element elementPai = doc.getElementById(lixtag.getTagMae().getNivel().replaceAll("\\.", ""));
			if (elementPai != null) {
				elementPai.appendChild(element);
			} else {
				throw new StreamSeiException("Não foi encontrado a tag mãe " + lixtag.getTagMae().getTag());
			}
		} else {
			rootElement.appendChild(element);
		}
	}
	
	public List<LayoutIntegracaoTagVO> getListaLayoutIntegracaoTag() {
		if (listaLayoutIntegracaoTag == null) {
			listaLayoutIntegracaoTag = new ArrayList<>();
		}
		return listaLayoutIntegracaoTag;
	}

	public void setListaLayoutIntegracaoTag(List<LayoutIntegracaoTagVO> listaLayoutIntegracaoContabilTag) {
		this.listaLayoutIntegracaoTag = listaLayoutIntegracaoContabilTag;
	}

	public TipoLayoutIntegracaoEnum getTipoLayoutIntegracao() {
		if (tipoLayoutIntegracao == null) {
			tipoLayoutIntegracao = TipoLayoutIntegracaoEnum.XML;
		}
		return tipoLayoutIntegracao;
	}

	public void setTipoLayoutIntegracao(TipoLayoutIntegracaoEnum tipoLayoutIntegracao) {
		this.tipoLayoutIntegracao = tipoLayoutIntegracao;
	}

	public String getDelimitadorTxt() {
		if (delimitadorTxt == null) {
			delimitadorTxt = "";
		}
		return delimitadorTxt;
	}

	public void setDelimitadorTxt(String delimitadorTxt) {
		this.delimitadorTxt = delimitadorTxt;
	}

	public Integer getNumeroTags() {
		return getListaLayoutIntegracaoTag().size();
	}

	public TipoLayoutPlanoContaEnum getTipoLayoutPlanoContaEnum() {
		if (tipoLayoutPlanoContaEnum == null) {
			tipoLayoutPlanoContaEnum = TipoLayoutPlanoContaEnum.NENHUM;
		}
		return tipoLayoutPlanoContaEnum;
	}

	public void setTipoLayoutPlanoContaEnum(TipoLayoutPlanoContaEnum tipoLayoutPlanoContaEnum) {
		this.tipoLayoutPlanoContaEnum = tipoLayoutPlanoContaEnum;
	}

	public boolean isTerminaInstrucaoComDelimitador() {
		return terminaInstrucaoComDelimitador;
	}

	public void setTerminaInstrucaoComDelimitador(boolean terminaInstrucaoComDelimitador) {
		this.terminaInstrucaoComDelimitador = terminaInstrucaoComDelimitador;
	}
	
	public String getValorPrefixo() {
		return valorPrefixo;
	}

	public void setValorPrefixo(String valorPrefixo) {
		this.valorPrefixo = valorPrefixo;
	}

	public String getValorSufixo() {
		return valorSufixo;
	}

	public void setValorSufixo(String valorSufixo) {
		this.valorSufixo = valorSufixo;
	}
	
	public String getValorCampoPrefixoAndSufixo(String campo) {
		return getValorPrefixo() + campo + getValorSufixo();
	}

	public boolean isGerarDelimitador(Integer posicao, LayoutIntegracaoTagVO tagMae){
		long tamanhoPosicao = getNumeroTags();
		if(Uteis.isAtributoPreenchido(tagMae)){
			tamanhoPosicao = getListaLayoutIntegracaoTag().stream().filter(p->(p.getTipoTagEnum().isCampo() || p.getTipoTagEnum().isTagFormula() || p.getTipoTagEnum().isFixo()) && p.getTagMae().getTag().equals(tagMae.getTag())).count();
		}
		return (!isTerminaInstrucaoComDelimitador() && posicao < tamanhoPosicao) || (isTerminaInstrucaoComDelimitador() && posicao <= tamanhoPosicao);
	}
	
	public boolean isGerarNovaLinha(Integer posicao, LayoutIntegracaoTagVO tagMae){
		long tamanhoPosicao = getNumeroTags();
		if(Uteis.isAtributoPreenchido(tagMae)){
			tamanhoPosicao = getListaLayoutIntegracaoTag().stream().filter(p->(p.getTipoTagEnum().isCampo() || p.getTipoTagEnum().isTagFormula() ||p.getTipoTagEnum().isFixo()) && p.getTagMae().getTag().equals(tagMae.getTag())).count();
		}
		return posicao == tamanhoPosicao;
	}
	
	
	
	
	
	

}
