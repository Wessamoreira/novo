package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.NivelImportanciaEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * @author Victor Hugo 08/09/2014
 */
public class AnotacaoDisciplinaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataCriacao;
	private String anotacao;
	private String palavraChave;
	private DisciplinaVO disciplinaVO;
	private ConteudoVO conteudoVOs;
	private UnidadeConteudoVO unidadeConteudoVO;
	private Boolean publica;
	private PessoaVO pessoaVO;
	private MatriculaVO matriculaVO;
	private ConteudoUnidadePaginaVO conteudoUnidadePaginaVO;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private NivelImportanciaEnum nivelImportanciaEnum;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataCriacao() {
		if (dataCriacao == null) {
			dataCriacao = new Date();
		}
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAnotacao() {
		if (anotacao == null) {
			anotacao = "";
		}
		return anotacao;
	}

	public void setAnotacao(String anotacao) {
		this.anotacao = anotacao;
	}

	public String getPalavraChave() {
		if (palavraChave == null) {
			palavraChave = "";
		}
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public ConteudoVO getConteudoVOs() {
		if (conteudoVOs == null) {
			conteudoVOs = new ConteudoVO();
		}
		return conteudoVOs;
	}

	public void setConteudoVOs(ConteudoVO conteudoVOs) {
		this.conteudoVOs = conteudoVOs;
	}

	public UnidadeConteudoVO getUnidadeConteudoVO() {
		if (unidadeConteudoVO == null) {
			unidadeConteudoVO = new UnidadeConteudoVO();
		}
		return unidadeConteudoVO;
	}

	public void setUnidadeConteudoVO(UnidadeConteudoVO unidadeConteudoVO) {
		this.unidadeConteudoVO = unidadeConteudoVO;
	}

	public Boolean getPublica() {
		if (publica == null) {
			publica = false;
		}
		return publica;
	}

	public void setPublica(Boolean publica) {
		this.publica = publica;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public ConteudoUnidadePaginaVO getConteudoUnidadePaginaVO() {
		if (conteudoUnidadePaginaVO == null) {
			conteudoUnidadePaginaVO = new ConteudoUnidadePaginaVO();
		}
		return conteudoUnidadePaginaVO;
	}

	public void setConteudoUnidadePaginaVO(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) {
		this.conteudoUnidadePaginaVO = conteudoUnidadePaginaVO;
	}

	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalVO() {
		if (conteudoUnidadePaginaRecursoEducacionalVO == null) {
			conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalVO(ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		this.conteudoUnidadePaginaRecursoEducacionalVO = conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public NivelImportanciaEnum getNivelImportanciaEnum() {
		if (nivelImportanciaEnum == null) {
			nivelImportanciaEnum = NivelImportanciaEnum.NORMAL;
		}
		return nivelImportanciaEnum;
	}

	public void setNivelImportanciaEnum(NivelImportanciaEnum nivelImportanciaEnum) {
		this.nivelImportanciaEnum = nivelImportanciaEnum;
	}
	
	public boolean isExisteUnidadeConteudo(){
		return Uteis.isAtributoPreenchido(getUnidadeConteudoVO().getCodigo());
	}
	
	public boolean isExisteConteudoUnidadePagina(){
		return Uteis.isAtributoPreenchido(getConteudoUnidadePaginaVO().getCodigo());
	}
}
