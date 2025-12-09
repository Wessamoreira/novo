package negocio.interfaces.academico;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.InclusaoHistoricoForaPrazoVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoComHistoricoAlunoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.protocolo.RequerimentoVO;

public interface InclusaoHistoricoForaPrazoInterfaceFacade {

    public void setIdEntidade(String idEntidade);

    public void incluir(final InclusaoHistoricoForaPrazoVO obj) throws Exception;

    public void incluir(final InclusaoHistoricoForaPrazoVO obj, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO) throws Exception;

    public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorMatricula(String matricula, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorMatriculaTotalRegistros(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorRegistroAcademico(String matricula, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;
    
    public Integer consultaRapidaPorRegistroAcademicoTotalRegistros(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorAluno(String nomeAluno, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorAlunoTotalRegistros(String nomeAluno, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorResponsavel(String nomeResponsavel, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;
    

    public Integer consultaRapidaPorResponsavelTotalRegistros(String nomeResponsavel, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public List<InclusaoHistoricoForaPrazoVO> consultaRapidaPorDataInclusao(Date dataIni, Date dataFim, boolean controlarAcesso, Integer limite, Integer offset, UsuarioVO usuario) throws Exception;

    public Integer consultaRapidaPorDataInclusaoTotalRegistros(Date dataIni, Date dataFim, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    
    public void realizarMontagemListaDisciplinasAproveitadas(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception;
        
    public void realizarMontagemPainelMatrizCurricularComDisciplinasAproveitadas(
            InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception;
    
    public void validarDadosReposicao(boolean incluindaPeloPagamentoRequerimento, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception;
    
    public void preencherDadosInclusaoHistoricoForaPrazo(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, MatriculaPeriodoVO matriculaPeriodoVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, RequerimentoVO requerimentoVO, UsuarioVO usuarioVO) throws Exception;
    
    public void persistir(boolean incluindaPeloPagamentoRequerimento, InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO, List<PeriodoLetivoVO> listaPeriodoLetivoVOs, MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, String justificativa, String observacaoJustificativa, TextoPadraoVO textoPadraoContratoInclusao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,   RequerimentoVO requerimentoVO, GradeCurricularVO gradeCurricularVO, HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs, boolean realizandoBaixaAutomatica, UsuarioVO usuarioVO) throws Exception;
    
    public void removerGradeDisciplinaEquivalente(List<PeriodoLetivoVO> listaPeriodoLetivoVOs, GradeDisciplinaVO gradeDisciplinaExcluirVO);
    
    public void removerMatriculaPeriodoTurmaDisciplinaEquivalenteCursada(MatriculaPeriodoVO obj, Integer codigoDisciplina);
    
    public MatriculaPeriodoVO verificarQualMatriculaPeriodoCarregarDadosParaChoqueHorario(MatriculaVO matriculaVO, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina, HashMap<String, MatriculaPeriodoVO> mapMatriculaPeriodoVOs, UsuarioVO usuario) throws Exception;
    
    public void alterarSituacaoDisciplinaCursandoPorEquivalenciaAposRemocaoDisciplinaEquivale(List<PeriodoLetivoVO> listaPeriodoLetivoVOs, GradeDisciplinaVO gradeDisciplinaEquivaleVO, MapaEquivalenciaDisciplinaVO mapaVisualizar);

	void adicionarEValidarMatriculaPeriodoTurmaDisciplinaVO(MatriculaPeriodoVO matriculaPeriodoVO, MatriculaVO matriculaVO, GradeDisciplinaVO gradeDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, Boolean liberarRealizarMatriculaDisciplinaPreRequisito, MatriculaPeriodoTurmaDisciplinaVO matricaPeriodoTurmaDisciplina,  ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, PeriodoLetivoComHistoricoAlunoVO periodoLetivoComHistoricoAlunoVO, Boolean liberarChoqueHorario, UsuarioVO usuario, GradeCurricularVO gradeCurricularSelecionadaVO, Boolean considerarVagaReposicao, Boolean realizandoRecebimento) throws Exception;

	void realizarDefinicaoNumeroVagasDisciplinaCompostaPorEscolha(GradeDisciplinaVO gradeDisciplinaVO) throws Exception;
	
	public void incluirDisciplinaApartirDoRequerimento(RequerimentoVO requerimentoVO, UsuarioVO usuarioVO, Boolean realizandoRecebimento, boolean realizandoBaixaAutomatica) throws Exception;

	MatriculaPeriodoTurmaDisciplinaVO realizarPreenchimentoDadosMatriculaPeriodoTurmaDisciplinaAdicionarPorRequerimento(RequerimentoVO requerimentoVO, boolean validarChoqueHorario, boolean validarVagasReposicao, UsuarioVO usuarioVO) throws Exception;
}
