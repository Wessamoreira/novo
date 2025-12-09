package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorCursoVO;
import negocio.comuns.administrativo.CampanhaColaboradorVO;
import negocio.comuns.administrativo.CampanhaPublicoAlvoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.MetaItemVO;
import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.crm.enumerador.TipoCampanhaEnum;
import negocio.comuns.crm.enumerador.TipoMetaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaColaboradorInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CampanhaColaborador extends ControleAcesso implements CampanhaColaboradorInterfaceFacade {

    private static String idEntidade;

    public static String getIdEntidade() {
        return idEntidade;
    }

    public void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    public static void validarDados(CampanhaColaboradorVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getFuncionarioCargoVO().getCodigo() == null || obj.getFuncionarioCargoVO().getCodigo() == 0) {
            throw new ConsistirException("O campo PARTICIPANTE deve ser informado.");
        }
        if (obj.getHoraInicioGerarAgenda().equals("")) {
            throw new ConsistirException("O campo HORA INÍCIO GERAR AGENDA deve ser informado.");
        }
        if (obj.getHoraFinalGerarAgenda().equals("")) {
            throw new ConsistirException("O campo HORA FINAL GERAR AGENDA deve ser informado.");
        }
        if ((!obj.getHoraInicioIntervalo().equals("")) && 
            (obj.getHoraFimIntervalo().equals(""))) {
            throw new ConsistirException("O campo HORA FINAL INTERVALO deve ser informado (Hora inícial do intervalo foi informada).");
        }
    }

    public CampanhaColaborador() {
        super();
        setIdEntidade("CampanhaColaborador");
    }

    public CampanhaColaboradorVO novo() throws Exception {
        CampanhaColaborador.incluir(getIdEntidade());
        CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CampanhaColaboradorVO obj) throws Exception {
        try {
            validarDados(obj);
            final String sql = "INSERT INTO campanhacolaborador (campanha, funcionariocargo, qtdcontato, "
                    + "qtdsucesso, qtdcaptacao, horaInicioGerarAgenda, horaFinalGerarAgenda, horaInicioIntervalo, horaFimIntervalo) "
                    + "VALUES (?,?,?,?,?,?,?,?,?) returning codigo";
        
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getCampanha().getCodigo());
                    if (obj.getFuncionarioCargoVO().getCodigo() != 0) {
                        sqlInserir.setInt(2, obj.getFuncionarioCargoVO().getCodigo());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setInt(3, obj.getQtdContato());
                    sqlInserir.setInt(4, obj.getQtdSucesso());
                    sqlInserir.setInt(5, obj.getQtdCaptacao());
                    sqlInserir.setString(6, obj.getHoraInicioGerarAgenda());
                    sqlInserir.setString(7, obj.getHoraFinalGerarAgenda());
                    sqlInserir.setString(8, obj.getHoraInicioIntervalo());
                    sqlInserir.setString(9, obj.getHoraFimIntervalo());
                    
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getCampanhaColaboradorCursoFacade().incluirCampanhaColaboradorCursos(obj.getCodigo(), obj.getListaCampanhaColaboradorCursoVOs(), true);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CampanhaColaboradorVO obj) throws Exception {
        try {
            validarDados(obj);
            final String sql = "UPDATE campanhacolaborador set campanha=?, funcionariocargo=?, qtdcontato=?, "
                    + "qtdsucesso=?, qtdcaptacao=?, horaInicioGerarAgenda=?, horaFinalGerarAgenda=?, "
                    + "horaInicioIntervalo=?, horaFimIntervalo=? where codigo=?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getCampanha().getCodigo());
                    if (obj.getFuncionarioCargoVO().getCodigo() != 0) {
                        sqlInserir.setInt(2, obj.getFuncionarioCargoVO().getCodigo());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setInt(3, obj.getQtdContato());
                    sqlInserir.setInt(4, obj.getQtdSucesso());
                    sqlInserir.setInt(5, obj.getQtdCaptacao());
                    
                    sqlInserir.setString(6, obj.getHoraInicioGerarAgenda());
                    sqlInserir.setString(7, obj.getHoraFinalGerarAgenda());
                    sqlInserir.setString(8, obj.getHoraInicioIntervalo());
                    sqlInserir.setString(9, obj.getHoraFimIntervalo());
                    
                    sqlInserir.setInt(10, obj.getCodigo());
                    return sqlInserir;
                }
            });
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getCampanhaColaboradorCursoFacade().alterarCampanhaColaboradorCurso(obj.getCodigo(), obj.getListaCampanhaColaboradorCursoVOs(), true);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCampanhaColaborador(Integer campanha, List objetos) throws Exception {
        String str = "DELETE FROM campanhacolaborador WHERE campanha = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CampanhaColaboradorVO objeto = (CampanhaColaboradorVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str, new Object[]{campanha});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaColaboradorVO objeto = (CampanhaColaboradorVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getCampanha().setCodigo(campanha);
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirCampanhaColaborador(Integer campanha, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaColaboradorVO obj = (CampanhaColaboradorVO) e.next();
            obj.getCampanha().setCodigo(campanha);
            incluir(obj);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCampanhaColaborador(Integer campanha) throws Exception {
        String sqlStr = "DELETE FROM campanhacolaborador WHERE campanha = ?";
        getConexao().getJdbcTemplate().update(sqlStr.toString(), new Object[]{campanha});

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CampanhaColaboradorVO obj, List objetos) throws Exception {
        try {
            objetos.remove(obj);
            CampanhaColaborador.excluir(getIdEntidade());
            String sql = "DELETE FROM campanhacolaborador WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    public void adicionarObjCampanhaColaborador(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, CampanhaColaboradorVO obj) throws Exception {
        obj.setCampanha(campanha);
        CampanhaColaborador.validarDados(obj);
        realizarValidacaoParaPreencherDadosCampanhaColaborador(campanha, obj);
        for (CampanhaColaboradorVO objExistente : campanha.getListaCampanhaColaborador()) {
            if (objExistente.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(obj.getFuncionarioCargoVO().getFuncionarioVO().getCodigo())) {
                return;
            }
        }
        campanha.getListaCampanhaColaborador().add(obj);
    }
    
    public void adicionarObjCampanhaColaboradorAlterarCompromisso(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, CampanhaColaboradorVO obj) throws Exception {
        obj.setCampanha(campanha);
        CampanhaColaborador.validarDados(obj);
        realizarValidacaoParaPreencherDadosCampanhaColaborador(campanha, obj);
        for (CampanhaColaboradorVO objExistente : listaCampanhaColaborador) {
            if (objExistente.getFuncionarioCargoVO().getFuncionarioVO().getCodigo().equals(obj.getFuncionarioCargoVO().getFuncionarioVO().getCodigo())) {
                return;
            }
        }
        listaCampanhaColaborador.add(obj);
    }

    public void realizarValidacaoParaPreencherDadosCampanhaColaborador(CampanhaVO campanha, CampanhaColaboradorVO obj) {
        if (!campanha.getMeta().getListaMetaItem().isEmpty()) {
            if (!obj.getFuncionarioCargoVO().getNivelExperiencia().equals(NivelExperienciaCargoEnum.NENHUM)) {
                for (MetaItemVO metaItem : campanha.getMeta().getListaMetaItem()) {
                    if (metaItem.getNivelExperiencia().equals(obj.getFuncionarioCargoVO().getNivelExperiencia())) {
                        realizarPreenchementoDadosCampanhaColaborador(campanha, obj, metaItem);
                        return;
                    }
                }
            }
            for (MetaItemVO metaItem : campanha.getMeta().getListaMetaItem()) {
                if (metaItem.getPadrao()) {
                    realizarPreenchementoDadosCampanhaColaborador(campanha, obj, metaItem);
                    return;
                }
            }
        }
    }

    public void realizarPreenchementoDadosCampanhaColaborador(CampanhaVO campanha, CampanhaColaboradorVO obj, MetaItemVO metaItem) {
        if (campanha.getMeta().getTipoMeta().equals(TipoMetaEnum.DIARIA)) {
            obj.setQtdContato(metaItem.getQuantidadeContatos().intValue());
            obj.setQtdSucesso(metaItem.getQuantidadeFinalizadoSucesso().intValue());
            obj.setQtdCaptacao(metaItem.getQuantidadeMetaCaptacaoProspect().intValue());
        } else if (campanha.getMeta().getTipoMeta().equals(TipoMetaEnum.SEMANAL)) {
            if (campanha.getMeta().getConsiderarSabado()) {
                if (metaItem.getQuantidadeContatos().intValue() > 6) {
                    obj.setQtdContato(metaItem.getQuantidadeContatos().intValue() / 6);
                } else {
                    obj.setQtdContato(1);
                }
                if (metaItem.getQuantidadeFinalizadoSucesso().intValue() > 6) {
                    obj.setQtdSucesso(metaItem.getQuantidadeFinalizadoSucesso().intValue() / 6);
                } else {
                    obj.setQtdSucesso(1);
                }
                if (metaItem.getQuantidadeMetaCaptacaoProspect().intValue() > 6) {
                    obj.setQtdCaptacao(metaItem.getQuantidadeMetaCaptacaoProspect().intValue() / 6);
                } else {
                    obj.setQtdCaptacao(1);
                }
            } else {
                if (metaItem.getQuantidadeContatos().intValue() > 5) {
                    obj.setQtdContato(metaItem.getQuantidadeContatos().intValue() / 5);
                } else {
                    obj.setQtdContato(1);
                }
                if (metaItem.getQuantidadeFinalizadoSucesso().intValue() > 5) {
                    obj.setQtdSucesso(metaItem.getQuantidadeFinalizadoSucesso().intValue() / 5);
                } else {
                    obj.setQtdSucesso(1);
                }
                if (metaItem.getQuantidadeMetaCaptacaoProspect().intValue() > 5) {
                    obj.setQtdCaptacao(metaItem.getQuantidadeMetaCaptacaoProspect().intValue() / 5);
                } else {
                    obj.setQtdCaptacao(1);
                }
            }
        } else {
            if (campanha.getMeta().getConsiderarSabado()) {
                if (metaItem.getQuantidadeContatos().intValue() > 26) {
                    obj.setQtdContato(metaItem.getQuantidadeContatos().intValue() / 26);
                } else {
                    obj.setQtdContato(1);
                }
                if (metaItem.getQuantidadeFinalizadoSucesso().intValue() > 26) {
                    obj.setQtdSucesso(metaItem.getQuantidadeFinalizadoSucesso().intValue() / 26);
                } else {
                    obj.setQtdSucesso(1);
                }
                if (metaItem.getQuantidadeMetaCaptacaoProspect().intValue() > 26) {
                    obj.setQtdCaptacao(metaItem.getQuantidadeMetaCaptacaoProspect().intValue() / 26);
                } else {
                    obj.setQtdCaptacao(1);
                }
            } else {
                if (metaItem.getQuantidadeContatos().intValue() > 22) {
                    obj.setQtdContato(metaItem.getQuantidadeContatos().intValue() / 22);
                } else {
                    obj.setQtdContato(1);
                }
                if (metaItem.getQuantidadeFinalizadoSucesso().intValue() > 22) {
                    obj.setQtdSucesso(metaItem.getQuantidadeFinalizadoSucesso().intValue() / 22);
                } else {
                    obj.setQtdSucesso(1);
                }
                if (metaItem.getQuantidadeMetaCaptacaoProspect().intValue() > 22) {
                    obj.setQtdCaptacao(metaItem.getQuantidadeMetaCaptacaoProspect().intValue() / 22);
                } else {
                    obj.setQtdCaptacao(1);
                }
            }
        }
    }

    public void excluirObjCampanhaColaborador(List<CampanhaColaboradorVO> listaCampanhaColaborador, CampanhaVO campanha, Integer funcionario) throws Exception {
        int index = 0;
        for (CampanhaColaboradorVO objExistente : listaCampanhaColaborador) {
            if (objExistente.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo().equals(funcionario)) {
                listaCampanhaColaborador.remove(index);
                return;
            }
            index++;
        }
    }

    public CampanhaColaboradorVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM CampanhaColaborador WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( CampanhaColaborador).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public static CampanhaColaboradorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.getCampanha().setCodigo(dadosSQL.getInt("campanha"));
        obj.getFuncionarioCargoVO().setCodigo(dadosSQL.getInt("funcionarioCargo"));
        obj.setQtdContato(dadosSQL.getInt("qtdContato"));
        obj.setQtdSucesso(dadosSQL.getInt("qtdSucesso"));
        obj.setQtdCaptacao(dadosSQL.getInt("qtdCaptacao"));
        
        obj.setHoraInicioGerarAgenda(dadosSQL.getString("horaInicioGerarAgenda"));
        obj.setHoraFinalGerarAgenda(dadosSQL.getString("horaFinalGerarAgenda"));
        obj.setHoraInicioIntervalo(dadosSQL.getString("horaInicioIntervalo"));
        obj.setHoraFimIntervalo(dadosSQL.getString("horaFimIntervalo"));
        
        obj.setNovoObj(Boolean.FALSE);
        montarDadosFuncionarioCargo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosCampanha(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public static void montarDadosFuncionarioCargo(CampanhaColaboradorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioCargoVO().getCodigo().intValue() == 0) {
            obj.setFuncionarioCargoVO(new FuncionarioCargoVO());
            return;
        }

        obj.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(obj.getFuncionarioCargoVO().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosCampanha(CampanhaColaboradorVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCampanha().getCodigo().intValue() == 0) {
            obj.setCampanha(new CampanhaVO());
            return;
        }

        obj.setCampanha(getFacadeFactory().getCampanhaFacade().consultarPorChavePrimaria(obj.getCampanha().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static List<CampanhaColaboradorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<CampanhaColaboradorVO> vetResultado = new ArrayList<CampanhaColaboradorVO>(0);
        while (tabelaResultado.next()) {
            CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public List consultarPorParticipante(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT campanhacolaborador.* FROM campanhacolaborador"
                + " INNER JOIN funcionariocargo ON funcionariocargo.codigo = campanhacolaborador.funcionariocargo"
                + " INNER JOIN funcionario ON funcionario.codigo = funcionariocargo.funcionario"
                + " INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa"
                + "WHERE upper(pessoa.nome) "
                + "like('" + valorConsulta.toUpperCase() + "%') ORDER BY pessoa.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public List consultarPorCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT campanhacolaborador.* FROM campanhacolaborador"
                + " INNER JOIN funcionariocargo ON funcionariocargo.codigo = campanhacolaborador.funcionariocargo"
                + " INNER JOIN cargo ON cargo.codigo = funcionariocargo.cargo"
                + "WHERE upper(cargo.nome) "
                + "like('" + valorConsulta.toUpperCase() + "%') ORDER BY cargo.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public void validarDadosFuncionario(FuncionarioVO funcionario) throws ConsistirException {
        try {
            if (funcionario == null || funcionario.getCodigo() == 0) {
                throw new ConsistirException("O campo funcionario está vazio.");
            }
        } catch (ConsistirException e) {
            throw e;
        }
    }

    public List<CampanhaColaboradorVO> montarListaCampanhaColaborador(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT campanhacolaborador.* FROM campanhacolaborador WHERE campanha = " + codigo;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public void realizarExclusaoPorCursoNaCampanha(CampanhaVO campanhaVO) throws Exception {
        for (CampanhaColaboradorVO colaborador : campanhaVO.getListaCampanhaColaborador()) {
            colaborador.getListaCampanhaColaboradorCursoVOs().clear();
        }
    }

    public void adicionarObjCampanhaColaboradorCursoVOs(CampanhaColaboradorVO obj, CampanhaColaboradorCursoVO campanhaColaboradorCursoVO) throws Exception {
        campanhaColaboradorCursoVO.setCampanhaColaboradorVO(obj);
        int index = 0;
        for (CampanhaColaboradorCursoVO objExistente : obj.getListaCampanhaColaboradorCursoVOs()) {
            if (objExistente.getCursoVO().getCodigo().equals(campanhaColaboradorCursoVO.getCursoVO().getCodigo())) {
                obj.getListaCampanhaColaboradorCursoVOs().set(index, campanhaColaboradorCursoVO);
                return;
            }
            index++;
        }
        obj.getListaCampanhaColaboradorCursoVOs().add(campanhaColaboradorCursoVO);
    }
    
    public void adicionarTodosCursosCampanhaColaborador(CampanhaColaboradorVO obj) {
    	List<CursoVO> listaCursoVOs = getFacadeFactory().getCursoFacade().consultarTodosCursos();
    	obj.getListaCampanhaColaboradorCursoVOs().clear();
    	for (CursoVO cursoVO : listaCursoVOs) {
    		CampanhaColaboradorCursoVO campanhaColaboradorCursoVO = new CampanhaColaboradorCursoVO();
    		campanhaColaboradorCursoVO.setCursoVO(cursoVO);
    		campanhaColaboradorCursoVO.setCampanhaColaboradorVO(obj);
    		obj.getListaCampanhaColaboradorCursoVOs().add(campanhaColaboradorCursoVO);
		}
    }

    public void excluirObjCampanhaColaboradorCursoVOs(CampanhaColaboradorVO obj, CampanhaColaboradorCursoVO campanhaColaboradorCursoVO) throws Exception {
        int index = 0;
        for (CampanhaColaboradorCursoVO objExistente : obj.getListaCampanhaColaboradorCursoVOs()) {
            if (objExistente.getCursoVO().getCodigo().equals(campanhaColaboradorCursoVO.getCursoVO().getCodigo())) {
                obj.getListaCampanhaColaboradorCursoVOs().remove(index);
                return;
            }
            index++;
        }
    }
    
    public List<CampanhaColaboradorVO> consultarCampanhaColaboradorPorCampanha(Integer campanha, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select distinct campanhacolaborador.codigo AS \"campanhacolaborador.codigo\", campanhacolaborador.campanha AS \"campanhacolaborador.campanha\", campanhacolaborador.qtdContato AS \"campanhacolaborador.qtdContato\", ");
    	sb.append(" campanhacolaborador.qtdsucesso AS \"campanhacolaborador.qtdsucesso\", campanhacolaborador.qtdcaptacao AS \"campanhacolaborador.qtdcaptacao\", campanhacolaborador.horainiciogeraragenda AS \"campanhacolaborador.horainiciogeraragenda\", ");
    	sb.append(" campanhacolaborador.horafinalgeraragenda AS \"campanhacolaborador.horafinalgeraragenda\", campanhacolaborador.horainiciointervalo AS \"campanhacolaborador.horainiciointervalo\", ");
    	sb.append(" campanhacolaborador.horafimintervalo AS \"campanhacolaborador.horafimintervalo\", ");
    	sb.append(" funcionariocargo.codigo AS \"funcionariocargo.codigo\", funcionariocargo.nivelExperiencia AS \"funcionariocargo.nivelExperiencia\", funcionariocargo.ativo AS \"funcionariocargo.ativo\",  cargo.nome AS \"cargo.nome\", ");
    	sb.append(" funcionario.codigo AS \"funcionario.codigo\", pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
    	sb.append(" from campanhacolaborador ");
    	sb.append(" left join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
    	sb.append(" left join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
    	sb.append(" left join pessoa on pessoa.codigo = funcionario.pessoa ");
    	sb.append(" LEFT JOIN cargo ON cargo.codigo = funcionariocargo.cargo ");
    	sb.append(" where campanhaColaborador.campanha = ").append(campanha);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<CampanhaColaboradorVO> campanhaColaboradorVOs = new ArrayList<CampanhaColaboradorVO>(0);
    	while (tabelaResultado.next()) {
    		CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
    		obj.setCodigo(tabelaResultado.getInt("campanhacolaborador.codigo"));
    		obj.getCampanha().setCodigo(tabelaResultado.getInt("campanhacolaborador.campanha"));
    		obj.setQtdContato(tabelaResultado.getInt("campanhacolaborador.qtdContato"));
    		obj.setQtdSucesso(tabelaResultado.getInt("campanhacolaborador.qtdsucesso"));
    		obj.setQtdCaptacao(tabelaResultado.getInt("campanhacolaborador.qtdcaptacao"));
    		obj.setHoraInicioGerarAgenda(tabelaResultado.getString("campanhacolaborador.horainiciogeraragenda"));
    		obj.setHoraFinalGerarAgenda(tabelaResultado.getString("campanhacolaborador.horafinalgeraragenda"));
    		obj.setHoraInicioIntervalo(tabelaResultado.getString("campanhacolaborador.horainiciointervalo"));
    		obj.setHoraFimIntervalo(tabelaResultado.getString("campanhacolaborador.horafimintervalo"));
    		
    		obj.getFuncionarioCargoVO().setNivelExperiencia(NivelExperienciaCargoEnum.valueOf(tabelaResultado.getString("funcionariocargo.nivelExperiencia")));
    		obj.getFuncionarioCargoVO().getCargo().setNome(tabelaResultado.getString("cargo.nome"));
    		obj.getFuncionarioCargoVO().setCodigo(tabelaResultado.getInt("funcionariocargo.codigo"));
    		obj.getFuncionarioCargoVO().setAtivo(tabelaResultado.getBoolean("funcionariocargo.ativo"));
    		
    		obj.getFuncionarioCargoVO().getFuncionarioVO().setCodigo(tabelaResultado.getInt("funcionario.codigo"));
    		obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
    		obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
    		obj.setListaCampanhaColaboradorCursoVOs(getFacadeFactory().getCampanhaColaboradorCursoFacade().consultarCampanhaColaboradorCursos(obj.getCodigo(), false, usuarioVO));
			campanhaColaboradorVOs.add(obj);
		}
    	return campanhaColaboradorVOs;
    }
    
    @Override
    public CampanhaColaboradorVO consultarCampanhaAndResponsavelInscritoProcessoSeletivo(Integer codigoProcessoSeletivo, Integer curso,
           PoliticaGerarAgendaEnum politicaGerarAgenda, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT campanhacolaborador.codigo, funcionario.codigo as funcionario, funcionario.pessoa, campanha.codigo as campanha, campanha.unidadeEnsino as unidadeEnsino, ");
        sqlStr.append(" case when campanhacolaboradorcurso.codigo is not null then max(campanhacolaboradorcurso.dataultimocompromissogerado) else ");
        sqlStr.append(" case when max(CompromissoAgendaPessoaHorario.datacadastro) is null then '1990-01-01' else max(CompromissoAgendaPessoaHorario.datacadastro) end end as ultimaAgendaGerada, ");
        sqlStr.append(" max(CompromissoAgendaPessoaHorario.codigo) as codigoUltimaAgenda from campanhacolaborador ");
        sqlStr.append(" inner join campanha on campanha.codigo = campanhacolaborador.campanha ");
        sqlStr.append(" inner join procSeletivo on procSeletivo.campanhagerarAgendaInscritos = campanha.codigo ");
        sqlStr.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
        sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
        sqlStr.append(" left join AgendaPessoa on AgendaPessoa.pessoa = funcionario.pessoa ");
        sqlStr.append(" left join AgendaPessoaHorario on AgendaPessoaHorario.agendaPessoa = AgendaPessoa.codigo ");
        sqlStr.append(" left join CompromissoAgendaPessoaHorario on CompromissoAgendaPessoaHorario.AgendaPessoaHorario = AgendaPessoaHorario.codigo ");
        
        sqlStr.append(" left join campanhacolaboradorcurso on campanhacolaboradorcurso.campanhacolaborador = campanhacolaborador.codigo ");
        
        sqlStr.append(" where campanha.tipocampanha = '").append(TipoCampanhaEnum.CONTACTAR_INSCRITOS_PROCSELETIVO.toString()).append("' ");
        sqlStr.append(" and campanha.politicaGerarAgenda = '").append(politicaGerarAgenda.toString()).append("' ");
        sqlStr.append(" and procseletivo.codigo = ").append(codigoProcessoSeletivo).append(" ");
        sqlStr.append(" and funcionariocargo.ativo ");
        sqlStr.append(" and case when campanhacolaboradorcurso.codigo is not null then campanhacolaboradorcurso.curso = ").append(curso).append(" else true end ");
        sqlStr.append(" group by funcionario.codigo, funcionario.pessoa, campanha.codigo, campanha.unidadeEnsino, campanhacolaboradorcurso.curso, campanhacolaborador.codigo, campanhacolaboradorcurso.codigo ");
//        sqlStr.append(" order by ultimaAgendaGerada, codigoUltimaAgenda, funcionario.pessoa ");
        sqlStr.append(" order by case when campanhacolaboradorcurso.dataultimocompromissogerado is not null then campanhacolaboradorcurso.dataultimocompromissogerado end,  ultimaAgendaGerada, codigoUltimaAgenda, funcionario.pessoa  ");
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (dadosSQL.next()) {
            // como já está ordenado de forma que o compromisso mais antigo venha primeiro,
            // então este funcionario de compromisso mais antigo deve ser privilegiado
            CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
            obj.setCodigo(dadosSQL.getInt("codigo"));
            obj.getFuncionarioCargoVO().getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
            obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
            obj.getCampanha().setCodigo(dadosSQL.getInt("campanha"));
            obj.getCampanha().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
            return obj;
        }
        return new CampanhaColaboradorVO();
    }
    
    @Override
	public void validarDadosAlterarCampanhaColaborador(CampanhaVO obj) throws ConsistirException {
		if (obj.getTipoCampanha().equals(TipoCampanhaEnum.PRE_INSCRICAO)) {
			if (Uteis.isAtributoPreenchido(obj.getListaCampanhaColaborador())) {
				for (CampanhaColaboradorVO ccVO : obj.getListaCampanhaColaborador()) {
					if (!Uteis.isAtributoPreenchido(ccVO.getListaCampanhaColaboradorCursoVOs())) {
						throw new ConsistirException("O campo CURSO (Colaboradores) deve ser informado.");
					}
				}
			} else {
				throw new ConsistirException("O campo PARTICIPANTE (Colaboradores) deve ser informado.");
			}
		}
	}
    
    @Override
    public void realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultorPublicoAlvoEspecifico(Integer campanha, CampanhaPublicoAlvoVO campanhaPublicoAlvoVO, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO) {
    	HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>> mapQuantidadeProspectIniciouCampanhaVOs = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarQuantidadeCompromissoIniciouAgendaPorCampanhaProspect(campanha, campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), campanhaColaboradorVOs, usuarioVO);
    	if (mapQuantidadeProspectIniciouCampanhaVOs != null && !mapQuantidadeProspectIniciouCampanhaVOs.isEmpty()) {
    		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaColaboradorVOs) {
    			if (mapQuantidadeProspectIniciouCampanhaVOs.containsKey(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo())) {
    				campanhaColaboradorVO.setQuantidadeProspectInicouAgenda(mapQuantidadeProspectIniciouCampanhaVOs.get(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo()).size());
    				campanhaColaboradorVO.setListaCompromissoAgendaIniciouCampanhaVOs(mapQuantidadeProspectIniciouCampanhaVOs.get(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo()));
    			}
    		}
    	} else {
    		for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaColaboradorVOs) {
    			campanhaColaboradorVO.setQuantidadeProspectInicouAgenda(0);
				campanhaColaboradorVO.getListaCompromissoAgendaIniciouCampanhaVOs().clear();;
    		}
    	}
    }
    
    @Override
    public void realizarCarregamentoQuantidadeProspectIniciouAgendaPorConsultor(CampanhaVO campanhaVO, List<CampanhaColaboradorVO> campanhaColaboradorVOs, UsuarioVO usuarioVO) {
    	for (CampanhaPublicoAlvoVO campanhaPublicoAlvoVO : campanhaVO.getListaCampanhaPublicoAlvo()) {
    		HashMap<Integer, List<CompromissoAgendaPessoaHorarioVO>> mapQuantidadeProspectIniciouCampanhaVOs = getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultarQuantidadeCompromissoIniciouAgendaPorCampanhaProspect(campanhaVO.getCodigo(), campanhaPublicoAlvoVO.getCampanhaPublicoAlvoProspectVOs(), campanhaColaboradorVOs, usuarioVO);
    		if (mapQuantidadeProspectIniciouCampanhaVOs != null && !mapQuantidadeProspectIniciouCampanhaVOs.isEmpty()) {
    			for (CampanhaColaboradorVO campanhaColaboradorVO : campanhaColaboradorVOs) {
    				if (mapQuantidadeProspectIniciouCampanhaVOs.containsKey(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo())) {
    					campanhaColaboradorVO.setQuantidadeProspectInicouAgenda(mapQuantidadeProspectIniciouCampanhaVOs.get(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo()).size());
    					campanhaColaboradorVO.setListaCompromissoAgendaIniciouCampanhaVOs(mapQuantidadeProspectIniciouCampanhaVOs.get(campanhaColaboradorVO.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().getCodigo()));
    				}
    			}
    		}
    		mapQuantidadeProspectIniciouCampanhaVOs.clear();
    	}
    }
    
    @Override
    public CampanhaColaboradorVO obterConsultorIniciouCampanhaProspect(List<CampanhaColaboradorVO> listaCampanhaColaboradorVOs, Integer prospect, UsuarioVO usuarioVO) {
    	for (CampanhaColaboradorVO campanhaColaboradorVO : listaCampanhaColaboradorVOs) {
			for (CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO : campanhaColaboradorVO.getListaCompromissoAgendaIniciouCampanhaVOs()) {
				if (compromissoAgendaPessoaHorarioVO.getProspect().getCodigo().equals(prospect)) {
					return campanhaColaboradorVO;
				}
			}
		}
    	return null;
    }
    
    @Override
    public CampanhaColaboradorVO consultarColaboradorDeveRealizarProximaAgendaOrdenandoPelaDataDoUltimoCompromissoPorCampanha(Integer campanha, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT campanhacolaborador.codigo, funcionario.codigo as funcionario, funcionario.pessoa, campanha.codigo as campanha, campanha.unidadeEnsino as unidadeEnsino, ");
        sqlStr.append(" case when max(CompromissoAgendaPessoaHorario.datacadastro) is null then '1990-01-01' else max(CompromissoAgendaPessoaHorario.datacadastro) end as ultimaAgendaGerada, ");
        sqlStr.append(" max(CompromissoAgendaPessoaHorario.codigo) as codigoUltimaAgenda from campanhacolaborador ");
        sqlStr.append(" inner join campanha on campanha.codigo = campanhacolaborador.campanha ");
        sqlStr.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo ");
        sqlStr.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario ");
        sqlStr.append(" left join AgendaPessoa on AgendaPessoa.pessoa = funcionario.pessoa ");
        sqlStr.append(" left join AgendaPessoaHorario on AgendaPessoaHorario.agendaPessoa = AgendaPessoa.codigo ");
        sqlStr.append(" left join CompromissoAgendaPessoaHorario on CompromissoAgendaPessoaHorario.AgendaPessoaHorario = AgendaPessoaHorario.codigo ");
        
        sqlStr.append(" where campanha.codigo = ").append(campanha);
        sqlStr.append(" and funcionariocargo.ativo ");
        sqlStr.append(" group by funcionario.codigo, funcionario.pessoa, campanha.codigo, campanha.unidadeEnsino, campanhacolaborador.codigo ");
        sqlStr.append(" order by ultimaAgendaGerada, codigoUltimaAgenda, funcionario.pessoa ");
        SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (dadosSQL.next()) {
            // como já está ordenado de forma que o compromisso mais antigo venha primeiro,
            // então este funcionario de compromisso mais antigo deve ser privilegiado
            CampanhaColaboradorVO obj = new CampanhaColaboradorVO();
            obj.setCodigo(dadosSQL.getInt("codigo"));
            obj.getFuncionarioCargoVO().getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
            obj.getFuncionarioCargoVO().getFuncionarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
            obj.getCampanha().setCodigo(dadosSQL.getInt("campanha"));
            obj.getCampanha().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
            return obj;
        }
        return new CampanhaColaboradorVO();
    }
}
