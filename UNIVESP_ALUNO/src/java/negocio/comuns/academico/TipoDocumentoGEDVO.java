package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.SituacaoDocumentoLocalizadoEnum;
import negocio.comuns.arquitetura.SuperVO;

public class TipoDocumentoGEDVO extends SuperVO {

	private static final long serialVersionUID = -747631094678678250L;

	private Integer codigo;
	private DocumentacaoGEDVO documentacaoGED;
	private TipoDocumentoVO tipoDocumento;
	private DocumetacaoMatriculaVO documetacaoMatricula;
	private SituacaoDocumentoLocalizadoEnum situacaoDocumentoLocalizadoEnum;
	private String identificadorGed;	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public DocumentacaoGEDVO getDocumentacaoGED() {
		if (documentacaoGED == null) {
			documentacaoGED = new DocumentacaoGEDVO();
		}
		return documentacaoGED;
	}

	public void setDocumentacaoGED(DocumentacaoGEDVO documentacaoGED) {
		this.documentacaoGED = documentacaoGED;
	}

	public TipoDocumentoVO getTipoDocumento() {
		if (tipoDocumento == null) {
			tipoDocumento = new TipoDocumentoVO();
		}
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumentoVO tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public DocumetacaoMatriculaVO getDocumetacaoMatricula() {
		if (documetacaoMatricula == null) {
			documetacaoMatricula = new DocumetacaoMatriculaVO();
		}
		return documetacaoMatricula;
	}

	public void setDocumetacaoMatricula(DocumetacaoMatriculaVO documetacaoMatricula) {
		this.documetacaoMatricula = documetacaoMatricula;
	}
	
	public SituacaoDocumentoLocalizadoEnum getSituacaoDocumentoLocalizadoEnum() {
		return situacaoDocumentoLocalizadoEnum;
	}

	public void setSituacaoDocumentoLocalizadoEnum(SituacaoDocumentoLocalizadoEnum situacaoDocumentoLocalizadoEnum) {
		this.situacaoDocumentoLocalizadoEnum = situacaoDocumentoLocalizadoEnum;
	}

	public String getIdentificadorGed() {
		if (identificadorGed == null) {
			identificadorGed = "";
		}
		return identificadorGed;
	}

	public void setIdentificadorGed(String identificadorGed) {
		this.identificadorGed = identificadorGed;
	}	
}