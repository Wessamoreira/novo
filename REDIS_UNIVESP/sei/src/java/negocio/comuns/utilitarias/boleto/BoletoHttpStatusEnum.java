package negocio.comuns.utilitarias.boleto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import negocio.comuns.utilitarias.StreamSeiException;

import static java.util.stream.Collectors.toCollection;

public enum BoletoHttpStatusEnum {
	OK(200, false, "OK."),
	CREATED(201, false, "A requisição foi bem sucessida e um novo recurso foi criado."),
	ACCEPTED(202, false, "A requisição foi recebida."),
	BAD_REQUEST(400, true, "O servidor não entendeu a requisição pois a mesma está com uma sintaxe inválida."),
	UNAUTHORIZED(401, true, "Deve-se autenticar para obter a resposta requisitada."),
	PAYMENT_REQUIRED(402, true, ""),
	FORBIDDEN(403, true, "O cliente não possui direitos de acesso ao conteúdo, portanto o servidor está rejeitando dar a resposta."),
	NOT_FOUND(404, true, "O servidor não pode encontrar o recurso solicitado."),
	METHOD_NOT_ALLOWED(405, true, "O método de solicitação foi desativado e não pode ser usado."),
	NOT_ACCEPTABLE(406, true, ""),
	PROXY_AUTHENTICATION_REQUIRED(407, true, "Faz-se necessário que a autenticação seja feita por um proxy."),
	REQUEST_TIMEOUT(408, true, ""),
	CONFLICT(409, true, ""),
	GONE(410, true, "O conteúdo requisitado foi deletado do servidor."),
	LENGTH_REQUIRED(411, true, ""),
	PRECONDITION_FAILED(412, true, ""),
	PAYLOAD_TOO_LARGE(413, true, ""),
	URI_TOO_LONG(414, true, ""),
	UNSUPPORTED_MEDIA_TYPE(415, true, ""),
	REQUEST_RANGE_NOT_SATISFIABLE(416, true, ""),
	EXPECTATION_FAILED(417, true, ""),
	MISDIRECTED_REQUEST(421, true, ""),
	UNPROCESSABLE_ENTITY(422, true, ""),
	LOCKED(423, true, ""),
	FAILED_DEPENDENCY(424, true, ""),
	UPGRADE_REQUIRED(426, true, ""),
	PRECONDITION_REQUIRED(428, true, ""),
	TOO_MANY_REQUESTS(429, true, ""),
	REQUEST_HEADER_FIELDS_TOO_LARGE(431, true, ""),
	UNAVAILABLE_FOR_LEGAL_REASONS(451, true, ""),
	INTERNAL_SERVER_ERROR(500, true, "O servidor encontrou uma situação inesperada."),
	NOT_IMPLEMENTED(501, true, "Requisição não suportada pelo servidor."),
	BAD_GATEWAY(502, true, "O servidor obteve uma resposta inválida para a requisição realizada."),
	SERVICE_UNAVAILABLE(503, true, "O servidor não está pronto para manipular a requisição."),
	GATEWAY_TIMEOUT(504, true, ""),
	HTTP_VERSION_NOT_SUPPORTED(505, true, ""),
	VARIANT_ALSO_NEGOTIATES(506, true, ""),
	INSUFFICIENT_STORAGE(507, true, ""),
	LOOP_DETECTED(508, true, ""),
	NOT_EXTENDED(510, true, ""),
	NETWORK_AUTHENTICATION_REQUIRED(511, true, "");

	private final Integer codigo;
	private final Boolean erro;
	private final String mensagem;

	private BoletoHttpStatusEnum(Integer codigo, Boolean erro, String mensagem) {
		this.codigo = codigo;
		this.erro = erro;
		this.mensagem = mensagem;
	}

	public Integer getCodigo() {
		return this.codigo;
	}

	public Boolean isErro() {
		return this.erro;
	}

	public String getMensagem() {
		return this.mensagem;
	}

	public static Optional<BoletoHttpStatusEnum> getBoletoHttpStatusEnumPorCodigo(Integer codigo) {
		return Objects.isNull(codigo) ? Optional.empty()
				: Stream.of(values()).filter(http -> http.getCodigo().equals(codigo)).findFirst();
	}

	public static List<BoletoHttpStatusEnum> getListaBoletoHttpStatusEnumErro() {
		return Stream.of(values()).filter(BoletoHttpStatusEnum::isErro).collect(toCollection(ArrayList::new));
	}

	public static List<BoletoHttpStatusEnum> getListaBoletoHttpStatusEnumNaoErro() {
		Predicate<BoletoHttpStatusEnum> naoIsErro = BoletoHttpStatusEnum::isErro;
		return Stream.of(values()).filter(naoIsErro.negate()).collect(toCollection(ArrayList::new));
	}

	public void throwExceptionQuandoErro() throws StreamSeiException {
		Predicate<BoletoHttpStatusEnum> naoIsErro = BoletoHttpStatusEnum::isErro;
		Optional.of(this).filter(naoIsErro.negate()).orElseThrow(() -> new StreamSeiException(this.toString()));
	}

	@Override
	public String toString() {
		return new StringBuilder().append(this.isErro() ? "ERRO: " : "STATUS: ").append(this.getCodigo()).append(". ").append(this.getMensagem()).toString();
	}
}
