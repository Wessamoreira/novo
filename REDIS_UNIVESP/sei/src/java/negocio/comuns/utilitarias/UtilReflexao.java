package negocio.comuns.utilitarias;

/**
 *
 * @author Diego
 */
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.rowset.serial.SerialArray;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.arquitetura.AtributoComparacao;

public class UtilReflexao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7761416623211774005L;

	public static String getNomeDoMetodoGet(String nomeDoAtributo) {
		return "get" + nomeDoAtributo.substring(0, 1).toUpperCase() + nomeDoAtributo.substring(1);
	}
	
	public static String getNomeDoMetodoIs(String nomeDoAtributo) {
		return "is" + nomeDoAtributo.substring(0, 1).toUpperCase() + nomeDoAtributo.substring(1);
	}

	public static String getNomeDoMetodoSet(String nomeDoAtributo) {
		return "set" + nomeDoAtributo.substring(0, 1).toUpperCase() + nomeDoAtributo.substring(1);
	}

	public static Object invocarMetodoGet(Object objetoDoMetodo, String nomeDaPropriedade) {
		Method metodo = null;
		try {
			metodo = objetoDoMetodo.getClass().getMethod(getNomeDoMetodoGet(nomeDaPropriedade), (Class[]) null);
			return metodo.invoke(objetoDoMetodo, (Object[]) null);
		} catch (Throwable e) {			
			return null;
		}finally {
			metodo = null;
		}
	}
	
	public static Object invocarMetodo(Object objetoDoMetodo, String nomeDaPropriedade) throws Exception {
		Method metodo = null;
		try {
			metodo = objetoDoMetodo.getClass().getMethod(getNomeDoMetodoUsar(objetoDoMetodo, nomeDaPropriedade), (Class[]) null);
			return metodo.invoke(objetoDoMetodo, (Object[]) null);
		} catch (Exception e) {			
			throw e;
		} finally {
			metodo = null;
		}
	}
	
	public static String getNomeDoMetodoUsar(Object objetoDoMetodo, String nomeDaPropriedade) throws Exception {
		try {
			List<Method> methods = new ArrayList<>(Arrays.asList(objetoDoMetodo.getClass().getMethods()));
			if (Uteis.isAtributoPreenchido(methods)) {
				for (Method method : methods) {
					if (method.getName().equals(getNomeDoMetodoGet(nomeDaPropriedade))) {
						return getNomeDoMetodoGet(nomeDaPropriedade);
					} else if (method.getName().equals(getNomeDoMetodoIs(nomeDaPropriedade))) {
						return getNomeDoMetodoIs(nomeDaPropriedade);
					} else if (method.getName().equals(nomeDaPropriedade)) {
						return nomeDaPropriedade;
					}
				}
				throw new Exception("Não foi encontrado o metódo no Objeto especificado (" + nomeDaPropriedade + ")");
			} else {
				throw new Exception("Não foi encontrado metódos no Objeto especificado, objeto: " + objetoDoMetodo.getClass().getName() +", propriedade: " + nomeDaPropriedade + "");
			}
		} catch (Exception e) {			
			throw e;
		}
	}

	public static Object invocarMetodoSet1Parametro(Object objetoDoMetodo, String nomeDaPropriedade, Class classeParametro1, Object valorParametro1) {
		Class[] classesParametro = null;
		Object[] valoresParametro = null;
		Method metodo = null;
		try {
			classesParametro = new Class[1];
			classesParametro[0] = classeParametro1;

			valoresParametro = new Object[1];
			valoresParametro[0] = valorParametro1;

			metodo = objetoDoMetodo.getClass().getMethod(getNomeDoMetodoSet(nomeDaPropriedade), classesParametro);

			return metodo.invoke(objetoDoMetodo, valoresParametro);
		} catch (Throwable e) {
			return null;
		}finally {
			classesParametro = null;
			valoresParametro = null;			
			metodo = null;
		}
	}

	public static Object invocarMetodo(Object objetoDoMetodo, String nomeDoMetodo, Object... parametro) {
		Method metodo = null;
		try {
			metodo = objetoDoMetodo.getClass().getMethod(nomeDoMetodo, getClassesDosParametros(parametro));
			return metodo.invoke(objetoDoMetodo, parametro);
		} catch (Exception e) {
			return null;
		}finally {
			metodo = null;
		}
	}

	public static Class[] getClassesDosParametros(Object... parametrosObj) {
		Class[] parametros = null;
		if (parametrosObj != null) {
			parametros = new Class[parametrosObj.length];
			for (int i = 0; i < parametrosObj.length; i++) {
				parametros[i] = parametrosObj[i].getClass();
			}
		}
		return parametros;
	}

	public static void invocarMetodoSetParametroNull(Object objetoDoMetodo, String nomeDaPropriedade) {
		try {
			getMethodParaParametroNull(objetoDoMetodo, getNomeDoMetodoSet(nomeDaPropriedade)).invoke(objetoDoMetodo, new Object[] { null });
		} catch (Exception e) {
			// //System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	private static Method getMethodParaParametroNull(Object objetoDoMetodo, String nomeDoMetodo) {
		for (Method method : objetoDoMetodo.getClass().getMethods()) {
			if (method.getName().equals(nomeDoMetodo)) {
				return method;
			}
		}
		return null;
	}

	/**
	 * Método recebe a posição da classe na pilha de chamadas, e retorna o nome
	 * dela. Por exemplo, se a Classe A e uma Classe B usam um método da Classe
	 * C, e em tempo de execução precisamos saber qual das duas classes, A ou B,
	 * chamou o método de C, usamos esse método passando como parâmetro 1, já a
	 * pilha de chamadas vai estar da seguinte maneira: [0] - Classe C [1] -
	 * Classe A ou B [2] - Classe que chamou A ou B [3] - Etc...
	 * 
	 * @author Murillo Parreira
	 * @param posicaoNaPilhaDeExecucao
	 * @return nomeDaClasseQueChamouOMetodo
	 */
	public static String getNomedaClasseQueChamouOMetodo(Integer posicaoNaPilhaDeExecucao) {
		Throwable throwable = new Throwable();
		try {
			String nomeDaClasseQueChamouOMetodo = throwable.fillInStackTrace().getStackTrace()[posicaoNaPilhaDeExecucao].getClassName();
			nomeDaClasseQueChamouOMetodo = nomeDaClasseQueChamouOMetodo.substring(nomeDaClasseQueChamouOMetodo.lastIndexOf(".") + 1);
			return nomeDaClasseQueChamouOMetodo;
		} finally {
			throwable = null;
		}
	}

	public static String getObterCodigoParaDaListaParaConsulta(List<Integer> listaCodigos) {
		StringBuilder sb = new StringBuilder();
		for (Integer codigo : listaCodigos) {
			sb.append(codigo).append(", ");
		}
		return sb.toString().substring(0, sb.toString().length() - 2);
	}

	
	@SuppressWarnings("unchecked")
	public static <T extends SuperVO> List<T> convertePostgresArrayParaListaEntidade(T entidade, SerialArray serialArray) throws Exception {
		List<T> lista = new ArrayList<>();
		if(serialArray != null && serialArray.getArray() != null) {
			Object[] arrayInteiro = (Object[]) serialArray.getArray();
			if(arrayInteiro != null && arrayInteiro.length > 0) {
				for (int i = 0; i < arrayInteiro.length; i++) {
					T obj = (T) entidade.getClass().newInstance();
					UtilReflexao.invocarMetodo(obj, "setCodigo", arrayInteiro[i]);
					lista.add(obj);
				}	
			}
		}
		return lista;
	}
	
	public static Integer[]  converteListaEntidadeParaArrayInteiro(List<? extends SuperVO> lista) {
		List<Integer> listaCodigos = new ArrayList<>();
		for (Object obj : lista) {
			Integer codigo = null;
			if (obj instanceof UnidadeEnsinoVO) {
				codigo = ((UnidadeEnsinoVO) obj).getCodigo();
			} else {
				codigo = (Integer) invocarMetodoGet(obj, "codigo");
			}
			if (Uteis.isAtributoPreenchido(codigo)) {
				listaCodigos.add(codigo);
			}
		}
		return listaCodigos.isEmpty() ? null : listaCodigos.stream().toArray(Integer[]::new);
	}
	
	public static List<Integer> converteListaEntidadeParaListaInteiro(List<? extends SuperVO> lista) {
		List<Integer> listaCodigos = new ArrayList<>();
		for (Object obj : lista) {
			Integer codigo = null;
			if (obj instanceof UnidadeEnsinoVO) {
				codigo = ((UnidadeEnsinoVO) obj).getCodigo();
			} else {
				codigo = (Integer) invocarMetodoGet(obj, "codigo");
			}
			if (Uteis.isAtributoPreenchido(codigo)) {
				listaCodigos.add(codigo);
			}
		}
		return listaCodigos;
	}	
	

	public static Object realizarValidacaoCampoReflection(Object obj, String propriedade)  {
		Field field = null;
		Object instancia = null;
		try {
			if (propriedade.contains(".")) {
				field = realizarValidacaoFieldExistente(obj, propriedade.substring(0, propriedade.indexOf(".")));
				field.setAccessible(true);
				instancia = field.get(obj);
				if (instancia == null) {
					return null;
				}
				return realizarValidacaoCampoReflection(instancia, propriedade.substring(propriedade.indexOf(".") + 1, propriedade.length()));
			} else {
				field = realizarValidacaoFieldExistente(obj, propriedade);
				field.setAccessible(true);
				return field.get(obj);
			}	
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}finally {
			field = null;
			instancia = null;
		}
		

	}	

	private static Field realizarValidacaoFieldExistente(Object obj, String propriedade) throws NoSuchFieldException,  InstantiationException, IllegalAccessException {
		try {
			return obj.getClass().getDeclaredField(propriedade);
		} catch (NoSuchFieldException e) {
			// Pego o atribubo que e da classe extendida ou classe mae
			try {
				return obj.getClass().getSuperclass().getDeclaredField(propriedade);
			} catch (NoSuchFieldException ex) {
				Object instancia = obj.getClass().getSuperclass().newInstance();
				if (instancia.getClass().getSimpleName().contains("VO")) {
					return realizarValidacaoFieldExistente(instancia, propriedade);
				}
				throw new NoSuchFieldException("Não foi encontrado o field " + propriedade);
			}
		}

	}
	
	public static void removerCamposChamadaAPI(Object obj, String... camposManter) {
		for(Field f: obj.getClass().getDeclaredFields()) {
			if(!Arrays.asList(camposManter).stream().anyMatch(a -> a.toLowerCase().equals(f.getName().toLowerCase())) && !f.getType().isPrimitive()){
				invocarMetodoSetParametroNull(obj, f.getName());				
			}
		}
	}
	
	private static class Condition {
		public Integer qtdCorreto;
		public Integer qtdValidados;

		public Condition(Integer qtdCorreto, Integer qtdValidados) {
			this.qtdCorreto = qtdCorreto;
			this.qtdValidados = qtdValidados;
		}
	}

	/**
	 * @serial Método usado para substituir OU editar um objeto dentro de uma lista dada as condições
	 * Método usado para substituir OU editar um objeto dentro de uma lista dada as condições
	 * estejam atendidas
	 * 
	 * @param lista                  LISTA QUE SERÁ ATUALIZADA COM OS NOVOS DADOS
	 * @param objeto                 OBJETO QUE SUBSTITUIRA UM ITEM DA LISTA
	 * @param condicoes              CONDIÇÕES ADEQUADAS NA QUAL O OBJETO DA LISTA DEVE ATENDER PARA SER SUBSTITUIDO
	 * @param adicionarObjetoNaLista SE ESTIVER TRUE, CASO NÃO SEJA SUBSTITUIDO NENHUM VALOR VAI ADICIONAR O objeto NA LISTA
	 * @author Felipi
	 * @throws Exception
	 */
	public static <T> void adicionarObjetoLista(List<T> lista, T objeto, AtributoComparacao atributoComparacao) throws Exception {
		if (lista != null && objeto != null) {
			if (Uteis.isAtributoPreenchido(lista) && Uteis.isAtributoPreenchido(atributoComparacao)) {
				for (int i = 0; i < lista.size(); i++) {
					T object = lista.get(i);
					Condition condition = new Condition(atributoComparacao.size(), 0);
					for (Entry<String, Object> entry : atributoComparacao.entrySet()) {
						if (entry.getKey() != null && entry.getValue() != null) {
							if (entry.getKey().contains(".")) {
								List<String> list = new ArrayList<>(Arrays.asList(entry.getKey().split("\\.")));
								realizarValidacaoObjetoHierarquio(list, object, entry.getValue(), condition, 0);
							} else {
								realizarComparacaoObjeto(object, entry.getKey(), entry.getValue(), condition);
							}
						}
					}
					if (condition.qtdCorreto.equals(condition.qtdValidados)) {
						lista.set(i, objeto);
						return;
					}
				}
			}
			lista.add(objeto);
		}
	}
	
	/**
	 * @serial Método usado para realizar a comparação do valor informado no map de
	 * condições com o valor do objeto dentro da lista
	 * 
	 * @param <T>
	 * @param objetoSendoValidado OBJETO ATUAL DA LISTA QUE ESTÁ SENDO PERCORRIDA
	 * @param propriedade         NOME DA PROPRIEDADE QUE SERÁ COMPARADA
	 * @param valor               VALOR DO OBJETO COMPARADO, OS VALORES DOS OBJETOS DENTRO DA LISTA DEVE SER IGUAL A ESSE VALOR
	 * @param condition           CONDITION USUADO PARA CONCATENAR A qtdvalidados
	 * @throws Exception
	 */
	private static <T> void realizarComparacaoObjeto(T objetoSendoValidado, String propriedade, Object valor, Condition condition) throws Exception {
		Object get = invocarMetodo(objetoSendoValidado, propriedade);
		if (get != null && get.equals(valor)) {
			condition.qtdValidados++;
		}
	}
	
	/**
	 * @serial Método usado para verificar se o objeto atual está sendo percorrido em uma
	 * lista e o último objeto dentro da lista
	 * 
	 * @param <T>
	 * @param lista LISTA PREENCHIDA
	 * @param index INDEX DO REGISTRO SENDO PERCORRIDO NO MOMENTO
	 * @return
	 */
	public static <T> boolean getUltimoObjetoLista(List<T> lista, int index) {
		return Integer.valueOf(lista.size() - 1).equals(index);
	}
	
	/**
	 * @serial Método usado para validar se a condição validada no momento e para realizar a
	 * comparação com o valor (realizarComparacaoObjeto) ou para pegar um valor
	 * de um objeto dentro de um outro Objeto
	 * 
	 * @param <T>
	 * @param listaCondicaoHierarquia	LISTA USADA PARA SABER QUAL A PROPRIEDADE QUE DEVE SER VALIDADA NO MOMENTO
	 * @param objetoSendoValidado		OBJETO QUE SERÁ VALIDADO
	 * @param valor						VALOR DO OBJETO COMPARADO, OS VALORES DOS OBJETOS DENTRO DA LISTA DEVE SER IGUAL A ESSE VALOR 
	 * @param condition					CONDITION USUADO PARA CONCATENAR A qtdvalidados
	 * @param indexValidar				INDEX DA PROPRIEDADE PRESENTE NA LISTA QUE SERÁ VALIDADO 
	 * @throws Exception
	 */
	private static <T> void realizarValidacaoObjetoHierarquio(List<String> listaCondicaoHierarquia, T objetoSendoValidado, Object valor, Condition condition, int indexValidar) throws Exception {
		String propriedade = listaCondicaoHierarquia.get(indexValidar);
		if (!getUltimoObjetoLista(listaCondicaoHierarquia, indexValidar)) {
			Object get = invocarMetodo(objetoSendoValidado, propriedade);
			realizarValidacaoObjetoHierarquio(listaCondicaoHierarquia, get, valor, condition, indexValidar+1);
		} else {
			realizarComparacaoObjeto(objetoSendoValidado, propriedade, valor, condition);
		}
	}
}