package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest
{
    private ProductoMenu productoBase;
    private ProductoAjustado productoAjustado;

    @BeforeEach
    void setUp( ) throws Exception
    {
        productoBase = new ProductoMenu( "corral", 14000 );
        productoAjustado = new ProductoAjustado( productoBase );
    }
    @Test
    void testGetNombre( )
    {
        assertEquals( "corral", productoAjustado.getNombre( ), "El nombre del producto ajustado debería ser el del producto base." );
    }
    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, productoAjustado.getPrecio( ), "Sin ajustes, el precio debería ser el del producto base." );
    }
    @Test
    void testGenerarTextoFactura( )
    {
        String texto = productoAjustado.generarTextoFactura( );

        assertTrue( texto.contains( "corral" ), "El texto debería incluir el nombre del producto base." );
        assertTrue( texto.contains( "14000" ), "El texto debería incluir el precio." );
    }
    @Test
    void testGetPrecioConAgregados( )
    {
        productoAjustado.agregarIngrediente( new Ingrediente( "queso mozzarella", 2500 ) );
        productoAjustado.agregarIngrediente( new Ingrediente( "huevo", 2500 ) );

        assertEquals( 19000, productoAjustado.getPrecio( ), "El precio debería sumar el costo de cada ingrediente agregado." );
    }

    @Test
    void testGetPrecioConEliminados( )
    {
        productoAjustado.eliminarIngrediente( new Ingrediente( "lechuga", 1000 ) );

        assertEquals( 14000, productoAjustado.getPrecio( ), "Quitar ingredientes no debería reducir el precio." );
    }

    @Test
    void testGenerarTextoFacturaConAjustes( )
    {
        productoAjustado.agregarIngrediente( new Ingrediente( "queso mozzarella", 2500 ) );
        productoAjustado.eliminarIngrediente( new Ingrediente( "lechuga", 1000 ) );

        String texto = productoAjustado.generarTextoFactura( );

        assertTrue( texto.contains( "+queso mozzarella" ), "Debería listar el ingrediente agregado." );
        assertTrue( texto.contains( "2500" ), "Debería mostrar el costo del ingrediente agregado." );
        assertTrue( texto.contains( "-lechuga" ), "Debería listar el ingrediente eliminado." );
        assertTrue( texto.contains( "16500" ), "Debería mostrar el precio total." );
        assertEquals( 5, texto.split( "\n" ).length, "Cada elemento de la factura debería quedar en su propia línea." );
    }

}