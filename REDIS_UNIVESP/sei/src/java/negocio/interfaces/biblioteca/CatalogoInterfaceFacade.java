package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ArquivoMarc21CatalogoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoAutorVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.TipoFiltroConsulta;
import negocio.comuns.utilitarias.TipoFiltroConsultaEnum;
import negocio.comuns.utilitarias.UteisFTP;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface CatalogoInterfaceFacade {

	public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public void adicionarExemplares(CatalogoVO catalogo, ExemplarVO obj) throws Exception;
	
    public CatalogoVO novo() throws Exception;

    public void incluir(CatalogoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, Boolean validarDados) throws Exception;

    public void alterar(CatalogoVO obj, UsuarioVO usuario) throws Exception;
    public void alterarEnvioEbsco(CatalogoVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(CatalogoVO obj, UsuarioVO usuario) throws Exception;

    public CatalogoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultarPorTitulo(String titulo, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, Integer tipoCatalogo, UsuarioVO usuario)
            throws Exception;

    public List<CatalogoVO> consultarPorNomeEditora(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario)
            throws Exception;

    public List<CatalogoVO> consultarPorNomeClassificacaoBibliografica(String valorConsulta, boolean controlarAcesso,
            int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultarPorTipoCatalogo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario)
            throws Exception;

    public List<CatalogoVO> consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<CatalogoVO> consultarPorAssunto(String assunto, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultarPorAutorNome(String valorConsulta, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultarPorPalavraChave(String valorConsulta, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public Object consultarPorIsbnIssn(String valorConsulta, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

    public List consultarTelaBuscaCatalogosAvancada(String codigoTombo, String titulo, String assunto, String palavrasChave, String autores, String classificacao, String isbn, String issn, int biblioteca, int areaconhecimento, String cutterPhaBuscaAvancada,int nivelMontarDados, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception;

    public void inicializarDadosCatalogoNovo(CatalogoVO catalogoVO, UsuarioVO usuario) throws Exception;

    public void inicializarDadosCatalogoEditar(CatalogoVO obj, UsuarioVO usuario) throws Exception;

    public void adicionarArquivoLista(ArquivoVO arquivoVO, CatalogoVO catalogoVO) throws Exception;

    public void adicionarArquivoListaSumarioCapa(ArquivoVO arquivoVO, CatalogoVO catalogoVO) throws Exception;

    public void atualizarNrExemplaresCatalogo(ExemplarVO obj) throws Exception;

    public void subtrairUmExemplarNumeroExemplaresCatalogo(CatalogoVO catalogo) throws Exception;

    public List<CatalogoVO> consultarTelaBuscaCatalogos(String valorConsulta, Integer biblioteca, Integer limite, Integer offset, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception;

    public  List<ReservaVO> executarReservaCatalogos(List<CatalogoVO> listaCatalogosAdicionadosNaGuiaReserva, BibliotecaVO biblioteca, PessoaVO pessoa, String tipoPessoa, String matricula, ConfiguracaoBibliotecaVO confBiblioteca, UsuarioVO usuario) throws Exception;

    public Integer consultarNumeroDeExemplaresDisponiveisPorCatalogo(Integer codigoCatalogo) throws Exception;

//    public List executarConsultaTelaBuscaCatalogos(String valorConsulta, UsuarioVO usuarioLogado, Boolean apenasExemplarDisponivel) throws Exception;

    public Integer consultaTotalDeRegistroRapidaPorBuscaCatalogo(String valorConsulta, Integer bibioteca, UsuarioVO usuario, boolean apenasExemplarDisponivelParaEmprestimo) throws Exception;

    public void carregarDados(CatalogoVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public void carregarDados(CatalogoVO obj, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultaRapidaPorNomeEditora(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultaRapidaPorTituloCatalogo(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalDeGegistroPorCodigoCatalogo(Integer valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
   
    public Integer consultarTotalDeRegistroPorCodigoCatalogo(Integer valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalDeGegistroPorTituloCatalogo(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

    public Integer consultarTotalDeGegistroPorNomeEditora(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, UsuarioVO usuario) throws Exception;

    public List<CatalogoVO> consultaRapidaPorTituloCatalogo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public List<CatalogoVO> consultarRichModalSituacaoExemplarRel(String valorConsulta, String campoConsulta, Integer codigoBiblioteca, Integer codigoSessao,String situacao, String nivelBibliografico, UsuarioVO usuario) throws Exception;

    List<CatalogoVO> consultarPorBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    
    public List<CatalogoVO> consultarPorCodigoTipoCatalogoAssinaturaPeriodica(Integer valorConsulta, String tipo, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    
    public List<CatalogoVO> consultarPorTituloTipoCatalogoAssinaturaPeriodico(String titulo, String tipo, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
    
	Integer consultarTotalDeGegistroPorNomeAutor(String valorConsulta, boolean controlarAcesso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	List<CatalogoVO> consultaRapidaPorNomeAutor(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	List<CatalogoVO> consultarPorNomeAutor(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer biblioteca, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	public List<CatalogoVO> consultaRapidaPorTombo(String valorConsulta, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	public Integer consultarTotalDeGegistroPorTombo(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	public void executarMontarResumoCatalogo(CatalogoVO catalogoVO) throws Exception;
	public Integer consultarTotalDeGegistroPorAssunto(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public List<CatalogoVO> consultaRapidaPorClassificacao(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public List<CatalogoVO> consultaRapidaPorAssunto(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public Integer consultarTotalDeGegistroPorClassificacao(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public void executarMontarAnoVolumeNrEdicaoInicialFinal(CatalogoVO catalogoVO) throws Exception;
	public Boolean verificarExisteCatalogoPorNumeroControle(String numeroControle, String tituloCatalogo, UsuarioVO usuarioVO) throws Exception;
	
	public Integer consultarTotalDeGegistroPorTipoCatalogo(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public List<CatalogoVO> consultaRapidaPorCutterPha(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	public Integer consultarTotalDeGegistroPorCutterPha(String valorConsulta, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;

	public List<CatalogoVO> consultaRapidaPorTipoCatalogo(String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, Integer unidadeEnsino, UsuarioVO usuario) throws Exception;
	
	public List<CatalogoVO> consultaRapidaPorCodigoTomboCatalogoAssinaturaPeriodico(String campoConsulta,String valorConsulta, String ordenarPor, Integer limite, Integer offset, boolean controlarAcesso, boolean assinaturaPeriodico, UsuarioVO usuario) throws Exception ;
	
	public void alterarOrdemAutores(CatalogoVO catalogoVO, CatalogoAutorVO dragValue, CatalogoAutorVO dropValue);
	Map<TipoFiltroConsultaEnum, List<TipoFiltroConsulta>> consultarFiltrosBibliotecaExterna(Integer biblioteca , UsuarioVO usuarioLogado) throws Exception;
	
	public Boolean realizarVerificacaoPosssuiIntegracaoEbsco();
	List<ArquivoMarc21CatalogoVO> consultaRapidaCatalogos(Integer codigoCatalogo , boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}
