package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.biblioteca.SituacaoExemplaresRelVO;
import relatorio.negocio.interfaces.biblioteca.SituacaoExemplaresRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.negocio.jdbc.biblioteca.enumeradores.SituacaoExemplaresRelEnum;

@Repository
@Scope("singleton")
@Lazy
public class SituacaoExemplaresRel extends SuperRelatorio implements SituacaoExemplaresRelInterfaceFacade {

    public SituacaoExemplaresRel() {

    }

    @Override
    public List<SituacaoExemplaresRelVO> criarObjeto(SituacaoExemplaresRelVO situacaoExemplaresRelVO, String tipoSaida) throws Exception {
        List<SituacaoExemplaresRelVO> listaSituacaoExemplaresRelVO = new ArrayList<SituacaoExemplaresRelVO>(0);
        SqlRowSet dadosSQL = executarConsultaParametrizada(situacaoExemplaresRelVO, tipoSaida);
        while (dadosSQL.next()) {
            listaSituacaoExemplaresRelVO.add(montarDados(dadosSQL));
        }
        return listaSituacaoExemplaresRelVO;
    }

    private SqlRowSet executarConsultaParametrizada(SituacaoExemplaresRelVO situacaoExemplaresRelVO, String tipoSaida) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT s.nome as nomesecao, c.nivelbibliografico, c.titulo as catalogo, c.classificacaobibliografica as classificacao, c.cutterpha as cutterpha, e.codigobarra, e.situacaoatual as situacao, itemregistrosaidaacervo.tiposaida ");
        sql.append(" FROM exemplar e ");
        sql.append("  INNER JOIN catalogo c                    ON e.catalogo     = c.codigo ");
        sql.append("  INNER JOIN biblioteca b                  ON e.biblioteca   = b.codigo ");
        sql.append("  INNER JOIN unidadeensinobiblioteca ueb   ON ueb.biblioteca = b.codigo ");
        sql.append("  INNER JOIN unidadeensino une             ON une.codigo     = ueb.unidadeensino ");
        sql.append("  LEFT JOIN secao s                        ON e.secao        = s.codigo ");
        sql.append("  LEFT JOIN itemregistrosaidaacervo	   ON e.codigo       = itemregistrosaidaacervo.exemplar ");
        sql.append("WHERE ");
        sql.append(" une.codigo = ");
        sql.append(situacaoExemplaresRelVO.getUnidadeEnsino().getCodigo());
        sql.append(" AND e.biblioteca = ");
        sql.append(situacaoExemplaresRelVO.getExemplar().getBiblioteca().getCodigo());

        if (situacaoExemplaresRelVO.getExemplar().getSecao().getCodigo() != null && situacaoExemplaresRelVO.getExemplar().getSecao().getCodigo() > 0) {
            sql.append(" AND s.codigo = ");
            sql.append(situacaoExemplaresRelVO.getExemplar().getSecao().getCodigo());
        }
        if (situacaoExemplaresRelVO.getExemplar().getCatalogo().getNivelBibliografico() != null
            && !situacaoExemplaresRelVO.getExemplar().getCatalogo().getNivelBibliografico().equals("")
            && !situacaoExemplaresRelVO.getExemplar().getCatalogo().getNivelBibliografico().equals("TODOS")) {
            sql.append(" AND c.nivelbibliografico = '");
            sql.append(situacaoExemplaresRelVO.getExemplar().getCatalogo().getNivelBibliografico());
            sql.append("'");
        }
        if (situacaoExemplaresRelVO.getExemplar().getCatalogo().getTitulo() != null && !situacaoExemplaresRelVO.getExemplar().getCatalogo().getTitulo().equals("")) {
            sql.append(" AND c.titulo = '");
            sql.append(situacaoExemplaresRelVO.getExemplar().getCatalogo().getTitulo());
            sql.append("'");
        }
        if (situacaoExemplaresRelVO.getExemplar().getSituacaoAtual() != null && !situacaoExemplaresRelVO.getExemplar().getSituacaoAtual().equals("0")) {
            sql.append(" AND e.situacaoatual = '");
            sql.append(situacaoExemplaresRelVO.getExemplar().getSituacaoAtual());
            sql.append("'");
        }
        
        if(situacaoExemplaresRelVO.getTipo() == 0) {
        	sql.append(" AND (c.assinaturaperiodico = false or c.assinaturaperiodico is null) ");
        }
        
        if(situacaoExemplaresRelVO.getTipo() == 1) {
        	sql.append(" AND c.assinaturaperiodico = true ");
        }
        if (situacaoExemplaresRelVO.getExemplar().getSituacaoAtual().equals(SituacaoExemplar.INUTILIZADO.getValor())) {
        	sql.append(" AND itemregistrosaidaacervo.tiposaida = '").append(tipoSaida).append("'");
        }

        sql.append(" ORDER BY c.titulo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
       
        return tabelaResultado;
    }

    private SituacaoExemplaresRelVO montarDados(SqlRowSet dadosSQL) {
        SituacaoExemplaresRelVO situacaoExemplaresRelVO = new SituacaoExemplaresRelVO();

        situacaoExemplaresRelVO.setSecao(dadosSQL.getString("nomesecao"));
        situacaoExemplaresRelVO.setNivelBibliografico(SituacaoExemplaresRelEnum.getDescricao(dadosSQL.getString("nivelbibliografico")));
        situacaoExemplaresRelVO.setCatalogo(dadosSQL.getString("catalogo"));
        situacaoExemplaresRelVO.setColunaExemplar(dadosSQL.getString("codigobarra"));
        situacaoExemplaresRelVO.setSituacao(SituacaoExemplar.getDescricao(dadosSQL.getString("situacao")));
        situacaoExemplaresRelVO.getExemplar().getCatalogo().setClassificacaoBibliografica(dadosSQL.getString("classificacao"));
        situacaoExemplaresRelVO.getExemplar().getCatalogo().setCutterPha(dadosSQL.getString("cutterpha"));
        return situacaoExemplaresRelVO;
    }

    @Override
    public void validarDados(SituacaoExemplaresRelVO situacaoExemplaresRelVO) throws ConsistirException {

        if (situacaoExemplaresRelVO.getUnidadeEnsino() == null || situacaoExemplaresRelVO.getUnidadeEnsino().getCodigo() == null
            || situacaoExemplaresRelVO.getUnidadeEnsino().getCodigo() == 0 || situacaoExemplaresRelVO.getUnidadeEnsino().getCodigo().equals(0)) {
            throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
        }

        if (situacaoExemplaresRelVO.getExemplar().getBiblioteca() == null || situacaoExemplaresRelVO.getExemplar().getBiblioteca().getCodigo() == null
            || situacaoExemplaresRelVO.getExemplar().getBiblioteca().getCodigo() == 0
            || situacaoExemplaresRelVO.getExemplar().getBiblioteca().getCodigo().equals(0)) {
            throw new ConsistirException("O campo BIBLIOTECA deve ser informado para a geração do relatório.");
        }
    }

    @Override
    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + ".jrxml");
    }

    @Override
    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
    }

    public static String getIdEntidade() {
        return "SituacaoExemplaresRel";
    }

}
