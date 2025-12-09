package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ArquivoLogVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ArquivoLogInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class ArquivoLog extends ControleAcesso implements ArquivoLogInterfaceFacade {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void inicializarDadosLogInclusaoArquivo(ArquivoVO arquivo, ArquivoLogVO arquivoLogVO, UsuarioVO responsavelUpload, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        arquivoLogVO.setArquivo(arquivo);
        arquivoLogVO.setNomeArquivo(arquivo.getNome());
        arquivoLogVO.setResponsavelUpload(responsavelUpload);
        arquivoLogVO.setDataUpload(new Date());
        arquivoLogVO.setArquivoGravadoDisco(getFacadeFactory().getArquivoHelper().executarVerificacaoExistenciaArquivoDisco(arquivo, PastaBaseArquivoEnum.ARQUIVO.getValue(), configuracaoGeralSistemaVO));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ArquivoLogVO obj) throws Exception {
        final String sql = "INSERT INTO ArquivoLog( responsavelUpload, arquivo, nomeArquivo, dataUpload, arquivoGravadoDisco ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getResponsavelUpload().getCodigo() != 0) {
                    sqlInserir.setInt(1, obj.getResponsavelUpload().getCodigo());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getArquivo().getCodigo() != 0) {
                    sqlInserir.setInt(2, obj.getArquivo().getCodigo());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                sqlInserir.setString(3, obj.getNomeArquivo());
                sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataUpload()));
                sqlInserir.setBoolean(5, obj.getArquivoGravadoDisco());

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
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = {Throwable.class}, propagation = Propagation.REQUIRED)
    public void alterarOrigemUpload(Integer arquivo, String origem, UsuarioVO usuario) throws Exception {
        try {
            String sql = "UPDATE arquivoLog set origem=? WHERE (arquivo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{origem, arquivo});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
}
