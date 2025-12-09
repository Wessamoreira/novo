package negocio.facade.jdbc.ead;

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

import negocio.comuns.ead.DuvidaProfessorInteracaoVO;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.DuvidaProfessorInteracaoInterfaceFacade;

@Repository
@Lazy
public class DuvidaProfessorInteracao extends ControleAcesso implements DuvidaProfessorInteracaoInterfaceFacade {

    @Override
    public void incluir(final DuvidaProfessorInteracaoVO duvidaProfessorInteracaoVO) throws Exception {
        try {
            validarDados(duvidaProfessorInteracaoVO);
            final StringBuilder sql = new StringBuilder("INSERT INTO DuvidaProfessorInteracao ");
            sql.append(" ( dataInteracao, interacao,  pessoa,  tipoPessoaInteracaoDuvidaProfessor, duvidaProfessor)  ");            
            sql.append(" VALUES ( ?, ?, ?, ?, ?) returning codigo");
            duvidaProfessorInteracaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(duvidaProfessorInteracaoVO.getDataInteracao()));
                    sqlInserir.setString(x++, duvidaProfessorInteracaoVO.getInteracao());
                    sqlInserir.setInt(x++, duvidaProfessorInteracaoVO.getPessoa().getCodigo());
                    sqlInserir.setString(x++, duvidaProfessorInteracaoVO.getTipoPessoaInteracaoDuvidaProfessor().name());
                    sqlInserir.setInt(x++, duvidaProfessorInteracaoVO.getDuvidaProfessor().getCodigo());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        duvidaProfessorInteracaoVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            duvidaProfessorInteracaoVO.setNovoObj(true);
            throw e;
        }

    }
    
    private void validarDados(DuvidaProfessorInteracaoVO duvidaProfessorInteracaoVO) throws Exception{
        ConsistirException ex = new ConsistirException();
        if (duvidaProfessorInteracaoVO.getInteracao().trim().isEmpty() 
           || Uteis.retiraTags(duvidaProfessorInteracaoVO.getInteracao()).trim().isEmpty()
           || Uteis.retiraTags(duvidaProfessorInteracaoVO.getInteracao()).trim().equals(UteisJSF.internacionalizar("msg_DuvidaProfessor_envieUmaNovaInteracao"))) {
             throw new Exception(UteisJSF.internacionalizar("msg_DuvidaProfessorInteracao_interacao"));
        }
        if (!ex.getListaMensagemErro().isEmpty()) {
            throw ex;
        }
    }

    @Override
    public List<DuvidaProfessorInteracaoVO> consultarPorDuvidaProfessor(Integer duvidaProfessor) {
        try{
	        StringBuilder sb = new StringBuilder("");
	        sb.append(" select DuvidaProfessorInteracao.*, ");
	        sb.append(" pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\", arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\",");
	        sb.append(" arquivo.nome AS \"arquivo.nome\", arquivo.cpfrequerimento AS \"arquivo.cpfrequerimento\" ");
	        sb.append(" from DuvidaProfessorInteracao");
	        sb.append(" inner join pessoa on pessoa.codigo = DuvidaProfessorInteracao.pessoa");
	        sb.append(" left join Arquivo on Arquivo.codigo = pessoa.arquivoImagem");
	        sb.append(" where duvidaProfessor = ").append(duvidaProfessor);
	        sb.append(" order by dataInteracao desc");
	        
	        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
        }catch (Exception e) {
        	e.printStackTrace();
            return new ArrayList<DuvidaProfessorInteracaoVO>(0);
        }
    }
    
    private List<DuvidaProfessorInteracaoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
        List<DuvidaProfessorInteracaoVO> duvidaProfessorInteracaoVOs = new ArrayList<DuvidaProfessorInteracaoVO>(0);
        while(rs.next()){
            duvidaProfessorInteracaoVOs.add(montarDados(rs));
        }
        return duvidaProfessorInteracaoVOs;
    }
    
    private DuvidaProfessorInteracaoVO montarDados(SqlRowSet rs) throws Exception{
        DuvidaProfessorInteracaoVO obj = new DuvidaProfessorInteracaoVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setDataInteracao(rs.getTimestamp("dataInteracao"));
        obj.getDuvidaProfessor().setCodigo(rs.getInt("duvidaProfessor"));
        obj.setInteracao(rs.getString("interacao"));
        obj.setTipoPessoaInteracaoDuvidaProfessor(TipoPessoaInteracaoDuvidaProfessorEnum.valueOf(rs.getString("tipoPessoaInteracaoDuvidaProfessor")));        
        obj.getPessoa().setCodigo(rs.getInt("pessoa"));
        obj.getPessoa().setNome(rs.getString("pessoa.nome"));
        obj.getPessoa().setCPF(rs.getString("pessoa.cpf"));
        if (rs.getString("arquivo.pastaBaseArquivo") != null) {
            obj.getPessoa().getArquivoImagem().setNome(rs.getString("arquivo.nome"));            
            obj.getPessoa().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo"));
        }
        return obj;
    }

}
