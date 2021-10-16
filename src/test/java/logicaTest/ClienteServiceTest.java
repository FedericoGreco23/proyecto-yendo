package logicaTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Repositorios.ClienteRepositorio;

class ClienteServiceTest {

	@Mock
	ClienteRepositorio clienteRepo;
	
	@InjectMocks
	ClienteService mockCliente;
	
	
	@SuppressWarnings("deprecation")
	@BeforeAll
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
