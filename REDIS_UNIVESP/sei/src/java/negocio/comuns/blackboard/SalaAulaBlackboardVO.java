package negocio.comuns.blackboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularEstagioVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.AgrupamentoUnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.utilitarias.Uteis;

public class SalaAulaBlackboardVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4513158258278195688L;
	private Integer codigo;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private GradeCurricularEstagioVO gradeCurricularEstagioVO;
	private AgrupamentoUnidadeEnsinoVO agrupamentoUnidadeEnsinoVO;
	private String id;
	private String idSalaAulaBlackboard;
	private String linkSalaAulaBlackboard;
	private String ano;
	private String semestre;
	private Integer bimestre;
	private String termId;
	private String dataSourceId;
	private TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum;
	private String nome;
	private Integer nrSala;
	private String idGrupo;
	private String grupoSetId;
	private String grupoExternalId;
	private String nomeGrupo;
	private Integer nrGrupo;
	@JsonManagedReference
	private List<SalaAulaBlackboardPessoaVO> listaProfessores;
	@JsonManagedReference
	private List<SalaAulaBlackboardPessoaVO> listaFacilitadores;
	@JsonManagedReference
	private List<SalaAulaBlackboardPessoaVO> listaAlunos;
	private ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO;
	@ExcluirJsonAnnotation
	private Integer qtdeAlunos;
	@ExcluirJsonAnnotation
	private Integer qtdeNotaNaoLocalizada;
	@ExcluirJsonAnnotation
	private boolean selecionado = false;
	@ExcluirJsonAnnotation
	private DataModelo dadosConsultaAlunos;
	@ExcluirJsonAnnotation
	private DataModelo dadosConsultaFacilitadores;
	@JsonManagedReference
	private List<SalaAulaBlackboardPessoaVO> listaOrientadores;
	@ExcluirJsonAnnotation
	private String idSalaAulaBlackboardConteudoMaster;
	@ExcluirJsonAnnotation
	private boolean conteudoMasterSelecionado = false;
	@ExcluirJsonAnnotation
	private String filtroAluno;
	@ExcluirJsonAnnotation
	private String filtroSupervisor;
	@ExcluirJsonAnnotation
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;
	@ExcluirJsonAnnotation
	private List<HistoricoNotaBlackboardVO> listaHistoricoNotaBlackboardVOs;
	
	private Integer qtdAlunosEnsalados;
	private Integer qtdAlunosNaoEnsalados;
	private String alunosEnsalados;
	private String alunosNaoEnsalados;	
	private Integer qtdeSalas;
	private String salasExistentes;
	private Integer qtdeSalasExistentes;
	private Integer qtdeSalasNecessarias;
	private Integer alunosPorSala;


	public SalaAulaBlackboardVO() {
		super();
	}

	public SalaAulaBlackboardVO(CursoVO cursoVO, TurmaVO turmaVO, DisciplinaVO disciplinaVO, String ano, String semestre) {
		super();
		this.cursoVO = cursoVO;
		this.turmaVO = turmaVO;
		this.disciplinaVO = disciplinaVO;
		this.ano = ano;
		this.semestre = semestre;
	}
	
	public void realizarValidacaoEmailExistente() {
		List<SalaAulaBlackboardPessoaVO> lista = new ArrayList<>();
		lista.addAll(getListaAlunos());
		lista.addAll(getListaOrientadores());
		getListaOrientadores().stream().forEach(p->{
			getListaProfessores().removeIf(pp-> pp.getPessoaEmailInstitucionalVO().getCodigo().equals(p.getPessoaEmailInstitucionalVO().getCodigo()));
			getListaFacilitadores().removeIf(pp-> pp.getPessoaEmailInstitucionalVO().getCodigo().equals(p.getPessoaEmailInstitucionalVO().getCodigo()));
			
		});
		getListaProfessores().stream().forEach(p->getListaFacilitadores().removeIf(pp-> pp.getPessoaEmailInstitucionalVO().getCodigo().equals(p.getPessoaEmailInstitucionalVO().getCodigo())));
		lista.addAll(getListaProfessores());
		lista.addAll(getListaFacilitadores());
		Map<String, List<SalaAulaBlackboardPessoaVO>> mapaPessoa = lista.stream().collect(Collectors.groupingBy(p-> p.getPessoaEmailInstitucionalVO().getEmail()));
		for (Map.Entry<String, List<SalaAulaBlackboardPessoaVO>> map : mapaPessoa.entrySet()) {
			Uteis.checkState(map.getValue().size() > 1, "Existe mais de uma Usário Membro para o mesmo Email "+ map.getKey()+". Por favor verificar a lista de Alunos/Professores/Facilitadores.");
		}
	}

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

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public String getId() {
		if (id == null) {
			id = "";
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getLinkSalaAulaBlackboard() {
		if (linkSalaAulaBlackboard == null) {
			linkSalaAulaBlackboard = "";
		}
		return linkSalaAulaBlackboard;
	}

	public void setLinkSalaAulaBlackboard(String linkSalaAulaBlackboard) {
		this.linkSalaAulaBlackboard = linkSalaAulaBlackboard;
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

	public List<SalaAulaBlackboardPessoaVO> getListaFacilitadores() {
		if (listaFacilitadores == null) {
			listaFacilitadores = new ArrayList<>();
		}
		return listaFacilitadores;
	}

	public void setListaFacilitadores(List<SalaAulaBlackboardPessoaVO> listaFacilitadores) {
		this.listaFacilitadores = listaFacilitadores;
	}

	public List<SalaAulaBlackboardPessoaVO> getListaProfessores() {
		if (listaProfessores == null) {
			listaProfessores = new ArrayList<>();
		}
		return listaProfessores;
	}

	public void setListaProfessores(List<SalaAulaBlackboardPessoaVO> listaProfessores) {
		this.listaProfessores = listaProfessores;
	}

	public List<SalaAulaBlackboardPessoaVO> getListaAlunos() {
		if (listaAlunos == null) {
			listaAlunos = new ArrayList<>();
		}
		return listaAlunos;
	}

	public void setListaAlunos(List<SalaAulaBlackboardPessoaVO> listaAlunos) {
		this.listaAlunos = listaAlunos;
	}


	public ProgramacaoTutoriaOnlineVO getProgramacaoTutoriaOnlineVO() {
		if(programacaoTutoriaOnlineVO == null) {
			programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
		}
		return programacaoTutoriaOnlineVO;
	}

	public void setProgramacaoTutoriaOnlineVO(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) {
		this.programacaoTutoriaOnlineVO = programacaoTutoriaOnlineVO;
	}

	public String getTermId() {
		if (termId == null) {
			termId = "";
		}
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getDataSourceId() {
		if (dataSourceId == null) {
			dataSourceId = "";
		}
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
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

	public GradeCurricularEstagioVO getGradeCurricularEstagioVO() {
		if (gradeCurricularEstagioVO == null) {
			gradeCurricularEstagioVO = new GradeCurricularEstagioVO();
		}
		return gradeCurricularEstagioVO;
	}

	public void setGradeCurricularEstagioVO(GradeCurricularEstagioVO gradeCurricularEstagioVO) {
		this.gradeCurricularEstagioVO = gradeCurricularEstagioVO;
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

	public TipoSalaAulaBlackboardEnum getTipoSalaAulaBlackboardEnum() {
		if (tipoSalaAulaBlackboardEnum == null) {
			tipoSalaAulaBlackboardEnum = TipoSalaAulaBlackboardEnum.DISCIPLINA;
		}
		return tipoSalaAulaBlackboardEnum;
	}

	public void setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum) {
		this.tipoSalaAulaBlackboardEnum = tipoSalaAulaBlackboardEnum;
	}

	public Integer getNrSala() {
		if (nrSala == null) {
			nrSala = 1;
		}
		return nrSala;
	}

	public void setNrSala(Integer nrSala) {
		this.nrSala = nrSala;
	}
	
	

	public Integer getNrGrupo() {
		if (nrGrupo == null) {
			nrGrupo = 0;
		}
		return nrGrupo;
	}

	public void setNrGrupo(Integer nrGrupo) {
		this.nrGrupo = nrGrupo;
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getNome() {
		if(nome == null) {
			nome =  "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdGrupo() {
		if(idGrupo == null) {
			idGrupo =  "";
		}
		return idGrupo;
	}

	public void setIdGrupo(String idGrupo) {
		this.idGrupo = idGrupo;
	}

	public String getGrupoSetId() {
		if(grupoSetId == null) {
			grupoSetId =  "";
		}
		return grupoSetId;
	}

	public void setGrupoSetId(String grupoSetId) {
		this.grupoSetId = grupoSetId;
	}

	public String getGrupoExternalId() {
		if(grupoExternalId == null) {
			grupoExternalId =  "";
		}
		return grupoExternalId;
	}

	public void setGrupoExternalId(String grupoExternalId) {
		this.grupoExternalId = grupoExternalId;
	}

	public String getNomeGrupo() {
		if(nomeGrupo == null) {
			nomeGrupo =  "";
		}
		return nomeGrupo;
	}

	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}

	public String getIdFilterAluno() {
		return getId()+"ALUNO";
	}
	
	public String getIdFilterProfessor() {
		return getId()+"PROF";
	}
	
	public String getIdFilterFacilitador() {
		return getId()+"FAC";
	}
	
	public String getIdFilterOrientador() {
		return getId()+"ORI";
	}

	public DataModelo getDadosConsultaAlunos() {
		if (dadosConsultaAlunos == null) {
			dadosConsultaAlunos = new DataModelo();
			dadosConsultaAlunos.setLimitePorPagina(5);
			dadosConsultaAlunos.setPaginaAtual(0);
			dadosConsultaAlunos.setPage(0);
		}
		return dadosConsultaAlunos;
	}

	public void setDadosConsultaAlunos(DataModelo dadosConsultaAlunos) {
		this.dadosConsultaAlunos = dadosConsultaAlunos;
	}

	public DataModelo getDadosConsultaFacilitadores() {
		if (dadosConsultaFacilitadores == null) {
			dadosConsultaFacilitadores = new DataModelo();
			dadosConsultaFacilitadores.setLimitePorPagina(5);
			dadosConsultaFacilitadores.setPaginaAtual(0);
			dadosConsultaFacilitadores.setPage(0);
		}
		return dadosConsultaFacilitadores;
	}

	public void setDadosConsultaFacilitadores(DataModelo dadosConsultaFacilitadores) {
		this.dadosConsultaFacilitadores = dadosConsultaFacilitadores;
	}

	public Integer getQtdeAlunos() {
		if(qtdeAlunos == null) {
			qtdeAlunos = 0;
		}
		return qtdeAlunos;
	}

	public void setQtdeAlunos(Integer qtdeAlunos) {
		this.qtdeAlunos = qtdeAlunos;
	}

	public Integer getQtdeNotaNaoLocalizada() {
		return qtdeNotaNaoLocalizada;
	}

	public void setQtdeNotaNaoLocalizada(Integer qtdeNotaNaoLocalizada) {
		this.qtdeNotaNaoLocalizada = qtdeNotaNaoLocalizada;
	}

	public List<SalaAulaBlackboardPessoaVO> getListaOrientadores() {
		if (listaOrientadores == null) {
			listaOrientadores = new ArrayList<>();
		}
		return listaOrientadores;
	}

	public void setListaOrientadores(List<SalaAulaBlackboardPessoaVO> listaOrientadores) {
		this.listaOrientadores = listaOrientadores;
	}

	public String getFiltroAluno() {
		if (filtroAluno == null) {
			filtroAluno = "";
		}
		return filtroAluno;
	}

	public void setFiltroAluno(String filtroAluno) {
		this.filtroAluno = filtroAluno;
	}

	public String getFiltroSupervisor() {
		if (filtroSupervisor == null) {
			filtroSupervisor = "";
		}
		return filtroSupervisor;
	}

	public void setFiltroSupervisor(String filtroSupervisor) {
		this.filtroSupervisor = filtroSupervisor;
	}

	public Integer getBimestre() {
		if(bimestre == null) {
			bimestre = 0;
		}
		return bimestre;
	}

	public void setBimestre(Integer bimestre) {
		this.bimestre = bimestre;
	}

	public Integer getQtdAlunosEnsalados() {
		if(qtdAlunosEnsalados == null) {
			qtdAlunosEnsalados = 0;
		}
		return qtdAlunosEnsalados;
	}

	public void setQtdAlunosEnsalados(Integer qtdAlunosEnsalados) {
		this.qtdAlunosEnsalados = qtdAlunosEnsalados;
	}

	public Integer getQtdAlunosNaoEnsalados() {
		if(qtdAlunosNaoEnsalados == null) {
			qtdAlunosNaoEnsalados = 0;
		}
		return qtdAlunosNaoEnsalados;
	}

	public void setQtdAlunosNaoEnsalados(Integer qtdAlunosNaoEnsalados) {
		this.qtdAlunosNaoEnsalados = qtdAlunosNaoEnsalados;
	}

	public String getAlunosEnsalados() {
		if(alunosEnsalados == null) {
			alunosEnsalados = "";
		}
		return alunosEnsalados;
	}

	public void setAlunosEnsalados(String alunosEnsalados) {
		this.alunosEnsalados = alunosEnsalados;
	}

	public String getAlunosNaoEnsalados() {
		if(alunosNaoEnsalados == null) {
			alunosNaoEnsalados = "";
		}
		return alunosNaoEnsalados;
	}

	public void setAlunosNaoEnsalados(String alunosNaoEnsalados) {
		this.alunosNaoEnsalados = alunosNaoEnsalados;
	}

	public Integer getQtdeSalas() {
		if(qtdeSalas == null) {
			qtdeSalas = 0;
		}
		return qtdeSalas;
	}

	public void setQtdeSalas(Integer qtdeSalas) {
		this.qtdeSalas = qtdeSalas;
	}

	public String getSalasExistentes() {
		if(salasExistentes == null) {
			salasExistentes = "";
		}
		return salasExistentes;
	}

	public void setSalasExistentes(String salasExistentes) {
		this.salasExistentes = salasExistentes;
	}

	public Integer getQtdeSalasExistentes() {
		if(qtdeSalasExistentes == null) {
			qtdeSalasExistentes = 0;
		}
		return qtdeSalasExistentes;
	}

	public void setQtdeSalasExistentes(Integer qtdeSalasExistentes) {
		this.qtdeSalasExistentes = qtdeSalasExistentes;
	}

	public Integer getQtdeSalasNecessarias() {
		if(qtdeSalasNecessarias == null) {
			qtdeSalasNecessarias = 0;
		}
		return qtdeSalasNecessarias;
	}

	public void setQtdeSalasNecessarias(Integer qtdeSalasNecessarias) {
		this.qtdeSalasNecessarias = qtdeSalasNecessarias;
	}

	public Integer getAlunosPorSala() {
		if(alunosPorSala == null) {
			alunosPorSala = 0;
		}
		return alunosPorSala;
	}

	public void setAlunosPorSala(Integer alunosPorSala) {
		this.alunosPorSala = alunosPorSala;
	}	
	
	
	public String getIdSalaAulaBlackboardConteudoMaster() {
		return idSalaAulaBlackboardConteudoMaster;
	}

	public void setIdSalaAulaBlackboardConteudoMaster(String idSalaAulaBlackboardConteudoMaster) {
		this.idSalaAulaBlackboardConteudoMaster = idSalaAulaBlackboardConteudoMaster;
	}

	public boolean isConteudoMasterSelecionado() {
		return conteudoMasterSelecionado;
	}

	public void setConteudoMasterSelecionado(boolean conteudoMasterSelecionado) {
		this.conteudoMasterSelecionado = conteudoMasterSelecionado;
	}

	public Boolean getExisteUsuarioLogadoNaSala() {
		return ((List<SalaAulaBlackboardPessoaVO>) getDadosConsultaAlunos().getListaConsulta()).stream().anyMatch(p-> p.isExisteUsuarioLogadoNaSala());
	}
	
	public SalaAulaBlackboardPessoaVO getSalaAulaBlackboardGrupoPessoaVO(UsuarioVO usuarioVO) {
		for (SalaAulaBlackboardPessoaVO sabp : ((List<SalaAulaBlackboardPessoaVO>) getDadosConsultaAlunos().getListaConsulta())) {
			if(sabp.getTipoSalaAulaBlackboardPessoaEnum().isAluno() 
					&& sabp.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo())) {
				return sabp;
			}
		}
		return null;
	}
	
	

	public Boolean getIsVinculaMatriculaPeriodoTurmaDisciplina(){
		return (getTipoSalaAulaBlackboardEnum().isDisciplina() || getTipoSalaAulaBlackboardEnum().isImportacao()
				|| getTipoSalaAulaBlackboardEnum().isProjetoIntegrador() || getTipoSalaAulaBlackboardEnum().equals(TipoSalaAulaBlackboardEnum.NENHUM));
	}
	
	

	public List<HistoricoNotaBlackboardVO> getListaHistoricoNotaBlackboardVOs() {
		if (listaHistoricoNotaBlackboardVOs == null) {
			listaHistoricoNotaBlackboardVOs = new ArrayList<>();
		}
		return listaHistoricoNotaBlackboardVOs;
	}

	public void setListaHistoricoNotaBlackboardVOs(List<HistoricoNotaBlackboardVO> listaHistoricoNotaBlackboardVOs) {
		this.listaHistoricoNotaBlackboardVOs = listaHistoricoNotaBlackboardVOs;
	}

	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if(matriculaPeriodoTurmaDisciplinaVOs == null) {
			matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOs(
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	public String getIdTipoSalaAulaBlackboardEnumPorDisciplina() {
		return "T"+getTipoSalaAulaBlackboardEnum()+"D"+getDisciplinaVO().getCodigo().toString();
	}


}
