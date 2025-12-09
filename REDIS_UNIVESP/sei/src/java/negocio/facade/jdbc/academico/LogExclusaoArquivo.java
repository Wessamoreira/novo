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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.LogExclusaoArquivoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LogExclusaoArquivoInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class LogExclusaoArquivo extends ControleAcesso implements LogExclusaoArquivoInterfaceFacade {

    protected static String idEntidade;

    public LogExclusaoArquivo() throws Exception {
        super();
        setIdEntidade("LogExclusaoArquivo");
    }

    public void realizarRegistroLogExclusaoArquivo(ArquivoVO arquivoVO, String tipoVisao, UsuarioVO usuarioVO) throws Exception {
        LogExclusaoArquivoVO logExclusaoArquivoVO = new LogExclusaoArquivoVO();
        arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorCodigo(arquivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        if(Uteis.isAtributoPreenchido(arquivoVO)){
	        logExclusaoArquivoVO.setCodigoArquivo(arquivoVO.getCodigo());
	        logExclusaoArquivoVO.setNomeArquivo(arquivoVO.getNome());
	        logExclusaoArquivoVO.setDescricaoArquivo(arquivoVO.getDescricao());
	        logExclusaoArquivoVO.setOrigem(arquivoVO.getOrigem());
	        if (!tipoVisao.equals("")) {
	            logExclusaoArquivoVO.setTipoVisao(tipoVisao);
	        } else {
	            if (!usuarioVO.getPerfilAcesso().getCodigo().equals(0)) {
	                logExclusaoArquivoVO.setTipoVisao("Perfil: "+getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(usuarioVO.getPerfilAcesso().getCodigo(), usuarioVO).getNome());
	            }
	        }
	        logExclusaoArquivoVO.getResponsavelExclusao().setCodigo(usuarioVO.getCodigo());
	        logExclusaoArquivoVO.getResponsavelExclusao().setNome(usuarioVO.getNome());
	        logExclusaoArquivoVO.setDisciplinaVO(arquivoVO.getDisciplina());
	        logExclusaoArquivoVO.setTurmaVO(arquivoVO.getTurma());
	        logExclusaoArquivoVO.setProfessor(arquivoVO.getProfessor());
	        logExclusaoArquivoVO.setDataUpload(arquivoVO.getDataUpload());
	        logExclusaoArquivoVO.setDataExclusao(new Date());
	        incluir(logExclusaoArquivoVO);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final LogExclusaoArquivoVO obj) throws Exception {
        final String sql = "INSERT INTO LogExclusaoArquivo( codigoArquivo, nomeArquivo, descricaoArquivo, origem, tipoVisao, responsavelExclusao, disciplina, turma, professor, dataUpload, dataExclusao) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getCodigoArquivo());
                sqlInserir.setString(2, obj.getNomeArquivo());
                sqlInserir.setString(3, obj.getDescricaoArquivo());
                sqlInserir.setString(4, obj.getOrigem());
                sqlInserir.setString(5, obj.getTipoVisao());
                if (obj.getResponsavelExclusao() != null && obj.getResponsavelExclusao().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(6, obj.getResponsavelExclusao().getCodigo());
                } else {
                    sqlInserir.setInt(6, 0);
                }
                if (obj.getDisciplinaVO() != null && obj.getDisciplinaVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(7, obj.getDisciplinaVO().getCodigo());
                } else {
                    sqlInserir.setInt(7, 0);
                }
                if (obj.getTurmaVO() != null && obj.getTurmaVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(8, obj.getTurmaVO().getCodigo());
                } else {
                    sqlInserir.setInt(8, 0);
                }
                if (obj.getProfessor() != null && obj.getProfessor().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(9, obj.getProfessor().getCodigo());
                } else {
                    sqlInserir.setInt(9, 0);
                }
                sqlInserir.setDate(10, Uteis.getDataJDBC(obj.getDataUpload()));
                sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(obj.getDataExclusao()));
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
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public static LogExclusaoArquivoVO montarDados(SqlRowSet dadosSQL) throws Exception {
        LogExclusaoArquivoVO obj = new LogExclusaoArquivoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setCodigoArquivo(new Integer(dadosSQL.getInt("codigoArquivo")));
        obj.setNomeArquivo(dadosSQL.getString("nomeArquivo"));
        obj.setDescricaoArquivo(dadosSQL.getString("descricaoArquivo"));
        obj.setOrigem(dadosSQL.getString("origem"));
        obj.setTipoVisao(dadosSQL.getString("tipoVisao"));
        obj.getResponsavelExclusao().setCodigo(dadosSQL.getInt("responsavelExclusao"));
        obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina"));
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma"));
        obj.getProfessor().setCodigo(dadosSQL.getInt("professor"));
        obj.setDataUpload(dadosSQL.getDate("dataUpload"));
        obj.setDataExclusao(dadosSQL.getDate("dataExclusao"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public LogExclusaoArquivoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM LogExclusaoArquivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado));
    }

    public static List<LogExclusaoArquivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LogExclusaoArquivoVO> vetResultado = new ArrayList<LogExclusaoArquivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		return vetResultado;
	}
    public static String getIdEntidade() {
        return LogFechamento.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        LogFechamento.idEntidade = idEntidade;
    }
    
	@Override
	public void realizarRegistroLogExclusaoArquivoBackup(ArquivoVO arquivoVO, String tipoVisao, UsuarioVO usuarioVO) throws Exception {
        LogExclusaoArquivoVO logExclusaoArquivoVO = new LogExclusaoArquivoVO();
        arquivoVO = getFacadeFactory().getArquivoFacade().consultarBackupPorChavePrimaria(arquivoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        logExclusaoArquivoVO.setCodigoArquivo(arquivoVO.getCodigo());
        logExclusaoArquivoVO.setNomeArquivo(arquivoVO.getNome());
        logExclusaoArquivoVO.setDescricaoArquivo(arquivoVO.getDescricao());
        logExclusaoArquivoVO.setOrigem(arquivoVO.getOrigem());
        if (!tipoVisao.equals("")) {
            logExclusaoArquivoVO.setTipoVisao(tipoVisao);
        } else {
            if (!usuarioVO.getPerfilAcesso().getCodigo().equals(0)) {
                logExclusaoArquivoVO.setTipoVisao("Perfil: "+getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(usuarioVO.getPerfilAcesso().getCodigo(), usuarioVO).getNome());
            }
        }
        logExclusaoArquivoVO.setResponsavelExclusao(usuarioVO);
        logExclusaoArquivoVO.setDisciplinaVO(arquivoVO.getDisciplina());
        logExclusaoArquivoVO.setTurmaVO(arquivoVO.getTurma());
        logExclusaoArquivoVO.setProfessor(arquivoVO.getProfessor());
        logExclusaoArquivoVO.setDataUpload(arquivoVO.getDataUpload());
        logExclusaoArquivoVO.setDataExclusao(new Date());
        incluir(logExclusaoArquivoVO);
	}
}
