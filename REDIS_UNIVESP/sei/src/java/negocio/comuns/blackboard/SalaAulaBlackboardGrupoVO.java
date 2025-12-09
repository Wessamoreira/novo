package negocio.comuns.blackboard;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import com.fasterxml.jackson.annotation.JsonIgnore;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;

public class SalaAulaBlackboardGrupoVO extends SuperVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1730217989079890478L;
	/**
	 * Atributos para Geracao de Sala com seus grupos
	 */
	private CursoVO cursoVO;
	private AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO;
	private DisciplinaVO disciplinaVO;
	private String ano;
	private String semestre;	
	private TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum;
	private Integer qtdAlunos;
	private Integer qtdAlunosEnsalados;
	private Integer qtdAlunosNaoEnsalados;
	private Integer vagasSalaAulaBlackboard;
	private Integer vagasSalaAulaBlackboardGrupo;
	private Integer vagasSalaAulaBlackboardNecessario;
	private Integer vagasSalaAulaBlackboardGrupoNecessario;
	private Integer vagasSalaAulaBlackboardExistentes;
	private Integer vagasSalaAulaBlackboardGrupoExistentes;
	private Integer vagasSalaAulaBlackboardNovo;
	private Integer vagasSalaAulaBlackboardGrupoNovo;
	private Integer codigoOrigem;
	private String tipoOrgem;
	private Integer nrSala;
    private  Boolean selecionado;
	
	/**
	 * Atributos para o Fechamento das rotinas de grupo
	 */
	private List<SalaAulaBlackboardVO> listaGrupoSalaAulaBlackboardVO;
	private String idSalaAulaBlackboard;
	private String alunosNaoEnsalados;
	@JsonIgnore
	@ExcluirJsonAnnotation
	private String idSalaAulaBlackboardConteudoMaster;
	@JsonIgnore
	@ExcluirJsonAnnotation
	private List<SelectItem> listaSelectConteudoMasterBlackboard;
	

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public AgrupamentoUnidadeEnsinoVO getAgrupamentoUnidadeEnsinoVO() {
		if (agrupamentoUnidadeEnsinoVO == null) {
			agrupamentoUnidadeEnsinoVO = new AgrupamentoUnidadeEnsinoVO();
		}
		return agrupamentoUnidadeEnsinoVO;
	}

	public void setAgrupamentoUnidadeEnsinoVO(AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO) {
		this.agrupamentoUnidadeEnsinoVO = agrupamentoUnidadeEnsinoVO;
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

	public Integer getQtdAlunos() {
		if (qtdAlunos == null) {
			qtdAlunos = 0;
		}
		return qtdAlunos;
	}

	public void setQtdAlunos(Integer qtdAlunos) {
		this.qtdAlunos = qtdAlunos;
	}

	public Integer getQtdAlunosEnsalados() {
		if (qtdAlunosEnsalados == null) {
			qtdAlunosEnsalados = 0;
		}
		return qtdAlunosEnsalados;
	}

	public void setQtdAlunosEnsalados(Integer qtdAlunosEnsalados) {
		this.qtdAlunosEnsalados = qtdAlunosEnsalados;
	}

	public Integer getQtdAlunosNaoEnsalados() {
		if (qtdAlunosNaoEnsalados == null) {
			qtdAlunosNaoEnsalados = 0;
		}
		return qtdAlunosNaoEnsalados;
	}

	public void setQtdAlunosNaoEnsalados(Integer qtdAlunosNaoEnsalados) {
		this.qtdAlunosNaoEnsalados = qtdAlunosNaoEnsalados;
	}

	public Integer getVagasSalaAulaBlackboard() {
		if (vagasSalaAulaBlackboard == null) {
			vagasSalaAulaBlackboard = 0;
		}
		return vagasSalaAulaBlackboard;
	}

	public void setVagasSalaAulaBlackboard(Integer vagasSalaAulaBlackboard) {
		this.vagasSalaAulaBlackboard = vagasSalaAulaBlackboard;
	}

	public Integer getVagasSalaAulaBlackboardGrupo() {
		if (vagasSalaAulaBlackboardGrupo == null) {
			vagasSalaAulaBlackboardGrupo = 0;
		}
		return vagasSalaAulaBlackboardGrupo;
	}

	public void setVagasSalaAulaBlackboardGrupo(Integer vagasSalaAulaBlackboardGrupo) {
		this.vagasSalaAulaBlackboardGrupo = vagasSalaAulaBlackboardGrupo;
	}

	public Integer getVagasSalaAulaBlackboardNecessario() {
		if (vagasSalaAulaBlackboardNecessario == null) {
			vagasSalaAulaBlackboardNecessario = 0;
		}
		return vagasSalaAulaBlackboardNecessario;
	}

	public void setVagasSalaAulaBlackboardNecessario(Integer vagasSalaAulaBlackboardNecessario) {
		this.vagasSalaAulaBlackboardNecessario = vagasSalaAulaBlackboardNecessario;
	}

	public Integer getVagasSalaAulaBlackboardGrupoNecessario() {
		if (vagasSalaAulaBlackboardGrupoNecessario == null) {
			vagasSalaAulaBlackboardGrupoNecessario = 0;
		}
		return vagasSalaAulaBlackboardGrupoNecessario;
	}

	public void setVagasSalaAulaBlackboardGrupoNecessario(Integer vagasSalaAulaBlackboardGrupoNecessario) {
		this.vagasSalaAulaBlackboardGrupoNecessario = vagasSalaAulaBlackboardGrupoNecessario;
	}

	public Integer getVagasSalaAulaBlackboardExistentes() {
		if (vagasSalaAulaBlackboardExistentes == null) {
			vagasSalaAulaBlackboardExistentes = 0;
		}
		return vagasSalaAulaBlackboardExistentes;
	}

	public void setVagasSalaAulaBlackboardExistentes(Integer vagasSalaAulaBlackboardExistentes) {
		this.vagasSalaAulaBlackboardExistentes = vagasSalaAulaBlackboardExistentes;
	}

	public Integer getVagasSalaAulaBlackboardGrupoExistentes() {
		if (vagasSalaAulaBlackboardGrupoExistentes == null) {
			vagasSalaAulaBlackboardGrupoExistentes = 0;
		}
		return vagasSalaAulaBlackboardGrupoExistentes;
	}

	public void setVagasSalaAulaBlackboardGrupoExistentes(Integer vagasSalaAulaBlackboardGrupoExistentes) {
		this.vagasSalaAulaBlackboardGrupoExistentes = vagasSalaAulaBlackboardGrupoExistentes;
	}

	public Integer getVagasSalaAulaBlackboardNovo() {
		if (vagasSalaAulaBlackboardNovo == null) {
			vagasSalaAulaBlackboardNovo = 0;
		}
		return vagasSalaAulaBlackboardNovo;
	}

	public void setVagasSalaAulaBlackboardNovo(Integer vagasSalaAulaBlackboardNovo) {
		this.vagasSalaAulaBlackboardNovo = vagasSalaAulaBlackboardNovo;
	}

	public Integer getVagasSalaAulaBlackboardGrupoNovo() {
		if (vagasSalaAulaBlackboardGrupoNovo == null) {
			vagasSalaAulaBlackboardGrupoNovo = 0;
		}
		return vagasSalaAulaBlackboardGrupoNovo;
	}

	public void setVagasSalaAulaBlackboardGrupoNovo(Integer vagasSalaAulaBlackboardGrupoNovo) {
		this.vagasSalaAulaBlackboardGrupoNovo = vagasSalaAulaBlackboardGrupoNovo;
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

	public Integer getNrSala() {
		if (nrSala == null) {
			nrSala = 0;
		}
		return nrSala;
	}

	public void setNrSala(Integer nrSala) {
		this.nrSala = nrSala;
	}

	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public String getTipoOrgem() {
		if (tipoOrgem == null) {
			tipoOrgem = "";
		}
		return tipoOrgem;
	}

	public void setTipoOrgem(String tipoOrgem) {
		this.tipoOrgem = tipoOrgem;
	}

	
	public List<SalaAulaBlackboardVO> getListaGrupoSalaAulaBlackboardVO() {
		if (listaGrupoSalaAulaBlackboardVO == null) {
			listaGrupoSalaAulaBlackboardVO = new ArrayList<>();
		}
		return listaGrupoSalaAulaBlackboardVO;
	}

	public void setListaGrupoSalaAulaBlackboardVO(List<SalaAulaBlackboardVO> listaGrupoSalaAulaBlackboardVO) {
		this.listaGrupoSalaAulaBlackboardVO = listaGrupoSalaAulaBlackboardVO;
	}

	public String getIdSalaAulaBlackboard() {
		if (idSalaAulaBlackboard == null) {
			idSalaAulaBlackboard = "";
		}
		return idSalaAulaBlackboard;
	}

	public void setIdSalaAulaBlackboard(String idSalaAulaBlackboard) {
		this.idSalaAulaBlackboard = idSalaAulaBlackboard;
	}

	public String getAlunosNaoEnsalados() {
		if (alunosNaoEnsalados == null) {
			alunosNaoEnsalados = "";
		}
		return alunosNaoEnsalados;
	}

	public void setAlunosNaoEnsalados(String alunosNaoEnsalados) {
		this.alunosNaoEnsalados = alunosNaoEnsalados;
	}
	
	public Integer getTotalElementosListaGrupoSalaAulaBlackboard() {
		if(getListaGrupoSalaAulaBlackboardVO().size() > 6) {
			return 6;
		}
		return getListaGrupoSalaAulaBlackboardVO().size();
	}
	
	public String getIdDisciplinaPorAgrupamentoUnidadeEnsino() {
		return "D"+getDisciplinaVO().getCodigo().toString()+"A"+getAgrupamentoUnidadeEnsinoVO().getCodigo().toString();
	}
   
	
	public Boolean getSelecionado() {
	if(selecionado == null) {
		selecionado =  false;
	}
	return selecionado;
	}
	
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}
	
	public TipoSalaAulaBlackboardEnum getTipoSalaAulaBlackboardEnum() {
		if (tipoSalaAulaBlackboardEnum == null) {
			tipoSalaAulaBlackboardEnum = TipoSalaAulaBlackboardEnum.NENHUM;
		}
		return tipoSalaAulaBlackboardEnum;
	}

	public void setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) {
		this.tipoSalaAulaBlackboardEnum = tipoSalaAulaBlackboardEnum;
	}

	public List<SelectItem> getListaSelectConteudoMasterBlackboard() {
		if(listaSelectConteudoMasterBlackboard == null) {
			listaSelectConteudoMasterBlackboard=new ArrayList<>();	
		}
		return listaSelectConteudoMasterBlackboard;
	}

	public void setListaSelectConteudoMasterBlackboard(List<SelectItem> listaSelectConteudoMasterBlackboard) {
		this.listaSelectConteudoMasterBlackboard = listaSelectConteudoMasterBlackboard;
	}

	public String getIdSalaAulaBlackboardConteudoMaster() {
		return idSalaAulaBlackboardConteudoMaster;
	}

	public void setIdSalaAulaBlackboardConteudoMaster(String idSalaAulaBlackboardConteudoMaster) {
		this.idSalaAulaBlackboardConteudoMaster = idSalaAulaBlackboardConteudoMaster;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
