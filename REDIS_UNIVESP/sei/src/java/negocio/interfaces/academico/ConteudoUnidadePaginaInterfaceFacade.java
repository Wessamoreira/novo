package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;

import negocio.comuns.academico.ConteudoUnidadePaginaGraficoCategoriaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface ConteudoUnidadePaginaInterfaceFacade {

    void persistir(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void validarDados(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO) throws ConsistirException;

    List<ConteudoUnidadePaginaVO> consultarPorUnidadeConteudo(Integer unidadeConteudo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//    void incluirConteudoUnidadePagina(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//    void alterarConteudoUnidadePagina(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    ConteudoUnidadePaginaVO consultarPorUnidadeConteudoPagina(Integer unidadeConteudo, Integer pagina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void excluir(ConteudoVO conteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Boolean alterarPagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void adicionarConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, MomentoApresentacaoRecursoEducacionalEnum momentoApresentacaoRecursoEducacionalEnum, ConteudoVO conteudo, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

    void removerConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

    void alterarOrdemApresentacaoConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO, int posicao, boolean recursoAnterior) throws Exception;

    void upLoadArquivoConteudoUnidadePagina(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

    void copiarArquivoConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    void adicionarSerieGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String serie, Double valor) throws Exception;

    void adicionarCategoriaGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String categoria) throws Exception;

    void removerCategoriaGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaGraficoCategoriaVO categoria) throws Exception;

    void removerSerieGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String serie) throws Exception;

    void realizarGeracaoGrafico(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO);

    void realizarGeracaoConteudoUnidadePaginaGraficoVO(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO)  throws Exception;

    void upLoadIconeConteudoUnidadePagina(FileUploadEvent uploadEvent, ConteudoUnidadePaginaVO conteudoUnidadePagina, boolean iconeVoltar, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

    void copiarIconeConteudoUnidadePaginaDaPastaTemp(ConteudoUnidadePaginaVO conteudoUnidadePagina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

    void alterarOrdemConteudoUnidadePaginaRecursoEducacional(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional1, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional2, UsuarioVO usuario) throws Exception;

    void alterarNumeroPagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuario) throws Exception;

    ConteudoUnidadePaginaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    Map<Integer, List<ConteudoUnidadePaginaVO>> consultarPorUnidadeConteudoPaginaPorConteudo(Integer conteudo, String matricula, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void upLoadArquivoConteudoUnidadePaginaHtml(FileUploadEvent upload, Integer disciplina, ConteudoUnidadePaginaVO conteudoUnidadePagina, Boolean publicarImagem, String nomeImagem, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void uploadImagemBackgroundConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Integer disciplina,FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	void removerImagemBackgroundConteudoUnidadePagina(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

	void realizarReplicacaoBackgroundParaPagina(List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs, OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum, Boolean gravarAlteracao) throws Exception;

	void alterarBackgroundEdicao(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, Boolean aplicarBackRecursoEducacional) throws Exception;

	void incluirConteudoUnidadePagina(List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs, UnidadeConteudoVO unidadeConteudoVO, ConteudoVO conteudoVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;

	void removerImagemSlide(ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, String imagem, UsuarioVO usuarioVO) throws Exception;
    

}
