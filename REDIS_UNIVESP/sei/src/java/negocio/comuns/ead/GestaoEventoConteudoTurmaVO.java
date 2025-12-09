package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoRecursoEnum;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public class GestaoEventoConteudoTurmaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
	 */

	private Integer codigo;
	private TurmaVO turmaVO;
	private ConteudoVO conteudoVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;
	private TipoRecursoEnum tipoRecurso;
	private UnidadeConteudoVO unidadeConteudoVO;
	private ConteudoUnidadePaginaVO conteudoUnidadePaginaVO;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private SituacaoPBLEnum situacao;
	private Date dateLiberacao;
	private String formulaCalculoNotaFinalGeral;
	private List<GestaoEventoConteudoTurmaResponsavelAtaVO> gestaoEventoConteudoTurmaResponsavelAtaVOs;
	private List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	private List<GestaoEventoConteudoTurmaInteracaoAtaVO> gestaoEventoConteudoTurmaInteracaoAtaVOs;
	private String tipoVariavelNota;
	/**
	 * Transient
	 */
	private PessoaVO professor;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public TipoRecursoEnum getTipoRecurso() {
		if (tipoRecurso == null) {
			tipoRecurso = TipoRecursoEnum.UNIDADE_CONTEUDO;
		}
		return tipoRecurso;
	}

	public void setTipoRecurso(TipoRecursoEnum tipoRecursoEducacionalPBL) {
		this.tipoRecurso = tipoRecursoEducacionalPBL;
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

	public SituacaoPBLEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoPBLEnum.PENDENTE;
		}
		return situacao;
	}

	public void setSituacao(SituacaoPBLEnum situacao) {
		this.situacao = situacao;
	}

	public Date getDateLiberacao() {
		/*if (dateLiberacao == null) {
			dateLiberacao = new Date();
		}*/
		return dateLiberacao;
	}

	public void setDateLiberacao(Date dateLiberacao) {
		this.dateLiberacao = dateLiberacao;
	}

	public List<GestaoEventoConteudoTurmaResponsavelAtaVO> getGestaoEventoConteudoTurmaResponsavelAtaVOs() {
		if (gestaoEventoConteudoTurmaResponsavelAtaVOs == null) {
			gestaoEventoConteudoTurmaResponsavelAtaVOs = new ArrayList<GestaoEventoConteudoTurmaResponsavelAtaVO>();
		}
		return gestaoEventoConteudoTurmaResponsavelAtaVOs;
	}

	public void setGestaoEventoConteudoTurmaResponsavelAtaVOs(List<GestaoEventoConteudoTurmaResponsavelAtaVO> gestaoEventoConteudoTurmaResponsavelAtaVOs) {
		this.gestaoEventoConteudoTurmaResponsavelAtaVOs = gestaoEventoConteudoTurmaResponsavelAtaVOs;
	}

	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> getGestaoEventoConteudoTurmaAvaliacaoPBLVOs() {
		if (gestaoEventoConteudoTurmaAvaliacaoPBLVOs == null) {
			gestaoEventoConteudoTurmaAvaliacaoPBLVOs = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}

	public void setGestaoEventoConteudoTurmaAvaliacaoPBLVOs(List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs) {
		this.gestaoEventoConteudoTurmaAvaliacaoPBLVOs = gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}
	
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> getListaAvaliacaoDaAvaliacaoPBLVOs() {
		if(getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().isEmpty()){
			return new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		}
		return getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLVOs();
	}

	public List<GestaoEventoConteudoTurmaInteracaoAtaVO> getGestaoEventoConteudoTurmaInteracaoAtaVOs() {
		if (gestaoEventoConteudoTurmaInteracaoAtaVOs == null) {
			gestaoEventoConteudoTurmaInteracaoAtaVOs = new ArrayList<GestaoEventoConteudoTurmaInteracaoAtaVO>();
		}
		return gestaoEventoConteudoTurmaInteracaoAtaVOs;
	}

	public void setGestaoEventoConteudoTurmaInteracaoAtaVOs(List<GestaoEventoConteudoTurmaInteracaoAtaVO> gestaoEventoConteudoTurmaInteracaoAtaVOs) {
		this.gestaoEventoConteudoTurmaInteracaoAtaVOs = gestaoEventoConteudoTurmaInteracaoAtaVOs;
	}
	
	public Integer getTamanhoListaInteracaoAta() {
		if (getGestaoEventoConteudoTurmaInteracaoAtaVOs().isEmpty()) {
			return 0;
		}
		return getGestaoEventoConteudoTurmaInteracaoAtaVOs().size();
	}
	
	public JRDataSource getGestaoEventoConteudoTurmaInteracaoAtaJRVOs() {
		return new JRBeanArrayDataSource(getGestaoEventoConteudoTurmaInteracaoAtaVOs().toArray());
	}
	
	public String getDataLiberacaoApresentar() {
		return Uteis.getDataAplicandoFormatacao(getDateLiberacao(), "dd/MM/yyyy");
	}
	
	public int getSizeGestaoEventoConteudoTurmaAvaliacaoPBLVOs() {
		if (!getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().isEmpty()) {
			return getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().size();
		}
		return 0;
	}

	public String getFormulaCalculoNotaFinalGeral() {
		if (formulaCalculoNotaFinalGeral == null) {
			formulaCalculoNotaFinalGeral = "";
		}
		return formulaCalculoNotaFinalGeral;
	}

	public void setFormulaCalculoNotaFinalGeral(String formulaCalculoNotaFinalGeral) {
		this.formulaCalculoNotaFinalGeral = formulaCalculoNotaFinalGeral;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public String getTipoVariavelNota() {
		if (tipoVariavelNota == null) {
			tipoVariavelNota = "";
		}
		return tipoVariavelNota;
	}

	public void setTipoVariavelNota(String tipoVariavelNota) {
		this.tipoVariavelNota = tipoVariavelNota;
	}
}
