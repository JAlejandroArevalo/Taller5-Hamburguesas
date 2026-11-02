package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu producto1;

    @BeforeEach
    void setUp( ) throws Exception
    {
        producto1 = new ProductoMenu( "papas medianas", 5500 );
    }
    @Test
    void testGetNombre( )
    {
        assertEquals( "papas medianas", producto1.getNombre( ), "El nombre del producto no es el esperado." );
    }
    @Test
    void testGetPrecio( )
    {
        assertEquals( 5500, producto1.getPrecio( ), "El precio del producto no es el esperado." );
    }
    @Test
    void testGenerarTextoFactura( )
    {
        String texto = producto1.generarTextoFactura( );

        assertTrue( texto.contains( "papas medianas" ), "El texto de la factura debería incluir el nombre del producto." );
        assertTrue( texto.contains( "5500" ), "El texto de la factura debería incluir el precio del producto." );
    }

}