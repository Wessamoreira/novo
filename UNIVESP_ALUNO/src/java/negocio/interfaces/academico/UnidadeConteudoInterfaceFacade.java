package negocio.interfaces.academico;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


public interface UnidadeConteudoInterfaceFacade {

    void persistir(UnidadeConteudoVO unidadeConteudo, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
        
    void incluirUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
    
    void alterarUnidadeConteudo(ConteudoVO conteudoVO, DisciplinaVO disciplinaVO, Boolean controlarAcesso, UsuarioVO usuario, Boolean realizandoClonagem) throws Exception;
    
    void inativarUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, Boolean controlarAcesso, UsuarioVO usuario);
    
    UnidadeConteudoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    void validarDados(UnidadeConteudoVO unidadeConteudoVO) throws ConsistirException;

    void alterarOrdemConteudoUnidadePagina(UnidadeConteudoVO unidadeConteudo1, UnidadeConteudoVO unidadeConteudo2, ConteudoUnidadePaginaVO conteudoUnidadePagina1, ConteudoUnidadePaginaVO conteudoUnidadePagina2, UsuarioVO usuario) throws Exception;

    UnidadeConteudoVO consultarUnidadeConteudoPorConteudoEOrdem(Integer conteudo, Integer ordem, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void excluirUnidadeConteudoEspecifico(UnidadeConteudoVO unidadeConteudoVO) throws Exception;

	void realizarCalculoTempoEPonto(UnidadeConteudoVO unidadeConteudo);

	void adicionarPagina(UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO);

	void uploadImagemBackgroundUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, FileUploadEvent uploadEvent, Boolean aplicarBackRecursoEducacional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	void removerImagemBackgroundUnidadeConteudo(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	void realizarReplicacaoBackgroundParaUnidade(List<UnidadeConteudoVO> unidadeConteudoVOs, OrigemBackgroundConteudoEnum origemBase, String caminhoBase, String nomeArquivo, String cor, Boolean aplicarBackRecursoEducacional, OrigemBackgroundConteudoEnum origemUtilizar, TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum, Boolean gravarAlteracao) throws Exception;

	void alterarBackground(UnidadeConteudoVO unidadeConteudoVO, DisciplinaVO disciplina, Boolean aplicarBackRecursoEducacional, UsuarioVO usuarioVO, Boolean realizandoClonagem) throws Exception;

	List<UnidadeConteudoVO> consultarUnidadeConteudoPorConteudo(Integer conteudo, Integer temaAssunto, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
}
