package negocio.interfaces.protocolo;
import java.util.List;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoTransferenciaInternaCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoCursoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface TipoRequerimentoInterfaceFacade {
	

    
    public void incluir(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void alterar(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
    public void excluir(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
    public TipoRequerimentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorCodigo(Integer valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorNome(String valorConsulta, String situacao, Integer unidadeensino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorValor(Double valorConsulta, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorPrazoExecucao(Integer valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorNomeDepartamento(String valorConsulta, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoAluno(Boolean visaoAluno, String situacao, Integer unidadeEnsino, Integer curso, String matricula, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoProfessor(Boolean visaoProfessor, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarPorPermissaoVisaoCoordenador(Boolean visaoCoordenador, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso,UsuarioVO usuario) throws Exception;
    public List<TipoRequerimentoVO> consultarTipoRequerimentoComboBox(boolean controlarAcesso, String situacao, Integer unidadeEnsino,Integer curso, boolean permiteAbriReqForaDoPrazo, UsuarioVO usuario, Boolean permitirUsuarioConsultarIncluirApenasRequerimentosProprios) throws Exception;
    List<TipoRequerimentoVO> consultarPorPermissaoVisaoPais(Boolean visaoPai, Integer unidadeEnsino, String matricula, Integer curso, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public void inativar(final TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
    public List<TipoRequerimentoVO> consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	List<TipoRequerimentoVO> consultarPorTipoRequerimento(String tipo, String situacao, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	List<TipoRequerimentoVO> consultarPorPermissaoVisaoAlunoMinhasNotas(Boolean minhasNotas, String situacao, Integer unidadeEnsino, Integer curso, String matricula, Integer disciplina, Integer turma, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public void adicionarTipoRequerimentoCursoVOs(List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, TipoRequerimentoCursoVO obj);
	public void removerTipoRequerimentoCursoVOs(List<TipoRequerimentoCursoVO> tipoRequerimentoCursoVOs, TipoRequerimentoCursoVO obj);
	public List<TipoRequerimentoVO> consultarTipoRequerimentoPorSituacaoESemVinculoCalendarioAbertura(String valorConsulta,int valorCodigo, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	List<TipoRequerimentoVO> consultarTipoRequerimentoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO);
	List<TipoRequerimentoVO> consultarTipoDoTipoRequerimentoComTextoPadraoComSituacaoAtiva();
	Boolean validarSeTipoRequerimentoUsaCentroResultadoTurma(Integer requeriemnto, UsuarioVO usuarioVO);
	String consultarMsgBloqueioNovaSolicitacaoAproveitamento(Integer tipoRequeriemnto, UsuarioVO usuarioVO);
	void realizarAtivacaoTipoRequerimento(TipoRequerimentoVO obj, UsuarioVO usuarioVO) throws Exception;
	public List<TipoRequerimentoVO> consultarPorNome(String valorConsulta, String situacao, List<UnidadeEnsinoVO> unidadeEnsinos, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public List<TipoRequerimentoVO> consultarTipoRequerimentoTCCComboBox(boolean controlarAcesso, String situacao, Integer unidadeensino, UsuarioVO usuario) throws Exception;
	
    public void adicionarCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj);
	public void removerCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj);
    public void adicionarListaImportacaoPlanilhaCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs, CidTipoRequerimentoVO obj);

}