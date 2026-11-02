package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private Combo combo;

    @BeforeEach
    void setUp( ) throws Exception
    {
        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "corral", 14000 ) );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );

        combo = new Combo( "combo corral", 0.1, items );
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "combo corral", combo.getNombre( ), "El nombre del combo no es el esperado." );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 22050, combo.getPrecio( ), "El precio debería ser la suma de los productos (24500) menos el 10% de descuento." );
    }
    @Test
    void testGenerarTextoFactura( )
    {
        String texto = combo.generarTextoFactura( );

        assertTrue( texto.contains( "combo corral" ), "El texto debería incluir el nombre del combo." );
        assertTrue( texto.contains( "0.1" ), "El texto debería incluir el valor del descuento." );
        assertTrue( texto.contains( "22050" ), "El texto debería incluir el precio del combo." );
    }
    @Test
    void testGetPrecioSinDescuento( )
    {
        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>( );
        items.add( new ProductoMenu( "papas medianas", 5500 ) );
        items.add( new ProductoMenu( "gaseosa", 5000 ) );

        Combo comboSinDescuento = new Combo( "combo simple", 0, items );

        assertEquals( 10500, comboSinDescuento.getPrecio( ), "Sin descuento el precio debería ser la suma de los productos." );
    }
}