package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class RestauranteTest
{
    private Restaurante restaurante;

    private File ingredientes = new File( "./data/ingredientes.txt" );
    private File menu = new File( "./data/menu.txt" );
    private File combos = new File( "./data/combos.txt" );

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );
    }

    @Test
    void testRestauranteVacio( )
    {
        assertEquals( 0, restaurante.getIngredientes( ).size( ), "Un restaurante nuevo no debería tener ingredientes." );
        assertEquals( 0, restaurante.getMenuBase( ).size( ), "Un restaurante nuevo no debería tener productos." );
        assertEquals( 0, restaurante.getMenuCombos( ).size( ), "Un restaurante nuevo no debería tener combos." );
        assertEquals( 0, restaurante.getPedidos( ).size( ), "Un restaurante nuevo no debería tener pedidos." );
        assertNull( restaurante.getPedidoEnCurso( ), "Un restaurante nuevo no debería tener pedido en curso." );
    }

    @Test
    void testCargarInformacion( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );

        assertEquals( 15, restaurante.getIngredientes( ).size( ), "Debería cargar los 15 ingredientes." );
        assertEquals( 22, restaurante.getMenuBase( ).size( ), "Debería cargar los 22 productos del menú." );
        assertEquals( 4, restaurante.getMenuCombos( ).size( ), "Debería cargar los 4 combos." );
        assertEquals( "combo corral", restaurante.getMenuCombos( ).get( 0 ).getNombre( ), "El primer combo no es el esperado." );
        assertEquals( 22050, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ), "El combo corral debería costar 22050 con su 10% de descuento." );
    }

    @Test
    void testIngredienteRepetido( )
    {
        File repetidos = new File( "./data/pruebas/ingredientes_repetidos.txt" );

        assertThrows( IngredienteRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( repetidos, menu, combos ), "Debería lanzar IngredienteRepetidoException." );
    }

    @Test
    void testProductoRepetido( )
    {
        File repetidos = new File( "./data/pruebas/menu_repetido.txt" );

        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, repetidos, combos ), "Debería lanzar ProductoRepetidoException." );
    }

    @Test
    void testComboRepetido( )
    {
        File repetidos = new File( "./data/pruebas/combos_repetidos.txt" );

        assertThrows( ProductoRepetidoException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, menu, repetidos ), "Debería lanzar ProductoRepetidoException por el combo repetido." );
    }

    @Test
    void testComboConProductoFaltante( )
    {
        File faltante = new File( "./data/pruebas/combos_faltante.txt" );

        assertThrows( ProductoFaltanteException.class, ( ) -> restaurante.cargarInformacionRestaurante( ingredientes, menu, faltante ), "Debería lanzar ProductoFaltanteException." );
    }

    @Test
    void testArchivoInexistente( )
    {
        File noExiste = new File( "./data/pruebas/no_existe.txt" );

        assertThrows( IOException.class, ( ) -> restaurante.cargarInformacionRestaurante( noExiste, menu, combos ), "Debería lanzar IOException si el archivo no existe." );
    }
    @Test
    void testIniciarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 1 # 2-3" );

        assertNotNull( restaurante.getPedidoEnCurso( ), "Debería haber un pedido en curso." );
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso debería ser el de Ana." );
    }

    @Test
    void testIniciarPedidoYaHayUnoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 1 # 2-3" );

        assertThrows( YaHayUnPedidoEnCursoException.class, ( ) -> restaurante.iniciarPedido( "Luis", "Carrera 4 # 5-6" ), "No debería permitir dos pedidos en curso a la vez." );
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ), "El pedido en curso debería seguir siendo el de Ana." );
    }

    @Test
    void testCerrarPedidoSinPedidoEnCurso( )
    {
        assertThrows( NoHayPedidoEnCursoException.class, ( ) -> restaurante.cerrarYGuardarPedido( ), "Debería lanzar NoHayPedidoEnCursoException." );
    }

    @Test
    void testCerrarYGuardarPedido( ) throws Exception
    {
        new File( "./facturas" ).mkdirs( );

        restaurante.iniciarPedido( "Ana", "Calle 1 # 2-3" );
        restaurante.getPedidoEnCurso( ).agregarProducto( new ProductoMenu( "corral", 14000 ) );
        int id = restaurante.getPedidoEnCurso( ).getIdPedido( );
        File factura = new File( "./facturas/factura_" + id + ".txt" );

        try
        {
            restaurante.cerrarYGuardarPedido( );

            assertTrue( factura.exists( ), "Debería existir el archivo de la factura." );
            assertNull( restaurante.getPedidoEnCurso( ), "Ya no debería haber pedido en curso." );
            assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debería quedar en el histórico." );
        }
        finally
        {
            factura.delete( );
        }
    }
}