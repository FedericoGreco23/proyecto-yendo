package logicaTest;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mockitoSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToInt;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vpi.springboot.Logica.ClienteService;
import com.vpi.springboot.Logica.NextSequenceService;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.LastDireccioClientenMongo;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTDireccion;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.GeoLocalizacionRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.UltimaDireccionRepositorio;
import com.vpi.springboot.exception.CarritoException;
import com.vpi.springboot.exception.DireccionException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.ReclamoException;
import com.vpi.springboot.exception.RestauranteException;
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
	@Mock
	private PedidoRepositorio pedidoRepo;
	@Mock
	private CalificacionRestauranteRepositorio calRestauranteRepo;
	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;
	@Mock
	private ReclamoRepositorio recRepo;
	@Mock
	private NextSequenceService nextSequence;
	
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
	private Optional<LastDireccioClientenMongo> optionalLastDir;
	private Optional<Producto> optionalProducto;
	private Producto producto;
	private Carrito carrito;
	private Carrito carritoVacio;
	private Optional<Carrito> optionalCarritoLleno;
	private DTProductoCarrito dtProductoCarrito;
	private List<DTProductoCarrito> listProductoCarrito = new ArrayList<DTProductoCarrito>();
	private Restaurante restaurante;
	private Optional<Restaurante> optionalRestaurante;
	private List<Producto> productos = new ArrayList<Producto>();
	private Pedido pedido;
	private Optional<Pedido> optionalPedido;
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	private Page<Pedido> pagePedido;
	private Calificacion calificacion;
	private CalificacionRestaurante calificacionRestaurante;
	private Optional<CalificacionRestaurante> optionalCalificacionRestaurante;
	List<CalificacionRestaurante> calificaciones = new ArrayList<CalificacionRestaurante>();
	private List<Reclamo> reclamos = new ArrayList<Reclamo>();
	private Page<Reclamo> pageReclamo;
	private Reclamo reclamo;
	private Optional<Reclamo> optionalReclamo;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
								true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);
		restaurante.setCostoDeEnvio(50);
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
		carritoVacio= new Carrito();
		optionalCarritoLleno = Optional.of(carrito);
		optionalLastDir = Optional.of(actualDire);
		pedido = new Pedido(1,LocalDateTime.now(), 1250.2, EnumEstadoPedido.PROCESADO, EnumMetodoDePago.EFECTIVO,50, dir.getCalleNro(), restaurante, cliente, null);
		optionalPedido = Optional.of(pedido); 
		cliente.addPedido(pedido);
		pedidos.add(pedido);
		pagePedido= new PageImpl<>(pedidos);
		calificacion = new Calificacion(5, "comentario", "foto",LocalDateTime.now());
		calificacionRestaurante = new CalificacionRestaurante(calificacion, cliente, restaurante);
		optionalCalificacionRestaurante = Optional.of(calificacionRestaurante);
		calificaciones.add(calificacionRestaurante);
		reclamo = new Reclamo("comentario", LocalDateTime.now(), EnumEstadoReclamo.ENVIADO, "");
		reclamo.setPedido(pedido);
		reclamo.setRestaurante(restaurante);
		reclamos.add(reclamo);
		pageReclamo=new PageImpl<>(reclamos);
		
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
	public void testAgregarACarrito2() throws ProductoException {
		Mockito.when(productoRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(null);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(restauranteRepo.getById(Mockito.anyString())).thenReturn(restaurante);
		Mockito.when(nextSequence.getNextSequence("customSequences")).thenReturn(2);
		Mockito.doReturn(carrito).when(mongoRepo).save(Mockito.any(Carrito.class));
		mockCliente.agregarACarrito(producto.getId(), 1, cliente.getMail(), restaurante.getMail());
	}
	
	@Test 
	public void testVerCarrito() {
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(carrito);
		mockCliente.verCarrito(cliente.getMail());
	}
	
	@Test
	public void testGetUltimaDireccionSeleccionada() {
		Mockito.doReturn(optionalLastDir).when(ultimaDireccionRepo).findById(Mockito.anyString());
		mockCliente.getUltimaDireccionSeleccionada(cliente.getMail());
	}
	
	@Test
	public void testAltaPedido() throws RestauranteException, CarritoException, DireccionException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		Mockito.doReturn(carrito).when(mongoRepo).save(Mockito.any(Carrito.class));
		Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(Mockito.anyString(),Mockito.any(DTProducto.class));
		mockCliente.altaPedido(50, EnumMetodoDePago.EFECTIVO, dir.getId(), cliente.getMail(), null);	
	}
	
	@Test
	public void testAltaReclamo() throws ReclamoException {
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		mockCliente.altaReclamo(pedido.getId(), cliente.getMail(), "reclamo");
	}
	
	@Test
	public void testEliminarProductoCarrito() {
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(carrito);
		Mockito.doNothing().when(mongoRepo).delete(Mockito.any(Carrito.class));
		Mockito.doReturn(carrito).when(mongoRepo).save(Mockito.any(Carrito.class));
		mockCliente.eliminarProductoCarrito(producto.getId(), 1, cliente.getMail());
	}
	
	@Test
	public void testEliminarCarrito() throws CarritoException {
		Mockito.when(mongoRepo.findByMailAndActivo(Mockito.anyString(), Mockito.anyBoolean())).thenReturn(carrito);
		Mockito.doNothing().when(mongoRepo).delete(Mockito.any(Carrito.class));
		mockCliente.eliminarCarrito((int) carrito.getId(), cliente.getMail());
	}
	
	@Test
	public void testListarPedidos() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByRestaurante(Mockito.anyString(), Mockito.any(Cliente.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		mockCliente.listarPedidos(5, 5, restaurante.getMail(),"" , "costoTotal", 0, "");	
	}
	
	@Test 
	public void testListarPedidos2() throws RestauranteException, UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByRestauranteFecha(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Cliente.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		mockCliente.listarPedidos(5, 5, restaurante.getMail(),"25/10/2021" , "costoTotal", 0, "");
	}
	
	@Test 
	public void testListarPedidos3() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findAllByCliente(Mockito.any(Cliente.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		mockCliente.listarPedidos(5, 5, "","" , "costoTotal", 0, cliente.getMail());
	}
	
	/*@Test 
	public void testListarPedidos4() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByFecha(Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		mockCliente.listarPedidos(5, 5, "","12/09/2021" , "costoTotal", 0, cliente.getMail());
	}*/
	
	@Test
	public void testBuscarPedidoRealizado() throws PedidoException, UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.buscarPedidoPorNumero(Mockito.anyInt())).thenReturn(pedido);
		mockCliente.buscarPedidoRealizado(pedido.getId());
	}
	
	@Test
	public void testCalificarRestaurante() throws UsuarioException, RestauranteException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(calificacionRestaurante).when(calRestauranteRepo).save(Mockito.any(CalificacionRestaurante.class));
		Mockito.doReturn(pedidos).when(pedidoRepo).findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class));
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockCliente.calificarRestaurante(cliente.getMail(), restaurante.getMail(), calificacion);
	}
	
	@Test
	public void testBajaCalificacionRestaurante() throws UsuarioException, RestauranteException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(calRestauranteRepo.findById(Mockito.any())).thenReturn(optionalCalificacionRestaurante);
		Mockito.doNothing().when(calRestauranteRepo).delete(Mockito.any(CalificacionRestaurante.class));
		Mockito.when(calRestauranteRepo.findByRestaurante(Mockito.any())).thenReturn(calificaciones);
		Mockito.doReturn(pedidos).when(pedidoRepo).findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class));
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockCliente.bajaCalificacionRestaurante(cliente.getMail(), restaurante.getMail());
	}
	
	@Test 
	public void testListarReclamos() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(recRepo.findAllByClienteRestaurante(Mockito.any(Cliente.class), Mockito.anyString(), Mockito.any())).thenReturn(pageReclamo);
		mockCliente.listarReclamos(5, 5, restaurante.getMail(), "", 0, cliente.getMail());
	}
}