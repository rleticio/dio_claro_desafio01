package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 * 
 * @author falvojr
 */
public interface ClienteService {

	Iterable<Cliente> buscarTodos();

	Cliente buscarPorId(Long id);

	ClienteDTO inserir(Cliente cliente);

	ClienteDTO atualizar(Long id, Cliente cliente);

	void deletar(Long id);

}
