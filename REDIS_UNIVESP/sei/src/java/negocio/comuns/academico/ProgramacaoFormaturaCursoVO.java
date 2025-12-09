package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ProgramacaoFormaturaCursoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private Integer codigo;
	private ProgramacaoFormaturaVO programacaoFormaturaVO;
	private CursoVO cursoVO;
	private Integer quantidadeAlunosCurso;
	private TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO;
	private String primeiroConhecimentoCurso;
	private String renovacaoConhecimentoCurso;
	private String nrRegistroInternoCurso;
	private List listaSelectItemPrimeiroReconhecimento;
	private List listaSelectItemRenovacaoReconhecimento;
	private Boolean assinarDigitalmente;
	private Boolean cursoProgramacaoDocumentoAssinado;
	private boolean existeAlunoSemVinculoDocumentoAssinado = false;
	private String mensagemExisteDocumentoAssinado;
	private DocumentoAssinadoVO documentoAssinadoVO;
	
//	public Integer getCodigo() {
//		if (codigo == null) {
//			codigo = 0;
//		}
//		return codigo;
//	}
//	
//	public void setCodigo(Integer codigo) {
//		this.codigo = codigo;
//	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}
	
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	
	public Integer getQuantidadeAlunosCurso() {
		return quantidadeAlunosCurso;
	}
	
	public void setQuantidadeAlunosCurso(Integer quantidadeAlunosCurso) {
		this.quantidadeAlunosCurso = quantidadeAlunosCurso;
	}
	
	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracaoVO() {
		if (textoPadraoDeclaracaoVO == null) {
			textoPadraoDeclaracaoVO = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracaoVO;
	}
	
	public void setTextoPadraoDeclaracaoVO(TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO) {
		this.textoPadraoDeclaracaoVO = textoPadraoDeclaracaoVO;
	}
	
	public String getPrimeiroConhecimentoCurso() {
		if (primeiroConhecimentoCurso == null) {
			primeiroConhecimentoCurso = "";
		}
		return primeiroConhecimentoCurso;
	}
	
	public void setPrimeiroConhecimentoCurso(String primeiroConhecimentoCurso) {
		this.primeiroConhecimentoCurso = primeiroConhecimentoCurso;
	}
	
	public String getRenovacaoConhecimentoCurso() {
		if (renovacaoConhecimentoCurso == null) {
			renovacaoConhecimentoCurso = "";
		}
		return renovacaoConhecimentoCurso;
	}
	
	public void setRenovacaoConhecimentoCurso(String renovacaoConhecimentoCurso) {
		this.renovacaoConhecimentoCurso = renovacaoConhecimentoCurso;
	}
	
	public List getListaSelectItemPrimeiroReconhecimento() {
		if (listaSelectItemPrimeiroReconhecimento == null) {
			listaSelectItemPrimeiroReconhecimento = new ArrayList<>(0);
		}
		return listaSelectItemPrimeiroReconhecimento;
	}
	
	public void setListaSelectItemPrimeiroReconhecimento(List listaSelectItemPrimeiroReconhecimento) {
		this.listaSelectItemPrimeiroReconhecimento = listaSelectItemPrimeiroReconhecimento;
	}
	
	public List getListaSelectItemRenovacaoReconhecimento() {
		if (listaSelectItemRenovacaoReconhecimento == null) {
			listaSelectItemRenovacaoReconhecimento = new ArrayList<>(0);
		}
		return listaSelectItemRenovacaoReconhecimento;
	}
	
	public void setListaSelectItemRenovacaoReconhecimento(List listaSelectItemRenovacaoReconhecimento) {
		this.listaSelectItemRenovacaoReconhecimento = listaSelectItemRenovacaoReconhecimento;
	}
	
	public String getNrRegistroInternoCurso() {
		if (nrRegistroInternoCurso == null) {
			nrRegistroInternoCurso = "";
		}
		return nrRegistroInternoCurso;
	}
	
	public void setNrRegistroInternoCurso(String nrRegistroInternoCurso) {
		this.nrRegistroInternoCurso = nrRegistroInternoCurso;
	}
	
	public Boolean getAssinarDigitalmente() {
		if (assinarDigitalmente == null) {
			assinarDigitalmente = false;
		}
		return assinarDigitalmente;
	}
	
	public void setAssinarDigitalmente(Boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}
	
	public Boolean getCursoProgramacaoDocumentoAssinado() {
		if (cursoProgramacaoDocumentoAssinado == null) {
			cursoProgramacaoDocumentoAssinado = false;
		}
		return cursoProgramacaoDocumentoAssinado;
	}
	
	public void setCursoProgramacaoDocumentoAssinado(Boolean cursoProgramacaoDocumentoAssinado) {
		this.cursoProgramacaoDocumentoAssinado = cursoProgramacaoDocumentoAssinado;
	}
	
	public String getMensagemExisteDocumentoAssinado() {
		if (mensagemExisteDocumentoAssinado == null) {
			mensagemExisteDocumentoAssinado = "";
		}
		return mensagemExisteDocumentoAssinado;
	}
	
	public void setMensagemExisteDocumentoAssinado(String mensagemExisteDocumentoAssinado) {
		this.mensagemExisteDocumentoAssinado = mensagemExisteDocumentoAssinado;
	}

	public boolean isExisteAlunoSemVinculoDocumentoAssinado() {
		return existeAlunoSemVinculoDocumentoAssinado;
	}

	public void setExisteAlunoSemVinculoDocumentoAssinado(boolean existeAlunoSemVinculoDocumentoAssinado) {
		this.existeAlunoSemVinculoDocumentoAssinado = existeAlunoSemVinculoDocumentoAssinado;
	}
	
	

}
