package logicaTest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vpi.springboot.Logica.GeneralService;
import com.vpi.springboot.Logica.MailService;
import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Promocion;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.TokenVerificacion;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.CategoriaRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.DireccionRepositorio;
import com.vpi.springboot.Repositorios.ProductoRepositorio;
import com.vpi.springboot.Repositorios.PromocionRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.VerificacionRepositorio;
import com.vpi.springboot.exception.RestauranteException;

class GeneralServiceTest {
	
	@Mock
	private ClienteRepositorio clienteRepo;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private RestauranteRepositorio resRepo;
	@Mock
	private AdministradorRepositorio adminRepo;
	@Mock
	private VerificacionRepositorio tokenRepo;
	@Mock
	private DireccionRepositorio dirRepo;
	@Mock
	private ProductoRepositorio proRepo;
	@Mock
	private PromocionRepositorio promoRepo;
	@Mock
	private CategoriaRepositorio catRepo;
	@Mock
	private MailService mailSender;

	@InjectMocks
	private GeneralService mockGeneral;
	
	private Restaurante restaurante;
	private Optional<Restaurante> optionalRestaurante;
	private Optional<Cliente> optionalCliente;
	private Cliente cliente;
	private Administrador admin;
	private Optional<Administrador> optionalAdmin;
	private GeoLocalizacion geo;
	private Direccion dir;
	private Optional<Direccion> optionalDireccion;
	private List<Direccion> direcciones = new ArrayList<Direccion>();
	private Producto producto;
	private List<Producto> productos = new ArrayList<Producto>();
	private TokenVerificacion token;
	private List<Restaurante> restauranteList = new ArrayList<Restaurante>();
	private Page<Restaurante> restaurantePage;
	private Promocion promo;
	private List<Promocion> promoList = new ArrayList<Promocion>();
	private Categoria cat;
	private List<Categoria> catList = new ArrayList<Categoria>();

	@SuppressWarnings("deprecation")
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
				true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		admin = new Administrador("admin@gmail.com","1234" , "2562156", null, false, true, LocalDate.now());
		optionalAdmin = Optional.of(admin);
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);
				restaurante.setCostoDeEnvio(50);
		cat = new Categoria("minutas", null);
		catList.add(cat);
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		producto.setCategoria(cat);
		producto.setRestaurante(restaurante);
		productos.add(producto);
		restaurante.setProductos(productos);
		optionalRestaurante = Optional.of(restaurante);
		geo = new GeoLocalizacion(2210.0, 2515.2);
		restaurante.setGeoLocalizacion(geo);
		restauranteList.add(restaurante);
		restaurantePage = new PageImpl<Restaurante>(restauranteList);
		dir = new Direccion("calle1 4555", geo);
		dir.setId(3);
		optionalDireccion = Optional.of(dir);
		direcciones.add(dir);
		cliente.setDirecciones(direcciones);
		token = new TokenVerificacion(cliente);
		token.setFechaExpiracion(Calendar.getInstance().getTime());
		promo = new Promocion("promo1", "descripcion", 232, null, 0, true);
		promo.setCategoria(cat);
		promo.setRestaurante(restaurante);
		promo.setProductos(productos);
		restaurante.addProducto(promo);
		promoList.add(promo);
		
	}
	
	@Test
	public void testRecuperarPass() throws Exception {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("pass");
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		Mockito.doNothing().when(mailSender).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockGeneral.recuperarPassword(cliente.getMail());
	
	}

	@Test
	public void testRecuperarPass2() throws Exception {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("pass");
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		Mockito.doNothing().when(mailSender).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockGeneral.recuperarPassword(restaurante.getMail());
	}
	
	@Test
	public void testRecuperarPass3() throws Exception {
		Mockito.when(adminRepo.findById(Mockito.anyString())).thenReturn(optionalAdmin);
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("pass");
		Mockito.doReturn(admin).when(adminRepo).save(Mockito.any(Administrador.class));
		Mockito.doNothing().when(mailSender).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		mockGeneral.recuperarPassword(admin.getMail());
	}
	
	@Test
	public void testActivarCuenta() throws MessagingException {
		Mockito.when(tokenRepo.findByToken(Mockito.anyString())).thenReturn(token);
		Mockito.doReturn(token).when(tokenRepo).save(Mockito.any(TokenVerificacion.class));
		Mockito.doNothing().when(mailSender).sendMail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(tokenRepo).delete(Mockito.any(TokenVerificacion.class));
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockGeneral.activarCuenta("ffefjifoasfhuw");
	}
	
	@Test
	public void testGetRestauranteTest() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		mockGeneral.getRestaurante(restaurante.getMail());
	}
	
	@Test
	public void testBuscarRestaurante() throws RestauranteException {
		Mockito.when(resRepo.buscarRestauranteDesdeClientePorNombreYCategoria(Mockito.anyString(),Mockito.anyString(),Mockito.any())).thenReturn(restauranteList);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		mockGeneral.buscarRestaurante("re", "minutas", 0);
	}
	
	@Test
	public void testBuscarRestaurante2() throws RestauranteException {
		Mockito.when(resRepo.buscarRestauranteDesdeClientePorNombre(Mockito.anyString(),Mockito.any())).thenReturn(restauranteList);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		mockGeneral.buscarRestaurante("re", "", dir.getId());
	}
	
	@Test
	public void testBuscarRestaurante3() throws RestauranteException {
		Mockito.when(resRepo.buscarRestauranteDesdeClientePorCategoria(Mockito.anyString(),Mockito.any())).thenReturn(restauranteList);
		Mockito.when(dirRepo.findById(Mockito.anyInt())).thenReturn(optionalDireccion);
		mockGeneral.buscarRestaurante("", "minutas", dir.getId());
	}
	
	@Test
	public void testListarRestaurantes() throws RestauranteException {
		Mockito.when(resRepo.listarRestauranteDesdeClientePorNombreYCategoria(Mockito.anyString(),Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, restaurante.getNombre(), "minutas", "1", 0);
	}
	
	@Test
	public void testListarRestaurantes2() throws RestauranteException {
		Mockito.when(resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivosPorNombre(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, restaurante.getNombre(), "", "1", 0);
	}
	
	@Test
	public void testListarRestaurantes3() throws RestauranteException {
		Mockito.when(resRepo.listarRestauranteDesdeClientePorCategoria(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, "", "minutas", "1", 0);
	}
	
	
	
	@Test
	public void testListarRestaurantes4() throws RestauranteException {
		Mockito.when(resRepo.buscarRestaurantesPorEstadoNoBloqueadosYActivos(Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, "", "", "1", 0);
	}
	
	@Test
	public void testListarRestaurantes5() throws RestauranteException {
		Mockito.when(resRepo.listarRestauranteDesdeClientePorCategoria(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, "", "minutas", "1", 1);
	}
	
	@Test
	public void testListarRestaurantes6() throws RestauranteException {
		Mockito.when(resRepo.listarRestauranteDesdeClientePorCategoria(Mockito.anyString(),Mockito.any(),Mockito.any())).thenReturn(restaurantePage);
		mockGeneral.listarRestaurantes(0, 5, 5, "", "minutas", "", 1);
	}
	@Test
	public void testListarMenus() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(proRepo.findAllByRestaurante(Mockito.any(Restaurante.class))).thenReturn(productos);
		mockGeneral.listarMenus(restaurante.getMail());
	}
	
	@Test
	public void testListarPromocionesRestaurantes() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(promoRepo.findAllByRestaurante(restaurante)).thenReturn(promoList);
		mockGeneral.listarPromocionesRestaurante(restaurante.getMail());
	}
	
	@Test
	public void testBuscarMenusPromociones() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.when(proRepo.findAllByParametro(Mockito.any(Restaurante.class), Mockito.anyString())).thenReturn(productos);
		mockGeneral.buscarMenusPromociones(restaurante.getMail(), producto.getNombre());
	}
	
	@Test
	public void testListarCategorias() {
		Mockito.when(catRepo.findAll()).thenReturn(catList);
		mockGeneral.listarCategorias();
	}
	
	
}
