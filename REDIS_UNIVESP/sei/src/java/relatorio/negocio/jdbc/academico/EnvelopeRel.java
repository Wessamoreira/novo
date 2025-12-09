/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.EnvelopeRelVO;
import relatorio.negocio.interfaces.academico.EnvelopeRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class EnvelopeRel extends SuperRelatorio implements EnvelopeRelInterfaceFacade {

    public EnvelopeRel() {
    }

    public void validarDados() {
    }

    public void validarDadosConsultaAlunoUnidadeEnsino(Integer unidadeEnsino) throws Exception {
        if (unidadeEnsino == null || unidadeEnsino == 0) {
            throw new Exception("O campo UNIDADE DE ENSINO deve ser informado para a consulta da Matrícula.");
        }
    }

    public void validarDadosConsultaAluno(MatriculaVO matriculaVO) throws Exception {
        if (matriculaVO == null || matriculaVO.getMatricula().equals("")) {
            throw new Exception("Aluno de matrícula " + matriculaVO.getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
        }
    }

    public List<EnvelopeRelVO> executarConsultaParametrizada(String matricula, Integer turma, Integer curso, String tipoRelatorio, Boolean apenasAlunoAtivo, UsuarioVO usuarioVO) throws Exception {
        if (tipoRelatorio.equals("aluno")) {
            return consultarDocumentosMatricula(matricula, turma, usuarioVO);
        } else {
            return consultarDocumentosTurma(turma, curso, apenasAlunoAtivo, usuarioVO);
        }
        
    }

    public List<EnvelopeRelVO> consultarDocumentosMatricula(String matricula, Integer turma, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select pessoa.nome, pessoa.endereco, pessoa.cep, pessoa.complemento, pessoa.numero, pessoa.setor, cidade.nome AS \"nomeCidade\", estado.nome AS \"nomeEstado\", tipodocumento.nome AS \"nomeDocumento\", documetacaomatricula.matricula, documetacaomatricula.entregue, cidade.nome AS \"cidade\", chancela.instituicaochanceladora ");
        sqlStr.append(" from documetacaomatricula  ");
        sqlStr.append(" inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento ");
        sqlStr.append(" inner join matricula on documetacaomatricula.matricula =  matricula.matricula ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" inner join cidade on cidade.codigo = pessoa.cidade ");
        sqlStr.append(" inner join estado on estado.codigo = cidade.estado ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" left join chancela on chancela.codigo = turma.chancela ");
        sqlStr.append(" where documetacaomatricula.matricula = '");
        sqlStr.append(matricula);
        sqlStr.append("' and turma.codigo = ");
        sqlStr.append(turma);
        sqlStr.append(" order by tipodocumento.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuarioVO));
    }

    public List<EnvelopeRelVO> consultarDocumentosTurma(Integer turma, Integer curso, Boolean apenasAlunoAtivo, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select pessoa.nome, pessoa.endereco, pessoa.cep, pessoa.complemento, pessoa.numero,  pessoa.setor, cidade.nome AS \"nomeCidade\", estado.nome AS \"nomeEstado\", tipodocumento.nome AS \"nomeDocumento\", documetacaomatricula.matricula, documetacaomatricula.entregue, cidade.nome AS \"cidade\", chancela.instituicaochanceladora ");
        sqlStr.append(" from documetacaomatricula  ");
        sqlStr.append(" inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento ");
        sqlStr.append(" inner join matricula on documetacaomatricula.matricula =  matricula.matricula ");
        sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sqlStr.append(" inner join cidade on cidade.codigo = pessoa.cidade ");
        sqlStr.append(" inner join estado on estado.codigo = cidade.estado ");
        sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
        sqlStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
        sqlStr.append(" left join chancela on chancela.codigo = turma.chancela ");
        sqlStr.append(" where turma.codigo = ");
        sqlStr.append(turma);
        sqlStr.append(" and matricula.curso = ");
        sqlStr.append(curso);
        if (apenasAlunoAtivo) {
            sqlStr.append(" and matricula.situacao = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'AT'");
        }
        sqlStr.append(" order by pessoa.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, usuarioVO));
    }

    public static List<EnvelopeRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<EnvelopeRelVO> vetResultado = new ArrayList<EnvelopeRelVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    public List<EnvelopeRelVO> montarDadosEnvelopeRequerimento(UnidadeEnsinoVO unidadeEnsinoVO, RequerimentoVO requerimentoVO) throws Exception{
        List<EnvelopeRelVO> lista = new ArrayList<EnvelopeRelVO>(0);
        EnvelopeRelVO envelopeRelVO = new EnvelopeRelVO();
        envelopeRelVO.setInstituicaoChanceladora(requerimentoVO.getMatricula().getUnidadeEnsino().getRazaoSocial());
        envelopeRelVO.setNome(requerimentoVO.getMatricula().getAluno().getNome());
        envelopeRelVO.setEndereco(requerimentoVO.getEndereco()+" "+requerimentoVO.getComplemento()+" "+requerimentoVO.getNumero()+"   "+requerimentoVO.getSetor()+"   "+requerimentoVO.getCidade().getNome()+" "+requerimentoVO.getCidade().getEstado().getNome());
        envelopeRelVO.setCep(requerimentoVO.getCEP());
        lista.add(envelopeRelVO);
        return lista;
    }


    public static EnvelopeRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        EnvelopeRelVO obj = new EnvelopeRelVO();
        obj.setMatricula(dadosSQL.getString("matricula"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCidade(dadosSQL.getString("cidade"));
        obj.setInstituicaoChanceladora(dadosSQL.getString("instituicaochanceladora"));
        obj.setNomeDocumento(dadosSQL.getString("nomeDocumento"));
        obj.setEntregue(dadosSQL.getBoolean("entregue"));
        obj.setEndereco(dadosSQL.getString("endereco")+" "+dadosSQL.getString("complemento")+" "+dadosSQL.getString("numero")+"   "+dadosSQL.getString("setor")+"   "+dadosSQL.getString("nomeCidade")+"-"+dadosSQL.getString("nomeEstado"));
        obj.setCep(dadosSQL.getString("cep"));
        return obj;
    }
    
     public static String getDesignIReportRelatorio() {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

     public static String getDesignIReportRelatorioEnvelopeRequerimento() {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "EnvelopeRequerimentoRel" + ".jrxml");
    }


     public static String getCaminhoBaseDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("EnvelopeRel");
    }

    
    
}
