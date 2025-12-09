package negocio.interfaces.ead;

import java.util.List;

import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.enumeradores.PeriodoDisponibilizacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.SituacaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.utilitarias.ConsistirException;


public interface ListaExercicioInterfaceFacade {
    
    void persistir(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;
    
    ListaExercicioVO novo();
    
    void excluir(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;
    
    void ativar(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;
    
    void inativar(ListaExercicioVO listaExercicioVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception;
    
    List<ListaExercicioVO> consultar(Integer disciplina, Integer turma, String descricao, SituacaoListaExercicioEnum situacaoListaExercicio, 
    TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio, PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicioEnum, 
    Boolean controlarAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception;
    
    Integer consultarTotalRegistro(Integer disciplina, Integer turma, String descricao, SituacaoListaExercicioEnum situacaoListaExercicio, 
    TipoGeracaoListaExercicioEnum tipoGeracaoListaExercicio, PeriodoDisponibilizacaoListaExercicioEnum periodoDisponibilizacaoListaExercicioEnum) throws Exception;
    
    List<ListaExercicioVO> consultarListaExercicioDisponivelAluno(Integer matriculaPeriodoTurmaDisciplina, Integer limite, Integer pagina) throws Exception;
            
    Integer consultarTotalRegistroListaExercicioDisponivelAluno(Integer matriculaPeriodoTurmaDisciplina) throws Exception;
    
    ListaExercicioVO consultarPorChavePrimaria(Integer codigo) throws Exception;
    
    void adicionarQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicioVO) throws Exception;
    
    void removerQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicioVO);
    
    void alterarOrdemApresentacaoQuestaoListaExercicio(ListaExercicioVO listaExercicioVO, QuestaoListaExercicioVO questaoListaExercicio1, QuestaoListaExercicioVO questaoListaExercicio2);

    ListaExercicioVO clonarListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception;

    void realizarVerificacaoQuestaoUnicaEscolha(ListaExercicioVO listaExercicioVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO);

    void realizarGeracaoGabarito(ListaExercicioVO listaExercicioVO) throws Exception;

    void validarDados(ListaExercicioVO listaExercicioVO) throws ConsistirException, Exception;

	void atualizarSituacaoListasExerciciosPorConteudo(SituacaoListaExercicioEnum situacao, ConteudoVO conteudoVO,
			UsuarioVO usuarioLogado) throws Exception;
    
    

}
