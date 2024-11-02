package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		return clienteRepository.findById(id).orElseThrow(() ->
		new NoSuchElementException("Cliente com ID " + id + " não encontrado")
		);
	}

	@Override
	public ClienteDTO inserir(Cliente cliente) {

		Cliente clienteSalvo = salvarClienteComCep(cliente);

		ClienteDTO clienteDTO = ClienteDTO.builder()
				.id(clienteSalvo.getId())
				.nome(clienteSalvo.getNome())
				.endereco(clienteSalvo.getEndereco())
				.build();

		return clienteDTO;
	}

	@Override
	public ClienteDTO atualizar(Long id, Cliente cliente) {

		if (clienteRepository.findById(id).isEmpty()) {
			throw new RuntimeException("Cliente com id: " + id + " não encontrado");
		}

		Cliente clienteSalvo = salvarClienteComCep(cliente);

		ClienteDTO clienteDTO = ClienteDTO.builder()
				.id(clienteSalvo.getId())
				.nome(clienteSalvo.getNome())
				.endereco(clienteSalvo.getEndereco())
				.build();

		return clienteDTO;
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
	}

	private Cliente salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		return clienteRepository.save(cliente);
	}

}
