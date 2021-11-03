package logicaTest;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.vpi.springboot.Logica.AdministradorService;
import com.vpi.springboot.Modelo.Administrador;
import com.vpi.springboot.Modelo.Categoria;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTRestaurantePedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Repositorios.AdministradorRepositorio;
import com.vpi.springboot.Repositorios.CalificacionRestauranteRepositorio;
import com.vpi.springboot.Repositorios.ClienteRepositorio;
import com.vpi.springboot.Repositorios.RestauranteRepositorio;
import com.vpi.springboot.Repositorios.mongo.RestaurantePedidosRepositorio;
import com.vpi.springboot.exception.AdministradorException;
import com.vpi.springboot.exception.RestauranteException;
import com.vpi.springboot.exception.UsuarioException;

class AdministradorServiceTest {
	
	@Mock
	PasswordEncoder passwordEncoder;
	@Mock
	AdministradorRepositorio adminRepo;
	@Mock
	ClienteRepositorio clienteRepo;
	@Mock
	RestauranteRepositorio resRepo;
	@Mock
	RestaurantePedidosRepositorio restaurantePedidosRepo;
	
	@InjectMocks
	AdministradorService mockAdmin;

	private Administrador admin;
	private Optional<Administrador> optionalAdminVacio = Optional.empty();
	private Optional<Cliente> optionalCliente;
	private Cliente cliente;
	private List<Cliente> clientes = new ArrayList<Cliente>();
	private Page<Cliente> clientePage;
	private Optional<Restaurante> optionalRestaurante;
	private Restaurante restaurante;
	private List<Restaurante> restaurantes = new ArrayList<Restaurante>();
	private Page<Restaurante> restaurantePage;
	private Optional<Administrador> optionalAdmin;
	private List<Administrador> adminList = new ArrayList<Administrador>();
	private Page<Administrador> adminPage;
	private GeoLocalizacion geo;
	private Producto producto;
	private List<Producto> productos = new ArrayList<Producto>();
	private Categoria cat;
	private List<Categoria> catList = new ArrayList<Categoria>();
	private Pageable paging;
	private Pageable paging2;
	private Sort sort; 
	private DTRestaurantePedido dtResPed;
	private List<DTRestaurantePedido> dtResPedList = new ArrayList<DTRestaurantePedido>();
	private Page<DTRestaurantePedido> pageRestaurantePedido;
	
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		cat = new Categoria("minutas", null);
		catList.add(cat);
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		producto.setCategoria(cat);
		
		productos.add(producto);
		geo = new GeoLocalizacion(2210.0, 2515.2);
		admin = new Administrador("admin@gmail.com", "1234", "2252654", null, false, true, LocalDate.now());
		adminList.add(admin);
		adminPage = new PageImpl<>(adminList);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
				true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", "token"));
		cliente = optionalCliente.get();
		clientes.add(cliente);
		clientePage = new PageImpl<>(clientes);
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.now(), LocalTime.now(), LocalDate.now(), 50, geo, productos, "SDLM", true);
				restaurante.setCostoDeEnvio(50);
		restaurante.setFechaCreacion(LocalDate.now());
		restaurante.setProductos(productos);
		producto.setRestaurante(restaurante);
		restaurantes.add(restaurante);
		restaurantePage = new PageImpl<>(restaurantes);
		optionalRestaurante = Optional.of(restaurante);
		dtResPed = new DTRestaurantePedido(restaurante.getMail(), 34);
		dtResPed.set_id(restaurante.getMail());
		dtResPedList.add(dtResPed);
		pageRestaurantePedido = new PageImpl<>(dtResPedList);
		sort = Sort.by(Sort.Order.desc("cantPedidos"));
		paging = PageRequest.of(0, 5);
		paging2 = PageRequest.of(0, 5, sort);
				
	}
	
	@Test
	public void testCrearAdministrador() throws AdministradorException {
		Mockito.when(adminRepo.findById(Mockito.anyString())).thenReturn(optionalAdminVacio);
		Mockito.doReturn(admin).when(adminRepo).save(Mockito.any(Administrador.class));
		Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("1234");
		mockAdmin.crearAdministrador(admin);
	}
	
	@Test
	public void testeliminarUsuario() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockAdmin.eliminarUsuario(cliente.getMail(), "cliente");
	}
	
	@Test
	public void testeliminarUsuario2() throws UsuarioException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		mockAdmin.eliminarUsuario(restaurante.getMail(), "restaurante");
	}
	
	@Test
	public void testBloquearUsuario() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockAdmin.bloquearUsuario(cliente.getMail(), "cliente");
	}
	
	@Test
	public void testBloquearUsuario2() throws UsuarioException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		mockAdmin.bloquearUsuario(restaurante.getMail(), "restaurante");
	}
	
	@Test
	public void testDesbloquearUsuario() throws UsuarioException {
		Mockito.when(clienteRepo.findById(Mockito.anyString())).thenReturn(optionalCliente);
		Mockito.doReturn(cliente).when(clienteRepo).save(Mockito.any(Cliente.class));
		mockAdmin.desbloquearUsuario(cliente.getMail(), "cliente");
	}
	
	@Test
	public void testDesbloquearUsuario2() throws UsuarioException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		mockAdmin.desbloquearUsuario(restaurante.getMail(), "restaurante");
	}
	
	@Test
	public void testListarUsuariosRegistrados() {
		Mockito.when(clienteRepo.findAll(paging)).thenReturn(clientePage);		
		mockAdmin.listarUsuariosRegistrados(0, 5, 0);
	}
	
	@Test
	public void testListarUsuariosRegistrados2() {
		Mockito.when(resRepo.findAll(paging)).thenReturn(restaurantePage);		
		mockAdmin.listarUsuariosRegistrados(0, 5, 1);
	}

	@Test
	public void testListarUsuariosRegistrados3() {
		Mockito.when(adminRepo.findAll(paging)).thenReturn(adminPage);		
		mockAdmin.listarUsuariosRegistrados(0, 5, 2);
	}
	
	@Test
	public void testBuscarUsuario() {
		Mockito.when(adminRepo.buscarAdministradorNombre(Mockito.anyString(), Mockito.any())).thenReturn(adminPage);		
		mockAdmin.buscarUsuario(0, 1, 2, 2, admin.getMail(), "1", 0);
	}

	@Test
	public void testBuscarUsuario2() {
		Mockito.when(adminRepo.buscarAdministrador(Mockito.any())).thenReturn(adminPage);		
		mockAdmin.buscarUsuario(0, 1, 2, 2, "", "1", 0);
	}
	
	@Test
	public void testBuscarUsuario3() {
		Mockito.when(adminRepo.buscarAdministradorNombre(Mockito.anyString(), Mockito.any())).thenReturn(adminPage);		
		mockAdmin.buscarUsuario(0, 1, 2, 0, admin.getMail(), "1", 0);
	}
	
	@Test
	public void testBuscarUsuario4() {
		Mockito.when(adminRepo.buscarAdministrador(Mockito.any())).thenReturn(adminPage);		
		mockAdmin.buscarUsuario(0, 1, 2, 0, "", "1", 0);
	}
	
	@Test
	public void testBuscarUsuario5() {
		Mockito.when(resRepo.buscarRestauranteNombre(Mockito.anyString(), Mockito.any())).thenReturn(restaurantePage);		
		mockAdmin.buscarUsuario(0, 1, 1, 2, restaurante.getMail(), "1", 0);
	}

	@Test
	public void testBuscarUsuario6() {
		Mockito.when(resRepo.buscarRestaurante(Mockito.any())).thenReturn(restaurantePage);		
		mockAdmin.buscarUsuario(0, 1, 1, 2, "", "1", 0);
	}
	
	@Test
	public void testBuscarUsuario7() {
		Mockito.when(resRepo.buscarRestauranteNombre(Mockito.anyString(), Mockito.any())).thenReturn(restaurantePage);		
		mockAdmin.buscarUsuario(0, 1, 1, 0, restaurante.getMail(), "1", 0);
	}
	
	@Test
	public void testBuscarUsuario8() {
		Mockito.when(resRepo.buscarRestaurante(Mockito.any())).thenReturn(restaurantePage);		
		mockAdmin.buscarUsuario(0, 1, 1, 0, "", "1", 0);
	}
	
	@Test
	public void testBuscarUsuario9() {
		Mockito.when(clienteRepo.buscarClienteNombre(Mockito.anyString(), Mockito.any())).thenReturn(clientePage);		
		mockAdmin.buscarUsuario(0, 1, 0, 2, cliente.getMail(), "1", 0);
	}

	@Test
	public void testBuscarUsuario10() {
		Mockito.when(clienteRepo.buscarCliente(Mockito.any())).thenReturn(clientePage);		
		mockAdmin.buscarUsuario(0, 1,0 , 2, "", "1", 0);
	}
	
	@Test
	public void testBuscarUsuario11() {
		Mockito.when(clienteRepo.buscarClienteNombre(Mockito.anyString(), Mockito.any())).thenReturn(clientePage);		
		mockAdmin.buscarUsuario(0, 1, 0, 0, cliente.getMail(), "1", 0);
	}
	
	@Test
	public void testBuscarUsuario12() {
		Mockito.when(clienteRepo.buscarCliente(Mockito.any())).thenReturn(clientePage);		
		mockAdmin.buscarUsuario(0, 1, 0, 0, "", "1", 0);
	}
	
	@Test
	public void testListarRestaurantes() {
		Mockito.when(resRepo.findByEstado(Mockito.any(), Mockito.any())).thenReturn(restaurantePage);	
		mockAdmin.listarRestaurantes(0, 5, 0);
	}
	
	@Test
	public void testListarRestaurantes1() {
		Mockito.when(resRepo.findByEstado(Mockito.any(), Mockito.any())).thenReturn(restaurantePage);	
		mockAdmin.listarRestaurantes(0, 5, 1);
	}
	@Test
	public void testListarRestaurantes2() {
		Mockito.when(resRepo.findByEstado(Mockito.any(), Mockito.any())).thenReturn(restaurantePage);	
		mockAdmin.listarRestaurantes(0, 5, 2);
	}
	@Test
	public void testListarRestaurantes3() {
		Mockito.when(resRepo.findAll(paging)).thenReturn(restaurantePage);	
		mockAdmin.listarRestaurantes(0, 5, 3);
	}
	
	@Test
	public void testCambiarEstadoRestaurante() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		mockAdmin.cambiarEstadoRestaurante(restaurante.getMail(), 1);
	}
	
	@Test
	public void testCambiarEstadoRestaurante2() throws RestauranteException {
		Mockito.when(resRepo.findById(Mockito.anyString())).thenReturn(optionalRestaurante);
		Mockito.doReturn(restaurante).when(resRepo).save(Mockito.any(Restaurante.class));
		mockAdmin.cambiarEstadoRestaurante(restaurante.getMail(), 2);
	}
	
	@Test
	public void testRestauranteConMasPedidos() {
		Mockito.when(restaurantePedidosRepo.findAll(paging2)).thenReturn(pageRestaurantePedido);
		mockAdmin.restaurantesConMasPedidos(0, 5);
	}
}
