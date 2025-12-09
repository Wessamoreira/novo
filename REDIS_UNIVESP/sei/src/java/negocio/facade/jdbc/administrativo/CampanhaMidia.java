package negocio.facade.jdbc.administrativo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.administrativo.CampanhaMidiaVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.CampanhaMidiaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class CampanhaMidia extends ControleAcesso implements CampanhaMidiaInterfaceFacade {

    protected static String idEntidade;

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CampanhaMidiaVO obj) throws Exception {
        try {
            final String sql = "INSERT INTO CampanhaMidia (tipoMidia, impactoEsperado, dataInicioVinculacao, dataFimVinculacao, apresentarPreInscricao, campanha) VALUES (?,?,?,?,?,?) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getTipoMidia().getCodigo() != 0) {
                        sqlInserir.setInt(1, obj.getTipoMidia().getCodigo());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getImpactoEsperado());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicioVinculacao()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFimVinculacao()));
                    sqlInserir.setBoolean(5, obj.getApresentarPreInscricao());
                    sqlInserir.setInt(6, obj.getCampanha().getCodigo());
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
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CampanhaMidiaVO obj) throws Exception {
        try {
            final String sql = "UPDATE CampanhaMidia set tipoMidia=?, impactoEsperado=?, dataInicioVinculacao=?, dataFimVinculacao=?, apresentarPreInscricao=?, campanha=? where codigo = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getTipoMidia().getCodigo() != 0) {
                        sqlInserir.setInt(1, obj.getTipoMidia().getCodigo());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getImpactoEsperado());
                    sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataInicioVinculacao()));
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataFimVinculacao()));
                    sqlInserir.setBoolean(5, obj.getApresentarPreInscricao());
                    sqlInserir.setInt(6, obj.getCampanha().getCodigo());
                    sqlInserir.setInt(7, obj.getCodigo());
                    return sqlInserir;
                }});
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCampanhaMidia(Integer campanha, List objetos) throws Exception {
        String str = "DELETE FROM CampanhaMidia WHERE campanha = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            CampanhaMidiaVO objeto = (CampanhaMidiaVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str, new Object[]{campanha});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaMidiaVO objeto = (CampanhaMidiaVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getCampanha().setCodigo(campanha);
                incluir(objeto);
            } else {
                alterar(objeto);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirCampanhaMidia(Integer CampanhaMidia, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            CampanhaMidiaVO obj = (CampanhaMidiaVO) e.next();
            obj.getCampanha().setCodigo(CampanhaMidia);
            incluir(obj);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCampanhaMidia(Integer codigo) throws Exception {
        try {
            CampanhaMidia.excluir(getIdEntidade());
            String sql = "DELETE FROM CampanhaMidia WHERE ((Campanha= ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    public void adicionarObjCampanhaMidiaVOs(CampanhaVO campanhaVO, CampanhaMidiaVO obj) throws Exception {
        obj.setCampanha(campanhaVO);
        validarDadosCampanhaMidia(obj);
        obj.setTipoMidia(getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorChavePrimaria(obj.getTipoMidia().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
        int index = 0;
        for (CampanhaMidiaVO objExistente : campanhaVO.getListaCampanhaMidia()) {
            if (objExistente.getTipoMidia().getCodigo().equals(obj.getTipoMidia().getCodigo())) {
                campanhaVO.getListaCampanhaMidia().set(index, obj);
                return;
            }
            index++;
        }
        campanhaVO.getListaCampanhaMidia().add(obj);
    }

    public void excluirObjCampanhaMidiaVOs(CampanhaVO campanhaVO, CampanhaMidiaVO obj) throws Exception {
        int index = 0;
        for (CampanhaMidiaVO objExistente : campanhaVO.getListaCampanhaMidia()) {
            if (objExistente.getTipoMidia().getNomeMidia().equals(obj.getTipoMidia().getNomeMidia())) {
                campanhaVO.getListaCampanhaMidia().remove(index);
                return;
            }
            index++;
        }
    }

    public void validarDadosCampanhaMidia(CampanhaMidiaVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getTipoMidia().getCodigo() == 0) {
            throw new Exception("O campo TIPO MIDIA (Mídias) deve ser informado.");
        }
        if (obj.getImpactoEsperado() == 0) {
            throw new Exception("O campo IMPACTO ESPERADO (Mídias) deve ser informado e desigual a 0.");
        }
    }

    public static String getIdEntidade() {
        return CampanhaMidia.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CampanhaMidia.idEntidade = idEntidade;
    }
    
    public List<CampanhaMidiaVO> consultarCampanhaMidiaPorCampanha(Integer campanha, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select campanhamidia.codigo AS \"campanhamidia.codigo\", campanhamidia.impactoEsperado AS \"campanhamidia.impactoEsperado\", campanhamidia.datainiciovinculacao AS \"campanhamidia.datainiciovinculacao\", ");
    	sb.append(" campanhamidia.datafimvinculacao AS \"campanhamidia.datafimvinculacao\", campanhamidia.apresentarpreinscricao AS \"campanhamidia.apresentarpreinscricao\", campanhamidia.campanha AS \"campanhamidia.campanha\",  ");
    	sb.append(" tipoMidiaCaptacao.nomeMidia AS \"tipoMidiaCaptacao.nomeMidia\", tipoMidiaCaptacao.codigo AS \"tipoMidiaCaptacao.codigo\" ");
    	sb.append(" from campanhamidia  ");
    	sb.append(" LEFT JOIN tipoMidiaCaptacao ON tipoMidiaCaptacao.codigo = campanhamidia.tipomidia   ");
    	sb.append(" where campanhamidia.campanha = ").append(campanha);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	List<CampanhaMidiaVO> campanhaMidiaVOs = new ArrayList<CampanhaMidiaVO>(0);
    	while (tabelaResultado.next()) {
    		CampanhaMidiaVO obj = new CampanhaMidiaVO();
    		obj.setCodigo(tabelaResultado.getInt("campanhamidia.codigo"));
    		obj.setImpactoEsperado(tabelaResultado.getInt("campanhamidia.impactoEsperado"));
    		obj.setDataInicioVinculacao(tabelaResultado.getDate("campanhamidia.datainiciovinculacao"));
    		obj.setDataFimVinculacao(tabelaResultado.getDate("campanhamidia.datafimvinculacao"));
    		obj.setApresentarPreInscricao(tabelaResultado.getBoolean("campanhamidia.apresentarpreinscricao"));
    		obj.getCampanha().setCodigo(tabelaResultado.getInt("campanhamidia.campanha"));
    		
    		obj.getTipoMidia().setNomeMidia(tabelaResultado.getString("tipoMidiaCaptacao.nomeMidia"));
    		obj.getTipoMidia().setCodigo(tabelaResultado.getInt("tipoMidiaCaptacao.codigo"));
    		
			campanhaMidiaVOs.add(obj);
		}
    	return campanhaMidiaVOs;
    }
}
