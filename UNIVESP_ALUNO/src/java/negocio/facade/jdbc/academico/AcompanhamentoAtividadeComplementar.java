package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.AtividadeComplementarRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EventoAtividadeComplementarVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaPeriodoVO;
import negocio.comuns.academico.RegistroAtividadeComplementarMatriculaVO;
import negocio.comuns.academico.enumeradores.SituacaoAtividadeComplementarMatriculaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.AcompanhamentoAtividadeComplementarInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class AcompanhamentoAtividadeComplementar extends ControleAcesso implements AcompanhamentoAtividadeComplementarInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public AcompanhamentoAtividadeComplementar() {
		super();
		this.setIdEntidade("AcompanhamentoAtividadeComplementar");
	}

	public static String getIdEntidade() {
		return AcompanhamentoAtividadeComplementar.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		AcompanhamentoAtividadeComplementar.idEntidade = idEntidade;
	}

	@Override
	public String getSqlTotalHorasConsideradas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(case when cargaHorariaRealizadaAtividadeComplementar > cargahoraria then cargahoraria else cargaHorariaRealizadaAtividadeComplementar end ) as cargaHorariaRealizadaAtividadeComplementar from ( ");
		sql.append(" select sum(cargahorariaconsiderada) as cargaHorariaRealizadaAtividadeComplementar, ");
		sql.append(" registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sql.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
		sql.append(" from registroatividadecomplementarmatricula ");
		sql.append(" inner join gradecurriculartipoatividadecomplementar on gradecurriculartipoatividadecomplementar.tipoAtividadeComplementar = registroatividadecomplementarmatricula.tipoAtividadeComplementar ");
		sql.append(" where matricula = matricula.matricula and gradecurriculartipoatividadecomplementar.gradecurricular = matricula.gradecurricularatual");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");		
		sql.append(" group by registroatividadecomplementarmatricula.tipoAtividadeComplementar, ");
		sql.append(" gradecurriculartipoatividadecomplementar.cargahoraria ");
		sql.append(" ) as t)	");
		return sql.toString();
	}

	@Override
	public String getSqlTotalHorasRealizadas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");		
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.DEFERIDO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}
	
	@Override
	public String getSqlTotalHorasIndeferido() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula ");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.INDEFERIDO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}
	
	@Override
	public String getSqlTotalHorasAguardandoDeferimento() {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select sum(registroatividadecomplementarmatricula.cargahorariaconsiderada) as totalHorasConsiderada ");
		sql.append(" from registroatividadecomplementarmatricula where matricula = matricula.matricula ");
		sql.append(" and registroatividadecomplementarmatricula.situacaoAtividadeComplementarMatricula = '").append(SituacaoAtividadeComplementarMatriculaEnum.AGUARDANDO_DEFERIMENTO).append("' ");
		sql.append(" ) ");
		return sql.toString();
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultar(Integer curso, Integer codigoTurma, String ano, String semestre, String situacao, String matricula, Integer codigoTipoAtividadeComplementar, SituacaoAtividadeComplementarMatriculaEnum situacaoAtividadeComplementarMatriculaEnum, boolean controlarAcesso,Integer codigoGradeCurricular  ,DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT DISTINCT COUNT(*) OVER() AS totalRegistroConsulta,  matricula.matricula, pessoa.nome , ");
		sql.append(" gradecurricular.totalCargaHorariaAtividadeComplementar as cargahorariaexigida, ");
		sql.append(getSqlTotalHorasRealizadasAtividadeComplementar(matricula,codigoGradeCurricular)).append(" as cargahorariaconsiderada, ");
		sql.append(getSqlTotalHorasRealizadas()).append(" as cargahorariarealizada, ");
		sql.append(getSqlTotalHorasIndeferido()).append(" as cargaHorariaIndeferido, ");
		sql.append(getSqlTotalHorasAguardandoDeferimento()).append(" as cargaHorariaAguardandoDeferimento, ");
		sql.append(getSqlTotalHorasAtividadeComplementarPendente(matricula , codigoGradeCurricular)).append(" as cargaHorariaPendente, ");
		sql.append(" matricula.gradecurricularAtual");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') order by mp.ano||'/'||mp.semestre desc limit 1) ");
		sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
//		sql.append(" where gradecurricular.totalCargaHorariaAtividadeComplementar > 0 ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" AND matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(codigoTurma)) {
			sql.append(" AND matriculaperiodo.turma=").append(codigoTurma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula= '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(codigoTipoAtividadeComplementar)) {
			sql.append(" AND exists (select matricula from registroAtividadeComplementarMatricula where registroAtividadeComplementarMatricula.matricula = matricula.matricula and registroAtividadeComplementarMatricula.tipoAtividadeComplementar=").append(codigoTipoAtividadeComplementar).append(" limit 1) ");
		}
		
		if (situacao != null && situacao.equals("AT")) {
			sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo in ('AT','FI','CO') ");
		}
		
		if(Uteis.isAtributoPreenchido(situacaoAtividadeComplementarMatriculaEnum)) {
			sql.append(" AND exists (select matricula from registroAtividadeComplementarMatricula where registroAtividadeComplementarMatricula.matricula = matricula.matricula and registroAtividadeComplementarMatricula.situacaoAtividadeComplementarMatricula= '").append(situacaoAtividadeComplementarMatriculaEnum.name()).append("' limit 1) ");
		}
		if (Uteis.isAtributoPreenchido(situacao) && (situacao.equals("CO") || situacao.equals("PF"))) {
			if (situacao.equals("CO")) {
				sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo = 'FI' ");
			} else {
				sql.append(" AND matriculaperiodo.situacaoMatriculaPeriodo = 'AT' ");
			}
			sql.append(" and 0 = (select count(codigo) from matriculaperiodo mp where mp.matricula = matriculaperiodo.matricula   ");
			sql.append(" and mp.situacaomatriculaperiodo not in ('PC')  and mp.codigo != matriculaperiodo.codigo ");
			sql.append(" and (mp.ano||'/'||mp.semestre) > (matriculaperiodo.ano||'/'||matriculaperiodo.semestre)) ");

			sql.append(" and (select count(distinct disciplina.codigo) from historico ");
			sql.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
			sql.append(" where disciplina.tipoDisciplina in ('OB', 'LG')  ");
			sql.append(" and historico.matricula = matricula.matricula ");
			sql.append(" and historico.situacao in ('AA', 'AP','CS', 'CC') ) ");
			sql.append(" >= (select count(distinct disciplina.codigo) from gradecurricular ");
			sql.append(" INNER JOIN periodoletivo AS pr ON gradecurricular.codigo = pr.gradecurricular ");
			sql.append(" INNER join gradedisciplina AS grDisc on grDisc.periodoletivo = pr.codigo  ");
			sql.append(" INNER join disciplina on grDisc.disciplina = disciplina.codigo  ");
			sql.append(" where gradecurricular.codigo =  matricula.gradecurricularatual  ");
			sql.append(" and disciplina.tipoDisciplina in ('OB', 'LG') ");
			sql.append(" ) ");
		}
		sql.append(" group by matricula.matricula, pessoa.nome, gradecurricular.totalCargaHorariaAtividadeComplementar , matricula.gradecurricularAtual ");
		sql.append(" order by nome, matricula ");
		
		if (Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
			sql.append(" LIMIT ").append(dataModelo.getLimitePorPagina()).append(" OFFSET ").append(dataModelo.getOffset());
		}
		
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
		if (tabelaResultado.next()) {			
			dataModelo.setTotalRegistrosEncontrados((tabelaResultado.getInt("totalRegistroConsulta")));
		}
		tabelaResultado.beforeFirst();	
		return (this.montarDadosConsulta(tabelaResultado, usuario));
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		// getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		return consultarPorMatriculaVisaoAluno(matricula, null, controlarAcesso, usuario);
	}

	public List<RegistroAtividadeComplementarMatriculaVO> consultarPorMatriculaVisaoAluno(String matricula, Integer codigoTipoAtividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List<AtividadeComplementarRelVO> atividadeComplementarRelVOs = getFacadeFactory().getAtividadeComplementarRelFacade().criarObjetoLayoutAtividadeComplementarAnalitico(null, null, matricula, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "matricula");
		Map<Integer, RegistroAtividadeComplementarMatriculaVO> registroAtividadeComplementarMatriculaVOs = new HashMap<Integer, RegistroAtividadeComplementarMatriculaVO>(0);
		for (AtividadeComplementarRelVO atividadeComplementarRelVO : atividadeComplementarRelVOs) {
			Ordenacao.ordenarListaDecrescente(atividadeComplementarRelVO.getListaEventoAtividadeComplementar(), "dataEvento");
		}
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(matricula, false, usuario);
		for (AtividadeComplementarRelVO atividadeComplementarRelVO : atividadeComplementarRelVOs) {
			for (EventoAtividadeComplementarVO eventoAtividadeComplementarVO : atividadeComplementarRelVO.getListaEventoAtividadeComplementar()) {
				if (codigoTipoAtividadeComplementar == null || codigoTipoAtividadeComplementar.intValue() == 0 || eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar().equals(codigoTipoAtividadeComplementar)) {
					String ano = cursoVO.getIntegral() ? "" : Uteis.getAno(eventoAtividadeComplementarVO.getDataEvento());
					String semestre = cursoVO.getSemestral() ?  Uteis.getSemestreData(eventoAtividadeComplementarVO.getDataEvento()) : "";
					if (registroAtividadeComplementarMatriculaVOs.containsKey(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar())) {
						RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO = registroAtividadeComplementarMatriculaVOs.get(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar());
						if(!registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().containsKey(ano+semestre)) {
							RegistroAtividadeComplementarMatriculaPeriodoVO  registroAtividadeComplementarMatriculaPeriodoVO = new RegistroAtividadeComplementarMatriculaPeriodoVO(ano, semestre);
							registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().put(ano+semestre, registroAtividadeComplementarMatriculaPeriodoVO);
							registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementarMatriculaPeriodoVOs().add(registroAtividadeComplementarMatriculaPeriodoVO);
						}
						RegistroAtividadeComplementarMatriculaPeriodoVO registroAtividadeComplementarMatriculaPeriodoVO = registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().get(ano+semestre);
						registroAtividadeComplementarMatriculaVO.setCargaHorariaEvento(registroAtividadeComplementarMatriculaVO.getCargaHorariaEvento() + eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaEvento(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaEvento() + eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaPeriodoVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setArquivoVO(eventoAtividadeComplementarVO.getArquivoVO());
						registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs().add(eventoAtividadeComplementarVO);
					} else {
						RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaVO = new RegistroAtividadeComplementarMatriculaVO();
						if(!registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().containsKey(ano+semestre)) {
							RegistroAtividadeComplementarMatriculaPeriodoVO  registroAtividadeComplementarMatriculaPeriodoVO = new RegistroAtividadeComplementarMatriculaPeriodoVO(ano, semestre);
							registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().put(ano+semestre, registroAtividadeComplementarMatriculaPeriodoVO);
							registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementarMatriculaPeriodoVOs().add(registroAtividadeComplementarMatriculaPeriodoVO);
						}
						RegistroAtividadeComplementarMatriculaPeriodoVO registroAtividadeComplementarMatriculaPeriodoVO = registroAtividadeComplementarMatriculaVO.getMapRegistroAtividadeComplementarMatriculaPeriodoVOs().get(ano+semestre);
						registroAtividadeComplementarMatriculaVO.getMatriculaVO().setMatricula(atividadeComplementarRelVO.getMatricula());
						registroAtividadeComplementarMatriculaVO.getMatriculaVO().getAluno().setNome(atividadeComplementarRelVO.getNomeAluno());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaEvento(eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaExigida(eventoAtividadeComplementarVO.getCargaHorariaMaximaConsiderada());
						registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().setCodigo(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar());
						registroAtividadeComplementarMatriculaVO.getTipoAtividadeComplementarVO().setNome(eventoAtividadeComplementarVO.getAtividadeComplementar());
						registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().getResponsavelUltimaAlteracao().getPessoa().setNome(eventoAtividadeComplementarVO.getResponsavelUltimaAlteracao());
						registroAtividadeComplementarMatriculaVO.getRegistroAtividadeComplementar().setDataUltimaAlteracao(eventoAtividadeComplementarVO.getDataUltimaAlteracao());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaConsiderada(eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaRealizada(eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaPeriodoVO.setCargaHorariaEvento(eventoAtividadeComplementarVO.getCargaHorariaEvento());
						registroAtividadeComplementarMatriculaVO.setArquivoVO(eventoAtividadeComplementarVO.getArquivoVO());
						registroAtividadeComplementarMatriculaVO.setObservacao(eventoAtividadeComplementarVO.getObservacao());
						registroAtividadeComplementarMatriculaVO.setCodigo(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getCodigo());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaRealizada(registroAtividadeComplementarMatriculaVO.getCargaHorariaRealizada() + eventoAtividadeComplementarVO.getCargaHorariaRealizada());
						registroAtividadeComplementarMatriculaVO.setCargaHorariaConsiderada(registroAtividadeComplementarMatriculaVO.getCargaHorariaConsiderada() + eventoAtividadeComplementarVO.getCargaHorariaConsiderada());
						registroAtividadeComplementarMatriculaVO.setDataDeferimentoIndeferimento(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getDataDeferimentoIndeferimento());
						if(Uteis.isAtributoPreenchido(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelDeferimentoIndeferimento().getCodigo())) {
							registroAtividadeComplementarMatriculaVO.setResponsavelDeferimentoIndeferimento(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelDeferimentoIndeferimento().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
						}
						registroAtividadeComplementarMatriculaVO.setDataEditarCHConsiderada(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getDataEditarCHConsiderada());
						registroAtividadeComplementarMatriculaVO.setJustificativaAlteracaoCHConsiderada(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getJustificativaAlteracaoCHConsiderada());
						if(Uteis.isAtributoPreenchido(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo()) && !eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo().equals(0)) {
							registroAtividadeComplementarMatriculaVO.setResponsavelEditarCHConsiderada(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(eventoAtividadeComplementarVO.getRegistroAtividadeComplementarMatriculaVO().getResponsavelEditarCHConsiderada().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
						}
						registroAtividadeComplementarMatriculaPeriodoVO.getEventoAtividadeComplementarVOs().add(eventoAtividadeComplementarVO);
						registroAtividadeComplementarMatriculaVOs.put(eventoAtividadeComplementarVO.getCodigoTipoAtividadeComplementar(), registroAtividadeComplementarMatriculaVO);
					}
				}
			}
		}
		List<RegistroAtividadeComplementarMatriculaVO> listaFinal = new ArrayList<RegistroAtividadeComplementarMatriculaVO>();
		listaFinal.addAll(registroAtividadeComplementarMatriculaVOs.values());
		
		return listaFinal;
	}

	public List<RegistroAtividadeComplementarMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(this.montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	// public List<RegistroAtividadeComplementarMatriculaVO>
	// montarDadosConsultaPorMatricula(SqlRowSet tabelaResultado, UsuarioVO
	// usuario) throws Exception {
	// List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new
	// ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
	// while (tabelaResultado.next()) {
	// vetResultado.add(this.montarDadosPorMatricula(tabelaResultado, usuario));
	// }
	// return vetResultado;
	// }

	// public List<RegistroAtividadeComplementarMatriculaVO>
	// montarDadosConsultaPorMatriculaVisaoAluno(SqlRowSet tabelaResultado,
	// UsuarioVO usuario) throws Exception {
	// List<RegistroAtividadeComplementarMatriculaVO> vetResultado = new
	// ArrayList<RegistroAtividadeComplementarMatriculaVO>(0);
	// while (tabelaResultado.next()) {
	// vetResultado.add(this.montarDadosPorMatriculaVisaoAluno(tabelaResultado,
	// usuario));
	// }
	// return vetResultado;
	// }

	public RegistroAtividadeComplementarMatriculaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
		obj.setCargaHorariaExigida(dadosSQL.getInt("cargaHorariaExigida"));
		obj.setCargaHorariaRealizada(dadosSQL.getInt("cargaHorariaRealizada"));
		obj.setCargaHorariaIndeferido(dadosSQL.getInt("cargaHorariaIndeferido"));
		obj.setCargaHorariaAguardandoDeferimento(dadosSQL.getInt("cargaHorariaAguardandoDeferimento"));
		if (dadosSQL.getInt("cargaHorariaConsiderada") > dadosSQL.getInt("cargaHorariaExigida")) {
			obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaExigida"));
		} else {
			obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
		}
		obj.getMatriculaVO().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularAtual"));
		obj.setAtividadeComplementarIntegraliazada(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().realizarValidacaoIntegracaoAtividadeComplementar(obj.getMatriculaVO().getMatricula(), obj.getMatriculaVO().getGradeCurricularAtual().getCodigo()));
		obj.setCargaHorariaPendente((int)(dadosSQL.getDouble("cargaHorariaPendente")));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}

	// public RegistroAtividadeComplementarMatriculaVO
	// montarDadosPorMatricula(SqlRowSet dadosSQL, UsuarioVO usuario) throws
	// Exception {
	// RegistroAtividadeComplementarMatriculaVO obj = new
	// RegistroAtividadeComplementarMatriculaVO();
	// obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
	// obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
	// obj.getTipoAtividadeComplementarVO().setCodigo(dadosSQL.getInt("tipoAtividadeComplementarCodigo"));
	// obj.getTipoAtividadeComplementarVO().setNome(dadosSQL.getString("tipoAtividadeComplementarNome"));
	// obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
	// obj.setCargaHorariaExigida(dadosSQL.getInt("cargaHorariaExigida"));
	// if (obj.getCargaHorariaConsiderada() > obj.getCargaHorariaExigida()) {
	// obj.setCargaHorariaPendente(0);
	// } else {
	// obj.setCargaHorariaPendente(obj.getCargaHorariaExigida() -
	// obj.getCargaHorariaConsiderada());
	// }
	//
	// obj.setNovoObj(Boolean.FALSE);
	// return obj;
	// }

	// public RegistroAtividadeComplementarMatriculaVO
	// montarDadosPorMatriculaVisaoAluno(SqlRowSet dadosSQL, UsuarioVO usuario)
	// throws Exception {
	// RegistroAtividadeComplementarMatriculaVO obj = new
	// RegistroAtividadeComplementarMatriculaVO();
	// obj.getRegistroAtividadeComplementar().setCodigo(dadosSQL.getInt("codigo"));
	// obj.getRegistroAtividadeComplementar().setNomeEvento(dadosSQL.getString("nomeEvento"));
	// if (dadosSQL.getTimestamp("data") == null) {
	// obj.getRegistroAtividadeComplementar().setData(dadosSQL.getTimestamp("data"));
	// } else {
	// obj.getRegistroAtividadeComplementar().setData(new
	// Date(dadosSQL.getTimestamp("data").getTime()));
	// }
	// obj.setCodigo(dadosSQL.getInt("registroATividadeComplementarMatricula"));
	// obj.setCargaHorariaEvento(dadosSQL.getInt("cargaHorariaEvento"));
	// obj.setCargaHorariaConsiderada(dadosSQL.getInt("cargaHorariaConsiderada"));
	// obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
	// if (!obj.getArquivoVO().getCodigo().equals(0)) {
	// obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	// }
	// obj.setNovoObj(Boolean.FALSE);
	// return obj;
	// }

	public static void validarConsulta(Integer codigoCurso) throws ConsistirException {
		if (codigoCurso == null || codigoCurso.equals(0)) {
			throw new ConsistirException("O campo CURSO deve ser informado");
		}
	}

	public static void validarDadosFiltroConsulta(Integer curso, Integer turma, String matricula, String periodicidade, String semestre, String ano) throws ConsistirException {
		if (turma > 0 && periodicidade != null && periodicidade.equals("AN") && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException("O campo Ano deve ser informado");
		}
		if (turma > 0 && periodicidade != null && periodicidade.equals("SE") && !Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException("O campo Semestre deve ser selecionado");
		}
	}
	
	private String getSqlTotalHorasRealizadasAtividadeComplementar(String matricula , Integer matrizCurricular) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select * from ( ");
		sqlStr.append(" select sum(cargaHorariaConsiderada) as cargaHorariaConsiderada ");
		sqlStr.append(" from (");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistroAtividadeComplementarMatricula(matricula, matrizCurricular, sqlStr);
		sqlStr.append("	union all ");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sqlStr);
		sqlStr.append(") as atividade ");
		sqlStr.append(" ) as resultado ");
		sqlStr.append(" where (resultado.cargahorariaconsiderada is null) = false ");
		sqlStr.append(" ) ");
		return sqlStr.toString();
	}
	
	private String getSqlTotalHorasAtividadeComplementarPendente(String matricula , Integer matrizCurricular) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" (select * from ( ");
		sqlStr.append(" select case when sum(cargahorariapendente-cargahorariaexcedida) < 0 then  0 else sum(cargahorariapendente-cargahorariaexcedida) end as cargahorariapendente");
		sqlStr.append(" from (");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarRegistroAtividadeComplementarMatricula(matricula, matrizCurricular, sqlStr);
		sqlStr.append("	union all ");
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarHorasMinimaAtividadeComplementar(matricula, matrizCurricular, sqlStr);
		sqlStr.append(") as atividade ");
		sqlStr.append(" ) as resultado ");
		sqlStr.append(" where (resultado.cargahorariapendente is null) = false ");
		sqlStr.append(" ) ");
		return sqlStr.toString();
	}

	public void validarDadosCargaHorariaRealizada(RegistroAtividadeComplementarMatriculaVO registroAtividadeComplementarMatriculaIncluirVO, List<RegistroAtividadeComplementarMatriculaVO> ListaConsultaRegistroAtividadeComplementarMatriculaVOs, List<RegistroAtividadeComplementarMatriculaVO> listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs) throws Exception {
		Integer atvComplementar = registroAtividadeComplementarMatriculaIncluirVO.getTipoAtividadeComplementarVO().getCodigo();
		if(ListaConsultaRegistroAtividadeComplementarMatriculaVOs.stream().anyMatch(c -> c.getTipoAtividadeComplementarVO().getCodigo().equals(atvComplementar)) || listaConsultaRegistroAtividadeComplementarMatriculaAguardandoDeferimentoVOs.stream().anyMatch(c -> c.getTipoAtividadeComplementarVO().getCodigo().equals(atvComplementar))){
			throw new Exception("Já existe um registro de atividade complementar (" + registroAtividadeComplementarMatriculaIncluirVO.getTipoAtividadeComplementarVO().getNome() + "), por isto não é possível realizar um novo cadastro.");
		}
		if (registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada() > 9999) {
			throw new Exception("C.H Realizada deve ser menor que 9999.");
		}
		if (registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada() <= 0) {
			throw new Exception("C.H Realizada deve ser maior que zero.");
		}
		if (!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaIncluirVO.getCargaHorariaConsiderada())) {
			throw new Exception("C.H Realizada deve ser informada.");
		}
		if (!Uteis.isAtributoPreenchido(registroAtividadeComplementarMatriculaIncluirVO.getArquivoVO().getDescricao())) {
			throw new Exception("O campo ARQUIVO deve ser informado.");
		}
	}
	
	public RegistroAtividadeComplementarMatriculaVO consultarCargaHorariaTotal(String matricula, boolean controlarAcesso, Integer codigoGradeCurricular, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT	gradecurricular.totalCargaHorariaAtividadeComplementar as cargahorariaexigida,");
		sql.append(getSqlTotalHorasRealizadasAtividadeComplementar(matricula,codigoGradeCurricular)).append(" as cargahorariaconsiderada, ");
		sql.append(getSqlTotalHorasIndeferido()).append(" as cargaHorariaIndeferido, ");
		sql.append(getSqlTotalHorasAguardandoDeferimento()).append(" as cargaHorariaAguardandoDeferimento, ");
		sql.append(getSqlTotalHorasAtividadeComplementarPendente(matricula , codigoGradeCurricular)).append(" as cargaHorariaPendente, ");
		sql.append(getSqlTotalHorasRealizadas()).append(" as cargahorariarealizada ");
		sql.append(" FROM matricula ");
		sql.append(" INNER JOIN gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sql.append(" INNER JOIN pessoa on pessoa.codigo = matricula.aluno ");
		if (matricula != null && !matricula.equals("")) {
			sql.append(" AND matricula.matricula= '").append(matricula).append("' ");
		}		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		RegistroAtividadeComplementarMatriculaVO obj = new RegistroAtividadeComplementarMatriculaVO();
		if (tabelaResultado.next()) {
			obj.setCargaHorariaExigida(tabelaResultado.getInt("cargahorariaexigida"));
			obj.setCargaHorariaConsiderada(tabelaResultado.getInt("cargahorariaconsiderada"));
			obj.setCargaHorariaIndeferido(tabelaResultado.getInt("cargaHorariaIndeferido"));
			obj.setCargaHorariaAguardandoDeferimento(tabelaResultado.getInt("cargaHorariaAguardandoDeferimento"));
			obj.setCargaHorariaPendente((int)(tabelaResultado.getDouble("cargaHorariaPendente")));
			obj.setCargaHorariaRealizada(tabelaResultado.getInt("cargaHorariaRealizada"));
		}
		return obj;
	}
}
