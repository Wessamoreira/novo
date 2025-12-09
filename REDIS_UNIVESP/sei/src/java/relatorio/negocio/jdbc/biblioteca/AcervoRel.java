package relatorio.negocio.jdbc.biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.biblioteca.AcervoCabecalhoVO;
import negocio.comuns.biblioteca.AcervoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ClassificacaoBibliograficaVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.utilitarias.Uteis;

import relatorio.negocio.interfaces.biblioteca.AcervoRelInterfaceFacade;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AcervoRel extends SuperRelatorio implements AcervoRelInterfaceFacade {

    protected static String idEntidade;

    public AcervoRel() throws Exception {
        super();
        setIdEntidade("Acervo");
    }

    public List consultarUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        //List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(valorConsulta, 0, controlarAcesso, nivelMontarDados, usuarioVO);
        List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNomeVinculadoBiblioteca(valorConsulta, unidadeEnsino, controlarAcesso, nivelMontarDados, usuarioVO);
        
        for (UnidadeEnsinoVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;
    }
    
    public List consultarUnidadeEnsinoPorBiblioteca(Integer biblioteca, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUnidadeEnsinoPorBiblioteca(biblioteca, unidadeEnsino, controlarAcesso, nivelMontarDados, usuarioVO);
        for (UnidadeEnsinoVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;
    }

    public List<SelectItem> consultarBiblioteca(Integer unidadeEnsino, Obrigatorio obrigatorio, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        if(obrigatorio == null || obrigatorio.equals(Obrigatorio.NAO)) {
        	objs.add(new SelectItem(0, ""));
        }
        List<BibliotecaVO> resultadoConsulta = getFacadeFactory().getBibliotecaFacade().consultarPorUnidadeEnsino(unidadeEnsino, controlarAcesso, nivelMontarDados, usuarioVO);
        for (BibliotecaVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;
    }

    public List consultarSecao(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        return getFacadeFactory().getSecaoFacade().consultar(campoConsulta, valorConsulta, controlarAcesso, nivelMontarDados, usuarioVO);
    }

    public List consultarMatrizCurricular(Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<GradeCurricularVO> resultadoConsulta = getFacadeFactory().getGradeCurricularFacade().consultarPorCurso(curso, controlarAcesso, nivelMontarDados, usuarioVO);

        for (GradeCurricularVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;
    }

    public List consultarClassificacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        List<ClassificacaoBibliograficaVO> resultadoConsulta = getFacadeFactory().getClassificacaoBibliograficaFacade().consultarPorNome("", false, nivelMontarDados, usuarioVO);

        for (ClassificacaoBibliograficaVO obj : resultadoConsulta) {
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        return objs;
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return AcervoRel.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AcervoRel.idEntidade = idEntidade;
    }

    public List realizarBuscaDeObjetosParaMontarRelatorioAcervo(UnidadeEnsinoVO unidadeEnsinoVO, BibliotecaVO bibliotecaVO, SecaoVO secaoVO, CursoVO cursoVO, TurnoVO turnoVO, GradeCurricularVO matrizVO, DisciplinaVO disciplinaVO, EditoraVO editoraVO, ClassificacaoBibliograficaVO classificacaoBibliograficaVO, CatalogoVO catalogoVO, String tipoRelatorio) throws Exception {

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct secao.nome as secao, catalogo.codigo, catalogo.titulo, catalogo.subtitulo, editora.nome as editora, classificacaobibliografica.nome as classificacao, catalogo.edicao, ");
        sb.append("catalogo.anopublicacao, ");
        sb.append("catalogo.numero, catalogo.serie, catalogo.isbn, catalogo.issn, catalogo.cutterpha, biblioteca.nome as biblioteca, exemplar.local, exemplar.volume, catalogo.nivelBibliografico, ");

        sb.append("(select count(exemplar2.situacaoatual) from exemplar as exemplar2 where exemplar2.catalogo = catalogo.codigo and situacaoatual IN ('IT', 'CE', 'DE', 'BA', 'EX')) as inativos, ");
        sb.append("(select count(exemplar2.situacaoatual) from exemplar as exemplar2 where exemplar2.catalogo = catalogo.codigo and situacaoatual NOT IN ('IT', 'CE', 'DE', 'BA', 'EX', 'EM')) as ativos, ");
        sb.append("(select count(exemplar2.situacaoatual) from exemplar as exemplar2 where exemplar2.catalogo = catalogo.codigo and situacaoatual NOT IN ('EM')) as emprestados ");

        sb.append("from unidadeensino ");
        sb.append("inner join unidadeensinobiblioteca on unidadeensinobiblioteca.unidadeensino = unidadeensino.codigo ");
        sb.append("inner join biblioteca on biblioteca.codigo = unidadeensinobiblioteca.biblioteca ");
        sb.append("inner join exemplar on exemplar.biblioteca = biblioteca.codigo ");
        sb.append("inner join secao on exemplar.secao = secao.codigo ");
        sb.append("inner join catalogo on exemplar.catalogo = catalogo.codigo ");
        sb.append("inner join editora on catalogo.editora = editora.codigo ");
        sb.append("inner join classificacaobibliografica on catalogo.classificacaobibliografica = classificacaobibliografica.codigo ");
        if (!cursoVO.getCodigo().equals(0)) {        	
            sb.append("inner join unidadeensinocurso on unidadeensinocurso.unidadeensino = unidadeensino.codigo ");
            sb.append("inner join curso on unidadeensinocurso.curso = curso.codigo ");
            sb.append("left join planoEnsino on planoEnsino.unidadeEnsino = unidadeEnsino.codigo and planoEnsino.curso = curso.codigo ");
            sb.append("left join catalogocurso on catalogocurso.catalogo = catalogo.codigo and catalogocurso.curso = curso.codigo ");
            if (!matrizVO.getCodigo().equals(0)) {
                sb.append("inner join gradecurricular on gradecurricular.curso = curso.codigo ");
                if (!disciplinaVO.getCodigo().equals(0)) {
                    sb.append("left join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
                    sb.append("left join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
                    sb.append("lfet join disciplina on gradedisciplina.disciplina = disciplina.codigo and disciplina.codigo = planoEnsino.disciplina ");
                }
            }
        }
        sb.append(" where unidadeensino.codigo = ").append(unidadeEnsinoVO.getCodigo());
        sb.append(" and biblioteca.codigo = ").append(bibliotecaVO.getCodigo());
        if (!secaoVO.getCodigo().equals(0)) {
            sb.append(" and secao.codigo= ").append(secaoVO.getCodigo());
        }
        if (!cursoVO.getCodigo().equals(0)) {
            sb.append(" and curso.codigo= ").append(cursoVO.getCodigo()).append(" and (planoEnsino.codigo is not null or catalogocurso.codigo is not null) ") ;
            if (!matrizVO.getCodigo().equals(0)) {
                sb.append(" and gradecurricular.codigo= ").append(matrizVO.getCodigo());
                if (!disciplinaVO.getCodigo().equals(0)) {
                    sb.append(" and disciplina.codigo= ").append(disciplinaVO.getCodigo()).append(" and planoEnsino.codigo is not null ");
                }
            }
        }
        if (!classificacaoBibliograficaVO.getCodigo().equals(0)) {
            sb.append(" and classificacaobibliografica.codigo= ").append(classificacaoBibliograficaVO.getCodigo());
        }
        if (!catalogoVO.getCodigo().equals(0)) {
            sb.append(" and catalogo.codigo= ").append(catalogoVO.getCodigo());
        }
        if (!editoraVO.getCodigo().equals(0)) {
            sb.append(" and editora.codigo= ").append(editoraVO.getCodigo());
        }

        if (tipoRelatorio.equals("porLocal")) {
            sb.append(" group by secao.nome, catalogo.codigo, catalogo.titulo, catalogo.subtitulo, editora.nome, ");
            sb.append(" classificacaobibliografica.nome, catalogo.edicao, catalogo.anopublicacao, ");
            sb.append(" catalogo.numero, catalogo.serie, catalogo.isbn, ");
            sb.append(" catalogo.issn, catalogo.cutterpha, biblioteca.nome, exemplar.local, exemplar.volume, catalogo.nivelBibliografico");
            sb.append(" order by secao.nome, exemplar.local");
        } else if (tipoRelatorio.equals("porClassificacao")) {
            sb.append(" group by catalogo.nivelBibliografico, classificacaobibliografica.nome, catalogo.codigo, catalogo.titulo, catalogo.subtitulo, editora.nome, ");
            sb.append(" secao.nome, catalogo.edicao, catalogo.anopublicacao, ");
            sb.append(" catalogo.numero, catalogo.serie, catalogo.isbn, ");
            sb.append(" catalogo.issn, catalogo.cutterpha, biblioteca.nome, exemplar.local, exemplar.volume");
            sb.append(" order by catalogo.nivelBibliografico, classificacaobibliografica.nome");
        }


        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

        List<AcervoVO> lista = montarDadosLista(tabelaResultado);
        List<AcervoCabecalhoVO> listaRetorno = new ArrayList<AcervoCabecalhoVO>(0);

        if (tipoRelatorio.equals("porLocal")) {
            listaRetorno = realizarAgrupamentoListaPorLocal(lista);

        } else if (tipoRelatorio.equals("porClassificacao")) {
            listaRetorno = realizarAgrupamentoListaPorClassificacao(lista);
        }
        return listaRetorno;

    }

    private List<AcervoVO> montarDadosLista(SqlRowSet tabelaResultado) {
        List<AcervoVO> acervos = new ArrayList<AcervoVO>(0);
        while (tabelaResultado.next()) {
            acervos.add(montarDados(tabelaResultado));
        }
        return acervos;
    }

    private AcervoVO montarDados(SqlRowSet sqlRowSet) {
        AcervoVO acervo = new AcervoVO();
        acervo.setAnoPublicacao(sqlRowSet.getString("anopublicacao"));
        acervo.setBiblioteca(sqlRowSet.getString("biblioteca"));
        acervo.setClassificacao(sqlRowSet.getString("classificacao"));
        acervo.setCutterPha(sqlRowSet.getString("cutterpha"));
        acervo.setEdicao(sqlRowSet.getString("edicao"));
        acervo.setEditora(sqlRowSet.getString("editora"));
        acervo.setExemplarAtivos(sqlRowSet.getLong("ativos"));
        acervo.setExemplarInativos(sqlRowSet.getLong("inativos"));
        acervo.setExemplarEmprestado(sqlRowSet.getLong("emprestados"));
        acervo.setIsbn(sqlRowSet.getString("isbn"));
        acervo.setIssn(sqlRowSet.getString("issn"));
        acervo.setLocal(sqlRowSet.getString("local"));
        acervo.setNumero(sqlRowSet.getString("numero"));
        acervo.setSecao(sqlRowSet.getString("secao"));
        acervo.setSerie(sqlRowSet.getString("serie"));
        acervo.setSubTitulo(sqlRowSet.getString("subtitulo"));
        acervo.setTitulo(sqlRowSet.getString("titulo"));
        acervo.setVolume(sqlRowSet.getString("volume"));
        acervo.setNivelBibliografico(sqlRowSet.getString("nivelbibliografico"));
        return acervo;
    }

    public String caminhoBaseRelatorio() throws Exception {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
    }

    public String designIReportRelatorio(String tipoRelatorio) throws Exception {
        if (tipoRelatorio.equals("porLocal")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + "PorLocalRel" + ".jrxml");
        } else if (tipoRelatorio.equals("porClassificacao")) {
            return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator + getIdEntidade() + "PorClassificacaoRel" + ".jrxml");
        }
        return "";
    }

    private List<AcervoCabecalhoVO> realizarAgrupamentoListaPorLocal(List<AcervoVO> lista) {
        List<AcervoCabecalhoVO> listaAcervo = new ArrayList<AcervoCabecalhoVO>(0);
        for (AcervoVO acervo : lista) {
            if (listaAcervo.isEmpty()) {
                AcervoCabecalhoVO acervoCabecalho = new AcervoCabecalhoVO();
                acervoCabecalho.setSecao(acervo.getSecao());
                acervoCabecalho.setLocal(acervo.getLocal());
                acervoCabecalho.setBiblioteca(acervo.getBiblioteca());
                acervoCabecalho.getAcervos().add(acervo);
                acervoCabecalho.setTotalGeral(lista.size());
                listaAcervo.add(acervoCabecalho);
            } else {
                int gatilho = 0;
                for (AcervoCabecalhoVO acervoCabecalho : listaAcervo) {
                    if (acervoCabecalho.getSecao().equals(acervo.getSecao()) && acervoCabecalho.getLocal().equals(acervo.getLocal())) {
                        acervoCabecalho.getAcervos().add(acervo);
                        gatilho++;
                    }
                }
                if (gatilho == 0) {
                    AcervoCabecalhoVO acervoCabecalho = new AcervoCabecalhoVO();
                    acervoCabecalho.setSecao(acervo.getSecao());
                    acervoCabecalho.setLocal(acervo.getLocal());
                    acervoCabecalho.setBiblioteca(acervo.getBiblioteca());
                    acervoCabecalho.getAcervos().add(acervo);
                    acervoCabecalho.setTotalGeral(lista.size());
                    listaAcervo.add(acervoCabecalho);
                }
            }
        }
        return listaAcervo;

    }

    private List<AcervoCabecalhoVO> realizarAgrupamentoListaPorClassificacao(List<AcervoVO> lista) {
        List<AcervoCabecalhoVO> listaAcervo = new ArrayList<AcervoCabecalhoVO>(0);
        for (AcervoVO acervo : lista) {
            if (listaAcervo.isEmpty()) {
                AcervoCabecalhoVO acervoCabecalho = new AcervoCabecalhoVO();
                acervoCabecalho.setNivelBibliografico(acervo.getNivelBibliografico());
                acervoCabecalho.setClassificacao(acervo.getClassificacao());
                acervoCabecalho.getAcervos().add(acervo);
                acervoCabecalho.setTotalGeral(lista.size());
                listaAcervo.add(acervoCabecalho);
            } else {
                int gatilho = 0;
                for (AcervoCabecalhoVO acervoCabecalho : listaAcervo) {
                    if (acervoCabecalho.getNivelBibliografico().equals(acervo.getNivelBibliografico()) && acervoCabecalho.getClassificacao().equals(acervo.getClassificacao())) {
                        acervoCabecalho.getAcervos().add(acervo);
                        gatilho++;
                    }
                }
                if (gatilho == 0) {
                    AcervoCabecalhoVO acervoCabecalho = new AcervoCabecalhoVO();
                    acervoCabecalho.setNivelBibliografico(acervo.getNivelBibliografico());
                    acervoCabecalho.setClassificacao(acervo.getClassificacao());
                    acervoCabecalho.getAcervos().add(acervo);
                    acervoCabecalho.setTotalGeral(lista.size());
                    listaAcervo.add(acervoCabecalho);
                }
            }
        }
        return listaAcervo;
    }
}
