package logicaTest;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Repositorios.CalificacionClienteRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;

class RestauranteServiceTest {

	@Mock
	private RestauranteRepositorio restauranteRepo;
	@Mock
	private CategoriaRepositorio catRepo;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private ProductoRepositorio prodRepo;
	@Mock
	private ProductoRepositorio productoRepo;
	@Mock
	private PedidoRepositorio pedidoRepo;
	@Mock
	private SimpMessagingTemplate simpMessagingTemplate;
	@Mock
	private MongoRepositorioCarrito mongoRepo;
	@Mock
	private PromocionRepositorio promoRepo;
	@Mock
	private ClienteRepositorio clienteRepo;
	@Mock
	private CalificacionClienteRepositorio calClienteRepo;
	
	
	@InjectMocks
	private RestauranteService mockRestaurante;
	
	private Restaurante restaurante;
	private Optional<Restaurante> optionalRestaurante;
	private Optional<Restaurante> optionalRestauranteVacio = Optional.empty();
	private Optional<Cliente> optionalCliente;
	private Cliente cliente;
	private Optional<Producto> optionalProducto = Optional.empty();
	private Producto producto;
	private List<Producto> productos = new ArrayList<Producto>();
	private GeoLocalizacion geo;
	private Direccion dir;
	private Categoria categoria;
	private Optional<Categoria> optionalCategoria;
	private List<Categoria> categorias = new ArrayList<>();
    private Producto productoSinRes;
    private Pedido pedido;
    private Optional<Pedido> optionalPedido;
    private List<Pedido> pedidos = new ArrayList<Pedido>();
    private Page<Pedido> pagePedido;
	private DTProductoCarrito dtProductoCarrito;
	private List<DTProductoCarrito> listProductoCarrito = new ArrayList<DTProductoCarrito>();
    private Carrito carrito;
	private Optional<Carrito> optionalCarritoLleno;
	private List<Promocion> promocionList = new ArrayList<Promocion>();
	private Promocion promo;
	private Optional<Promocion> optionalPromocion;
	private DTPromocionConPrecio promoConprecio;
	private DTProductoIdCantidad dtproductoIdCant;
	private List<DTProductoIdCantidad> listdtproductoIdCant = new ArrayList<DTProductoIdCantidad>();
	private CalificacionCliente calCliente;
	private List<CalificacionCliente> calClienteList = new ArrayList<CalificacionCliente>();
	private Calificacion calificacion;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);
		optionalRestaurante = Optional.of(restaurante);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
				true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		producto.setRestaurante(restaurante);
		productos.add(producto);
		productoSinRes = new Producto("producto1", "producto2", 50,"foto",25,true);
		geo = new GeoLocalizacion(2210.0, 2515.2);
		dir = new Direccion("calle1 4555", geo);
		categoria = new Categoria("minutas", "foto");
		categorias.add(categoria);
		restaurante.setCategorias(categorias);
		optionalCategoria = Optional.of(categoria);
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		producto.setRestaurante(restaurante);
		productos.add(producto);
		optionalProducto = Optional.of(producto);
		producto.setCategoria(categoria);
		pedido = new Pedido(1,LocalDateTime.now(), 1250.2, EnumEstadoPedido.PROCESADO, EnumMetodoDePago.EFECTIVO,50, dir.getCalleNro(), restaurante, cliente, null);
		optionalPedido = Optional.of(pedido); 
		cliente.addPedido(pedido);
		pedidos.add(pedido);
		pagePedido= new PageImpl<>(pedidos);
		dtProductoCarrito = new DTProductoCarrito(new DTProducto(producto), 2);
		listProductoCarrito.add(dtProductoCarrito);
		carrito = new Carrito(50, cliente.getMail(), restaurante.getMail(), listProductoCarrito, true);
		optionalCarritoLleno = Optional.of(carrito);
		promo = new Promocion("promo1", "descripcion", 329, null, 0, true);
		optionalPromocion = Optional.of(promo);
		promocionList.add(promo);
		dtproductoIdCant = new DTProductoIdCantidad(producto.getId(), 2);
		listdtproductoIdCant.add(dtproductoIdCant);
		promoConprecio = new DTPromocionConPrecio(listdtproductoIdCant, 230, 25, "promo1", "descri", null);
		calificacion = new Calificacion(5, "comentario", null, LocalDateTime.now());
		calCliente = new CalificacionCliente(calificacion, restaurante, cliente);
		calClienteList.add(calCliente);
	}
	
	@Test
	public void testAltaRestaurante() throws RestauranteException, CategoriaException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestauranteVacio);
		Mockito.when(restauranteRepo.existeRestauranteNombre(Mockito.anyString())).thenReturn(null);
		Mockito.when(catRepo.findById(Mockito.anyString())).thenReturn(optionalCategoria);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("1234");
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockRestaurante.altaRestaurante(restaurante);
		
	}
	
	@Test
	public void testAltaMenu() throws ProductoException, RestauranteException, CategoriaException, Exception {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(catRepo.findById(Mockito.anyString())).thenReturn(optionalCategoria);
		Mockito.when(prodRepo.findByNombre(Mockito.anyString(), Mockito.any(Restaurante.class))).thenReturn(null);
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockRestaurante.altaMenu(producto, restaurante.getMail());
	}
	
	@Test
	public void testBajaProducto() throws ProductoException {
		Mockito.when(prodRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);
		Mockito.doReturn(producto).when(prodRepo).save(Mockito.any(Producto.class));
		mockRestaurante.bajaMenu(producto.getId());
	}
	
	@Test
	public void testModificarMenu() throws ProductoException {
		Mockito.when(prodRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);	
		Mockito.doReturn(producto).when(prodRepo).save(Mockito.any(Producto.class));
		mockRestaurante.modificarMenu(productoSinRes);
	}

	@Test 
	public void testListarPedidos() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);;
		Mockito.when(pedidoRepo.findByCliente(Mockito.anyString(), Mockito.any(Restaurante.class), Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), cliente.getMail(), "", "", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos2() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByFecha(Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), "", "12/10/2021", "", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos3() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByClienteFechaEstado(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), cliente.getMail(), "25/10/2021", "ACEPTADO", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos4() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByClienteEstado(Mockito.anyString(),Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "", cliente.getMail(), "", "ACEPTADO", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos5() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByClienteFecha(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "", cliente.getMail(), "25/10/2021", "", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos6() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByFechaEstado(Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "", "", "25/10/2021", "ACEPTADO", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos7() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByEstado(Mockito.any(), Mockito.any(Restaurante.class),	Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "", "", "", "ACEPTADO", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos8() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findAllByRestaurante(Mockito.any(Restaurante.class), Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), "", "", "", "costoTotal", 0);
	}
	
	@Test
	public void testAbrirRestaurante() {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockRestaurante.abrirRestaurante(restaurante.getMail());
	}
	
	@Test
	public void testCerrarRestaurante() {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(restauranteRepo).save(Mockito.any(Restaurante.class));
		mockRestaurante.cerrarRestaurante(restaurante.getMail());
	}
	
	@Test
	public void testConfirmarPedido() throws PedidoException {
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(Mockito.anyString(),Mockito.anyString());
		mockRestaurante.confirmarPedido(pedido.getId());
	}
	
	@Test
	public void testModificarDescuentoProducto() throws ProductoException {
		Mockito.when(prodRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);
		Mockito.doReturn(producto).when(prodRepo).save(Mockito.any(Producto.class));
		mockRestaurante.modificarDescuentoProducto(producto.getId(), 10);
	}
	
	@Test
	public void testAltaPromocion() throws RestauranteException, PromocionException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(promoRepo.findAllByRestauranteSimple(Mockito.any(Restaurante.class))).thenReturn(promocionList);
		Mockito.when(prodRepo.findByIdAndRest(Mockito.anyInt(), Mockito.any(Restaurante.class))).thenReturn(producto);
		Mockito.doReturn(promo).when(promoRepo).save(Mockito.any(Promocion.class));
		mockRestaurante.altaPromocion(promoConprecio, restaurante.getMail());
	}
	
	@Test
	public void testBajaPromocion() throws PromocionException {
		Mockito.when(promoRepo.findById(Mockito.anyInt())).thenReturn(optionalPromocion);
		Mockito.doReturn(promo).when(promoRepo).save(Mockito.any(Promocion.class));
		mockRestaurante.bajaPromocion(promo.getId());
	}
	
	@Test
	public void testModificarPromocion() throws PromocionException, ProductoException {
		Mockito.when(promoRepo.findById(Mockito.anyInt())).thenReturn(optionalPromocion);
		Mockito.when(prodRepo.findByIdAndRest(Mockito.anyInt(), Mockito.any(Restaurante.class))).thenReturn(producto);
		Mockito.doReturn(promo).when(promoRepo).save(Mockito.any(Promocion.class));
		mockRestaurante.modificarPromocion(promo);
	}
	
	@Test
	public void testRechazarPedido() throws PedidoException {
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		Mockito.doNothing().when(simpMessagingTemplate).convertAndSend(Mockito.anyString(),Mockito.anyString());
		mockRestaurante.rechazarPedido(pedido.getId());
	}
	
	@Test
	public void testCalificarCliente() {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class))).thenReturn(pedidos);
	}
}
