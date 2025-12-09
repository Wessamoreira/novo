package negocio.facade.jdbc.biblioteca;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
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
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.ArquivoMarc21VO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoAreaConhecimentoVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoCursoVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.CidadePublicacaoCatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EditoraVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.LinguaPublicacaoCatalogoVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.TipoCatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.TipoFiltroConsulta;
import negocio.comuns.utilitarias.TipoFiltroConsultaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisFTP;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.biblioteca.CatalogoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>CatalogoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CatalogoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CatalogoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Catalogo extends ControleAcesso implements CatalogoInterfaceFacade {

    protected static String idEntidade;
  
   

	public Catalogo() throws Exception {
        super();
        setIdEntidade("Catalogo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>CatalogoVO</code>.
     */
    public CatalogoVO novo() throws Exception {
        Catalogo.incluir(getIdEntidade());
        CatalogoVO obj = new CatalogoVO();
        
        return obj;
        
    }

    /**
     * Método que atualiza o numero de exemplares da Catalogo, a partir do momento em que se cadastra um novo exemplar
     * de uma determinada Catalogo.
     * 
     * @param exemplarVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarNrExemplaresCatalogo(ExemplarVO exemplarVO) throws Exception {
        try {
            int contador = getFacadeFactory().getExemplarFacade().consultarNrExemplaresCatalogoGravadosDisponiveis(exemplarVO, "");
            String sql = "UPDATE Catalogo set nrExemplares=? WHERE (codigo = ?)";
            getConexao().getJdbcTemplate().update(sql, new Object[]{contador, exemplarVO.getCatalogo().getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public void inicializarDadosCatalogoNovo(CatalogoVO catalogoVO, UsuarioVO usuario) throws Exception {
        catalogoVO.getResponsavel().setCodigo(usuario.getCodigo());
        catalogoVO.getResponsavel().setNome(usuario.getNome());
        catalogoVO.getResponsavelAtualizacao().setCodigo(usuario.getCodigo());
        catalogoVO.getResponsavelAtualizacao().setNome(usuario.getNome());
        catalogoVO.setDataCadastro(new Date());
        catalogoVO.setDataUltimaAtualizacao(new Date());
    }

    public void inicializarDadosCatalogoEditar(CatalogoVO catalogoVO, UsuarioVO usuario) throws Exception {
    	catalogoVO.getResponsavelAtualizacao().setCodigo(usuario.getCodigo());
        catalogoVO.getResponsavelAtualizacao().setNome(usuario.getNome());
        catalogoVO.setDataUltimaAtualizacao(new Date());
    }

    private void validarDados(CatalogoVO catalogoVO) throws Exception {
        if (catalogoVO.getTitulo().equals("")) {
            throw new Exception("Informe o título do catálogo.");
        }
        if (catalogoVO.getLinguaPublicacaoCatalogo().getCodigo() == 0 && !catalogoVO.getAssinaturaPeriodico()) {
            throw new Exception("Informe a língua de publicação do catálogo.");
        }
        if (!Uteis.isAtributoPreenchido(catalogoVO.getNivelBibliografico())) {
            throw new Exception("Informe o Nível Bibliográfico do catálogo.");
        }
        if (!catalogoVO.getAnoPublicacao().trim().isEmpty() && catalogoVO.getAnoPublicacao().length() < 4 && !catalogoVO.getAssinaturaPeriodico()) {
        	throw new Exception("O campo ANO DE PUBLICAÇÃO (Dados Adicionais) deve ter 4 caracteres.");
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>CatalogoVO</code>. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>CatalogoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CatalogoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean validarDados) throws Exception {
        try {
            Catalogo.incluir(getIdEntidade(), true, usuario);
            if (validarDados) {
            	validarDados(obj);
            }

//            if (!obj.getConfiguracaoEspecifica()) {
//                obj.setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPadrao(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
//            }

            final StringBuilder sql = new StringBuilder(" INSERT INTO Catalogo( nivelBibliografico, editora, tipoCatalogo, dataCadastro, dataUltimaAtualizacao, nrPaginas, responsavel, numero, serie, isbn, issn, ");
            sql.append(" notas, anoPublicacao, responsavelAtualizacao, edicao, versaoDigital, nrExemplaresParaConsulta, ");
            sql.append(" palavrasChave, cutterPha, assunto, subtitulo, titulo, linguaPublicacao, cidadePublicacao, ocultar, classificacaoBibliografica,controlatempodefasagem,bibliograficacomplementarmes,bibliograficabasicames, ");
            sql.append(" datainicioassinatura, datafinalassinatura, periodicidade, cdu, situacaoAssinatura, assinaturaPeriodico, abreviacaoTitulo, link, numerocontrole , enviadoEbsco ) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo");

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    int i = 0;

                    sqlInserir.setString(++i, obj.getNivelBibliografico());
                    if (obj.getEditora().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getEditora().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getTipoCatalogo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getTipoCatalogo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastro()));
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataUltimaAtualizacao()));
                    sqlInserir.setString(++i, obj.getNrPaginas());
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
//                    sqlInserir.setString(++i, obj.getVolume());
                    sqlInserir.setString(++i, obj.getNumero());
                    sqlInserir.setString(++i, obj.getSerie());
                    sqlInserir.setString(++i, obj.getIsbn());
                    sqlInserir.setString(++i, obj.getIssn());
                    sqlInserir.setString(++i, obj.getNotas());
                    sqlInserir.setString(++i, obj.getAnoPublicacao());
                    if (obj.getResponsavelAtualizacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getResponsavelAtualizacao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getEdicao());
//                    if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() != 0) {
//                        sqlInserir.setInt(++i, obj.getConfiguracaoBiblioteca().getCodigo().intValue());
//                    } else {
//                        sqlInserir.setNull(++i, 0);
//                    }
                    sqlInserir.setBoolean(++i, obj.getVersaoDigital());
                    sqlInserir.setInt(++i, obj.getNrExemplaresParaConsulta());
//                    sqlInserir.setBoolean(++i, obj.getConfiguracaoEspecifica());
                    sqlInserir.setString(++i, obj.getPalavrasChave());
                    sqlInserir.setString(++i, obj.getCutterPha());
                    sqlInserir.setString(++i, obj.getAssunto());
                    sqlInserir.setString(++i, obj.getSubtitulo());
                    sqlInserir.setString(++i, obj.getTitulo());

                    if (obj.getLinguaPublicacaoCatalogo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getLinguaPublicacaoCatalogo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getCidadePublicacaoCatalogo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(++i, obj.getCidadePublicacaoCatalogo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setBoolean(++i, obj.getOcultar());
                    
                    sqlInserir.setString(++i, obj.getClassificacaoBibliografica());
                    
                    sqlInserir.setBoolean(++i, obj.getControlaTempoDefasagem());
                    if (obj.getBibliograficaComplementarMes() != 0) {
                        sqlInserir.setInt(++i, obj.getBibliograficaComplementarMes());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getBibliograficaBasicaMes() != 0) {
                        sqlInserir.setInt(++i, obj.getBibliograficaBasicaMes());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioAssinatura()));
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataFinalAssinatura()));
                    sqlInserir.setString(++i, obj.getPeriodicidade());
                    sqlInserir.setString(++i, obj.getCdu());
                    sqlInserir.setString(++i, obj.getSituacaoAssinatura());
                    sqlInserir.setBoolean(++i, obj.getAssinaturaPeriodico());
                    sqlInserir.setString(++i, obj.getAbreviacaoTitulo());
                    sqlInserir.setString(++i, obj.getLink());
                    sqlInserir.setString(++i, obj.getNumeroControle());
                    sqlInserir.setBoolean(++i, obj.getEnviadoEbsco());      
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

            persistirListaExemplares(obj, usuario);
            persistirListaCatalogoAutor(obj);
            persistirListaCatalogoCurso(obj);
            persistirListaCatalogoAreaConhecimento(obj);

            // Inclusão dos arquivos digitais da Catalogo no Banco de dados.
            if (!obj.getArquivoVOs().isEmpty()) {
                getFacadeFactory().getArquivoFacade().incluirArquivos(obj.getArquivoVOs(), obj.getCodigo(), OrigemArquivo.OBRA_DIGITAL, usuario, configuracaoGeralSistema);
            }
            // Inclusão dos arquivos digitais do sumário e capa da Catalogo no
            // Banco de dados.
            if (!obj.getArquivoSumarioCapaVOs().isEmpty()) {
                getFacadeFactory().getArquivoFacade().incluirArquivos(obj.getArquivoSumarioCapaVOs(), obj.getCodigo(), OrigemArquivo.OBRA_SUMARIO, usuario, configuracaoGeralSistema);
            }

        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void persistirListaExemplares(CatalogoVO catalogoVO, UsuarioVO usuario) throws Exception {
        for (ExemplarVO exemplarVO : catalogoVO.getExemplarVOs()) {
            exemplarVO.setCatalogo(catalogoVO);
            getFacadeFactory().getExemplarFacade().incluir(exemplarVO, false, false, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void persistirListaCatalogoAutor(CatalogoVO catalogoVO) throws Exception {
        for (CatalogoAutorVO catalogoAutorVO : catalogoVO.getCatalogoAutorVOs()) {
            catalogoAutorVO.setCatalogo(catalogoVO);
            getFacadeFactory().getCatalogoAutorFacade().incluir(catalogoAutorVO, new UsuarioVO());
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void persistirListaCatalogoCurso(CatalogoVO catalogoVO) throws Exception {
    	for (CatalogoCursoVO catalogoCursoVO : catalogoVO.getCatalogoCursoVOs()) {
    		catalogoCursoVO.setCatalogoVO(catalogoVO);
    		getFacadeFactory().getCatalogoCursoFacade().incluir(catalogoCursoVO);
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void persistirListaCatalogoAreaConhecimento(CatalogoVO catalogoVO) throws Exception {
    	for (CatalogoAreaConhecimentoVO catalogoAreaConhecimentoVO : catalogoVO.getCatalogoAreaConhecimentoVOs()) {
    		catalogoAreaConhecimentoVO.setCatalogoVO(catalogoVO);
    		getFacadeFactory().getCatalogoAreaConhecimentoFacade().incluir(catalogoAreaConhecimentoVO);
    	}
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>CatalogoVO</code>. Sempre utiliza a
     * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
     * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>CatalogoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CatalogoVO obj, UsuarioVO usuario) throws Exception {
        try {
			Catalogo.alterar(getIdEntidade(), true, usuario);
            validarDados(obj);

            final StringBuilder sql = new StringBuilder(" UPDATE Catalogo set nivelBibliografico=?, editora=?, tipoCatalogo=?, dataCadastro=?, dataUltimaAtualizacao=?, nrPaginas=?, responsavel=?, ");
            sql.append(" numero=?, serie=?, isbn=?, issn=?, notas=?, anoPublicacao=?, responsavelAtualizacao=?, edicao=?, versaoDigital=?, ");
            sql.append(" nrExemplaresParaConsulta=?, palavrasChave=?, cutterPha=?, assunto=?, subtitulo=?, titulo=?, linguaPublicacao=?, ");
            sql.append(" cidadePublicacao=?, ocultar=?, classificacaoBibliografica=?,controlatempodefasagem=?,bibliograficacomplementarmes=?,bibliograficabasicames=?, ");
            sql.append(" datainicioassinatura=?, datafinalassinatura=?, periodicidade=?, cdu=?, situacaoAssinatura=?, assinaturaPeriodico=?, abreviacaoTitulo=?, link=?, numerocontrole=? , enviadoEbsco=?  WHERE ((codigo = ?)) ");
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    int i = 0;

                    sqlAlterar.setString(++i, obj.getNivelBibliografico());
                    if (obj.getEditora().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getEditora().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getTipoCatalogo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getTipoCatalogo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataCadastro()));
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataUltimaAtualizacao()));
                    sqlAlterar.setString(++i, obj.getNrPaginas());
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
//                    sqlAlterar.setString(++i, obj.getVolume());
                    sqlAlterar.setString(++i, obj.getNumero());
                    sqlAlterar.setString(++i, obj.getSerie());
                    sqlAlterar.setString(++i, obj.getIsbn());
                    sqlAlterar.setString(++i, obj.getIssn());
                    sqlAlterar.setString(++i, obj.getNotas());
                    sqlAlterar.setString(++i, obj.getAnoPublicacao());
                    if (obj.getResponsavelAtualizacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getResponsavelAtualizacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setString(++i, obj.getEdicao());
//                    if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() != 0) {
//                        sqlAlterar.setInt(++i, obj.getConfiguracaoBiblioteca().getCodigo().intValue());
//                    } else {
//                        sqlAlterar.setNull(++i, 0);
//                    }
                    sqlAlterar.setBoolean(++i, obj.getVersaoDigital());
                    sqlAlterar.setInt(++i, obj.getNrExemplaresParaConsulta());
//                    sqlAlterar.setBoolean(++i, obj.getConfiguracaoEspecifica());
                    sqlAlterar.setString(++i, obj.getPalavrasChave());
                    sqlAlterar.setString(++i, obj.getCutterPha());
                    sqlAlterar.setString(++i, obj.getAssunto());
                    sqlAlterar.setString(++i, obj.getSubtitulo());
                    sqlAlterar.setString(++i, obj.getTitulo());

                    if (obj.getLinguaPublicacaoCatalogo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getLinguaPublicacaoCatalogo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getCidadePublicacaoCatalogo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(++i, obj.getCidadePublicacaoCatalogo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setBoolean(++i, obj.getOcultar());

                    sqlAlterar.setString(++i, obj.getClassificacaoBibliografica());

                    sqlAlterar.setBoolean(++i, obj.getControlaTempoDefasagem());
                    if (obj.getBibliograficaComplementarMes() != 0) {
                        sqlAlterar.setInt(++i, obj.getBibliograficaComplementarMes());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    if (obj.getBibliograficaBasicaMes() != 0) {
                        sqlAlterar.setInt(++i, obj.getBibliograficaBasicaMes());
                    } else {
                        sqlAlterar.setNull(++i, 0);
                    }
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataInicioAssinatura()));
                    sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataFinalAssinatura()));
                    sqlAlterar.setString(++i, obj.getPeriodicidade());
                    sqlAlterar.setString(++i, obj.getCdu());
                    sqlAlterar.setString(++i, obj.getSituacaoAssinatura());
                    sqlAlterar.setBoolean(++i, obj.getAssinaturaPeriodico());
                    sqlAlterar.setString(++i, obj.getAbreviacaoTitulo());
                    sqlAlterar.setString(++i, obj.getLink());
                    sqlAlterar.setString(++i, obj.getNumeroControle());
                    sqlAlterar.setBoolean(++i, obj.getEnviadoEbsco());                    
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getExemplarFacade().alterarListaExemplaresPorCodigoCatalogoSituacaoAtual(obj.getCodigo(), SituacaoExemplar.DISPONIVEL.getValor(), obj.getExemplarVOs(), usuario);

            getFacadeFactory().getCatalogoAutorFacade().alterarListaCatalogoAutorPorCodigoCatalogo(obj, obj.getCatalogoAutorVOs());
            getFacadeFactory().getCatalogoCursoFacade().alterarListaCatalogoCursoPorCodigoCatalogo(obj, obj.getCatalogoCursoVOs());
            
            getFacadeFactory().getCatalogoAreaConhecimentoFacade().alterarListaCatalogoCursoPorCodigoCatalogo(obj, obj.getCatalogoAreaConhecimentoVOs());

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>CatalogoVO</code>. Sempre localiza o registro a
     * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     * 
     * @param obj
     *            Objeto da classe <code>CatalogoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CatalogoVO obj, UsuarioVO usuario) throws Exception {
        try {
        	Catalogo.excluir(getIdEntidade(), true, usuario);
            getFacadeFactory().getExemplarFacade().excluirExemplarCatalogos(obj, usuario);
            getFacadeFactory().getCatalogoAutorFacade().excluirCatalogoAutorCatalogos(obj.getCodigo());
            getFacadeFactory().getCatalogoCursoFacade().excluirCatalogoCursoCatalogos(obj.getCodigo());
            getFacadeFactory().getCatalogoAreaConhecimentoFacade().excluirCatalogoCursoCatalogos(obj.getCodigo());
            String sql = "DELETE FROM Catalogo WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Subtrai em 1 unidade o número de Exemplares de uma Catalogo.
     * 
     * @param catalogoVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void subtrairUmExemplarNumeroExemplaresCatalogo(final CatalogoVO catalogoVO) throws Exception {
        // try {
        // catalogoVO.setNrExemplares(catalogoVO.getNrExemplares() - 1);
        // final String sql =
        // "UPDATE Catalogo set nrExemplares = ? WHERE codigo = ? ";
        // getConexao().getJdbcTemplate()
        // .update(sql, new Object[] { catalogoVO.getNrExemplares(),
        // catalogoVO.getCodigo() });
        // } catch (Exception e) {
        // throw e;
        // }
    }

    /**
     * Responsável por realizar uma consulta de <code>Catalogo</code> através do valor do atributo
     * <code>Date dataCadastro</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Catalogo WHERE ((dataCadastro >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataCadastro <= '"
                + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataCadastro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Catalogo</code> através do valor do atributo
     * <code>String tipoCatalogo</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CatalogoVO> consultarPorTipoCatalogo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM catalogo LEFT JOIN tipocatalogo ON catalogo.tipocatalogo = tipocatalogo.codigo");
        sqlStr.append(" WHERE sem_acentos(tipocatalogo.nome) iLIKE sem_acentos(?) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        sqlStr.append(" order by catalogo.titulo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Catalogo</code> através do valor do atributo <code>nome</code> da
     * classe <code>ClassificacaoBibliografica</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CatalogoVO> consultarPorNomeClassificacaoBibliografica(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select catalogo.* from catalogo ");
        sqlStr.append(" inner join classificacaobibliografica on catalogo.codigo=classificacaobibliografica.codigo");
        sqlStr.append(" where sem_acentos( classificacaobibliografica.nome ) ilike sem_acentos(?)");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
           	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
          	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        sqlStr.append(" ORDER BY classificacaobibliografica.nome, catalogo.titulo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Catalogo</code> através do valor do atributo <code>nome</code> da
     * classe <code>Editora</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     * 
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CatalogoVO> consultarPorNomeEditora(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Catalogo.* FROM Catalogo inner join Editora on Catalogo.editora = Editora.codigo ");
        sqlStr.append(" where sem_acentos( Editora.nome ) ilike sem_acentos(?) ");
        if(Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo and exemplar.biblioteca = ").append(biblioteca).append(" limit 1) ");
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        sqlStr.append(" ORDER BY Editora.nome, catalogo.titulo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CatalogoVO> consultarPorTitulo(String titulo, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, Integer tipocatalogo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM catalogo WHERE trim(sem_acentos(titulo)) ilike sem_acentos(trim(?)) ");        
        if(Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo and exemplar.biblioteca = ").append(biblioteca).append(" limit 1) ");
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        if(Uteis.isAtributoPreenchido(tipocatalogo)) {
        	sqlStr.append(" and catalogo.tipocatalogo = ").append(tipocatalogo);
        }
        sqlStr.append(" ORDER BY titulo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+titulo+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public List<CatalogoVO> consultarPorNomeAutor(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append(" SELECT Catalogo.*, array_to_string(array(select Autor.nome from CatalogoAutor inner JOIN Autor on Autor.codigo = CatalogoAutor.autor where CatalogoAutor.catalogo = Catalogo.codigo order by Autor.nome), ', ') as autores FROM Catalogo ");
//    	sqlStr.append(" LEFT JOIN CatalogoAutor on CatalogoAutor.catalogo = Catalogo.codigo ");
//    	sqlStr.append(" LEFT JOIN Autor on Autor.codigo = CatalogoAutor.autor ");
    	
    	sqlStr.append(" where (exists (select Autor.nome from CatalogoAutor inner JOIN Autor on Autor.codigo = CatalogoAutor.autor where CatalogoAutor.catalogo = Catalogo.codigo and sem_acentos(Autor.nome) ilike(sem_acentos(?))) ");
    	sqlStr.append(" or not exists (select Autor.nome from CatalogoAutor inner JOIN Autor on Autor.codigo = CatalogoAutor.autor where CatalogoAutor.catalogo = Catalogo.codigo)) ");
    	if(Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo and exemplar.biblioteca = ").append(biblioteca).append(" limit 1) ");
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(biblioteca)) {    	
    		sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
    		sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
    	}
    	sqlStr.append(" ORDER BY case when  not exists (select Autor.nome from CatalogoAutor inner JOIN Autor on Autor.codigo = CatalogoAutor.autor where CatalogoAutor.catalogo = Catalogo.codigo) then 1 else 0 end, Catalogo.titulo, autores");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
    	return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }
    
    public List<CatalogoVO> consultarPorTituloTipoCatalogoAssinaturaPeriodico(String titulo, String tipo, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT * FROM catalogo WHERE sem_acentos(titulo) ILIKE sem_acentos(?) ");
    	if (tipo.equals("CATALOGO")) {
    		sb.append(" and assinaturaPeriodico = false ");
    	}
    	if (tipo.equals("PERIODICO")) {
    		sb.append(" and assinaturaPeriodico = true ");
    	}
    	if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sb.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
    		sb.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
    	}
    	sb.append(" ORDER BY Catalogo.titulo");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+titulo+"%");
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<CatalogoVO> consultarRichModalSituacaoExemplarRel(String valorConsulta, String campoConsulta, Integer codigoBiblioteca, Integer codigoSessao,String situacao, String nivelBibliografico, UsuarioVO usuario) throws NumberFormatException,Exception {
    	List<CatalogoVO> objs = new ArrayList<CatalogoVO>(0);
    	StringBuilder sql = new StringBuilder();
    	String where  = " WHERE biblioteca.codigo = "+codigoBiblioteca;
    	sql.append("SELECT distinct catalogo.* from catalogo ");
    	sql.append("inner join exemplar on exemplar.catalogo = catalogo.codigo ");
    	sql.append("inner join biblioteca on exemplar.biblioteca = biblioteca.codigo ");
    	if(codigoSessao != null && codigoSessao > 0){
    		sql.append("inner join secao on exemplar.secao = secao.codigo ");
    		where+=" and secao.codigo = "+codigoSessao;
    	}
    	if(nivelBibliografico != null && !nivelBibliografico.equals("") && !nivelBibliografico.equals("TODOS")){
    		where+=" and catalogo.nivelbibliografico = '"+nivelBibliografico+"'";
    	}
    	if(situacao != null && !situacao.equals("") && !situacao.equals("0")){
    		where+=" and exemplar.situacaoatual = '"+situacao+"'";
    	}
    	if(campoConsulta != null && campoConsulta.equals("titulo")){
    		where+=" and catalogo.titulo ilike '%"+valorConsulta+"%' ORDER BY catalogo.titulo";
    	}
    	else{
    		if(campoConsulta != null && campoConsulta.equals("codigo") && !valorConsulta.equals("")){
      		where+=" and catalogo.codigo = "+Integer.parseInt(valorConsulta);
      		}
      	}
    	sql.append(where);
       	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	objs = ((List<CatalogoVO>) montarDadosConsulta(tabelaResultado,Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
       	return objs;
    }

    public List consultarPorAssunto(String assunto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM catalogo WHERE LOWER(assunto) LIKE '%" + assunto.toLowerCase() + "%' ORDER BY titulo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorAutorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM catalogo c LEFT JOIN catalogoAutor ca ON c.codigo = ca.catalogo LEFT JOIN autor a ON a.codigo = ca.autor "
                + "WHERE LOWER (nome) LIKE '%" + valorConsulta.toLowerCase() + "%' ORDER BY titulo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorPalavraChave(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM catalogo LEFT JOIN titulo ON catalogo.titulo = titulo.codigo LEFT JOIN titulopalavraschave ON titulo.codigo = titulopalavraschave.titulo "
                + "WHERE UPPER (titulopalavraschave.palavrachave) LIKE '%" + valorConsulta.toUpperCase() + "%' ORDER BY titulo.titulo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public CatalogoVO consultarPorIsbnIssn(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM catalogo WHERE isbn = '" + valorConsulta + "' OR issn = '" + valorConsulta + "' ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Catalogo ).");
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);

    }

    public List consultarTelaBuscaCatalogosAvancada(String codigoTombo, String titulo, String assunto, String palavrasChave, String autores, String classificacao, String isbn, String issn, int biblioteca, int areaconhecimento, String cutterPhaBuscaAvancada, int nivelMontarDados, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception {
        if (codigoTombo == null && titulo.trim().isEmpty() && assunto.trim().isEmpty() && palavrasChave.trim().isEmpty() && classificacao.trim().isEmpty() && autores.trim().isEmpty() && isbn.trim().isEmpty() && issn.trim().isEmpty() && cutterPhaBuscaAvancada.trim().isEmpty()) {
            throw new Exception("Para efetuar a consulta deve ser informado no mínimo 1 parametro. ");
        }
        StringBuilder sqlStr = new StringBuilder("select c.codigo, c.classificacaoBibliografica, c.titulo, c.subtitulo, c.edicao, c.assunto, c.isbn, c.issn, c.cutterpha, c.anopublicacao, e.local, c.link, c.enviadoEbsco , cidadepublicacaocatalogo.nome as cidadepublicacao, ");
		
        sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo and ex.situacaoatual = 'DI'  and ex.biblioteca = ").append(biblioteca).append("  and ex.local = e.local and (ex.paraconsulta = false or ex.paraconsulta is null)) as nrexemplaresdisponivel, ");
		 
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo and ex.local = e.local  and ex.biblioteca = ").append(biblioteca).append("  and (ex.situacaoatual in ('DI','CO'))) as nrexemplaresemprestimo,  ");
		  
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo and ex.local = e.local  and ex.biblioteca = ").append(biblioteca).append("  and ex.situacaoatual = 'EM') as nrexemplaresemprestado,  ");
 
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo and ex.local = e.local  and ex.biblioteca = ").append(biblioteca).append("  and (ex.situacaoatual in ('DI', 'CO') and ex.paraconsulta = true)) as nrexemplaresparaconsulta,  ");
        sqlStr.append(" editora.codigo AS \"editora.codigo\", editora.nome AS \"editora.nome\", b.nome AS \"biblioteca.nome\" ");
        sqlStr.append(" FROM catalogo c ");
        sqlStr.append(" LEFT JOIN catalogoautor ca on ca.catalogo = c.codigo ");
        sqlStr.append(" LEFT JOIN autor a on ca.autor = a.codigo ");
        sqlStr.append(" LEFT JOIN autorvariacaonome avn on avn.autor = a.codigo ");
        sqlStr.append(" LEFT JOIN exemplar e on e.catalogo = c.codigo ");
        sqlStr.append(" LEFT JOIN editora on editora.codigo = c.editora ");
        sqlStr.append(" LEFT JOIN biblioteca b on e.biblioteca = b.codigo ");
        sqlStr.append(" LEFT JOIN catalogoareaconhecimento cac on cac.catalogo = c.codigo ");
        sqlStr.append(" LEFT JOIN cidadepublicacaocatalogo ON c.cidadepublicacao = cidadepublicacaocatalogo.codigo "); 
        if (biblioteca != 0 ) {
        sqlStr.append("where b.codigo = ").append(biblioteca);
		}
        sqlStr.append(" and e.situacaoatual in ('DI', 'EM', 'CO') ");
        if (!autores.equals("")) {
            sqlStr.append(" and lower(sem_acentos(a.nome)) ilike lower(sem_acentos('").append(autores).append("%'))  ");
            sqlStr.append(" or lower(sem_acentos(avn.variacaonome)) ilike lower(sem_acentos('").append(autores).append("%')) ");
        }
        if(!classificacao.equals("")) {
        	sqlStr.append(" and lower(sem_acentos(c.classificacaobibliografica)) ilike lower(sem_acentos('%").append(classificacao).append("%')) ");
        }
        if (!titulo.equals("")) {
            sqlStr.append(" and lower(sem_acentos(c.titulo)) ilike lower(sem_acentos('%").append(titulo).append("%')) ");
        }
        if (!assunto.equals("")) {
            sqlStr.append(" and lower(sem_acentos(c.assunto)) ilike lower(sem_acentos('%").append(assunto).append("%'))  ");
        }
        if (!palavrasChave.equals("")) {
            sqlStr.append(" and lower(sem_acentos(c.palavraschave)) ilike lower(sem_acentos('%").append(palavrasChave).append("%')) ");
        }
        if (Uteis.isAtributoPreenchido(codigoTombo)) {
            sqlStr.append(" AND e.codigobarra::NUMERIC(20,0) = '").append(codigoTombo).append("'::NUMERIC(20,0) ");
        }
        if (!isbn.equals("")) {
            sqlStr.append("and lower(c.isbn) ilike '").append(isbn).append("%'  ");
        }
        if (!issn.equals("")) {
            sqlStr.append("and lower(c.issn) ilike '").append(issn).append("%'  ");
        }
        if (!cutterPhaBuscaAvancada.equals("")) {
        	sqlStr.append("and lower(c.cutterpha) ilike '").append(cutterPhaBuscaAvancada).append("%'  ");
        }
        if (areaconhecimento > 0) {
        	sqlStr.append("and cac.areaconhecimento = ").append(areaconhecimento).append(" ");
        }
        if (apenasExemplarDisponivelParaEmprestimo) {
			sqlStr.append(" and e.situacaoatual = 'DI' and ((paraConsulta = false or paraConsulta is null) or (paraConsulta and emprestarSomenteFinalDeSemana and EXTRACT(DOW FROM current_date)::int = 5)) ");
		}
        sqlStr.append(" group by c.codigo, c.titulo, c.subtitulo, c.edicao, c.assunto, c.isbn, c.issn, c.cutterpha, c.anopublicacao, e.local, c.nrexemplaresparaconsulta, editora.codigo, editora.nome, b.nome, cidadepublicacaocatalogo.codigo ");
        sqlStr.append(" order by c.edicao desc, c.titulo, c.anopublicacao ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsultaTelaBusca(tabelaResultado, usuario));
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Catalogo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * 
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CatalogoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Catalogo WHERE codigo = ? ");
        if(Uteis.isAtributoPreenchido(biblioteca)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo and exemplar.biblioteca = ").append(biblioteca).append(" limit 1) ");
        }
        if(Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(biblioteca)) {
        	
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        sqlStr.append(" ORDER BY codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.intValue());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CatalogoVO> consultarPorCodigoTipoCatalogoAssinaturaPeriodica(Integer valorConsulta, String tipo, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sb = new StringBuilder();
    	sb.append(" SELECT * FROM Catalogo WHERE codigo = ").append(valorConsulta.intValue());
    	if (tipo.equals("CATALOGO")) {
    		sb.append(" and (assinaturaPeriodico is null or assinaturaPeriodico = false) ");
    	}
    	if (tipo.equals("PERIODICO")) {
    		sb.append(" and assinaturaPeriodico = true ");
    	}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sb.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
			sb.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
		}
    	sb.append(" ORDER BY codigo ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     * 
     * @return List Contendo vários objetos da classe <code>CatalogoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>CatalogoVO</code> .
     * 
     * @return O objeto da classe <code>CatalogoVO</code> com os dados devidamente montados.
     */
    public static CatalogoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CatalogoVO obj = new CatalogoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setTitulo(dadosSQL.getString("titulo"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
        	obj.setNovoObj(Boolean.FALSE);
        	return obj;
        }
        obj.setEdicao(dadosSQL.getString("edicao"));
        obj.setAutorVOs(getFacadeFactory().getAutorFacade().consultarPorCodigoCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
        	obj.setNovoObj(Boolean.FALSE);
        	return obj;
        }
        obj.setNivelBibliografico(dadosSQL.getString("nivelBibliografico"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("abreviacaoTitulo"));
        obj.setOcultar(dadosSQL.getBoolean("ocultar"));
        obj.getTipoCatalogo().setCodigo(dadosSQL.getInt("tipoCatalogo"));
        obj.setDataCadastro(dadosSQL.getDate("dataCadastro"));
        obj.setDataUltimaAtualizacao(dadosSQL.getDate("dataUltimaAtualizacao"));
        obj.setNrPaginas(dadosSQL.getString("nrPaginas"));
        obj.setNrExemplaresParaConsulta(dadosSQL.getInt("nrExemplaresParaConsulta"));
//        obj.setVolume(dadosSQL.getString("volume"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setSerie(dadosSQL.getString("serie"));
        obj.setIsbn(dadosSQL.getString("isbn"));
        obj.setIssn(dadosSQL.getString("issn"));
        obj.setNotas(dadosSQL.getString("notas"));
        obj.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
//        obj.setConfiguracaoEspecifica(dadosSQL.getBoolean("configuracaoEspecifica"));
//        obj.getConfiguracaoBiblioteca().setCodigo(dadosSQL.getInt("configuracaoBiblioteca"));
        //obj.setTitulo(dadosSQL.getString("titulo"));
        obj.setEnviadoEbsco(dadosSQL.getBoolean("enviadoEbsco"));
        obj.setSubtitulo(dadosSQL.getString("subtitulo"));
        obj.setAssunto(dadosSQL.getString("assunto"));
        obj.setClassificacaoBibliografica(dadosSQL.getString("classificacaoBibliografica"));
        obj.setVersaoDigital(dadosSQL.getBoolean("versaoDigital"));
        obj.setCutterPha(dadosSQL.getString("cutterPha"));
        obj.setPalavrasChave(dadosSQL.getString("palavrasChave"));
        obj.getLinguaPublicacaoCatalogo().setCodigo(dadosSQL.getInt("linguaPublicacao"));
        obj.getCidadePublicacaoCatalogo().setCodigo(dadosSQL.getInt("cidadePublicacao"));
        obj.getEditora().setCodigo(dadosSQL.getInt("editora"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.getResponsavelAtualizacao().setCodigo(dadosSQL.getInt("responsavelAtualizacao"));
        obj.setControlaTempoDefasagem(dadosSQL.getBoolean("controlatempodefasagem"));
        if (obj.getControlaTempoDefasagem()) {
            obj.setBibliograficaComplementarMes(dadosSQL.getInt("bibliograficacomplementarmes"));
            obj.setBibliograficaBasicaMes(dadosSQL.getInt("bibliograficabasicames"));
        }
        obj.setDataInicioAssinatura(dadosSQL.getDate("dataInicioAssinatura"));
        obj.setDataFinalAssinatura(dadosSQL.getDate("dataFinalAssinatura"));
        obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
        obj.setCdu(dadosSQL.getString("cdu"));
        obj.setSituacaoAssinatura(dadosSQL.getString("situacaoAssinatura"));
        obj.setAssinaturaPeriodico(dadosSQL.getBoolean("assinaturaPeriodico"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	montarDadosTipoCatalogo(obj, nivelMontarDados, usuario);       
            return obj;
        }
        obj.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, usuario));
        montarDadosTipoCatalogo(obj, nivelMontarDados, usuario);
        montarDadosEditora(obj, nivelMontarDados, usuario);
        montarDadosLinguaPublicacao(obj, nivelMontarDados, usuario);
        montarDadosCidadePublicacao(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarListaArquivos(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        montarDadosResponsavelAtualizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public static void montarListaArquivos(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj == null || obj.getCodigo().equals(0)) {
            return;
        }
        obj.setArquivoVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(),
                OrigemArquivo.OBRA_DIGITAL.getValor(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        obj.setArquivoSumarioCapaVOs(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(),
                OrigemArquivo.OBRA_SUMARIO.getValor(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    public static void montarDadosTipoCatalogo(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTipoCatalogo().getCodigo().intValue() == 0) {
            obj.setTipoCatalogo(new TipoCatalogoVO());
            return;
        }
        obj.setTipoCatalogo(getFacadeFactory().getTipoCatalogoFacade().consultarPorChavePrimaria(obj.getTipoCatalogo().getCodigo(),
                nivelMontarDados, usuario));
    }

    public static void montarDadosLinguaPublicacao(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getLinguaPublicacaoCatalogo().getCodigo().intValue() == 0) {
            obj.setLinguaPublicacaoCatalogo(new LinguaPublicacaoCatalogoVO());
            return;
        }
        obj.setLinguaPublicacaoCatalogo(getFacadeFactory().getLinguaPublicacaoCatalogoFacade().consultarPorChavePrimaria(
                obj.getLinguaPublicacaoCatalogo().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosCidadePublicacao(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCidadePublicacaoCatalogo().getCodigo().intValue() == 0) {
            obj.setCidadePublicacaoCatalogo(new CidadePublicacaoCatalogoVO());
            return;
        }
        obj.setCidadePublicacaoCatalogo(getFacadeFactory().getCidadePublicacaoCatalogoFacade().consultarPorChavePrimaria(
                obj.getCidadePublicacaoCatalogo().getCodigo(), nivelMontarDados, usuario));
    }

//    public static void montarDadosConfiguracaoBiblioteca(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        if (obj.getConfiguracaoBiblioteca().getCodigo().intValue() == 0) {
//            obj.setConfiguracaoBiblioteca(new ConfiguracaoBibliotecaVO());
//            return;
//        }
//        obj.setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(
//                obj.getConfiguracaoBiblioteca().getCodigo(), nivelMontarDados, usuario));
//    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto
     * <code>CatalogoVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavelAtualizacao(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavelAtualizacao().getCodigo().intValue() == 0) {
            obj.setResponsavelAtualizacao(new UsuarioVO());
            return;
        }
        obj.setResponsavelAtualizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(
                obj.getResponsavelAtualizacao().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto
     * <code>CatalogoVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosResponsavel(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(),
                nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>EditoraVO</code> relacionado ao objeto
     * <code>CatalogoVO</code>. Faz uso da chave primária da classe <code>EditoraVO</code> para realizar a consulta.
     * 
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosEditora(CatalogoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getEditora().getCodigo().intValue() == 0) {
            obj.setEditora(new EditoraVO());
            return;
        }
        obj.setEditora(getFacadeFactory().getEditoraFacade().consultarPorChavePrimaria(obj.getEditora().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>CatalogoVO</code> através de sua chave primária.
     * 
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CatalogoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql = new StringBuilder("SELECT Catalogo.* FROM Catalogo WHERE codigo = ?");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sql.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sql.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Catalogo).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Catalogo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Catalogo.idEntidade = idEntidade;
    }

    public void adicionarArquivoLista(ArquivoVO arquivoVO, CatalogoVO catalogoVO) throws Exception {
        catalogoVO.getArquivoVOs().add(arquivoVO);
    }

    public void adicionarArquivoListaSumarioCapa(ArquivoVO arquivoVO, CatalogoVO catalogoVO) throws Exception {
        catalogoVO.getArquivoSumarioCapaVOs().add(arquivoVO);
    }

	public List<CatalogoVO> consultarTelaBuscaCatalogos(String valorConsulta, Integer bibioteca, Integer limite, Integer offset, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception {
		if (valorConsulta.length() < 2) {
			throw new Exception("Para efetuar a consulta deve ser informado no mínimo 2 caracteres. ");
		}
		StringBuilder sqlStr = new StringBuilder("select c.codigo, c.classificacaoBibliografica, c.titulo, c.subtitulo, c.edicao, c.assunto, c.isbn, c.issn, c.cutterPha, c.anopublicacao, e.local, b.nome AS \"biblioteca.nome\", c.link, c.enviadoEbsco , cidadepublicacaocatalogo.nome as cidadepublicacao, ");
		
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo and ex.situacaoatual = 'DI'  and ex.local = e.local and (ex.paraconsulta = false or ex.paraconsulta is null)  and ex.biblioteca = ").append(bibioteca).append(" ) as nrexemplaresdisponivel, ");
		 
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo  and ex.local = e.local and (ex.situacaoatual in ('DI','CO')) and ex.biblioteca = ").append(bibioteca).append(" ) as nrexemplaresemprestimo,  ");
		  
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo  and ex.local = e.local and ex.situacaoatual = 'EM'  and ex.biblioteca = ").append(bibioteca).append(" ) as nrexemplaresemprestado,  ");
 
		sqlStr.append("(select count(distinct ex.codigo) from exemplar ex where ex.catalogo = c.codigo  and ex.local = e.local and (ex.situacaoatual in ('DI', 'CO') and ex.paraconsulta = true)  and ex.biblioteca = ").append(bibioteca).append(" ) as nrexemplaresparaconsulta,  ");
		sqlStr.append("editora.codigo AS \"editora.codigo\", editora.nome AS \"editora.nome\" ");
		sqlStr.append("from catalogo c ");
		sqlStr.append("left join catalogoautor ca on ca.catalogo = c.codigo ");
		sqlStr.append("left join autor a on ca.autor = a.codigo ");
		sqlStr.append("left join autorvariacaonome avn on avn.autor = a.codigo ");
		sqlStr.append("left join exemplar e on e.catalogo = c.codigo ");
		sqlStr.append("left join editora on editora.codigo = c.editora ");
		sqlStr.append("left join biblioteca b on e.biblioteca = b.codigo ");
        sqlStr.append(" LEFT JOIN cidadepublicacaocatalogo ON c.cidadepublicacao = cidadepublicacaocatalogo.codigo "); 
		sqlStr.append("where (sem_acentos(lower(a.nome)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(avn.variacaonome)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(c.titulo)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(c.assunto)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(c.palavraschave)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(c.isbn)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("or sem_acentos(lower(c.issn)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))) and  ");
		sqlStr.append("e.biblioteca= " + bibioteca + "");
		sqlStr.append(" and e.situacaoatual in ('DI', 'EM', 'CO') ");
		if (apenasExemplarDisponivelParaEmprestimo) {
			sqlStr.append(" and e.situacaoatual = 'DI' and ((paraConsulta = false or paraConsulta is null) or (paraConsulta and emprestarSomenteFinalDeSemana and EXTRACT(DOW FROM current_date)::int = 5)) ");
		}
		sqlStr.append("group by c.edicao, c.codigo, c.titulo, c.subtitulo, c.assunto, c.isbn, c.issn, e.local, c.anopublicacao, editora.nome, editora.codigo, b.nome, cidadepublicacaocatalogo.codigo ");
		sqlStr.append(" order by c.edicao desc, c.titulo, c.anopublicacao ");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return (montarDadosConsultaTelaBusca(tabelaResultado, usuario));
		} finally {
			sqlStr = null;
		}
	}

    public Integer consultaTotalDeRegistroRapidaPorBuscaCatalogo(String valorConsulta, Integer bibioteca, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" select count(t.codigo) from ( ");
		sqlStr.append("SELECT c.codigo ");
        sqlStr.append("from catalogo c ");
        sqlStr.append("left join catalogoautor ca on ca.catalogo = c.codigo ");
        sqlStr.append("left join autor a on ca.autor = a.codigo ");
        sqlStr.append("left join autorvariacaonome avn on avn.autor = a.codigo ");
        sqlStr.append("left join exemplar e on e.catalogo = c.codigo ");
        sqlStr.append("left join editora on editora.codigo = c.editora ");
        sqlStr.append("left join biblioteca b on e.biblioteca = b.codigo ");
        sqlStr.append("where (sem_acentos(lower(a.nome)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(avn.variacaonome)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(c.titulo)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(c.assunto)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(c.palavraschave)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(c.isbn)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))  ");
        sqlStr.append("or sem_acentos(lower(c.issn)) like (sem_acentos('%").append(valorConsulta.toLowerCase()).append("%'))) and  ");
        sqlStr.append("e.biblioteca = " + bibioteca + "");
        sqlStr.append(" and e.situacaoatual in ('DI', 'EM', 'CO') ");
        if (apenasExemplarDisponivelParaEmprestimo) {
			sqlStr.append(" and e.situacaoatual = 'DI' and ((paraConsulta = false or paraConsulta is null) or (paraConsulta and emprestarSomenteFinalDeSemana and EXTRACT(DOW FROM current_date)::int = 5)) ");
		}
        sqlStr.append("group by c.edicao, c.codigo, c.titulo, c.subtitulo, c.assunto, c.isbn, c.issn, e.local, c.anopublicacao, editora.nome, editora.codigo, b.nome ");
		sqlStr.append(" ) as t ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

//    @Transactional(readOnly = true)
//    public List executarConsultaTelaBuscaCatalogos(String valorConsulta, UsuarioVO usuarioLogado, Boolean apenasExemplarDisponivel) throws Exception {
//        try {
//
//            List retorno = new ArrayList(0);
//
//            if (valorConsulta.trim().length() < 3) {
//                throw new Exception("Para efetuar a consulta deve ser informado no mínimo 3 caracteres. ");
//            }
//
//            StringBuilder valor = new StringBuilder(0);
//            String[] termos = valorConsulta.split("\\s");
//            if (termos.length == 1) {
//                valor.append(valorConsulta).append("~");
//            } else {
//                for (String str : termos) {
//                    valor.append(str).append("~ ");
//                }
//            }
//
//            String campos[] = new String[]{"titulo", "isbn", "issn", "subtitulo", "assunto"};
//
//            MultiFieldQueryParser query = new MultiFieldQueryParser(campos, new StandardAnalyzer());
//            query.setFuzzyMinSim(0.8f);
//            query.setDefaultOperator(Operator.OR);
//            query.setFuzzyPrefixLength(1);
//            query.setAllowLeadingWildcard(true);
//
//            org.hibernate.Query fullTextQuery = getFullTextSession().createFullTextQuery(query.parse(valor.toString()), CatalogoVO.class);
//
//
//            //List<CatalogoAutorVO> lista = fullTextQuery.list(); //return a list of managed objects
//            List<CatalogoVO> lista = fullTextQuery.list();
//            getFullTextSession().beginTransaction().commit();
//            List<Integer> listaDeCodigos = new ArrayList<Integer>(0);
//            for (CatalogoVO catalogo : lista) {
//                String sql = "select count(ex.codigo) from exemplar ex where ex.catalogo = ? and ex.situacaoatual = 'DI' ";
//                try {
//                    if (!listaDeCodigos.contains(catalogo.getCodigo())) {
//
//                        SqlRowSet tabelaResultadoNrExemplaresDisponiveis = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{catalogo.getCodigo()});
//                        tabelaResultadoNrExemplaresDisponiveis.next();
//                        catalogo.setNrExemplaresParaEmprestimo(tabelaResultadoNrExemplaresDisponiveis.getInt("count"));
//                        if (apenasExemplarDisponivel && !catalogo.getNrExemplaresParaEmprestimo().equals(0)) { // apenas unidades disponiveis
//                            retorno.add(catalogo);
//                            listaDeCodigos.add(catalogo.getCodigo());
//                        }
//                        if (!apenasExemplarDisponivel) {// unidades disponiveis e nao disponiveis
//                            retorno.add(catalogo);
//                            listaDeCodigos.add(catalogo.getCodigo());
//                        }
//                    }
//                } catch (Exception ex) {
//                    throw ex;
//                }
//            }
//            lista = null;
//            listaDeCodigos = null;
//            return retorno;
//        } catch (Exception ex) {
//            throw ex;
//        }
//    }

//     public Integer consultarNumeroDeExemplaresDisponiveisPorCatalogo(Integer codigoCatalogo) throws Exception {
//        String sqlStrNrExemplaresDisponiveis = "SELECT COUNT(codigo) FROM exemplar WHERE catalogo = ? and situacaoatual = 'DI'";
//        try {
//            SqlRowSet tabelaResultadoNrExemplaresDisponiveis = getConexao().getJdbcTemplate().queryForRowSet(sqlStrNrExemplaresDisponiveis,
//                    new Object[]{codigoCatalogo});
//            tabelaResultadoNrExemplaresDisponiveis.next();
//            return tabelaResultadoNrExemplaresDisponiveis.getInt("count");
//        } finally {
//            sqlStrNrExemplaresDisponiveis = null;
//        }
//    }
    public static List<CatalogoVO> montarDadosConsultaTelaBusca(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<CatalogoVO> vetResultado = new ArrayList<CatalogoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosTelaBusca(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static CatalogoVO montarDadosTelaBusca(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        CatalogoVO obj = new CatalogoVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setClassificacaoBibliografica(dadosSQL.getString("classificacaoBibliografica"));
        obj.setNrExemplaresParaConsulta(dadosSQL.getInt("nrExemplaresParaConsulta"));
        obj.setEdicao(dadosSQL.getString("edicao"));
        obj.setIsbn(dadosSQL.getString("isbn"));
        obj.setIssn(dadosSQL.getString("issn"));
        obj.setCutterPha(dadosSQL.getString("cutterpha"));
        obj.setTitulo(dadosSQL.getString("titulo"));
        obj.setSubtitulo(dadosSQL.getString("subtitulo"));
        obj.setAssunto(dadosSQL.getString("assunto"));
        obj.setNrExemplaresParaEmprestimo(dadosSQL.getInt("nrexemplaresemprestimo"));
        obj.setNrExemplaresDisponivel(dadosSQL.getInt("nrexemplaresdisponivel"));
        obj.setNrExemplaresEmprestado(dadosSQL.getInt("nrexemplaresemprestado"));
        obj.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
        obj.setLocal(dadosSQL.getString("local"));
        obj.getEditora().setCodigo(dadosSQL.getInt("editora.codigo"));
        obj.getEditora().setNome(dadosSQL.getString("editora.nome"));
        obj.setEnviadoEbsco(dadosSQL.getBoolean("enviadoEbsco"));
        obj.setBiblioteca(dadosSQL.getString("biblioteca.nome"));
        obj.getCidadePublicacaoCatalogo().setNome(dadosSQL.getString("cidadepublicacao"));
        obj.setLink(dadosSQL.getString("link"));
		if (Uteis.isAtributoPreenchido(obj.getLink()) && !obj.getLink().contains("http")) {
			obj.setLink("http://" + obj.getLink());
		}

        obj.setAutorVOs(getFacadeFactory().getAutorFacade().consultarPorCodigoCatalogo(obj.getCodigo(), false,
                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
        return obj;
    }

    /**
     * Método que realiza a execução da reserva do(s) catálogo(s) que estão na guia de reserva para o usuário. Primeiro
     * ele valida os seguintes aspectos: - Verifica se o usuário possui reservas para algum dos catálogos da lista no
     * período corrente. - Verifica se o número de reservas do usuário existentes para o período corrente ultrapassa o
     * limite máximo pela biblioteca. - Verifica se o usuário possui algum empréstimo de um exemplar do catálogo que ele deseja 
     * reservar. Após essas validações, os dados da reserva são montados, respeitando o prazo da
     * validade da reserva especificado na configuração da biblioteca, e um modal contendo as informações da reserva é
     * mostrado para o usuário.
     * 
     * @param listaCatalogosAdicionadosNaGuiaReserva
     * @param pessoa
     * @param confBiblioteca
     * @throws Exception
     * @author Murillo Parreira
     */
    public List<ReservaVO> executarReservaCatalogos(List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva, BibliotecaVO biblioteca, PessoaVO pessoa, String tipoPessoa, String matricula, ConfiguracaoBibliotecaVO confBiblioteca, UsuarioVO usuario) throws Exception {
		Integer numeroCatalogoReservados = 0;
		Integer numeroExemplaresDisponiveis = 0;
		Integer numeroReservasValidas = 0;
		Integer quantidadeExemplaresParaReserva = 0;
		Date dataTerminoReserva = null;
		List<ReservaVO> reservas = new ArrayList<ReservaVO>();
		
		try {
			String mensagemReserva = "";
			numeroCatalogoReservados = getFacadeFactory().getReservaFacade().consultarNumeroDeCatalogosReservadosParaDeterminadaPessoa(pessoa.getCodigo());
			if (numeroCatalogoReservados + listaCatalogosAdicionadosNaGuiaReserva.size() > confBiblioteca.getNumeroMaximoLivrosReservados()) {
				throw new Exception("Você já possui o número máximo de catálogos reservados permitidos pela biblioteca!");
			}
			for (CatalogoVO catalogo : listaCatalogosAdicionadosNaGuiaReserva) {
				numeroExemplaresDisponiveis = getFacadeFactory().getReservaFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(catalogo, biblioteca, confBiblioteca, false);
				numeroReservasValidas = getFacadeFactory().getReservaFacade().consultarQuantidadeDeReservasValidasPorCatalogo(catalogo, biblioteca);
				quantidadeExemplaresParaReserva = getFacadeFactory().getExemplarFacade().numeroExemplaresDisponiveisParaReserva(catalogo.getCodigo());
				
				if (quantidadeExemplaresParaReserva.equals(0)) {					
					mensagemReserva += "O catálogo: " + catalogo.getTitulo() + ",não pode ser reservado.";
					reservas.add(new ReservaVO());
					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);	
					continue;		
				}	
				if (getFacadeFactory().getEmprestimoFacade().verificarExisteEmprestimoParaDeterminadoCatalogo(catalogo.getCodigo(), pessoa.getCodigo(), biblioteca.getCodigo())) {
					mensagemReserva += "Você já possui empréstimo para o catálogo: " + catalogo.getTitulo() + ", portanto não pode fazer uma reserva para o mesmo!\n";
					reservas.add(new ReservaVO());
					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
					continue;
				}
				if (getFacadeFactory().getReservaFacade().verificarExistenciaReservaCatalogoParaDeterminadaPessoa(catalogo.getCodigo(), pessoa.getCodigo(), biblioteca.getCodigo())) {
					mensagemReserva += "Você já possui uma reserva ativa para o catálogo " + catalogo.getTitulo() + "!\n";
					reservas.add(new ReservaVO());
					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
					continue;
				}

                if (!confBiblioteca.getPermiteRealizarReservaCatalogoDisponivel() && numeroExemplaresDisponiveis > 0 && numeroExemplaresDisponiveis > numeroReservasValidas) {
					dataTerminoReserva = null;
					mensagemReserva += "Não é possível realizar a reserva do catálogo \"" + catalogo.getTitulo() + "\". Porque o mesmo está disponível para empréstimo.";    				
					reservas.add(new ReservaVO());
					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
    				continue;
				} else {
					if (confBiblioteca.getPermiteReserva()) {
						if ((numeroExemplaresDisponiveis - numeroReservasValidas) > 0) {
							if (confBiblioteca.getPrazoValidadeReservaCatalogosDisponiveis().equals(0)) {
		        				mensagemReserva += "Não é possível realizar a reserva do catálogo \"" + catalogo.getTitulo() + "\". Porque não está configurado a validade da reserva para catálogos disponíveis!\n";
		        				reservas.add(new ReservaVO());
		    					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
		    					continue;
							}else if (confBiblioteca.getPrazoValidadeReservaCatalogosDisponiveis().equals(0) && (numeroExemplaresDisponiveis - numeroReservasValidas) == 1) {
								mensagemReserva += "Não é possível realizar a reserva do catálogo \"" + catalogo.getTitulo() + "\". Porque só existe 01 (um) exemplar disponível.\n";
								reservas.add(new ReservaVO());
								reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
								continue;
							} else {
								Calendar data = Calendar.getInstance();
								data.add(Calendar.HOUR_OF_DAY, confBiblioteca.getPrazoValidadeReservaCatalogosDisponiveis());
								dataTerminoReserva = data.getTime();
							}
						}
						
						else {
							if (confBiblioteca.getPrazoValidadeReservaCatalogosIndisponiveis().equals(0)) {
		        				mensagemReserva += "Não é possível realizar a reserva do catálogo \"" + catalogo.getTitulo() + "\". Porque não está configurado a validade da reserva para catálogos indisponíveis!\n";
		        				reservas.add(new ReservaVO());
		    					reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
		    					continue;
		        			} 							
							else {
								Calendar data = Calendar.getInstance();
								data.add(Calendar.DAY_OF_MONTH, confBiblioteca.getPrazoValidadeReservaCatalogosIndisponiveis());
								dataTerminoReserva = data.getTime();
		        			}
						}
					}
				}

				reservas.add(getFacadeFactory().getReservaFacade().montarReserva(catalogo, pessoa, dataTerminoReserva, tipoPessoa, biblioteca, matricula, usuario));
				mensagemReserva += " Catálogo " + catalogo.getTitulo() + " reservado com sucesso.";
			}
			/* return mensagemReserva; */
			if (reservas.size() > 0) {
				reservas.get(0).setMensagemListaCatalogoReservado(mensagemReserva);
			}
			return reservas;
		} finally {
			numeroCatalogoReservados = null;
			numeroExemplaresDisponiveis = null;
		}
	}

    public List<CatalogoVO> consultaRapidaPorTipoCatalogo(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = getSQLPadraoConsultaBasica();
    	
    	sqlStr.append("LEFT JOIN tipoCatalogo on tipoCatalogo.codigo = catalogo.tipoCatalogo ");
    	sqlStr.append("WHERE sem_acentos(tipoCatalogo.nome) ilike(sem_acentos(?))");
    	sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
    	
    	if (ordenarPor.equals("edicao")) {
    		sqlStr.append(" ORDER BY catalogo.edicao ");
    	} else if (ordenarPor.equals("titulo")) {
    		sqlStr.append(" ORDER BY catalogo.titulo ");
    	} else if (ordenarPor.equals("ano")) {
    		sqlStr.append(" ORDER BY catalogo.anopublicacao ");
    	} else if (ordenarPor.equals("crescente")) {
    		sqlStr.append(" ORDER BY catalogo.codigo asc ");
    	} else if (ordenarPor.equals("decrescente")) {
    		sqlStr.append(" ORDER BY catalogo.codigo desc ");
    	} else if (ordenarPor.equals("classificacao")) {
    		sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
    	} else {
    		sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
    	}
    	
    	if (limite != null) {
    		sqlStr.append(" LIMIT ").append(limite);
    		if (offset != null) {
    			sqlStr.append(" OFFSET ").append(offset);
    		}
    	}
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
    	return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    

    public Integer consultarTotalDeGegistroPorCutterPha(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder str = new StringBuilder();
    	str.append("SELECT DISTINCT COUNT (codigo) FROM (");
    	str.append(getSQLPadraoConsultaBasica());
    	str.append(" WHERE ");    	
    	str.append(" sem_acentos(catalogo.cutterPha) ilike sem_acentos(?) ");
    	str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
    	str.append(") as t ");
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), valorConsulta != null && !valorConsulta.equals("") ? valorConsulta.trim() : "%%");
    	if (resultado.next()) {
    		return resultado.getInt("count");
    	}
    	return 0;
    }
    
    public Integer consultarTotalDeGegistroPorTipoCatalogo(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder str = new StringBuilder();
    	str.append("SELECT DISTINCT COUNT (codigo) FROM (");
    	str.append(getSQLPadraoConsultaBasica());    	
    	str.append("LEFT JOIN tipoCatalogo on tipoCatalogo.codigo = catalogo.tipoCatalogo ");
    	str.append("WHERE upper (sem_acentos(tipoCatalogo.nome)) ilike(sem_acentos(?))");
        str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
    	str.append(") as t ");
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), "%"+valorConsulta+"%");
    	if (resultado.next()) {
    		return resultado.getInt("count");
    	}
    	return 0;
    }
    
    public List<CatalogoVO> consultaRapidaPorCutterPha(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = getSQLPadraoConsultaBasica();
    	sqlStr.append(" WHERE ");
    	sqlStr.append(" sem_acentos(catalogo.cutterPha) ilike sem_acentos(?) ");    	    
    	sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
    	if (ordenarPor.equals("edicao")) {
    		sqlStr.append(" ORDER BY catalogo.edicao ");
    	} else if (ordenarPor.equals("titulo")) {
    		sqlStr.append(" ORDER BY catalogo.titulo ");
    	} else if (ordenarPor.equals("ano")) {
    		sqlStr.append(" ORDER BY catalogo.anopublicacao ");
    	} else if (ordenarPor.equals("crescente")) {
    		sqlStr.append(" ORDER BY catalogo.codigo asc ");
    	} else if (ordenarPor.equals("decrescente")) {
    		sqlStr.append(" ORDER BY catalogo.codigo desc ");
    	} else if (ordenarPor.equals("classificacao")) {
    		sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
    	} else {
    		sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
    	}
		if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta != null && !valorConsulta.equals("") ? valorConsulta.trim() : "%%");
    	return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    	
    /**
     * Método que pega a quantidade de exemplares disponíveis de um catálogo.
     * @param codigoCatalogo
     * @return
     * @throws Exception
     * @author Murillo Parreira
     */
    public Integer consultarNumeroDeExemplaresDisponiveisPorCatalogo(Integer codigoCatalogo) throws Exception {
        String sqlStrNrExemplaresDisponiveis = "SELECT COUNT(codigo) FROM exemplar WHERE catalogo = ? and situacaoatual = 'DI'";
        try {
            SqlRowSet tabelaResultadoNrExemplaresDisponiveis = getConexao().getJdbcTemplate().queryForRowSet(sqlStrNrExemplaresDisponiveis,
                    new Object[]{codigoCatalogo});
            tabelaResultadoNrExemplaresDisponiveis.next();
            return tabelaResultadoNrExemplaresDisponiveis.getInt("count");
        } finally {
            sqlStrNrExemplaresDisponiveis = null;
        }
    }

    public void carregarDados(CatalogoVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        carregarDados((CatalogoVO) obj, unidadeEnsino, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(CatalogoVO obj, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
            montarDadosBasico((CatalogoVO) obj, resultado, usuario);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
            montarDadosCompleto((CatalogoVO) obj, unidadeEnsino , resultado, usuario);

        }
    }
    
    
   
    
    
    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codExemplar, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (Catalogo.codigo= ").append(codExemplar).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codExemplar, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (Catalogo.codigo= ").append(codExemplar).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }
  
   

    private StringBuilder getSQLPadraoConsultaBasicaTotalRegistro() {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("Select COUNT(exemplar.codigo) From exemplar ");
        sqlStr.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
        sqlStr.append(" LEFT JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
        return sqlStr;
    }

    private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT catalogo.codigo, catalogo.titulo AS \"catalogo.titulo\", catalogo.edicao AS \"catalogo.edicao\", catalogo.subtitulo AS \"catalogo.subtitulo\", ");
        str.append(" catalogo.anopublicacao AS \"catalogo.anopublicacao\", catalogo.dataCadastro AS \"catalogo.dataCadastro\", catalogo.datainicioassinatura AS \"catalogo.datainicioassinatura\", ");
        str.append(" catalogo.datafinalassinatura AS \"catalogo.datafinalassinatura\", editora.nome AS \"editora.nome\", catalogo.abreviacaoTitulo AS \"catalogo.abreviacaoTitulo\", catalogo.classificacaoBibliografica AS \"catalogo.classificacaoBibliografica\",");
        str.append(" responsavel.codigo AS \"responsavel.codigo\", responsavel.nome AS \"responsavel.nome\", catalogo.numeroControle AS \"catalogo.numeroControle\",  catalogo.enviadoEbsco as \"catalogo.enviadoEbsco\" , ");
        str.append(" editora.codigo AS \"editora.codigo\" ");
        str.append(" FROM catalogo ");
        str.append(" LEFT JOIN editora ON editora.codigo = catalogo.editora ");
        str.append(" LEFT JOIN usuario responsavel ON responsavel.codigo = catalogo.responsavel ");
        return str;
    }

    private StringBuilder getSQLPadraoConsultaCompleta() {
        StringBuilder str = new StringBuilder();
        //Dados Exemplar
        str.append("SELECT DISTINCT catalogo.codigo AS \"catalogo.codigo\", catalogo.titulo AS \"catalogo.titulo\", catalogo.dataUltimaAtualizacao AS \"catalogo.dataUltimaAtualizacao\", ");
        str.append(" catalogo.anopublicacao AS \"catalogo.anopublicacao\", catalogo.dataCadastro AS \"catalogo.dataCadastro\", catalogo.notas AS \"catalogo.notas\",  ");
        str.append(" catalogo.issn AS \"catalogo.issn\", catalogo.isbn AS \"catalogo.isbn\", catalogo.serie AS \"catalogo.serie\", catalogo.numero AS \"catalogo.numero\", ");
        str.append(" catalogo.edicao AS \"catalogo.edicao\", catalogo.versaoDigital AS \"catalogo.versaoDigital\", catalogo.nrexemplaresParaConsulta AS \"catalogo.nrexemplaresParaConsulta\", ");
        str.append(" catalogo.nivelbibliografico AS \"catalogo.nivelbibliografico\", catalogo.configuracaoEspecifica AS \"catalogo.configuracaoEspecifica\", catalogo.ocultar AS \"catalogo.ocultar\",  ");
        str.append(" catalogo.cutterpha AS \"catalogo.cutterpha\", catalogo.controlaTempoDefasagem AS \"catalogo.controleTempoDefasagem\", catalogo.bibliograficaComplementarmes AS \"catalogo.bibliograficaComplementarmes\", ");
        str.append(" catalogo.bibliograficaBasicaMes AS \"catalogo.bibliograficaBasicaMes\", catalogo.nrpaginas AS \"catalogo.nrpaginas\", catalogo.classificacaobibliografica AS \"catalogo.classificacaobibliografica\", catalogo.subtitulo AS \"catalogo.subtitulo\", catalogo.assunto AS \"catalogo.assunto\", ");
        str.append(" catalogo.palavrasChave AS \"catalogo.palavrasChave\", catalogo.datainicioassinatura AS \"catalogo.datainicioassinatura\", ");
        str.append(" catalogo.datafinalassinatura AS \"catalogo.datafinalassinatura\", catalogo.periodicidade AS \"catalogo.periodicidade\", catalogo.cdu AS \"catalogo.cdu\", ");
        str.append(" catalogo.situacaoAssinatura AS \"catalogo.situacaoAssinatura\", catalogo.assinaturaPeriodico AS \"catalogo.assinaturaPeriodico\", catalogo.abreviacaoTitulo AS \"catalogo.abreviacaoTitulo\", catalogo.link AS \"catalogo.link\",  ");
        str.append(" catalogo.linkRevista AS \"catalogo.linkRevista\", catalogo.numerocontrole as \"catalogo.numerocontrole\", linguapublicacaocatalogo.marccode as \"linguapublicacaocatalogo.marccode\",  catalogo.enviadoEbsco as \"catalogo.enviadoEbsco\" , ");
        //Dados CatalogoAutor
        str.append(" catalogoAutor.codigo AS \"catalogoAutor.codigo\", catalogoAutor.catalogo AS \"catalogoAutor.catalogo\", catalogoAutor.autor AS \"catalogoAutor.autor\", ");
        str.append(" autor.codigo AS \"autor.codigo\", autor.nome AS \"autor.nome\", ");
        //Dados Editora
        str.append(" editora.codigo AS \"editora.codigo\", editora.nome AS \"editora.nome\", ");
        //Dados Classificacao Bibliografica
        //str.append(" classificacaobibliografica.codigo AS \"classificacaobibliografica.codigo\", classificacaobibliografica.nome AS \"classificacaobibliografica.nome\", ");
        //Dados Tipo Catalogo
        str.append(" tipocatalogo.codigo AS \"tipocatalogo.codigo\", tipocatalogo.nome AS \"tipocatalogo.nome\", ");
        //Dados Responsavel Atualização
        str.append(" responsavelAtualizacao.codigo AS \"responsavelAtualizacao.codigo\", responsavelAtualizacao.nome AS \"responsavelAtualizacao.nome\", ");
        //Dados Responsavel
        str.append(" responsavel.codigo AS \"responsavel.codigo\", responsavel.nome AS \"responsavel.nome\", ");
        //Dados Configuracao Biblioteca
        str.append(" configuracaoBiblioteca.codigo AS \"configuracaoBiblioteca.codigo\", configuracaoBiblioteca.nome  AS \"configuracaoBiblioteca.nome\", ");
        //Dados Lingua Publicação
        str.append(" linguapublicacaocatalogo.codigo AS \"linguapublicacaocatalogo.codigo\", linguapublicacaocatalogo.nome AS \"linguapublicacaocatalogo.nome\", ");
        //Dados Cidade Publicação
        str.append(" cidadepublicacaocatalogo.codigo AS \"cidadepublicacaocatalogo.codigo\", cidadepublicacaocatalogo.nome AS \"cidadepublicacaocatalogo.nome\", ");
        str.append(" cidadepublicacaocatalogo.estado AS \"cidadepublicacaocatalogo.estado\",  ");
        //Dados Exemplar
        str.append(" exemplar.codigo AS \"exemplar.codigo\", biblioteca.codigo AS \"biblioteca.codigo\", biblioteca.nome AS \"biblioteca.nome\", ");
        str.append(" secao.codigo AS \"secao.codigo\", secao.nome AS \"secao.nome\", exemplar.local AS \"exemplar.local\", exemplar.codigobarra AS \"exemplar.codigobarra\", ");
        str.append(" exemplar.situacaoatual AS \"exemplar.situacaoatual\", exemplar.volume as \"exemplar.volume\" ");
        str.append(" FROM catalogo ");
        str.append(" LEFT JOIN catalogoAutor ON catalogoAutor.catalogo = catalogo.codigo ");
        str.append(" LEFT JOIN autor ON autor.codigo = catalogoautor.autor ");
        str.append(" LEFT JOIN editora ON editora.codigo = catalogo.editora ");
//        str.append(" LEFT JOIN classificacaobibliografica ON classificacaobibliografica.codigo = catalogo.classificacaobibliografica ");
        str.append(" LEFT JOIN tipocatalogo ON tipocatalogo.codigo = catalogo.tipoCatalogo ");
        str.append(" LEFT JOIN usuario responsavelAtualizacao ON responsavelAtualizacao.codigo = catalogo.responsavelAtualizacao ");
        str.append(" LEFT JOIN usuario responsavel ON responsavel.codigo = catalogo.responsavel ");
        str.append(" LEFT JOIN configuracaoBiblioteca ON configuracaoBiblioteca.codigo = catalogo.configuracaoBiblioteca ");
        str.append(" LEFT JOIN linguapublicacaocatalogo ON linguapublicacaocatalogo.codigo = catalogo.linguaPublicacao ");
        str.append(" LEFT JOIN cidadepublicacaocatalogo ON cidadepublicacaocatalogo.codigo = catalogo.cidadepublicacao ");
        str.append(" LEFT JOIN exemplar ON exemplar.catalogo = catalogo.codigo ");
        str.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
        str.append(" LEFT JOIN secao ON secao.codigo = exemplar.secao ");
        return str;
    }
    
    private StringBuilder getSQLPadraoConsultaMarcCatalogoCompleta() {
        StringBuilder str = new StringBuilder();
        //Dados Exemplar
        str.append("SELECT DISTINCT catalogo.codigo AS \"catalogo.codigo\", catalogo.titulo AS \"catalogo.titulo\", catalogo.edicao AS \"catalogo.edicao\",");
        str.append(" catalogo.subtitulo as \"catalogo.subtitulo\", catalogo.anopublicacao AS \"catalogo.anopublicacao\" ,catalogo.link AS \"catalogo.link\", catalogo.assunto AS \"catalogo.assunto\",  ");
        str.append(" catalogo.issn AS \"catalogo.issn\", catalogo.isbn AS \"catalogo.isbn\", catalogo.nrpaginas AS \"catalogo.nrpaginas\", catalogo.abreviacaoTitulo as \"catalogo.abreviacaoTitulo\",");
        str.append(" catalogo.classificacaoBibliografica as \"catalogo.classificacaoBibliografica\", responsavel.codigo as \"responsavel.codigo\",	responsavel.nome as \"responsavel.nome\",	catalogo.numeroControle as \"catalogo.numeroControle\", ");
        str.append(" catalogo.enviadoEbsco as \"catalogo.enviadoEbsco\" ,	editora.codigo as \"editora.codigo\",	editora.nome as \"editora.nome\", ");
        str.append(" tipocatalogo.codigo as \"tipocatalogo.codigo\",tipocatalogo.nome as \"tipocatalogo.nome\",	cidadepublicacaocatalogo.codigo as \"cidadepublicacaocatalogo.codigo\",");
        str.append("	cidadepublicacaocatalogo.nome  as \"cidadepublicacaocatalogo.nome\", 	linguapublicacaocatalogo.codigo as \"linguapublicacaocatalogo.codigo\",	linguapublicacaocatalogo.marccode \"linguapublicacaocatalogo.marccode\" ");
        str.append(" FROM catalogo ");     
        str.append(" LEFT JOIN editora ON editora.codigo = catalogo.editora ");
        str.append(" LEFT JOIN tipocatalogo ON tipocatalogo.codigo = catalogo.tipoCatalogo ");        
        str.append(" LEFT JOIN cidadepublicacaocatalogo ON cidadepublicacaocatalogo.codigo = catalogo.cidadepublicacao ");
        str.append(" LEFT JOIN linguapublicacaocatalogo ON linguapublicacaocatalogo.codigo = catalogo.linguaPublicacao ");       
        str.append(" LEFT JOIN usuario responsavel ON responsavel.codigo = catalogo.responsavel ");       
        return str;
    }


    private void montarDadosBasico(CatalogoVO obj, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        //Dados Catalogo
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setTitulo(dadosSQL.getString("catalogo.titulo"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("catalogo.abreviacaoTitulo"));
        obj.setEdicao(dadosSQL.getString("catalogo.edicao"));
        obj.setAnoPublicacao(dadosSQL.getString("catalogo.anopublicacao"));
        obj.setDataCadastro(dadosSQL.getDate("catalogo.dataCadastro"));
        obj.setDataInicioAssinatura(dadosSQL.getDate("catalogo.datainicioassinatura"));
        obj.setDataFinalAssinatura(dadosSQL.getDate("catalogo.datafinalassinatura"));
        obj.getEditora().setCodigo(dadosSQL.getInt("editora.codigo"));
        obj.getEditora().setNome(dadosSQL.getString("editora.nome"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
        obj.setClassificacaoBibliografica(dadosSQL.getString("catalogo.classificacaoBibliografica"));
        obj.setNumeroControle(dadosSQL.getString("catalogo.numeroControle"));
        obj.setSubtitulo(dadosSQL.getString("catalogo.subtitulo"));
        obj.setEnviadoEbsco(dadosSQL.getBoolean("catalogo.enviadoEbsco"));
        obj.setAutorVOs(getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuarioVO));
        obj.setCatalogoCursoVOs(getFacadeFactory().getCatalogoCursoFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        obj.setCatalogoAreaConhecimentoVOs(getFacadeFactory().getCatalogoAreaConhecimentoFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
    }

    private void montarDadosCompleto(CatalogoVO obj, Integer unidadeEnsino, SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
        //Dados Catalogo
        obj.setCodigo(dadosSQL.getInt("catalogo.codigo"));
        obj.setTitulo(dadosSQL.getString("catalogo.titulo"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("catalogo.abreviacaoTitulo"));
        obj.setDataUltimaAtualizacao(dadosSQL.getDate("catalogo.dataUltimaAtualizacao"));
        obj.setAnoPublicacao(dadosSQL.getString("catalogo.anopublicacao"));
        obj.setDataCadastro(dadosSQL.getDate("catalogo.dataCadastro"));
        obj.setNotas(dadosSQL.getString("catalogo.notas"));
        obj.setIssn(dadosSQL.getString("catalogo.issn"));
        obj.setIsbn(dadosSQL.getString("catalogo.isbn"));
        obj.setSerie(dadosSQL.getString("catalogo.serie"));
        obj.setNumero(dadosSQL.getString("catalogo.numero"));
        obj.setEnviadoEbsco(dadosSQL.getBoolean("catalogo.enviadoEbsco"));
        obj.setEdicao(dadosSQL.getString("catalogo.edicao"));
        obj.setVersaoDigital(dadosSQL.getBoolean("catalogo.versaoDigital"));
        obj.setNrExemplaresParaConsulta(dadosSQL.getInt("catalogo.nrexemplaresParaConsulta"));
        obj.setNivelBibliografico(dadosSQL.getString("catalogo.nivelbibliografico"));
//        obj.setConfiguracaoEspecifica(dadosSQL.getBoolean("catalogo.configuracaoEspecifica"));
        obj.setOcultar(dadosSQL.getBoolean("catalogo.ocultar"));
        obj.setCutterPha(dadosSQL.getString("catalogo.cutterpha"));
        obj.setControlaTempoDefasagem(dadosSQL.getBoolean("catalogo.controleTempoDefasagem"));
        obj.setBibliograficaComplementarMes(dadosSQL.getInt("catalogo.bibliograficaComplementarmes"));
        obj.setBibliograficaBasicaMes(dadosSQL.getInt("catalogo.bibliograficaBasicaMes"));
        obj.setNrPaginas(dadosSQL.getString("catalogo.nrpaginas"));
        obj.setSubtitulo(dadosSQL.getString("catalogo.subtitulo"));
        obj.setAssunto(dadosSQL.getString("catalogo.assunto"));
        obj.setPalavrasChave(dadosSQL.getString("catalogo.palavrasChave"));
        
        obj.setDataInicioAssinatura(dadosSQL.getDate("catalogo.datainicioassinatura"));
        obj.setDataFinalAssinatura(dadosSQL.getDate("catalogo.datafinalassinatura"));
        obj.setPeriodicidade(dadosSQL.getString("catalogo.periodicidade"));
        obj.setCdu(dadosSQL.getString("catalogo.cdu"));
        obj.setSituacaoAssinatura(dadosSQL.getString("catalogo.situacaoAssinatura"));
        obj.setAssinaturaPeriodico(dadosSQL.getBoolean("catalogo.assinaturaPeriodico"));
        obj.setLink(dadosSQL.getString("catalogo.link"));
        obj.setNumeroControle(dadosSQL.getString("catalogo.numerocontrole"));
        
        //Dados Editora
        obj.getEditora().setCodigo(dadosSQL.getInt("editora.codigo"));
        obj.getEditora().setNome(dadosSQL.getString("editora.nome"));
        //Dados Classificação Bibliográfica
        obj.setClassificacaoBibliografica(dadosSQL.getString("catalogo.classificacaobibliografica"));
        //        obj.getClassificacaoBibliografica().setCodigo(dadosSQL.getInt("classificacaobibliografica.codigo"));
//        obj.getClassificacaoBibliografica().setNome(dadosSQL.getString("classificacaobibliografica.nome"));
        //Dados Tipo Catalogo
        obj.getTipoCatalogo().setCodigo(dadosSQL.getInt("tipocatalogo.codigo"));
        obj.getTipoCatalogo().setNome(dadosSQL.getString("tipocatalogo.nome"));
        //Dados Responsavel Atualização
        obj.getResponsavelAtualizacao().setCodigo(dadosSQL.getInt("responsavelAtualizacao.codigo"));
        obj.getResponsavelAtualizacao().setNome(dadosSQL.getString("responsavelAtualizacao.nome"));
        //Dados Responsavel
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
        //Dados Configuração Biblioteca
//        obj.getConfiguracaoBiblioteca().setCodigo(dadosSQL.getInt("configuracaoBiblioteca.codigo"));
//        obj.getConfiguracaoBiblioteca().setNome(dadosSQL.getString("configuracaoBiblioteca.nome"));
        //Dados Lingua Publicação
        obj.getLinguaPublicacaoCatalogo().setCodigo(dadosSQL.getInt("linguapublicacaocatalogo.codigo"));
        obj.getLinguaPublicacaoCatalogo().setNome(dadosSQL.getString("linguapublicacaocatalogo.nome"));
        obj.getLinguaPublicacaoCatalogo().setMarcCode(dadosSQL.getString("linguapublicacaocatalogo.marccode"));
        //Dados Cidade Publicação
        obj.getCidadePublicacaoCatalogo().setCodigo(dadosSQL.getInt("cidadepublicacaocatalogo.codigo"));
        obj.getCidadePublicacaoCatalogo().setNome(dadosSQL.getString("cidadepublicacaocatalogo.nome"));
        obj.getCidadePublicacaoCatalogo().setEstado(dadosSQL.getString("cidadepublicacaocatalogo.estado"));
        //Dados Exemplar
        obj.setExemplarVOs(getFacadeFactory().getExemplarFacade().consultaRapidaPorCodigoCatalogo(obj.getCodigo(), unidadeEnsino, false, usuarioVO));
        //Dados Catalogo Autor
        obj.setCatalogoAutorVOs(getFacadeFactory().getCatalogoAutorFacade().consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuarioVO));
        //Dados Autor
        obj.setAutorVOs(getFacadeFactory().getAutorFacade().consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuarioVO));
        obj.setCatalogoCursoVOs(getFacadeFactory().getCatalogoCursoFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        
        //Dados Area de Conhecimento
        obj.setCatalogoAreaConhecimentoVOs(getFacadeFactory().getCatalogoAreaConhecimentoFacade().consultarPorCatalogo(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));

    }
    
    
    private void montarDadosMarcCatalogoCompleto(CatalogoVO obj,  SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
    	 //Dados Catalogo
        obj.setCodigo(dadosSQL.getInt("catalogo.codigo"));
        obj.setTitulo(dadosSQL.getString("catalogo.titulo"));
        obj.setEdicao(dadosSQL.getString("catalogo.edicao"));
        obj.setSubtitulo(dadosSQL.getString("catalogo.subtitulo"));
        obj.setAnoPublicacao(dadosSQL.getString("catalogo.anopublicacao"));
        obj.setLink(dadosSQL.getString("catalogo.link"));
        obj.setAssunto(dadosSQL.getString("catalogo.assunto"));
        obj.setIsbn(dadosSQL.getString("catalogo.isbn"));
        obj.setIssn(dadosSQL.getString("catalogo.issn"));
        obj.setNrPaginas(dadosSQL.getString("catalogo.nrpaginas"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("catalogo.abreviacaoTitulo"));
        obj.setClassificacaoBibliografica(dadosSQL.getString("catalogo.classificacaobibliografica"));
        obj.setNumeroControle(dadosSQL.getString("catalogo.numerocontrole"));
        obj.setEnviadoEbsco(dadosSQL.getBoolean("catalogo.enviadoEbsco"));       
        obj.getEditora().setCodigo(dadosSQL.getInt("editora.codigo"));
        obj.getEditora().setNome(dadosSQL.getString("editora.nome"));
        obj.getTipoCatalogo().setCodigo(dadosSQL.getInt("tipocatalogo.codigo"));
        obj.getTipoCatalogo().setNome(dadosSQL.getString("tipocatalogo.nome"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
        obj.getLinguaPublicacaoCatalogo().setCodigo(dadosSQL.getInt("linguapublicacaocatalogo.codigo"));    
        obj.getLinguaPublicacaoCatalogo().setMarcCode(dadosSQL.getString("linguapublicacaocatalogo.marccode"));        
        obj.getCidadePublicacaoCatalogo().setCodigo(dadosSQL.getInt("cidadepublicacaocatalogo.codigo"));
        obj.getCidadePublicacaoCatalogo().setNome(dadosSQL.getString("cidadepublicacaocatalogo.nome"));
        obj.setCatalogoAutorVOs(getFacadeFactory().getCatalogoAutorFacade().consultaRapidaNivelComboBoxPorCodigoCatalogo(obj.getCodigo(), usuarioVO));
        
    }

    public List<CatalogoVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List<CatalogoVO> vetResultado = new ArrayList<CatalogoVO>(0);
        while (tabelaResultado.next()) {
            CatalogoVO obj = new CatalogoVO();
            montarDadosBasico(obj, tabelaResultado, usuario);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE catalogo.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        
        if(assinaturaPeriodico){
       	sqlStr.append(" and (catalogo.assinaturaPeriodico = true) ");
       } else {
       	sqlStr.append(" and (catalogo.assinaturaPeriodico = false or catalogo.assinaturaPeriodico is null) ");
       } 
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        
        sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    
    public List<CatalogoVO> consultaRapidaPorTituloCatalogo(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(); 
        
        sqlStr.append(" WHERE sem_acentos(trim(catalogo.titulo)) ilike(trim(sem_acentos(?))) ");
        sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
        
        if (ordenarPor.equals("edicao")) {
        	sqlStr.append(" ORDER BY catalogo.edicao ");
        } else if (ordenarPor.equals("titulo")) {
        	sqlStr.append(" ORDER BY catalogo.titulo ");
        } else if (ordenarPor.equals("ano")) {
        	sqlStr.append(" ORDER BY catalogo.anopublicacao ");
        } else if (ordenarPor.equals("crescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo asc ");
        } else if (ordenarPor.equals("decrescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo desc ");
        } else if (ordenarPor.equals("classificacao")) {
        	sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
        } else {
        	sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        }
        
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
        return montarDadosConsultaRapida(tabelaResultado, usuario);
    }

    public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT codigo, titulo FROM catalogo ");
        sqlStr.append("WHERE catalogo.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append(" ORDER BY catalogo.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            CatalogoVO obj = new CatalogoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setTitulo(tabelaResultado.getString("titulo"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT codigo, titulo FROM catalogo ");
        sqlStr.append("WHERE catalogo.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        
        if(assinaturaPeriodico){
       	sqlStr.append(" and (assinaturaPeriodico = true) ");
       } else {
       	sqlStr.append(" and (assinaturaPeriodico = false or assinaturaPeriodico is null) ");
       } 
        
        sqlStr.append(" ORDER BY catalogo.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            CatalogoVO obj = new CatalogoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setTitulo(tabelaResultado.getString("titulo"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<CatalogoVO> consultaRapidaPorTituloCatalogo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT DISTINCT codigo, titulo FROM catalogo ");
        sqlStr.append(" WHERE sem_acentos(lower(catalogo.titulo)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        
        sqlStr.append(" ORDER BY catalogo.titulo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            CatalogoVO obj = new CatalogoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setTitulo(tabelaResultado.getString("titulo"));
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    public List<CatalogoVO> consultaRapidaPorNomeEditora(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append("WHERE sem_acentos(trim(editora.nome)) ilike(trim(sem_acentos(?)))")
        	.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
        
        if (ordenarPor.equals("edicao")) {
        	sqlStr.append(" ORDER BY catalogo.edicao ");
        } else if (ordenarPor.equals("titulo")) {
        	sqlStr.append(" ORDER BY catalogo.titulo ");
        } else if (ordenarPor.equals("ano")) {
        	sqlStr.append(" ORDER BY catalogo.anopublicacao ");
        } else if (ordenarPor.equals("crescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo asc ");
        } else if (ordenarPor.equals("decrescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo desc ");
        } else if (ordenarPor.equals("classificacao")) {
        	sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
        } else {
        	sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        }

        
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
        return montarDadosConsultaRapida(tabelaResultado, usuario);
    }

    public Integer consultarTotalDeGegistroPorCodigoCatalogo(Integer valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT(Catalogo.codigo) FROM Catalogo ");
        str.append(" LEFT JOIN catalogoAutor ON catalogoAutor.catalogo = catalogo.codigo ");
        str.append(" LEFT JOIN autor ON autor.codigo = catalogoautor.autor ");
        str.append(" LEFT JOIN editora ON editora.codigo = catalogo.editora ");
        str.append("WHERE (catalogo.assinaturaPeriodico = false or catalogo.assinaturaPeriodico is null) and catalogo.codigo = ");
        str.append(valorConsulta.intValue());
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	str.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	str.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    
    public Integer consultarTotalDeRegistroPorCodigoCatalogo(Integer valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT(Catalogo.codigo) FROM Catalogo ");
        str.append(" LEFT JOIN editora ON editora.codigo = catalogo.editora ");
        str.append(" LEFT JOIN usuario responsavel ON responsavel.codigo = catalogo.responsavel ");      
        str.append("WHERE (catalogo.assinaturaPeriodico = false or catalogo.assinaturaPeriodico is null) and catalogo.codigo = ");
        str.append(valorConsulta.intValue());
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	str.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ");
        	str.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) limit 1) ");
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    public Integer consultarTotalDeGegistroPorTituloCatalogo(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT (codigo) FROM (");
        str.append(getSQLPadraoConsultaBasica());
        str.append("WHERE sem_acentos(trim(catalogo.titulo)) ilike(trim(sem_acentos(?))) ");
        str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, false, usuario));        
        str.append(" ) AS t");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), "%"+valorConsulta+"%");
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultarTotalDeGegistroPorNomeEditora(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT (codigo) FROM (");
        str.append(getSQLPadraoConsultaBasica());
        str.append("WHERE sem_acentos(trim(editora.nome)) ilike(trim(sem_acentos(?)))");
        str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, false, usuario));
        str.append(" ) AS t");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), "%"+valorConsulta+"%");
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    
    @Override
    public Integer consultarTotalDeGegistroPorAssunto(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT (codigo) FROM (");
        str.append(getSQLPadraoConsultaBasica());
        str.append("WHERE sem_acentos(trim(catalogo.assunto)) ilike(trim(sem_acentos(?))) ");
        str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
		str.append(") as t ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), "%"+valorConsulta+"%");
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    
    @Override
    public Integer consultarTotalDeGegistroPorClassificacao(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT COUNT (codigo) FROM (");
        str.append(getSQLPadraoConsultaBasica());
        str.append("WHERE sem_acentos(catalogo.classificacaobibliografica) ilike sem_acentos(?) ");
        str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
		str.append(") as t ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), valorConsulta.trim());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    

    @Override
    public List<CatalogoVO> consultarPorBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select catalogo.* from catalogo ");
        sqlStr.append(" where exists (select exemplar.codigo from exemplar inner join  biblioteca on exemplar.biblioteca =biblioteca.codigo where exemplar.catalogo = catalogo.codigo ");
        sqlStr.append(" and sem_acentos( biblioteca.nome) ilike sem_acentos(?) ");        
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
          	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }		
        sqlStr.append(" limit 1) ");                
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public void adicionarExemplares(CatalogoVO catalogo, ExemplarVO obj) throws Exception {        
        int index = 0;
        Iterator i = catalogo.getExemplarVOs().iterator();
        while (i.hasNext()) {
            ExemplarVO objExistente = (ExemplarVO) i.next();
            if (objExistente.getNumeroEdicao().equals(obj.getNumeroEdicao())) {
            	catalogo.getExemplarVOs().set(index, obj);
                return;
            }
            index++;
        }        
        catalogo.getExemplarVOs().add(obj);
    }
    
    @Override
	public List<CatalogoVO> consultaRapidaPorNomeAutor(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN CatalogoAutor on CatalogoAutor.catalogo = catalogo.codigo ");
		sqlStr.append(" LEFT JOIN Autor on Autor.codigo = CatalogoAutor.autor ");
		sqlStr.append(" WHERE sem_acentos(trim(Autor.nome)) ilike (trim(sem_acentos(?)))");		
		sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
        if (ordenarPor.equals("edicao")) {
        	sqlStr.append(" ORDER BY catalogo.edicao ");
        } else if (ordenarPor.equals("titulo")) {
        	sqlStr.append(" ORDER BY catalogo.titulo ");
        } else if (ordenarPor.equals("ano")) {
        	sqlStr.append(" ORDER BY catalogo.anopublicacao ");
        } else if (ordenarPor.equals("crescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo asc ");
        } else if (ordenarPor.equals("decrescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo desc ");
        } else if (ordenarPor.equals("classificacao")) {
        	sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
        } else {
        	sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        }

		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}
    
    @Override
    public List<CatalogoVO> consultaRapidaPorTombo(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		List<String> parametros = new ArrayList<String>(0);
		sqlStr.append(" LEFT JOIN exemplar on exemplar.catalogo = catalogo.codigo ");
		sqlStr.append(" WHERE 1=1 ");
		if (valorConsulta != null && !valorConsulta.equals("")) {
			if(Uteis.getIsValorNumerico(valorConsulta)){
				sqlStr.append(" and (exemplar.codigobarra::NUMERIC(20,0) = ?::NUMERIC(20,2)) ");
				parametros.add(valorConsulta);
			}else{
				sqlStr.append(" and (exemplar.codigobarra::NUMERIC(20,0)::TEXT LIKE ? ");
				sqlStr.append(" or exemplar.codigobarra LIKE ?) ");
				parametros.add(valorConsulta);
				parametros.add(valorConsulta);
			}
		}
		sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
		sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    
    @Override
    public List<CatalogoVO> consultaRapidaPorAssunto(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ");
		sqlStr.append(" sem_acentos(trim(catalogo.assunto)) ilike(trim(sem_acentos(?))) ");					
		sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));

        if (ordenarPor.equals("edicao")) {
        	sqlStr.append(" ORDER BY catalogo.edicao ");
        } else if (ordenarPor.equals("titulo")) {
        	sqlStr.append(" ORDER BY catalogo.titulo ");
        } else if (ordenarPor.equals("ano")) {
        	sqlStr.append(" ORDER BY catalogo.anopublicacao ");
        } else if (ordenarPor.equals("crescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo asc ");
        } else if (ordenarPor.equals("decrescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo desc ");
        } else if (ordenarPor.equals("classificacao")) {
        	sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
        } else {
        	sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        }
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    
    @Override
    public List<CatalogoVO> consultaRapidaPorClassificacao(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ");
		sqlStr.append(" sem_acentos(trim(catalogo.classificacaobibliografica)) ilike sem_acentos(?) ");
		sqlStr.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
        if (ordenarPor.equals("edicao")) {
        	sqlStr.append(" ORDER BY catalogo.edicao ");
        } else if (ordenarPor.equals("titulo")) {
        	sqlStr.append(" ORDER BY catalogo.titulo ");
        } else if (ordenarPor.equals("ano")) {
        	sqlStr.append(" ORDER BY catalogo.anopublicacao ");
        } else if (ordenarPor.equals("crescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo asc ");
        } else if (ordenarPor.equals("decrescente")) {
        	sqlStr.append(" ORDER BY catalogo.codigo desc ");
        } else if (ordenarPor.equals("classificacao")) {
        	sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
        } else {
        	sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
        }
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.trim());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
    }
    
    @Override
    public void executarMontarResumoCatalogo(CatalogoVO catalogoVO) throws Exception {
    	StringBuilder str = new StringBuilder();
        str.append(" SELECT DISTINCT ");
        str.append(" biblioteca.nome AS \"biblioteca.nome\", ");
        str.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'DI'  and (ex.paraconsulta = false or ex.paraConsulta is null)) as nrexemplaresdisponivel, ");
        str.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI','CO'))) as nrexemplaresemprestimo, ");
        str.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and ex.situacaoatual = 'EM') as nrexemplaresemprestado, ");
        str.append(" (select count(ex.codigo) from exemplar ex where ex.catalogo = catalogo.codigo and (ex.situacaoatual in ('DI', 'CO') and ex.paraconsulta = true)) as nrexemplaresparaconsulta ");
        str.append(" FROM catalogo ");
        str.append(" LEFT JOIN exemplar on exemplar.catalogo = catalogo.codigo ");
        str.append(" LEFT JOIN biblioteca on biblioteca.codigo = exemplar.biblioteca ");
        str.append(" WHERE catalogo.codigo = ").append(catalogoVO.getCodigo());
        
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
		if (resultado.next()) {
			catalogoVO.setBiblioteca(resultado.getString("biblioteca.nome"));
			catalogoVO.setNrExemplaresDisponivel(resultado.getInt("nrexemplaresdisponivel"));
			catalogoVO.setNrExemplaresParaEmprestimo(resultado.getInt("nrexemplaresemprestimo"));
			catalogoVO.setNrExemplaresEmprestado(resultado.getInt("nrexemplaresemprestado"));
			catalogoVO.setNrExemplaresParaConsulta(resultado.getInt("nrexemplaresparaconsulta"));
		}
    }
    
	@Override
    public void executarMontarAnoVolumeNrEdicaoInicialFinal(CatalogoVO catalogoVO) throws Exception {
    	StringBuilder str = new StringBuilder();
    	str.append(" SELECT DISTINCT ");
    	str.append(" (select min(anovolume) from exemplar ex where ex.catalogo = catalogo.codigo ) as anovolumeinicial,  ");
    	str.append(" (select max(anovolume) from exemplar ex where ex.catalogo = catalogo.codigo ) as anovolumefinal,  ");
    	str.append(" (select min(edicao) from exemplar ex where ex.catalogo = catalogo.codigo ) as nredicaoinicial,  ");
    	str.append(" (select max(edicao) from exemplar ex where ex.catalogo = catalogo.codigo ) as nredicaofinal  ");
    	str.append(" FROM catalogo ");
    	str.append(" LEFT JOIN exemplar on exemplar.catalogo = catalogo.codigo ");
    	str.append(" LEFT JOIN biblioteca on biblioteca.codigo = exemplar.biblioteca ");
    	str.append(" WHERE catalogo.codigo = ").append(catalogoVO.getCodigo());
    	
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString());
    	if (resultado.next()) {
    		catalogoVO.setAnoVolumeInicial(resultado.getString("anovolumeinicial"));
    		catalogoVO.setAnoVolumeFinal(resultado.getString("anovolumefinal"));
    		catalogoVO.setNrEdicaoInicial(resultado.getString("nredicaoinicial"));
    		catalogoVO.setNrEdicaoFinal(resultado.getString("nredicaofinal"));
    	}
    }
    
	@Override
	public Integer consultarTotalDeGegistroPorTombo(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder str = new StringBuilder();
		List<String> parametros = new ArrayList<String>(0);
		str.append(" SELECT COUNT (distinct catalogo.codigo) FROM catalogo ");	
		str.append(" LEFT JOIN exemplar on exemplar.catalogo = catalogo.codigo Where 1=1 ");
		if (valorConsulta != null && !valorConsulta.equals("")) {
			if(Uteis.getIsValorNumerico(valorConsulta)){
				str.append(" and (exemplar.codigobarra::NUMERIC(20,0) = ?::NUMERIC(20,2)) ");
				parametros.add(valorConsulta);
			}else{
				str.append(" and (exemplar.codigobarra::NUMERIC(20,0)::TEXT LIKE ? ");
				str.append(" or exemplar.codigobarra LIKE ?) ");
				parametros.add(valorConsulta);
				parametros.add(valorConsulta);
			}
		}
		str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, assinaturaPeriodico, usuario));
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), parametros.toArray());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}
    
	@Override
	public Integer consultarTotalDeGegistroPorNomeAutor(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder str = new StringBuilder();
		str.append(" SELECT COUNT (distinct catalogo.codigo) FROM catalogo ");	
		str.append(" LEFT JOIN CatalogoAutor on CatalogoAutor.catalogo = catalogo.codigo ");
		str.append(" LEFT JOIN Autor on Autor.codigo = CatalogoAutor.autor ");
		str.append(" WHERE sem_acentos(trim(Autor.nome)) ilike(trim(sem_acentos(?))) ");
		str.append(getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(unidadeEnsino, false, usuario));
		
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), "%"+valorConsulta+"%");
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}
	
	public Boolean verificarExisteCatalogoPorNumeroControle(String numeroControle, String tituloCatalogo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT codigo FROM catalogo WHERE numerocontrole = '").append(numeroControle).append("' ");
		sqlStr.append(" and titulo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), tituloCatalogo);
		if (rs.next()) {
			return true;
		}
		return false;
	}
	 
	@Override
    public List<CatalogoVO> consultaRapidaPorCodigoTomboCatalogoAssinaturaPeriodico(String campoConsulta,String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		
		if (campoConsulta.equals("tombo")) {
			sqlStr.append(" LEFT JOIN exemplar ON exemplar.catalogo = catalogo.codigo ");
			sqlStr.append(" WHERE 1=1 ");
			
			 boolean permiteAcessarPeriodicosOutrasUnidades = permiteAcessarCatalogosOutrasUnidades(usuario , assinaturaPeriodico);
             
	        	if (!permiteAcessarPeriodicosOutrasUnidades && usuario.getTipoUsuario().equals("FU")) {			
	        
	        	 sqlStr.append("AND  exists (select exemplar.codigo from exemplar inner join biblioteca on biblioteca.codigo = exemplar.biblioteca inner join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo "); 
	             sqlStr.append(" where unidadeensinobiblioteca.unidadeensino = "+ usuario.getUnidadeEnsinoLogado().getCodigo()+" and exemplar.catalogo = catalogo.codigo limit 1)");
	        	}
			
			sqlStr.append(" AND  catalogo.assinaturaPeriodico = true  ");

			if (Uteis.isAtributoPreenchido(valorConsulta)) {
				if(Uteis.getIsValorNumerico(valorConsulta)){
					sqlStr.append(" and (exemplar.codigobarra::NUMERIC(20,0) = ").append(valorConsulta).append("::NUMERIC(20,2)) ");
				}else{
					sqlStr.append(" and (exemplar.codigobarra::NUMERIC(20,0)::TEXT LIKE '").append(valorConsulta).append("' ");
					sqlStr.append(" or exemplar.codigobarra LIKE '").append(valorConsulta).append("') ");
				}
			}
		}
 
		if (ordenarPor.equals("edicao")) {
			sqlStr.append(" ORDER BY catalogo.edicao ");
		} else if (ordenarPor.equals("titulo")) {
			sqlStr.append(" ORDER BY catalogo.titulo ");
		} else if (ordenarPor.equals("ano")) {
			sqlStr.append(" ORDER BY catalogo.anopublicacao ");
		} else if (ordenarPor.equals("crescente")) {
			sqlStr.append(" ORDER BY catalogo.codigo asc ");
		} else if (ordenarPor.equals("decrescente")) {
			sqlStr.append(" ORDER BY catalogo.codigo desc ");
		} else if (ordenarPor.equals("classificacao")) {
			sqlStr.append(" ORDER BY catalogo.classificacaobibliografica desc ");
		} else {
			sqlStr.append(" ORDER BY catalogo.edicao desc, catalogo.titulo, catalogo.anopublicacao ");
		}
		if (Uteis.isAtributoPreenchido(limite)) {
			sqlStr.append(" LIMIT ").append(limite);
			if (Uteis.isAtributoPreenchido(offset)) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapida(tabelaResultado, usuario);
    }

	public void alterarOrdemAutores(CatalogoVO catalogoVO, CatalogoAutorVO catalogoAutorVO1, CatalogoAutorVO catalogoAutorVO2) {
		int ordem1 = catalogoAutorVO1.getOrdemApresentacao();
		catalogoAutorVO1.setOrdemApresentacao(catalogoAutorVO2.getOrdemApresentacao());
		catalogoAutorVO2.setOrdemApresentacao(ordem1);
		Ordenacao.ordenarLista(catalogoVO.getCatalogoAutorVOs(), "ordemApresentacao");
	}
	
		
	private boolean permiteAcessarCatalogosOutrasUnidades(UsuarioVO usuarioVO , boolean assinaturaPeriodico) {		
		if (assinaturaPeriodico) {			
			return ControleAcesso.verificarPermissaoOperacao(usuarioVO.getPerfilAcesso().getCodigo(), "Catalogo_AcessarPeriodicosOutrasUnidades" , 1);
		} else {			
			return ControleAcesso.verificarPermissaoOperacao(usuarioVO.getPerfilAcesso().getCodigo(), "Catalogo_AcessarCatalogoOutrasUnidades" , 1);
		}
	}
	private String getSQLPadraoValidacaoPermiteAcessarCatalogosOutrasUnidades(Integer unidadeEnsino, boolean assinaturaPeriodico, UsuarioVO usuarioVO) {
		StringBuilder str = new StringBuilder();
		boolean permiteAcessarCatalogosPeriodicosOutrasUnidades = permiteAcessarCatalogosOutrasUnidades(usuarioVO, assinaturaPeriodico);
		if (!permiteAcessarCatalogosPeriodicosOutrasUnidades && usuarioVO.getTipoUsuario().equals("FU") && Uteis.isAtributoPreenchido(usuarioVO.getUnidadeEnsinoLogado())) {
			str.append("AND exists (select exemplar.codigo from exemplar inner join biblioteca on biblioteca.codigo = exemplar.biblioteca ")
			.append(" inner join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = biblioteca.codigo ")
			.append(" where unidadeensinobiblioteca.unidadeensino = ")
			.append(usuarioVO.getUnidadeEnsinoLogado().getCodigo())
			.append(" and exemplar.catalogo = catalogo.codigo limit 1)");
		}
		str.append(assinaturaPeriodico ? " and (catalogo.assinaturaPeriodico) " : " and (catalogo.assinaturaPeriodico = false or catalogo.assinaturaPeriodico is null) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			str.append(" and exists (select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo ")
			.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ")
			.append(unidadeEnsino)
			.append("  ) limit 1) ");
		}
		return str.toString();
	}
	
	@Override
	public Map<TipoFiltroConsultaEnum, List<TipoFiltroConsulta>> consultarFiltrosBibliotecaExterna(Integer biblioteca , UsuarioVO usuarioLogado) throws Exception{
		StringBuilder sql =  new StringBuilder("");
		sql.append(" (select  '").append(TipoFiltroConsultaEnum.TIPO_CATALOGO.name()).append("' as tipofiltro,  tipocatalogo.codigo, tipocatalogo.nome, count(catalogo.codigo) as qtde from catalogo");
		sql.append(" inner join tipocatalogo on tipocatalogo.codigo = catalogo.tipocatalogo");
		sql.append(" where exists (");
		sql.append(" 	select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo");
		if(Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" 	and exemplar.biblioteca = ").append(biblioteca);
		}
		sql.append(" limit 1)");
		sql.append(" group by tipocatalogo.codigo, tipocatalogo.nome order by tipocatalogo.nome)");
		sql.append("");
		sql.append(" union all");
		sql.append("");
		sql.append(" (select '").append(TipoFiltroConsultaEnum.AREA_CONHECIMENTO.name()).append("' as tipofiltro,  areaconhecimento.codigo, areaconhecimento.nome, count(catalogo.codigo) as qtde from catalogo");
		sql.append(" inner join catalogoareaconhecimento on catalogoareaconhecimento.catalogo = catalogo.codigo ");
		sql.append(" inner join areaconhecimento on areaconhecimento.codigo = catalogoareaconhecimento.areaconhecimento");
		sql.append(" where exists (");
		sql.append(" 	select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo");
		if(Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" 	and exemplar.biblioteca = ").append(biblioteca);
		}
		sql.append(" limit 1)");		
		sql.append(" group by areaconhecimento.codigo, areaconhecimento.nome order by areaconhecimento.nome)");
		sql.append(" union all (");
		sql.append(" select 'CURSO' as tipofiltro,curso.codigo,curso.nome,count(catalogo.codigo) as qtde from catalogo");
		sql.append(" inner join catalogocurso on catalogocurso.catalogo = catalogo.codigo");
		sql.append(" inner join curso on catalogocurso.curso = curso.codigo ");
		sql.append(" where exists(");
		sql.append(" select	exemplar.codigo from exemplar where	exemplar.catalogo = catalogo.codigo");
		if(Uteis.isAtributoPreenchido(biblioteca)) {
			sql.append(" 	and exemplar.biblioteca = ").append(biblioteca);
		}
		sql.append(" limit 1)");
		sql.append(" group by curso.codigo, curso.nome order by curso.nome)");
		if (Uteis.isAtributoPreenchido(usuarioLogado) && usuarioLogado.getTipoUsuario().equals(TipoPessoa.ALUNO.getValor())) {
			sql.append(" union all (");
			sql.append(" select 'DISCIPLINA' as tipofiltro, disciplina.codigo, disciplina.nome, count(disciplina.codigo) as qtde from catalogo");
			sql.append(" inner join referenciabibliografica on referenciabibliografica.catalogo = catalogo.codigo");
			sql.append(" inner join disciplina on disciplina.codigo = referenciabibliografica.disciplina");
			sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.disciplina = referenciabibliografica.disciplina");
			sql.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
			sql.append(" where exists(");
			sql.append(" 	select exemplar.codigo from exemplar where exemplar.catalogo = catalogo.codigo");
			if(Uteis.isAtributoPreenchido(biblioteca)) {
				sql.append(" 	and exemplar.biblioteca = ").append(biblioteca);
			}
			sql.append(" limit 1)");
			List<String> listaMatriculasVOs = getFacadeFactory().getMatriculaFacade().consultarMatriculasPorCodigoPessoa(usuarioLogado.getPessoa().getCodigo());
			if (!listaMatriculasVOs.isEmpty()) {
				sql.append(" and matriculaperiodoturmadisciplina.matricula in ('");
				 for (String obj : listaMatriculasVOs) {
					sql.append(obj);
				}
				sql.append("') and matriculaperiodo.situacaoMatriculaPeriodo = 'AT'");
			}
			sql.append(" group by disciplina.codigo, disciplina.nome order by disciplina.nome)");
		}
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TipoFiltroConsulta> tipoCatalogoVOs = new ArrayList<TipoFiltroConsulta>();
		List<TipoFiltroConsulta> areaConhecimentoVOs = new ArrayList<TipoFiltroConsulta>();
		List<TipoFiltroConsulta> cursoVOs = new ArrayList<TipoFiltroConsulta>();
		List<TipoFiltroConsulta> disciplinaVOs = new ArrayList<TipoFiltroConsulta>();
		while(rs.next()) {
			TipoFiltroConsulta tipoFiltroConsulta = new TipoFiltroConsulta(TipoFiltroConsultaEnum.valueOf(rs.getString("tipofiltro")), rs.getString("nome"), rs.getObject("codigo"), rs.getInt("qtde"));
			if(tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.TIPO_CATALOGO)) {
				tipoCatalogoVOs.add(tipoFiltroConsulta);
			}else if(tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.AREA_CONHECIMENTO)) {
				areaConhecimentoVOs.add(tipoFiltroConsulta);
			}else if(tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.CURSO)) {
				cursoVOs.add(tipoFiltroConsulta);
			}else if(tipoFiltroConsulta.getCampoConsulta().equals(TipoFiltroConsultaEnum.DISCIPLINA)) {
				disciplinaVOs.add(tipoFiltroConsulta);
			}
		}
		Map<TipoFiltroConsultaEnum, List<TipoFiltroConsulta>> mapResultado = new HashMap<TipoFiltroConsultaEnum, List<TipoFiltroConsulta>>(0);
		mapResultado.put(TipoFiltroConsultaEnum.TIPO_CATALOGO, tipoCatalogoVOs);
		mapResultado.put(TipoFiltroConsultaEnum.AREA_CONHECIMENTO, areaConhecimentoVOs);
		mapResultado.put(TipoFiltroConsultaEnum.CURSO, cursoVOs);
		mapResultado.put(TipoFiltroConsultaEnum.DISCIPLINA, disciplinaVOs);
		return mapResultado;
	}

	

	

	@Override
	public Boolean realizarVerificacaoPosssuiIntegracaoEbsco()  {
		try {
			//return getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoBibliotecaEbsco().getPossuiIntegracaoEbsco();
			return  getAplicacaoControle().carregarDadosConfiguracaoBibliotecaPadrao().getPossuiIntegracaoEbsco();
		} catch (Exception e) {
			return Boolean.FALSE;
			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarEnvioEbsco(final CatalogoVO obj, UsuarioVO usuario) throws Exception {
        try {
			Catalogo.alterar(getIdEntidade(), true, usuario);
            

            final StringBuilder sql = new StringBuilder(" UPDATE Catalogo set  enviadoEbsco=?  WHERE ((codigo = ?)) ");
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    int i = 0;                    
                    sqlAlterar.setBoolean(++i, obj.getEnviadoEbsco());                    
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

           

        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	public List<ArquivoMarc21CatalogoVO> consultaRapidaCatalogos(Integer codigoCatalogo ,boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaMarcCatalogoCompleta(); 
        sqlStr.append("where 1=1  ");
        if(Uteis.isAtributoPreenchido(codigoCatalogo)) {        	
        	sqlStr.append(" and catalogo.codigo= ").append(codigoCatalogo);
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        List<ArquivoMarc21CatalogoVO> vetResultado = new ArrayList<ArquivoMarc21CatalogoVO>(0);
        while(tabelaResultado.next()){
        	CatalogoVO obj = new CatalogoVO();
			ArquivoMarc21CatalogoVO arquivoMarc21CatalogoVO = new ArquivoMarc21CatalogoVO();
            montarDadosMarcCatalogoCompleto((CatalogoVO) obj, tabelaResultado, usuario);           
            arquivoMarc21CatalogoVO.setCatalogoVO(obj);
            vetResultado.add(arquivoMarc21CatalogoVO);     	
        	
        }
        return vetResultado;
        
    }
	

	 
	
		
}
