package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.AproveitamentoDisciplinaVO;
import negocio.comuns.academico.AproveitamentoDisciplinasEntreMatriculasVO;
import negocio.comuns.academico.ConcessaoCargaHorariaDisciplinaVO;
import negocio.comuns.academico.ConcessaoCreditoDisciplinaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoDisciplinasAproveitadasVO;
import negocio.comuns.protocolo.RequerimentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface AproveitamentoDisciplinaInterfaceFacade {

	public AproveitamentoDisciplinaVO novo() throws Exception;

	public void incluir(AproveitamentoDisciplinaVO obj, boolean controleAcesso, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void alterar(AproveitamentoDisciplinaVO obj, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void alterarSituacao(Integer codigo, String situacao, UsuarioVO usuario) throws Exception;

	public void alterarMatricula(Integer codigo, String matricula, UsuarioVO usuario) throws Exception;

	public void excluir(AproveitamentoDisciplinaVO obj,UsuarioVO usuario) throws Exception;

	public AproveitamentoDisciplinaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorTipoJustificativa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorInstituicaoOrigem(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List<AproveitamentoDisciplinaVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
	public List consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

        public void adicionarConcessaoCreditoDisciplina(AproveitamentoDisciplinaVO obj, ConcessaoCreditoDisciplinaVO concessaoCreditoDisciplinaVO, UsuarioVO usuarioLogado) throws Exception;

        public void excluirObjConcessaoCreditoDisciplinaVOs(AproveitamentoDisciplinaVO obj, Integer disciplina) throws Exception;

        public void inicializarDadosAdicionarConcessaoCargaHorariaDisciplina(AproveitamentoDisciplinaVO obj, ConcessaoCargaHorariaDisciplinaVO concessaoCargaHorariaDisciplinaVO, UsuarioVO usuarioLogado) throws Exception;

        public void adicionarConcessaoCargaHorariaDisciplina(AproveitamentoDisciplinaVO obj, ConcessaoCargaHorariaDisciplinaVO concessaoCargaHorariaDisciplinaVO, UsuarioVO usuarioLogado) throws Exception;

        public void excluirObjConcessaoCargaHorariaDisciplinaVOs(AproveitamentoDisciplinaVO obj, Integer disciplina) throws Exception;
        
        public Boolean realizarVerificacaoExistenciaCursandoDisciplinaHistoricoAluno(String matricula, DisciplinaVO disciplinaVO, UsuarioVO usuarioVO) throws Exception;
        
    	public AproveitamentoDisciplinaVO consultarPorCodigoRequerimento(
    			Integer codigoPrm, int nivelMontarDados,
    			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario)
    			throws Exception;
        
        public void realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(
            AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
        
        public void realizarMontagemPainelMapaEquivalenciaDisciplinasAproveitadas(
            AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, MapaEquivalenciaDisciplinaVO mapaEquivalencia, UsuarioVO usuarioVO) throws Exception;
    	
    	public void realizarMontagemListaDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, String instituicao, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception;
        
        public void veriricarJaExisteAproveitamentoDisciplinaGrupoOptativa(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO,
            GradeCurricularGrupoOptativaDisciplinaVO obj) throws Exception;

    public List<HistoricoVO> gerarHistoricosPrevistosDisciplinasAproveitadas(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO, UsuarioVO usuario) throws Exception;
    
    public void alterarAproveitoPrevistoParaEfetivo(AproveitamentoDisciplinaVO obj, UsuarioVO usuario) throws Exception;

	void excluirComBaseNaMatriculaPeriodo(Integer matriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	/**
	 * @author Rodrigo Wind - 06/01/2016
	 * @param aproveitamentoDisciplinaVO
	 * @throws Exception
	 */
	void realizarRegistroAproveitamentoDisciplinasRegistrado(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) throws Exception;

	public void verificarEMapearDisciplinasPodemSerAproveitadasOutraMatricula(AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO, Boolean carregarDadosHistoricoMatrizCurricularMatriculaDestino, UsuarioVO usuario) throws Exception;
	
	void realizarGeracaoAproveitamentoDisciplinaAutomaticoPorRequerimento(RequerimentoVO requerimento, UsuarioVO usuarioVO) throws Exception;

	List<AproveitamentoDisciplinaVO> consultaRapidaPorRegistroAcademicoAluno(String valorConsulta,
			Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
}