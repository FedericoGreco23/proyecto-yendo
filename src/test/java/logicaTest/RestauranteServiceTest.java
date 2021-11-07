package logicaTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vpi.springboot.IdCompuestas.CalificacionClienteId;
import com.vpi.springboot.Logica.RestauranteService;
import com.vpi.springboot.Modelo.Calificacion;
import com.vpi.springboot.Modelo.CalificacionCliente;
import com.vpi.springboot.Modelo.CalificacionRestaurante;
import com.vpi.springboot.Modelo.Carrito;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Reclamo;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.BalanceVentaDTO;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTProductoIdCantidad;
import com.vpi.springboot.Modelo.dto.DTProductoVendido;
import com.vpi.springboot.Modelo.dto.DTPromocionConPrecio;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;
import com.vpi.springboot.Modelo.dto.PedidoMonto;
import com.vpi.springboot.Repositorios.CalificacionClienteRepositorio;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.MongoRepositorioCarrito;
import com.vpi.springboot.Repositorios.PedidoRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.ReclamoRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.BalanceVentasRepositorio;
import com.vpi.springboot.Repositorios.mongo.ProductosVendidosRepositorio;
import com.vpi.springboot.Repositorios.mongo.RestaurantePedidosRepositorio;
import com.vpi.springboot.exception.CategoriaException;
import com.vpi.springboot.exception.PedidoException;
import com.vpi.springboot.exception.ProductoException;
import com.vpi.springboot.exception.PromocionException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

import javassist.expr.NewArray;

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
	@Mock
	private ReclamoRepositorio recRepo;
	@Mock 
	private CalificacionRestauranteRepositorio calRestauranteRepo;
	@Mock
	private RestaurantePedidosRepositorio resPedRepo;
	@Mock
	private BalanceVentasRepositorio balanceVentasRepo;
	@Mock
	private ProductosVendidosRepositorio productosVendidosRepo;
	
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
	private Optional<CalificacionCliente> optionalCalCliente;
	private List<CalificacionCliente> calClienteList = new ArrayList<CalificacionCliente>();
	private Calificacion calificacion;
	private List<Reclamo> reclamos = new ArrayList<Reclamo>();
	private Page<Reclamo> pageReclamo;
	private Reclamo reclamo;
	private Optional<Reclamo> optionalReclamo;
	private CalificacionRestaurante calRestaurante;
	private Page<CalificacionRestaurante> pageCalificacion;
	private List<CalificacionRestaurante> calRestauranteList = new ArrayList<CalificacionRestaurante>();
	private DTRestaurantePedido dtResPed;
	private Optional<DTRestaurantePedido> optionalDtResPed;
	private Optional<DTRestaurantePedido> optionalDtResPedVacio = Optional.empty();
	private List<Object[]> listObject = new ArrayList<>();
	private Object[] ob = new Object[2];
	private BalanceVentaDTO balanceVenta;
	private Optional<BalanceVentaDTO> balanceByMailOp;
	private DTProductoVendido dtProductoVendido;
	private List<DTProductoVendido> dtProductoVendidoList = new ArrayList<DTProductoVendido>();
	private Page<DTProductoVendido> dtProductoVendidoPage;
	private Pageable paging;
			
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);
		optionalRestaurante = Optional.of(restaurante);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
				true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", null));
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
		reclamo = new Reclamo("comentario", LocalDateTime.now(), EnumEstadoReclamo.ENVIADO, "");
		reclamo.setRestaurante(restaurante);
		optionalReclamo = Optional.of(reclamo);
		pedido = new Pedido(1,LocalDateTime.now(), 1250.2, EnumEstadoPedido.PROCESADO, EnumMetodoDePago.EFECTIVO,50, dir.getCalleNro(), restaurante, cliente, null);
		reclamo.setPedido(pedido);
		reclamos.add(reclamo);
		pedido.setReclamos(reclamos);
		pageReclamo=new PageImpl<>(reclamos);
		optionalPedido = Optional.of(pedido); 
		cliente.addPedido(pedido);
		pedidos.add(pedido);
		pagePedido= new PageImpl<>(pedidos);
		dtProductoCarrito = new DTProductoCarrito(new DTProducto(producto), 2);
		listProductoCarrito.add(dtProductoCarrito);
		carrito = new Carrito(50, cliente.getMail(), restaurante.getMail(), listProductoCarrito, true);
		optionalCarritoLleno = Optional.of(carrito);
		promo = new Promocion("promo1", "descripcion", 329, null, 0, true);
		promo.setProductos(productos);
		optionalPromocion = Optional.of(promo);
		promocionList.add(promo);
		dtproductoIdCant = new DTProductoIdCantidad(producto.getId(), 2);
		listdtproductoIdCant.add(dtproductoIdCant);
		promoConprecio = new DTPromocionConPrecio(listdtproductoIdCant, 230, 25, "promo1", "descri", null);
		calificacion = new Calificacion(5, "comentario", null, LocalDateTime.now());
		calCliente = new CalificacionCliente(calificacion, restaurante, cliente);
		optionalCalCliente = Optional.of(calCliente);
		calClienteList.add(calCliente);
		calRestaurante = new  CalificacionRestaurante(5, null, null, null, restaurante, cliente);
		calRestauranteList.add(calRestaurante);
		pageCalificacion = new PageImpl<>(calRestauranteList);
		dtResPed = new DTRestaurantePedido(restaurante.getMail(), 34);
		dtResPed.set_id(restaurante.getMail());
		optionalDtResPed = Optional.of(dtResPed);
		ob[0] = restaurante.getMail();
		ob[1] = BigInteger.valueOf(25);
		listObject.add(ob);
		balanceVenta = new BalanceVentaDTO();
		Map<LocalDate, Map<Integer,  List<String>>> fechaidPedidoMonto= new HashMap<>();
		Map<Integer,  List<String>> pedidoMonto = new HashMap<>();
		List<String> montoAndPago= new ArrayList<String>();
		montoAndPago.add(String.valueOf(BigDecimal.valueOf(pedido.getCostoTotal()).setScale(4, RoundingMode.HALF_UP).doubleValue()));
		montoAndPago.add(pedido.getMetodoDePago().name());
		pedidoMonto.put(pedido.getId(), montoAndPago);
		fechaidPedidoMonto.put(LocalDate.now(), pedidoMonto);
		balanceVenta.setTotal(2505.2);
		//balanceVenta.setFechaidPedidoMonto(fechaidPedidoMonto);
		balanceByMailOp = Optional.of(balanceVenta);
		dtProductoVendido = new DTProductoVendido(String.valueOf(producto.getId()), producto.getNombre(), restaurante.getNombre(), "5", "minutas", "2021-10-21");
		dtProductoVendidoList.add(dtProductoVendido);
		dtProductoVendidoPage = new PageImpl<>(dtProductoVendidoList);
		Sort sort = Sort.by(Sort.Order.desc("cantidad"));
		paging = PageRequest.of(0, 5, sort);
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
		mockRestaurante.listarPedidos(5, 5, "", "", "", "ACEPTADO", "costoTotal", 1);
	}
	
	@Test 
	public void testListarPedidos8() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findAllByRestaurante(Mockito.any(Restaurante.class), Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), "", "", "", "", 0);
	}
	
	@Test 
	public void testListarPedidos9() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByIdFechaEstado(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, restaurante.getMail(), String.valueOf(pedido.getId()), "25/10/2021", "ACEPTADO", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos10() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByIdFecha(Mockito.anyInt(), Mockito.any(), Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);	
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "", String.valueOf(pedido.getId()), "25/10/2021", "", "costoTotal", 0);
	}
	
	@Test 
	public void testListarPedidos11() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(pedidoRepo.findByIdEstado(Mockito.anyInt(),Mockito.any(), Mockito.any(Restaurante.class),
				Mockito.any(Pageable.class))).thenReturn(pagePedido);
		Mockito.when(mongoRepo.findById(Mockito.anyInt())).thenReturn(optionalCarritoLleno);
		mockRestaurante.listarPedidos(5, 5, "",String.valueOf(pedido.getId()), "", "ACEPTADO", "costoTotal", 0);
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
		Mockito.when(prodRepo.findById(Mockito.anyInt())).thenReturn(optionalProducto);
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
	public void testCalificarCliente() throws UsuarioException, RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class))).thenReturn(pedidos);
		Mockito.when(calClienteRepo.findById(Mockito.any(CalificacionClienteId.class))).thenReturn(optionalCalCliente);
		Mockito.doNothing().when(calClienteRepo).delete(Mockito.any(CalificacionCliente.class));
		Mockito.when(calClienteRepo.findByCliente(Mockito.any(Cliente.class))).thenReturn(calClienteList);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockRestaurante.calificarCliente(cliente.getMail(), restaurante.getMail(), calificacion);
	}
	
	@Test
	public void testBajaCalificacionCliente() throws UsuarioException, RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(pedidoRepo.findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class))).thenReturn(pedidos);
		Mockito.when(calClienteRepo.findById(Mockito.any(CalificacionClienteId.class))).thenReturn(optionalCalCliente);
		Mockito.doNothing().when(calClienteRepo).delete(Mockito.any(CalificacionCliente.class));
		Mockito.when(calClienteRepo.findByCliente(Mockito.any(Cliente.class))).thenReturn(calClienteList);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockRestaurante.bajaCalificacionCliente(cliente.getMail(), restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteCliente(Mockito.anyString(), Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageReclamo);
		Mockito.when(recRepo.findAllByRestauranteClienteEstadoFecha(Mockito.anyString(), Mockito.any(Restaurante.class), Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, cliente.getMail(), "enviado", "12/10/2021", "1", 0, restaurante.getMail());
	//	mockRestaurante.listarReclamos(0, 5, cliente.getMail(), "1", 2, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos2() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteClienteEstado(Mockito.anyString(), Mockito.any(Restaurante.class), Mockito.any(), Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, cliente.getMail(), "enviado", "", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos3() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteClienteFecha(Mockito.anyString(), Mockito.any(Restaurante.class), Mockito.any(), Mockito.any(),Mockito.any() )).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, cliente.getMail(), "", "22/10/2021", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos4() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteEstadoFecha(Mockito.any(Restaurante.class), Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, "", "enviado", "22/10/2021", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos5() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteCliente(Mockito.anyString(),Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, cliente.getMail(), "", "", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos6() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteEstado(Mockito.any(),Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, "", "enviado", "", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos7() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestauranteFecha(Mockito.any(), Mockito.any(),Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, "", "", "22/10/2021", "1", 0, restaurante.getMail());
	}
	
	@Test
	public void testListarReclamos8() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(recRepo.findAllByRestaurante(Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageReclamo);
		mockRestaurante.listarReclamos(0, 5, "", "", "", "1", 0, restaurante.getMail());
	}
	
	
	@Test
	public void testBuscarPedidoRecibido() throws PedidoException {
		Mockito.when(pedidoRepo.buscarPedidoPorNumero(Mockito.anyInt())).thenReturn(pedido);
		mockRestaurante.buscarPedidoRecibido(pedido.getId());
	}
	
	
	@Test
	public void testgetCalificacionCliente() throws UsuarioException, RestauranteException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(calClienteRepo.findByClienteRestaurante(Mockito.any(Cliente.class), Mockito.any(Restaurante.class))).thenReturn(calCliente);
		mockRestaurante.getCalificacionCliente(cliente.getMail(), restaurante.getMail());
	}
	
	@Test
	public void testConsultarCalificacion() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(calRestauranteRepo.consultarCalificacion(Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageCalificacion);
		mockRestaurante.consultarCalificacion(0, 5, "", 1, restaurante.getMail());
	}
	
	@Test
	public void testConsultarCalificacion2() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(calRestauranteRepo.consultarCalificacion(Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageCalificacion);
		mockRestaurante.consultarCalificacion(0, 5, "1", 1, restaurante.getMail());
	}
	
	@Test
	public void testConsultarCalificacion3() throws RestauranteException {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(calRestauranteRepo.consultarCalificacion(Mockito.any(Restaurante.class), Mockito.any())).thenReturn(pageCalificacion);
		mockRestaurante.consultarCalificacion(0, 5, "2", 2, restaurante.getMail());
	}
	
	@Test
	public void testResolucionReclamo() throws IOException {
		Mockito.when(recRepo.findById(Mockito.anyInt())).thenReturn(optionalReclamo);
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		mockRestaurante.resolucionReclamo(reclamo.getId(), true, "comentario");
	}
	
	@Test
	public void testResolucionReclamo2() throws IOException {
		Mockito.when(recRepo.findById(Mockito.anyInt())).thenReturn(optionalReclamo);
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		mockRestaurante.resolucionReclamo(reclamo.getId(), false, "comentario");
	}
	
	@Test
	public void testGuardarEnMongo() {
		Mockito.when(restauranteRepo.buscarRestaurantesConMasPedidos()).thenReturn(listObject);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(resPedRepo.findById(Mockito.anyString())).thenReturn(optionalDtResPed);
		Mockito.doReturn(dtResPed).when(resPedRepo).save(Mockito.any(DTRestaurantePedido.class));
		mockRestaurante.guaradarEnMongo();

	}
	
	@Test
	public void testGuardarEnMongo2() {
		Mockito.when(restauranteRepo.buscarRestaurantesConMasPedidos()).thenReturn(listObject);
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(resPedRepo.findById(Mockito.anyString())).thenReturn(optionalDtResPedVacio);
		Mockito.doReturn(dtResPed).when(resPedRepo).save(Mockito.any(DTRestaurantePedido.class));
		mockRestaurante.guaradarEnMongo();

	}
	
	/*@Test
	public void testVentaProducto() {
		mockRestaurante.ventaProducto("pro2", "2", "minutas", "22/10/2021");
	}*/
	
	@Test
	public void testDevolucionPedido() {
		Mockito.when(pedidoRepo.findById(Mockito.anyInt())).thenReturn(optionalPedido);
		Mockito.doReturn(pedido).when(pedidoRepo).save(Mockito.any(Pedido.class));
		mockRestaurante.devolucionPedido(pedido.getId(), 2);
	}
	
	@Test
	public void testGetEstado() {
		Mockito.when(restauranteRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		mockRestaurante.getEstado(restaurante.getMail());
	}
	
	@Test
	public void testGetBalanceVenta() {
		Mockito.when(balanceVentasRepo.findById(Mockito.anyString())).thenReturn(balanceByMailOp);
		mockRestaurante.getBalanceVentaByFecha("2020-12-25", "2021-12-25", restaurante.getMail());
	}
	
	@Test
	public void testGetBalanceVenta2() {
		Mockito.when(balanceVentasRepo.findById(Mockito.anyString())).thenReturn(balanceByMailOp);
		mockRestaurante.getBalanceVentaByFecha(LocalDate.now().toString(), LocalDate.now().toString(), restaurante.getMail());
	}
	
	@Test
	public void testActualizarBalanceVentas() {
		Mockito.when(pedidoRepo.findAll()).thenReturn(pedidos);
		Mockito.when(balanceVentasRepo.findById(Mockito.anyString())).thenReturn(balanceByMailOp);
		Mockito.doReturn(balanceVenta).when(balanceVentasRepo).save(Mockito.any(BalanceVentaDTO.class));
		mockRestaurante.actualizarBalanceVentas();
	}
	
	@Test
	public void testTopProductos() {
		Mockito.when(productosVendidosRepo.findAll(paging)).thenReturn(dtProductoVendidoPage);
		mockRestaurante.topProductos(0, 5);
	}
}
