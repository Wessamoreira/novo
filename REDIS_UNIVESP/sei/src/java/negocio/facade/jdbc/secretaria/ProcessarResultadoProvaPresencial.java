/**
 * 
 */
package negocio.facade.jdbc.secretaria;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaProvaPresencialVO;
import negocio.comuns.processosel.enumeradores.TipoProcessamentoProvaPresencial;
import negocio.comuns.secretaria.MatriculaProvaPresencialDisciplinaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialNaoLocalizadaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialRespostaVO;
import negocio.comuns.secretaria.MatriculaProvaPresencialVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialDisciplinaEnum;
import negocio.comuns.secretaria.enumeradores.SituacaoMatriculaProvaPresencialEnum;
import negocio.comuns.secretaria.enumeradores.TipoAlteracaoSituacaoHistoricoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.ProcessarResultadoProvaPresencialInterfaceFacade;

/**
 * @author Carlos Eugênio
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class ProcessarResultadoProvaPresencial extends ControleAcesso implements ProcessarResultadoProvaPresencialInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ProcessarResultadoProvaPresencial() {
		super();
		setIdEntidade("ProcessarResultadoProvaPresencial");
	}
	
	public void validarDadosArquivoNotaLancada(Integer configuracaoAcademico, String periodicidadeCurso, String ano, String semestre) throws Exception {
		if (configuracaoAcademico.equals(0)) {
			throw new Exception("O campo CONFIGURAÇÃO ACADÊMICO deve ser informado.");
		}
		if (periodicidadeCurso.equals("SE")) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			if (semestre.equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado.");
			}
		}
		if (periodicidadeCurso.equals("AN")) {
			if (ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
		}
	}
	
	public void inicializarDadosResultadoProcessamentoArquivoRespostaProvaPresencial(ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultado, TipoProcessamentoProvaPresencial tipoProcessamentoProvaPresencial,  String ano, String semestre, Integer configuracaoAcademico, String periodicidadeCurso, Boolean realizarCalculoMediaLancamentoNota, String nomeArquivo, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, UsuarioVO usuarioVO) {
		resultado.setAno(ano);
		resultado.setSemestre(semestre);
		resultado.setTipoProcessamentoProvaPresencialEnum(tipoProcessamentoProvaPresencial);
		resultado.setPeriodicidadeCurso(periodicidadeCurso);
		resultado.setRealizarCalculoMediaLancamentoNota(realizarCalculoMediaLancamentoNota);
		resultado.setNomeArquivo(nomeArquivo);
		resultado.setDataProcessamento(new Date());
		resultado.setUsuarioVO(usuarioVO);
		resultado.setTipoAlteracaoSituacaoHistorico(tipoAlteracaoSituacaoHistorico);
	}

	/**
	 * Método responsável por fazer o processamento do arquivo de resposta
	 * sendo ele com nota lançada ou por gabarito.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public ResultadoProcessamentoArquivoRespostaProvaPresencialVO realizarProcessamentoArquivoResposta(FileUploadEvent uploadEvent, TipoProcessamentoProvaPresencial tipoProcessamentoProvaPresencial,  GabaritoVO gabaritoVO, String ano, String semestre, Integer configuracaoAcademico, String periodicidadeCurso, Boolean realizarCalculoMediaLancamentoNota, TipoAlteracaoSituacaoHistoricoEnum tipoAlteracaoSituacaoHistorico, UsuarioVO usuarioVO) throws Exception {
		try {
			ResultadoProcessamentoArquivoRespostaProvaPresencialVO resultado = new ResultadoProcessamentoArquivoRespostaProvaPresencialVO();

			if (tipoProcessamentoProvaPresencial.name().equals(TipoProcessamentoProvaPresencial.LEITURA_ARQUIVO_NOTA_LANCADA.name())) {
				validarDadosArquivoNotaLancada(configuracaoAcademico, periodicidadeCurso, ano, semestre);
				inicializarDadosResultadoProcessamentoArquivoRespostaProvaPresencial(resultado, tipoProcessamentoProvaPresencial, ano, semestre, configuracaoAcademico, periodicidadeCurso, realizarCalculoMediaLancamentoNota, uploadEvent.getUploadedFile().getName().toString(), tipoAlteracaoSituacaoHistorico, usuarioVO);
				
				Map<MatriculaProvaPresencialVO, List<MatriculaProvaPresencialDisciplinaVO>> mapMatriculas = realizarLeituraArquivoPorNota(uploadEvent, ano, semestre, configuracaoAcademico, usuarioVO);
				
				for (MatriculaProvaPresencialVO matriculaProvaPresencialVO : mapMatriculas.keySet()) {
					matriculaProvaPresencialVO.setMatriculaProvaPresencialDisciplinaVOs(mapMatriculas.get(matriculaProvaPresencialVO));
					matriculaProvaPresencialVO.setQuantidadeDisciplina(matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs().size());
					inicializarQuantidadeDisciplinaMatriculaProvaPresencial(matriculaProvaPresencialVO);
					matriculaProvaPresencialVO.setSelecionado(Boolean.TRUE);
					resultado.getMatriculaProvaPresencialVOs().add(matriculaProvaPresencialVO);
				}
				
			} else {

				Map<String, String> mapMatriculas = null;
				List<MatriculaVO> listaMatriculaVOs = getFacadeFactory().getMatriculaFacade().consultarPorMatriculaConfiguracaoGabaritoProvaPresencial(gabaritoVO, SituacaoVinculoMatricula.ATIVA.getValor(), usuarioVO);
				mapMatriculas = realizarSeparacaoRespostaPorMatricula(uploadEvent, gabaritoVO, usuarioVO);
				for (MatriculaVO matriculaVO : listaMatriculaVOs) {
					String matricula = matriculaVO.getMatricula().replace("_", "");
					String matriculaComZeroEsquerda = Uteis.preencherComZerosPosicoesVagas(matriculaVO.getMatricula().replace("_", ""), gabaritoVO.getTamanhoNrMatriculaArquivo());					
					if (mapMatriculas.containsKey(matricula)) {
						MatriculaProvaPresencialVO matriculaProvaPresencialVO = inicializarDadosMatriculaProvaPresencial(matriculaVO, SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA);
						executarCorrecaoProvaPorGabarito(gabaritoVO, matriculaProvaPresencialVO, mapMatriculas.get(matricula), usuarioVO);
						resultado.getMatriculaProvaPresencialVOs().add(matriculaProvaPresencialVO);
						String respostas = mapMatriculas.get(matriculaComZeroEsquerda);
						mapMatriculas.remove(matriculaComZeroEsquerda);
						mapMatriculas.put(matriculaVO.getMatricula(), respostas);
					}else if (mapMatriculas.containsKey(matriculaComZeroEsquerda)) {
						MatriculaProvaPresencialVO matriculaProvaPresencialVO = inicializarDadosMatriculaProvaPresencial(matriculaVO, SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA);
						executarCorrecaoProvaPorGabarito(gabaritoVO, matriculaProvaPresencialVO, mapMatriculas.get(matriculaComZeroEsquerda), usuarioVO);
						resultado.getMatriculaProvaPresencialVOs().add(matriculaProvaPresencialVO);
						String respostas = mapMatriculas.get(matriculaComZeroEsquerda);
						mapMatriculas.remove(matriculaComZeroEsquerda);
						mapMatriculas.put(matriculaVO.getMatricula(), respostas);
					}else if (mapMatriculas.containsKey(matriculaVO.getMatricula())) {
						MatriculaProvaPresencialVO matriculaProvaPresencialVO = inicializarDadosMatriculaProvaPresencial(matriculaVO, SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA);
						executarCorrecaoProvaPorGabarito(gabaritoVO, matriculaProvaPresencialVO, mapMatriculas.get(matriculaVO.getMatricula()), usuarioVO);
						resultado.getMatriculaProvaPresencialVOs().add(matriculaProvaPresencialVO);
					} else {
						MatriculaProvaPresencialVO matriculaProvaPresencialVO = inicializarDadosMatriculaProvaPresencial(matriculaVO, SituacaoMatriculaProvaPresencialEnum.MATRICULA_NAO_ENCONTRADA_ARQUIVO);
						matriculaProvaPresencialVO.setSelecionado(false);
						resultado.getMatriculaProvaPresencialVOs().add(matriculaProvaPresencialVO);
					}
				}

				for (String matricula : mapMatriculas.keySet()) {
					Boolean existeMatricula = getFacadeFactory().getMatriculaFacade().consultarExistenciaMatriculaPorMatricula(matricula, usuarioVO);
					if (!existeMatricula) {
						resultado.getMatriculaProvaPresencialNaoLocalizadaVOs().add(inicializarDadosMatriculaProvaPresencialNaoLocalizada(matricula, mapMatriculas.get(matricula), usuarioVO));
					}
				}
			}
			return resultado;
		} catch (Exception e) {
			throw e;
		} finally {
			uploadEvent = null;
		}
	}
	
	/**
	 * Método responsável por inicializar os dados do obj MatriculaProvaPresencial
	 * @author Carlos Eugênio - 24/06/2015
	 * @param matriculaProvaPresencialVO
	 */
	public void inicializarQuantidadeDisciplinaMatriculaProvaPresencial(MatriculaProvaPresencialVO matriculaProvaPresencialVO) {
		matriculaProvaPresencialVO.setQuantidadeDisciplina(matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs().size());
		for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
			if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA.name())) {
				matriculaProvaPresencialVO.setQuantidadeDisciplinaLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaLocalizada() + 1);
			} else if(matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA.name())) {
				matriculaProvaPresencialVO.setQuantidadeDisciplinaNaoLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaNaoLocalizada() + 1);
			} else if(matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_OUTRA_CONFIGURACAO_ACADEMICA.name())) {
				matriculaProvaPresencialVO.setQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaConfiguracaoAcademicoNaoLocalizada() + 1);
			}
		}
	}

	/**
	 * Método responsável por inicializar os dados do obj MatriculaProvaPresencialNãoLocalizada no SEI
	 * essas matrículas são salvas em uma tabela a parte
	 * @author Carlos Eugênio - 24/06/2015
	 * @param matricula
	 * @param resposta
	 * @param usuarioVO
	 * @return
	 */
	public MatriculaProvaPresencialNaoLocalizadaVO inicializarDadosMatriculaProvaPresencialNaoLocalizada(String matricula, String resposta, UsuarioVO usuarioVO) {
		MatriculaProvaPresencialNaoLocalizadaVO obj = new MatriculaProvaPresencialNaoLocalizadaVO();
		obj.setMatriculaNaoLocalizada(matricula);
		obj.setRespostaGabarito(resposta);
		return obj;
	}

	public MatriculaProvaPresencialVO inicializarDadosMatriculaProvaPresencial(MatriculaVO matriculaVO, SituacaoMatriculaProvaPresencialEnum situacaoMatriculaProvaPresencialEnum) {
		MatriculaProvaPresencialVO obj = new MatriculaProvaPresencialVO();
		obj.setMatriculaVO(matriculaVO);
		obj.setSituacaoMatriculaProvaPresencialEnum(situacaoMatriculaProvaPresencialEnum);
		return obj;
	}

	/**
	 * Método responsável por fazer a comparação das respostas que estão no arquivo texto
	 * e das respostas do gabarito. Diferenciando as regras por TipoRespostaGabarito(Disciplina e Área de Conhecimento)
	 * @author Carlos Eugênio - 24/06/2015
	 * @param gabaritoVO
	 * @param matriculaProvaPresencialVO
	 * @param resposta
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void executarCorrecaoProvaPorGabarito(GabaritoVO gabaritoVO, MatriculaProvaPresencialVO matriculaProvaPresencialVO, String resposta, UsuarioVO usuarioVO) throws Exception {
		String opcaoMarcado = null;
		matriculaProvaPresencialVO.setTotalAcerto(BigDecimal.ZERO);
		Map<Integer, MatriculaProvaPresencialDisciplinaVO> mapProvaPresencialDisciplinaVOs = new HashMap<Integer, MatriculaProvaPresencialDisciplinaVO>(0);
		Map<Integer, List<DisciplinaVO>> mapProvaPresencialAreaConhecimentoVOs = new HashMap<Integer, List<DisciplinaVO>>(0);

		int x = 1;
		for (GabaritoRespostaVO gabaritoRespostaVO : gabaritoVO.getGabaritoRespostaVOs()) {
			if (resposta.length() >= gabaritoVO.getQuantidadeQuestao()) {
				opcaoMarcado = resposta.substring(gabaritoRespostaVO.getNrQuestao() - 1, gabaritoRespostaVO.getNrQuestao());

				// Dados Prova Resposta
				MatriculaProvaPresencialRespostaVO matriculaProvaPresencialRespostaVO = new MatriculaProvaPresencialRespostaVO();
				matriculaProvaPresencialRespostaVO.setNrQuestao(x);
				matriculaProvaPresencialRespostaVO.setRespostaAluno(opcaoMarcado);
				matriculaProvaPresencialRespostaVO.setRespostaGabarito(gabaritoRespostaVO.getRespostaCorreta());
				matriculaProvaPresencialRespostaVO.setMatriculaProvaPresencialVO(matriculaProvaPresencialVO);

				// Regra Prova Presencial Por Disciplina
				if (gabaritoVO.getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {
					matriculaProvaPresencialRespostaVO.setDisciplinaVO(gabaritoRespostaVO.getDisciplinaVO());

					MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO = new MatriculaProvaPresencialDisciplinaVO();
					matriculaProvaPresencialDisciplinaVO.setDisciplinaVO(gabaritoRespostaVO.getDisciplinaVO());

					// Verifica se a resposta está correta
					if ( (opcaoMarcado.equalsIgnoreCase(gabaritoRespostaVO.getRespostaCorreta()) && !opcaoMarcado.equalsIgnoreCase("W") && !opcaoMarcado.equalsIgnoreCase("Z")) 
							|| gabaritoRespostaVO.getAnulado()) {
						matriculaProvaPresencialVO.setTotalAcerto(matriculaProvaPresencialVO.getTotalAcerto().add(gabaritoRespostaVO.getValorNota()));
						matriculaProvaPresencialDisciplinaVO.setNota(gabaritoRespostaVO.getValorNota());
						matriculaProvaPresencialRespostaVO.setTotalAcerto(gabaritoRespostaVO.getValorNota());
					} else {
						matriculaProvaPresencialDisciplinaVO.setNota(BigDecimal.ZERO);
						matriculaProvaPresencialVO.setTotalErro(matriculaProvaPresencialVO.getTotalErro().add(gabaritoRespostaVO.getValorNota()));
					}

					if (!mapProvaPresencialDisciplinaVOs.containsKey(gabaritoRespostaVO.getDisciplinaVO().getCodigo())) {
						HistoricoVO historico = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), gabaritoRespostaVO.getDisciplinaVO().getCodigo(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), 0, false, usuarioVO);
						if (historico != null) {
							matriculaProvaPresencialDisciplinaVO.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA);
							matriculaProvaPresencialVO.setQuantidadeDisciplinaLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaLocalizada() + 1);
						} else {
							matriculaProvaPresencialDisciplinaVO.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA);
							matriculaProvaPresencialVO.setQuantidadeDisciplinaNaoLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaNaoLocalizada() + 1);
						}
						mapProvaPresencialDisciplinaVOs.put(gabaritoRespostaVO.getDisciplinaVO().getCodigo(), matriculaProvaPresencialDisciplinaVO);
						matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs().add(matriculaProvaPresencialDisciplinaVO);
					} else {
						MatriculaProvaPresencialDisciplinaVO obj = mapProvaPresencialDisciplinaVOs.get(gabaritoRespostaVO.getDisciplinaVO().getCodigo());
						if(obj.getNota() != null) {
							obj.setNota(obj.getNota().add(matriculaProvaPresencialDisciplinaVO.getNota()));
						}else {
							obj.setNota(matriculaProvaPresencialDisciplinaVO.getNota());
						}
						for (MatriculaProvaPresencialDisciplinaVO matriculaProvaDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
							if (matriculaProvaDisciplinaVO.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo())) {
								matriculaProvaDisciplinaVO.setNota(obj.getNota());
							}
						}
					}
				} else {
					// Regra Prova Presencial Por Área de Conhecimento

					// Verifica se a resposta está correta
					if (opcaoMarcado.equalsIgnoreCase(gabaritoRespostaVO.getRespostaCorreta()) && !opcaoMarcado.equalsIgnoreCase("W") && !opcaoMarcado.equalsIgnoreCase("Z")) {
						matriculaProvaPresencialVO.setTotalAcerto(matriculaProvaPresencialVO.getTotalAcerto().add(gabaritoRespostaVO.getValorNota()));
						matriculaProvaPresencialRespostaVO.setTotalAcerto(gabaritoRespostaVO.getValorNota());

					} else {
						matriculaProvaPresencialVO.setTotalErro(matriculaProvaPresencialVO.getTotalErro().add(gabaritoRespostaVO.getValorNota()));
					}
					matriculaProvaPresencialRespostaVO.setAreaConhecimentoVO(gabaritoRespostaVO.getAreaConhecimentoVO());
					if (!mapProvaPresencialAreaConhecimentoVOs.containsKey(gabaritoRespostaVO.getAreaConhecimentoVO().getCodigo())) {

						List<DisciplinaVO> listaDisciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasPorGradeCurricularPeriodoLetivoAreaConhecimento(gabaritoVO.getGradeCurricularVO().getCodigo(), gabaritoVO.getPeriodoLetivoVO().getCodigo(), gabaritoRespostaVO.getAreaConhecimentoVO().getCodigo(), matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), usuarioVO);
						for (DisciplinaVO disciplinaVO : listaDisciplinaVOs) {
							MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO = new MatriculaProvaPresencialDisciplinaVO();
							matriculaProvaPresencialDisciplinaVO.setDisciplinaVO(disciplinaVO);
							matriculaProvaPresencialDisciplinaVO.setAreaConhecimentoVO(gabaritoRespostaVO.getAreaConhecimentoVO());
							HistoricoVO historico = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), disciplinaVO.getCodigo(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), gabaritoVO.getAno(), gabaritoVO.getSemestre(), 0, false, usuarioVO);
							if (historico != null) {
								matriculaProvaPresencialDisciplinaVO.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA);
								matriculaProvaPresencialVO.setQuantidadeDisciplinaLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaLocalizada() + 1);
							} else {
								matriculaProvaPresencialDisciplinaVO.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA);
								matriculaProvaPresencialVO.setQuantidadeDisciplinaNaoLocalizada(matriculaProvaPresencialVO.getQuantidadeDisciplinaNaoLocalizada() + 1);
							}
							matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs().add(matriculaProvaPresencialDisciplinaVO);
						}

						mapProvaPresencialAreaConhecimentoVOs.put(gabaritoRespostaVO.getAreaConhecimentoVO().getCodigo(), listaDisciplinaVOs);
					}
				}
				matriculaProvaPresencialVO.getMatriculaProvaPresencialRespostaVOs().add(matriculaProvaPresencialRespostaVO);
				x++;
			}
		}

	}

	/**
	 * Método responsável por realizar a leitura do arquivo texto fazendo a leitura por gabarito.
	 * @author Carlos Eugênio - 24/06/2015
	 * @param uploadEvent
	 * @param gabaritoVO
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> realizarSeparacaoRespostaPorMatricula(FileUploadEvent uploadEvent, GabaritoVO gabaritoVO, UsuarioVO usuarioVO) throws Exception {
		Integer tamanhoNrMatriculaArquivo = gabaritoVO.getTamanhoNrMatriculaArquivo();
		Map<String, String> MapMatriculas = new HashMap<String, String>();
		BufferedReader reader = null;
		FileReader fr = null;
		try {
			byte[] byteArray;
			byteArray = uploadEvent.getUploadedFile().getData();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO);
			File pastaDestino = new File((configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().endsWith(File.separator) ? configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() : configuracaoGeralSistemaVO.getLocalUploadArquivoFixo()+File.separator) + "processamentoResultadoProvaPresencial");
			if (!pastaDestino.exists()) {
				if(!pastaDestino.mkdir()) {					
					throw new Exception("Erro ao gerar a estrutura de diretório de armazenamento do arquivo processado.");
				}
			}
			File file = new File(pastaDestino + File.separator + uploadEvent.getUploadedFile().getName());
			FileOutputStream in = new FileOutputStream(file) ;  
			in.write(byteArray);
			in.close();
			fr = new FileReader(file);
			reader = new BufferedReader(fr);
			String str;			
			while (reader.ready()) {
				str = reader.readLine();
				if (str != null && !str.trim().isEmpty() && str.trim().length() >= tamanhoNrMatriculaArquivo) {
					MapMatriculas.put((str.substring(0, tamanhoNrMatriculaArquivo)).trim(), str.substring(tamanhoNrMatriculaArquivo, str.length()));
				}
			}
		} catch (Exception e) {
			throw new Exception("Falha na leitura do arquivo");
		} finally {
			if (fr != null) {
				fr.close();
			}

			if (reader != null) {
				reader.close();
			}
			uploadEvent = null;
			reader = null;
			fr = null;
		}
		return MapMatriculas;
	}

	/**
	 * Método responsável de realizar a leitura do arquivo texto por nota.
	 * @author Carlos Eugênio - 24/06/2015
	 * @param uploadEvent
	 * @param ano
	 * @param semestre
	 * @param configuracaoAcademico
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	private Map<MatriculaProvaPresencialVO, List<MatriculaProvaPresencialDisciplinaVO>> realizarLeituraArquivoPorNota(FileUploadEvent uploadEvent, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO) throws Exception {
		Map<MatriculaProvaPresencialVO, List<MatriculaProvaPresencialDisciplinaVO>> mapMatriculas = new HashMap<MatriculaProvaPresencialVO, List<MatriculaProvaPresencialDisciplinaVO>>(0);
		BufferedReader reader = null;
		FileReader fr = null;
		try {
			byte[] byteArray;
			byteArray = uploadEvent.getUploadedFile().getData();
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuarioVO);
			File pastaDestino;
			if (configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().substring(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo().length() - 1).equals("\\")) {
				pastaDestino = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + "processamentoResultadoProvaPresencial");
			} else {
				pastaDestino = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + "\\processamentoResultadoProvaPresencial");
			}
			if (!pastaDestino.exists()) {
				pastaDestino.mkdir();
			}
			File file = new File(pastaDestino + File.separator + uploadEvent.getUploadedFile().getName());
			FileOutputStream in = new FileOutputStream(file) ;  
			in.write(byteArray);
			in.close();
			fr = new FileReader(file);
			reader = new BufferedReader(fr);
			String str;
			while (reader.ready()) {
				str = reader.readLine();
				if (str != null && !str.trim().isEmpty() && str.trim().length() >= 5) {
					
					String[] textos = str.split(";");
					
					if (textos.length != 3) {
						if (textos.length == 1) {
							throw new Exception("O arquivo está com problema nos seguintes dados: Matrícula: " + textos[0]);
						} else if (textos.length == 2) {
							throw new Exception("O arquivo está com problema nos seguintes dados: Matrícula: " + textos[0] + " - Disciplina: " + textos[1]);
						} else {
							throw new Exception("Falha na leitura do arquivo.");
						}
					}
					String matricula = textos[0];
					String disciplina = textos[1];
					String nota = textos[2];
					
					if (matricula != null && !matricula.equals("") && !str.contains("inscricao")) {
						
						List<MatriculaProvaPresencialDisciplinaVO> listaMatriculaProvaPresencialDisciplinaVOs = null;
						MatriculaProvaPresencialVO matriculaProvaPresencialVO = realizarCriacaoMatriculaProvaPresencialDadosArquivos(matricula, usuarioVO);
						if (!mapMatriculas.containsKey(matriculaProvaPresencialVO)) {
							listaMatriculaProvaPresencialDisciplinaVOs = new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0);
						} else {
							listaMatriculaProvaPresencialDisciplinaVOs = mapMatriculas.get(matriculaProvaPresencialVO);
							
						}
						listaMatriculaProvaPresencialDisciplinaVOs.add(realizarCriacaoMatriculaProvaDisciplinaDadosArquivo(matriculaProvaPresencialVO, disciplina, nota, ano, semestre, configuracaoAcademico, usuarioVO));
						mapMatriculas.put(matriculaProvaPresencialVO, listaMatriculaProvaPresencialDisciplinaVOs);
					}
					
				}
			}
		} catch (Exception e) {
			throw new Exception("Falha na leitura do arquivo");
		} finally {
			if (fr != null) {
				fr.close();
			}

			if (reader != null) {
				reader.close();
			}
			uploadEvent = null;
			reader = null;
			fr = null;
		}
		return mapMatriculas;
	}
	
	public MatriculaProvaPresencialVO realizarCriacaoMatriculaProvaPresencialDadosArquivos(String matricula, UsuarioVO usuarioVO) throws Exception {
		MatriculaProvaPresencialVO obj = new MatriculaProvaPresencialVO(); 
		obj.setSelecionado(Boolean.TRUE);
		MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnicaRemovendoCaracterEspecialMatricula(matricula.replace("_", ""), "_", null, false, "", usuarioVO);
		if (!matriculaVO.getMatricula().equals("")) {
			obj.setMatriculaVO(matriculaVO);
			obj.setSituacaoMatriculaProvaPresencialEnum(SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA);
		} else {
			obj.getMatriculaVO().setMatricula(matricula);
			obj.setSituacaoMatriculaProvaPresencialEnum(SituacaoMatriculaProvaPresencialEnum.MATRICULA_NAO_ENCONTRADA_SEI);
		}
		return obj;
	}
	
	/**
	 * Método responsável por consultar o histórico de acordo com os dados do arquivo texto,
	 * caso não encontre as disciplinas é setado uma situação de acordo com a mesma.
	 * @author Carlos Eugênio - 24/06/2015
	 * @param matriculaProvaPresencialVO
	 * @param disciplina
	 * @param nota
	 * @param ano
	 * @param semestre
	 * @param configuracaoAcademico
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	public MatriculaProvaPresencialDisciplinaVO realizarCriacaoMatriculaProvaDisciplinaDadosArquivo(MatriculaProvaPresencialVO matriculaProvaPresencialVO, String disciplina, String nota, String ano, String semestre, Integer configuracaoAcademico, UsuarioVO usuarioVO) throws Exception {
		MatriculaProvaPresencialDisciplinaVO obj = new MatriculaProvaPresencialDisciplinaVO();

		if (matriculaProvaPresencialVO.getSituacaoMatriculaProvaPresencialEnum().name().equals(SituacaoMatriculaProvaPresencialEnum.MATRICULA_ENCONTRADA.name())) {
			if (disciplina != null && !disciplina.equals("")) {
				Integer codigoDisciplina = Integer.parseInt(disciplina);
				HistoricoVO historicoVO = null;
				historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), codigoDisciplina, SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), ano, semestre, configuracaoAcademico, false, usuarioVO);
				
				if (historicoVO != null && !historicoVO.getCodigo().equals(0)) {
					obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_LOCALIZADA);
					obj.setDisciplinaVO(historicoVO.getDisciplina());
					obj.setHistoricoVO(historicoVO);
				} else {
					historicoVO = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacaoHistoricoAnoSemestre(matriculaProvaPresencialVO.getMatriculaVO().getMatricula(), codigoDisciplina, SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), ano, semestre, 0, false, usuarioVO);
					if (historicoVO == null) {
						obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA);
						obj.getDisciplinaVO().setCodigo(codigoDisciplina);
					} else {
						obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_OUTRA_CONFIGURACAO_ACADEMICA);
						obj.setDisciplinaVO(historicoVO.getDisciplina());
					}
					
				}
			}
			if (nota != null && !nota.equals("")) {
				obj.setNota(BigDecimal.valueOf(Double.parseDouble(nota.replace(",", "."))));
			}	
		} else {
			obj.setSituacaoMatriculaProvaPresencialDisciplinaEnum(SituacaoMatriculaProvaPresencialDisciplinaEnum.DISCIPLINA_NAO_LOCALIZADA);
			Integer codigoDisciplina = 0;
			if (disciplina != null && !disciplina.equals("")) {
				codigoDisciplina = Integer.parseInt(disciplina);
				obj.getDisciplinaVO().setCodigo(codigoDisciplina);
				if (nota != null && !nota.equals("")) {
					obj.setNota(BigDecimal.valueOf(Double.parseDouble(nota.replace(",", "."))));
				}
			}
				
		}
		return obj;
		
	}

	public static String getIdEntidade() {
		return ProcessarResultadoProvaPresencial.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ProcessarResultadoProvaPresencial.idEntidade = idEntidade;

	}

	public List<MatriculaProvaPresencialDisciplinaVO> realizarObtencaoListaPorSituacaoLocalizacao(MatriculaProvaPresencialVO matriculaProvaPresencialVO, SituacaoMatriculaProvaPresencialDisciplinaEnum situacaoMatriculaProvaPresencialDisciplinaEnum, UsuarioVO usuarioVO) {
		List<MatriculaProvaPresencialDisciplinaVO> listaProvaDisciplinaVOs = new ArrayList<MatriculaProvaPresencialDisciplinaVO>(0);
		if (situacaoMatriculaProvaPresencialDisciplinaEnum.name().equals(SituacaoMatriculaProvaPresencialDisciplinaEnum.TODAS_DISCIPLINAS.name())) {
			listaProvaDisciplinaVOs = matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs(); 
			return listaProvaDisciplinaVOs;
		}
		for (MatriculaProvaPresencialDisciplinaVO matriculaProvaPresencialDisciplinaVO : matriculaProvaPresencialVO.getMatriculaProvaPresencialDisciplinaVOs()) {
			if (matriculaProvaPresencialDisciplinaVO.getSituacaoMatriculaProvaPresencialDisciplinaEnum().name().equals(situacaoMatriculaProvaPresencialDisciplinaEnum.name())) {
				listaProvaDisciplinaVOs.add(matriculaProvaPresencialDisciplinaVO);
			}
		}
		return listaProvaDisciplinaVOs;
	}

	/**
	 * Consulta  se existe resultado processado pelo {@link GabaritoVO} informado.
	 * 
	 * @param gabaritoVO
	 * @return
	 * @throws Exception
	 */
	public Boolean existeResultadoProcessamentoRespostaPorGabarito(GabaritoVO gabaritoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT CASE WHEN COUNT(inscricao.gabarito) > 0 THEN true ELSE false end AS existeResultado FROM resultadoprocessoseletivo");
		sql.append(" INNER JOIN inscricao ON inscricao.codigo = resultadoprocessoseletivo.inscricao");
		sql.append(" WHERE gabarito = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), gabaritoVO.getCodigo());
        return tabelaResultado.next() ? tabelaResultado.getBoolean("existeResultado") : Boolean.FALSE;
	}

}
