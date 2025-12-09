package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PeriodoDisponibilizacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ListaExercicioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ListaExercicio extends ControleAcesso implements ListaExercicioInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    public void validarDados(ListaExercicioVO listaExercicioVO) throws ConsistirException, Exception {
        ConsistirException ce = null;
        if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)) {
        	if(listaExercicioVO.getQuestaoListaExercicioVOs().isEmpty()) {
        		ce = ce == null ? new ConsistirException() : ce;
            	ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_nrQuestaoInvalido"));            	
            }
        }

        if (listaExercicioVO.getDisciplina() == null || listaExercicioVO.getDisciplina().getCodigo() == null
                || listaExercicioVO.getDisciplina().getCodigo() == 0) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_disciplina"));
        }
        if (listaExercicioVO.getDescricao().trim().isEmpty()) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_descricao"));
        }

        if (listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio() != null && listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO)
                && listaExercicioVO.getLiberarDia() == null) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_liberarDia"));
        }
        if (listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio() != null && listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO)
                && listaExercicioVO.getEncerrarDia() == null) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_encerrarDia"));
        }
        if (listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio() != null && listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO)
                && listaExercicioVO.getLiberarDia() != null && listaExercicioVO.getEncerrarDia() != null
                && Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(listaExercicioVO.getLiberarDia()).
                        compareTo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(listaExercicioVO.getEncerrarDia())) > 1) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_liberarDiaMaiorEncerrarDia"));
        }

        if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)
                && (listaExercicioVO.getQuantidadeNivelQuestaoDificil()
                        + listaExercicioVO.getQuantidadeNivelQuestaoFacil()
                        + listaExercicioVO.getQuantidadeNivelQuestaoMedio()
                        + listaExercicioVO.getQuantidadeQualquerNivelQuestao()) == 0) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_informarNumeroQuestaoSorteio"));
        }
        if (listaExercicioVO.getDisciplina().getCodigo() > 0) {
            if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)
                    && listaExercicioVO.getQuantidadeNivelQuestaoFacil() > 0 && listaExercicioVO.getQuantidadeNivelQuestaoFacil() >
                    getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(),
                            SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.FACIL, listaExercicioVO.getConteudoVO().getCodigo(), listaExercicioVO.getPoliticaSelecaoQuestaoEnum(), false, null, null, null, false)) {
                ce = ce == null ? new ConsistirException() : ce;
                ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_numeroQuestaoFacilInvalido"));
            }
            if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)
                    && listaExercicioVO.getQuantidadeNivelQuestaoMedio() > 0 && listaExercicioVO.getQuantidadeNivelQuestaoMedio() >
                    getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(),
                            SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.MEDIO, listaExercicioVO.getConteudoVO().getCodigo(), listaExercicioVO.getPoliticaSelecaoQuestaoEnum(), false, null, null, null, false)) {
                ce = ce == null ? new ConsistirException() : ce;
                ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_numeroQuestaoMedioInvalido"));
            }

            if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)
                    && listaExercicioVO.getQuantidadeNivelQuestaoDificil() > 0 && listaExercicioVO.getQuantidadeNivelQuestaoDificil() >
                    getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("", new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(),
                            SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, NivelComplexidadeQuestaoEnum.DIFICIL, listaExercicioVO.getConteudoVO().getCodigo(), listaExercicioVO.getPoliticaSelecaoQuestaoEnum(), false, null, null, null, false)) {
                ce = ce == null ? new ConsistirException() : ce;
                ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_numeroQuestaoDificilInvalido"));
            }
            if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)
                    && listaExercicioVO.getQuantidadeQualquerNivelQuestao() > 0 && listaExercicioVO.getQuantidadeQualquerNivelQuestao() >
                    getFacadeFactory().getQuestaoFacade().consultarTotalResgistro("",  new TemaAssuntoVO(), listaExercicioVO.getDisciplina().getCodigo(),
                            SituacaoQuestaoEnum.ATIVA, false, false, true, false, null, null, listaExercicioVO.getConteudoVO().getCodigo(), listaExercicioVO.getPoliticaSelecaoQuestaoEnum(), false, null, null, null, false)) {
                ce = ce == null ? new ConsistirException() : ce;
                ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ListaExercicio_numeroQualquerNivelQuestaoInvalido"));
            }
        }

        if (listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)) {
            listaExercicioVO.getQuestaoListaExercicioVOs().clear();
        }
        if (ce != null) {
            throw ce;
        }

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        validarDados(listaExercicioVO);
        if (listaExercicioVO.isNovoObj()) {
            incluir(listaExercicioVO, controlarAcesso, idEntidade, usuarioVO);
        } else {
            alterar(listaExercicioVO, controlarAcesso, idEntidade, usuarioVO);
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void incluir(final ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        incluir(idEntidade, controlarAcesso, usuarioVO);
        listaExercicioVO.setDataCriacao(new Date());
        listaExercicioVO.setResponsavelCriacao(usuarioVO);
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO ListaExercicio ");
            sql.append(" (situacaoListaExercicio, tipoGeracaoListaExercicio, periodoDisponibilizacaoListaExercicio, descricao, ");
            sql.append(" liberarDia, encerrarDia, quantidadeNivelQuestaoMedio, quantidadeNivelQuestaoFacil, quantidadeNivelQuestaoDificil, quantidadequalquernivelquestao, ");
            sql.append(" dataCriacao, responsavelCriacao, turma, disciplina, conteudo, politicaselecaoquestao, regradistribuicaoquestao, randomizarApenasQuestoesCadastradasPeloProfessor ");
            sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?) ");
            sql.append(" returning codigo ");
            listaExercicioVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setString(x++, listaExercicioVO.getSituacaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getTipoGeracaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getDescricao());
                    if (listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO)) {
                        ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getLiberarDia()));
                        ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getEncerrarDia()));
                    } else {
                        ps.setNull(x++, 0);
                        ps.setNull(x++, 0);
                    }
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoMedio());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoFacil());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoDificil());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeQualquerNivelQuestao());
                    ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getDataCriacao()));
                    ps.setInt(x++, listaExercicioVO.getResponsavelCriacao().getCodigo());
                    if (listaExercicioVO.getTurma().getCodigo() > 0) {
                        ps.setInt(x++, listaExercicioVO.getTurma().getCodigo());
                    } else {
                        ps.setNull(x++, 0);
                    }
                    ps.setInt(x++, listaExercicioVO.getDisciplina().getCodigo());
                    if(listaExercicioVO.getConteudoVO().getCodigo() != 0) {
                    	ps.setInt(x++, listaExercicioVO.getConteudoVO().getCodigo());
                    } else {
                    	ps.setNull(x++, 0);
                    }
                    if(listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)) {
                    	ps.setString(x++, listaExercicioVO.getPoliticaSelecaoQuestaoEnum().getName());
                    	ps.setString(x++, listaExercicioVO.getRegraDistribuicaoQuestaoEnum().getName());                    	
                    } else {
                    	ps.setNull(x++, 0);
                    	ps.setNull(x++, 0);
                    }
                    ps.setBoolean(x++, listaExercicioVO.getRandomizarApenasQuestoesCadastradasPeloProfessor());
                    return ps;
                }
            }, new ResultSetExtractor<Integer>() {

                @Override
                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
                }

            }));
            getFacadeFactory().getQuestaoListaExercicioFacade().incluirQuestaoListaExercicio(listaExercicioVO);
            listaExercicioVO.setNovoObj(false);
        } catch (Exception e) {
            listaExercicioVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void alterar(final ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        alterar(idEntidade, controlarAcesso, usuarioVO);
        listaExercicioVO.setDataAlteracao(new Date());
        listaExercicioVO.setResponsavelAlteracao(usuarioVO);
        try {
            final StringBuilder sql = new StringBuilder("UPDATE ListaExercicio set ");
            sql.append(" situacaoListaExercicio = ?, tipoGeracaoListaExercicio = ?, periodoDisponibilizacaoListaExercicio = ?, descricao = ?, ");
            sql.append(" liberarDia=?, encerrarDia = ?, quantidadeNivelQuestaoMedio = ?, quantidadeNivelQuestaoFacil = ?, quantidadeNivelQuestaoDificil = ?, quantidadequalquernivelquestao = ?,");
            sql.append(" dataAlteracao = ?, responsavelAlteracao = ?, turma = ?, disciplina = ?, conteudo = ?,  politicaselecaoquestao  = ?, regradistribuicaoquestao  = ?, ");
            sql.append(" randomizarApenasQuestoesCadastradasPeloProfessor = ?");
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setString(x++, listaExercicioVO.getSituacaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getTipoGeracaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().name());
                    ps.setString(x++, listaExercicioVO.getDescricao());
                    if (listaExercicioVO.getPeriodoDisponibilizacaoListaExercicio().equals(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO)) {
                        ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getLiberarDia()));
                        ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getEncerrarDia()));
                    } else {
                        ps.setNull(x++, 0);
                        ps.setNull(x++, 0);
                    }
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoMedio());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoFacil());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeNivelQuestaoDificil());
                    ps.setInt(x++, listaExercicioVO.getQuantidadeQualquerNivelQuestao());
                    ps.setDate(x++, Uteis.getDataJDBC(listaExercicioVO.getDataAlteracao()));
                    ps.setInt(x++, listaExercicioVO.getResponsavelAlteracao().getCodigo());
                    if (listaExercicioVO.getTurma().getCodigo() > 0) {
                        ps.setInt(x++, listaExercicioVO.getTurma().getCodigo());
                    } else {
                        ps.setNull(x++, 0);
                    }
                    ps.setInt(x++, listaExercicioVO.getDisciplina().getCodigo());
                    if(listaExercicioVO.getConteudoVO().getCodigo() != 0) {
                    	ps.setInt(x++, listaExercicioVO.getConteudoVO().getCodigo());
                    } else {
                    	ps.setNull(x++, 0);
                    }
                    if(listaExercicioVO.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.RANDOMICO)) {
                    	ps.setString(x++, listaExercicioVO.getPoliticaSelecaoQuestaoEnum().getName());
                    	ps.setString(x++, listaExercicioVO.getRegraDistribuicaoQuestaoEnum().getName());                    	
                    } else {
                    	ps.setNull(x++, 0);
                    	ps.setNull(x++, 0);
                    }
                    ps.setBoolean(x++, listaExercicioVO.getRandomizarApenasQuestoesCadastradasPeloProfessor());
                    ps.setInt(x++, listaExercicioVO.getCodigo());
                    return ps;
                }
            }) == 0) {
                incluir(listaExercicioVO, controlarAcesso, idEntidade, usuarioVO);
                return;
            }
            getFacadeFactory().getQuestaoListaExercicioFacade().alterarQuestaoListaExercicio(listaExercicioVO);
            listaExercicioVO.setNovoObj(false);
        } catch (Exception e) {
            listaExercicioVO.setNovoObj(false);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluir(final ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        excluir(idEntidade, controlarAcesso, usuarioVO);
        final StringBuilder sql = new StringBuilder("DELETE FROM ListaExercicio ");
        sql.append(" where codigo = ? ");
        if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement ps = arg0.prepareStatement(sql.toString());
                ps.setInt(1, listaExercicioVO.getCodigo());
                return ps;
            }
        }) == 0) {
            return;
        }

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void ativar(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        SituacaoListaExercicioEnum situacaoListaExercicioEnum = listaExercicioVO.getSituacaoListaExercicio();
        try {
            listaExercicioVO.setDataAlteracao(new Date());
            listaExercicioVO.setResponsavelAlteracao(usuarioVO);
            listaExercicioVO.setSituacaoListaExercicio(SituacaoListaExercicioEnum.ATIVA);
            persistir(listaExercicioVO, controlarAcesso, idEntidade, usuarioVO);
        } catch (Exception e) {
            listaExercicioVO.setSituacaoListaExercicio(situacaoListaExercicioEnum);
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void inativar(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {
        SituacaoListaExercicioEnum situacaoListaExercicioEnum = listaExercicioVO.getSituacaoListaExercicio();
        try {
            listaExercicioVO.setDataAlteracao(new Date());
            listaExercicioVO.setResponsavelAlteracao(usuarioVO);
            listaExercicioVO.setSituacaoListaExercicio(SituacaoListaExercicioEnum.INATIVA);
            persistir(listaExercicioVO, controlarAcesso, idEntidade, usuarioVO);
        } catch (Exception e) {
            listaExercicioVO.setSituacaoListaExercicio(situacaoListaExercicioEnum);
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ListaExercicioVO clonarListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception {
        return listaExercicioVO.clone();
    }

    public String getSelectDadosBasicos() {
        StringBuilder sb = new StringBuilder("SELECT ListaExercicio.*, ");
        sb.append(" disciplina.nome as \"disciplina.nome\", ");
        sb.append(" turma.identificadorTurma as \"turma.identificadorTurma\", ");
        sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
        sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\" ");
        sb.append(" FROM ListaExercicio ");
        sb.append(" inner join disciplina on  disciplina.codigo = ListaExercicio.disciplina ");
        sb.append(" left join usuario as responsavelCriacao on  responsavelCriacao.codigo = ListaExercicio.responsavelCriacao ");
        sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = ListaExercicio.responsavelAlteracao ");
        sb.append(" left join turma on  turma.codigo = ListaExercicio.turma ");
        return sb.toString();
    }

    public String getSelectDadosCompleto() {
        StringBuilder sb = new StringBuilder("SELECT ListaExercicio.*, ");
        sb.append(" disciplina.nome as \"disciplina.nome\", ");
        sb.append(" turma.identificadorTurma as \"turma.identificadorTurma\", ");
        sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
        sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\",  ");
        sb.append(" qle.codigo as \"qle.codigo\",  ");
        sb.append(" qle.ordemApresentacao as \"qle.ordemApresentacao\",  ");
        sb.append(" questao.codigo as \"questao.codigo\",  ");
        sb.append(" questao.enunciado as \"questao.enunciado\",   ");
        sb.append(" questao.nivelComplexidadeQuestao as \"questao.nivelComplexidadeQuestao\",   ");
        sb.append(" questao.tipoQuestaoEnum as \"questao.tipoQuestaoEnum\"  ");
        sb.append(" FROM ListaExercicio ");
        sb.append(" inner join disciplina on  disciplina.codigo = ListaExercicio.disciplina ");
        sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = ListaExercicio.responsavelCriacao ");
        sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = ListaExercicio.responsavelAlteracao ");
        sb.append(" left join turma on  turma.codigo = ListaExercicio.turma ");
        sb.append(" left join QuestaoListaExercicio as qle on  qle.listaExercicio = ListaExercicio.codigo ");
        sb.append(" left join Questao as questao on  questao.codigo = qle.questao ");
        return sb.toString();
    }

    @Override
    public List<ListaExercicioVO> consultar(Integer disciplina, Integer turma, String descricao,
            SituacaoListaExercicioEnum situacaoListaExercicio, TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio,
            PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicioEnum,
            Boolean controlarAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception {
        consultar(idEntidade, controlarAcesso, usuario);
        List<Object> param = new ArrayList<>();
        StringBuilder sql = new StringBuilder(getSelectDadosBasicos());
        sql.append(" WHERE 0=0 ");
        if (descricao != null && !descricao.trim().isEmpty()) {
            sql.append(" and upper(sem_acentos(ListaExercicio.descricao)) like(upper(sem_acentos(?))) ");
            param.add(PERCENT + descricao.trim() + PERCENT);
        }
        if (disciplina != null && disciplina > 0) {
            sql.append(" and disciplina.codigo = ").append(disciplina);
        }
        if (turma != null && turma > 0) {
            sql.append(" and turma.codigo = ").append(turma);
        }
        if (situacaoListaExercicio != null) {
            sql.append(" and situacaoListaExercicio = '").append(situacaoListaExercicio.name()).append("'");
        }
        if (tipoGeracaoListaExercicio != null) {
            sql.append(" and tipoGeracaoListaExercicio = '").append(tipoGeracaoListaExercicio.name()).append("'");
        }
        if (periodoDisponibilizacaoListaExercicioEnum != null) {
            sql.append(" and periodoDisponibilizacaoListaExercicio = '").append(periodoDisponibilizacaoListaExercicioEnum.name()).append("'");
        }
        if (limite != null && limite > 0) {
            sql.append(" limit ").append(limite).append(" offset ").append(pagina);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray()));
    }

    private List<ListaExercicioVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
        List<ListaExercicioVO> listaExercicioVOs = new ArrayList<ListaExercicioVO>(0);
        while (rs.next()) {
            listaExercicioVOs.add(montarDadosBasicos(rs));
        }
        return listaExercicioVOs;
    }

    private List<ListaExercicioVO> montarDadosConsultaCompleta(SqlRowSet rs) throws Exception {
        List<ListaExercicioVO> questaoVOs = new ArrayList<ListaExercicioVO>(0);
        Map<Integer, ListaExercicioVO> qMap = new HashMap<Integer, ListaExercicioVO>(0);
        ListaExercicioVO obj = null;
        while (rs.next()) {
            if (!qMap.containsKey(rs.getInt("codigo"))) {
                obj = montarDadosBasicos(rs);
                qMap.put(obj.getCodigo(), obj);
            } else {
                obj = qMap.get(rs.getInt("codigo"));
            }
            if (rs.getInt("qle.codigo") > 0) {
                QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
                questaoListaExercicioVO.setNovoObj(false);
                questaoListaExercicioVO.setCodigo(rs.getInt("qle.codigo"));
                questaoListaExercicioVO.setOrdemApresentacao(rs.getInt("qle.ordemApresentacao"));
                questaoListaExercicioVO.setListaExercicio(obj);
                questaoListaExercicioVO.getQuestao().setNovoObj(false);
                questaoListaExercicioVO.getQuestao().setCodigo(rs.getInt("questao.codigo"));
                questaoListaExercicioVO.getQuestao().setEnunciado(rs.getString("questao.enunciado"));
                questaoListaExercicioVO.getQuestao().setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.valueOf(rs.getString("questao.nivelComplexidadeQuestao")));
                questaoListaExercicioVO.getQuestao().setTipoQuestaoEnum(TipoQuestaoEnum.valueOf(rs.getString("questao.tipoQuestaoEnum")));
                obj.getQuestaoListaExercicioVOs().add(questaoListaExercicioVO);
            }
        }
        questaoVOs.addAll(qMap.values());
        return questaoVOs;
    }

    private ListaExercicioVO montarDadosBasicos(SqlRowSet rs) throws Exception {
        ListaExercicioVO obj = new ListaExercicioVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setDataCriacao(rs.getDate("dataCriacao"));
        obj.setDataAlteracao(rs.getDate("dataAlteracao"));
        obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
        obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
        obj.getTurma().setCodigo(rs.getInt("turma"));
        obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
        if (rs.getInt("responsavelAlteracao") > 0) {
            obj.setResponsavelAlteracao(new UsuarioVO());
            obj.getResponsavelAlteracao().setCodigo(rs.getInt("responsavelAlteracao"));
            obj.getResponsavelAlteracao().setNome(rs.getString("responsavelAlteracao.nome"));
        }
        obj.setResponsavelCriacao(new UsuarioVO());
        obj.getResponsavelCriacao().setCodigo(rs.getInt("responsavelCriacao"));
        obj.getResponsavelCriacao().setNome(rs.getString("responsavelCriacao.nome"));
        obj.setDescricao(rs.getString("descricao"));
        obj.setSituacaoListaExercicio(SituacaoListaExercicioEnum.valueOf(rs.getString("situacaoListaExercicio")));
        obj.setPeriodoDisponibilizacaoListaExercicio(PeriodoDisponibilizacaoListaExercicioEnum.valueOf(rs.getString("periodoDisponibilizacaoListaExercicio")));
        obj.setTipoGeracaoListaExercicio(TipoGeracaoListaExercicioEnum.valueOf(rs.getString("tipoGeracaoListaExercicio")));
        obj.setEncerrarDia(rs.getDate("encerrarDia"));
        obj.setLiberarDia(rs.getDate("liberarDia"));
        obj.setQuantidadeNivelQuestaoDificil(rs.getInt("quantidadeNivelQuestaoDificil"));
        obj.setQuantidadeNivelQuestaoFacil(rs.getInt("quantidadeNivelQuestaoFacil"));
        obj.setQuantidadeNivelQuestaoMedio(rs.getInt("quantidadeNivelQuestaoMedio"));
        obj.getConteudoVO().setCodigo(rs.getInt("conteudo"));
        obj.setQuantidadeQualquerNivelQuestao(rs.getInt("quantidadequalquernivelquestao"));
        obj.setRandomizarApenasQuestoesCadastradasPeloProfessor(rs.getBoolean("randomizarApenasQuestoesCadastradasPeloProfessor"));
        if(Uteis.isAtributoPreenchido(rs.getString("politicaselecaoquestao"))) {
        	obj.setPoliticaSelecaoQuestaoEnum(PoliticaSelecaoQuestaoEnum.valueOf(rs.getString("politicaselecaoquestao")));
        }
        if(Uteis.isAtributoPreenchido(rs.getString("regradistribuicaoquestao"))) {
        	obj.setRegraDistribuicaoQuestaoEnum(RegraDistribuicaoQuestaoEnum.valueOf(rs.getString("regradistribuicaoquestao")));
        }
        return obj;
    }

    @Override
    public Integer consultarTotalRegistro(Integer disciplina, Integer turma, String descricao, SituacaoListaExercicioEnum situacaoListaExercicio, TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio, PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicioEnum) throws Exception {
    	List<Object> param = new ArrayList<>();
    	StringBuilder sql = new StringBuilder("SELECT count(codigo) as qtde ");
        sql.append(" FROM ListaExercicio ");
        sql.append(" WHERE 0=0 ");
        if (descricao != null && !descricao.trim().isEmpty()) {
            sql.append(" and upper(sem_acentos(ListaExercicio.descricao)) like(upper(sem_acentos(?))) ");
            param.add(PERCENT + descricao.trim() + PERCENT);
        }
        if (disciplina != null && disciplina > 0) {
            sql.append(" and disciplina = ").append(disciplina);
        }
        if (turma != null && turma > 0) {
            sql.append(" and turma = ").append(turma);
        }
        if (situacaoListaExercicio != null) {
            sql.append(" and situacaoListaExercicio = '").append(situacaoListaExercicio.name()).append("'");
        }
        if (tipoGeracaoListaExercicio != null) {
            sql.append(" and tipoGeracaoListaExercicio = '").append(tipoGeracaoListaExercicio.name()).append("'");
        }
        if (periodoDisponibilizacaoListaExercicioEnum != null) {
            sql.append(" and periodoDisponibilizacaoListaExercicio = '").append(periodoDisponibilizacaoListaExercicioEnum.name()).append("'");
        }
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;
    }

    @Override
    public List<ListaExercicioVO> consultarListaExercicioDisponivelAluno(Integer matriculaPeriodoTurmaDisciplina, Integer limite, Integer pagina) throws Exception {
        StringBuilder sql = new StringBuilder(getSelectDadosBasicos());
        sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina == null ? 0 : matriculaPeriodoTurmaDisciplina);
        sql.append(" where (( ListaExercicio.turma is null and (disciplina.codigo = matriculaperiodoturmadisciplina.disciplina or matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = disciplina.codigo ) ) ) ");
        sql.append(" or (ListaExercicio.turma is not null AND ((matriculaperiodoturmadisciplina.turmapratica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turma.codigo = matriculaperiodoturmadisciplina.turmapratica )");
 		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turma.codigo =  matriculaperiodoturmadisciplina.turmateorica )");
   		sql.append(" or (turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
   		sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ) ) ");
   		sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ) ) ");
   		sql.append(" or (turma.subturma = false and turma.turmaagrupada = false and turma.codigo = matriculaperiodoturmadisciplina.turma)) and (matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
   		sql.append(" or (turma.turmaagrupada and matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = disciplina.codigo))))) ");
        sql.append(" and situacaoListaExercicio = '").append(SituacaoListaExercicioEnum.ATIVA.name()).append("'");
        sql.append(" and ( (periodoDisponibilizacaoListaExercicio = '").append(PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO.name()).append("') ");
        sql.append(" or ( periodoDisponibilizacaoListaExercicio = '").append(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO.name()).append("'  ");
        sql.append(" and liberarDia <= current_date and encerrarDia >= current_date )");
        sql.append("  )");
        sql.append(" order by ListaExercicio.codigo ");
        if (limite != null && limite > 0) {
            sql.append(" limit ").append(limite).append(" offset ").append(pagina);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
    }

    @Override
    public Integer consultarTotalRegistroListaExercicioDisponivelAluno(Integer matriculaPeriodoTurmaDisciplina) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT count(ListaExercicio.codigo) as qtde from ListaExercicio left join turma on  turma.codigo = ListaExercicio.turma ");
        sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplina == null ? 0 : matriculaPeriodoTurmaDisciplina);
        sql.append(" where (( ListaExercicio.turma is null and (ListaExercicio.disciplina = matriculaperiodoturmadisciplina.disciplina or matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ListaExercicio.disciplina)))");
        sql.append(" or (ListaExercicio.turma is not null AND ((matriculaperiodoturmadisciplina.turmapratica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.PRATICA.getName()).append("' and turma.codigo = matriculaperiodoturmadisciplina.turmapratica )");
    	sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.subturma and turma.tiposubturma = '").append(TipoSubTurmaEnum.TEORICA.getName()).append("' and turma.codigo =  matriculaperiodoturmadisciplina.turmateorica )");
    	sql.append(" or (turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turma ))");
    	sql.append(" or (matriculaperiodoturmadisciplina.turmapratica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmapratica ))");
    	sql.append(" or (matriculaperiodoturmadisciplina.turmateorica is not null and turma.turmaagrupada and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and  turmaagrupada.turma = matriculaperiodoturmadisciplina.turmateorica ))");
    	sql.append(" or (turma.subturma = false and turma.turmaagrupada = false and turma.codigo = matriculaperiodoturmadisciplina.turma)) and (ListaExercicio.disciplina = matriculaperiodoturmadisciplina.disciplina ");
    	sql.append(" or (turma.turmaagrupada and matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ListaExercicio.disciplina)))))");
        sql.append(" and situacaoListaExercicio = '").append(SituacaoListaExercicioEnum.ATIVA.name()).append("'");
        sql.append(" and ( (periodoDisponibilizacaoListaExercicio = '").append(PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO.name()).append("') ");
        sql.append(" or ( periodoDisponibilizacaoListaExercicio = '").append(PeriodoDisponibilizacaoListaExercicioEnum.PERIODO.name()).append("'  ");
        sql.append(" and liberarDia <= current_date and encerrarDia >= current_date )");
        sql.append("  )");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;
    }

    @Override
    public ListaExercicioVO consultarPorChavePrimaria(Integer codigo) throws Exception {
        StringBuilder sql = new StringBuilder(getSelectDadosCompleto());
        sql.append(" WHERE ListaExercicio.codigo = ").append(codigo);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<ListaExercicioVO> listaExercicioVOs = montarDadosConsultaCompleta(rs);
        if (listaExercicioVOs.isEmpty()) {
            throw new Exception("Dados não encontrados(Lista Exercício).");
        }
        return listaExercicioVOs.get(0);
    }

    @Override
    public void adicionarQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicioVO) throws Exception {
        for (QuestaoListaExercicioVO questaoListaExercicioVO2 : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            if (questaoListaExercicioVO2.getQuestao().getCodigo().intValue() == questaoListaExercicioVO.getQuestao().getCodigo().intValue()) {
                throw new Exception(UteisJSF.internacionalizar("msg_ListaExercicio_questaoJaAdicionada"));
            }
        }
        questaoListaExercicioVO.setOrdemApresentacao(listaExercicioVO.getQuestaoListaExercicioVOs().size() + 1);
        listaExercicioVO.getQuestaoListaExercicioVOs().add(questaoListaExercicioVO);
    }

    @Override
    public void removerQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicioVO) {
        int x = 0;
        for (QuestaoListaExercicioVO questaoListaExercicioVO2 : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            if (questaoListaExercicioVO2.getQuestao().getCodigo().intValue() == questaoListaExercicioVO.getQuestao().getCodigo().intValue()) {
                listaExercicioVO.getQuestaoListaExercicioVOs().remove(x);
                break;
            }
            x++;
        }
        x = 1;
        for (QuestaoListaExercicioVO questaoListaExercicioVO2 : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            questaoListaExercicioVO2.setOrdemApresentacao(x++);
        }
    }

    @Override
    public void alterarOrdemApresentacaoQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicio1, QuestaoListaExercicioVO questaoListaExercicio2) {
        int ordem1 = questaoListaExercicio1.getOrdemApresentacao();
        questaoListaExercicio1.setOrdemApresentacao(questaoListaExercicio2.getOrdemApresentacao());
        questaoListaExercicio2.setOrdemApresentacao(ordem1);
        Ordenacao.ordenarLista(listaExercicioVO.getQuestaoListaExercicioVOs(), "ordemApresentacao");
    }

    @Override
    public ListaExercicioVO novo() {
        ListaExercicioVO listaExercicioVO = new ListaExercicioVO();
        listaExercicioVO.setSituacaoListaExercicio(SituacaoListaExercicioEnum.EM_ELABORACAO);
        listaExercicioVO.setPeriodoDisponibilizacaoListaExercicio(PeriodoDisponibilizacaoListaExercicioEnum.INDETERMINADO);
        listaExercicioVO.setTipoGeracaoListaExercicio(TipoGeracaoListaExercicioEnum.RANDOMICO);
        return listaExercicioVO;
    }

    
    
    @Override
    public void realizarVerificacaoQuestaoUnicaEscolha(ListaExercicioVO listaExercicioVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) {
        if (opcaoRespostaQuestaoVO.getMarcada()) {
            for (QuestaoListaExercicioVO questaoListaExercicioVO : listaExercicioVO.getQuestaoListaExercicioVOs()) {
                if (questaoListaExercicioVO.getQuestao().getCodigo().intValue() == opcaoRespostaQuestaoVO.getQuestaoVO().getCodigo().intValue()) {
                     if(questaoListaExercicioVO.getQuestao().getTipoQuestaoEnum().equals(TipoQuestaoEnum.UNICA_ESCOLHA)){
                         for(OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO2:questaoListaExercicioVO.getQuestao().getOpcaoRespostaQuestaoVOs()){
                             if(opcaoRespostaQuestaoVO2.getCodigo().intValue() != opcaoRespostaQuestaoVO.getCodigo().intValue()){
                                 opcaoRespostaQuestaoVO2.setMarcada(false);
                             }
                         }
                     }
                     return;
                }
            }
        }
    }
    
    @Override
    public void realizarGeracaoGabarito(ListaExercicioVO listaExercicioVO) throws Exception{
        List<Integer> questoesNaoRespondida = new ArrayList<Integer>(0);
        listaExercicioVO.setNumeroAcertos(0);
        listaExercicioVO.setNumeroErros(0);
        listaExercicioVO.setDescricaoBloqueio("");
        for (QuestaoListaExercicioVO questaoListaExercicioVO : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            getFacadeFactory().getQuestaoFacade().realizarCorrecaoQuestao(questaoListaExercicioVO.getQuestao());
            if(questaoListaExercicioVO.getQuestao().getQuestaoNaoRespondida()){
                questoesNaoRespondida.add(questaoListaExercicioVO.getOrdemApresentacao());
            }
            if(questaoListaExercicioVO.getQuestao().getAcertouQuestao()){
                listaExercicioVO.setNumeroAcertos(listaExercicioVO.getNumeroAcertos()+1);
            }
            if(questaoListaExercicioVO.getQuestao().getErrouQuestao()){
                listaExercicioVO.setNumeroErros(listaExercicioVO.getNumeroErros()+1);
            }            
        }    
        if(!questoesNaoRespondida.isEmpty()){             
        	listaExercicioVO.setDescricaoBloqueio("scrollTOREA('div.exercicio"+(questoesNaoRespondida.get(0)-1)+"')");
            if(questoesNaoRespondida.size() == 1){
                throw new Exception("O Exercício "+questoesNaoRespondida.get(0)+" não está respondido.");
            }
            StringBuilder msg = new StringBuilder("Os Exercícios ");
            int x = 1;
            for(Integer questao:questoesNaoRespondida){
                if(x > 1 && x < questoesNaoRespondida.size()){
                    msg.append(", ");
                }
                if(x == questoesNaoRespondida.size()){
                    msg.append(" e ");
                }
                msg.append(questao);                
                x++;
            }
            msg.append(" devem ser respondidos.");
            throw new Exception(msg.toString());
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoListasExerciciosPorConteudo(SituacaoListaExercicioEnum situacao, ConteudoVO conteudoVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append("UPDATE ListaExercicio set situacaoListaExercicio = '").append(situacao.name()).append("' ");
		sb.append(" where codigo in ( ");
		sb.append(" SELECT distinct listaExercicio from ConteudoUnidadePaginaRecursoEducacional ");
		sb.append(" inner join ConteudoUnidadePagina on ConteudoUnidadePagina.codigo = ConteudoUnidadePaginaRecursoEducacional.conteudoUnidadePagina ");
		sb.append(" inner join UnidadeConteudo on UnidadeConteudo.codigo = ConteudoUnidadePagina.unidadeConteudo ");
		sb.append(" where conteudo =  ").append(conteudoVO.getCodigo());
		sb.append(" and ConteudoUnidadePaginaRecursoEducacional.tiporecursoeducacional = '").append(TipoRecursoEducacionalEnum.EXERCICIO.name()).append("'");
		sb.append(" ) ");
		getConexao().getJdbcTemplate().update(sb.toString());

	}

}
