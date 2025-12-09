
package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.bancocurriculum.VagaContatoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.PessoaVO;

public interface VagasInterfaceFacade {
    public VagasVO novo() throws Exception;
    public void incluir(VagasVO obj, UsuarioVO usuario) throws Exception;
    public void gravarSituacao(VagasVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(VagasVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(VagasVO obj, UsuarioVO usuario) throws Exception;
    public VagasVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public List consultarPorParceiro(String valorConsulta, String ordenacao, String situacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorCargo(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorAreaProfissional(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorNumeroVagas(Integer valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSalario(String valorConsulta, String ordenacao, String situacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public List consultarPorSituacao(String valorConsulta, String ordenacao, Integer codigoParceiro, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void realizarAlteracaoCandidatarVaga(final CandidatosVagasVO obj, UsuarioVO usuario) throws Exception;
    public void realizarCandidatarVaga(final CandidatosVagasVO candidatosVagasVO, UsuarioVO usuario) throws Exception;
    public Boolean consultarExistenciaCandidatosVagas(Integer codigoVaga, Integer codigoPessoa) throws Exception;
    public String consultarDataUltimoCadastro() throws Exception;
    public Integer consultarQuantidadeVagasAbertas() throws Exception;
    public List<VagasVO> consultarVagasRecentementeAtivadas() throws Exception;
    public Integer consultarQuantidadeEmpresasVagasAbertas() throws Exception;
    public Integer consultarQuantidadeAlunosSelecionados() throws Exception;
    public void realizarNavegacaoParaVisualizarCandidatos(List<CandidatosVagasVO> listaCandidatosVagasVOs, VagasVO vaga) throws Exception;
    public List consultarVagasExpiramQtdDias(Integer dia, UsuarioVO usu) throws Exception;
    public void alterarSituacaoEDataDeVagasExpiracao(List lista, UsuarioVO usuario) throws Exception;
    public List consultarVagasParaNotificacaoDeExpiracaoBaseadoDiasParamento(Integer dia, UsuarioVO usu) throws Exception;
    public List<VagasVO> consultaRapidaBuscaVaga(VagasVO obj, UsuarioVO usuario) throws Exception;
    public List<VagasVO> consultaRapidaBuscaVagaVisaoAluno(VagasVO obj, UsuarioVO usuario) throws Exception;
    public List<VagasVO> consultarListaVagasPorAluno(Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception;
    public void realizarSairVaga(final VagasVO obj, final PessoaVO pessoa, UsuarioVO usuario) throws Exception;
    public void alterarSituacaoEDataDeVagasEncerrada(List lista, UsuarioVO usuario) throws Exception;
    public List<VagasVO> consultaRapidaBuscaVagaVisaoAluno(VagasVO obj, List listaAreaProfissional, UsuarioVO usuario) throws Exception;
	void alterarOrdemVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO, VagaQuestaoVO vagaQuestaoVO2) throws Exception;
	void adicionarVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO)  throws Exception;
	void removerVagaQuestao(VagasVO vagasVO, VagaQuestaoVO vagaQuestaoVO)  throws Exception;
	void removerVagaContato(VagasVO vagasVO, VagaContatoVO vagaContatoVO) throws Exception;
	void adicionarVagaContato(VagasVO vagasVO, VagaContatoVO vagaContatoVO) throws Exception;
        public void alterarParceiro(final Integer codParceiroManter, final Integer codParceiroRemover) throws Exception;
        public Integer consultarQuantidadeAlunosContratado() throws Exception;
        public Integer consultarQuantidadeVagasInativas() throws Exception;
        public Integer consultarQuantidadeVagasEncerradas() throws Exception;
        public Integer consultarQuantidadeVagasExpiradas() throws Exception;
        public Integer consultarQuantidadeVagasSobAnalise() throws Exception;
}
