package negocio.comuns.arquitetura;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoSubModuloEnum;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade PerfilAcesso. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "perfilAcesso")
public class PerfilAcessoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private Integer responsavel;
	/**
	 * Atributo responsável por manter os objetos da classe <code>Permissao</code>.
	 */
	private List<PermissaoVO> permissaoVOs;
	public static final long serialVersionUID = 1L;
	private String nomeModulo;
	private String modulo;
	private Boolean moduloPossuiPermissoes;
	private TipoVisaoEnum ambiente;
	private Boolean possuiAcessoAdministrativo;
	private Boolean possuiAcessoAcademico;
	private Boolean possuiAcessoFinanceiro;
	private Boolean possuiAcessoCompras;
	private Boolean possuiAcessoSeiDecidir;
	private Boolean possuiAcessoProcessoSeletivo;
	private Boolean possuiAcessoEAD;
	private Boolean possuiAcessoBiblioteca;
	private Boolean possuiAcessoCRM;
	private Boolean possuiAcessoAvaliacaoInsitucional;
	private Boolean possuiAcessoBancoDeCurriculos;
	private Boolean possuiAcessoPlanoOrcamentario;
	private Boolean possuiAcessoNotaFiscal;
	private Boolean possuiAcessoEstagio;
	private Boolean possuiAcessoPatrimonio;
	private Boolean possuiAcessoRh;
    private Boolean possuiAcessoContabil;
    
    //Transiente
    private Boolean filtrarPerfilAcesso;

	/**
	 * Construtor padrão da classe <code>PerfilAcesso</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public PerfilAcessoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>PerfilAcessoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(PerfilAcessoVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Perfil Acesso) deve ser informado.");
		}
		obj.setPossuiAcessoAcademico(false);
		obj.setPossuiAcessoAdministrativo(false);
		obj.setPossuiAcessoAvaliacaoInsitucional(false);
		obj.setPossuiAcessoBancoDeCurriculos(false);
		obj.setPossuiAcessoBiblioteca(false);
		obj.setPossuiAcessoCompras(false);
		obj.setPossuiAcessoCRM(false);
		obj.setPossuiAcessoEAD(false);
		obj.setPossuiAcessoNotaFiscal(false);
		obj.setPossuiAcessoFinanceiro(false);
		obj.setPossuiAcessoPlanoOrcamentario(false);
		obj.setPossuiAcessoProcessoSeletivo(false);
		obj.setPossuiAcessoSeiDecidir(false);
		obj.setPossuiAcessoRh(false);
        obj.setPossuiAcessoEstagio(false);
        obj.setPossuiAcessoPatrimonio(false);
        obj.setPossuiAcessoFinanceiro(false);
        obj.setPossuiAcessoContabil(false);
        obj.setPossuiAcessoNotaFiscal(false);
		if (obj.getAmbiente().equals(TipoVisaoEnum.ADMINISTRATIVA)) {
			for (PermissaoVO permissaoVO : obj.getPermissaoVOs()) {
				if (!permissaoVO.getPermissoes().trim().isEmpty()) {
					
					switch (((PerfilAcessoPermissaoEnumInterface) permissaoVO.getPermissao()).getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum()) {
					case ACADEMICO:
						obj.setPossuiAcessoAcademico(true);
        				obj.setPossuiAcessoContabil(true);
						break;
					case ADMINISTRATIVO:
						obj.setPossuiAcessoAdministrativo(true);
						break;
					case AVALIACAO_INSTITUCIONAL:
						obj.setPossuiAcessoAvaliacaoInsitucional(true);
						break;
//					case BANCO_DE_CURRICULOS:
//						obj.setPossuiAcessoBancoDeCurriculos(true);
//						break;
					case BIBLIOTECA:
						obj.setPossuiAcessoBiblioteca(true);
						break;
					case COMPRAS:
						obj.setPossuiAcessoCompras(true);
						break;
					case CRM:
						obj.setPossuiAcessoCRM(true);
						break;
					case EAD:
						obj.setPossuiAcessoEAD(true);
						break;
					case FINANCEIRO:
						obj.setPossuiAcessoFinanceiro(true);
						break;
					case PLANO_ORCAMENTARIO:
						obj.setPossuiAcessoPlanoOrcamentario(true);
						break;
					case PROCESSO_SELETIVO:
						obj.setPossuiAcessoProcessoSeletivo(true);
						break;
					case SEI_DECIDIR:
						obj.setPossuiAcessoSeiDecidir(true);
						break;
					case RH:
						obj.setPossuiAcessoRh(true);
						break;
        			case PATRIMONIO:
        				obj.setPossuiAcessoPatrimonio(true);
        				break;
        			case NOTA_FISCAL:
        				obj.setPossuiAcessoNotaFiscal(true);
        				break;
        			case ESTAGIO:
        				obj.setPossuiAcessoEstagio(true);
        				break;
        			case CONTABIL:
        				obj.setPossuiAcessoContabil(true);
        				break;
					default:
						break;
					}
					}
				
			}
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
	}

	public void montarPermissoes(List<PermissaoVO> permissaoVOs) {
		Iterator<PermissaoVO> i = permissaoVOs.iterator();
		while (i.hasNext()) {
			PermissaoVO p = (PermissaoVO) i.next();
			try {
				this.adicionarObjPermissaoVOs(p);
			} catch (Exception e) {

			}

		}
	}

	public PermissaoAcessoMenuVO montarPermissoesMenu() {

		PermissaoAcessoMenuVO permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
		Iterator<PermissaoVO> j = getPermissaoVOs().iterator();

		while (j.hasNext()) {
			try {
				PermissaoVO obj = (PermissaoVO) j.next();

				Class<?> classeGenerica = Class.forName(obj.getClass().getName());
				Method metodoGet = classeGenerica.getMethod("getNomeEntidade");
				metodoGet.invoke(obj).toString();

				Class<?> permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());
				Method metodoSet = permissaoMenu.getMethod("set" + metodoGet.invoke(obj).toString(), Boolean.class);
				metodoSet.invoke(permissaoAcessoMenuVO, Boolean.TRUE);

			} catch (Exception e) {
				//// System.out.println("Erro:" + e.getMessage()); foi verificado que tem a
				//// posibilidade de nao encontrar uma permissao especifica pois foi alterada no
				//// codigo e nao deletou o codigo na base
			}
		}
		return permissaoAcessoMenuVO;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>PermissaoVO</code> ao List <code>permissaoVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>Permissao</code> -
	 * getNomeEntidade() - como identificador (key) do objeto no List.
	 *
	 * @param obj
	 *            Objeto da classe <code>PermissaoVO</code> que será adiocionado ao
	 *            Hashtable correspondente.
	 */
	public void adicionarObjPermissaoVOs(PermissaoVO obj) throws Exception {
		PermissaoVO.validarDados(obj);
		int index = 0;
		Iterator<PermissaoVO> i = getPermissaoVOs().iterator();
		while (i.hasNext()) {
			PermissaoVO objExistente = (PermissaoVO) i.next();
			if (((PerfilAcessoPermissaoEnumInterface) obj.getPermissao()).getTipoPerfilAcesso().getIsFuncionalidade()
					&& ((PerfilAcessoPermissaoEnumInterface) obj.getPermissao()).getPermissaoSuperiorEnum()
							.equals(objExistente.getPermissao())) {
				int index2 = 0;
				for (PermissaoVO func : objExistente.getFuncionalidades()) {
					if (func.getPermissao().equals(obj.getPermissao())) {
						objExistente.getFuncionalidades().set(index2, obj);
						return;
					}
					index2++;
				}
			} else if (!((PerfilAcessoPermissaoEnumInterface) obj.getPermissao()).getTipoPerfilAcesso()
					.getIsFuncionalidade()
					&& ((PerfilAcessoPermissaoEnumInterface) objExistente.getPermissao()).equals(obj.getPermissao())) {
				getPermissaoVOs().set(index, obj);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>PermissaoVO</code>
	 * no List <code>permissaoVOs</code>. Utiliza o atributo padrão de consulta da
	 * classe <code>Permissao</code> - getNomeEntidade() - como identificador (key)
	 * do objeto no List.
	 *
	 * @param nomeEntidade
	 *            Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjPermissaoVOs(String nomeEntidade) throws Exception {
		int index = 0;
		Iterator<PermissaoVO> i = getPermissaoVOs().iterator();
		while (i.hasNext()) {
			PermissaoVO objExistente = (PermissaoVO) i.next();
			if (objExistente.getNomeEntidade().equals(nomeEntidade)) {
				getPermissaoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>PermissaoVO</code> no List <code>permissaoVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>Permissao</code> -
	 * getNomeEntidade() - como identificador (key) do objeto no List.
	 *
	 * @param nomeEntidade
	 *            Parâmetro para localizar o objeto do List.
	 */
	public PermissaoVO consultarObjPermissaoVO(String nomeEntidade) throws Exception {
		Iterator<PermissaoVO> i = getPermissaoVOs().iterator();
		while (i.hasNext()) {
			PermissaoVO objExistente = (PermissaoVO) i.next();
			if (objExistente.getNomeEntidade().equals(nomeEntidade)) {
				return objExistente;
			}
		}
		return null;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>Permissao</code>.
	 */
	public List<PermissaoVO> getPermissaoVOs() {
		if (permissaoVOs == null) {
			permissaoVOs = new ArrayList<PermissaoVO>(0);
		}
		return (permissaoVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>Permissao</code>.
	 */
	public void setPermissaoVOs(List<PermissaoVO> permissaoVOs) {
		this.permissaoVOs = permissaoVOs;
	}

	public String getNome() {
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(String nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public Boolean getModuloPossuiPermissoes() {
		return moduloPossuiPermissoes;
	}

	public void setModuloPossuiPermissoes(Boolean moduloPossuiPermissoes) {
		this.moduloPossuiPermissoes = moduloPossuiPermissoes;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	/**
	 * @return the responsavel
	 */
	public Integer getResponsavel() {
		return responsavel;
	}

	/**
	 * @param responsavel
	 *            the responsavel to set
	 */
	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}

	public TipoVisaoEnum getAmbiente() {
		if (ambiente == null) {
			ambiente = TipoVisaoEnum.ADMINISTRATIVA;
		}
		return ambiente;
	}

	public void setAmbiente(TipoVisaoEnum ambiente) {
		this.ambiente = ambiente;
	}

	public Boolean getPossuiAcessoAdministrativo() {
		if (possuiAcessoAdministrativo == null) {
			possuiAcessoAdministrativo = true;
		}
		return possuiAcessoAdministrativo;
	}

	public void setPossuiAcessoAdministrativo(Boolean possuiAcessoAdministrativo) {
		this.possuiAcessoAdministrativo = possuiAcessoAdministrativo;
	}

	public Boolean getPossuiAcessoAcademico() {
		if (possuiAcessoAcademico == null) {
			possuiAcessoAcademico = true;
		}
		return possuiAcessoAcademico;
	}

	public void setPossuiAcessoAcademico(Boolean possuiAcessoAcademico) {
		this.possuiAcessoAcademico = possuiAcessoAcademico;
	}

	public Boolean getPossuiAcessoFinanceiro() {
		if (possuiAcessoFinanceiro == null) {
			possuiAcessoFinanceiro = true;
		}
		return possuiAcessoFinanceiro;
	}

	public void setPossuiAcessoFinanceiro(Boolean possuiAcessoFinanceiro) {
		this.possuiAcessoFinanceiro = possuiAcessoFinanceiro;
	}

	public Boolean getPossuiAcessoCompras() {
		if (possuiAcessoCompras == null) {
			possuiAcessoCompras = true;
		}
		return possuiAcessoCompras;
	}

	public void setPossuiAcessoCompras(Boolean possuiAcessoCompras) {
		this.possuiAcessoCompras = possuiAcessoCompras;
	}

	public Boolean getPossuiAcessoSeiDecidir() {
		if (possuiAcessoSeiDecidir == null) {
			possuiAcessoSeiDecidir = true;
		}
		return possuiAcessoSeiDecidir;
	}

	public void setPossuiAcessoSeiDecidir(Boolean possuiAcessoSeiDecidir) {
		this.possuiAcessoSeiDecidir = possuiAcessoSeiDecidir;
	}

	public Boolean getPossuiAcessoProcessoSeletivo() {
		if (possuiAcessoProcessoSeletivo == null) {
			possuiAcessoProcessoSeletivo = true;
		}
		return possuiAcessoProcessoSeletivo;
	}

	public void setPossuiAcessoProcessoSeletivo(Boolean possuiAcessoProcessoSeletivo) {
		this.possuiAcessoProcessoSeletivo = possuiAcessoProcessoSeletivo;
	}

	public Boolean getPossuiAcessoEAD() {
		if (possuiAcessoEAD == null) {
			possuiAcessoEAD = true;
		}
		return possuiAcessoEAD;
	}

	public void setPossuiAcessoEAD(Boolean possuiAcessoEAD) {
		this.possuiAcessoEAD = possuiAcessoEAD;
	}

	public Boolean getPossuiAcessoBiblioteca() {
		if (possuiAcessoBiblioteca == null) {
			possuiAcessoBiblioteca = true;
		}
		return possuiAcessoBiblioteca;
	}

	public void setPossuiAcessoBiblioteca(Boolean possuiAcessoBiblioteca) {
		this.possuiAcessoBiblioteca = possuiAcessoBiblioteca;
	}

	public Boolean getPossuiAcessoCRM() {
		if (possuiAcessoCRM == null) {
			possuiAcessoCRM = true;
		}
		return possuiAcessoCRM;
	}

	public void setPossuiAcessoCRM(Boolean possuiAcessoCRM) {
		this.possuiAcessoCRM = possuiAcessoCRM;
	}

	public Boolean getPossuiAcessoAvaliacaoInsitucional() {
		if (possuiAcessoAvaliacaoInsitucional == null) {
			possuiAcessoAvaliacaoInsitucional = true;
		}
		return possuiAcessoAvaliacaoInsitucional;
	}

	public void setPossuiAcessoAvaliacaoInsitucional(Boolean possuiAcessoAvaliacaoInsitucional) {
		this.possuiAcessoAvaliacaoInsitucional = possuiAcessoAvaliacaoInsitucional;
	}

	public Boolean getPossuiAcessoBancoDeCurriculos() {
		if (possuiAcessoBancoDeCurriculos == null) {
			possuiAcessoBancoDeCurriculos = true;
		}
		return possuiAcessoBancoDeCurriculos;
	}

	public void setPossuiAcessoBancoDeCurriculos(Boolean possuiAcessoBancoDeCurriculos) {
		this.possuiAcessoBancoDeCurriculos = possuiAcessoBancoDeCurriculos;
	}

	public Boolean getPossuiAcessoPlanoOrcamentario() {
		if (possuiAcessoPlanoOrcamentario == null) {
			possuiAcessoPlanoOrcamentario = true;
		}
		return possuiAcessoPlanoOrcamentario;
	}

	public void setPossuiAcessoPlanoOrcamentario(Boolean possuiAcessoPlanoOrcamentario) {
		this.possuiAcessoPlanoOrcamentario = possuiAcessoPlanoOrcamentario;
	}

	public Boolean getPossuiAcessoNotaFiscal() {
		if (possuiAcessoNotaFiscal == null) {
			possuiAcessoNotaFiscal = true;
		}
		return possuiAcessoNotaFiscal;
	}

	public void setPossuiAcessoNotaFiscal(Boolean possuiAcessoNotaFiscal) {
		this.possuiAcessoNotaFiscal = possuiAcessoNotaFiscal;
	}

	public Boolean getPossuiAcessoEstagio() {
		if (possuiAcessoEstagio == null) {
			possuiAcessoEstagio = true;
		}
		return possuiAcessoEstagio;
	}

	public void setPossuiAcessoEstagio(Boolean possuiAcessoEstagio) {
		this.possuiAcessoEstagio = possuiAcessoEstagio;
	}

	public Boolean getPossuiAcessoPatrimonio() {
		if (possuiAcessoPatrimonio == null) {
			possuiAcessoPatrimonio = true;
		}
		return possuiAcessoPatrimonio;
	}

	public void setPossuiAcessoPatrimonio(Boolean possuiAcessoPatrimonio) {
		this.possuiAcessoPatrimonio = possuiAcessoPatrimonio;
	}

	public Boolean getPossuiAcessoRh() {
		if (possuiAcessoRh == null)
			possuiAcessoRh = true;
		return possuiAcessoRh;
	}

	public void setPossuiAcessoRh(Boolean possuiAcessoRh) {
		this.possuiAcessoRh = possuiAcessoRh;
	}

	public Boolean getPossuiAcessoContabil() {
		if(possuiAcessoContabil == null) {
			possuiAcessoContabil = false;
		}
		return possuiAcessoContabil;
	}

	public void setPossuiAcessoContabil(Boolean possuiAcessoContabil) {
		this.possuiAcessoContabil = possuiAcessoContabil;
	}

	public Boolean getFiltrarPerfilAcesso() {
		if (filtrarPerfilAcesso == null) {
			filtrarPerfilAcesso = Boolean.FALSE;
		}
		return filtrarPerfilAcesso;
	}

	public void setFiltrarPerfilAcesso(Boolean filtrarPerfilAcesso) {
		this.filtrarPerfilAcesso = filtrarPerfilAcesso;
	}
}
