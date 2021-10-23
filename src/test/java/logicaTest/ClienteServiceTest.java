package logicaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.mongo.UltimaDireccionRepositorio;
import com.vpi.springboot.exception.UsuarioException;

//@RunWith(MockitoJUnitRunner.class)
class ClienteServiceTest {

	
	
	@Mock
	private ClienteRepositorio clienteRepo;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private GeoLocalizacionRepositorio geoRepo;
	@Mock
	private DireccionRepositorio dirRepo;
	@Mock
	private UltimaDireccionRepositorio ultimaDireccionRepo;
	
	@InjectMocks
	private ClienteService mockCliente;
	
	
	private Optional<Cliente> optionalCliente;
	private Optional<Cliente> optionalClienteVacio = Optional.empty();
	private Cliente cliente;
	private List<Direccion> direcciones = new ArrayList<Direccion>();
	private GeoLocalizacion geo;
	private Direccion dir;
	private List<Cliente> clientes = new ArrayList<Cliente>();
	private DTDireccion dtDir;
	LastDireccioClientenMongo actualDire = new LastDireccioClientenMongo();
	
	

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
								true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		geo = new GeoLocalizacion(2210.0, 2515.2);
		dir = new Direccion("calle1 4555", geo);
		direcciones.add(dir);
		cliente.setDirecciones(direcciones);
		clientes.add(cliente);
		dtDir = new DTDireccion("calle1 4555", geo);
		actualDire.setIdDireccion(dir.getId());
		actualDire.set_id(cliente.getMail());
		
	}
	
	@Test
	public void testAltaCliente() throws UsuarioException, Exception {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalClienteVacio);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("1234");
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		//Mockito.doNothing().when(clienteRepo).save(Mockito.any(Cliente.class));
		mockCliente.altaCliente(cliente);
	}
	
	@Test
	public void testGetDireccionCliente() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		mockCliente.getDireccionCliente(cliente.getMail());
	}
	
	@Test
	public void testAltaDireccion() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.doReturn(geo).when(geoRepo).save(Mockito.any(GeoLocalizacion.class));
		Mockito.doReturn(dir).when(dirRepo).save(Mockito.any(Direccion.class));
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));	
		Mockito.doReturn(actualDire).when(ultimaDireccionRepo).save(Mockito.any(LastDireccioClientenMongo.class));
		mockCliente.altaDireccion(dtDir, cliente.getMail());
	}


}