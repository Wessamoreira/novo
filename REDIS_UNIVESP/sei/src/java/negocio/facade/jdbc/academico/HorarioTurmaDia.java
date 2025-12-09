/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.ChoqueHorarioVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioTurmaDiaInterfaceFacade;

/**
 *
 * @author Otimize-TI
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurmaDia extends ControleAcesso implements HorarioTurmaDiaInterfaceFacade {

    protected static String idEntidade;

    
    public HorarioTurmaDia() throws Exception {
        super();
        setIdEntidade("ProgramacaoAula");
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#novo()
     */
    public HorarioTurmaDiaVO novo() throws Exception {
        HorarioTurmaDia.incluir(getIdEntidade());
        HorarioTurmaDiaVO obj = new HorarioTurmaDiaVO();
        return obj;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#incluir(negocio.comuns.academico.HorarioTurmaDiaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final HorarioTurmaDiaVO obj, final UsuarioVO usuario) throws Exception {
        HorarioTurmaDiaVO.validarDados(obj);
        obj.getUsuarioResp().setCodigo(usuario.getCodigo());
        obj.getUsuarioResp().setNome(usuario.getNome());
        final String sql = "INSERT INTO HorarioTurmaDia( data, horarioTurma, ocultarDataAula, usuarioResp) VALUES ( ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
                if (obj.getHorarioTurma().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getHorarioTurma().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
				sqlInserir.setBoolean(3, obj.getOcultarDataAula());
                if (usuario.getCodigo().intValue() > 0) {
                    sqlInserir.setInt(4, usuario.getCodigo().intValue());
                } else {
                    sqlInserir.setNull(4, 0);
                }
                return sqlInserir;
            }
        }, new ResultSetExtractor<Integer>() {

            public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    obj.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
        getFacadeFactory().getHorarioTurmaDiaItemFacade().persistirHorarioTurmaDiaItem(obj, usuario);
        obj.setNovoObj(Boolean.FALSE);
        
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#alterar(negocio.comuns.academico.HorarioTurmaDiaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final HorarioTurmaDiaVO obj, final UsuarioVO usuario) throws Exception {
        HorarioTurmaDiaVO.validarDados(obj);
        obj.getUsuarioResp().setCodigo(usuario.getCodigo());
        obj.getUsuarioResp().setNome(usuario.getNome());
        final String sql = "UPDATE HorarioTurmaDia set data=?, horarioTurma=?, ocultarDataAula=?, usuarioResp=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                if (obj.getHorarioTurma().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(2, obj.getHorarioTurma().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(2, 0);
                }
                sqlAlterar.setBoolean(3, obj.getOcultarDataAula());
                if (usuario.getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(4, usuario.getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(4, 0);
                }
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        })==0){
        	incluir(obj, usuario);
        	return;
        };
        getFacadeFactory().getHorarioTurmaDiaItemFacade().persistirHorarioTurmaDiaItem(obj, usuario);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarOcultarAula(final HorarioTurmaDiaVO obj, final UsuarioVO usuario) throws Exception {
        HorarioTurmaDiaVO.validarDados(obj);
        final String sql = "UPDATE HorarioTurmaDia set ocultarDataAula=?  WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setBoolean(1, obj.getOcultarDataAula());
                sqlAlterar.setInt(2, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });        
    }

    
    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#excluir(negocio.comuns.academico.HorarioTurmaDiaVO)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(HorarioTurmaDiaVO obj, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM HorarioTurmaDia WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#consultarPorCodigoHorarioTuma(java.lang.Integer, boolean, int)
     */
    public List consultarPorCodigoHorarioTuma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT HorarioTurmaDia.* FROM HorarioTurmaDia, HorarioTuma WHERE HorarioTurmaDia.horarioTurma = HorarioTuma.codigo and HorarioTuma.codigo >= " + valorConsulta.intValue() + " ORDER BY HorarioTuma.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados);
    }

    public List<HorarioTurmaDiaVO> consultarPorTurmaDisciplinaPeriodoProfessor(Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, boolean limitarAteDataAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception {    	
        StringBuilder sqlStr =  getFacadeFactory().getHorarioTurmaFacade().getSqlConsultaCompleta(null, turma, null, ano, semestre, professor, disciplina, dataInicio, dataFim, null);       
        sqlStr.append(getFacadeFactory().getHorarioTurmaFacade().getSqlOrdenarConsultaCompleta());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaCompleta(tabelaResultado, usuario);
    }
    
    private List<HorarioTurmaDiaVO> montarDadosConsultaCompleta(SqlRowSet rs, UsuarioVO usuario) throws Exception {		
		Integer horarioTurmaDia = null;
		HorarioTurmaDiaVO horarioTurmaDiaVO = null;
		List<HorarioTurmaDiaVO> horarioTurmaDiaVOs = new ArrayList<HorarioTurmaDiaVO>(0);
		while (rs.next()) {			
			if (horarioTurmaDia == null || !horarioTurmaDia.equals(rs.getInt("horarioturmadia"))) {
				horarioTurmaDia = rs.getInt("horarioturmadia");
				horarioTurmaDiaVO = montarDadosConsultaCompletaHorarioTurmaDia(rs, usuario);
				horarioTurmaDiaVOs.add(horarioTurmaDiaVO);
			}
			horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().add(montarDadosConsultaCompletaHorarioTurmaDiaItem(rs, usuario));
		}
		return horarioTurmaDiaVOs;
	}
    
        
    // metodo corrigi situação onde a programação da pos de 4 aulas, esta correta porem foi registrado 3 aulas, ai o sistema ficava faltando 1 dia de aula, e apenas era possível corrigir via base. Esse metodo permiti corrigir.
    public List<HorarioTurmaDiaVO> consultarPorTurmaDisciplinaPeriodoProfessorNaoConstaRegistroAula(Integer turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataInicio, Date dataFim, boolean limitarAteDataAtual, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	 StringBuilder sqlStr =  getFacadeFactory().getHorarioTurmaFacade().getSqlConsultaCompleta(null, turma, null, ano, semestre, professor, disciplina, dataInicio, dataFim, null);
    	 sqlStr.append(" WHERE registroaula.codigo is null ");
         sqlStr.append(getFacadeFactory().getHorarioTurmaFacade().getSqlOrdenarConsultaCompleta());
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
         return montarDadosConsultaCompleta(tabelaResultado, usuario);
         
    }

    public List<HorarioTurmaDiaVO> consultarPorTurmaPeriodoUnidadeAnoSemestre(String identificadorTurma, Date dataInicio, Date dataFim, String ano, String semestre, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso,  int nivelMontarDados, UsuarioVO usuario) throws Exception {    	
    	 StringBuilder sqlStr =  getFacadeFactory().getHorarioTurmaFacade().getSqlConsultaCompleta(null, null, identificadorTurma, ano, semestre, null, disciplina, dataInicio, dataFim, unidadeEnsino);    	 
         sqlStr.append(getFacadeFactory().getHorarioTurmaFacade().getSqlOrdenarConsultaCompleta());
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
         return montarDadosConsultaCompleta(tabelaResultado, usuario);        
    }

    
    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HorarioTurmaDia WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }    

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            HorarioTurmaDiaVO obj = new HorarioTurmaDiaVO();
            obj = montarDados(tabelaResultado, nivelMontarDados);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>HorarioTurmaDiaVO</code>.
     * @return  O objeto da classe <code>HorarioTurmaDiaVO</code> com os dados devidamente montados.
     */
    public static HorarioTurmaDiaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        HorarioTurmaDiaVO obj = new HorarioTurmaDiaVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setData(tabelaResultado.getDate("data"));
        obj.getHorarioTurma().setCodigo(new Integer(tabelaResultado.getInt("horarioTurma")));
        obj.setOcultarDataAula(tabelaResultado.getBoolean("ocultarDataAula"));
        obj.getUsuarioResp().setCodigo(tabelaResultado.getInt("usuarioresp"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        if(Uteis.isAtributoPreenchido(obj.getUsuarioResp().getCodigo())){
        	obj.setUsuarioResp(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(tabelaResultado.getInt("usuarioresp"), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
        }
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
        	obj.setHorarioTurmaDiaItemVOs(getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarPorHorarioTurmaDia(obj.getCodigo()));
            return obj;
        }

        return obj;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>HorarioTurmaDiaVO</code>.
     * @return  O objeto da classe <code>HorarioTurmaDiaVO</code> com os dados devidamente montados.
     */
    public HorarioTurmaDiaVO montarDados(SqlRowSet tabelaResultado, HorarioTurmaVO horarioTurma, int nivelMontarDados) throws Exception {
        HorarioTurmaDiaVO obj = new HorarioTurmaDiaVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setData(tabelaResultado.getDate("data"));
        obj.getHorarioTurma().setCodigo(new Integer(tabelaResultado.getInt("horarioTurma")));
        obj.setOcultarDataAula(tabelaResultado.getBoolean("ocultarDataAula"));		
        obj.setNovoObj(false);

        obj.setHorarioTurmaDiaItemVOs(new ArrayList<HorarioTurmaDiaItemVO>(0));
        int x = 1;
        int nrAulas = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurma.getTurno(), obj.getDiaSemanaEnum());
        while (x <= nrAulas) {
            HorarioTurmaDiaItemVO item = new HorarioTurmaDiaItemVO();
            item.setNrAula(x);
            item.setHorario(getFacadeFactory().getTurnoFacade().realizarConsultaHorarioInicioFim(horarioTurma.getTurno(), obj.getDiaSemanaEnum(), x));
            item.getDisciplinaVO().setCodigo(obj.getCodigoDisciplina(x));
            item.getFuncionarioVO().setCodigo(obj.getCodigoProfessor(x));
            obj.getHorarioTurmaDiaItemVOs().add(item);
            x++;
        }
        Ordenacao.ordenarLista(obj.getHorarioTurmaDiaItemVOs(), "nrAula");

        return obj;

    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#excluirHorarioTurmaDias(java.lang.Integer)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirHorarioTurmaDias(Integer horarioTurma, UsuarioVO usuario) throws Exception {
        HorarioTurmaDia.excluir(getIdEntidade());
        String sql = "DELETE FROM HorarioTurmaDia WHERE (horarioTurma = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{horarioTurma});
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#alterarHorarioTurmaDias(java.lang.Integer, int, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarHorarioTurmaDias(Integer horarioTurma, TurnoVO turno, List<HorarioTurmaDiaVO> objetos, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM HorarioTurmaDia WHERE (horarioTurma = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        Integer nrAula = 0;
        for (HorarioTurmaDiaVO horarioTurmaDiaVO : objetos) {
            nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turno, horarioTurmaDiaVO.getDiaSemanaEnum());
            if (horarioTurmaDiaVO.getExisteAula(nrAula) && horarioTurmaDiaVO.getCodigo().intValue() > 0) {
                sql += " and codigo <> " + horarioTurmaDiaVO.getCodigo();
            }
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{horarioTurma});
        for (HorarioTurmaDiaVO horarioTurmaDiaVO : objetos) {
            nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turno, horarioTurmaDiaVO.getDiaSemanaEnum());
            if (horarioTurmaDiaVO.getExisteAula(nrAula)) {
                if (horarioTurmaDiaVO.getCodigo().intValue() == 0) {
                    horarioTurmaDiaVO.getHorarioTurma().setCodigo(horarioTurma);
                    incluir(horarioTurmaDiaVO, usuario);
                    getFacadeFactory().getHorarioTurmaLogFacade().executarLogGravarInclusao(horarioTurmaDiaVO, usuario);
                } else {
                    horarioTurmaDiaVO.getHorarioTurma().setCodigo(horarioTurma);
                    alterar(horarioTurmaDiaVO, usuario);
                    getFacadeFactory().getHorarioTurmaLogFacade().executarLogGravarAlteracao(horarioTurmaDiaVO, usuario);
                }
            } else {
                excluir(horarioTurmaDiaVO, usuario);
            }
        }
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#incluirHorarioTurmaDias(java.lang.Integer, int, java.util.List)
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirHorarioTurmaDias(Integer horarioTurmaPrm, TurnoVO turno, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        Integer nrAula = 0;
        while (e.hasNext()) {
            HorarioTurmaDiaVO obj = (HorarioTurmaDiaVO) e.next();
            nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(turno, obj.getDiaSemanaEnum());
            obj.getHorarioTurma().setCodigo(horarioTurmaPrm);
            if (obj.getExisteAula(nrAula)) {
                incluir(obj, usuario);
                getFacadeFactory().getHorarioTurmaLogFacade().executarLogGravarInclusao(obj, usuario);
            }
        }
    }

    /***
     * Obtém o códigp do ultimo registro
     * @return
     * @throws Exception
     */
    public int consultaUltimoRegistro() throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT MAX(codigo) as codigoMaior FROM HorarioTurmaDia ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        int ultimoReg = 0;
        while (tabelaResultado.next()) {
            ultimoReg = tabelaResultado.getInt("codigoMaior");
        }
        return ultimoReg;
    }

    /**
     * Operação responsável por consultar todos os <code>HorarioTurmaDiaVO</code> relacionados a um objeto da classe <code>academico.HorarioTuma</code>.
     * @param horarioTurma  Atributo de <code>academico.HorarioTuma</code> a ser utilizado para localizar os objetos da classe <code>HorarioTurmaDiaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public List consultarHorarioTurmaDias(Integer horarioTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HorarioTurmaDia.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT codigo, data,horarioTurma  ");
        sqlStr.append(" FROM HorarioTurmaDia WHERE ");
        sqlStr.append(" horarioTurma = ").append(horarioTurma);
        sqlStr.append(" order by data");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        while (tabelaResultado.next()) {
            HorarioTurmaDiaVO novoObj = new HorarioTurmaDiaVO();
            novoObj = HorarioTurmaDia.montarDados(tabelaResultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    public List<HorarioTurmaDiaVO> consultarHorarioTurmaDias(HorarioTurmaVO horarioTurma, String mes, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        HorarioTurmaDia.consultar(getIdEntidade(), controlarAcesso, usuario);
        
        StringBuilder sqlStr = getSqlConsultaCompleta(horarioTurma.getCodigo(), null, null, null, null, null, null, null, null, null);
        sqlStr.append(" WHERE 1 = 1 ");
        if (mes != null && !mes.equals("")) {
            sqlStr.append(" and EXTRACT( MONTH from (data) ) = '").append(mes).append("'");
        }
        if (ano != null && !ano.equals("")) {
            sqlStr.append(" and EXTRACT( YEAR from (data) ) = '").append(ano).append("'");
        }        
        return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), usuario);        
    }


    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int)
     */
    public HorarioTurmaDiaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM HorarioTurmaDia WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return HorarioTurmaDia.idEntidade;
    }

    /* (non-Javadoc)
     * @see negocio.facade.jdbc.academico.HorarioTurmaDiaInterfaceFacade#setIdEntidade(java.lang.String)
     */
    public void setIdEntidade(String idEntidade) {
        HorarioTurmaDia.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<HorarioTurmaDiaVO> consultarPorHorarioTurmaDiaContendoDisciplina(Integer codigoDisciplina) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT HorarioTurmaDia.* FROM HorarioTurmaDia WHERE ");
        sqlStr.append(" exists (select HorarioTurmaDiaitem.codigo from HorarioTurmaDiaitem where HorarioTurmaDiaitem.HorarioTurmaDia = HorarioTurmaDia.codigo and disciplina  = ").append(codigoDisciplina).append(" limit 1)");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
    }

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurmaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
        for (HorarioTurmaDiaVO item : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
            montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, item, matriculaPeriodoTurmaDisciplinaVOs);
        }
    }

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurmaVO) {
        for (HorarioTurmaDiaVO item : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
            montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, item);
        }
    }

    /**
     * Método responsável por verificar a disponibilidade do horario da turma 
     * o parametro retornarExcecao defini se deve retorno throws ou true(Disponivel)/false(Indisponivel)
     * 
     * @param horarioTurmaDiaVO
     * @param nrAula
     * @param horario
     * @param disciplina
     * @param novoProfessor
     * 
     * @throws Exception
     * 
     * @see {@link HorarioTurma} método - executarVerificarDisponibilidadeHorarioMarcadoTurmaPorDia
     * 
     */
    public Boolean executarVerificarDisponibilidadeProfessorEDisciplinaAula(HorarioTurmaVO horarioTurmaVO, HorarioTurmaDiaVO horarioTurmaDiaVO, Integer nrAula, String horario, DisciplinaVO disciplina, PessoaVO novoProfessor, boolean retornarExcecao) throws Exception {
        
        for (HorarioTurmaDiaItemVO horarioTurmaDiaItem : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
            if (horarioTurmaDiaItem.getNrAula().intValue() == nrAula.intValue()) {
                if (horarioTurmaDiaItem.getDisciplinaLivre() && horarioTurmaDiaItem.getProfessorLivre()) {
                    return true;
                }
//                if ((horarioTurmaDiaItem.getDisciplinaVO().getCodigo().intValue() != disciplina.getCodigo().intValue()
//                        || horarioTurmaDiaItem.getFuncionarioVO().getCodigo().intValue() != novoProfessor.getCodigo().intValue())) {
                	if(retornarExcecao){
                		throw new ChoqueHorarioVO(horarioTurmaDiaVO.getData(), nrAula, horario, horarioTurmaVO.getTurma().getIdentificadorTurma(), disciplina.getNome(), novoProfessor.getNome(), horarioTurmaVO.getTurma().getTurno().getNome(), "", false); 
                				//"O horario " + horario + " do dia " + Uteis.getData(horarioTurmaDiaVO.getData()) + " não está disponivel no horário da turma.");
                	}
                	return false;
//                }
//                return false;
            }
            
        }
        return true;
    }

    public boolean executarVerificarAlunoPossuiAula(DisciplinaVO disciplina, TurmaVO turma, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
        for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
            if ((matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo().intValue() == disciplina.getCodigo().intValue())
                    && (matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo().intValue() == turma.getCodigo().intValue() || turma.getIsPossuiCodigoTurmaAgrupada(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo()))) {
                return true;
            }
        }
        return false;
    }

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
//        horarioTurmaDiaVO.setHorarioTurmaDiaItemVOs(new ArrayList<HorarioTurmaDiaItemVO>(0));
        int x = 1;
        int nrAulas = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurma.getTurno(), horarioTurmaDiaVO.getDiaSemanaEnum());
        q:while (x <= nrAulas) {
        	for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO: horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()){
        		if(horarioTurmaDiaItemVO.getNrAula().intValue() == x){
        			x++;
        			continue q;
        		}
        	}	
            HorarioTurmaDiaItemVO item = new HorarioTurmaDiaItemVO();
            item.setNrAula(x);
            item.setHorario(getFacadeFactory().getTurnoFacade().realizarConsultaHorarioInicioFim(horarioTurma.getTurno(), horarioTurmaDiaVO.getDiaSemanaEnum(), x));
            item.getDisciplinaVO().setCodigo(horarioTurmaDiaVO.getCodigoDisciplina(x));
            item.getFuncionarioVO().setCodigo(horarioTurmaDiaVO.getCodigoProfessor(x));
            if (executarVerificarAlunoPossuiAula(item.getDisciplinaVO(), horarioTurma.getTurma(), matriculaPeriodoTurmaDisciplinaVOs)) {
                horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().add(item);
            }
            x++;
        }
        Ordenacao.ordenarLista(horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs(), "nrAula");
    }

    public void montarDadosHorarioTurmaDiaItemVOs(HorarioTurmaVO horarioTurma, HorarioTurmaDiaVO horarioTurmaDiaVO) {

        int x = 1;
        int nrAulas = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurma.getTurno(), horarioTurmaDiaVO.getDiaSemanaEnum());
        if(nrAulas == horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().size()){
        	return;
        }
        q:while (x <= nrAulas) {
        	for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO: horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()){
        		if(horarioTurmaDiaItemVO.getNrAula().intValue() == x){
        			x++;
        			continue q;
        		}
        	}
            HorarioTurmaDiaItemVO item = new HorarioTurmaDiaItemVO();
            item.setNrAula(x);
            TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioTurma.getTurno(), horarioTurmaDiaVO.getDiaSemanaEnum(), x);
            item.setHorario(turnoHorarioVO.getHorarioInicioAula()+" "+UteisJSF.internacionalizar("prt_a")+" "+turnoHorarioVO.getHorarioFinalAula());
            item.setHorarioInicio(turnoHorarioVO.getHorarioInicioAula());
            item.setHorarioTermino(turnoHorarioVO.getHorarioFinalAula());
            item.setDuracaoAula(getFacadeFactory().getTurnoFacade().realizarConsultaDuracaoHorarioInicioFim(horarioTurma.getTurno(), horarioTurmaDiaVO.getDiaSemanaEnum(), x));
            item.getDisciplinaVO().setCodigo(horarioTurmaDiaVO.getCodigoDisciplina(x));
            item.getFuncionarioVO().setCodigo(horarioTurmaDiaVO.getCodigoProfessor(x));
            item.getGoogleMeetVO().setCodigo(horarioTurmaDiaVO.getCodigoGoogleMeet(x));
            horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().add(item);
            x++;
        }
        Ordenacao.ordenarLista(horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs(), "nrAula");
    }

    public HorarioTurmaDiaVO consultarHorarioTurmaPorDiaPorDia(HorarioTurmaVO horarioTurmaVO, Date date) {
        for (HorarioTurmaDiaVO obj : horarioTurmaVO.getHorarioTurmaDiaVOs()) {
            if (Uteis.getData(obj.getData()).equals(Uteis.getData(date))) {
                //if (obj.getHorarioTurmaDiaItemVOs().isEmpty()) {
                    montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, obj);
                //}
                return obj;
            }
        }
        HorarioTurmaDiaVO obj = new HorarioTurmaDiaVO();
        obj.setData(date);   
        obj.setHorarioTurma(horarioTurmaVO);
        montarDadosHorarioTurmaDiaItemVOs(horarioTurmaVO, obj);
        return obj;
    }

    /***
     * Método verifica se realmente exite no horário da turma a aula do horariodoprofessor
     * @param data
     * @param codigoDisciplina
     * @param codigoProfessor
     * @param ano
     * @param semestre
     * @param codigoTurma
     * @return
     */    
    public boolean executarVerificarSeHaAulaNaTurmaParaDeterminadoProfessor(Date data, Integer numeroAula, Integer codigoDisciplina, Integer codigoProfessor, int codigoTurma) throws Exception {               
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("select horarioturma.codigo from horarioturma ");
        sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sqlStr.append("inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append("where horarioturma.turma = ").append(codigoTurma);
        if(Uteis.isAtributoPreenchido(data)) {
        	sqlStr.append(" and horarioturmadia.data::date = '").append(Uteis.getDataJDBC(data)).append("' ");
        }
        if(Uteis.isAtributoPreenchido(numeroAula)) {
        	sqlStr.append("and horarioturmadiaitem.nrAula = ").append(numeroAula);
        }
        if(Uteis.isAtributoPreenchido(codigoDisciplina)) {
        	sqlStr.append("and horarioturmadiaitem.disciplina = ").append(codigoDisciplina);
        }
        if(Uteis.isAtributoPreenchido(codigoProfessor)) {
        	sqlStr.append("and horarioturmadiaitem.professor = ").append(codigoProfessor);
        }        
        sqlStr.append(" limit 1 ");        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado.next();            
    }

    public Date consultarUltimaDataAulaPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre, Integer professor) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" ");
        sqlStr.append(" SELECT MAX(datafinal.data) AS data FROM (");
        sqlStr.append(" SELECT MAX(htd.data) AS data FROM horarioturmadia htd");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
        sqlStr.append(" INNER JOIN turma ON ht.turma = turma.codigo");
        if (Uteis.isAtributoPreenchido(disciplina)) {
        	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
            sqlStr.append(" AND htdi.disciplina = ").append(disciplina);
        }  
        if (Uteis.isAtributoPreenchido(professor)) {
            sqlStr.append(" AND htdi.professor = ").append(professor);
        }
        sqlStr.append(" WHERE turma.codigo = ").append(turma);
        sqlStr.append(" and ((turma.anual and ht.anovigente = '").append(ano).append("') ");
        sqlStr.append(" or (turma.semestral and ht.anovigente = '").append(ano).append("' and ht.semestrevigente = '").append(semestre).append("') ");
        sqlStr.append(" or (turma.anual = false and turma.semestral =  false)) ");
        sqlStr.append(" union all");
        sqlStr.append(" select max(pto.dataterminoaula) as data from programacaotutoriaonline pto ");
        sqlStr.append(" inner join turma on pto.turma = turma.codigo ");
        if (Uteis.isAtributoPreenchido(disciplina)) {
            sqlStr.append(" AND pto.disciplina = ").append(disciplina);
        }
        sqlStr.append(" WHERE turma.codigo = ").append(turma);
        sqlStr.append(") as datafinal");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
        	if(tabelaResultado.getDate("data") != null) {
        		return tabelaResultado.getDate("data");
        	}
        }
        sqlStr = new StringBuilder("");
        sqlStr.append(" SELECT MAX(htd.data) AS data FROM horarioturmadia htd");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
        sqlStr.append(" INNER JOIN turma ON ht.turma = turma.codigo");
        sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turmaorigem = turma.codigo");
        sqlStr.append(" INNER JOIN turma as t ON turmaagrupada.turma = t.codigo");
        if (Uteis.isAtributoPreenchido(disciplina)) {
        	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
            sqlStr.append(" AND ( (htdi.disciplina = ").append(disciplina).append(") ");
            sqlStr.append(" or (htdi.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina  =  ").append(disciplina).append(" )) ");
            sqlStr.append(" or (htdi.disciplina in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente  =  ").append(disciplina).append(" )) ");
            sqlStr.append(" ) ");
        }else {
        	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
        	sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.turma = t.codigo ");
        	sqlStr.append(" and ( turmadisciplina.disciplina = htdi.disciplina ");
        	sqlStr.append(" or (turmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina  =   htdi.disciplina )) ");
            sqlStr.append(" or (turmadisciplina.disciplina in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente  =  htdi.disciplina )) ");
            sqlStr.append(" ) ");
        }
        sqlStr.append(" WHERE t.codigo = ").append(turma);
        sqlStr.append(" and ((turma.anual and ht.anovigente = '").append(ano).append("') ");
        sqlStr.append(" or (turma.semestral and ht.anovigente = '").append(ano).append("' and ht.semestrevigente = '").append(semestre).append("') ");
        sqlStr.append(" or (turma.anual = false and turma.semestral =  false)) ");
        
        tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarPrimeiraDataAulaPorTurmaDisciplina(Integer turma, Integer disciplina, String ano, String semestre) throws Exception {
    	 StringBuilder sqlStr = new StringBuilder(" ");
    	 sqlStr.append(" select min(datainicio.data) as data from (");
         sqlStr.append(" SELECT MIN(htd.data) AS data FROM horarioturmadia htd");
         sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
         sqlStr.append(" INNER JOIN turma ON ht.turma = turma.codigo");
         if (Uteis.isAtributoPreenchido(disciplina)) {
         	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
             sqlStr.append(" AND htdi.disciplina = ").append(disciplina);
         }        
         sqlStr.append(" WHERE turma.codigo = ").append(turma);
         sqlStr.append(" and ((turma.anual and ht.anovigente = '").append(ano).append("') ");
         sqlStr.append(" or (turma.semestral and ht.anovigente = '").append(ano).append("' and ht.semestrevigente = '").append(semestre).append("') ");
         sqlStr.append(" or (turma.anual = false and turma.semestral =  false)) ");
         sqlStr.append(" union all");
         sqlStr.append(" select min(pto.datainicioaula) as data from programacaotutoriaonline pto ");
         sqlStr.append(" inner join turma on pto.turma = turma.codigo ");
         if (Uteis.isAtributoPreenchido(disciplina)) {
             sqlStr.append(" AND pto.disciplina = ").append(disciplina);
         }
         sqlStr.append(" WHERE turma.codigo = ").append(turma);
         sqlStr.append(") as datainicio");
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
         while (tabelaResultado.next()) {
        	 if(tabelaResultado.getDate("data") != null) {
        		 return tabelaResultado.getDate("data");
        	 }
         }
         sqlStr = new StringBuilder("");
         sqlStr.append(" SELECT MIN(htd.data) AS data FROM horarioturmadia htd");
         sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
         sqlStr.append(" INNER JOIN turma ON ht.turma = turma.codigo");
         sqlStr.append(" INNER JOIN turmaagrupada ON turmaagrupada.turmaorigem = turma.codigo");
         sqlStr.append(" INNER JOIN turma as t ON turmaagrupada.turma = t.codigo");
         if (Uteis.isAtributoPreenchido(disciplina)) {
         	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
             sqlStr.append(" AND ( (htdi.disciplina = ").append(disciplina).append(") ");
             sqlStr.append(" or (htdi.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina  =  ").append(disciplina).append(" )) ");
             sqlStr.append(" or (htdi.disciplina in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente  =  ").append(disciplina).append(" )) ");
             sqlStr.append(" ) ");
         }else {
         	sqlStr.append(" INNER JOIN horarioturmadiaitem htdi ON htd.codigo = htdi.horarioturmadia");
         	sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.turma = t.codigo ");
         	sqlStr.append(" and ( turmadisciplina.disciplina = htdi.disciplina ");
         	sqlStr.append(" or (turmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina  =   htdi.disciplina )) ");
             sqlStr.append(" or (turmadisciplina.disciplina in (select disciplina from disciplinaequivalente where disciplinaequivalente.equivalente  =  htdi.disciplina )) ");
             sqlStr.append(" ) ");
         }
         sqlStr.append(" WHERE t.codigo = ").append(turma);
         sqlStr.append(" and ((turma.anual and ht.anovigente = '").append(ano).append("') ");
         sqlStr.append(" or (turma.semestral and ht.anovigente = '").append(ano).append("' and ht.semestrevigente = '").append(semestre).append("') ");
         sqlStr.append(" or (turma.anual = false and turma.semestral =  false)) ");
         
        tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }
    
    @Override
    public Date consultarPrimeiraDataAulaPorMatriculaPorDisciplina(String matricula, Integer disciplina) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" SELECT MIN(horarioturmadia.data) AS data ");
        sqlStr.append(" FROM matriculaperiodoturmadisciplina as mptd ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ");
        sqlStr.append(" inner join horarioturma on horarioturma.turma = mptd.turma ");
        sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
        sqlStr.append(" WHERE matriculaperiodo.matricula = '").append(matricula).append("' ");
        sqlStr.append(" and mptd.disciplina = ").append(disciplina);
        sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo != 'PC' ");
        sqlStr.append(" and horarioturmadiaitem.disciplina = mptd.disciplina ");
        sqlStr.append(" and mptd.codigo in ( ");
        sqlStr.append(" select mptd1.codigo from matriculaperiodoturmadisciplina as mptd1 where mptd1.matricula =  matriculaperiodo.matricula ");
        sqlStr.append(" and mptd1.disciplina = mptd.disciplina  and mptd1.modalidadeDisciplina = mptd.modalidadeDisciplina  ");
        sqlStr.append(" order by mptd1.ano ||'/'|| mptd1.semestre desc, mptd1.codigo desc limit 1 ) ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }
    
    @Override
    public Date consultarUltimaDataAulaPorMatriculaPorDisciplina(String matricula, Integer disciplina) throws Exception {
    	StringBuilder sqlStr = new StringBuilder(" SELECT MAX(horarioturmadia.data) AS data ");
    	sqlStr.append(" FROM matriculaperiodoturmadisciplina as mptd ");
    	sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = mptd.matriculaperiodo ");
    	sqlStr.append(" inner join horarioturma on horarioturma.turma = mptd.turma ");
    	sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
    	sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
    	sqlStr.append(" WHERE matriculaperiodo.matricula = '").append(matricula).append("' ");
    	sqlStr.append(" and mptd.disciplina = ").append(disciplina);
    	sqlStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo != 'PC' ");
    	sqlStr.append(" and horarioturmadiaitem.disciplina = mptd.disciplina ");
    	sqlStr.append(" and mptd.codigo in ( ");
    	sqlStr.append(" select mptd1.codigo from matriculaperiodoturmadisciplina as mptd1 where mptd1.matricula =  matriculaperiodo.matricula ");
    	sqlStr.append(" and mptd1.disciplina = mptd.disciplina  and mptd1.modalidadeDisciplina = mptd.modalidadeDisciplina  ");
    	sqlStr.append(" order by mptd1.ano ||'/'|| mptd1.semestre desc, mptd1.codigo desc limit 1 ) ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	while (tabelaResultado.next()) {
    		return tabelaResultado.getDate("data");
    	}
    	return null;
    }
    

    public Date consultarUltimaDataAulaPorTurmaAnteriorDataAtual(Integer turma) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT MAX(data) AS data FROM horarioturmadia htd");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
        sqlStr.append(" WHERE ht.turma = ").append(turma).append(" AND htd.data <= '").append(Uteis.getDataJDBC(new Date())).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarUltimaDataAulaPorTurma(Integer turma, String ano, String semestre) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT MAX(data) AS data FROM horarioturmadia htd");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
        sqlStr.append(" WHERE ht.turma = ").append(turma);
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append(" AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("'");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select max(htd.data) as data from matriculaperiodoturmadisciplina ");
        sqlStr.append(" inner join turma on matriculaperiodoturmadisciplina.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturmadia htd ON htd.horarioturma = ht.codigo");
        sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarUltimaDataAulaPorMatricula(String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select max(htd.data) as data from matriculaperiodo ");
        sqlStr.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturmadia htd ON htd.horarioturma = ht.codigo");
        sqlStr.append(" where matriculaperiodo.matricula = '").append(matricula).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarPrimeiraDataAulaPorMatricula(String matricula) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select min(htd.data) as data from matriculaperiodo ");
        sqlStr.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.turma = turma.codigo");
        sqlStr.append(" INNER JOIN horarioturmadia htd ON htd.horarioturma = ht.codigo");
        sqlStr.append(" where matriculaperiodo.matricula = '").append(matricula).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarPrimeiraDataAulaPorTurma(Integer turma, String ano, String semestre) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT MIN(data) AS data FROM horarioturmadia htd");
        sqlStr.append(" INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma");
        sqlStr.append(" WHERE ht.turma = ").append(turma);
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append(" AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("'");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarPrimeiraDataAulaPorTurmaAgrupada(Integer turma, String ano, String semestre) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select MIN(data) AS data FROM(");
        sqlStr.append("(SELECT MIN(data) AS data FROM horarioturmadia htd ");
        sqlStr.append("INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma ");
        sqlStr.append("WHERE ht.turma = ").append(turma).append(" ");
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append("AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("' ");
        }
        sqlStr.append(") UNION (");
        sqlStr.append("SELECT MIN(data) AS data FROM horarioturmadia htd ");
        sqlStr.append("INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma ");
        sqlStr.append("INNER JOIN turmaagrupada ta ON ta.turmaorigem = ht.turma ");
        sqlStr.append("WHERE ta.turma = ").append(turma).append(" ");
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append("AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("' ");
        }
        sqlStr.append(")) as primeiraData");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Date consultarUltimaDataAulaPorTurmaAgrupada(Integer turma, String ano, String semestre) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT MAX(data) AS data FROM(");
        sqlStr.append("(SELECT MAX(data) AS data FROM horarioturmadia htd ");
        sqlStr.append("INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma ");
        sqlStr.append("WHERE ht.turma = ").append(turma).append(" ");
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append("AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("' ");
        }
        sqlStr.append(") UNION (");
        sqlStr.append("SELECT MAX(data) AS data FROM horarioturmadia htd ");
        sqlStr.append("INNER JOIN horarioturma ht ON ht.codigo = htd.horarioturma ");
        sqlStr.append("INNER JOIN turmaagrupada ta ON ta.turmaorigem = ht.turma ");
        sqlStr.append("WHERE ta.turma = ").append(turma).append(" ");
        if (!ano.equalsIgnoreCase("") && !semestre.equalsIgnoreCase("")) {
            sqlStr.append("AND ht.anoVigente = '").append(ano).append("'").append(" AND ht.semestreVigente = '").append(semestre).append("' ");
        }
        sqlStr.append(")) as ultimaData");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public Integer consultarQtdAulaRealizadaAteDataAtual(Integer turma) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select count(distinct disciplina.codigo) as qtdAulas ");
        sqlStr.append(" from horarioturma ");
        sqlStr.append(" inner join HorarioTurmaDia on horarioturma.codigo = HorarioTurmaDia.horarioturma ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.HorarioTurmaDia = HorarioTurmaDia.codigo ");
        sqlStr.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
        sqlStr.append(" where horarioturma.turma = ").append(turma).append(" ");
        sqlStr.append(" and HorarioTurmaDia.data <= now()  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getInt("qtdAulas");
        }
        return null;
    }

    public Date consultarUltimaDataAulaProgramadaMenorDataAtual(Integer turma) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select horarioturmadia.data from horarioturmadia ");
        sqlStr.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(" where horarioturmadia.data <= '").append(Uteis.getDataJDBC(new Date())).append("' ");
        sqlStr.append(" and horarioturma.turma = ").append(turma).append(" ");
        sqlStr.append(" order by horarioturmadia.data desc limit 1 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            return tabelaResultado.getDate("data");
        }
        return null;
    }

    public List<DisciplinaVO> consultarDisciplinaUltimaDataAulaProgramadaMenorDataAtual(Integer turma, Integer qtd ) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");        
        sqlStr.append(" select distinct disciplina.codigo  ");
        sqlStr.append(" from horarioturma ");
        sqlStr.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma  ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia  ");
        sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina  ");
        sqlStr.append(" where horarioturma.turma = ").append(turma);
        sqlStr.append(" limit ").append(qtd);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List<DisciplinaVO> lista = new ArrayList<DisciplinaVO>();
        while (tabelaResultado.next()) {
            DisciplinaVO disc = new DisciplinaVO();
            disc.setCodigo(Integer.parseInt(tabelaResultado.getString("codigo")));
            lista.add(disc);
        }
        return lista;
    }

    public Boolean consultarExistenciaProgramacaoAulaPorCursoTurmaProfessorData(Integer unidadeEnsino, Integer curso, Integer turma, Integer professor, Boolean graduacao, Boolean posGraduacao, String ano, String semestre, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	 ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
         StringBuilder sqlStr = new StringBuilder("SELECT htd.* FROM horarioturmadia htd ");
         sqlStr.append("inner join horarioturma ht on ht.codigo = htd.horarioturma ");
         sqlStr.append("inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia "); 
         sqlStr.append("inner join turma on turma.codigo = ht.turma ");
         sqlStr.append("left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso)  ");
         sqlStr.append(" or (turma.turmaagrupada and curso.codigo in (select distinct t.curso from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo))) ");
         sqlStr.append("WHERE 1=1 ");
         if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
             sqlStr.append("AND turma.unidadeensino = ").append(unidadeEnsino).append(" ");
         }
         if (curso != null && !curso.equals(0)) {
             sqlStr.append("AND turma.curso = ").append(curso).append(" ");
         }
         if (turma != null && !turma.equals(0)) {
             sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
         }
         if (professor != null && !professor.equals(0)) {
             sqlStr.append("AND htdi.professor = ").append(professor).append(" ");
         }
         if (ano != null && !ano.equals("")) {
             sqlStr.append("AND ht.anovigente = '").append(ano).append("' ");
         }
         if (semestre != null && !semestre.equals("")) {
             sqlStr.append("AND ht.semestrevigente = '").append(semestre).append("' ");
         }
         if (disciplina != null && !disciplina.equals(0)) {
        	 sqlStr.append(" AND htdi.disciplina = ").append(disciplina).append(" ");
         }
         if (posGraduacao != null && posGraduacao) {
             sqlStr.append("AND curso.nivelEducacional = 'PO' ");
         }
         if (graduacao != null && graduacao) {
             sqlStr.append("AND curso.nivelEducacional != 'PO' ");
         }
         sqlStr.append("LIMIT 1");
         SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
         return tabelaResultado.next();
    }

    public Boolean consultarExistenciaProgramacaoAulaPorTurmaDisciplinaData(Integer turma, Integer disciplina, Date dataVerificacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT htd.codigo FROM horarioturmadia htd ");
        sqlStr.append("INNER JOIN horarioturma ht on ht.codigo = htd.horarioturma ");        
        sqlStr.append("INNER JOIN turma on turma.codigo = ht.turma ");
        sqlStr.append("INNER JOIN curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada = false and curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal limit 1)) ");
        sqlStr.append("or (turma.turmaAgrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo and t.situacao = 'AB' limit 1)) ) ");
        sqlStr.append("INNER JOIN horarioturmadiaitem h ON h.horarioturmadia = htd.codigo AND h.disciplina = ").append(disciplina).append(" ");
        sqlStr.append("INNER JOIN turmadisciplina t2 ON t2.turma = ht.turma AND t2.disciplina = h.disciplina AND t2.definicoestutoriaonline = 'PROGRAMACAO_DE_AULA' ");
        sqlStr.append("WHERE 1=1 ");
        if (turma != null && !turma.equals(0)) {
            sqlStr.append("AND (ht.turma = ").append(turma).append(" OR (turma.turmaagrupada AND turma.codigo IN (SELECT turmaorigem FROM turmaagrupada WHERE turma = ").append(turma).append("))) ");
        }
        if (disciplina != null && !disciplina.equals(0)) {
            sqlStr.append("AND EXISTS (SELECT horarioturmadiaitem.codigo FROM horarioturmadiaitem ");
            sqlStr.append("WHERE horarioturmadiaitem.horarioturmadia = htd.codigo AND (horarioturmadiaitem.disciplina = ").append(disciplina).append(" ");
            sqlStr.append("OR (turma.turmaagrupada AND horarioturmadiaitem.disciplina IN ( ");
            sqlStr.append("SELECT de.disciplina FROM disciplinaequivalente de WHERE de.equivalente = ").append(disciplina).append(" ");
            sqlStr.append("UNION ");
            sqlStr.append("SELECT de.equivalente  FROM disciplinaequivalente de WHERE de.disciplina = ").append(disciplina).append("))) LIMIT 1) ");
        }
        if (dataVerificacao != null) {
            sqlStr.append(" AND htd.data <= '").append(dataVerificacao).append("' ");
        }
        sqlStr.append(" AND (curso.periodicidade = 'IN') ");
        sqlStr.append("UNION ");
        sqlStr.append("SELECT p.codigo FROM programacaotutoriaonline p ");
        sqlStr.append("INNER JOIN turmadisciplina t2 ON t2.turma = p.turma	AND t2.disciplina = p.disciplina AND t2.definicoestutoriaonline = 'DINAMICA' ");
        sqlStr.append("INNER JOIN turma ON turma.codigo = p.turma ");
        sqlStr.append("LEFT JOIN curso ON ((turma.turmaagrupada = FALSE AND curso.codigo = turma.curso) OR (turma.turmaagrupada = FALSE AND curso.codigo = (SELECT t.curso FROM turma t WHERE t.codigo = turma.turmaprincipal LIMIT 1)) ");
        sqlStr.append("OR (turma.turmaAgrupada AND curso.codigo = (SELECT t.curso FROM turmaagrupada INNER JOIN turma AS t ON t.codigo = turmaagrupada.turma WHERE turmaagrupada.turmaorigem = turma.codigo AND t.situacao = 'AB' LIMIT 1)) ) ");
        sqlStr.append("WHERE definirperiodoaulaonline AND p.situacao = 'ATIVO' ");
        if (turma != null && !turma.equals(0)) {
        	sqlStr.append("AND (p.turma = ").append(turma).append(" OR (turma.turmaagrupada AND turma.codigo IN (SELECT turmaorigem FROM turmaagrupada WHERE turma = "+ turma +" ))) ");
        }
        if (disciplina != null && !disciplina.equals(0)) {
        	sqlStr.append("AND (p.disciplina = ").append(disciplina).append(" OR (turma.turmaagrupada  AND p.disciplina IN ( ");
        	sqlStr.append("SELECT de.disciplina FROM disciplinaequivalente de WHERE de.equivalente = ").append(disciplina).append(" ");
        	sqlStr.append("UNION ");
        	sqlStr.append("SELECT de.equivalente  FROM disciplinaequivalente de WHERE de.disciplina = ").append(disciplina).append("))) ");
        }
        if (dataVerificacao != null) {
        	sqlStr.append("AND p.datainicioaula <= '").append(dataVerificacao +"' ");
        }
        sqlStr.append("AND (curso.periodicidade = 'IN')" );
        sqlStr.append(" LIMIT 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return tabelaResultado.next();
    }

    public Integer consultarNrModuloDisciplina(Integer turma, Integer disciplina) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append("  select * from (");
        sqlStr.append("          select row_number() over(order by min(horarioturmadia.data), horarioturmadiaitem.disciplina ) as qtd, horarioturmadiaitem.disciplina, min(horarioturmadia.data) as data ");
        sqlStr.append("          from horarioturma");
        sqlStr.append("          inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
        sqlStr.append("          inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
        sqlStr.append("          inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo");
        sqlStr.append("          where horarioturma.turma = ").append(turma);
        sqlStr.append("          group by horarioturmadiaitem.disciplina");
        sqlStr.append("          ) as t where t.disciplina = ").append(disciplina);      
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return (tabelaResultado.getInt("qtd"));
        } 
        return 0;        
    }

    public String consultarDataAulaTurmaDisciplina(Integer turma, Integer disciplina) throws Exception {
        StringBuilder sqlStr = new StringBuilder("select horarioturmadia.data from horarioturmadia inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
        sqlStr.append(" inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
        sqlStr.append(" where horarioturma.turma =  ").append(turma).append(" ");
        sqlStr.append(" AND horarioturmadiaitem.disciplina = ").append(disciplina).append(" order by horarioturmadia.data ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        String datasAulaStr = "(";
        while (tabelaResultado.next()) {            
            datasAulaStr += Uteis.getDiaSemana_Apresentar(tabelaResultado.getDate("data")) + " " + Uteis.getData(tabelaResultado.getDate("data")) + ", ";
        }
        datasAulaStr += ")";
        datasAulaStr = datasAulaStr.replace(", )", ")");
        return datasAulaStr;
    }
    
	@Override
	public HorarioTurmaDiaVO montarDadosConsultaCompletaHorarioTurmaDia(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaVO horarioTurmaDiaVO = new HorarioTurmaDiaVO();
		horarioTurmaDiaVO.setCodigo(rs.getInt("horarioturmadia"));
		horarioTurmaDiaVO.setNovoObj(false);
		horarioTurmaDiaVO.setData(rs.getDate("data"));
		horarioTurmaDiaVO.getHorarioTurma().setCodigo(rs.getInt("horarioTurma"));
		horarioTurmaDiaVO.setOcultarDataAula(rs.getBoolean("ocultarDataAula"));
		horarioTurmaDiaVO.getUsuarioResp().setCodigo(rs.getInt("usuarioResp"));
		if (Uteis.isAtributoPreenchido(horarioTurmaDiaVO.getUsuarioResp().getCodigo())) {
			horarioTurmaDiaVO.setUsuarioResp(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(horarioTurmaDiaVO.getUsuarioResp().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}

		return horarioTurmaDiaVO;
	}
	
	@Override
	public HorarioTurmaDiaItemVO montarDadosConsultaCompletaHorarioTurmaDiaItem(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		return this.montarDadosConsultaCompletaHorarioTurmaDiaItem(rs, usuario, null);
	}
	

	@Override
	public HorarioTurmaDiaItemVO montarDadosConsultaCompletaHorarioTurmaDiaItem(SqlRowSet rs, UsuarioVO usuario, FuncionarioCargoVO funcionarioCargoVO) throws Exception {
		HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();
		horarioTurmaDiaItemVO.setNovoObj(false);
		horarioTurmaDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
		horarioTurmaDiaItemVO.getSala().setSala(rs.getString("sala.sala"));
		horarioTurmaDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("localAula.local"));
		horarioTurmaDiaItemVO.getSala().getLocalAula().setCodigo(rs.getInt("localAula.codigo"));
		horarioTurmaDiaItemVO.setData(rs.getDate("data"));
		horarioTurmaDiaItemVO.setCodigo(rs.getInt("horarioturmadiaitem"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().setCodigo(rs.getInt("horarioturmadia"));
		horarioTurmaDiaItemVO.setDuracaoAula(rs.getInt("duracaoaula"));
		horarioTurmaDiaItemVO.setNrAula(rs.getInt("nrAula"));
		horarioTurmaDiaItemVO.setHorario(rs.getString("horarioinicioaula") + " " + UteisJSF.internacionalizar("prt_a") + " " + rs.getString("horariofinalaula"));
		horarioTurmaDiaItemVO.setHorarioInicio(rs.getString("horarioinicioaula"));
		horarioTurmaDiaItemVO.setHorarioTermino(rs.getString("horariofinalaula"));
		horarioTurmaDiaItemVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioTurmaDiaItemVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setCodigo(rs.getInt("professor"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setNome(rs.getString("professor.nome"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setEmail(rs.getString("professor.email"));
		horarioTurmaDiaItemVO.getFuncionarioVO().getFormacaoAcademicaVOs().add(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigoPessoa(rs.getInt("professor"), false, usuario));
		horarioTurmaDiaItemVO.getGoogleMeetVO().setCodigo(rs.getInt("googlemeet.codigo"));
		horarioTurmaDiaItemVO.setPossuiAulaRegistrada(rs.getBoolean("possuiAulaRegistrada"));
		horarioTurmaDiaItemVO.setAulaReposicao(rs.getBoolean("aulareposicao"));
		if (Uteis.isAtributoPreenchido(funcionarioCargoVO)) {
			horarioTurmaDiaItemVO.setFuncionarioCargoVO(funcionarioCargoVO);
		}
		return horarioTurmaDiaItemVO;
	}
    
	public StringBuilder getSqlConsultaCompleta(Integer horarioTurma, Integer turma, String identificadorTurma, String ano, String semestre, Integer professor, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino) {
		return getFacadeFactory().getHorarioTurmaFacade().getSqlConsultaCompleta(horarioTurma, turma, identificadorTurma, ano, semestre, professor, disciplina, dataInicio, dataFim, unidadeEnsino);
	}
	
	 /**
     * Operação responsável por consultar todos os <code>HorarioTurmaDiaVO</code> relacionados a um objeto da classe <code>academico.HorarioTuma</code>.
     * @param horarioTurma  Atributo de <code>academico.HorarioTuma</code> a ser utilizado para localizar os objetos da classe <code>HorarioTurmaDiaVO</code>.
     * @return List  Contendo todos os objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
	@Override
    public List<HorarioTurmaDiaVO> consultarHorarioTurmaDias(Integer horarioTurma, Integer turma, String identificadorTurma, String ano, String semestre, Integer professor, Integer disciplina, Date dataInicio, Date dataFim, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = getSqlConsultaCompleta(horarioTurma, turma, identificadorTurma, ano, semestre, professor, disciplina, dataInicio, dataFim, unidadeEnsino);
        sqlStr.append(getFacadeFactory().getHorarioTurmaFacade().getSqlOrdenarConsultaCompleta());
        return montarDadosConsultaCompleta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), usuario);
    }
	
	@Override
	 public void realizarCarregamentoDados(List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, UsuarioVO usuario) throws Exception {    	
		if (Uteis.isAtributoPreenchido(horarioTurmaDiaVOs)) {
			StringBuilder sqlStr = getFacadeFactory().getHorarioTurmaFacade().getSqlConsultaCompleta(horarioTurmaDiaVOs.get(0).getHorarioTurma().getCodigo(), null, null, null, null, null, null, null, null, null);
			sqlStr.append(" where horarioturmadia in (0");
			for (HorarioTurmaDiaVO htd : horarioTurmaDiaVOs) {
				sqlStr.append(", ").append(htd.getCodigo());
			}
			sqlStr.append(") ");
			sqlStr.append(getFacadeFactory().getHorarioTurmaFacade().getSqlOrdenarConsultaCompleta());
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			Integer horarioTurmaDia = null;
			HorarioTurmaDiaVO horarioTurmaDiaVO = null;
			while (rs.next()) {
				if (horarioTurmaDia == null || !horarioTurmaDia.equals(rs.getInt("horarioturmadia"))) {
					horarioTurmaDia = rs.getInt("horarioturmadia");
					for (HorarioTurmaDiaVO htd : horarioTurmaDiaVOs) {
						if (htd.getCodigo().equals(horarioTurmaDia)) {
							horarioTurmaDiaVO = htd;
							horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().clear();
						}
					}
					horarioTurmaDiaVO.setCodigo(rs.getInt("horarioturmadia"));
					horarioTurmaDiaVO.setNovoObj(false);
					horarioTurmaDiaVO.setData(rs.getDate("data"));
					horarioTurmaDiaVO.getHorarioTurma().setCodigo(rs.getInt("horarioTurma"));
					horarioTurmaDiaVO.setOcultarDataAula(rs.getBoolean("ocultarDataAula"));
				}
				horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().add(montarDadosConsultaCompletaHorarioTurmaDiaItem(rs, usuario));
			}
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAulaProgramadaComBaseFeriado(FeriadoVO feriado, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("  delete from horarioturmadiaitem where codigo in (");
		sql.append("  select distinct horarioturmadiaitem.codigo from horarioturma");
		sql.append("  inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append("  inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sql.append("  inner join turma on horarioturma.turma = turma.codigo");
		sql.append("  inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sql.append("  where ");
		if (!feriado.getRecorrente()) {
			sql.append("  horarioturmadia.data = '").append(Uteis.getDataJDBC(feriado.getData())).append("' ");
		} else {
			sql.append("  to_char(horarioturmadia.data, 'dd/MM') = '").append(Uteis.getData(feriado.getData(), "dd/MM")).append("' ");
		}
		sql.append("  and horarioturmadiaitem.disciplina is not null");
		if (!feriado.getNacional() && Uteis.isAtributoPreenchido(feriado.getCidade().getCodigo())) {
			sql.append("  and unidadeensino.cidade = ").append(feriado.getCidade().getCodigo());
		}
		sql.append("  and (select count(registroaula.codigo) from registroaula where registroaula.turma = horarioturma.turma ");
		sql.append("  and horarioturmadia.data = registroaula.data and registroaula.disciplina = horarioturmadiaitem.disciplina ) = 0");
		sql.append("  ) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
				
		sql = new StringBuilder("");
		sql.append(" delete from horarioprofessordia where codigo in (");
		sql.append(" select distinct horarioprofessordia.codigo from horarioprofessor");
		sql.append(" inner join horarioprofessordia on horarioprofessordia.horarioprofessor = horarioprofessor.codigo");
		sql.append(" where ");
		if (!feriado.getRecorrente()) {
			sql.append("  horarioprofessordia.data = '").append(Uteis.getDataJDBC(feriado.getData())).append("' ");
		} else {
			sql.append("  to_char(horarioprofessordia.data, 'dd/MM') = '").append(Uteis.getData(feriado.getData(), "dd/MM")).append("' ");
		}
		sql.append(" and (select count(horarioprofessordiaitem.codigo) from horarioprofessordiaitem ");
		sql.append(" where horarioprofessordiaitem.horarioprofessordia = horarioprofessordia.codigo ");
		sql.append(" and horarioprofessordiaitem.disciplina is not null ) = 0) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));		
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	
	@Override
	public Date consultarUltimaAulaAlunoPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception{
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" select max(horario.datatermino) as datatermino from matricula");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sql.append(" inner join historico on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sql.append(" inner join periodoauladisciplinaaluno(historico.codigo) as horario on professor_codigo is not null ");
		sql.append(" where matriculaperiodo.codigo = ").append(matriculaPeriodo);
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			return rs.getDate("datatermino");
		}
		return null;
	}
    
}
