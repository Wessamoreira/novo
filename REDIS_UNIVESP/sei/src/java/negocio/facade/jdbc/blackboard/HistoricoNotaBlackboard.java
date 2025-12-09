package negocio.facade.jdbc.blackboard;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import jobs.enumeradores.TipoUsoNotaEnum;
import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.blackboard.HistoricoNotaBlackboardVO;
import negocio.comuns.blackboard.SalaAulaBlackboardNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaNotaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.interfaces.blackboard.HistoricoNotaBlackboardInterfaceFacade;

@Service
@Scope
@Lazy
public class HistoricoNotaBlackboard extends SuperFacade<HistoricoNotaBlackboardVO> implements HistoricoNotaBlackboardInterfaceFacade<HistoricoNotaBlackboardVO> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7486467405222747728L;
	protected static String idEntidade;

    @PostConstruct
    public void init() {
        setIdEntidade("HistoricoNotaBlackboard");
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(HistoricoNotaBlackboardVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
        if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
               incluir(obj, validarAcesso, usuarioVO);
           } else {
               alterar(obj, validarAcesso, usuarioVO);
        }
    }
    

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(HistoricoNotaBlackboardVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
        HistoricoNotaBlackboard.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		try {
			incluir(obj, "HistoricoNotaBlackboard",
		            new AtributoPersistencia().add("salaAulaBlackboardPessoa", obj.getSalaAulaBlackboardPessoaVO())
		                    .add("salaAulaBlackboard", obj.getSalaAulaBlackboardVO())
		                    .add("notaAnterior", obj.getNotaAnterior())
		                    .add("nota", obj.getNota())
		                    .add("historico", obj.getHistoricoVO())
		                    .add("situacaoHistoricoNotaBlackboardEnum", obj.getSituacaoHistoricoNotaBlackboardEnum().name())
		                    .add("motivo", obj.getMotivo())
		                    .add("nomePessoaBlackboard", obj.getNomePessoaBlackboard())
		                    .add("emailPessoaBlackboard", obj.getEmailPessoaBlackboard())
		                    .add("motivoDeferimentoIndeferimento", obj.getMotivoDeferimentoIndeferimento())
		                    .add("usuarioResponsavel", Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getCodigo()) ? obj.getUsuarioResponsavel().getCodigo() : null)
		                    .add("dataDeferimentoIndeferimento", Uteis.getDataJDBCTimestamp(obj.getDataDeferimentoIndeferimento()))
		                    .add("created", Uteis.getDataJDBCTimestamp(obj.getCreated()))
		                    .add("codigoCreated", usuarioVO.getCodigo()),
		            usuarioVO);
		    obj.setNovoObj(Boolean.TRUE);
		} catch (Exception e) {
			obj.setCodigo(0);
			throw e;
		}
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(HistoricoNotaBlackboardVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
        HistoricoNotaBlackboard.alterar(getIdEntidade(), validarAcesso, usuarioVO);
        obj.setUpdated(new Date());
        obj.setCodigoUpdated(usuarioVO.getCodigo());
        try {
        	alterar(obj, "HistoricoNotaBlackboard",
                    new AtributoPersistencia().add("salaAulaBlackboardPessoa", obj.getSalaAulaBlackboardPessoaVO())
                    		.add("salaAulaBlackboard", obj.getSalaAulaBlackboardVO())        
                    		.add("notaAnterior", obj.getNotaAnterior())
                            .add("nota", obj.getNota())
                            .add("historico", obj.getHistoricoVO())
                            .add("situacaoHistoricoNotaBlackboardEnum", obj.getSituacaoHistoricoNotaBlackboardEnum().name())
                            .add("motivo", obj.getMotivo())
                            .add("nomePessoaBlackboard", obj.getNomePessoaBlackboard())
		                    .add("emailPessoaBlackboard", obj.getEmailPessoaBlackboard())
                            .add("motivoDeferimentoIndeferimento", obj.getMotivoDeferimentoIndeferimento())
                            .add("usuarioResponsavel", Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getCodigo()) ? obj.getUsuarioResponsavel().getCodigo() : null)
                            .add("dataDeferimentoIndeferimento", Uteis.getDataJDBCTimestamp(obj.getDataDeferimentoIndeferimento()))
                            .add("updated", Uteis.getDataJDBCTimestamp(obj.getUpdated()))
                            .add("codigoUpdated", usuarioVO.getCodigo()),
                    new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioVO);
            obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
    }
    

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HistoricoNotaBlackboardVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder("DELETE FROM HistoricoNotaBlackboard WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
        getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deferirIndeferirNota(HistoricoNotaBlackboardVO obj, SituacaoHistoricoNotaBlackboardEnum situacaoHistoricoNotaBlackboardEnum, boolean realizarCalculoMediaApuracaoNotas,  UsuarioVO usuarioVO) throws Exception {
        if (SituacaoHistoricoNotaBlackboardEnum.DEFERIDO.equals(situacaoHistoricoNotaBlackboardEnum)) {
            HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimaria(obj.getHistoricoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null, usuarioVO);
            ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO = getFacadeFactory().getConfiguracaoAcademicoNotaFacade().consultarPorConfiguracaoAcademicoPorTipoUsoNotaEnum(historicoVO.getConfiguracaoAcademico(), TipoUsoNotaEnum.BLACKBOARD);
            Field nota = HistoricoVO.class.getDeclaredField(StringUtils.join("nota", configuracaoAcademicaNotaVO.getNota().getNumeroNota()));
            nota.setAccessible(true);
            FieldUtils.writeField(nota, historicoVO, obj.getNota());
            getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(Collections.singletonList(historicoVO), true,  realizarCalculoMediaApuracaoNotas, usuarioVO);
        }
        obj.setSituacaoHistoricoNotaBlackboardEnum(situacaoHistoricoNotaBlackboardEnum);
        obj.setUsuarioResponsavel(usuarioVO);
        persistir(obj, false, usuarioVO);
    }
    
    
    private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) over() as totalRegistroConsulta, ");
		sql.append(" historiconotablackboard.codigo as \"historiconotablackboard.codigo\", historiconotablackboard.nomepessoablackboard as \"historiconotablackboard.nomepessoablackboard\", ");
		sql.append(" historiconotablackboard.emailpessoablackboard as \"historiconotablackboard.emailpessoablackboard\", historiconotablackboard.nota as \"historiconotablackboard.nota\", ");
		sql.append(" historiconotablackboard.notaAnterior as \"historiconotablackboard.notaAnterior\", historiconotablackboard.situacaoHistoricoNotaBlackboardEnum as \"historiconotablackboard.situacaoHistoricoNotaBlackboardEnum\", ");
		sql.append(" historiconotablackboard.motivo as \"historiconotablackboard.motivo\", historiconotablackboard.motivoDeferimentoIndeferimento as \"historiconotablackboard.motivoDeferimentoIndeferimento\", ");
		sql.append(" historiconotablackboard.salaAulaBlackboardPessoa as \"historiconotablackboard.salaAulaBlackboardPessoa\", historiconotablackboard.salaAulaBlackboard as \"historiconotablackboard.salaAulaBlackboard\", ");
		sql.append(" historiconotablackboard.historico as \"historiconotablackboard.historico\", historiconotablackboard.dataDeferimentoIndeferimento as \"historiconotablackboard.dataDeferimentoIndeferimento\", ");
		sql.append(" historiconotablackboard.historico as \"historiconotablackboard.historico\", historiconotablackboard.dataDeferimentoIndeferimento as \"historiconotablackboard.dataDeferimentoIndeferimento\", ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\" ");
		sql.append(" from historiconotablackboard ");
		sql.append(" left join  usuario on  usuario.codigo = historiconotablackboard.usuarioResponsavel ");
		

		return sql;
	}
    
    private StringBuilder getSQLPadraoConsultaCompleta() {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" select count(*) over() as totalRegistroConsulta, ");
    	sql.append(" historiconotablackboard.codigo as \"historiconotablackboard.codigo\", historiconotablackboard.nomepessoablackboard as \"historiconotablackboard.nomepessoablackboard\", ");
    	sql.append(" historiconotablackboard.emailpessoablackboard as \"historiconotablackboard.emailpessoablackboard\", historiconotablackboard.nota as \"historiconotablackboard.nota\", ");
    	sql.append(" historiconotablackboard.datadeferimentoindeferimento AS \"historiconotablackboard.datadeferimentoindeferimento\", historiconotablackboard.nomecreated AS \"usuarioresponsavel.nome\", ");
    	sql.append(" historiconotablackboard.motivo as \"historiconotablackboard.motivo\",");
    	sql.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", salaaulablackboard.ano as \"salaaulablackboard.ano\", ");
    	sql.append(" salaaulablackboard.semestre as \"salaaulablackboard.semestre\",   ");
    	sql.append(" salaaulablackboard.bimestre as \"salaaulablackboard.bimestre\",   ");
    	sql.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\",  ");
    	sql.append(" salaaulablackboard.id as \"salaaulablackboard.id\",  ");
    	sql.append(" salaaulablackboard.linkSalaAulaBlackboard as \"salaaulablackboard.linkSalaAulaBlackboard\",  ");
    	sql.append(" salaaulablackboard.termId as \"salaaulablackboard.termId\",  ");
    	sql.append(" salaaulablackboard.tipoSalaAulaBlackboardEnum as \"salaaulablackboard.tipoSalaAulaBlackboardEnum\",  ");
    	sql.append(" salaaulablackboard.nrSala as \"salaaulablackboard.nrSala\",  ");
    	sql.append(" salaaulablackboard.nome as \"salaaulablackboard.nome\", ");
    	sql.append(" salaaulablackboard.nomeGrupo as \"salaaulablackboard.nomeGrupo\", ");
    	sql.append(" salaaulablackboard.grupoExternalId as \"salaaulablackboard.grupoExternalId\", ");
    	sql.append(" salaaulablackboard.grupoSetId as \"salaaulablackboard.grupoSetId\", ");
    	sql.append(" salaaulablackboard.idGrupo as \"salaaulablackboard.idGrupo\", ");
    	sql.append(" salaaulablackboard.nrGrupo as \"salaaulablackboard.nrGrupo\", ");
    	sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
    	sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\", ");
    	sql.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\", disciplina.abreviatura as \"disciplina.abreviatura\", ");
    	sql.append(" gradecurricularestagio.codigo as \"gradecurricularestagio.codigo\", gradecurricularestagio.nome as \"gradecurricularestagio.nome\", ");
    	sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.razaosocial as \"unidadeensino.razaosocial\" ");
    	sql.append(" from historiconotablackboard ");
    	sql.append(" inner join  salaaulablackboard on  salaaulablackboard.codigo = historiconotablackboard.salaaulablackboard ");
    	sql.append(" left join curso on curso.codigo =  salaaulablackboard.curso ");
    	sql.append(" left join turma on turma.codigo =  salaaulablackboard.turma ");
    	sql.append(" left join unidadeensino on unidadeensino.codigo =  turma.unidadeensino ");
    	sql.append(" left join disciplina on disciplina.codigo =  salaaulablackboard.disciplina ");
    	sql.append(" left join gradecurricularestagio on gradecurricularestagio.codigo =  salaaulablackboard.gradecurricularestagio ");
    	
    	return sql;
    }
    
    @Override
    public void consultar(DataModelo controleConsulta, HistoricoNotaBlackboardVO obj, Boolean usarLimiteConsulta, UsuarioVO usuarioVO) throws Exception {
    	controleConsulta.setListaFiltros(new ArrayList<>());
    	StringBuilder sql = getSQLPadraoConsultaCompleta();
    	sql.append(" where  historiconotablackboard.situacaohistoriconotablackboardenum in (?,?) ");
    	controleConsulta.getListaFiltros().add(SituacaoHistoricoNotaBlackboardEnum.NAO_LOCALIZADO.name());
    	controleConsulta.getListaFiltros().add(SituacaoHistoricoNotaBlackboardEnum.ERRO.name());
    	if(Uteis.isAtributoPreenchido(obj.getSalaAulaBlackboardVO())) {
    		sql.append(" and  historiconotablackboard.salaaulablackboard = ? ");
    		controleConsulta.getListaFiltros().add(obj.getSalaAulaBlackboardVO().getCodigo());
    	}
    	if(Uteis.isAtributoPreenchido(obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard())) {
    		sql.append(" and  salaaulablackboard.idSalaAulaBlackboard ilike (?) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getSalaAulaBlackboardVO().getIdSalaAulaBlackboard() + PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(obj.getSalaAulaBlackboardVO().getDisciplinaVO().getAbreviatura())) {
    		sql.append(" and  disciplina.abreviatura ilike (sem_acentos(?)) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getSalaAulaBlackboardVO().getDisciplinaVO().getAbreviatura() + PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(obj.getNomePessoaBlackboard())) {
    		sql.append(" and  historiconotablackboard.nomepessoablackboard ilike (sem_acentos(?)) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getNomePessoaBlackboard() + PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(obj.getEmailPessoaBlackboard())) {
    		sql.append(" and  historiconotablackboard.emailpessoablackboard ilike (sem_acentos(?)) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getEmailPessoaBlackboard() + PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(obj.getDataDeferimentoIndeferimentoFiltro())) {
    		sql.append(" and  historiconotablackboard.datadeferimentoindeferimento BETWEEN ? and ? ");
    		controleConsulta.getListaFiltros().add(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(obj.getDataDeferimentoIndeferimentoFiltro())));
    		controleConsulta.getListaFiltros().add(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(obj.getDataDeferimentoIndeferimentoFiltro())));
    	}
    	if(Uteis.isAtributoPreenchido(obj.getUsuarioResponsavel().getNome())) {
    		sql.append(" and  historiconotablackboard.nomecreated ilike (sem_acentos(?)) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getUsuarioResponsavel().getNome() + PERCENT);
    	}
    	if(Uteis.isAtributoPreenchido(obj.getMotivo())) {
    		sql.append(" and historiconotablackboard.motivo ilike (?) ");
    		controleConsulta.getListaFiltros().add(PERCENT + obj.getMotivo() + PERCENT);
    	}
    	sql.append(" order by historiconotablackboard.datadeferimentoindeferimento desc, salaaulablackboard.nome, salaaulablackboard.nrsala,  salaaulablackboard.nrgrupo, historiconotablackboard.nomepessoablackboard  ");
    	if(usarLimiteConsulta){
    	sql.append(Uteis.limitOffset(controleConsulta.getLimitePorPagina(), controleConsulta.getOffset()));
    	}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), controleConsulta.getListaFiltros().toArray());
        montarTotalizadorConsultaBasica(controleConsulta, tabelaResultado);
        controleConsulta.setListaConsulta(montarDadosConsultaOtimizado(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    @Override
    public List<HistoricoNotaBlackboardVO> consultarHistoricoNotaBlackboardPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, UsuarioVO usuario) {
    	StringBuilder sql = getSQLPadraoConsultaBasica();
        sql.append("where  HistoricoNotaBlackboard.salaAulaBlackboard = ? and HistoricoNotaBlackboard.situacaohistoriconotablackboardenum = 'NAO_LOCALIZADO' order by HistoricoNotaBlackboard.nomepessoablackboard ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());
        List<HistoricoNotaBlackboardVO> lista = new ArrayList<>();
        while (tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
        return lista;
    }
    
    
    @Override
    public HistoricoNotaBlackboardVO consultarPorHistoricoESituacaoHistoricoNotaBlackboardEnum(HistoricoVO historicoVO) {
    	StringBuilder sql = getSQLPadraoConsultaBasica();
    	sql.append(" WHERE HistoricoNotaBlackboard.historico = ? ");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{historicoVO.getCodigo()});
    	if (!tabelaResultado.next()) {
    		return new HistoricoNotaBlackboardVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }
    
    @Override
    public HistoricoNotaBlackboardVO consultarPorSalaAulaBlacboardNotaNaoLocalizada(Integer salaAulaBlackboardVO, String emailNaoLocalizado) {
    	StringBuilder sql = getSQLPadraoConsultaBasica();
    	sql.append(" WHERE HistoricoNotaBlackboard.salaAulaBlackboard = ? AND HistoricoNotaBlackboard.emailPessoaBlackboard = ?");
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{salaAulaBlackboardVO, emailNaoLocalizado});
    	if (!tabelaResultado.next()) {
    		return new HistoricoNotaBlackboardVO();
    	}
    	return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    @Override
    public HistoricoNotaBlackboardVO consultarPorCodigo(Integer codigo) {
    	StringBuilder sql = getSQLPadraoConsultaBasica();
        sql.append("WHERE HistoricoNotaBlackboard.codigo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        if (!tabelaResultado.next()) {
            return new HistoricoNotaBlackboardVO();
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    @Override
    public HistoricoNotaBlackboardVO consultarPorChavePrimaria(Long id) throws Exception {
    	StringBuilder sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE HistoricoNotaBlackboard.codigo = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
        if (!tabelaResultado.next()) {
            throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
    }

    @Override
    public void validarDados(HistoricoNotaBlackboardVO obj) throws ConsistirException {
    }
    
    public List<HistoricoNotaBlackboardVO> montarDadosConsultaOtimizado(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List<HistoricoNotaBlackboardVO> vetResultado = new ArrayList<HistoricoNotaBlackboardVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosOtimizado(tabelaResultado, nivelMontarDados, usuarioVO));
        }
        return vetResultado;
    }
    
	private HistoricoNotaBlackboardVO montarDadosOtimizado(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws SQLException {
		HistoricoNotaBlackboardVO obj = new HistoricoNotaBlackboardVO();
		obj.setNovoObj(false);
		obj.setCodigo(dadosSQL.getInt("historiconotablackboard.codigo"));
		obj.setNota(dadosSQL.getDouble("historiconotablackboard.nota"));
		obj.setNomePessoaBlackboard(dadosSQL.getString("historiconotablackboard.nomePessoaBlackboard"));
		obj.setEmailPessoaBlackboard(dadosSQL.getString("historiconotablackboard.emailPessoaBlackboard"));
		obj.setDataDeferimentoIndeferimento(dadosSQL.getDate("historiconotablackboard.datadeferimentoindeferimento"));
		obj.setDataDeferimentoIndeferimentoFiltro(dadosSQL.getDate("historiconotablackboard.datadeferimentoindeferimento"));
		if(Uteis.isColunaExistente(dadosSQL, "historiconotablackboard.motivo")) {
			obj.setMotivo(dadosSQL.getString("historiconotablackboard.motivo"));
		}
		obj.getUsuarioResponsavel().setNome(dadosSQL.getString("usuarioresponsavel.nome"));
		obj.getSalaAulaBlackboardVO().setCodigo(dadosSQL.getInt("salaaulablackboard.codigo"));
		obj.getSalaAulaBlackboardVO().setId(dadosSQL.getString("salaaulablackboard.id"));
		obj.getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(dadosSQL.getString("salaaulablackboard.idsalaaulablackboard"));
		obj.getSalaAulaBlackboardVO().setLinkSalaAulaBlackboard(dadosSQL.getString("salaaulablackboard.linkSalaAulaBlackboard"));
		obj.getSalaAulaBlackboardVO().setTermId(dadosSQL.getString("salaaulablackboard.termId"));
		obj.getSalaAulaBlackboardVO().setAno(dadosSQL.getString("salaaulablackboard.ano"));
		obj.getSalaAulaBlackboardVO().setSemestre(dadosSQL.getString("salaaulablackboard.semestre"));
		obj.getSalaAulaBlackboardVO().setBimestre(dadosSQL.getInt("salaaulablackboard.bimestre"));
		obj.getSalaAulaBlackboardVO().setNrSala(dadosSQL.getInt("salaaulablackboard.nrsala"));
		obj.getSalaAulaBlackboardVO().setNome(dadosSQL.getString("salaaulablackboard.nome"));
		obj.getSalaAulaBlackboardVO().setNomeGrupo(dadosSQL.getString("salaaulablackboard.nomeGrupo"));
		obj.getSalaAulaBlackboardVO().setGrupoExternalId(dadosSQL.getString("salaaulablackboard.grupoExternalId"));
		obj.getSalaAulaBlackboardVO().setGrupoSetId(dadosSQL.getString("salaaulablackboard.grupoSetId"));
		obj.getSalaAulaBlackboardVO().setIdGrupo(dadosSQL.getString("salaaulablackboard.idGrupo"));
		obj.getSalaAulaBlackboardVO().setNrGrupo(dadosSQL.getInt("salaaulablackboard.nrGrupo"));
		obj.getSalaAulaBlackboardVO().setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.valueOf(dadosSQL.getString("salaaulablackboard.tipoSalaAulaBlackboardEnum")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("curso.codigo"))) {
			obj.getSalaAulaBlackboardVO().getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
			obj.getSalaAulaBlackboardVO().getCursoVO().setNome(dadosSQL.getString("curso.nome"));

		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("turma.codigo"))) {
			obj.getSalaAulaBlackboardVO().getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
			obj.getSalaAulaBlackboardVO().getTurmaVO().setIdentificadorTurma(dadosSQL.getString("turma.identificadorturma"));
			obj.getSalaAulaBlackboardVO().getTurmaVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			obj.getSalaAulaBlackboardVO().getTurmaVO().getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino.razaosocial"));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("disciplina.codigo"))) {
			obj.getSalaAulaBlackboardVO().getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			obj.getSalaAulaBlackboardVO().getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
			obj.getSalaAulaBlackboardVO().getDisciplinaVO().setAbreviatura(dadosSQL.getString("disciplina.abreviatura"));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("gradecurricularestagio.codigo"))) {
			obj.getSalaAulaBlackboardVO().getGradeCurricularEstagioVO().setCodigo(dadosSQL.getInt("gradecurricularestagio.codigo"));
			obj.getSalaAulaBlackboardVO().getGradeCurricularEstagioVO().setNome(dadosSQL.getString("gradecurricularestagio.nome"));
		}
		return obj;
	}

    @Override
    public HistoricoNotaBlackboardVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) {
        HistoricoNotaBlackboardVO obj = new HistoricoNotaBlackboardVO();
        obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("historiconotablackboard.codigo"));
		obj.setNota(tabelaResultado.getDouble("historiconotablackboard.nota"));
		obj.setNomePessoaBlackboard(tabelaResultado.getString("historiconotablackboard.nomePessoaBlackboard"));
		obj.setEmailPessoaBlackboard(tabelaResultado.getString("historiconotablackboard.emailPessoaBlackboard"));
        obj.setNotaAnterior(tabelaResultado.getDouble("historiconotablackboard.notaAnterior"));
        obj.setSituacaoHistoricoNotaBlackboardEnum(SituacaoHistoricoNotaBlackboardEnum.valueOf(tabelaResultado.getString("historiconotablackboard.situacaoHistoricoNotaBlackboardEnum")));
        obj.setMotivo(tabelaResultado.getString("historiconotablackboard.motivo"));
        obj.setMotivoDeferimentoIndeferimento(tabelaResultado.getString("historiconotablackboard.motivoDeferimentoIndeferimento"));
        obj.getSalaAulaBlackboardPessoaVO().setCodigo(tabelaResultado.getInt("historiconotablackboard.salaAulaBlackboardPessoa"));
        obj.getSalaAulaBlackboardVO().setCodigo(tabelaResultado.getInt("historiconotablackboard.salaAulaBlackboard"));
        obj.getHistoricoVO().setCodigo(tabelaResultado.getInt("historiconotablackboard.historico"));
        obj.setDataDeferimentoIndeferimento(tabelaResultado.getDate("historiconotablackboard.dataDeferimentoIndeferimento"));
        if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("usuario.codigo"))) {
			obj.getUsuarioResponsavel().setCodigo(tabelaResultado.getInt("usuario.codigo"));
			obj.getUsuarioResponsavel().setNome(tabelaResultado.getString("usuario.nome"));
		}
        return obj;
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String idEntidade) {
        HistoricoNotaBlackboard.idEntidade = idEntidade;
    }
    
}
