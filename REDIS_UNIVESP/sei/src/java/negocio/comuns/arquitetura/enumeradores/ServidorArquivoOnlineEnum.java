package negocio.comuns.arquitetura.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.utilitarias.UteisJSF;

@SuppressWarnings({"unchecked", "rawtypes"})
public enum ServidorArquivoOnlineEnum {
    
	APACHE("Apache","Apache"),
	CERTISIGN("CERTISIGN","Certisign"),
	TECHCERT("TECHCERT","TechCert"),
	IMPRESSAO_OFICIAL("IMPRESSAO_OFICIAL","Impressão Oficial"),
	AMAZON_S3("S3","Amazon S3");

	String valor;
    String descricao;

	public static List<SelectItem> listaSelectItemServidorArquivoOnlineEnum(Boolean campoEmBranco){
    	
		List objs = new ArrayList(0);
		
		if(campoEmBranco)		
			objs.add(new SelectItem("", ""));
		
    	ServidorArquivoOnlineEnum[] valores = values();
		for (ServidorArquivoOnlineEnum obj : valores) {
			objs.add(new SelectItem(obj.getValor(), obj.getDescricao()));
		}
		return objs;
    }
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_ServidorArquivoOnline_"+this.name());
    }
    
	private ServidorArquivoOnlineEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

    public static ServidorArquivoOnlineEnum getEnum(String valor) {
    	ServidorArquivoOnlineEnum[] valores = values();
        for (ServidorArquivoOnlineEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	ServidorArquivoOnlineEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }
    
    public boolean isApache() {
		return name()!= null && name().equals(ServidorArquivoOnlineEnum.APACHE.name());
	}
    
    public boolean isCertisign() {
    	return name()!= null && name().equals(ServidorArquivoOnlineEnum.CERTISIGN.name());
    }
    
    public boolean isImpressaoOficial() {
    	return name()!= null && name().equals(ServidorArquivoOnlineEnum.IMPRESSAO_OFICIAL.name());
    }
    
    public boolean isAmazonS3() {
    	return name()!= null && name().equals(ServidorArquivoOnlineEnum.AMAZON_S3.name());
    }
}