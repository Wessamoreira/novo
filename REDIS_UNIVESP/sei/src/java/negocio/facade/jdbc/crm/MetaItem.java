/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.MetaItemVO;
import negocio.comuns.crm.MetaVO;
import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.MetaItemInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MetaItem extends ControleAcesso implements MetaItemInterfaceFacade {

    protected static String idEntidade;

    public void incluirMetaItens(Integer meta, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MetaItemVO obj = (MetaItemVO) e.next();
            obj.getMeta().setCodigo(meta);
            incluirMetaItem(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirMetaItem(final MetaItemVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO metaitem (quantidadecontatos, quantidadefinalizadosucesso, quantidadecaptacaoprospect, nivelexperiencia, padrao, meta) VALUES (?,?,?,?,?,?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setLong(1, obj.getQuantidadeContatos());
                    sqlInserir.setLong(2, obj.getQuantidadeFinalizadoSucesso());
                    sqlInserir.setLong(3, obj.getQuantidadeMetaCaptacaoProspect());
                    sqlInserir.setString(4, obj.getNivelExperiencia().toString());
                    sqlInserir.setBoolean(5, obj.getPadrao());
                    sqlInserir.setInt(6, obj.getMeta().getCodigo());
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
    public void alterarMetaItem(final MetaItemVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE metaitem SET quantidadecontatos = ?, quantidadefinalizadosucesso = ?, quantidadecaptacaoprospect = ?, nivelexperiencia = ?, padrao = ?, meta = ? WHERE codigo = ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setLong(1, obj.getQuantidadeContatos());
                    sqlAlterar.setLong(2, obj.getQuantidadeFinalizadoSucesso());
                    sqlAlterar.setLong(3, obj.getQuantidadeMetaCaptacaoProspect());
                    sqlAlterar.setString(4, obj.getNivelExperiencia().toString());
                    sqlAlterar.setBoolean(5, obj.getPadrao());
                    sqlAlterar.setInt(6, obj.getMeta().getCodigo());
                    sqlAlterar.setInt(7, obj.getCodigo());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirMetaItens(Integer codigo, UsuarioVO usuario) throws Exception {
        try {
            Meta.excluir(getIdEntidade());
            String sql = "DELETE FROM metaitem WHERE ((meta = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    public void alterarMetaItens(Integer meta, List objetos, UsuarioVO usuario) throws Exception {
        String str = "DELETE FROM MetaItem WHERE Meta = ?";
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            MetaItemVO objeto = (MetaItemVO) i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        str += adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(str, new Object[]{meta});
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MetaItemVO objeto = (MetaItemVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                incluirMetaItem(objeto, usuario);
            } else {
                alterarMetaItem(objeto, usuario);
            }
        }
    }

    public void adicionarObjMetaItemVOs(MetaVO metaVO, MetaItemVO obj) throws Exception {
        obj.setMeta(metaVO);
        validarDadosMetaItem(obj);
        int index = 0;
        for (MetaItemVO objExistente : metaVO.getListaMetaItem()) {
            if (objExistente.getNivelExperiencia().equals(obj.getNivelExperiencia())) {
                metaVO.getListaMetaItem().set(index, obj);
                return;
            }
            index++;
        }
        metaVO.getListaMetaItem().add(obj);
    }

    public void excluirObjMetaItemVOs(MetaVO metaVO, MetaItemVO metaItemVO) throws Exception {
        int index = 0;
        for (MetaItemVO objExistente : metaVO.getListaMetaItem()) {
            if (objExistente.getNivelExperiencia().equals(metaItemVO.getNivelExperiencia())) {
                metaVO.getListaMetaItem().remove(index);
                return;
            }
            index++;
        }
    }

    public void validarDadosMetaItem(MetaItemVO obj) throws Exception {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getNivelExperiencia() == obj.getNivelExperiencia().NENHUM)) {
            throw new Exception("O campo NÍVEL EXPERIÊNCIA CARGO deve ser informado.");
        }
        if (obj.getQuantidadeMetaCaptacaoProspect() == 0) {
            throw new Exception("O campo META PARA CAPTAÇÃO DE PROSPECTS deve ser informado e desigual a 0.");
        }
        if (obj.getQuantidadeFinalizadoSucesso() == 0) {
            throw new Exception("O campo FINALIZAÇÃO COM SUCESSO deve ser informado e desigual a 0.");
        }
        if (obj.getQuantidadeContatos() == 0) {
            throw new Exception("O campo QUANTIDADE DE CONTATOS deve ser informado e desigual a 0.");
        }
    }

    public List montarListaMetaItem(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT metaitem.* FROM metaitem WHERE meta = " + codigo;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            MetaItemVO obj = new MetaItemVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public Boolean verificarUnicidadePadrao(List objetos) {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            MetaItemVO obj = (MetaItemVO) e.next();
            if (obj.getPadrao()) {
                return false;
            }
        }
        return true;
    }

    public static MetaItemVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        MetaItemVO obj = new MetaItemVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getMeta().setCodigo(dadosSQL.getInt("meta"));
        obj.setQuantidadeContatos(dadosSQL.getLong("quantidadeContatos"));
        obj.setQuantidadeFinalizadoSucesso(dadosSQL.getLong("quantidadeFinalizadoSucesso"));
        obj.setQuantidadeMetaCaptacaoProspect(dadosSQL.getLong("quantidadeCaptacaoProspect"));
        obj.setNivelExperiencia(NivelExperienciaCargoEnum.valueOf(dadosSQL.getString("nivelExperiencia")));
        obj.setPadrao(dadosSQL.getBoolean("padrao"));
        obj.setNovoObj(Boolean.FALSE);
        //    montarDadosMeta(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        return obj;
    }

    public static String getIdEntidade() {
        return Meta.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Meta.idEntidade = idEntidade;
    }
}
