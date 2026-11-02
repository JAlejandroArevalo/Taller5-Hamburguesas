package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest
{
    private Pedido pedido;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido = new Pedido( "Ana", "Calle 1 # 2-3" );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Ana", pedido.getNombreCliente( ), "El nombre del cliente no es el esperado." );
    }

    @Test
    void testIdPedidoSeIncrementa( )
    {
        Pedido otroPedido = new Pedido( "Luis", "Carrera 4 # 5-6" );

        assertEquals( pedido.getIdPedido( ) + 1, otroPedido.getIdPedido( ), "Cada pedido nuevo debería tener un id una unidad mayor que el anterior." );
    }

    @Test
    void testPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ), "Un pedido sin productos debería costar 0." );
    }

    @Test
    void testPrecioTotalPedido( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas medianas", 5500 ) );

        assertEquals( 23205, pedido.getPrecioTotalPedido( ), "El total debería ser el neto (19500) más el IVA del 19% (3705)." );
    }
    @Test
    void testGenerarTextoFactura( )
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );

        String esperado = "Cliente: Ana\n"
                + "Dirección: Calle 1 # 2-3\n"
                + "----------------\n"
                + "corral\n"
                + "            14000\n"
                + "----------------\n"
                + "Precio Neto:  14000\n"
                + "IVA:          2660\n"
                + "Precio Total: 16660\n";

        assertEquals( esperado, pedido.generarTextoFactura( ), "El texto de la factura no es el esperado." );
      
    }
    @TempDir
    Path carpetaTemporal;

    @Test
    void testGuardarFactura( ) throws Exception
    {
        pedido.agregarProducto( new ProductoMenu( "corral", 14000 ) );
        File archivo = carpetaTemporal.resolve( "factura.txt" ).toFile( );

        pedido.guardarFactura( archivo );

        assertTrue( archivo.exists( ), "El archivo de la factura debería existir." );
        assertEquals( pedido.generarTextoFactura( ), Files.readString( archivo.toPath( ) ), "El archivo debería contener el texto de la factura." );
    }

    @Test
    void testGuardarFacturaEnCarpetaInexistente( )
    {
        File archivo = new File( carpetaTemporal.toFile( ), "no_existe/factura.txt" );

        assertThrows( FileNotFoundException.class, ( ) -> pedido.guardarFactura( archivo ), "Debería lanzar FileNotFoundException si no se puede crear el archivo." );
    }
    
}