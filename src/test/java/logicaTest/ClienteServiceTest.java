package logicaTest;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.UltimaDireccionRepositorio;
import com.vpi.springboot.exception.ProductoException;
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
	@Mock
	private ProductoRepositorio productoRepo;
	@Mock
	private MongoRepositorioCarrito mongoRepo;
	@Mock
	private RestauranteRepositorio restauranteRepo;
	
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
	private Optional<Direccion> optionalDireccion;
	private LastDireccioClientenMongo actualDire = new LastDireccioClientenMongo();
	private Optional<Producto> optionalProducto;
	private Producto producto;
	private Carrito carrito;
	private Optional<Carrito> optionalCarrito;
	private DTProductoCarrito dtProductoCarrito;
	private List<DTProductoCarrito> listProductoCarrito = new ArrayList<DTProductoCarrito>();
	private Restaurante restaurante;
	private Optional<Restaurante> optionalRestaurante;
	private List<Producto> productos = new ArrayList<Producto>();
	

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
								true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);

		optionalRestaurante = Optional.of(restaurante);
		geo = new GeoLocalizacion(2210.0, 2515.2);
		dir = new Direccion("calle1 4555", geo);
		direcciones.add(dir);
		cliente.setDirecciones(direcciones);
		clientes.add(cliente);
		dtDir = new DTDireccion("calle1 4555", geo);
		actualDire.setIdDireccion(dir.getId());
		actualDire.set_id(cliente.getMail());
		optionalDireccion = Optional.of(dir);
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		producto.setRestaurante(restaurante);
		productos.add(producto);
		optionalProducto = Optional.of(producto);
		dtProductoCarrito = new DTProductoCarrito(new DTProducto(producto), 2);
		listProductoCarrito.add(dtProductoCarrito);
		carrito = new Carrito(50, cliente.getMail(), restaurante.getMail(), listProductoCarrito, true);
		
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
	
	@Test
	public void testBajaCuenta() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));	
		mockCliente.bajaCuenta(cliente.getMail());
	}
	
	@Test
	public void testModificarDireccion() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));	
		Mockito.doReturn(dir).when(dirRepo).save(Mockito.any(Direccion.class));
		Mockito.doReturn(actualDire).when(ultimaDireccionRepo).save(Mockito.any(LastDireccioClientenMongo.class));
		mockCliente.modificarDireccion(dir.getId(), dtDir, cliente.getMail());
	}
	
	@Test
	public void testEliminarDireccion() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		Mockito.doNothing().when(dirRepo).delete(Mockito.any(Direccion.class));
		mockCliente.eliminarDireccion(dir.getId(), cliente.getMail());
	}
	
	@Test
	public void testAgregarACarrito() throws ProductoException {
		Mockito.when(productoRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(carrito);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(carrito).when(mongoRepo).save(Mockito.any(Carrito.class));
		mockCliente.agregarACarrito(producto.getId(), 1, cliente.getMail(), restaurante.getMail());
	}
	
	@Test 
	public void testVerCarrito() {
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(carrito);
		mockCliente.verCarrito(cliente.getMail());
	}

}