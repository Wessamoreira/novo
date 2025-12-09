package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

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

import com.amazonaws.services.inspector.model.AgentHealth;

import controle.arquitetura.DataModelo;
import integracoes.cobranca.CECAM;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaUnidadeEnsinoVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberItemVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.TipoDocumentoPendenciaAgenteCobrancaVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroNegativacaoCobrancaContaReceberInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Lazy
@Repository
@Scope("singleton")
public class RegistroNegativacaoCobrancaContaReceber extends ControleAcesso implements RegistroNegativacaoCobrancaContaReceberInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public RegistroNegativacaoCobrancaContaReceber() throws Exception {
		super();
		setIdEntidade("RegistroNegativacaoCobrancaContaReceber");
	}

	public void setIdEntidade(String idEntidade) {
		RegistroNegativacaoCobrancaContaReceber.idEntidade = idEntidade;
	}
	
	@Override
	public void validarDados(UnidadeEnsinoVO unidadeEnsino, AgenteNegativacaoCobrancaContaReceberVO agente, Date dataFim) throws ConsistirException {
		if (dataFim.after(new Date())) {
			throw new ConsistirException("O campo Data Final (Registro Negativação/Cobrança Conta Receber) deve ser menor ou igual a data de hoje.");
		}
		if (!Uteis.isAtributoPreenchido(agente) || !Uteis.isAtributoPreenchido(agente.getCodigo())) {
			throw new ConsistirException("O campo Agente (Registro Negativação/Cobrança Conta Receber) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			throw new ConsistirException("O campo Unidade de Ensino (Registro Negativação/Cobrança Conta Receber) deve ser informado.");
		}
	}
	
	@Override
	public void validarDadosExclusao(RegistroNegativacaoCobrancaContaReceberItemVO registroExclusao) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(registroExclusao.getMotivo())) {
			throw new ConsistirException("O campo Motivo (Registro Cobrança Conta Receber Exclusão) deve ser informado.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RegistroNegativacaoCobrancaContaReceberVO criarRegistro(AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs, String centroReceita) throws Exception {
		try {
			return executarMontagemRegistroNegativacaoCobrancaContaReceberVO(agente, tipoAgente, unidadeEnsino, curso, turma, matricula, usuarioVO, periodoInicial, periodoFinal, aluno, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceita, centroReceitaVOs);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO config) throws Exception {
		AgenteNegativacaoCobrancaContaReceberVO anccr =  getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(registroExecucaoJobVO.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());
		Uteis.checkState((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && anccr.getAmbienteAgenteNegativacaoCobranca().isProducao(), "O SEI está em um ambiente diferente de Produção e por isso não é possível executar  a Integração do Agente de Negativação para ambiente de Produção.");
			for (AgenteNegativacaoCobrancaUnidadeEnsinoVO ancue : anccr.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs()) {
				RegistroNegativacaoCobrancaContaReceberVO rnccr = new RegistroNegativacaoCobrancaContaReceberVO();
				rnccr.setRegistrarNegativacaoContaReceberViaIntegracao(true);
				rnccr.setAgente(anccr);
				rnccr.setUnidadeEnsinoVO(ancue.getUnidadeEnsino());
				rnccr.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, new UsuarioVO()));
				rnccr.setTipoAgente(rnccr.getAgente().getTipo());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(anccr.getTipoOrigemBiblioteca());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(anccr.getTipoOrigemBolsaCusteadaConvenio());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(anccr.getTipoOrigemContratoReceita());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(anccr.getTipoOrigemDevolucaoCheque());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(anccr.getTipoOrigemInclusaoReposicao());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(anccr.getTipoOrigemMatricula());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(anccr.getTipoOrigemMensalidade());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(anccr.getTipoOrigemNegociacao());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(anccr.getTipoOrigemOutros());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(anccr.getTipoOrigemInscricaoProcessoSeletivo());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(anccr.getTipoOrigemRequerimento());
				rnccr.getFiltroRelatorioFinanceiroVO().setTipoOrigemMaterialDidatico(anccr.getTipoOrigemMaterialDidatico());
				rnccr.setListaContasReceberCobranca(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().execultarConsultaContaReceberPendente(rnccr.getAgente().getTipo(), rnccr.getAgente(), rnccr.getUnidadeEnsinoVO(), null, null, null,	rnccr.getUsuarioVO(), null, null, null, rnccr.getFiltroRelatorioFinanceiroVO(), null,true));				
				if(!rnccr.getListaContasReceberCobranca().isEmpty()) {
					rnccr.setDataGeracao(new Date());
					rnccr.setDataInicioFiltro(rnccr.getListaContasReceberCobranca().stream().min(Comparator.comparing(p-> p.getDataVencimento())).get().getDataVencimento());
					rnccr.setDataFimFiltro(rnccr.getListaContasReceberCobranca().stream().max(Comparator.comparing(p-> p.getDataVencimento())).get().getDataVencimento());
					getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().persistir(rnccr, false, config, rnccr.getUsuarioVO());					
				}			
			}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarRemocaoRegistroPorAgenteNegativacaoCobrancaContaReceberPorAPI(RegistroExecucaoJobVO registroExecucaoJobVO, ConfiguracaoGeralSistemaVO config) throws Exception {
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(config.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, new UsuarioVO());
		AgenteNegativacaoCobrancaContaReceberVO anccr =  getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(registroExecucaoJobVO.getCodigoOrigem(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		Uteis.checkState((!Uteis.isAmbienteProducao() || Uteis.isVersaoDev()) && anccr.getAmbienteAgenteNegativacaoCobranca().isProducao(), "O SEI está em um ambiente diferente de Produção e por isso não é possível executar  a Integração do Agente de Negativação para ambiente de Produção.");
		List<UnidadeEnsinoVO> unidadeEnsinos = new ArrayList<>();
		anccr.getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().stream().forEach(p -> {
			p.getUnidadeEnsino().setFiltrarUnidadeEnsino(true);
			unidadeEnsinos.add(p.getUnidadeEnsino());
		});
		List<RegistroNegativacaoCobrancaContaReceberItemVO> lista = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().consultarRegistroNegativacaoCobrancaContaReceberItem(anccr, anccr.getSituacaoParcelaRemoverSerasaApiGeo().getValor(), anccr.getTipo(), null, null, anccr.getSituacaoParcelaRemoverNegociada(), "", unidadeEnsinos, "", null, null, null, null, "REGISTRADO", null);
		lista.parallelStream().forEach(p -> p.setSelecionado(true));
		getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().excluirNegativacaoCobrancaListagemVOs(anccr, lista, "Remoção Automatica por Api de Integração", true, false, config, usuario);
	}

	
	
//	@Override
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	public RegistroNegativacaoCobrancaContaReceberVO gravarRegistro(RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO, UsuarioVO usuarioLogado) throws Exception {
//		try {
//			incluir(registroNegativacaoCobrancaContaReceberVO, usuarioLogado);
//			for (RegistroNegativacaoCobrancaContaReceberItemVO item : registroNegativacaoCobrancaContaReceberVO.getListaContasReceberCobranca()) {
//				if (registroNegativacaoCobrancaContaReceberVO.getTipoAgenteCobranca()) {
//					getFacadeFactory().getContaReceberFacade().executarBloqueioContaReceber(item.getContaReceber(), registroNegativacaoCobrancaContaReceberVO.getCodigo(), registroNegativacaoCobrancaContaReceberVO.getUsuarioVO());
//				} else if (registroNegativacaoCobrancaContaReceberVO.getTipoAgenteNegativacao()) {
//					ContaReceberNegativacaoVO obj = new ContaReceberNegativacaoVO();
//					obj.getContaReceber().setCodigo(item.getContaReceber());
//					obj.setRegistro(registroNegativacaoCobrancaContaReceberVO);
//					incluirContaReceberNegativacao(obj, usuarioLogado);
//				}
//			}
//			return registroNegativacaoCobrancaContaReceberVO;
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarExclusaoRegistroNegativacaoCobranca(RegistroNegativacaoCobrancaContaReceberItemVO item, UsuarioVO usuario) throws Exception {
		validarDadosExclusao(item);
		incluirRegistroNegativacaoCobrancaContaReceberExclusao(item, usuario);
		getFacadeFactory().getContaReceberFacade().executarDesbloqueioContaReceber(item.getContaReceber(), usuario);
	}
	
	public void executarExclusaoTotalRegistroNegativacaoCobranca(RegistroNegativacaoCobrancaContaReceberVO registro, String motivo, UsuarioVO usuario) throws Exception {
		for (RegistroNegativacaoCobrancaContaReceberItemVO item : registro.getListaContasReceberCobranca()) {
			if (item.getMotivo().trim().isEmpty()) {
				item.setRegistroNegativacaoCobrancaContaReceber(registro.getCodigo());
				item.setCodigoUsuario(usuario.getCodigo());
				item.setNomeUsuario(usuario.getNome());
				item.setDataExclusao(new Date());
				item.setMotivo(motivo);
				validarDadosExclusao(item);
				incluirRegistroNegativacaoCobrancaContaReceberExclusao(item, usuario);
				getFacadeFactory().getContaReceberFacade().executarDesbloqueioContaReceber(item.getContaReceber(), usuario);
			}
		}
	}
	
	private RegistroNegativacaoCobrancaContaReceberVO executarMontagemRegistroNegativacaoCobrancaContaReceberVO(AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, String aluno, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String centroReceita, List<CentroReceitaVO> centroReceitaVOs) throws Exception {
		RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceberVO = new RegistroNegativacaoCobrancaContaReceberVO();
		try {
			registroNegativacaoCobrancaContaReceberVO.setDataGeracao(new Date());
			registroNegativacaoCobrancaContaReceberVO.setAgente(agente);
			registroNegativacaoCobrancaContaReceberVO.setTipoAgente(tipoAgente);
			registroNegativacaoCobrancaContaReceberVO.setAluno(aluno);
			registroNegativacaoCobrancaContaReceberVO.setMatricula(matricula.getMatricula());
			registroNegativacaoCobrancaContaReceberVO.setCursoVO(curso);
			registroNegativacaoCobrancaContaReceberVO.setTurmaVO(turma);
			registroNegativacaoCobrancaContaReceberVO.setUsuarioVO(usuarioVO);
			registroNegativacaoCobrancaContaReceberVO.setUnidadeEnsinoVO(unidadeEnsino);
			registroNegativacaoCobrancaContaReceberVO.setDataInicioFiltro(periodoInicial);
			registroNegativacaoCobrancaContaReceberVO.setDataFimFiltro(periodoFinal);
			registroNegativacaoCobrancaContaReceberVO.setCentroReceitaApresentar(centroReceita);
			if (matricula.getMatricula().equals("")) {
				registroNegativacaoCobrancaContaReceberVO.setFiltroRelatorioAcademicoVO(filtroRelatorioAcademicoVO);
			}
			registroNegativacaoCobrancaContaReceberVO.setFiltroRelatorioFinanceiroVO(filtroRelatorioFinanceiroVO);
			registroNegativacaoCobrancaContaReceberVO.setListaContasReceberCobranca(execultarConsultaContaReceberPendente(tipoAgente, agente, unidadeEnsino, curso, turma, matricula, usuarioVO, periodoInicial, periodoFinal, filtroRelatorioAcademicoVO, filtroRelatorioFinanceiroVO, centroReceitaVOs , false));
			if (registroNegativacaoCobrancaContaReceberVO.getListaContasReceberCobranca().isEmpty()) {
				throw new Exception("Não foi encontrada nenhuma Conta Receber com os parâmetros informados!");
			}
		} catch (Exception e) {
			throw e;
		}
		return registroNegativacaoCobrancaContaReceberVO;
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> execultarConsultaContaReceberPendente(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, AgenteNegativacaoCobrancaContaReceberVO agente, UnidadeEnsinoVO unidadeEnsino, CursoVO curso, TurmaVO turma, MatriculaVO matricula, UsuarioVO usuarioVO, Date periodoInicial, Date periodoFinal, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<CentroReceitaVO> centroReceitaVOs , Boolean jobSerasaApiGeo) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select");
		sql.append(" resultado.matricula, resultado.matriculaperiodo, resultado.curso,resultado.aluno,resultado.alunocpf,resultado.alunodatanascimento,resultado.alunocep,resultado.alunoendereco,");
		sql.append(" resultado.alunocomplemento,resultado.alunonumero,resultado.alunosetor,cidadealuno.alunocidade,cidadealuno.alunouf,resultado.responsavelfinanceiro,");
		sql.append(" resultado.responsavelfinanceirocpf, resultado.responsavelfinanceirodatanascimento, resultado.responsavelfinanceirocep, resultado.responsavelfinanceiroendereco,");
		sql.append(" resultado.responsavelfinanceirocomplemento, resultado.responsavelfinanceironumero, resultado.responsavelfinanceirosetor, resultado.cidaderesponsavelfinanceiro, resultado.estadoresponsavelfinanceiro, resultado.unidadeensino,");
		sql.append(" resultado.unidadeensinocnpj, resultado.contareceber, resultado.parcela, resultado.nossonumero, resultado.datavencimento, resultado.valor, resultado.nomeusuario, resultado.dataexclusao,");
		sql.append(" resultado.motivo, resultado.codigousuarioexclusao, resultado.nomeusuarioexclusao, resultado.situacao");
		sql.append(" from ( ");	
		sql.append(" select distinct ma.matricula, contareceber.matriculaperiodo, curso.nome curso, ");
		sql.append(" aluno.nome aluno, aluno.cpf alunocpf, aluno.datanasc alunoDataNascimento, ");
		sql.append(" aluno.cep alunocep, aluno.endereco alunoendereco, aluno.complemento alunocomplemento, ");
		sql.append(" aluno.numero alunonumero, aluno.setor alunosetor, aluno.cidade as codigocidadealuno, ");
		
		sql.append(" responsavelfinanceiro.nome responsavelfinanceiro, responsavelfinanceiro.cpf responsavelfinanceirocpf, responsavelfinanceiro.datanasc responsavelfinanceiroDataNascimento, ");
		sql.append(" responsavelfinanceiro.cep responsavelfinanceirocep, responsavelfinanceiro.endereco responsavelfinanceiroendereco, responsavelfinanceiro.complemento responsavelfinanceirocomplemento, ");
		sql.append(" responsavelfinanceiro.numero responsavelfinanceironumero, responsavelfinanceiro.setor responsavelfinanceirosetor,  ");   
		sql.append(" cidaderesponsavelfinanceiro.nome cidaderesponsavelfinanceiro, estadoresponsavelfinanceiro.sigla estadoresponsavelfinanceiro,  ");		
		
		sql.append(" unidadeEnsino.razaosocial unidadeensino, unidadeEnsino.cnpj unidadeensinocnpj, ");
		// agente de cobranca
		if (tipoAgente.equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA)) {
			sql.append(" (select registroNegativacaoCobrancaContaReceberItem.codigo from registronegativacaocobrancacontareceber ");
			sql.append(" inner join registroNegativacaoCobrancaContaReceberItem on registroNegativacaoCobrancaContaReceberItem.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber.codigo ");
			sql.append(" inner join agentenegativacaocobrancacontareceber on agentenegativacaocobrancacontareceber.codigo =  registroNegativacaoCobrancaContaReceber.agente ");
			sql.append(" where 1=1 ");
			sql.append(" and agentenegativacaocobrancacontareceber.tipo = 'COBRANCA' ");
			sql.append(" and registroNegativacaoCobrancaContaReceberItem.dataexclusao is null ");
			sql.append(" and registroNegativacaoCobrancaContaReceberItem.nossonumero = contareceber.nossonumero) as agenteCobranca, ");
		}
		sql.append(" contareceber.codigo contareceber, contareceber.parcela, contareceber.nossonumero, contareceber.datavencimento, contareceber.valor,");
		sql.append(" null nomeUsuario, null dataExclusao, null motivo, null codigoUsuarioExclusao, '' nomeUsuarioExclusao, contareceber.situacao ");
		sql.append(" from contareceber ");
		sql.append(" inner join matricula as ma on contareceber.matriculaaluno = ma.matricula ");
		sql.append(" inner join matriculaperiodo map on contareceber.matriculaaluno = map.matricula");
		sql.append(" and map.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = contareceber.matriculaaluno order by mp.ano ||'/'|| mp.semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)");
		sql.append(" inner join curso on ma.curso = curso.codigo");
		sql.append(" inner join turma on turma.codigo= map.turma");
		sql.append(" inner join unidadeEnsino on contareceber.unidadeEnsinofinanceira = unidadeEnsino.codigo");
		sql.append(" inner join pessoa as aluno on ma.aluno = aluno.codigo");		
		sql.append(" left join pessoa responsavelfinanceiro  on responsavelfinanceiro.codigo = contareceber.responsavelfinanceiro");
		sql.append(" left join cidade as cidaderesponsavelfinanceiro on cidaderesponsavelfinanceiro.codigo = responsavelfinanceiro.cidade");
		sql.append(" left join estado as estadoresponsavelfinanceiro on estadoresponsavelfinanceiro.codigo = cidaderesponsavelfinanceiro.estado");
		sql.append(" left join fornecedor on fornecedor.codigo = contareceber.fornecedor");
		sql.append(" left join parceiro on parceiro.codigo = contareceber.parceiro");
		
		sql.append(" WHERE contareceber.situacao = 'AR' ");
		if (!jobSerasaApiGeo && matricula.getMatricula().equals("")) {
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "map"));
		}
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
			sql.append(" AND contareceber.unidadeensinofinanceira=").append(unidadeEnsino.getCodigo());
		}
		if (curso != null && curso.getCodigo().intValue() != 0) {
			sql.append(" AND curso.codigo=").append(curso.getCodigo());
		}
		if (turma != null && turma.getCodigo().intValue() != 0) {
			sql.append(" AND turma.codigo=").append(turma.getCodigo());
		}

		if (matricula != null && !matricula.getMatricula().equals("")) {
			sql.append(" AND ma.matricula like('").append(matricula.getMatricula()).append("%')");
		}		
	
		if (jobSerasaApiGeo) {
			sql.append(" and ").append(getSqlAdicionarFiltrosSerasaApiGeo(tipoAgente, agente, unidadeEnsino));
		}else {
			sql.append(" AND ((contareceber.datavencimento >='").append(Uteis.getDataJDBC(periodoInicial)).append("' and contareceber.datavencimento <= '").append(Uteis.getDataJDBC(periodoFinal)).append("')").append(" and contareceber.datavencimento < CURRENT_DATE)");
			StringBuilder sql2 = new StringBuilder("");
			for (CentroReceitaVO centroReceitaVO : centroReceitaVOs) {
				if (centroReceitaVO.getFiltrarCentroReceitaVO()) {
					sql2.append(sql2.length() == 0 ? " and contareceber.centroreceita in(" : ", ").append(centroReceitaVO.getCodigo());
				}
			}
			if (sql2.length() > 0) {
				sql2.append(") ");
				sql.append(sql2);
			}
			sql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO,  "contareceber"));
		}
		sql.append(getSqlRegistroNegativacaoCobrancaContaReceberItem(agente.getNegativarTodasParcelas(), tipoAgente , agente.getCodigo(), "contareceber.nossonumero"));		
		sql.append(" ) as resultado ");
		sql.append(" inner join lateral ( ");
		sql.append(" select estado.sigla as alunouf ,cidade.nome as alunocidade ");
		sql.append(" from cidade ");
		sql.append(" inner join estado on cidade.estado = estado.codigo ");
		sql.append(" where cidade.codigo = resultado.codigocidadealuno ");
		sql.append(" ) as cidadealuno on 1=1 ");
		sql.append(" order by resultado.unidadeensino, resultado.curso, resultado.aluno ");
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosRegistroNegativacaoCobrancaContaReceberItem(agente, sqlRowSet);
	}
	
	private List<RegistroNegativacaoCobrancaContaReceberItemVO> montarDadosConsulta(SqlRowSet sql2) throws Exception {
		List<RegistroNegativacaoCobrancaContaReceberItemVO> listaConsulta = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		while (sql2.next()) {
			RegistroNegativacaoCobrancaContaReceberItemVO obj = montarDados(sql2);
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}

	private RegistroNegativacaoCobrancaContaReceberItemVO montarDados(SqlRowSet sql2) {
		RegistroNegativacaoCobrancaContaReceberItemVO obj = new RegistroNegativacaoCobrancaContaReceberItemVO();
		obj.setContaReceber(sql2.getInt("contareceber"));
		obj.setUnidadeEnsino(sql2.getString("unidadeensino"));
		obj.setUnidadeEnsinoCnpj(sql2.getString("unidadeensinocnpj"));
		obj.setCurso(sql2.getString("curso"));			
		obj.setMatricula(sql2.getString("matricula"));
		obj.setMatriculaPeriodo(sql2.getInt("matriculaPeriodo"));
		obj.setNossoNumero(sql2.getString("nossonumero"));
		obj.setParcela(sql2.getString("parcela"));
		obj.setDataVencimento(sql2.getDate("dataVencimento"));
		obj.setValor(sql2.getDouble("valor"));
		obj.setNomeUsuario(sql2.getString("nomeUsuario"));
		obj.setDataExclusao(sql2.getDate("dataExclusao"));
		obj.setNomeUsuarioExclusao(sql2.getString("nomeUsuarioExclusao"));
		obj.setCodigoUsuarioExclusao(sql2.getInt("codigoUsuarioExclusao"));
		obj.setMotivo(sql2.getString("motivo"));
		obj.setSituacaoContaReceber(sql2.getString("situacao"));
		obj.setAluno(sql2.getString("aluno"));
		obj.setAlunoCpf(sql2.getString("alunocpf"));
		obj.setAlunoDataNascimento(sql2.getDate("alunoDataNascimento"));
		obj.setAlunoCep(sql2.getString("alunoCep"));
		obj.setAlunoEndereco(sql2.getString("alunoEndereco"));
		obj.setAlunoNumero(sql2.getString("alunoNumero"));
		obj.setAlunoBairro(sql2.getString("alunoBairro"));
		obj.setAlunoMunicipio(sql2.getString("alunoMunicipio"));
		obj.setAlunoUf(sql2.getString("alunouf"));
		obj.setCodigoIntegracaoApi(sql2.getString("codigoIntegracaoApi"));
		obj.setTipoContratoAgenteNegativacaoCobrancaEnum(TipoContratoAgenteNegativacaoCobrancaEnum.valueOf(sql2.getString("tipoContratoAgenteNegativacaoCobrancaEnum")));
		return obj;
	}
	
	private List<RegistroNegativacaoCobrancaContaReceberItemVO> montarDadosRegistroNegativacaoCobrancaContaReceberItem(AgenteNegativacaoCobrancaContaReceberVO agente, SqlRowSet sql2) throws Exception {
		List<RegistroNegativacaoCobrancaContaReceberItemVO> listaConsulta = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>(0);
		while (sql2.next()) {
			RegistroNegativacaoCobrancaContaReceberItemVO obj = new RegistroNegativacaoCobrancaContaReceberItemVO();
			obj.setContaReceber(sql2.getInt("contareceber"));
			obj.setUnidadeEnsino(sql2.getString("unidadeensino"));
			obj.setUnidadeEnsinoCnpj(sql2.getString("unidadeensinocnpj"));
			obj.setCurso(sql2.getString("curso"));			
			obj.setMatricula(sql2.getString("matricula"));
			obj.setMatriculaPeriodo(sql2.getInt("matriculaPeriodo"));
			obj.setNossoNumero(sql2.getString("nossonumero"));
			obj.setParcela(sql2.getString("parcela"));
			obj.setDataVencimento(sql2.getDate("dataVencimento"));
			obj.setValor(sql2.getDouble("valor"));
			obj.setNomeUsuario(sql2.getString("nomeUsuario"));
			obj.setDataExclusao(sql2.getDate("dataExclusao"));
			obj.setNomeUsuarioExclusao(sql2.getString("nomeUsuarioExclusao"));
			obj.setCodigoUsuarioExclusao(sql2.getInt("codigoUsuarioExclusao"));
			obj.setMotivo(sql2.getString("motivo"));
			obj.setSituacaoContaReceber(sql2.getString("situacao"));
			if(sql2.getString("responsavelfinanceiro") != null && sql2.getString("responsavelfinanceiro") != "") {
				obj.setAluno(sql2.getString("responsavelfinanceiro"));
				obj.setAlunoCpf(sql2.getString("responsavelfinanceirocpf"));
				obj.setAlunoDataNascimento(sql2.getDate("responsavelfinanceiroDataNascimento"));
				obj.setAlunoCep(sql2.getString("responsavelfinanceirocep"));
				obj.setAlunoEndereco(sql2.getString("responsavelfinanceiroendereco") +" - "+sql2.getString("responsavelfinanceirocomplemento"));
				obj.setAlunoNumero(sql2.getString("responsavelfinanceironumero"));
				obj.setAlunoBairro(sql2.getString("responsavelfinanceirosetor"));
				obj.setAlunoMunicipio(sql2.getString("cidaderesponsavelfinanceiro"));
				obj.setAlunoUf(sql2.getString("estadoresponsavelfinanceiro"));
			}else {
				obj.setAluno(sql2.getString("aluno"));
				obj.setAlunoCpf(sql2.getString("alunocpf"));
				obj.setAlunoDataNascimento(sql2.getDate("alunoDataNascimento"));
				obj.setAlunoCep(sql2.getString("alunoCep"));
				obj.setAlunoEndereco(sql2.getString("alunoEndereco") +" - "+sql2.getString("alunocomplemento"));
				obj.setAlunoNumero(sql2.getString("alunoNumero"));
				obj.setAlunoBairro(sql2.getString("alunoSetor"));
				obj.setAlunoMunicipio(sql2.getString("alunoCidade"));
				obj.setAlunoUf(sql2.getString("alunouf"));	
			}
			obj.setTipoContratoAgenteNegativacaoCobrancaEnum(agente.getTipoContratoAgenteNegativacaoCobrancaEnum());
			listaConsulta.add(obj);
		}
		return listaConsulta;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RegistroNegativacaoCobrancaContaReceberVO obj, boolean verificarAcesso, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception{
		if(obj.getAgente().getIntegracaoSerasaApiGeo() && obj.isRegistrarNegativacaoContaReceberViaIntegracao()) {
			for (RegistroNegativacaoCobrancaContaReceberItemVO rnccri : obj.getListaContasReceberCobranca()) {
				getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().executarEnvioIntegracaoWebService(obj.getAgente(), rnccri, JobsEnum.JOB_SERASA_API_GEO_REGISTRAR, config, usuarioVO);
			}
		}
		if (obj.getCodigo() == 0) {
			incluir(obj, usuarioVO);
		} else {
			//alterar(obj,  usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RegistroNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			final String sql = "INSERT INTO registronegativacaocobrancacontareceber (aluno, matricula, curso, turma, usuario, unidadeEnsino, dataGeracao, dataInicioFiltro, dataFimFiltro, tipoOrigemInscricaoProcessoSeletivo, tipoOrigemMatricula, tipoOrigemRequerimento, "+
			" tipoOrigemBiblioteca, tipoOrigemMensalidade, tipoOrigemDevolucaoCheque, tipoOrigemNegociacao, tipoOrigemBolsaCusteadaConvenio, tipoOrigemContratoReceita, tipoOrigemInclusaoReposicao, tipoOrigemOutros, "+ 
			" ativo, preMatricula, preMatriculaCancelada, trancado, abandonado, transferenciaInterna, transferenciaExterna, cancelado, concluido, formado, centroReceitaApresentar, agente, tipoAgente) " + 
			" VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getAluno());
					if (obj.getMatricula().isEmpty()) {
						sqlInserir.setNull(2, 0);
					} else {
						sqlInserir.setString(2, obj.getMatricula());
					}
					if (obj.getCursoVO().getCodigo().intValue() > 0) {
						sqlInserir.setInt(3, obj.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setInt(4, obj.getTurmaVO().getCodigo());
					sqlInserir.setInt(5, obj.getUsuarioVO().getCodigo());
					sqlInserir.setInt(6, obj.getUnidadeEnsinoVO().getCodigo());
					sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataGeracao()));
					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataInicioFiltro()));
					sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataFimFiltro()));
					sqlInserir.setBoolean(10, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemInscricaoProcessoSeletivo());
					sqlInserir.setBoolean(11, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemMatricula());
					sqlInserir.setBoolean(12, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemRequerimento());
					sqlInserir.setBoolean(13, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemBiblioteca());
					sqlInserir.setBoolean(14, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemMensalidade());
					sqlInserir.setBoolean(15, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemDevolucaoCheque());
					sqlInserir.setBoolean(16, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemNegociacao());
					sqlInserir.setBoolean(17, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemBolsaCusteadaConvenio());
					sqlInserir.setBoolean(18, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemContratoReceita());
					sqlInserir.setBoolean(19, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemInclusaoReposicao());
					sqlInserir.setBoolean(20, obj.getFiltroRelatorioFinanceiroVO().getTipoOrigemOutros());
					sqlInserir.setBoolean(21, obj.getFiltroRelatorioAcademicoVO().getAtivo());
					sqlInserir.setBoolean(22, obj.getFiltroRelatorioAcademicoVO().getPreMatricula());
					sqlInserir.setBoolean(23, obj.getFiltroRelatorioAcademicoVO().getPreMatriculaCancelada());
					sqlInserir.setBoolean(24, obj.getFiltroRelatorioAcademicoVO().getTrancado());
					sqlInserir.setBoolean(25, obj.getFiltroRelatorioAcademicoVO().getAbandonado());
					sqlInserir.setBoolean(26, obj.getFiltroRelatorioAcademicoVO().getTransferenciaInterna());
					sqlInserir.setBoolean(27, obj.getFiltroRelatorioAcademicoVO().getTransferenciaExterna());
					sqlInserir.setBoolean(28, obj.getFiltroRelatorioAcademicoVO().getCancelado());
					sqlInserir.setBoolean(29, obj.getFiltroRelatorioAcademicoVO().getConcluido());
					sqlInserir.setBoolean(30, obj.getFiltroRelatorioAcademicoVO().getFormado());
					sqlInserir.setString(31, obj.getCentroReceitaApresentar());
					sqlInserir.setInt(32, obj.getAgente().getCodigo());
					sqlInserir.setString(33, obj.getTipoAgente().name());					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberItemFacade().incluirListaVOs(obj.getListaContasReceberCobranca(), obj, usuarioLogado);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirRegistroNegativacaoCobrancaContaReceberExclusao(final RegistroNegativacaoCobrancaContaReceberItemVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			final String sql = "INSERT INTO registronegativacaocobrancacontareceberexclusao (aluno, matricula, curso, codigousuario, nomeusuario, unidadeEnsino, contaReceber, parcela, dataVencimento, valor, dataExclusao, motivo, registronegativacaocobrancacontareceber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getAluno());
					sqlInserir.setString(2, obj.getMatricula());
					sqlInserir.setString(3, obj.getCurso());
					sqlInserir.setInt(4, obj.getCodigoUsuario());
					sqlInserir.setString(5, obj.getNomeUsuario());
					sqlInserir.setString(6, obj.getUnidadeEnsino());
					sqlInserir.setInt(7, obj.getContaReceber());
					sqlInserir.setString(8, obj.getParcela());
					sqlInserir.setDate(9, Uteis.getDataJDBC(obj.getDataVencimento()));
					sqlInserir.setDouble(10, obj.getValor());
					sqlInserir.setDate(11, Uteis.getDataJDBC(obj.getDataExclusao()));
					sqlInserir.setString(12, obj.getMotivo());
					sqlInserir.setInt(13, obj.getRegistroNegativacaoCobrancaContaReceber());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}
	
//	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//	private void incluirContaReceberNegativacao(final ContaReceberNegativacaoVO obj, final UsuarioVO usuarioLogado) throws Exception {
//		try {
//			final String sql = "INSERT INTO contarecebernegativacao (contaReceber, registro) VALUES (?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
//			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
//				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//					sqlInserir.setInt(1, obj.getContaReceber().getCodigo());
//					sqlInserir.setInt(2, obj.getRegistro().getCodigo());
//					return sqlInserir;
//				}
//			}, new ResultSetExtractor<Object>() {
//				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
//					if (arg0.next()) {
//						obj.setNovoObj(Boolean.FALSE);
//						return arg0.getInt("codigo");
//					}
//					return null;
//				}
//			}));
//			obj.setNovoObj(Boolean.FALSE);
//		} catch (Exception e) {
//			obj.setNovoObj(true);
//			throw e;
//		}
//	}
	
	@Override
	public List<SelectItem> montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("", unidadeEnsinoLogado, usuarioLogado);
		return UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome");
	}
	
	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, unidadeEnsinoLogado.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registronegativacaocobrancacontareceber.*, dataregistro, ");
        sqlStr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
        sqlStr.append(" from registronegativacaocobrancacontareceber ");
        sqlStr.append(" inner JOIN usuario ON usuario.codigo = registronegativacaocobrancacontareceber.usuario ");
        sqlStr.append(" inner JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
        sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
        return sqlStr;
    }
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorUsuario(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND upper(sem_acentos(pessoa.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		}		
		if(Uteis.isAtributoPreenchido(dataGeracaoInicio) && Uteis.isAtributoPreenchido(dataGeracaoFim)) {
			sqlStr.append(" AND (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorMatricula(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean validarDataExclusao ,  boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" inner join contareceber on contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceberitem.matricula = '").append(valorConsulta.toUpperCase()).append("' ");
		}
		
		if(validarDataExclusao ) {
			sqlStr.append(" AND (registronegativacaocobrancacontareceberitem.dataexclusao is null )");
		}
		if(Uteis.isAtributoPreenchido(dataGeracaoInicio) && Uteis.isAtributoPreenchido(dataGeracaoFim)) {
			sqlStr.append(" AND (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by dataregistro ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorNossoNumero(Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceberitem.nossonumero = '").append(valorConsulta.toUpperCase()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorAluno(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND upper(sem_acentos(registronegativacaocobrancacontareceberitem.aluno)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		}
		if(Uteis.isAtributoPreenchido(dataGeracaoInicio) && Uteis.isAtributoPreenchido(dataGeracaoFim)) {
			sqlStr.append(" AND (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorTurma(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		//StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registronegativacaocobrancacontareceber.*, turma.identificadorTurma, dataregistro,  ");
        sqlStr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
        sqlStr.append(" from registronegativacaocobrancacontareceber ");
        sqlStr.append(" inner JOIN usuario ON usuario.codigo = registronegativacaocobrancacontareceber.usuario ");
        sqlStr.append(" inner JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
        sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
		sqlStr.append(" left JOIN turma ON turma.codigo = registronegativacaocobrancacontareceber.turma ");
		sqlStr.append(" left JOIN contareceber ON contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");
		sqlStr.append(" left JOIN turma as turmacontareceber ON contareceber.turma = turmacontareceber.codigo ");
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND (upper(sem_acentos(turma.identificadorTurma)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
			sqlStr.append(" or upper(sem_acentos(turmacontareceber.identificadorTurma)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%')))");
		}
		if(Uteis.isAtributoPreenchido(dataGeracaoInicio) && Uteis.isAtributoPreenchido(dataGeracaoFim)) {
			sqlStr.append(" AND (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by turma.identificadorTurma, dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorCurso(Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		//StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registronegativacaocobrancacontareceber.*, curso.nome, dataregistro,  ");
        sqlStr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
        sqlStr.append(" from registronegativacaocobrancacontareceber ");
        sqlStr.append(" inner JOIN usuario ON usuario.codigo = registronegativacaocobrancacontareceber.usuario ");
        sqlStr.append(" inner JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
        sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = registronegativacaocobrancacontareceber.curso ");
		sqlStr.append(" WHERE 1=1");
		if(Uteis.isAtributoPreenchido(valorConsulta)) {
			sqlStr.append(" AND upper(sem_acentos(curso.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		}
		if(Uteis.isAtributoPreenchido(dataGeracaoInicio) && Uteis.isAtributoPreenchido(dataGeracaoFim)) {
			sqlStr.append(" AND (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		}
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStr.append(" order by curso.nome, dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}
	
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		//StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registronegativacaocobrancacontareceber.*, unidadeensino.nome, dataregistro,  ");
        sqlStr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
        sqlStr.append(" from registronegativacaocobrancacontareceber ");
        sqlStr.append(" inner JOIN usuario ON usuario.codigo = registronegativacaocobrancacontareceber.usuario ");
        sqlStr.append(" inner JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
        sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
		sqlStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = registronegativacaocobrancacontareceber.unidadeensino ");
		sqlStr.append(" WHERE upper(sem_acentos(unidadeensino.nome)) like upper(sem_acentos('%").append(valorConsulta.toUpperCase()).append("%'))");
		sqlStr.append(" order by unidadeensino.nome, dataregistro ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	@Override
	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarPorDataGeracao(Date dataGeracaoInicio, Date dataGeracaoFim, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (dataGeracao >='").append(Uteis.getDataJDBC(dataGeracaoInicio)).append("' and dataGeracao <='").append(Uteis.getDataJDBC(dataGeracaoFim)).append("')");
		sqlStr.append(" order by registronegativacaocobrancacontareceber.datageracao ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
	}

	public static List<RegistroNegativacaoCobrancaContaReceberVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RegistroNegativacaoCobrancaContaReceberVO> vetResultado = new ArrayList<RegistroNegativacaoCobrancaContaReceberVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	public static List<RegistroNegativacaoCobrancaContaReceberVO> montarDadosConsulta(SqlRowSet tabelaResultado, Integer codigoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<RegistroNegativacaoCobrancaContaReceberVO> vetResultado = new ArrayList<RegistroNegativacaoCobrancaContaReceberVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, codigoPessoa, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static RegistroNegativacaoCobrancaContaReceberVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroNegativacaoCobrancaContaReceberVO obj = new RegistroNegativacaoCobrancaContaReceberVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
		obj.getAgente().setCodigo(dadosSQL.getInt("agente"));
		obj.setTipoAgente((TipoAgenteNegativacaoCobrancaContaReceberEnum) TipoAgenteNegativacaoCobrancaContaReceberEnum.valueOf(dadosSQL.getString("tipoAgente")));
		obj.setAluno(dadosSQL.getString("aluno"));
		obj.setmatricula(dadosSQL.getString("matricula"));
		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso"));
		obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma"));
		obj.getUsuarioVO().setCodigo(dadosSQL.getInt("usuario"));
		obj.getUsuarioVO().getPessoa().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getUsuarioVO().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		montarDadosCurso(obj, nivelMontarDados, usuario);
		montarDadosTurma(obj, nivelMontarDados, usuario);
		montarDadosUsuario(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		montarDadosAgente(obj, usuario);
		obj.setDataInicioFiltro(dadosSQL.getDate("dataInicioFiltro"));
		obj.setDataFimFiltro(dadosSQL.getDate("dataFimFiltro"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemInscricaoProcessoSeletivo(dadosSQL.getBoolean("tipoOrigemInscricaoProcessoSeletivo"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(dadosSQL.getBoolean("tipoOrigemMatricula"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemRequerimento(dadosSQL.getBoolean("tipoOrigemRequerimento"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemBiblioteca(dadosSQL.getBoolean("tipoOrigemBiblioteca"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(dadosSQL.getBoolean("tipoOrigemMensalidade"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemDevolucaoCheque(dadosSQL.getBoolean("tipoOrigemDevolucaoCheque"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemNegociacao(dadosSQL.getBoolean("tipoOrigemNegociacao"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemBolsaCusteadaConvenio(dadosSQL.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemContratoReceita(dadosSQL.getBoolean("tipoOrigemContratoReceita"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemInclusaoReposicao(dadosSQL.getBoolean("tipoOrigemInclusaoReposicao"));
		obj.getFiltroRelatorioFinanceiroVO().setTipoOrigemOutros(dadosSQL.getBoolean("tipoOrigemOutros"));
		obj.getFiltroRelatorioAcademicoVO().setAtivo(dadosSQL.getBoolean("ativo"));
		obj.getFiltroRelatorioAcademicoVO().setPreMatricula(dadosSQL.getBoolean("preMatricula"));
		obj.getFiltroRelatorioAcademicoVO().setPreMatriculaCancelada(dadosSQL.getBoolean("preMatriculaCancelada"));
		obj.getFiltroRelatorioAcademicoVO().setTrancado(dadosSQL.getBoolean("trancado"));
		obj.getFiltroRelatorioAcademicoVO().setAbandonado(dadosSQL.getBoolean("abandonado"));
		obj.getFiltroRelatorioAcademicoVO().setTransferenciaInterna(dadosSQL.getBoolean("transferenciaInterna"));
		obj.getFiltroRelatorioAcademicoVO().setTransferenciaExterna(dadosSQL.getBoolean("transferenciaExterna"));
		obj.getFiltroRelatorioAcademicoVO().setCancelado(dadosSQL.getBoolean("cancelado"));
		obj.getFiltroRelatorioAcademicoVO().setConcluido(dadosSQL.getBoolean("concluido"));
		obj.getFiltroRelatorioAcademicoVO().setFormado(dadosSQL.getBoolean("formado"));
		obj.setCentroReceitaApresentar(dadosSQL.getString("centroReceitaApresentar"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}
	
	public static RegistroNegativacaoCobrancaContaReceberVO montarDados(SqlRowSet dadosSQL, Integer codigoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RegistroNegativacaoCobrancaContaReceberVO obj = new RegistroNegativacaoCobrancaContaReceberVO();
		obj.getAgente().setCodigo(dadosSQL.getInt("agente"));
		obj.setTipoAgente((TipoAgenteNegativacaoCobrancaContaReceberEnum) TipoAgenteNegativacaoCobrancaContaReceberEnum.valueOf(dadosSQL.getString("tipoAgente")));
		montarDadosAgente(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		obj.setListaContasReceberCobranca(getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade().consultarContasReceberPorRegistroNegativacaoCobrancaItemPorPessoa(obj.getAgente().getCodigo(), codigoPessoa, usuario));
		
		return obj;
	}
	
	public static void montarDadosCurso(RegistroNegativacaoCobrancaContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCursoVO().getCodigo().intValue() == 0) {
			obj.setCursoVO(new CursoVO());
			return;
		}
		obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuario));
	}
	
	public static void montarDadosTurma(RegistroNegativacaoCobrancaContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurmaVO().getCodigo().intValue() == 0) {
			obj.setTurmaVO(new TurmaVO());
			return;
		}
		obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	public static void montarDadosUsuario(RegistroNegativacaoCobrancaContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUsuarioVO().getCodigo().equals(0)) {
			obj.setUsuarioVO(new UsuarioVO());
			return;
		}
		obj.setUsuarioVO(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	public static void montarDadosUnidadeEnsino(RegistroNegativacaoCobrancaContaReceberVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
			obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}
	
	public static void montarDadosAgente(RegistroNegativacaoCobrancaContaReceberVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getAgente().getCodigo().intValue() == 0) {
			obj.setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
			return;
		}
		obj.setAgente(getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(obj.getAgente().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}
	
	@Override
	public RegistroNegativacaoCobrancaContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" select registronegativacaocobrancacontareceber.*, ");
		sql.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
		sql.append(" from registronegativacaocobrancacontareceber ");
		sql.append(" inner join usuario on usuario.codigo =  registronegativacaocobrancacontareceber.usuario ");
		sql.append(" inner join pessoa on pessoa.codigo =  usuario.pessoa ");
		sql.append(" where registronegativacaocobrancacontareceber.codigo = ? ");		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}		
		return new RegistroNegativacaoCobrancaContaReceberVO();
	}
	
	@Override
	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarContasReceberPorRegistroNegativacaoCobranca(Integer codigoRegistro, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * ");
		sql.append(" from registronegativacaocobrancacontareceberitem ");
		sql.append(" where registronegativacaocobrancacontareceber = ").append(codigoRegistro).append(" ");
		sql.append(" order by datavencimento, aluno ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado);
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> consultarContasReceberPorRegistroNegativacaoCobrancaItemPorPessoa(Integer codigoAgente, Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT registronegativacaocobrancacontareceberitem.* from registronegativacaocobrancacontareceber ");
        sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");        
        sqlStr.append(" INNER JOIN contareceber ON contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");
		sqlStr.append(" WHERE contareceber.pessoa = ").append(codigoPessoa).append(" and dataexclusao is null  ");
		sqlStr.append(" and registronegativacaocobrancacontareceber.agente = ").append(codigoAgente).append(" ");
		sqlStr.append(" order by registronegativacaocobrancacontareceberitem.dataregistro desc, contareceber.datavencimento ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs);
	}

	public List<RegistroNegativacaoCobrancaContaReceberVO> consultarContasReceberPorRegistroNegativacaoCobrancaPorPessoa(Integer codigoPessoa, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct registronegativacaocobrancacontareceber.agente, registronegativacaocobrancacontareceber.tipoagente, agenteNegativacaoCobrancaContaReceber.nome from registronegativacaocobrancacontareceber ");
		sqlStr.append(" inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");        
		sqlStr.append(" INNER JOIN agenteNegativacaoCobrancaContaReceber ON agenteNegativacaoCobrancaContaReceber.codigo = registronegativacaocobrancacontareceber.agente ");
		sqlStr.append(" INNER JOIN contareceber ON contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");
		sqlStr.append(" WHERE contareceber.pessoa = ").append(codigoPessoa).append(" and registronegativacaocobrancacontareceberitem.dataexclusao is null ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, codigoPessoa, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
	}
	
	@Override
	public String consultarSituacaoContaReceber(IntegracaoNegativacaoCobrancaContaReceberEnum integracao, String matricula, Integer curso) throws Exception {
		String situacao = "";
		switch (integracao) {
		case CECAM:
			situacao = CECAM.consultarSituacaoBoleto(matricula, curso);
			break;

		default:
			break;
		}
		return situacao;
	}

	public List<ContaReceberVO> consultarNegociacao(Integer codigoContaReceber) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT contanegociada.codigo , contanegociada.situacao, contanegociada.nossoNumero, contanegociada.dataVencimento, contanegociada.nrDocumento, contanegociada.valor ");
		sql.append(" FROM contareceber AS contanegociada");
		sql.append(" WHERE contanegociada.tipoorigem = 'NCR'  ");
		sql.append(" AND contanegociada.codorigem = (select contarecebernegociado.negociacaocontareceber::varchar from contarecebernegociado where contarecebernegociado.contareceber = ").append(codigoContaReceber).append(" ) order by case contanegociada.situacao when 'RE' then 1 when 'AR' then 2 when 'NE' then 3 else 4 END ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ContaReceberVO> vetResultado = new ArrayList<ContaReceberVO>(0);
		while (rs.next()) {
			ContaReceberVO obj = new ContaReceberVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNossoNumero(rs.getString("nossoNumero"));
			obj.setDataVencimento(rs.getDate("dataVencimento"));
			obj.setNrDocumento(rs.getString("nrDocumento"));
			obj.setValor(rs.getDouble("valor"));
			obj.setSituacao(rs.getString("situacao"));
			vetResultado.add(obj);
		}
		return vetResultado;		
	}

	public Boolean consultarContaReceberNegativadaCobranca(Integer matriculaPeriodo) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select true as possuiNegativacao from registronegativacaocobrancacontareceberitem ");
		sql.append(" inner join contareceber on contareceber.codigo = registronegativacaocobrancacontareceberitem .contareceber ");
		sql.append(" where dataexclusao is null and contareceber.matriculaperiodo = ").append(matriculaPeriodo).append(" limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {			
			return rs.getBoolean("possuiNegativacao"); 
		}
		return false;		
	}

	public Boolean consultarContaReceberNegativadaCobrancaContaReceber(Integer contaReceber) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select true as possuiNegativacao from registronegativacaocobrancacontareceberitem ");
		sql.append(" inner join contareceber on contareceber.codigo = registronegativacaocobrancacontareceberitem .contareceber ");		
		sql.append(" where dataexclusao is null and contareceber.codigo = ").append(contaReceber).append(" limit 1 ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {			
			return rs.getBoolean("possuiNegativacao"); 
		}
		return false;		
	}
	
	@Override
	public void consultar(DataModelo dataModelo, String consultarPor, Date dataGeracaoInicio, Date dataGeracaoFim, Integer valorConsultaUnidadeEnsino, String valorConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		ControleAcesso.consultar(PerfilAcessoPermissaoFinanceiroEnum.REGISTRO_NEGATIVACAO_COBRANCA_CONTA_RECEBER.getValor(), controlarAcesso, usuarioLogado);
		//StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		StringBuilder sqlStr = new StringBuilder();
		StringBuilder sqlStrSelect = new StringBuilder();
		StringBuilder sqlStrCount = new StringBuilder();
		sqlStrSelect.append(" SELECT distinct registronegativacaocobrancacontareceber.*, turma.identificadorturma, curso.nome, ");
		sqlStrSelect.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\" ");
		sqlStrCount.append(" select count(registronegativacaocobrancacontareceber.codigo) as qtde ");
        sqlStr.append(" from registronegativacaocobrancacontareceber ");
		sqlStr.append(" inner JOIN usuario ON usuario.codigo = registronegativacaocobrancacontareceber.usuario ");
		sqlStr.append(" inner JOIN pessoa ON pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" left JOIN curso ON curso.codigo = registronegativacaocobrancacontareceber.curso ");
		sqlStr.append(" left JOIN turma ON turma.codigo = registronegativacaocobrancacontareceber.turma ");	
		sqlStr.append(" WHERE 1=1");
		
		if(Uteis.isAtributoPreenchido(valorConsulta) && consultarPor.equals("curso")) {
			sqlStr.append(" AND ( upper(sem_acentos(curso.nome)) like upper(sem_acentos(?)) ");
			sqlStr.append(" or (curso.codigo is null and exists (select registronegativacaocobrancacontareceberitem.codigo from registronegativacaocobrancacontareceberitem ");			
			sqlStr.append(" inner join matricula on registronegativacaocobrancacontareceberitem.matricula = matricula.matricula ");
			sqlStr.append(" inner join curso as c on c.codigo = matricula.curso ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
			sqlStr.append(" AND upper(sem_acentos(c.nome)) like upper(sem_acentos(?)) limit 1 ");
			sqlStr.append(" ))) ");
			valorConsulta = "%"+valorConsulta+"%";
		}else if(Uteis.isAtributoPreenchido(valorConsulta) && consultarPor.equals("turma")) {
			sqlStr.append(" AND (upper(sem_acentos(turma.identificadorturma)) like upper(sem_acentos(?))");
			sqlStr.append(" or (turma.codigo is null and exists (select registronegativacaocobrancacontareceberitem.codigo from registronegativacaocobrancacontareceberitem ");
			sqlStr.append(" inner join contareceber on contareceber.codigo = registronegativacaocobrancacontareceberitem.contareceber ");			
			sqlStr.append(" inner join turma as t on t.codigo = contareceber.turma ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
			sqlStr.append(" AND upper(sem_acentos(t.identificadorturma)) like upper(sem_acentos(?)) limit 1 ");
			sqlStr.append(" ))) ");
			valorConsulta = valorConsulta+"%";
		}else if(Uteis.isAtributoPreenchido(valorConsulta) && consultarPor.equals("aluno")) {
			sqlStr.append(" AND (");
			sqlStr.append(" exists (select registronegativacaocobrancacontareceberitem.codigo from registronegativacaocobrancacontareceberitem ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
			sqlStr.append(" AND upper(sem_acentos(registronegativacaocobrancacontareceberitem.aluno)) like upper(sem_acentos(?)) limit 1 ");
			sqlStr.append(" )) ");
			valorConsulta = "%"+valorConsulta+"%";
		}else if(Uteis.isAtributoPreenchido(valorConsulta) && consultarPor.equals("matricula")) {
			sqlStr.append(" AND (");
			sqlStr.append(" exists (select registronegativacaocobrancacontareceberitem.codigo from registronegativacaocobrancacontareceberitem ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
			sqlStr.append(" AND registronegativacaocobrancacontareceberitem.matricula = ? limit 1 ");
			sqlStr.append(" )) ");			
		}else if(Uteis.isAtributoPreenchido(valorConsulta) && consultarPor.equals("nossonumero")) {
			sqlStr.append(" AND (");
			sqlStr.append(" exists (select registronegativacaocobrancacontareceberitem.codigo from registronegativacaocobrancacontareceberitem ");
			sqlStr.append(" where registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber = registronegativacaocobrancacontareceber.codigo ");
			sqlStr.append(" AND registronegativacaocobrancacontareceberitem.nossonumero = ? limit 1 ");
			sqlStr.append(" )) ");			
		}
		sqlStr.append(" AND ").append(realizarGeracaoWherePeriodo(dataGeracaoInicio, dataGeracaoFim, "registronegativacaocobrancacontareceber.dataGeracao", false));		
		if(Uteis.isAtributoPreenchido(valorConsultaUnidadeEnsino)) {
			sqlStr.append(" AND registronegativacaocobrancacontareceber.unidadeensino = ").append(valorConsultaUnidadeEnsino);
		}
		sqlStrSelect.append(sqlStr);
		sqlStrCount.append(sqlStr);
		sqlStrSelect.append(" order by ");
		if(consultarPor.equals("curso")) {
			sqlStrSelect.append(" curso.nome, ");  
		}else if(consultarPor.equals("turma")) {
			sqlStrSelect.append(" turma.identificadorturma, ");  
		}
		sqlStrSelect.append(" registronegativacaocobrancacontareceber.dataGeracao ");		
		sqlStrSelect.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		
		SqlRowSet rs = null;
		SqlRowSet rsCount = null;
		if(Uteis.isAtributoPreenchido(valorConsulta)){
			if(consultarPor.equals("curso") || consultarPor.equals("turma")) {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStrSelect.toString(), valorConsulta, valorConsulta);
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlStrCount.toString(), valorConsulta, valorConsulta);
			}else {
				rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStrSelect.toString(), valorConsulta);
				rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlStrCount.toString(), valorConsulta);
			}
		}else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStrSelect.toString());
			rsCount = getConexao().getJdbcTemplate().queryForRowSet(sqlStrCount.toString());
		}
		dataModelo.setListaConsulta(montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		if(rsCount.next()) {
			dataModelo.setTotalRegistrosEncontrados(rsCount.getInt("qtde"));
		}else {
			dataModelo.setTotalRegistrosEncontrados(0);
		}
	}
	
	private static StringBuilder getSqlAdicionarFiltrosSerasaApiGeo(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO, UnidadeEnsinoVO unidadeEnsino) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" contareceber.datavencimento >= '").append(UteisData.obterDataAntiga(new Date(), agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencidaInicial())).append("'");
		sqlStr.append(" and contareceber.datavencimento <= '").append(UteisData.obterDataAntiga(new Date(),agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencida())).append("'");
		sqlStr.append(adicionarTipoOrigemContaReceber(agenteNegativacaoCobrancaContaReceberVO, "contareceber.tipoOrigem"));
		if (!agenteNegativacaoCobrancaContaReceberVO.getNegativarTodasParcelas()) {
			sqlStr.append(getSqlNegativarApenasUmaParcela(tipoAgente, agenteNegativacaoCobrancaContaReceberVO, unidadeEnsino));
		}
		if(!agenteNegativacaoCobrancaContaReceberVO.isNegativarNegociacoesVencidaSemParcelaRecebida()) {
			sqlStr.append("and ((contareceber.tipoorigem != 'NCR') or (exists ( ");
			sqlStr.append(" select cr.codigo from contareceber cr  ");
			sqlStr.append(" where cr.tipoorigem = 'NCR' and cr.codorigem = contareceber.codorigem and cr.situacao in('RE','NE')");
			sqlStr.append("))) ");
		}
		if (agenteNegativacaoCobrancaContaReceberVO.getValidarDocumentosEntregue()) {
			sqlStr.append(getSqlVerificacaoDocumentoEntregue(agenteNegativacaoCobrancaContaReceberVO.getTipoDocumentoPendenciaAgenteCobrancaVOs()));
		}
		if (agenteNegativacaoCobrancaContaReceberVO.getDesconsiderarAlunoDisciplinaReprovadas()) {
			sqlStr.append(" and ma.matricula not in  (");
			sqlStr.append(" 			select t.matricula from ( 	");
			sqlStr.append(" 					select matricula.matricula, historico.codigo, matriculaperiodo.data from (");
			sqlStr.append(" 								select matricula.matricula, matricula.gradecurricularatual from contareceber cr");
			sqlStr.append(" 								inner join matricula on cr.matriculaaluno = matricula.matricula ");
			sqlStr.append(" 								where matricula.matricula = ma.matricula ");
			sqlStr.append(" 								and  cr.situacao = 'AR' ");
			if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
				sqlStr.append(" and cr.unidadeensinofinanceira = ").append(unidadeEnsino.getCodigo());
			}
			sqlStr.append(" 								and cr.datavencimento >= '").append(UteisData.obterDataAntiga(new Date(), agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencidaInicial())).append("'");
			sqlStr.append("                                 and cr.datavencimento <= '").append(UteisData.obterDataAntiga(new Date(),agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencida())).append("'");
			sqlStr.append(" 								and cr.tipoorigem in ('BIB', 'BCC', 'CTR', 'DCH', 'IRE','MAT','MEN','NCR','OUT' ) ");
			sqlStr.append(" 							) as matricula ");
			sqlStr.append(" 							inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append(" 							inner join historico on matriculaperiodo.codigo = historico.matriculaperiodo ");
			sqlStr.append(" 							left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
			sqlStr.append(" 							where historico.situacao in ('RE', 'RF') ");
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
			sqlStr.append(" 								and not exists ( ");
			sqlStr.append(" 									select h.codigo from historico as h ");
			sqlStr.append(" 									where h.matricula = matricula.matricula ");
			sqlStr.append(" 										and h.gradedisciplina = historico.gradedisciplina ");
			sqlStr.append(" 										and h.situacao in ('CS', 'AP', 'AA', 'CH','IS','CC','AE','AB','CE') ");
			sqlStr.append(" 									union all ");
			sqlStr.append(" 									select h.codigo from historico as h ");
			sqlStr.append(" 									where h.matricula = matricula.matricula ");
			sqlStr.append(" 										and h.gradecurriculargrupooptativadisciplina = historico.gradecurriculargrupooptativadisciplina							");
			sqlStr.append(" 										and h.situacao in ('CS', 'AP', 'AA', 'CH','IS','CC','AE','AB','CE') ");
			sqlStr.append(" 									limit 1 ");
			sqlStr.append(" 								)");
			sqlStr.append(" 							group by matricula.matricula, historico.codigo, matriculaperiodo.data ");
			sqlStr.append(" 							");
			sqlStr.append(" 					) as t ");
			sqlStr.append(" 			inner join periodoauladisciplinaaluno(t.codigo) as aula on aula.datatermino >= t.data ");			
			sqlStr.append(" 			group by t.matricula ");
			sqlStr.append(" 			having count(t.codigo) > ").append(agenteNegativacaoCobrancaContaReceberVO.getQuantidadeMinimaDisciplinaReprovada());
			sqlStr.append(" 	)");
		}
		return sqlStr;
	}
	
	private static StringBuilder adicionarTipoOrigemContaReceber(AgenteNegativacaoCobrancaContaReceberVO obj, String  keyEntidade) {
		StringBuilder sqlStr = new StringBuilder() ;
		boolean virgula = false;
		boolean existe = false;
		sqlStr.append("and ").append(keyEntidade).append(" in (");
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(virgula ? "," : "").append("'BIB'");
			existe = true;
			virgula = true;
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(virgula ? "," : "").append("'BCC'");
			existe = true;
			virgula = true;
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(virgula ? "," : "").append("'CTR'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(virgula ? "," : "").append("'DCH'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(virgula ? "," : "").append("'IRE'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(virgula ? "," : "").append("'MAT'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(virgula ? "," : "").append("'MEN'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(virgula ? "," : "").append("'NCR'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(virgula ? "," : "").append("'OUT'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(virgula ? "," : "").append("'IPS'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(virgula ? "," : "").append("'REQ'");
			virgula = true;
			existe = true;
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(virgula ? "," : "").append("'MDI'");
			virgula = true;
			existe = true;
		}
		sqlStr.append(" ) ");
		if(!existe) {
			sqlStr = new StringBuilder("and 1 = 1 ");
		}
		return sqlStr;
	}
	
	private static StringBuilder getSqlNegativarApenasUmaParcela(TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, AgenteNegativacaoCobrancaContaReceberVO  agenteNegativacaoCobrancaContaReceberVO, UnidadeEnsinoVO unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" and contareceber.codigo = (select cr.codigo from contareceber as cr where cr.matriculaaluno = ma.matricula");
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
			sqlStr.append(" and cr.unidadeensinofinanceira = ").append(unidadeEnsino.getCodigo());
		}
		sqlStr.append(" and cr.situacao = 'AR' ");
		sqlStr.append(" and cr.datavencimento >= '").append(UteisData.obterDataAntiga(new Date(),agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencidaInicial())).append("' ");
		sqlStr.append(" and cr.datavencimento <= '").append(UteisData.obterDataAntiga(new Date(),agenteNegativacaoCobrancaContaReceberVO.getQuantidadeDiasConsiderarParcelaVencida())).append("' ");
		sqlStr.append(adicionarTipoOrigemContaReceber(agenteNegativacaoCobrancaContaReceberVO, "cr.tipoOrigem"));
		if(!agenteNegativacaoCobrancaContaReceberVO.isNegativarNegociacoesVencidaSemParcelaRecebida()) {
			sqlStr.append("and ((cr.tipoorigem != 'NCR') or (exists ( ");
			sqlStr.append(" select conta_receber.codigo from contareceber conta_receber  ");
			sqlStr.append(" where conta_receber.tipoorigem = 'NCR' and conta_receber.codorigem = cr.codorigem and conta_receber.situacao in('RE','NE')");
			sqlStr.append("))) ");
		}
		sqlStr.append(getSqlRegistroNegativacaoCobrancaContaReceberItem(false , tipoAgente, agenteNegativacaoCobrancaContaReceberVO.getCodigo(), "cr.nossonumero" ));		
		sqlStr.append("  order by cr.datavencimento limit 1) ");
		return sqlStr;
	}
	
	private static StringBuilder getSqlVerificacaoDocumentoEntregue(List<TipoDocumentoPendenciaAgenteCobrancaVO> TipoDocumentoPendenciaAgenteCobrancaVOs) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" and not exists ( select documetacaomatricula.codigo as documentosPendentes from documetacaomatricula ");
		sqlStr.append(" where entregue = false ");
		if(Uteis.isAtributoPreenchido(TipoDocumentoPendenciaAgenteCobrancaVOs)) {
			sqlStr.append(TipoDocumentoPendenciaAgenteCobrancaVOs.stream().map(TipoDocumentoPendenciaAgenteCobrancaVO::getCodigo).map(String::valueOf).collect(Collectors.joining(", ", "and documetacaomatricula.tipodedocumento in (", ") ")));
		} 
		sqlStr.append(" and documetacaomatricula.matricula = ma.matricula limit 1)");
		return sqlStr;
	}
	
	private static StringBuilder getSqlRegistroNegativacaoCobrancaContaReceberItem(Boolean negativarTodasParcelas , TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, int codigoAgenteNegativacao, String campoNossoNumero) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("and not exists (");
		sqlStr.append("	select registroNegativacaoCobrancaContaReceberItem.codigo");
		sqlStr.append("	from registronegativacaocobrancacontareceber");
		sqlStr.append("	inner join registroNegativacaoCobrancaContaReceberItem on registroNegativacaoCobrancaContaReceberItem.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber.codigo");
		sqlStr.append("	inner join agentenegativacaocobrancacontareceber on agentenegativacaocobrancacontareceber.codigo = registroNegativacaoCobrancaContaReceber.agente");
		sqlStr.append("	where	registroNegativacaoCobrancaContaReceber.agente = ").append(codigoAgenteNegativacao);
		sqlStr.append("	and agentenegativacaocobrancacontareceber.tipo = '").append(tipoAgente).append("' ");
		sqlStr.append("	and registroNegativacaoCobrancaContaReceberItem.dataexclusao is null");
		if (negativarTodasParcelas) {
			sqlStr.append(" and registroNegativacaoCobrancaContaReceberItem.nossonumero = ").append(campoNossoNumero);
		}else {
			sqlStr.append("	and registroNegativacaoCobrancaContaReceberItem.matricula = ma.matricula");
		}
		sqlStr.append(")");
		return sqlStr; 
	}                  		
	
}