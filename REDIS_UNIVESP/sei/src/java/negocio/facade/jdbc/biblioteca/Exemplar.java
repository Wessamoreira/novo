package negocio.facade.jdbc.biblioteca;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.AssinaturaPeriodicoVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarPainelGestorBibliotecaVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.HistoricoExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.SecaoVO;
import negocio.comuns.biblioteca.enumeradores.TipoMidiaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.TipoExemplar;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.biblioteca.ExemplarInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ExemplarVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ExemplarVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ExemplarVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Exemplar extends ControleAcesso implements ExemplarInterfaceFacade {

    protected static String idEntidade;

    public Exemplar() throws Exception {
        super();
        setIdEntidade("Catalogo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ExemplarVO</code>.
     */
    public ExemplarVO novo() throws Exception {
        Exemplar.incluir(getIdEntidade());
        ExemplarVO obj = new ExemplarVO();
        return obj;
    }

    public void validarDadosGeracaoExemplares(Integer numeroExemplaresASeremGerados, ExemplarVO exemplarVO, Boolean periodicos) throws Exception {
        if (exemplarVO.getBiblioteca().getCodigo() == 0) {
            throw new Exception("Informe a Biblioteca para a geração dos exemplares.");
        }
//        if (exemplarVO.getSecao().getCodigo() == 0 && !periodicos) {
//            throw new Exception("Informe a Seção para a geração dos exemplares.");
//        }
        if ((numeroExemplaresASeremGerados == null || numeroExemplaresASeremGerados <= 0) && !periodicos) {
            throw new Exception("Informe o número de exemplares a serem gerados.");
        }
        
        if (exemplarVO.getDataPublicacao() == null && periodicos) {
            throw new Exception("Informe a Data de Publicação do exemplar.");
        }
        if (exemplarVO.getNumeroEdicao().equals(0) && periodicos) {
            throw new Exception("Informe o Nº da Edição do exemplar.");
        }
        if (Uteis.isAtributoPreenchido(exemplarVO.getAnoPublicacao()) && exemplarVO.getAnoPublicacao().length() < 4) {
            throw new Exception("O campo ANO DE PUBLICAÇÃO deve ter 4 caracteres");
        }
    }

    /**
     * Operação que altera o estado do exemplar para o estado escolhido na tela de devolução.
     *
     * @param exemplarVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarEstadoExemplares(ExemplarVO exemplarVO, UsuarioVO usuarioLogado) throws Exception {
        try {
            String sql = "UPDATE Exemplar set estadoexemplar=?, bibliotecaatual=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{exemplarVO.getEstadoExemplar(), (exemplarVO.getBibliotecaAtual() != null && exemplarVO.getBibliotecaAtual() > 0) ?exemplarVO.getBibliotecaAtual():exemplarVO.getBiblioteca().getCodigo(), exemplarVO.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método que consulta o numero de exemplares existentes de uma determinado catalogo e retorna um inteiro com o
     * valor;
     *
     * @param exemplarVO
     * @return
     * @throws Exception
     */
    public int consultarNrExemplaresCatalogoGravadosDisponiveis(ExemplarVO exemplarVO, String situacaoAtual) throws Exception {
        String sqlStr = "SELECT COUNT (*) FROM exemplar WHERE catalogo = " + exemplarVO.getCatalogo().getCodigo() + " ";
        if (!situacaoAtual.equals("")) {
            sqlStr += "AND situacaoAtual = '" + situacaoAtual + "' ";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt(1));
    }
    
    public int numeroExemplaresDisponiveisParaReserva(Integer codigoCatalogo) throws Exception {
        String sqlStr = "SELECT COUNT (*) FROM exemplar WHERE catalogo = " + codigoCatalogo + " and desconsiderarReserva = 'f' ";                     
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (tabelaResultado.getInt(1));
    }

    /**
     * Operação que, ao incluir um exemplar, muda a sua situação para <b>DISPONIVEL</b>, de acordo com o <b>Enum</b>
     * <code>SituacaoExemplar</code>.
     *
     * @param exemplarVO
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void setarSituacaoAtualExemplar(ExemplarVO exemplarVO) {
        exemplarVO.setSituacaoAtual(SituacaoExemplar.DISPONIVEL.getValor());
    }

    /**
     * Operação que, ao incluir um emprestimo, sendo ele uma Devolução, pega a lista de itens desse empréstimo e muda a
     * situação de cada exemplar para <b>DISPONIVEL</b>.
     *
     * @param itemEmprestimoVOs
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoExemplaresParaDisponivel(ItemEmprestimoVO itemEmprestimoVO, UsuarioVO usuarioLogado) throws Exception {
        executarAlteracaoSituacaoExemplares(itemEmprestimoVO.getExemplar(), SituacaoExemplar.DISPONIVEL.getValor(), usuarioLogado);
    }

    /**
     * Operação que, ao incluir um empréstimo pega a lista de itens desse empréstimo e muda a situação de cada exemplar
     * para <b>EMPRESTADO</b>, de acordo com o <b>Enum</b> <code>SituacaoExemplar</code>
     *
     * @param itemEmprestimoVOs
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoExemplaresParaEmprestado(List<ItemEmprestimoVO> itemEmprestimoVOs, UsuarioVO usuarioLogado) throws Exception {
        for (ItemEmprestimoVO itemEmprestimoVO : itemEmprestimoVOs) {
            executarAlteracaoSituacaoExemplares(itemEmprestimoVO.getExemplar(), SituacaoExemplar.EMPRESTADO.getValor(), usuarioLogado);
        }
    }

    /**
     * Executa a operação de alteração de situação do exemplar.
     *
     * @param exemplarVO
     * @param situacao
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarAlteracaoSituacaoExemplares(ExemplarVO exemplarVO, final String situacao, UsuarioVO usuarioLogado) throws Exception {
        try {
            String sql = "UPDATE Exemplar set situacaoAtual=?, bibliotecaatual=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{situacao, (exemplarVO.getBibliotecaAtual() != null && exemplarVO.getBibliotecaAtual() > 0) ?exemplarVO.getBibliotecaAtual():exemplarVO.getBiblioteca().getCodigo(), exemplarVO.getCodigo()});
            
//          getFacadeFactory().getRegistroEntradaAcervoFacade().excluirExemplarItemRegistroEntradaAcervo(exemplarVO.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método que pega o número de exemplares a ser gerado informado na tela, e monta os objetos desses exemplares,
     * colocando em uma lista. Ao gerar os exemplares, deve ser levado em conta os seguintes aspectos: - A biblioteca
     * selecionada na combobox será espelhada no exemplar, assim como a seção. - O código de barras será populado
     * automaticamente com base em uma sequence do banco de dados. - O local será preenchido também automaticamente com
     * a String de classificação Bibliográfica e um ponto. - Ambos os campos gerados automaticamente (local e código de
     * barras) poderão ser alterados pelo usuário.
     *
     * @author Murillo Parreira
     * @param catalogoVO
     * @param exemplarVO
     * @param exemplarVOs
     * @param numeroExemplaresAGerar
     * @throws Exception
     */
	public void gerarExemplares(CatalogoVO catalogoVO, ExemplarVO exemplarVO, List<ExemplarVO> exemplarVOs, int numeroExemplaresAGerar, ConfiguracaoGeralSistemaVO conf) throws Exception {
		if (exemplarVO.getBiblioteca().getCodigo() != 0) {
			int numeroExemplar = 0;
			if (catalogoVO.getExemplarVOs().isEmpty()) {
				numeroExemplar = 1;
			} else {
				if (conf.getIncrementarNumeroExemplarPorBiblioteca()) {
					int maior = 0;
					List<ExemplarVO> exemplarVOsBiblioteca = obterExemplaresBiblioteca(catalogoVO, exemplarVO);
					for (ExemplarVO exemplar : exemplarVOsBiblioteca) {
						maior = Math.max(maior, exemplar.getNumeroExemplar());
					}
					numeroExemplar = maior + 1;
				} else {
					int maior = 0;
					for (ExemplarVO exemplar : catalogoVO.getExemplarVOs()) {
						maior = Math.max(maior, exemplar.getNumeroExemplar());
					}
					numeroExemplar = maior + 1;										
				}
			}
			while (exemplarVOs.size() < numeroExemplaresAGerar) {
				ExemplarVO exemplarGerado = new ExemplarVO();
				exemplarGerado.setBiblioteca(exemplarVO.getBiblioteca());
				exemplarGerado.setSecao(exemplarVO.getSecao());
				exemplarGerado.setCatalogo(catalogoVO);
				// exemplarGerado.setLocal(catalogoVO.getClassificacaoBibliografica()
				// + "." + exemplarVO.getLocal());
				exemplarGerado.setLocal(exemplarVO.getLocal());
				exemplarGerado.setTipoEntrada(exemplarVO.getTipoEntrada());
				exemplarGerado.setTipoExemplar(exemplarVO.getTipoExemplar());
				exemplarGerado.setCodigoBarra("");
				exemplarGerado.setNumeroEdicao(null);
				exemplarGerado.setVolume(exemplarVO.getVolume());
				exemplarGerado.setNumeroExemplar(numeroExemplar);
				exemplarGerado.setDataPublicacao(exemplarVO.getDataPublicacao());
				exemplarGerado.setTituloExemplar(exemplarVO.getTituloExemplar());
				exemplarGerado.setLocal(exemplarVO.getLocal());
				exemplarGerado.setEdicao(exemplarVO.getEdicao());
				exemplarGerado.setSubtitulo(exemplarVO.getSubtitulo());
				exemplarGerado.setMes(exemplarVO.getMes());
				exemplarGerado.setAnovolume(exemplarVO.getAnovolume());
				exemplarGerado.setNrEdicaoEspecial(exemplarVO.getNrEdicaoEspecial());
				exemplarGerado.setEmprestarSomenteFinalDeSemana(exemplarVO.getEmprestarSomenteFinalDeSemana());
				exemplarGerado.setParaConsulta(exemplarVO.getParaConsulta());
				exemplarGerado.setNrNotaFiscal(exemplarVO.getNrNotaFiscal());
				exemplarGerado.setFornecedorVO(exemplarVO.getFornecedorVO());
				exemplarGerado.setValorCompra(exemplarVO.getValorCompra());
				exemplarGerado.setDataCompra(exemplarVO.getDataCompra());
				if (exemplarGerado.getTipoEntrada().equals("CO")) {
					exemplarGerado.setDataAquisicao(exemplarVO.getDataCompra());
				}else {
					exemplarGerado.setDataAquisicao(exemplarVO.getDataAquisicao());
				}
				exemplarGerado.setAbreviacaoTitulo(exemplarVO.getAbreviacaoTitulo());
				exemplarGerado.setTipoMidia(exemplarVO.getTipoMidia());
				if (exemplarVO.getCatalogo() != null && !exemplarVO.getCatalogo().getLink().equals("")) {
					exemplarGerado.setLink(exemplarVO.getCatalogo().getLink());
				} else {
					exemplarGerado.setLink(exemplarVO.getLink());
				}
				
				exemplarGerado.setFasciculos(exemplarVO.getFasciculos());
				exemplarGerado.setAnoPublicacao(exemplarVO.getAnoPublicacao());
				exemplarGerado.setNrPaginas(exemplarVO.getNrPaginas());
				exemplarGerado.setIsbn(exemplarVO.getIsbn());
				exemplarGerado.setIssn(exemplarVO.getIssn());
				exemplarGerado.setDesconsiderarReserva(exemplarVO.getDesconsiderarReserva());
				exemplarGerado.setExemplarUnidadeLogado(true);				
				exemplarVOs.add(exemplarGerado);
				numeroExemplar++;
			}
		} else {
			throw new Exception("Selecione a Biblioteca e o Catálogo antes de gerar os Exemplares.");
		}
	}

	public List<ExemplarVO> obterExemplaresBiblioteca(CatalogoVO catalogoVO, ExemplarVO exemplarVO) {
		List<ExemplarVO> exemplares = new ArrayList<ExemplarVO>();
		Iterator i = catalogoVO.getExemplarVOs().iterator();
		while (i.hasNext()) {
			ExemplarVO exe = (ExemplarVO)i.next();
			if (exe.getCodigoHash().equalsIgnoreCase(exemplarVO.getCodigoHash())) {
				exemplares.add(exe);
			}
		}
		return exemplares;
	}
	
    public void gerarExemplaresPeriodicos(ExemplarVO exemplarVO, List<ExemplarVO> exemplarVOs, int numeroExemplaresAGerar) throws Exception {
        if (exemplarVO.getBiblioteca().getCodigo() != 0) {
        	int numeroExemplar = 0;
			if (exemplarVOs.isEmpty()) {
				numeroExemplar = 1;
			} else {
				int maior = 0;
				for (ExemplarVO exemplar : exemplarVOs) {
					maior = Math.max(maior, exemplar.getNumeroExemplar());
				}
				numeroExemplar = maior + 1;
			}
            while (exemplarVOs.size() < numeroExemplaresAGerar) {
                ExemplarVO exemplarGerado = new ExemplarVO();
                exemplarGerado.setBiblioteca(exemplarVO.getBiblioteca());
                exemplarGerado.setSecao(exemplarVO.getSecao());
                exemplarGerado.setLocal(exemplarVO.getLocal());
                exemplarGerado.setTipoEntrada(exemplarVO.getTipoEntrada());
                exemplarGerado.setTipoExemplar(TipoExemplar.PERIODICO.getValor());
                exemplarGerado.setCodigoBarra("");
                exemplarGerado.setNumeroEdicao(exemplarVO.getNumeroEdicao());
                exemplarGerado.setVolume(exemplarVO.getVolume());
                exemplarGerado.setNumeroExemplar(numeroExemplar);
                exemplarVOs.add(exemplarGerado);
                numeroExemplar++;
            }
        } else {
            throw new Exception("Selecione a Biblioteca antes de gerar os Exemplares.");
        }
    }

    /**
     * Método gera o código de barras do exemplar automaticamente de acordo com o último código de exemplar inserido na
     * base. Ao pegar esse código, ele concatena zeros a esquerda até completar 7 casas.
     *
     * @param exemplarVO
     * @throws Exception
     * @author Murillo Parreira
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void gerarCodigoBarrasExemplar(ExemplarVO exemplarVO) throws Exception {
        Long codigo = null;
        boolean controle = true;
        String codigoBarras = "";
		SqlRowSet tabelaResultado;
        try {
    		if (exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra() < 6) {
    			throw new ConsistirException("A Quantidade de Números de Codigo de Barra a Ser Gerado na Configuração Biblioteca (Aba Geral) não pode ser menor que 6.");
    		}
			tabelaResultado = this.consultarMaiorCodigoBarraValido(exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
            if (tabelaResultado.next()) { 
            	codigo = tabelaResultado.getLong("resultado");								
                codigoBarras = Uteis.preencherComZerosPosicoesVagas(String.valueOf(codigo), exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
            } else {
            	codigo = 0L;
                codigoBarras = "1";
                codigoBarras = Uteis.preencherComZerosPosicoesVagas(codigoBarras, exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
            }
            while (controle) {
				if (verificarExisteCodigoBarraExemplar(codigoBarras)) {
                    codigo++;
                    if (String.valueOf(codigo).length() > exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra()) {
                    	throw new Exception("Devido já existir exemplar do codigo de barra ("+codigoBarras+"),não foi gerado o codigo de barra automático,pois é maior que a quantidade de números ("+exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra()+") digitos definido na configuração da Bilbioteca");
					}else {
					 codigoBarras = Uteis.preencherComZerosPosicoesVagas(String.valueOf(codigo), exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
				  } 
				} else {
                    controle = false;
                }
            }
            exemplarVO.setCodigoBarra(codigoBarras);
		} catch (Exception e) {
			throw e;
        } finally {
            codigoBarras = null;
			tabelaResultado = null;
			codigo = null;
        }
    }

    /**
     * Método que valida se já existe um exemplar com o código de barras informado pelo usuário na tela.Foi Realizado a conversão para int
    * para evitar o cadastro de código de barras com números finais iguais.
    *
    * @param exemplarVO
    * @throws Exception
    * @author Murillo Parreira
    */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void validarCodigoBarrasDigitadoPeloUsuario(ExemplarVO exemplarVO) throws Exception {
		String codigoBarras = "";
		StringBuilder sqlStr = new StringBuilder();
		if (exemplarVO.getCodigoBarra().length()  > exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra()) {
			throw new Exception("O codigo do exemplar "+exemplarVO.getCodigoBarra()+" e maior que o permitido na configuração da biblioteca.");
		}if (exemplarVO.getCodigoBarra().equals("0")) {
			throw new Exception("O codigo do exemplar informado não pode ser zerado");
		}
		else {
		sqlStr.append("SELECT exemplar.codigo FROM exemplar WHERE exemplar.codigoBarra::NUMERIC(30) = ").append(Long.parseLong(exemplarVO.getCodigoBarra())).append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		codigoBarras = Uteis.preencherComZerosPosicoesVagas(String.valueOf(exemplarVO.getCodigoBarra()), exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
		exemplarVO.setCodigoBarra(codigoBarras);
		try {
			if (tabelaResultado.next()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Exemplar_codigoBarraExistenteCadastro") + ' ' + exemplarVO.getCodigoBarra());
			}
		}
		finally {
			sqlStr = null;
			tabelaResultado = null;
		}
	}
	}

    /**
     * Método que valida se já existe um exemplar com o código de barras informado pelo usuário na tela no momento
     * da alteração de um catálogo com exemplares.
     *
     * @param exemplarVO
     * @throws Exception
     * @author Murillo Parreira
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void validarCodigoBarrasDigitadoPeloUsuarioNaAlteracaoCatalogoComExemplares(ExemplarVO exemplarVO) throws Exception {
    	String codigoBarras = "";
    	StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT exemplar.codigo FROM exemplar WHERE exemplar.codigoBarra::NUMERIC(30) = ").append(Long.parseLong(exemplarVO.getCodigoBarra())).append(" and exemplar.codigo != ").append(exemplarVO.getCodigo()).append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		try {
			codigoBarras = Uteis.preencherComZerosPosicoesVagas(String.valueOf(exemplarVO.getCodigoBarra()), exemplarVO.getBiblioteca().getConfiguracaoBiblioteca().getTamanhoCodigoBarra());
			exemplarVO.setCodigoBarra(codigoBarras);
			if (tabelaResultado.next()) {
				if (tabelaResultado.getInt("codigo") != exemplarVO.getCodigo().intValue()) {
					throw new Exception(UteisJSF.internacionalizar("msg_Exemplar_codigoBarraExistenteCadastro") + ' ' + exemplarVO.getCodigoBarra());
				}
			}
		} finally {
			sqlStr = null;
			tabelaResultado = null;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirListaExemplares(List<ExemplarVO> exemplarVOs, UsuarioVO usuario) throws Exception {
    	validarDadosUnicidadeBibliotecaEdicaoVolumeNumero(exemplarVOs);
        for (ExemplarVO exemplarVO : exemplarVOs) {
            if (exemplarVO.getTipoExemplar().equals(TipoExemplar.PERIODICO.getValor())) {
                incluir(exemplarVO, true, true, usuario);
            } else {
                incluir(exemplarVO, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(ExemplarVO exemplarVO, UsuarioVO usuario) throws Exception {
        incluir(exemplarVO, false, true, usuario);
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ExemplarVO</code>. Primeiramente
     * valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ExemplarVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public synchronized void incluir(final ExemplarVO obj, boolean periodico, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            ExemplarVO.validarDados(obj, periodico);
            obj.realizarUpperCaseDados();
           obj.getBiblioteca().setConfiguracaoBiblioteca(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getConfiguracaoBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)); 
            if (obj.getCodigoBarra().equals("")) {
                gerarCodigoBarrasExemplar(obj);
            } else {
                validarCodigoBarrasDigitadoPeloUsuario(obj);
            }

            // Quando se inclui um novo exemplar no Banco, ele automaticamente
            // deve estar Disponível para empréstimo.
            if (obj.getParaConsulta()) {
				obj.setSituacaoAtual(SituacaoExemplar.CONSULTA.getValor());
			} else {
				setarSituacaoAtualExemplar(obj);
			}

            final StringBuilder sql = new StringBuilder(" INSERT INTO Exemplar( biblioteca, catalogo, codigoBarra, tipoExemplar, tipoEntrada, situacaoAtual, estadoExemplar, ");
            sql.append(" local, secao, assinaturaPeriodico, numeroEdicao, tituloExemplar, dataPublicacao, volume, numeroExemplar, edicao, subtitulo, mes, anovolume, nrEdicaoEspecial, paraconsulta, ");
            sql.append(" emprestarsomentefinaldesemana, nrNotaFiscal, fornecedor, valorCompra, dataCompra, bibliotecaatual, abreviacaoTitulo, tipoMidia, link, fasciculos, anoPublicacao, nrpaginas, isbn, issn , desconsiderarReserva , dataAquisicao) ");
            sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ,? ) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    if (obj.getBiblioteca().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getBiblioteca().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getCatalogo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getCatalogo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setString(3, obj.getCodigoBarra());
                    sqlInserir.setString(4, obj.getTipoExemplar());
                    sqlInserir.setString(5, obj.getTipoEntrada());
                    sqlInserir.setString(6, obj.getSituacaoAtual());
                    sqlInserir.setString(7, obj.getEstadoExemplar());
                    sqlInserir.setString(8, obj.getLocal());
                    if (obj.getSecao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(9, obj.getSecao().getCodigo());
                    } else {
                        sqlInserir.setNull(9, 0);
                    }
                    if (obj.getAssinaturaPeriodico().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(10, obj.getAssinaturaPeriodico().getCodigo());
                    } else {
                        sqlInserir.setNull(10, 0);
                    }
                    if (obj.getNumeroEdicao().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getNumeroEdicao());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setString(12, obj.getTituloExemplar());
                    if (obj.getDataPublicacao() != null) {
                        sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataPublicacao()));
                    } else {
                        sqlInserir.setNull(13, 0);
                    }
                    sqlInserir.setString(14, obj.getVolume());
                    sqlInserir.setInt(15, obj.getNumeroExemplar());
                    sqlInserir.setString(16, obj.getEdicao());
                    sqlInserir.setString(17, obj.getSubtitulo());
                    
                    sqlInserir.setString(18, obj.getMes());
                    sqlInserir.setString(19, obj.getAnovolume());
                    sqlInserir.setString(20, obj.getNrEdicaoEspecial());
                    
                    sqlInserir.setBoolean(21, obj.getParaConsulta());
                    sqlInserir.setBoolean(22, obj.getEmprestarSomenteFinalDeSemana());
                    
                    sqlInserir.setInt(23, obj.getNrNotaFiscal());
                    if (obj.getFornecedorVO().getCodigo() != 0) {
                        sqlInserir.setInt(24, obj.getFornecedorVO().getCodigo());
                    } else {
                        sqlInserir.setNull(24, 0);
                    }
                    sqlInserir.setDouble(25, obj.getValorCompra());
                    sqlInserir.setDate(26, Uteis.getDataJDBC(obj.getDataCompra()));
                    if (obj.getBibliotecaAtual() != 0) {
                        sqlInserir.setInt(27, obj.getBibliotecaAtual());
                    } else {
                        sqlInserir.setNull(27, 0);
                    }
                    sqlInserir.setString(28, obj.getAbreviacaoTitulo());
                    sqlInserir.setString(29, obj.getTipoMidia());
                    sqlInserir.setString(30, obj.getLink());
                    sqlInserir.setString(31, obj.getFasciculos());
                    sqlInserir.setString(32, obj.getAnoPublicacao());
                    sqlInserir.setInt(33, obj.getNrPaginas());
                    sqlInserir.setString(34, obj.getIsbn());
                    sqlInserir.setString(35, obj.getIssn());
                    sqlInserir.setBoolean(36, obj.getDesconsiderarReserva());
                    sqlInserir.setDate(37, Uteis.getDataJDBC(obj.getDataAquisicao()));
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

            // Ao gravar um Exemplar, um Registro de Entrada no Acervo deve ser
            // registrado.
            getFacadeFactory().getRegistroEntradaAcervoFacade().registrarEntradaAcervoExemplar(obj, usuario);

            // Ao gravar um Exemplar, automaticamente deve-se atualizar o número
            // de Exemplares da Catalogo
            // getFacadeFactory().getCatalogoFacade().atualizarNrExemplaresCatalogo(obj);

            obj.setNovoObj(Boolean.FALSE);

        } catch (Exception e) {
            obj.setNovoObj(true);
            if(e.getMessage().contains("check_exemplar_codigobarra")){
            	throw new Exception(UteisJSF.internacionalizar("msg_Exemplar_codigoBarraExistenteCadastro") + ' ' + obj.getCodigoBarra());
            }
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ExemplarVO</code>. Sempre utiliza a
     * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
     * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
     * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ExemplarVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ExemplarVO obj, boolean periodico, UsuarioVO usuarioVO) throws Exception {
        try {
            ExemplarVO.validarDados(obj, periodico);
            obj.realizarUpperCaseDados();

            if (obj.getCodigoBarra().equals("")) {
                gerarCodigoBarrasExemplar(obj);
            } else {
                validarCodigoBarrasDigitadoPeloUsuarioNaAlteracaoCatalogoComExemplares(obj);
            }

            final StringBuilder sql = new StringBuilder(" UPDATE Exemplar set biblioteca=?, catalogo=?, codigoBarra=?, tipoExemplar=?, tipoEntrada=?, ");
            sql.append(" estadoExemplar=?, local=?, secao=?, assinaturaPeriodico=?, numeroEdicao=?, tituloExemplar=?, dataPublicacao=?, volume=?, numeroExemplar=?, edicao=?, ");
            sql.append(" subtitulo=?, mes=?, anovolume=?, nrEdicaoEspecial=?, paraconsulta=?, emprestarsomentefinaldesemana=?, ");
            sql.append(" nrNotaFiscal=?, fornecedor=?, valorCompra=?, dataCompra=?, bibliotecaatual=?, abreviacaoTitulo=?, tipoMidia=?, link=?, fasciculos=?, anoPublicacao=?, nrpaginas=?, isbn=?, issn=? , desconsiderarReserva = ? , dataaquisicao = ?, situacaoatual = ? ");
            sql.append(" WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    if (obj.getBiblioteca().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getBiblioteca().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getCatalogo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getCatalogo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setString(3, obj.getCodigoBarra());
                    sqlAlterar.setString(4, obj.getTipoExemplar());
                    sqlAlterar.setString(5, obj.getTipoEntrada());
                    sqlAlterar.setString(6, obj.getEstadoExemplar());
                    sqlAlterar.setString(7, obj.getLocal());
                    if (obj.getSecao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(8, obj.getSecao().getCodigo());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                    if (obj.getAssinaturaPeriodico().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getAssinaturaPeriodico().getCodigo());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (obj.getNumeroEdicao().intValue() != 0) {
                        sqlAlterar.setInt(10, obj.getNumeroEdicao());
                    } else {
                        sqlAlterar.setNull(10, 0);
                    }
                    sqlAlterar.setString(11, obj.getTituloExemplar());
                    if (obj.getDataPublicacao() != null) {
                        sqlAlterar.setDate(12, Uteis.getDataJDBC(obj.getDataPublicacao()));
                    } else {
                        sqlAlterar.setNull(12, 0);
                    }
                    
                    sqlAlterar.setString(13, obj.getVolume());
                    sqlAlterar.setInt(14, obj.getNumeroExemplar());
                    sqlAlterar.setString(15, obj.getEdicao());
                    sqlAlterar.setString(16, obj.getSubtitulo());
                 
                    sqlAlterar.setString(17, obj.getMes());
                    sqlAlterar.setString(18, obj.getAnovolume());
                    sqlAlterar.setString(19, obj.getNrEdicaoEspecial());
                    
                    sqlAlterar.setBoolean(20, obj.getParaConsulta());
                    sqlAlterar.setBoolean(21, obj.getEmprestarSomenteFinalDeSemana());
                    
                    sqlAlterar.setInt(22, obj.getNrNotaFiscal());
                    if (obj.getFornecedorVO().getCodigo() != 0) {
                        sqlAlterar.setInt(23, obj.getFornecedorVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(23, 0);
                    }
                    sqlAlterar.setDouble(24, obj.getValorCompra());
                    sqlAlterar.setDate(25, Uteis.getDataJDBC(obj.getDataCompra()));
                    if (obj.getBibliotecaAtual() != 0) {
                        sqlAlterar.setInt(26, obj.getBibliotecaAtual());
                    } else {
                        sqlAlterar.setNull(26, 0);
                    }
                    sqlAlterar.setString(27, obj.getAbreviacaoTitulo());
                    sqlAlterar.setString(28, obj.getTipoMidia());
                    sqlAlterar.setString(29, obj.getLink());
                    sqlAlterar.setString(30, obj.getFasciculos());
                    sqlAlterar.setString(31, obj.getAnoPublicacao());
                    sqlAlterar.setInt(32, obj.getNrPaginas());
                    sqlAlterar.setString(33, obj.getIsbn());
                    sqlAlterar.setString(34, obj.getIssn());
                    sqlAlterar.setBoolean(35, obj.getDesconsiderarReserva());
                    sqlAlterar.setDate(36, Uteis.getDataJDBC(obj.getDataAquisicao()));
                    sqlAlterar.setString(37, obj.getSituacaoAtual());
                    sqlAlterar.setInt(38, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            }) == 0) {
            	incluir(obj, usuarioVO);
            	return;
            }

        } catch (DuplicateKeyException dup) {
        	if(dup.getMessage().contains("check_exemplar_codigobarra")){
        		throw new Exception(UteisJSF.internacionalizar("msg_Exemplar_codigoBarraExistenteCadastro") + ' ' + obj.getCodigoBarra());
        	}
        	if (dup.getMessage().contains("uniq_exemplar_catalogo_numeroexemplar")) {
        		throw new Exception("Já existe um exemplar cadastrado com este NÚMERO ( " + obj.getNumeroExemplar() +" ) e NÚMERO EDIÇÃO ( " + obj.getEdicao() +" )");
        	} else {
        		throw new Exception("Já existe um exemplar cadastrado com este código de barras: " + obj.getCodigoBarra());
        	}
        } catch (Exception e) {
        	if(e.getMessage().contains("check_exemplar_codigobarra")){
        		throw new Exception(UteisJSF.internacionalizar("msg_Exemplar_codigoBarraExistenteCadastro") + ' ' + obj.getCodigoBarra());
        	}
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ExemplarVO</code>. Sempre localiza o registro a
     * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ExemplarVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ExemplarVO obj, UsuarioVO usuarioLogado) throws Exception {
        try {
            String sql = "DELETE FROM Exemplar WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirExemplarCatalogos(CatalogoVO catalogo, UsuarioVO usuarioLogado) throws Exception {
        getFacadeFactory().getRegistroEntradaAcervoFacade().excluirRegistrarEntradaAcervoExemplar(catalogo);
        String sql = "DELETE FROM Exemplar WHERE (catalogo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
        getConexao().getJdbcTemplate().update(sql, new Object[]{catalogo.getCodigo()});
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirExemplarItemEmprestimo(Integer exemplar, UsuarioVO usuarioLogado) throws Exception {
        try {
            String sql = "delete from itememprestimo where situacao != 'EM' and (exemplar = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{exemplar});
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer executarVerificarExistenciaExemplarItemEmprestimo(Integer exemplar) {
    	String sql = "select COUNT(*) from itememprestimo where exemplar = " + exemplar;
    	
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean excluirExemplarCatalogoValidandoItemEmprestimo(ExemplarVO exemplar, UsuarioVO usuarioLogado) throws Exception {					
		Catalogo.excluir(Catalogo.getIdEntidade(), usuarioLogado);		
    	if(realizaVerificacaoExemplarEstaEmprestado(exemplar.getCodigo())) {
			if(!exemplar.getTituloExemplar().equals("")) {
				throw new Exception("O exemplar \""+ exemplar.getTituloExemplar()+"\" e tombo "+exemplar.getCodigoBarra()+" não pode ser excluido porque está emprestado.");
			} else {
				throw new Exception("O exemplar \""+ exemplar.getCodigoBarra()+"\" não pode ser excluido porque está emprestado.");
			}
   		} else {
   			excluirExemplarItemEmprestimo(exemplar.getCodigo(), usuarioLogado);
   			getFacadeFactory().getHistoricoExemplarFacade().excluirHistoricoExemplars(exemplar.getCodigo(), usuarioLogado);
   			getFacadeFactory().getRegistroEntradaAcervoFacade().excluirExemplarItemRegistroEntradaAcervo(exemplar.getCodigo(), usuarioLogado);
   			getFacadeFactory().getItemRegistroSaidaAcervoFacade().excluirPorExemplar(exemplar.getCodigo(), usuarioLogado);
   			getFacadeFactory().getItensRenovadosFacade().excluirPorExemplar(exemplar.getCodigo(), usuarioLogado);
   			excluir(exemplar, usuarioLogado);
   			return true;
   		}
    }
    
    /**
     * Responsável por realizar uma consulta de <code>Exemplar</code> através do valor do atributo
     * <code>String situacaoAtual</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ExemplarVO> consultarPorSituacaoAtual(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino,  UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE upper( situacaoAtual ) like(?) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        	
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }
        sqlStr.append(" ORDER BY situacaoAtual, codigobarra ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorSituacaoAtualECodigoBiblioteca(String valorConsulta, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE upper( situacaoAtual ) like('" + valorConsulta.toUpperCase() + "%') and biblioteca = " + codigoBiblioteca + " ORDER BY situacaoAtual";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoAssinaturaPeriodico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE assinaturaPeriodico = " + valorConsulta + " ORDER BY situacaoAtual";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Exemplar</code> através do valor do atributo
     * <code>String codigoBarra</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ExemplarVO> consultarPorCodigoBarraDisponivel(String valorConsulta, String disponivel, boolean controlarAcesso, int nivelMontarDados,  Integer unidadeEnsino,  UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE upper( codigoBarra ) like(?) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        	
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }
        if (disponivel.equals("")) {
            sqlStr.append("ORDER BY codigoBarra");
        } else {
            sqlStr.append(" AND situacaoAtual = '").append(disponivel).append("' ORDER BY codigoBarra");
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoBarraDisponivelECodigoBiblioteca(String valorConsulta, String disponivel, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = " SELECT * FROM Exemplar WHERE upper( codigoBarra ) like('" + valorConsulta.toUpperCase() + "%') AND biblioteca =" + codigoBiblioteca;
        if (disponivel.equals("")) {
            sqlStr += " ORDER BY codigoBarra";
        } else {
            sqlStr += " AND situacaoAtual = '" + disponivel + "' ORDER BY codigoBarra";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public ExemplarVO consultarPorCodigoBarrasUnico(String codigoBarras, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE codigoBarra::NUMERIC(30) = ").append(Long.parseLong(codigoBarras));
        if (biblioteca > 0) {
        	sqlStr.append(" and biblioteca = ").append(biblioteca);
        }
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            if (!tabelaResultado.next()) {
                return new ExemplarVO();
            }
            return (montarDados(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public ExemplarVO consultarPorCodigoBarrasUnicoCodigoCatalogo(String codigoBarras, Integer codigoCatalogo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE codigoBarra::NUMERIC(30) = ? AND catalogo = ?");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{Long.parseLong(codigoBarras), codigoCatalogo});
            if (!tabelaResultado.next()) {
                return new ExemplarVO();
            }
            return (montarDados(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List consultarPorTituloCatalogoDisponivel(String titulo, String disponivel, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar ");
        sqlStr.append("INNER JOIN catalogo ON exemplar.catalogo = catalogo.codigo ");
        sqlStr.append("WHERE LOWER( catalogo.titulo ) like ('" + titulo.toLowerCase() + "%') AND situacaoAtual = '" + disponivel
                + "' ORDER BY codigoBarra");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Exemplar</code> através do valor do atributo
     * <code>Integer catalogo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ExemplarVO> consultarPorCatalogo(Integer codigoCatalogo, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE catalogo = ");
        sqlStr.append(codigoCatalogo.intValue());
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }
        sqlStr.append(" ORDER BY biblioteca, numeroExemplar desc");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<ExemplarVO> consultarPorCatalogoDisponivel(Integer catalogo, Integer biblioteca, String disponivel, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE catalogo = " + catalogo + "  and desconsiderarReserva = 'f' ";
        if(Uteis.isAtributoPreenchido(biblioteca)){
        	sqlStr += " and biblioteca = "+biblioteca;
        }
        if (disponivel.equals("")) {
            sqlStr += "ORDER BY codigoBarra";
        } else {
            sqlStr += "AND situacaoAtual = '" + disponivel + "' ORDER BY codigoBarra";
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    public Boolean realizarVerifacaoCatalogoPossuiExemplar(Integer codigoCatalogo) throws Exception {    	
    	String sqlStr = "SELECT * FROM Exemplar WHERE catalogo = " + codigoCatalogo.intValue() + " ORDER BY codigo limit 1";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (tabelaResultado.next());
    }

    public List consultarPorCodigoCatalogoNaoInutilizados(Integer codigoCatalogo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE catalogo = " + codigoCatalogo.intValue()
                + " AND situacaoAtual <> 'IT' ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoCatalogoSituacaoAtual(Integer codigoCatalogo, String situacaoAtual, boolean controlarAcesso,
            int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE catalogo = " + codigoCatalogo.intValue() + " AND situacaoAtual = '" + situacaoAtual
                + "' ORDER BY catalogo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeAssinaturaPeriodico(String assinaturaPeriodico, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct Exemplar.* FROM Exemplar");
        sqlStr.append(" INNER JOIN assinaturaPeriodico ON assinaturaPeriodico.codigo = exemplar.assinaturaPeriodico");
        sqlStr.append(" WHERE UPPER(assinaturaPeriodico.nome) LIKE('").append(assinaturaPeriodico).append("%')");
        sqlStr.append(" ORDER BY exemplar.numeroEdicao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Exemplar</code> através do valor do atributo <code>nome</code> da
     * classe <code>Biblioteca</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ExemplarVO> consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Exemplar.* FROM Exemplar inner join Biblioteca on Exemplar.biblioteca = Biblioteca.codigo where sem_acentos( Biblioteca.nome ) ilike(sem_acentos(?)) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        	
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }
        sqlStr.append(" ORDER BY Biblioteca.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeBibliotecaETipoSaida(String valorConsulta, String tipoSaida, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Exemplar.* FROM Exemplar ");
        sqlStr.append("INNER JOIN biblioteca ON exemplar.biblioteca = biblioteca.codigo ");
        sqlStr.append("INNER JOIN itemRegistrosaidaAcervo ON itemRegistrosaidaAcervo.exemplar = Exemplar.codigo ");
        sqlStr.append("WHERE Exemplar.biblioteca = Biblioteca.codigo and upper( Biblioteca.nome ) like('");
        sqlStr.append(valorConsulta.toUpperCase()).append("') ");
        sqlStr.append("AND itemRegistrosaidaAcervo.tipoSaida = '").append(tipoSaida).append("' ORDER BY Biblioteca.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarExemplaresDefasados(Integer codigoBiblioteca, String tipoDefasagem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Exemplar.* FROM Exemplar ");
        sqlStr.append("INNER JOIN catalogo c on exemplar.catalogo = c.codigo ");
        if (tipoDefasagem.equals("basica")) {
            sqlStr.append("where cast('01-01-'||c.anoPublicacao AS date) +  cast( c.bibliograficaBasicaMes || '  month' as interval) < current_date ");
        } else {
            // complementar
            sqlStr.append("where cast('01-01-'||c.anoPublicacao AS date) +  cast( c.bibliograficaComplementarMes || '  month' as interval) < current_date ");
        }
        sqlStr.append("AND exemplar.biblioteca = ").append(codigoBiblioteca).append(" ORDER BY Exemplar.codigo ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<ExemplarVO> consultaRapidaPorCodigoExemplar(Integer valorConsulta, UsuarioVO usuario) throws Exception {
    	StringBuilder SQL = new StringBuilder();
    	SQL.append(" SELECT exemplar.codigo, exemplar.codigobarra, exemplar.situacaoatual, exemplar.biblioteca, catalogo.codigo as \"catalogo.codigo\", catalogo.titulo FROM exemplar ");
    	SQL.append(" LEFT JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
    	SQL.append(" WHERE exemplar.codigo = " + valorConsulta.intValue());
        
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(SQL.toString());
        
        List<ExemplarVO> vetResultado = new ArrayList<ExemplarVO>(0);
        while (tabelaResultado.next()) {
			ExemplarVO exemplar = new ExemplarVO();
			exemplar.setCodigo(tabelaResultado.getInt("codigo"));
			exemplar.setCodigoBarra(tabelaResultado.getString("codigobarra"));
			exemplar.setSituacaoAtual(tabelaResultado.getString("situacaoatual"));
			exemplar.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("biblioteca"), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			exemplar.getCatalogo().setCodigo(tabelaResultado.getInt("catalogo.codigo"));
			exemplar.getCatalogo().setTitulo(tabelaResultado.getString("titulo"));
			vetResultado.add(exemplar);
		}
        
        return vetResultado;
        
    }

    /**
     * Responsável por realizar uma consulta de <code>Exemplar</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ExemplarVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE codigo = ? ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)) {        	
        	sqlStr.append(" and exists (select ueb.biblioteca from unidadeensinobiblioteca ueb where ueb.biblioteca = exemplar.biblioteca and ueb.unidadeensino = ").append(unidadeEnsino).append("  ) ");
        }
        sqlStr.append(" ORDER BY codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.intValue());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCodigoECodigoBiblioteca(Integer valorConsulta, Integer codigoBiblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Exemplar WHERE codigo = " + valorConsulta.intValue() + " AND biblioteca = " + codigoBiblioteca + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ExemplarVO</code> resultantes da consulta.
     */
    public static List<ExemplarVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ExemplarVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ExemplarVO</code>.
     *
     * @return O objeto da classe <code>ExemplarVO</code> com os dados devidamente montados.
     */
    public static ExemplarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ExemplarVO obj = new ExemplarVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getSecao().setCodigo(dadosSQL.getInt("secao"));
        obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
        obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo"));
        obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
        obj.setSituacaoAtual(dadosSQL.getString("situacaoAtual"));
        obj.setEstadoExemplar(dadosSQL.getString("estadoExemplar"));
        obj.setTipoEntrada(dadosSQL.getString("tipoEntrada"));
        obj.setTipoExemplar(dadosSQL.getString("tipoExemplar"));
        obj.setLocal(dadosSQL.getString("local"));
        obj.setNumeroEdicao(dadosSQL.getInt("numeroEdicao"));
        obj.getAssinaturaPeriodico().setCodigo(dadosSQL.getInt("assinaturaPeriodico"));
        obj.setTituloExemplar(dadosSQL.getString("tituloExemplar"));
        obj.setDataPublicacao(dadosSQL.getDate("dataPublicacao"));
        obj.setVolume(dadosSQL.getString("volume"));
        obj.setNumeroExemplar(dadosSQL.getInt("numeroExemplar"));
        obj.setEdicao(dadosSQL.getString("edicao"));
        obj.setSubtitulo(dadosSQL.getString("subtitulo"));
        obj.setMes(dadosSQL.getString("mes"));
        obj.setAnovolume(dadosSQL.getString("anovolume"));
        obj.setAnoPublicacao(dadosSQL.getString("anopublicacao"));
        obj.setNrEdicaoEspecial(dadosSQL.getString("nrEdicaoEspecial"));
        obj.setParaConsulta(dadosSQL.getBoolean("paraconsulta"));
        obj.setEmprestarSomenteFinalDeSemana(dadosSQL.getBoolean("emprestarsomentefinaldesemana"));
        obj.setNrNotaFiscal(dadosSQL.getInt("nrNotaFiscal"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("abreviacaoTitulo"));
        obj.setTipoMidia(dadosSQL.getString("tipoMidia"));
        obj.setDataAquisicao(dadosSQL.getDate("dataAquisicao"));      
		if (!Uteis.isAtributoPreenchido(obj.getTipoMidia())) {
			obj.setTipoMidia("NP");
		}
		obj.setDesconsiderarReserva(dadosSQL.getBoolean("desconsiderarReserva"));
        obj.setBibliotecaAtual(dadosSQL.getInt("bibliotecaAtual"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS){
            montarDadosSecao(obj, nivelMontarDados, usuario);
            montarDadosBiblioteca(obj, nivelMontarDados, usuario);        	
        	montarDadosMinimosCatalogo(obj, usuario);
        	return obj;
        }
		if(obj.getTipoMidia() != null && !obj.getTipoMidia().equals(TipoMidiaEnum.NAO_POSSUI.getKey())) {
			obj.getCatalogo().setTitulo(UteisJSF.internacionalizar("msg_Biblioteca_MidiaAdicional").replace("{0}", obj.getCatalogo().getTitulo()).replace("{1}", TipoMidiaEnum.getEnumPorValor(obj.getTipoMidia()).getValue()));
		}
        if (dadosSQL.getInt("fornecedor") > 0) {
        	obj.setFornecedorVO(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(dadosSQL.getInt("fornecedor"), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
        }
        obj.setValorCompra(dadosSQL.getDouble("valorCompra"));
        obj.setDataCompra(dadosSQL.getDate("dataCompra"));
        
        if(nivelMontarDados == Uteis.NIVELMONTARDADOS_JOBATRASODEVOLUCAO){
        	return obj;
        }
        
//        montarDadosMinimosAssinaturaPeriodico(obj, usuario);
        montarDadosMinimosCatalogo(obj, usuario);
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            montarDadosSecao(obj, nivelMontarDados, usuario);
            montarDadosBiblioteca(obj, nivelMontarDados, usuario);
            return obj;
        }
        montarDadosSecao(obj, nivelMontarDados, usuario);
        montarDadosBiblioteca(obj, nivelMontarDados, usuario);

        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        obj.setHistoricoExemplarVOs(HistoricoExemplar.consultarHistoricoExemplars(obj.getCodigo(), nivelMontarDados, usuario));
        return obj;
    }

    public static void montarDadosMinimosCatalogo(ExemplarVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCatalogo().getCodigo().intValue() == 0) {
            obj.setCatalogo(new CatalogoVO());
            return;
        }
        obj.setCatalogo(getFacadeFactory().getCatalogoFacade().consultarPorChavePrimaria(obj.getCatalogo().getCodigo(),
                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, 0, usuario));
    }

    public static void montarDadosMinimosAssinaturaPeriodico(ExemplarVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getAssinaturaPeriodico().getCodigo().intValue() == 0) {
            obj.setAssinaturaPeriodico(new AssinaturaPeriodicoVO());
            return;
        }
        obj.setAssinaturaPeriodico(getFacadeFactory().getAssinaturaPeriodicoFacade().consultarPorChavePrimaria(obj.getAssinaturaPeriodico().getCodigo(),
                Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }

    public static void montarDadosSecao(ExemplarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getSecao().getCodigo().intValue() == 0) {
            obj.setSecao(new SecaoVO());
            return;
        }
        obj.setSecao(getFacadeFactory().getSecaoFacade().consultarPorChavePrimaria(obj.getSecao().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>BibliotecaVO</code> relacionado ao objeto
     * <code>ExemplarVO</code>. Faz uso da chave primária da classe <code>BibliotecaVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosBiblioteca(ExemplarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBiblioteca().getCodigo().intValue() == 0) {
            obj.setBiblioteca(new BibliotecaVO());
            return;
        }
        obj.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getCodigo(),
                nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ExemplarVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ExemplarVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sqlStr = "SELECT * FROM Exemplar INNER JOIN catalogo on catalogo.codigo = exemplar.catalogo  WHERE exemplar.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Exemplar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarListaExemplaresPorCodigoCatalogoSituacaoAtual(Integer catalogo, String situacaoAtual, List<ExemplarVO> objetos, UsuarioVO usuario)  throws Exception {
    	validarDadosUnicidadeBibliotecaEdicaoVolumeNumero(objetos);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ExemplarVO objeto = (ExemplarVO) e.next();
            if (objeto.getCodigo().equals(0)) {
                objeto.getCatalogo().setCodigo(catalogo);
                incluir(objeto, usuario);
            } else {
                objeto.getCatalogo().setCodigo(catalogo);
                alterar(objeto, false, usuario);
            }
        }
    }
   
    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Exemplar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        Exemplar.idEntidade = idEntidade;
    }

    public void carregarDados(ExemplarVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        carregarDados((ExemplarVO) obj, unidadeEnsino, NivelMontarDados.TODOS, usuario);
    }

    /**
     * Método responsavel por validar se o Nivel de Montar Dados é Básico ou Completo e faz a consulta
     * de acordo com o nível especificado.
     * @param obj
     * @param nivelMontarDados
     * @throws Exception
     * @author Carlos
     */
    public void carregarDados(ExemplarVO obj, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), unidadeEnsino, usuario);
            montarDadosBasico((ExemplarVO) obj, resultado, unidadeEnsino);
        }
        if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), unidadeEnsino, usuario);
            montarDadosCompleto((ExemplarVO) obj, resultado, unidadeEnsino);
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codExemplar, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append(" WHERE (Exemplar.codigo= ").append(codExemplar).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return tabelaResultado;
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codExemplar, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaCompleta(unidadeEnsino);
        sqlStr.append(" WHERE (Exemplar.codigo= ").append(codExemplar).append(")");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Exemplar).");
        }
        return tabelaResultado;
    }

    private StringBuilder getSQLPadraoConsultaBasicaTotalRegistro() {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("Select COUNT(distinct exemplar.codigo) From exemplar ");
        sqlStr.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
        sqlStr.append(" LEFT JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
        sqlStr.append(" LEFT JOIN secao ON secao.codigo = exemplar.secao ");
        sqlStr.append(" LEFT JOIN unidadeEnsinoBiblioteca ON unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo ");
        return sqlStr;
    }

    private StringBuilder getSQLPadraoConsultaBasica(Integer unidadeEnsino) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT DISTINCT exemplar.codigo AS \"exemplar.codigo\",exemplar.datapublicacao AS \"exemplar.datapublicacao\", exemplar.abreviacaoTitulo AS \"exemplar.abreviacaoTitulo\", exemplar.codigoBarra AS \"exemplar.codigoBarra\", exemplar.situacaoatual AS \"exemplar.situacaoatual\", exemplar.local AS \"exemplar.local\", ");
        str.append(" exemplar.volume AS \"exemplar.volume\", exemplar.numeroExemplar as \"exemplar.numeroExemplar\", exemplar.edicao as \"exemplar.edicao\", exemplar.subtitulo as \"exemplar.subtitulo\", exemplar.tituloexemplar as \"exemplar.tituloexemplar\", ");
        str.append(" exemplar.nrNotaFiscal AS \"exemplar.nrNotaFiscal\", exemplar.fornecedor as \"exemplar.fornecedor\", exemplar.valorCompra as \"exemplar.valorCompra\", exemplar.dataCompra as \"exemplar.dataCompra\", exemplar.tipomidia as \"exemplar.tipomidia\", exemplar.tipoEntrada as \"exemplar.tipoEntrada\", ");
        str.append(" exemplar.paraconsulta AS \"exemplar.paraconsulta\", exemplar.anoVolume AS \"exemplar.anoVolume\", exemplar.mes AS \"exemplar.mes\", exemplar.emprestarsomentefinaldesemana AS \"exemplar.emprestarsomentefinaldesemana\", biblioteca.codigo AS \"biblioteca.codigo\", biblioteca.nome AS \"biblioteca.nome\", ");
        str.append(" catalogo.codigo AS \"catalogo.codigo\", catalogo.titulo AS \"catalogo.titulo\", secao.codigo AS \"secao.codigo\", secao.nome AS \"secao.nome\", exemplar.link as \"exemplar.link\", exemplar.fasciculos as \"exemplar.fasciculos\" , exemplar.anoPublicacao as \"exemplar.anoPublicacao\", exemplar.anoPublicacao as \"exemplar.anoPublicacao\",  ");
        str.append(" exemplar.nrPaginas as \"exemplar.nrPaginas\", exemplar.isbn as \"exemplar.isbn\", exemplar.issn as \"exemplar.issn\", exemplar.bibliotecaatual as \"exemplar.bibliotecaatual\", exemplar.estadoexemplar as \"exemplar.estadoexemplar\", exemplar.nredicaoespecial as \"exemplar.nredicaoespecial\", exemplar.dataAquisicao as \"exemplar.dataAquisicao\" ");
        if (unidadeEnsino > 0) {
        	str.append(", (select count(distinct unidadeEnsinoBiblioteca.unidadeensino) from unidadeEnsinoBiblioteca where unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo and unidadeEnsinoBiblioteca.unidadeensino = " + unidadeEnsino + " ) as qtdunidadeensino ");
        }
        str.append(" FROM exemplar ");
        str.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
        str.append(" INNER JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
        str.append(" LEFT JOIN secao ON secao.codigo = exemplar.secao ");
        str.append(" LEFT JOIN unidadeEnsinoBiblioteca ON unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo ");
        return str;
    }

    private StringBuilder getSQLPadraoConsultaCompleta(Integer unidadeEnsino) {
        StringBuilder str = new StringBuilder();
        //Dados Exemplar
        str.append("SELECT DISTINCT exemplar.codigo AS \"exemplar.codigo\",  exemplar.abreviacaoTitulo AS \"exemplar.abreviacaoTitulo\", exemplar.codigoBarra AS \"exemplar.codigoBarra\", exemplar.situacaoatual AS \"exemplar.situacaoatual\", exemplar.numeroExemplar as \"exemplar.numeroExemplar\", ");
        str.append(" exemplar.tipoExemplar AS \"exemplar.tipoExemplar\", exemplar.tipoEntrada AS \"exemplar.tipoEntrada\", exemplar.estadoExemplar AS \"exemplar.estadoExemplar\", exemplar.volume as \"exemplar.volume\", ");
        str.append(" exemplar.local AS \"exemplar.local\", exemplar.numeroedicao AS \"exemplar.numeroedicao\", exemplar.tituloExemplar AS \"exemplar.tituloExemplar\", exemplar.datapublicacao AS \"exemplar.datapublicacao\", ");
        str.append(" exemplar.edicao as \"exemplar.edicao\", exemplar.subtitulo as \"exemplar.subtitulo\", exemplar.tituloexemplar as \"exemplar.tituloexemplar\", exemplar.paraconsulta AS \"exemplar.paraconsulta\", exemplar.emprestarsomentefinaldesemana AS \"exemplar.emprestarsomentefinaldesemana\", ");
        str.append(" exemplar.nrNotaFiscal AS \"exemplar.nrNotaFiscal\", exemplar.fornecedor as \"exemplar.fornecedor\", exemplar.valorCompra as \"exemplar.valorCompra\", exemplar.dataCompra as \"exemplar.dataCompra\", ");
        str.append(" exemplar.fasciculo as \"exemplar.fasciculo\", exemplar.bibliotecaatual as \"exemplar.bibliotecaatual\", exemplar.anopublicacao as \"exemplar.anopublicacao\", exemplar.desconsiderarReserva  as \"exemplar.desconsiderarReserva\" , exemplar.dataAquisicao as \"exemplar.dataAquisicao\", ");
        //Dados da Biblioteca
        str.append(" biblioteca.codigo AS \"biblioteca.codigo\", biblioteca.nome AS \"biblioteca.nome\", ");
        //Dados do Catalogo
        str.append(" catalogo.codigo AS \"catalogo.codigo\", catalogo.titulo AS \"catalogo.titulo\", ");
        //Dados Secao
        str.append(" secao.codigo AS \"secao.codigo\", secao.nome AS \"secao.nome\", secao.sigla AS \"secao.sigla\", ");
        //Dados AssinaturaPeriodico
        str.append(" assinaturaperiodico.codigo AS \"assinaturaperiodico.codigo\", assinaturaperiodico.nome AS \"assinaturaperiodico.nome\", ");
        //Dados HistoricoExemplar
        str.append(" historicoExemplar.motivo AS \"historicoExemplar.motivo\", historicoExemplar.situacao AS \"historicoExemplar.situacao\", historicoExemplar.data AS \"historicoExemplar.data\", ");
        str.append(" historicoExemplar.exemplar AS \"historicoExemplar.exemplar\", historicoexemplar.codigo  AS \"historicoexemplar.codigo\", historicoexemplar.estado AS \"historicoexemplar.estado\", ");
        str.append(" usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\"  ");
        if (unidadeEnsino > 0) {
        	str.append(", (select count( distinct unidadeEnsinoBiblioteca.unidadeensino) from unidadeEnsinoBiblioteca where unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo and unidadeEnsinoBiblioteca.unidadeensino = " + unidadeEnsino + " ) as qtdunidadeensino ");
        }
        str.append(" FROM exemplar ");
        str.append(" LEFT JOIN biblioteca ON biblioteca.codigo = exemplar.biblioteca ");
        str.append(" LEFT JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
        str.append(" LEFT JOIN secao ON secao.codigo = exemplar.secao ");
        str.append(" LEFT JOIN assinaturaperiodico ON assinaturaperiodico.codigo = exemplar.assinaturaperiodico ");
        str.append(" LEFT JOIN historicoexemplar ON historicoExemplar.exemplar = exemplar.codigo ");
        str.append(" LEFT JOIN usuario ON usuario.codigo = historicoexemplar.responsavel ");
        str.append(" LEFT JOIN unidadeEnsinoBiblioteca ON unidadeEnsinoBiblioteca.biblioteca = biblioteca.codigo ");        
        return str;
    }

    private void montarDadosBasico(ExemplarVO obj, SqlRowSet dadosSQL, Integer unidadeEnsino) throws Exception {
        //Dados Exemplar
        obj.setCodigo(dadosSQL.getInt("exemplar.codigo"));
        obj.setCodigoBarra(dadosSQL.getString("exemplar.codigoBarra"));
        obj.setSituacaoAtual(dadosSQL.getString("exemplar.situacaoatual"));
        obj.setLocal(dadosSQL.getString("exemplar.local"));
        obj.setVolume(dadosSQL.getString("exemplar.volume"));
        obj.setNumeroExemplar(dadosSQL.getInt("exemplar.numeroExemplar"));
        obj.setTituloExemplar(dadosSQL.getString("exemplar.tituloexemplar"));
        obj.setSubtitulo(dadosSQL.getString("exemplar.subtitulo"));
        obj.setEdicao(dadosSQL.getString("exemplar.edicao"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("exemplar.abreviacaoTitulo"));
        obj.setParaConsulta(dadosSQL.getBoolean("exemplar.paraconsulta"));
        obj.setEmprestarSomenteFinalDeSemana(dadosSQL.getBoolean("exemplar.emprestarsomentefinaldesemana"));
        obj.setNrNotaFiscal(dadosSQL.getInt("exemplar.nrNotaFiscal"));
        obj.setDataPublicacao(dadosSQL.getDate("exemplar.datapublicacao"));
        obj.setBibliotecaAtual(dadosSQL.getInt("exemplar.bibliotecaatual"));
        if (dadosSQL.getInt("exemplar.fornecedor") > 0) {
        	obj.setFornecedorVO(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(dadosSQL.getInt("exemplar.fornecedor"), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null));
        }
        obj.setValorCompra(dadosSQL.getDouble("exemplar.valorCompra"));
        obj.setDataCompra(dadosSQL.getDate("exemplar.dataCompra"));
        obj.setDataAquisicao(dadosSQL.getDate("exemplar.dataAquisicao"));
        obj.setTipoMidia(dadosSQL.getString("exemplar.tipomidia"));
        obj.setTipoEntrada(dadosSQL.getString("exemplar.tipoEntrada"));
        obj.setAnovolume(dadosSQL.getString("exemplar.anoVolume"));
        obj.setMes(dadosSQL.getString("exemplar.mes"));
        obj.setLink(dadosSQL.getString("exemplar.link"));
        obj.setFasciculos(dadosSQL.getString("exemplar.fasciculos"));
        obj.setAnoPublicacao(dadosSQL.getString("exemplar.anoPublicacao"));
        obj.setNrPaginas(dadosSQL.getInt("exemplar.nrPaginas"));
        obj.setIsbn(dadosSQL.getString("exemplar.isbn"));
        obj.setIssn(dadosSQL.getString("exemplar.issn"));
        obj.setEstadoExemplar(dadosSQL.getString("exemplar.estadoexemplar"));
        obj.setNrEdicaoEspecial(dadosSQL.getString("exemplar.nredicaoespecial"));
        
        //Dados Biblioteca
        obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca.codigo"));
        obj.getBiblioteca().setNome(dadosSQL.getString("biblioteca.nome"));
        //Dados Catálogo
        obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo.codigo"));
        obj.getCatalogo().setTitulo(dadosSQL.getString("catalogo.titulo"));
        //Dados Secao
        obj.getSecao().setCodigo(dadosSQL.getInt("secao.codigo"));
        obj.getSecao().setNome(dadosSQL.getString("secao.nome"));

        //logs Transferencia
        obj.setLogTransferenciaBibliotecaExemplarVOs(getFacadeFactory().getLogTransferenciaBibliotecaExemplarFacade().consultarPorExemplar(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO()));
       
        if (unidadeEnsino > 0) {
	        Integer qtdUnidadeEnsino = dadosSQL.getInt("qtdunidadeensino");
//	        Integer codUnidadeEnsino = dadosSQL.getInt("codunidadeensino");
//	        if ((codUnidadeEnsino != null && codUnidadeEnsino == unidadeEnsino) || unidadeEnsino == 0) {
        	if (qtdUnidadeEnsino > 0) {
	        	obj.setExemplarUnidadeLogado(Boolean.TRUE);        	
	        }      
        } else {
        	obj.setExemplarUnidadeLogado(Boolean.TRUE);
        }

    }

    private void montarDadosCompleto(ExemplarVO obj, SqlRowSet dadosSQL, Integer unidadeEnsino) throws Exception {
        //Dados Exemplar
        obj.setCodigo(dadosSQL.getInt("exemplar.codigo"));
        obj.setCodigoBarra(dadosSQL.getString("exemplar.codigoBarra"));
        obj.setSituacaoAtual(dadosSQL.getString("exemplar.situacaoatual"));
        obj.setTipoExemplar(dadosSQL.getString("exemplar.tipoExemplar"));
        obj.setTipoEntrada(dadosSQL.getString("exemplar.tipoEntrada"));
        obj.setEstadoExemplar(dadosSQL.getString("exemplar.estadoExemplar"));
        obj.setLocal(dadosSQL.getString("exemplar.local"));
        obj.setNumeroEdicao(dadosSQL.getInt("exemplar.numeroedicao"));
        obj.setTituloExemplar(dadosSQL.getString("exemplar.tituloExemplar"));
        obj.setDataPublicacao(dadosSQL.getDate("exemplar.datapublicacao"));        
        obj.setVolume(dadosSQL.getString("exemplar.volume"));
        obj.setNumeroExemplar(dadosSQL.getInt("exemplar.numeroExemplar"));
        obj.setTituloExemplar(dadosSQL.getString("exemplar.tituloexemplar"));
        obj.setSubtitulo(dadosSQL.getString("exemplar.subtitulo"));
        obj.setAnoPublicacao(dadosSQL.getString("exemplar.anopublicacao"));
        obj.setEdicao(dadosSQL.getString("exemplar.edicao"));
        obj.setAbreviacaoTitulo(dadosSQL.getString("exemplar.abreviacaoTitulo"));
        obj.setParaConsulta(dadosSQL.getBoolean("exemplar.paraconsulta"));
        obj.setEmprestarSomenteFinalDeSemana(dadosSQL.getBoolean("exemplar.emprestarsomentefinaldesemana"));
        obj.setNrNotaFiscal(dadosSQL.getInt("exemplar.nrNotaFiscal"));
        obj.setBibliotecaAtual(dadosSQL.getInt("exemplar.bibliotecaatual"));
        obj.setDesconsiderarReserva(dadosSQL.getBoolean("exemplar.desconsiderarReserva"));
        obj.setDataAquisicao(dadosSQL.getDate("exemplar.dataAquisicao"));
        if (dadosSQL.getInt("exemplar.fornecedor") > 0) {
        	obj.setFornecedorVO(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(dadosSQL.getInt("exemplar.fornecedor"), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null));
        }
       
        obj.setValorCompra(dadosSQL.getDouble("exemplar.valorCompra"));
        obj.setDataCompra(dadosSQL.getDate("exemplar.dataCompra"));
        //Dados Biblioteca
        obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca.codigo"));
        obj.getBiblioteca().setNome(dadosSQL.getString("biblioteca.nome"));
        //Dados Catalogo
        obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo.codigo"));
        obj.getCatalogo().setTitulo(dadosSQL.getString("catalogo.titulo"));
        //Dados Secao
        obj.getSecao().setCodigo(dadosSQL.getInt("secao.codigo"));
        obj.getSecao().setNome(dadosSQL.getString("secao.nome"));
        obj.getSecao().setSigla(dadosSQL.getString("secao.sigla"));
        //Dados Assinatura Periodico
        obj.getAssinaturaPeriodico().setCodigo(dadosSQL.getInt("assinaturaperiodico.codigo"));
        obj.getAssinaturaPeriodico().setNome(dadosSQL.getString("assinaturaperiodico.nome"));

//        Integer codUnidadeEnsino = dadosSQL.getInt("qtdunidadeensino");
//        if ((codUnidadeEnsino != null && codUnidadeEnsino == unidadeEnsino) || unidadeEnsino == 0) {
//        	obj.setExemplarUnidadeLogado(Boolean.TRUE);        	
//        }
        
        if (unidadeEnsino > 0) {
	        Integer qtdUnidadeEnsino = dadosSQL.getInt("qtdunidadeensino");
//	        Integer codUnidadeEnsino = dadosSQL.getInt("codunidadeensino");
//	        if ((codUnidadeEnsino != null && codUnidadeEnsino == unidadeEnsino) || unidadeEnsino == 0) {
        	if (qtdUnidadeEnsino > 0) {
	        	obj.setExemplarUnidadeLogado(Boolean.TRUE);        	
	        }      
        } else {
        	obj.setExemplarUnidadeLogado(Boolean.TRUE);
        }
        
        // Dados Historico Exemplar
        HistoricoExemplarVO historicoExemplarVO = null;
        obj.getHistoricoExemplarVOs().clear();
        do {
            historicoExemplarVO = new HistoricoExemplarVO();
            historicoExemplarVO.setCodigo(dadosSQL.getInt("historicoexemplar.codigo"));
            historicoExemplarVO.setMotivo(dadosSQL.getString("historicoExemplar.motivo"));
            historicoExemplarVO.setSituacao(dadosSQL.getString("historicoExemplar.situacao"));
            historicoExemplarVO.setData(dadosSQL.getDate("historicoExemplar.data"));
            historicoExemplarVO.setExemplar(dadosSQL.getInt("historicoExemplar.exemplar"));
            historicoExemplarVO.setEstado(dadosSQL.getString("historicoexemplar.estado"));
            historicoExemplarVO.getResponsavel().setCodigo(dadosSQL.getInt("usuario.codigo"));
            historicoExemplarVO.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));
            obj.getHistoricoExemplarVOs().add(historicoExemplarVO);
            if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("historicoExemplar.exemplar")))) {
                return;
            }
        } while (dadosSQL.next());
    }

    public List<ExemplarVO> montarDadosConsultaRapida(SqlRowSet tabelaResultado, Integer unidadeEnsino) throws Exception {
        List<ExemplarVO> vetResultado = new ArrayList<ExemplarVO>(0);
        while (tabelaResultado.next()) {
            ExemplarVO obj = new ExemplarVO();
            montarDadosBasico(obj, tabelaResultado, unidadeEnsino);
            vetResultado.add(obj);
            if (tabelaResultado.getRow() == 0) {
                return vetResultado;
            }
        }
        return vetResultado;
    }

    public List<ExemplarVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE exemplar.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY exemplar.codigo");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado,unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE sem_acentos(lower(biblioteca.nome)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY biblioteca.nome");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado,unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorCodigoCatalogo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE catalogo.codigo = ");
        sqlStr.append(valorConsulta.intValue());
        sqlStr.append(" ORDER BY exemplar.codigo");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado,unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorTituloCatalogo(String valorConsulta, Integer unidadeEnsino, Integer biblioteca, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {    	
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE sem_acentos(lower(catalogo.titulo)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY catalogo.titulo");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE sem_acentos(lower(exemplar.codigoBarra)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY exemplar.codigoBarra");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorSituacaoAtual(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE sem_acentos(lower(exemplar.situacaoAtual)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY exemplar.situacaoAtual");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
    }

    public List<ExemplarVO> consultaRapidaPorTipoExemplar(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        if(valorConsulta.equals("0")){
        	sqlStr.append("WHERE 1 = 1");
        } else {
        	sqlStr.append("WHERE sem_acentos(lower(exemplar.tipoExemplar)) like(sem_acentos('");
        	sqlStr.append(valorConsulta.toLowerCase());
        	sqlStr.append("%')) or (catalogo.assinaturaPeriodico = true) ");
        }
        
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY exemplar.codigo");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
    }

    public Integer consultaTotalDeRegistroRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        if (valorConsulta!=0) {
			sqlStr.append("WHERE exemplar.codigo= ");
		} else {
			sqlStr.append("WHERE exemplar.codigo>= ");
		} 
        sqlStr.append(valorConsulta.intValue());
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultaTotalDeRegistroRapidaPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        sqlStr.append(" WHERE sem_acentos(lower(biblioteca.nome)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND biblioteca.nome = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultaTotalDeRegistroRapidaPorTituloCatalogo(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        sqlStr.append(" WHERE sem_acentos(lower(catalogo.titulo)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultaTotalDeRegistroRapidaPorCodigoBarra(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        sqlStr.append(" WHERE sem_acentos(lower(exemplar.codigoBarra)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultaTotalDeRegistroRapidaPorSituacaoAtual(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        sqlStr.append(" WHERE sem_acentos(lower(exemplar.situacaoAtual)) like(sem_acentos('");
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    public Integer consultaTotalDeRegistroRapidaPorTipoExemplar(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        if(valorConsulta.equals("0")){
        	sqlStr.append("WHERE 1 = 1");
           
        }else{
        sqlStr.append(" WHERE sem_acentos(lower(exemplar.tipoExemplar)) like(sem_acentos('");        
        sqlStr.append(valorConsulta.toLowerCase());
        sqlStr.append("%'))");
        }
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }

    @Override
    public Integer consultarTotalRegistro(String situacao, String codigoBarra, String catalogo, Integer biblioteca, Integer unidadeEnsino) throws Exception {

        StringBuilder sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        sqlStr.append("WHERE 1=1 ");

        if (situacao != null && !situacao.isEmpty() && situacao.contains("(")) {
            sqlStr.append(" AND situacaoatual IN ");
            sqlStr.append(situacao);            
        }else if (situacao != null && !situacao.isEmpty()) {
            sqlStr.append(" AND situacaoatual = '");
            sqlStr.append(situacao);
            sqlStr.append("' ");
        }
        if (catalogo != null && !catalogo.isEmpty()) {
            sqlStr.append(" AND upper(sem_acentos(catalogo.titulo)) like (upper(sem_acentos('%");
            sqlStr.append(catalogo);
            sqlStr.append("%'))) ");
        }

        if (biblioteca != null && biblioteca > 0) {
            sqlStr.append(" AND biblioteca.codigo = ");
            sqlStr.append(biblioteca);
        }

        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;

    }

    
    @Override
    public List<ExemplarVO> consultar(String situacao, String codigoBarra, String catalogo, Integer biblioteca, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        sqlStr.append("WHERE 1=1 ");

        if (situacao != null && !situacao.isEmpty() && situacao.contains("(")) {
            sqlStr.append(" AND situacaoatual IN ");
            sqlStr.append(situacao);            
        }else if (situacao != null && !situacao.isEmpty()) {
            sqlStr.append(" AND situacaoatual = '");
            sqlStr.append(situacao);
            sqlStr.append("' ");
        }
        if (catalogo != null && !catalogo.isEmpty()) {
            sqlStr.append(" AND upper(sem_acentos(catalogo.titulo)) like (upper(sem_acentos('%");
            sqlStr.append(catalogo);
            sqlStr.append("%'))) ");
        }
        
        if (biblioteca != null && biblioteca > 0) {
            sqlStr.append(" AND biblioteca.codigo = ");
            sqlStr.append(biblioteca);
        }

        if(!Uteis.isAtributoPreenchido(configuracaoBiblioteca) || !configuracaoBiblioteca.getLiberaDevolucaoExemplarOutraBiblioteca()) {
	        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
	            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
	            sqlStr.append(unidadeEnsino.intValue());
	        }
        }
	        
        sqlStr.append(" ORDER BY catalogo.titulo");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);

    }

    public List<ExemplarPainelGestorBibliotecaVO> consultarAcervoPeriodo(Date dataInicio, Date dataFim) throws Exception {
        StringBuilder sqlStr = new StringBuilder("");
        sqlStr.append(" SELECT DISTINCT b.codigo AS codigoBiblioteca, b.nome AS nomeBiblioteca, ");
        sqlStr.append(" (SELECT COUNT(exemplar.codigo) from exemplar ");
        sqlStr.append(" INNER JOIN catalogo ca on exemplar.catalogo = ca.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" WHERE situacaoatual <> 'IT' AND exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" AND ca.dataCadastro Between '").append(Uteis.getDataFormatoBD(dataInicio)).append("' AND '");
        sqlStr.append(Uteis.getDataFormatoBD(dataFim)).append("') AS qtdAtivo, ");
        sqlStr.append(" (SELECT COUNT(exemplar.codigo) from exemplar ");
        sqlStr.append(" INNER JOIN catalogo ca on exemplar.catalogo = ca.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" where case when ca.anoPublicacao is not null and ca.anoPublicacao != '' ");
        sqlStr.append(" then cast('01-01-'||ca.anoPublicacao AS date) + cast( ca.bibliograficaBasicaMes || '  month' as interval) <= current_date end ");
        sqlStr.append(" AND exemplar.biblioteca = ba.codigo) AS qtdDefasadoBasica, ");
        sqlStr.append(" (SELECT COUNT(exemplar.codigo) from exemplar ");
        sqlStr.append(" INNER JOIN catalogo ca on exemplar.catalogo = ca.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" where case when ca.anoPublicacao is not null and ca.anoPublicacao != '' then  cast('01-01-'||ca.anoPublicacao AS date) + ");
        sqlStr.append(" cast( ca.bibliograficaComplementarMes || '  month' as interval) <= current_date end ");
        sqlStr.append(" AND exemplar.biblioteca = ba.codigo) AS qtdDefasadoComplementar, ");
        sqlStr.append(" (SELECT COUNT (rsa.codigo) FROM RegistroSaidaAcervo rsa ");
        sqlStr.append(" INNER JOIN itemRegistroSaidaAcervo irsa ON irsa.registroSaidaAcervo = rsa.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" WHERE tipoSaida = 'EX' AND data Between '").append(Uteis.getDataFormatoBD(dataInicio)).append("' AND '");
        sqlStr.append(Uteis.getDataFormatoBD(dataFim)).append("') AS qtdExtraviado, ");
        sqlStr.append(" (SELECT COUNT (rsa.codigo) FROM RegistroSaidaAcervo rsa ");
        sqlStr.append(" INNER JOIN itemRegistroSaidaAcervo irsa ON irsa.registroSaidaAcervo = rsa.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" WHERE tipoSaida = 'DO' AND rsa.biblioteca = ba.codigo ");
        sqlStr.append(" AND data Between '").append(Uteis.getDataFormatoBD(dataInicio)).append("' AND '");
        sqlStr.append(Uteis.getDataFormatoBD(dataFim)).append("') AS qtdDoacao, ");
        sqlStr.append(" (SELECT COUNT (rsa.codigo) FROM RegistroSaidaAcervo rsa ");
        sqlStr.append(" INNER JOIN itemRegistroSaidaAcervo irsa ON irsa.registroSaidaAcervo = rsa.codigo ");
        sqlStr.append(" INNER JOIN biblioteca ba on exemplar.biblioteca = ba.codigo ");
        sqlStr.append(" WHERE tipoSaida = 'MU' AND rsa.biblioteca = ba.codigo ");
        sqlStr.append(" AND data Between '").append(Uteis.getDataFormatoBD(dataInicio)).append("' and '");
        sqlStr.append(Uteis.getDataFormatoBD(dataFim)).append("') AS qtdMutilado ");
        sqlStr.append(" FROM exemplar ");
        sqlStr.append(" INNER JOIN biblioteca b on exemplar.biblioteca = b.codigo ");
        sqlStr.append(" INNER JOIN catalogo c on exemplar.catalogo = c.codigo ");
        sqlStr.append(" WHERE dataCadastro Between '").append(Uteis.getDataFormatoBD(dataInicio)).append("' and '");
        sqlStr.append(Uteis.getDataFormatoBD(dataFim)).append("' ");
        sqlStr.append(" ORDER BY b.nome ");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            ExemplarPainelGestorBibliotecaVO obj = new ExemplarPainelGestorBibliotecaVO();
            obj.setCodigoBiblioteca(tabelaResultado.getInt("codigoBiblioteca"));
            obj.setNomeBiblioteca(tabelaResultado.getString("nomeBiblioteca"));
            obj.setQtdAtivo(tabelaResultado.getInt("qtdAtivo"));
            obj.setQtdDefasado(tabelaResultado.getInt("qtdDefasadoBasica"));
            obj.setQtdDefasadoComplementar(tabelaResultado.getInt("qtdDefasadoComplementar"));
            obj.setQtdExtraviado(tabelaResultado.getInt("qtdExtraviado"));
            obj.setQtdDoacao(tabelaResultado.getInt("qtdDoacao"));
            obj.setQtdMutilado(tabelaResultado.getInt("qtdMutilado"));

            vetResultado.add(obj);
        }
        return vetResultado;
    }
    
    
    public List<ExemplarVO> consultaRapidaPorCodigoBiblioteca(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
        if(!valorConsulta.equals("0")){
        sqlStr.append("WHERE biblioteca.codigo=");
           
        }else{
        	 sqlStr.append("WHERE biblioteca.codigo >=");
        }
        sqlStr.append(valorConsulta); 
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append(" ORDER BY biblioteca.nome");
        if (limite != null) {
            sqlStr.append(" LIMIT ").append(limite);
            if (offset != null) {
                sqlStr.append(" OFFSET ").append(offset);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
    }
   
    public Integer consultaTotalDeRegistroRapidaPorCodigoBiblioteca(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
        if(!valorConsulta.equals("0")){
            sqlStr.append("WHERE biblioteca.codigo=");
               
            }else{
            	 sqlStr.append("WHERE biblioteca.codigo>=");
            }
            sqlStr.append(valorConsulta);  
        if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
            sqlStr.append(" AND biblioteca.nome = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (resultado.next()) {
            return resultado.getInt("count");
        }
        return 0;
    }
    @Override
	public List<ExemplarVO> consultaRapidaPorCodigoCatalogo(String valorConsulta, Integer unidadeEnsino, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
		if (!valorConsulta.equals("0")) {
			sqlStr.append("WHERE catalogo.codigo= ");
		} else {
			sqlStr.append("WHERE catalogo.codigo>= ");
		}
		sqlStr.append(valorConsulta);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeensino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY catalogo.titulo");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
	}
    @Override
	public Integer consultaTotalDeRegistroRapidaPorCodigoCatalogo(String valorConsulta, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr = getSQLPadraoConsultaBasicaTotalRegistro();
		if (!valorConsulta.equals("0")) {
			sqlStr.append("WHERE catalogo.codigo= ");
		} else {
			sqlStr.append("WHERE catalogo.codigo>= ");
		}
		sqlStr.append(valorConsulta);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND unidadeEnsinoBiblioteca.unidadeEnsino = ");
			sqlStr.append(unidadeEnsino.intValue());
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}
    
	@Override
	public List<ExemplarVO> consultaRapidaPorCodigoBarraCatalogo(String valorConsulta, Integer unidadeEnsino, Integer catalogo, Integer biblioteca, Integer limite, Integer offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica(unidadeEnsino);
		sqlStr.append("WHERE sem_acentos(lower(exemplar.codigoBarra)) like(sem_acentos('");
		sqlStr.append(valorConsulta.toLowerCase());
		sqlStr.append("%'))");
		if (biblioteca.intValue() != 0 && biblioteca != null) {
			sqlStr.append(" AND unidadeEnsinoBiblioteca.biblioteca = ");
			sqlStr.append(biblioteca.intValue());
		}
		if (catalogo != 0) {
			sqlStr.append(" AND catalogo.codigo = ").append(catalogo);
		}
		sqlStr.append(" ORDER BY exemplar.codigoBarra");
		if (limite != null) {
			sqlStr.append(" LIMIT ").append(limite);
			if (offset != null) {
				sqlStr.append(" OFFSET ").append(offset);
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return montarDadosConsultaRapida(tabelaResultado, unidadeEnsino);
	}
	
	public Boolean verificarExisteEmprestimoParaDeterminadoExemplar(Integer codigoExemplar) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT itememprestimo.codigo FROM emprestimo ");
		sqlStr.append(" INNER JOIN itememprestimo ON itememprestimo.emprestimo = emprestimo.codigo ");
		sqlStr.append(" INNER JOIN exemplar ON itememprestimo.exemplar = exemplar.codigo ");
		sqlStr.append(" WHERE exemplar.codigo = ").append(codigoExemplar);
		sqlStr.append(" AND itememprestimo.situacao = 'EX' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	public static String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "biblioteca" + File.separator);
    }

    public static String designerGuiaReservaExemplar() {
        return caminhoBaseRelatorio() + "GuiaReservaExemplarRel.jrxml";
    }
    
	public ExemplarVO consultarPorCodigoBarrasUnicoBiblioteca(String codigoBarras, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM Exemplar WHERE codigoBarra::NUMERIC(30) = ").append(Long.parseLong(codigoBarras));
		if (biblioteca > 0) {
			sqlStr.append(" and biblioteca = ").append(biblioteca);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new ExemplarVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	

	@Override
	public Boolean verificarExemplarPertenceBiblioteca(Integer codigoExemplar, Integer biblioteca) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT exemplar.codigo as exemplar FROM exemplar WHERE exemplar.codigo = ").append(codigoExemplar);
		sqlStr.append(" AND exemplar.biblioteca = ").append(biblioteca);
		sqlStr.append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean realizaVerificacaoExemplarEstaEmprestado(Integer codigoExemplar) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT exemplar.codigo as exemplar FROM exemplar WHERE exemplar.codigo = ").append(codigoExemplar);
		sqlStr.append(" AND exemplar.situacaoAtual = '"+SituacaoExemplar.EMPRESTADO.getValor()+"' ");
		sqlStr.append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next();
	}
	public boolean realizaVerificacaoExemplarDiferenteDisponivel(Integer codigoExemplar) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT exemplar.codigo as exemplar FROM exemplar WHERE exemplar.codigo = ").append(codigoExemplar);
		sqlStr.append(" AND exemplar.situacaoAtual != '"+SituacaoExemplar.DISPONIVEL.getValor()+"' ");
		sqlStr.append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next();
	}
    
	@Override
	public List<String> consultarMensagemCadastrosReferenciamExemplar(Integer exemplar) throws Exception{
		List<String> entidadesReferenciadas = new ArrayList<String>(0);
		StringBuilder sqBuilder = new StringBuilder("");
		sqBuilder.append("select 'EMPRESTIMO' as entidade, count(codigo) as qtde from itememprestimo where exemplar = ").append(exemplar);
		sqBuilder.append(" UNION ");
		sqBuilder.append("select 'SAIDA_ACERVO' as entidade, count(codigo) as qtde from itemregistrosaidaacervo where exemplar = ").append(exemplar);
		sqBuilder.append(" UNION ");
		sqBuilder.append("select 'ENTRADA_ACERVO' as entidade, count(codigo) as qtde from itemregistroentradaacervo where exemplar = ").append(exemplar);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqBuilder.toString());
		while(rs.next()){
			if(rs.getInt("qtde") > 0){
				if(rs.getString("entidade").equals("EMPRESTIMO")){
					entidadesReferenciadas.add(UteisJSF.internacionalizar("msg_Exemplar_excluirItemEmprestimo").replace("{0}", ""+rs.getInt("qtde")));
				}else if(rs.getString("entidade").equals("SAIDA_ACERVO")){
					entidadesReferenciadas.add(UteisJSF.internacionalizar("msg_Exemplar_excluirSaidaAcervo").replace("{0}", ""+rs.getInt("qtde")));
				}else if(rs.getString("entidade").equals("ENTRADA_ACERVO")){
					entidadesReferenciadas.add(UteisJSF.internacionalizar("msg_Exemplar_excluirEntradaAcervo").replace("{0}", ""+rs.getInt("qtde")));
				}
			}
		}
		return entidadesReferenciadas;
	} 

	
    	 /**
        * Operação que altera a biblioteca do exemplar.
        *
        * @param exemplarVO
        * @throws Exception
        */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarTransferenciaExemplarBiblioteca(ExemplarVO exemplarVO, UsuarioVO usuarioLogado) throws Exception {
        try {
        	if(exemplarVO.getCatalogo().getAssinaturaPeriodico()){
        		if(!ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Periodico_PermiteTransfrenciaExemplarEntreBibliotecas", usuarioLogado)){
        			throw new Exception("O usuário "+usuarioLogado.getNome()+" Não possui permissão para realizar Transfência de Exemplar Entre Bibliotecas - Periódico");
        		}
        	}else {
        		if(!ControleAcesso.verificarPermissaoFuncionalidadeUsuario("Catalogo_PermiteTransfrenciaExemplarEntreBibliotecas", usuarioLogado)){
        			throw new Exception("O usuário "+usuarioLogado.getNome()+" Não possui permissão para realizar Transfência de Exemplar Entre Bibliotecas - Catalogo");
        		}
			}
        	if(realizaVerificacaoExemplarDiferenteDisponivel(exemplarVO.getCodigo())){
        		throw new Exception("O Exemplar está com a situação diferente de Disponível. Sendo assim não é possível realizar a Transfência de Exemplar Entre Bibliotecas.");
        	}
            String sql = "UPDATE Exemplar set biblioteca=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
            getConexao().getJdbcTemplate().update(sql, new Object[]{exemplarVO.getBibliotecaDestino().getCodigo(), exemplarVO.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
       
       public SqlRowSet consultarMaiorCodigoBarraValido(byte tamanhoCodigo) throws Exception {
   		StringBuilder sqlStr = new StringBuilder();
   		sqlStr.append("SELECT t.codigo as resultado FROM ( ");
   		sqlStr.append("SELECT CASE WHEN (codigobarra::text  ~E'^\\\\d+$') THEN codigobarra::NUMERIC(20,0) ELSE codigo END as codigo, CASE WHEN codigobarra::TEXT  ~E'^\\\\d+$' THEN 1 ELSE 2 END prioridade ");
   		sqlStr.append("FROM exemplar where length(codigobarra) <= ").append(tamanhoCodigo).append(" ) t ORDER BY prioridade desc, codigo DESC LIMIT 1;");
   		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
   	}
       
   	public boolean verificarExisteCodigoBarraExemplar(String codigoBarra) throws Exception {
   		StringBuilder sqlStr = new StringBuilder();
   		if (Uteis.getIsValorNumerico(codigoBarra)) {
   			sqlStr.append("SELECT exemplar.codigo FROM exemplar WHERE exemplar.codigoBarra::NUMERIC(20,0) = ").append(Long.parseLong(codigoBarra)).append(";");
   		} else {
   			sqlStr.append("SELECT exemplar.codigo FROM exemplar WHERE exemplar.codigoBarra = '").append(codigoBarra).append("';");
   		}
   		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
   	}
       
       public void validarDadosUnicidadeBibliotecaEdicaoVolumeNumero(List<ExemplarVO> objetos)  throws Exception {
    	   Map<String, ExemplarVO> mapEdicaoNumero = new HashMap<String, ExemplarVO>(0);
       	for(ExemplarVO exemplar: objetos){    		       		
       		if(!mapEdicaoNumero.containsKey(exemplar.getCodigoHashComNumeroExemplar())){    			
       			mapEdicaoNumero.put(exemplar.getCodigoHashComNumeroExemplar(), exemplar);    			
       		}else{
       			throw new Exception("O exemplar de código de barra "+exemplar.getCodigoBarra()+" possui o mesmo número do exemplar de código de barra "+mapEdicaoNumero.get(exemplar.getCodigoHashComNumeroExemplar()).getCodigoBarra()+" para a mesma biblioteca, titulo, subtítulo, edição, ano de publicação e volume.");
       		}
       	}   
       }
       
       public int consultarNrExemplaresCatalogoGravadosDisponiveisParaEmprestimo(ExemplarVO exemplarVO, String situacaoAtual) throws Exception {
           String sqlStr = "SELECT COUNT (*) FROM exemplar WHERE catalogo = " + exemplarVO.getCatalogo().getCodigo() + " ";
           if (!situacaoAtual.equals("")) {
               sqlStr += "AND situacaoAtual = '" + situacaoAtual + "' ";
           }
           if (Uteis.isAtributoPreenchido(exemplarVO.getBiblioteca().getCodigo())) {
               sqlStr += "AND biblioteca = '" + exemplarVO.getBiblioteca().getCodigo() + "' ";
           }           
           if (exemplarVO.getEmprestarSomenteFinalDeSemana() && UteisData.isFinalDeSemanaComSextaFeira(new Date(), true)) {
           		sqlStr += " AND emprestarsomentefinaldesemana <> true ";
           }           
           SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
           if (!tabelaResultado.next()) {
               return 0;
           }
           return (tabelaResultado.getInt(1));
       }
       
       public List<ExemplarVO> consultarPorCatalogoBiblioteca(Integer codigoCatalogo, Integer biblioteca, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
           ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
           String sqlStr = "SELECT * FROM Exemplar WHERE catalogo = ? and biblioteca = ? ORDER BY biblioteca, numeroExemplar desc";
           SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoCatalogo.intValue(), biblioteca});
           return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
       } 
       public List<ExemplarVO> buscarExemplaresEdicao(Integer codigoCatalogo , String TipoColunaAlterarExemplar , String valorConsultaAlteracao , Date valorConsultaAlteracaoData){
    	   
    	   StringBuilder sb = new StringBuilder();
   		
   		sb.append("select exemplar.numeroexemplar,exemplar.codigo , exemplar.catalogo , exemplar.codigobarra , exemplar.dataaquisicao,exemplar.tituloexemplar ,exemplar.subtitulo ,exemplar.secao, secao.nome AS \"secao.nome\"  , exemplar.edicao ,exemplar.anopublicacao,exemplar.volume ,exemplar.local,exemplar.link  ,exemplar.fasciculos ,exemplar.paraconsulta,"); 
   		sb.append(" exemplar.emprestarsomentefinaldesemana,exemplar.desconsiderarreserva,exemplar.datacompra , exemplar.biblioteca , exemplar.tipoentrada ,exemplar.tipomidia , exemplar.abreviacaotitulo , exemplar.dataPublicacao , exemplar.mes ,  exemplar.nredicaoespecial    from exemplar" );
   		sb.append(" left join secao on exemplar.secao = secao.codigo");
   		sb.append(" inner join biblioteca on exemplar.biblioteca = biblioteca.codigo");
   		sb.append(" where exemplar.catalogo ='").append(codigoCatalogo).append("'");
   		if (TipoColunaAlterarExemplar.equals("datacompra")) {
   			sb.append(" and exemplar.tipoentrada = 'CO' ");	
		}
   		if (!valorConsultaAlteracao.equals("")) {
   			if ( TipoColunaAlterarExemplar.equals("tipoentrada")|| TipoColunaAlterarExemplar.equals("desconsiderarreserva") || TipoColunaAlterarExemplar.equals("paraconsulta") || TipoColunaAlterarExemplar.equals("emprestarsomentefinaldesemana") || (TipoColunaAlterarExemplar.equals("secao") && !valorConsultaAlteracao.equals("0"))) {
   		   		sb.append(" and exemplar.").append(TipoColunaAlterarExemplar).append(" = '").append(valorConsultaAlteracao).append("'");	
   			}
   			else {
   			   sb.append(" and exemplar.").append(TipoColunaAlterarExemplar).append(" ilike '%").append(valorConsultaAlteracao).append("%'");	
   			}
   		}
   		if (valorConsultaAlteracaoData != null) {
				sb.append(" and exemplar.").append(TipoColunaAlterarExemplar).append(" = '").append(valorConsultaAlteracaoData).append("'");	
			}
   		
   		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
   		List<ExemplarVO> listaExemplarVOs = new ArrayList<ExemplarVO>(0);
   		while (dadosSQL.next()) {
   			ExemplarVO obj = new ExemplarVO();
   			obj.setNumeroExemplar(dadosSQL.getInt("numeroExemplar"));
   			obj.setCodigo(dadosSQL.getInt("codigo"));
   			obj.getCatalogo().setCodigo(dadosSQL.getInt("catalogo"));
   			obj.setCodigoBarra(dadosSQL.getString("codigoBarra"));
   			obj.setDataAquisicao(dadosSQL.getDate("dataAquisicao"));
   			obj.setTituloExemplar(dadosSQL.getString("tituloExemplar"));
   			obj.setSubtitulo(dadosSQL.getString("subtitulo"));   			
   			obj.getSecao().setCodigo(dadosSQL.getInt("secao"));
   			obj.getSecao().setNome(dadosSQL.getString("secao.nome"));
   			obj.setEdicao(dadosSQL.getString("edicao"));
   			obj.setAnoPublicacao(dadosSQL.getString("anoPublicacao"));
   			obj.setVolume(dadosSQL.getString("volume"));
   			obj.setLocal(dadosSQL.getString("local"));
   			obj.setLink(dadosSQL.getString("link"));
   			obj.setFasciculos(dadosSQL.getString("fasciculos"));   		
			obj.setParaConsulta(dadosSQL.getBoolean("paraConsulta"));
			obj.setEmprestarSomenteFinalDeSemana(dadosSQL.getBoolean("emprestarSomenteFinalDeSemana"));
			obj.setDesconsiderarReserva(dadosSQL.getBoolean("desconsiderarReserva"));
			obj.setDataCompra(dadosSQL.getDate("dataCompra"));
			obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
			obj.setTipoEntrada(dadosSQL.getString("tipoEntrada"));
   			obj.setTipoMidia(dadosSQL.getString("tipoMidia"));
   			obj.setAbreviacaoTitulo(dadosSQL.getString("abreviacaotitulo"));
   			obj.setDataPublicacao(dadosSQL.getDate("dataPublicacao"));
   			obj.setMes(dadosSQL.getString("mes"));
   			obj.setNrEdicaoEspecial(dadosSQL.getString("nredicaoespecial"));
   		   
   			
   			listaExemplarVOs.add(obj);
   		}
   		return listaExemplarVOs;    	   
    	   
       }
    @Override
   	public void marcarTodosListaExemplares(List<ExemplarVO> exemplarVO) {
   		for (ExemplarVO obj : exemplarVO) {   			
   				obj.setExemplarSelecionado(true);   			
   		}
   	}

   	@Override
   	public void desmarcarTodosListaExemplares(List<ExemplarVO> exemplarVO) {
   		for (ExemplarVO obj : exemplarVO) {
   			obj.setExemplarSelecionado(false);
   		}
   	}
   	
   	public void editarDadosExemplares(List<ExemplarVO> exemplarVOs, UsuarioVO usuario , String valorAlteracao , Boolean valorAlteracaoBoolean,Date valorAlteracaoData,String tipoAlterarColunaExemplar , Boolean assinaturaPeriodico) throws Exception{
   		List<ExemplarVO> listaExemplarSelecionadoVOs = exemplarVOs.stream().filter(ExemplarVO::getExemplarSelecionado).collect(Collectors.toList());
   		
   		for (ExemplarVO obj : listaExemplarSelecionadoVOs) {
   			
   			if (valorAlteracao.equals("") && valorAlteracaoBoolean.equals(false) && valorAlteracaoData == null) {
   				alterar(obj, assinaturaPeriodico, usuario);
			}else {   			
	   			if (tipoAlterarColunaExemplar.equals("anopublicacao")) {
	   				obj.setAnoPublicacao(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("tituloexemplar")) {
	   				obj.setTituloExemplar(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("subtitulo")) {
	   				obj.setSubtitulo(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("edicao")) {
	   				obj.setEdicao(valorAlteracao);	
				}
	   			else if (tipoAlterarColunaExemplar.equals("volume")) {
	   				obj.setVolume(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("local")) {
	   				obj.setLocal(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("link")) {
	   				obj.setLink(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("fasciculo")) {
	   				obj.setFasciculos(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("tipoentrada")) {
	   				obj.setTipoEntrada(valorAlteracao);	   				
				}
	   			else if (tipoAlterarColunaExemplar.equals("dataaquisicao")) {
	   				obj.setDataAquisicao(valorAlteracaoData);	   				
				}
	   			else if (tipoAlterarColunaExemplar.equals("datacompra")) {
	   				obj.setDataCompra(valorAlteracaoData);
				}
	   			else if (tipoAlterarColunaExemplar.equals("paraconsulta")) {
	   				obj.setParaConsulta(valorAlteracaoBoolean);
				}
	   			else if (tipoAlterarColunaExemplar.equals("desconsiderarreserva")) {
	   				obj.setDesconsiderarReserva(valorAlteracaoBoolean);
				}
	   			else if (tipoAlterarColunaExemplar.equals("emprestarsomentefinaldesemana")) {
	   				obj.setEmprestarSomenteFinalDeSemana(valorAlteracaoBoolean);
				}
	   			else if (tipoAlterarColunaExemplar.equals("secao")) {
	   				obj.getSecao().setCodigo(Integer.valueOf(valorAlteracao));
				}
	   			else if (tipoAlterarColunaExemplar.equals("abreviacaotitulo")) {
	   				obj.setAbreviacaoTitulo(valorAlteracao);
				}
	   			else if (tipoAlterarColunaExemplar.equals("dataPublicacao")) {
	   				obj.setDataPublicacao(valorAlteracaoData);
				}
	   			else if (tipoAlterarColunaExemplar.equals("mes")) {
	   				obj.setMes(valorAlteracao);
				}	   			
	   			else if (tipoAlterarColunaExemplar.equals("nredicaoespecial")) {
	   				obj.setNrEdicaoEspecial(valorAlteracao);
				}
	   			alterar(obj, assinaturaPeriodico, usuario);
			}
		}
   	}
       
	public void realizarValidacaoDisponibilizarExemplarConsulta(ExemplarVO exemplarVO) throws Exception {
		if (Uteis.isAtributoPreenchido(exemplarVO)) {
			if (!exemplarVO.getParaConsulta() && exemplarVO.getSituacaoAtual().equals(SituacaoExemplar.CONSULTA.getValor())) {
				exemplarVO.setSituacaoAtual(SituacaoExemplar.DISPONIVEL.getValor());
			}
			if (exemplarVO.getParaConsulta() && exemplarVO.getSituacaoAtual().equals(SituacaoExemplar.DISPONIVEL.getValor())) {
				exemplarVO.setSituacaoAtual(SituacaoExemplar.CONSULTA.getValor());
			}
		}
	}
}