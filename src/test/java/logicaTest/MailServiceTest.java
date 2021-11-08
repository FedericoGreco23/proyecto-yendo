package logicaTest;

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
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import com.vpi.springboot.Logica.MailService;
import com.vpi.springboot.Modelo.Cliente;
import com.vpi.springboot.Modelo.Direccion;
import com.vpi.springboot.Modelo.GeoLocalizacion;
import com.vpi.springboot.Modelo.Pedido;
import com.vpi.springboot.Modelo.Producto;
import com.vpi.springboot.Modelo.Restaurante;
import com.vpi.springboot.Modelo.dto.DTCarrito;
import com.vpi.springboot.Modelo.dto.DTPedido;
import com.vpi.springboot.Modelo.dto.DTProducto;
import com.vpi.springboot.Modelo.dto.DTProductoCarrito;
import com.vpi.springboot.Modelo.dto.DTReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoPedido;
import com.vpi.springboot.Modelo.dto.EnumEstadoReclamo;
import com.vpi.springboot.Modelo.dto.EnumEstadoRestaurante;
import com.vpi.springboot.Modelo.dto.EnumMetodoDePago;

import io.jsonwebtoken.lang.Assert;

class MailServiceTest {

	@Mock
	private JavaMailSender mailSender;
	
	@InjectMocks
	private MailService mailMock;
	
	
	private Optional<Producto> optionalProducto = Optional.empty();
	private Producto producto;
	private List<Producto> productos = new ArrayList<Producto>();
	private GeoLocalizacion geo;
	private Direccion dir;
	private Optional<Cliente> optionalCliente;
	private Cliente cliente;
	private DTPedido pedido;
	private Pedido pedido1;
	private Restaurante restaurante;
	private DTProductoCarrito dtProductoCarrito;
	private List<DTProductoCarrito> listProductoCarrito = new ArrayList<DTProductoCarrito>();
    private DTCarrito carrito;
    private DTReclamo reclamo;

	
	@SuppressWarnings("deprecation")
	@BeforeEach
	void init() {
		MockitoAnnotations.initMocks(this);
		geo = new GeoLocalizacion(2210.0, 2515.2);
		dir = new Direccion("calle1 4555", geo);
		producto = new Producto("producto1", "producto1", 50,"foto",25,true);
		restaurante = new Restaurante("restaurante1@gmail.com", "123456", "25125325", "foto", false, true, "resto1","dire 3223", 5.0f, EnumEstadoRestaurante.ACEPTADO,
				LocalTime.NOON, LocalTime.MAX, LocalDate.now(), 50, geo, productos, "SDLM", true);
		producto.setRestaurante(restaurante);
		productos.add(producto);
		optionalCliente = Optional.of(new Cliente("cliente1@gmail.com", "1234","25222355" , "linkFoto", false, 
				true, LocalDate.now(), "cliente1", 5.0f, 0.0f, "cliente1", "apellido", null));
		cliente = optionalCliente.get();
		pedido = new DTPedido(1, LocalDateTime.now(), 1250.0, EnumEstadoPedido.PROCESADO, EnumMetodoDePago.EFECTIVO,50, dir.getCalleNro(), null);
		pedido.setRestaurante(restaurante.getNombre());
		pedido1 = new Pedido(1,LocalDateTime.now(), 1250.2, EnumEstadoPedido.PROCESADO, EnumMetodoDePago.EFECTIVO,50, dir.getCalleNro(), restaurante, cliente, null);
		dtProductoCarrito = new DTProductoCarrito(new DTProducto(producto), 2);
		listProductoCarrito.add(dtProductoCarrito);
		carrito = new DTCarrito(1, listProductoCarrito, restaurante.getMail(), 25);
		pedido.setCarrito(carrito);
		reclamo = new DTReclamo(2, "comentario", LocalDateTime.now(), EnumEstadoReclamo.ENVIADO, pedido1, "Resuelto", restaurante.getMail());
	}

	@Test
	public void testGetMailVerificacion() {
		Assert.notNull(mailMock.getMailVerificacion("link"));;
		
	}
	
	
	@Test
	public void testGetPasswordReset() {
		Assert.notNull(mailMock.getPasswordReset("pass"));;
		
	}
	
	@Test
	public void testGetConfirmarPedido() {
		Assert.notNull(mailMock.getConfirmarPedido(pedido, cliente));;
		
	}
	
	@Test
	public void testGetRechazarPedido() {
		Assert.notNull(mailMock.getRechazarPedido(restaurante.getNombre(), cliente.getNombre()));
		
	}
	
	@Test
	public void testGetResolucionReclamo() {
		Assert.notNull(mailMock.getResolucionReclamo(reclamo, cliente));
		
	}
	
}
