/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.teatromoro;

/**
 *
 * @author Lazareth Parra Barras
 */
import java.util.Scanner;

public class TeatroMoro {

    // Variables estáticas
    static int totalEntradasVendidas = 0;
    static int totalIngresos = 0;
    static double porcentajeOcupacion = 0.0; // Porcentaje de entradas vendidas

    // Variables de instancia
    private String nombreTeatro = "Teatro Moro";
    private final int precioEntrada = 5200;
    private boolean[] asientosDisponibles = new boolean[50];
    private String[] reservas = new String[50];
    private String[] tipoEntradas = new String[50];
    private int[] preciosPagados = new int[50];
    private boolean[] boletaImpresa = new boolean[50]; // NUEVO: Marca si ya se imprimió boleta

    // Variables locales
    private String tipoEntrada;
    private int descuento;
    private int asientoSeleccionado;
    private boolean asientoReservado;
    private int precioFinal;

    public TeatroMoro() {
        for (int i = 0; i < asientosDisponibles.length; i++) {
            asientosDisponibles[i] = true;
            reservas[i] = "Disponible";
            tipoEntradas[i] = "";
            preciosPagados[i] = 0;
            boletaImpresa[i] = false; // Ninguna impresa incialmente
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TeatroMoro teatro = new TeatroMoro();
        int opcion;

        do {
            System.out.println("\n--- ¡Bienvenido a Teatro Moro! ---");
            System.out.println("A continuacion podras acceder a nuestro menu de compra:");
            System.out.println("1. Reservar entradas");
            System.out.println("2. Comprar entradas");
            System.out.println("3. Modificar una venta existente");
            System.out.println("4. Salir");
            System.out.print("Ingrese una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    teatro.reservarEntradas(scanner);
                    break;
                case 2:
                    teatro.comprarEntradas(scanner);
                    break;
                case 3:
                    teatro.modificarVenta(scanner);
                    break;
                case 4:
                    System.out.println("\n--- RESUMEN DEL DÍA ---");
                    System.out.println("Total de entradas vendidas: " + totalEntradasVendidas);
                    System.out.println("Total de ingresos: $" + totalIngresos);
                    System.out.printf("Porcentaje de venta: %.2f%%\n", porcentajeOcupacion);
                    System.out.println("¡Gracias por utilizar el sistema del Teatro Moro!");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 4);

        scanner.close();
    }

    public void reservarEntradas(Scanner scanner) {
        System.out.println("\n--- Reservar Entradas ---");
        mostrarAsientosGraficamente();

        System.out.print("Seleccione el número de asiento (1-50): ");
        asientoSeleccionado = scanner.nextInt() - 1;

        if (asientoSeleccionado >= 0 && asientoSeleccionado < 50) {
            if (asientosDisponibles[asientoSeleccionado]) {
                System.out.print("Seleccione tipo de entrada (1 - Normal / 2 - Estudiante): ");
                int tipo = scanner.nextInt();

                if (tipo == 1) {
                    tipoEntrada = "Normal";
                    descuento = 0;
                } else if (tipo == 2) {
                    tipoEntrada = "Estudiante";
                    descuento = precioEntrada / 3;
                } else {
                    System.out.println("Opción inválida. Se aplicará tarifa normal.");
                    tipoEntrada = "Normal";
                    descuento = 0;
                }

                asientosDisponibles[asientoSeleccionado] = false;
                reservas[asientoSeleccionado] = "Reservado";
                tipoEntradas[asientoSeleccionado] = tipoEntrada;
                System.out.println("¡Reserva realizada con éxito!");
                asientoReservado = true;

            } else {
                System.out.println("El asiento ya está ocupado.");
            }
        } else {
            System.out.println("Número de asiento inválido.");
        }
    }

    public void comprarEntradas(Scanner scanner) {
        System.out.println("\n--- Comprar Entradas ---");
        boolean tieneReservas = false;

        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i].equals("Reservado")) {
                if (!tieneReservas) {
                    System.out.println("Asientos reservados:");
                }
                System.out.print((i + 1) + " ");
                tieneReservas = true;
            }
        }

        if (!tieneReservas) {
            System.out.println("No tiene asientos reservados.");
            return;
        }

        System.out.print("\n¿Desea pagar estos asientos? (1 - Sí / 2 - No): ");
        int opcion = scanner.nextInt();

        if (opcion == 1) {
            for (int i = 0; i < reservas.length; i++) {
                if (reservas[i].equals("Reservado")) {
                    if (tipoEntradas[i].equals("Estudiante")) {
                        precioFinal = precioEntrada - (precioEntrada / 3);
                    } else {
                        precioFinal = precioEntrada;
                    }

                    totalIngresos += precioFinal;
                    totalEntradasVendidas++;
                    reservas[i] = "Vendido";
                    asientosDisponibles[i] = false;
                    preciosPagados[i] = precioFinal;
                    boletaImpresa[i] = false; // Marca que aún no se ha entregado boleta
                    actualizarPorcentajeOcupacion();
                }
            }

            System.out.print("\n¿Desea su boleta? (1 - Sí / 2 - No): ");
            int boleta = scanner.nextInt();
            if (boleta == 1) {
                imprimirBoletas(); // Solo imprime boletas no entregadas
            } else {
                System.out.println("Boleta no solicitada.");
            }

        } else {
            for (int i = 0; i < reservas.length; i++) {
                if (reservas[i].equals("Reservado")) {
                    reservas[i] = "Disponible";
                    asientosDisponibles[i] = true;
                    tipoEntradas[i] = "";
                    preciosPagados[i] = 0;
                }
            }
            System.out.println("Compra cancelada. Las reservas han sido eliminadas.");
        }
    }

    public void modificarVenta(Scanner scanner) {
        System.out.println("\n--- Modificar Venta ---");
        System.out.print("Ingrese el número de asiento para cancelar (1-50): ");
        asientoSeleccionado = scanner.nextInt() - 1;

        if (asientoSeleccionado >= 0 && asientoSeleccionado < 50) {
            if (reservas[asientoSeleccionado].equals("Vendido")) {
                asientosDisponibles[asientoSeleccionado] = true;
                reservas[asientoSeleccionado] = "Disponible";
                totalEntradasVendidas--;
                actualizarPorcentajeOcupacion();
                totalIngresos -= preciosPagados[asientoSeleccionado];
                tipoEntradas[asientoSeleccionado] = "";
                preciosPagados[asientoSeleccionado] = 0;
                boletaImpresa[asientoSeleccionado] = false; // Reinicia estado de impresión
                System.out.println("Venta cancelada. Asiento disponible nuevamente.");
            } else {
                System.out.println("Este asiento no ha sido vendido aún.");
            }
        } else {
            System.out.println("Número de asiento inválido.");
        }
    }

    public void imprimirBoletas() {
        System.out.println("\n--- BOLETA DE VENTA ---");
        System.out.println("Teatro: " + nombreTeatro);
        int totalPagado = 0;
        boolean seImprimio = false;

        for (int i = 0; i < reservas.length; i++) {
            if (reservas[i].equals("Vendido") && !boletaImpresa[i]) {
                System.out.println("Asiento: " + (i + 1));
                System.out.println("Tipo de entrada: " + tipoEntradas[i]);
                System.out.println("Precio pagado: $" + preciosPagados[i]);
                System.out.println("-----------------------------");
                totalPagado += preciosPagados[i];
                boletaImpresa[i] = true; // Marca como ya impresa
                seImprimio = true;
            }
        }

        if (seImprimio) {
            System.out.println("Total pagado: $" + totalPagado);
        } else {
            System.out.println("No hay nuevas boletas para imprimir.");
        }
    }

    public void mostrarAsientosGraficamente() {
        System.out.println("\n              ---Pantalla---");
        for (int i = 0; i < asientosDisponibles.length; i++) {
            if (i % 10 == 0 && i != 0) System.out.println();
            if (reservas[i].equals("Reservado") || reservas[i].equals("Vendido")) {
                System.out.print("- ");
            } else {
                System.out.printf("%-2d ", i + 1);
            }
        }
        System.out.println();
    }

    public static void actualizarPorcentajeOcupacion() {
        porcentajeOcupacion = (totalEntradasVendidas / 50.0) * 100;
    }
}

