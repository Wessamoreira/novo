package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoAcademicoNotaConceitoInterfaceFacade;

@Repository
@Lazy
public class ConfiguracaoAcademicoNotaConceito extends ControleAcesso implements ConfiguracaoAcademicoNotaConceitoInterfaceFacade {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void incluirConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {    	
    	for(int x=1; x<= 40; x++){
        	List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs = (List<ConfiguracaoAcademicoNotaConceitoVO>)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicoNota"+x+"ConceitoVOs");
        	if((Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota"+x+"PorConceito")){
        		excluirConfiguracaoAcademicoNotaConceito(configuracaoAcademicoVO, configuracaoAcademicoNotaConceitoVOs, TipoNotaConceitoEnum.getTipoNota(x));
        		 for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
                     obj.setConfiguracaoAcademico(configuracaoAcademicoVO.getCodigo());                    
                     incluir(obj);                     
                 }
        	}
    	}        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void incluir(final ConfiguracaoAcademicoNotaConceitoVO obj) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO ConfiguracaoAcademicoNotaConceito ");
            sql.append(" (configuracaoAcademico, tipoNotaConceito, conceitoNota, abreviaturaConceitoNota, faixaNota1, faixaNota2,situacao) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo ");
            obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, obj.getConfiguracaoAcademico());
                    sqlInserir.setString(x++, obj.getTipoNotaConceito().name());
                    sqlInserir.setString(x++, obj.getConceitoNota());
                    sqlInserir.setString(x++, obj.getAbreviaturaConceitoNota());
                    sqlInserir.setDouble(x++, obj.getFaixaNota1());
                    sqlInserir.setDouble(x++, obj.getFaixaNota2());
                    sqlInserir.setString(x++, obj.getSituacao());
                    return sqlInserir;
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
            obj.setNovoObj(false);
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void alterar(final ConfiguracaoAcademicoNotaConceitoVO obj) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE ConfiguracaoAcademicoNotaConceito ");
            sql.append(" set configuracaoAcademico=?, tipoNotaConceito =?, conceitoNota =?, abreviaturaConceitoNota =?, faixaNota1 =?, faixaNota2 =?, situacao = ? ");
            sql.append(" WHERE codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setInt(x++, obj.getConfiguracaoAcademico());
                    sqlInserir.setString(x++, obj.getTipoNotaConceito().name());
                    sqlInserir.setString(x++, obj.getConceitoNota());
                    sqlInserir.setString(x++, obj.getAbreviaturaConceitoNota());
                    sqlInserir.setDouble(x++, obj.getFaixaNota1());
                    sqlInserir.setDouble(x++, obj.getFaixaNota2());
                    sqlInserir.setString(x++, obj.getSituacao());
                    sqlInserir.setInt(x++, obj.getCodigo());
                    return sqlInserir;
                }
            }) == 0) {
                incluir(obj);
                return;
            }
            obj.setNovoObj(false);
        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void excluirConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO, List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs, TipoNotaConceitoEnum tipoNotaConceito) {
        StringBuilder sb = new StringBuilder("DELETE FROM ConfiguracaoAcademicoNotaConceito where configuracaoAcademico =  ").append(configuracaoAcademicoVO.getCodigo());
        sb.append(" and tipoNotaConceito = '").append(tipoNotaConceito.name()).append("' ");
        sb.append(" and codigo not in ( 0 ");        
        for (ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO : configuracaoAcademicoNotaConceitoVOs) {
            if (!configuracaoAcademicoNotaConceitoVO.isNovoObj()) {
                sb.append(", ").append(configuracaoAcademicoNotaConceitoVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void alterarConfiguracaoAcademicoNotaConceito(ConfiguracaoAcademicoVO configuracaoAcademicoVO) throws Exception {
    	
    	for(int x=1; x<= 40; x++){
        	List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs = (List<ConfiguracaoAcademicoNotaConceitoVO>)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "configuracaoAcademicoNota"+x+"ConceitoVOs");
        	if((Boolean)UtilReflexao.invocarMetodoGet(configuracaoAcademicoVO, "utilizarNota"+x+"PorConceito")){
        		excluirConfiguracaoAcademicoNotaConceito(configuracaoAcademicoVO, configuracaoAcademicoNotaConceitoVOs, TipoNotaConceitoEnum.getTipoNota(x));
        		 for (ConfiguracaoAcademicoNotaConceitoVO obj : configuracaoAcademicoNotaConceitoVOs) {
                     obj.setConfiguracaoAcademico(configuracaoAcademicoVO.getCodigo());
                     if (obj.getNovoObj()) {
                         incluir(obj);
                     } else {
                         alterar(obj);
                     }
                 }
        	}else{
        		configuracaoAcademicoNotaConceitoVOs.clear();
        		excluirConfiguracaoAcademicoNotaConceito(configuracaoAcademicoVO, configuracaoAcademicoNotaConceitoVOs, TipoNotaConceitoEnum.getTipoNota(x));
        	}
    	}

    }

    @Override
    public void consultarPorConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ConfiguracaoAcademicoNotaConceito where configuracaoAcademico =  ");
        sql.append(configuracaoAcademicoVO.getCodigo()).append(" order by tipoNotaConceito, faixaNota2 desc ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (rs.next()) {
            ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO = montarDados(rs);
            switch (configuracaoAcademicoNotaConceitoVO.getTipoNotaConceito()) {
                case NOTA_1:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota1ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_2:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota2ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_3:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota3ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_4:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota4ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_5:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota5ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_6:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota6ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_7:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota7ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_8:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota8ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_9:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota9ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_10:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota10ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_11:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota11ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_12:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota12ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_13:
                    configuracaoAcademicoVO.getConfiguracaoAcademicoNota13ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_14:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota14ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                    break;
                case NOTA_15:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota15ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_16:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota16ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_17:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota17ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_18:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota18ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_19:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota19ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_20:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota20ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_21:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota21ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_22:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota22ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_23:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota23ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_24:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota24ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_25:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota25ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_26:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota26ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_27:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota27ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_28:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota28ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_29:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota29ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_30:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota30ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_31:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota31ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_32:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota32ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_33:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota33ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_34:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota34ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_35:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota35ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_36:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota36ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_37:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota37ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_38:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota38ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_39:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota39ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                case NOTA_40:
                	configuracaoAcademicoVO.getConfiguracaoAcademicoNota40ConceitoVOs().add(configuracaoAcademicoNotaConceitoVO);
                	break;
                default:
                    break;
            }
        }
    }

    private ConfiguracaoAcademicoNotaConceitoVO montarDados(SqlRowSet rs) {
        ConfiguracaoAcademicoNotaConceitoVO obj = new ConfiguracaoAcademicoNotaConceitoVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setConfiguracaoAcademico(rs.getInt("configuracaoAcademico"));
        obj.setTipoNotaConceito(TipoNotaConceitoEnum.valueOf(rs.getString("tipoNotaConceito")));
        obj.setAbreviaturaConceitoNota(rs.getString("abreviaturaConceitoNota"));
        obj.setSituacao(rs.getString("situacao"));
        obj.setConceitoNota(rs.getString("conceitoNota"));
        obj.setFaixaNota1(rs.getDouble("faixaNota1"));
        obj.setFaixaNota2(rs.getDouble("faixaNota2"));

        return obj;
    }

    @Override
    public void validarDados(ConfiguracaoAcademicoNotaConceitoVO obj, boolean mediaFinal) throws ConsistirException {
        if (!obj.isValidarDados()) {
            return;
        }
        ConsistirException consistirException = new ConsistirException();
        if (obj.getConceitoNota().trim().isEmpty()) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_conceitoNota"));
        }
        if (obj.getAbreviaturaConceitoNota().trim().isEmpty()) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_abreviaturaConceitoNota"));
        }
        if (obj.getFaixaNota1() == null) {
            if (mediaFinal) {
                consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNota1Media"));
            } else {
                consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNota1"));
            }
        }
        if (mediaFinal && obj.getFaixaNota2() == null) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNota2Media"));

        }

        if (mediaFinal && obj.getFaixaNota1() > obj.getFaixaNota2()) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_ConfiguracaoAcademicoNotaConceito_faixaNota1MaiorNota2"));
        }

    }

    @Override
    public ConfiguracaoAcademicoNotaConceitoVO consultarPorChavePrimaria(Integer codigo) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ConfiguracaoAcademicoNotaConceito WHERE codigo = ");
        sql.append(codigo);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(rs);
        }
        return null;
    }
    
    @Override
    public List<ConfiguracaoAcademicoNotaConceitoVO> consultarPorConfiguracaoAcademicoTipoNota(Integer conf, TipoNotaConceitoEnum tipoNota){
    	StringBuilder sql  = new StringBuilder("select configuracaoacademiconotaconceito.* from configuracaoacademiconotaconceito ");
    	sql.append(" where configuracaoacademico =? and tipoNotaConceito = ? order by conceitoNota ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), conf, tipoNota.name());
    	List<ConfiguracaoAcademicoNotaConceitoVO> configuracaoAcademicoNotaConceitoVOs = new ArrayList<ConfiguracaoAcademicoNotaConceitoVO>(0);    	
    	while(rs.next()){
    		configuracaoAcademicoNotaConceitoVOs.add(montarDados(rs));
    	}
    	return configuracaoAcademicoNotaConceitoVOs;
    }

}
