package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.MapaFinanceiroAlunosRelVO;
import relatorio.negocio.comuns.financeiro.MapaFinanceiroAlunosRel_ParcelasVO;
import relatorio.negocio.interfaces.financeiro.MapaFinanceiroAlunosRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class MapaFinanceiroAlunosRel extends SuperRelatorio implements MapaFinanceiroAlunosRelInterfaceFacade {

    public MapaFinanceiroAlunosRel() {

    }

    @Override
    public void validarDados(Integer unidadeEnsino, Integer curso, String tipoRelatorio, String ano, String semestre, CursoVO cursoVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) throws ConsistirException {
		if (unidadeEnsino == null || unidadeEnsino.equals(0)) {
			throw new ConsistirException("A UNIDADE DE ENSINO deve ser informada para a geração do relatório.");
		}
		if (curso == null || curso.equals(0)) {
			throw new ConsistirException("O CURSO deve ser informada para a geração do relatório.");
		}
		if (tipoRelatorio == null || tipoRelatorio.equals("")) {
			throw new ConsistirException("O TIPO DO RELATÓRIO deve ser informado para a geração do relatório.");
		}
		if (!unidadeEnsinoCursoVO.getCurso().getNivelEducacionalPosGraduacao()) {
			if (ano == null || ano.equals("")) {
				throw new ConsistirException("O ANO deve ser informado para a geração do relatório.");
			}
			if (semestre == null || semestre.equals("")) {
				throw new ConsistirException("O SEMESTRE deve ser informado para a geração do relatório.");
			}
		}
		if (!cursoVO.getPeriodicidade().equals("IN")) {
			if (ano.length() < 4) {
				throw new ConsistirException("O ANO deve possuir 4 dígitos.");
			}
		}
	}

    @Override
    public List<MapaFinanceiroAlunosRelVO> criarLista(Boolean trazerApenasAlunosAtivos, Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre) throws Exception {
        List<MapaFinanceiroAlunosRelVO> lista;
        lista = executarConsultaAlunos(trazerApenasAlunosAtivos, unidadeEnsino, curso, turma, ano, semestre);
        if (!lista.isEmpty()) {
            for (MapaFinanceiroAlunosRelVO mapaFinanceiroAlunosRelVO : lista) {
                List<MapaFinanceiroAlunosRel_ParcelasVO> listaParcelas;
                listaParcelas = executarConsultaParcelas(mapaFinanceiroAlunosRelVO.getMatricula());
                mapaFinanceiroAlunosRelVO.setListaMapaParcelas(listaParcelas);
            }
        }
        return lista;
    }

    private List<MapaFinanceiroAlunosRelVO> executarConsultaAlunos(Boolean trazerApenasAlunosAtivos, Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre) throws SQLException, Exception {
        List<MapaFinanceiroAlunosRelVO> lista = new ArrayList<MapaFinanceiroAlunosRelVO>(0);
        StringBuilder sql = new StringBuilder();
        sql.append("select m.matricula, p.nome, gc.nome as gradecurricular, pfc.descricao as planofinanceiro, ");
        sql.append("t.identificadorturma as turma, t.codigo as codigoturma, c.codigo as curso ");
        sql.append("from matriculaPeriodo mp ");
        sql.append("inner join matricula m on m.matricula = mp.matricula ");
        sql.append("inner join pessoa p on p.codigo = m.aluno ");
        sql.append("inner join curso c on c.codigo = m.curso ");
        sql.append("inner join turma t on t.codigo = mp.turma ");
        sql.append("inner join gradecurricular gc on gc.codigo = mp.gradecurricular ");
        sql.append("inner join planoFinanceiroCurso pfc on pfc.codigo = mp.planoFinanceiroCurso ");
        sql.append("inner join unidadeEnsino ue on ue.codigo = m.unidadeensino ");
        sql.append("inner join unidadeEnsinoCurso uec on uec.codigo = mp.unidadeensinocurso ");
        sql.append("inner join turno tn on tn.codigo = uec.turno ");
        sql.append("where ");//mp.situacaoMatriculaPeriodo = 'AT' and mp.situacao = 'CO' and");
        sql.append(" ue.codigo = ");
        sql.append(unidadeEnsino);
        sql.append(" and c.codigo = ");
        sql.append(curso);
        if (turma != null && turma > 0) {
            sql.append(" and t.codigo = ");
            sql.append(turma);
        }
        if (trazerApenasAlunosAtivos) {
        	sql.append(" and mp.situacaoMatriculaPeriodo = 'AT' and m.situacao = 'AT' ");
        }
        if (ano != null && !ano.equals("")) {
            sql.append(" and mp.ano = '");
            sql.append(ano);
            sql.append("' ");
        }
        if (semestre != null && !semestre.equals("")) {
            sql.append(" and mp.semestre = '");
            sql.append(semestre);
            sql.append("' ");
        }
        sql.append(" group by t.codigo, c.codigo, p.nome, m.matricula, gc.nome, pfc.descricao, t.identificadorturma ");
        sql.append(" order by c.codigo, t.codigo, p.nome ");

        SqlRowSet dadosSql = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (dadosSql.next()) {
            lista.add(montarDadosAlunos(dadosSql));
        }
        return lista;
    }

    private MapaFinanceiroAlunosRelVO montarDadosAlunos(SqlRowSet sqlRowSet) throws Exception {
        MapaFinanceiroAlunosRelVO obj = new MapaFinanceiroAlunosRelVO();
        obj.setMatricula(sqlRowSet.getString("matricula"));
        obj.setNome(sqlRowSet.getString("nome"));
        obj.setGradeCurricular(sqlRowSet.getString("gradecurricular"));
        obj.setPlanoFinanceiro(sqlRowSet.getString("planofinanceiro"));
        obj.setTurma(sqlRowSet.getString("turma"));
        return obj;
    }

    private List<MapaFinanceiroAlunosRel_ParcelasVO> executarConsultaParcelas(String matriculaAluno) throws SQLException, Exception {
        List<MapaFinanceiroAlunosRel_ParcelasVO> lista = new ArrayList<MapaFinanceiroAlunosRel_ParcelasVO>(0);
        StringBuilder sql = new StringBuilder();
        sql.append("select cr.valordescontoprogressivo, cr.descontoprogressivoutilizado, cr.datavencimento, cr.parcela, cr.valor, cr.descontoprogressivoutilizado, ");
        sql.append("cr.situacao, cr.tipoorigem, cr.juro, cr.multa, cr.acrescimo, cr.valorrecebido from contareceber cr ");
        sql.append("where cr.matriculaaluno = '");
        sql.append(matriculaAluno);
        sql.append("' order by cr.datavencimento ");
        SqlRowSet dadosSql = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (dadosSql.next()) {
            lista.add(montarDadosParcelas(dadosSql));
        }
        return lista;
    }

    private MapaFinanceiroAlunosRel_ParcelasVO montarDadosParcelas(SqlRowSet sqlRowSet) throws Exception {
        MapaFinanceiroAlunosRel_ParcelasVO obj = new MapaFinanceiroAlunosRel_ParcelasVO();
        obj.setParcela(sqlRowSet.getString("parcela"));
        String tipoOrigem = sqlRowSet.getString("tipoorigem");
        obj.setTipoOrigemParcela(TipoOrigemContaReceber.getDescricao(tipoOrigem));
        obj.setValor(sqlRowSet.getDouble("valor"));
        obj.setJuro(sqlRowSet.getDouble("juro"));
        obj.setMulta(sqlRowSet.getDouble("multa"));
        obj.setAcrescimo(sqlRowSet.getDouble("acrescimo"));
        obj.setSituacao(SituacaoContaReceber.getDescricao(sqlRowSet.getString("situacao")));
        obj.setValorPago(sqlRowSet.getDouble("valorrecebido"));
        obj.setAnt1(0.0);
        obj.setAnt2(0.0);
        obj.setAnt3(0.0);
        obj.setAnt4(0.0);
        String descontoProgressivoUtilizado = sqlRowSet.getString("descontoprogressivoutilizado");
        if (descontoProgressivoUtilizado != null) {
            if (descontoProgressivoUtilizado.equals("PRIMEIRO")) {
                obj.setAnt1(sqlRowSet.getDouble("valordescontoprogressivo"));
            } else if (descontoProgressivoUtilizado.equals("SEGUNDO")) {
                obj.setAnt2(sqlRowSet.getDouble("valordescontoprogressivo"));
            } else if (descontoProgressivoUtilizado.equals("TERCEIRO")) {
                obj.setAnt3(sqlRowSet.getDouble("valordescontoprogressivo"));
            } else if (descontoProgressivoUtilizado.equals("QUARTO")) {
                obj.setAnt4(sqlRowSet.getDouble("valordescontoprogressivo"));
            }
        }

        obj.setDataVencimento(sqlRowSet.getDate("datavencimento"));
        return obj;
    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
    }

    public static String getIdEntidade() {
        return ("MapaFinanceiroAlunosRel");
    }
    
    public void calcularSomatoriosParcelas (List<MapaFinanceiroAlunosRelVO> listaMapaFinanceiroAlunosRelVOs, SuperParametroRelVO superParametroRelVO) throws Exception {
    	Double somaValor = 0.0;
    	Double somaAnt1 = 0.0; 
    	Double somaAnt2 = 0.0;
    	Double somaAnt3 = 0.0;
    	Double somaAnt4 = 0.0;
    	Double somaJuro = 0.0;
    	Double somaMulta = 0.0;
    	Double somaAcrescimo = 0.0; 
    	Double somaValorPago = 0.0;
    	for (MapaFinanceiroAlunosRelVO mapaFinanceiroAlunosRelVO : listaMapaFinanceiroAlunosRelVOs) {
    		for (MapaFinanceiroAlunosRel_ParcelasVO parcela : mapaFinanceiroAlunosRelVO.getListaMapaParcelas()) {
    			somaValor += Uteis.isAtributoPreenchido(parcela.getValor()) ?  parcela.getValor() : 0.0;
    			somaAnt1 += Uteis.isAtributoPreenchido(parcela.getAnt1()) ?  parcela.getAnt1() : 0.0;
    			somaAnt2 += Uteis.isAtributoPreenchido(parcela.getAnt2()) ?  parcela.getAnt2() : 0.0;
    			somaAnt3 += Uteis.isAtributoPreenchido(parcela.getAnt3()) ?  parcela.getAnt3() : 0.0;
    			somaAnt4 += Uteis.isAtributoPreenchido(parcela.getAnt4()) ?  parcela.getAnt4() : 0.0;
    			somaJuro += Uteis.isAtributoPreenchido(parcela.getJuro()) ?  parcela.getJuro() : 0.0;
    			somaMulta += Uteis.isAtributoPreenchido(parcela.getMulta()) ?  parcela.getMulta() : 0.0;
    			somaAcrescimo += Uteis.isAtributoPreenchido(parcela.getAcrescimo()) ?  parcela.getAcrescimo() : 0.0;
    			somaValorPago += Uteis.isAtributoPreenchido(parcela.getValorPago()) ?  parcela.getValorPago() : 0.0;
    		}
    	}
    	superParametroRelVO.adicionarParametro("somaTotalValor", somaValor);
    	superParametroRelVO.adicionarParametro("somaTotalAnt1", somaAnt1);
    	superParametroRelVO.adicionarParametro("somaTotalAnt2", somaAnt2);
    	superParametroRelVO.adicionarParametro("somaTotalAnt3", somaAnt3);
    	superParametroRelVO.adicionarParametro("somaTotalAnt4", somaAnt4);
    	superParametroRelVO.adicionarParametro("somaTotalJuro", somaJuro);
    	superParametroRelVO.adicionarParametro("somaTotalMulta", somaMulta);
    	superParametroRelVO.adicionarParametro("somaTotalAcrescimo", somaAcrescimo);
    	superParametroRelVO.adicionarParametro("somaTotalValorPago", somaValorPago);
    }

}
