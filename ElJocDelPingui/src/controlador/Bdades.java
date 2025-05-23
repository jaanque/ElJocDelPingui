package controlador;
import java.sql.*;
import java.util.Scanner;

/**
 * Clase que proporciona métodos para interactuar con una base de datos Oracle.
 */
public class Bdades {

    /**
     * Intenta establecer una conexión a la base de datos Oracle. NO HACE FALTA QUE ENTENDAIS COMO FUNCIONA,
     * SE HACE TODO DE MANERA AUTOMÁTICA.
     *
     * @return Objeto Connection si la conexión es exitosa, null en caso contrario. LA VARIABLE QUE DEVUELVE
     * LA TENEIS QUE GUARDAR PARA LAS DEMÁS FUNCIONES
     */
	public static Connection conectarBaseDatos(Connection con) {
		System.out.println("Intentando conectarse a la base de datos");
		Scanner scan = new Scanner(System.in);
		String s = "";

		while (true) {
		    System.out.println("Selecciona centro o fuera de centro: (CENTRO/FUERA)");
		    s = scan.nextLine().trim().toLowerCase();

		    if (s.equals("centro") || s.equals("fuera")) {
		        break;
		    } else {
		        System.out.println("❌ Entrada no válida. Has de escriure 'centro' o 'fuera'. Torna-ho a intentar.");
		    }
		}

		String URL;

		if (s.equals("centro")) {
			URL = "jdbc:oracle:thin:@192.168.3.26:1521/XEPDB2";
		} else {
			URL = "jdbc:oracle:thin:@oracle.ilerna.com:1521/XEPDB2";
		}

		String USER = "DW2425_PIN_GRUP01";
		String PWD = "AFMSQ01";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(URL, USER, PWD);
		} catch (ClassNotFoundException e) {
			System.out.println("No se ha encontrado el driver " + e);
		} catch (SQLException e) {
			System.out.println("Error en las credenciales o en la URL " + e);
		}

		System.out.println("Conectados a la base de datos");
		return con;
	}

    /**
     * Realiza una inserción en la base de datos y devuelve cuántas filas se han insertado.
     *
     * @param con Objeto Connection que representa la conexión a la base de datos.
     * @param sql Sentencia SQL de inserción que hayais creado.
     * @return número de filas afectadas (1 si se insertó correctamente, 0 si falló)
     */
	public static int insert(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			int filas = st.executeUpdate(sql);
			System.out.println("Insert hecho correctamente");
			return filas;
		} catch (SQLException e) {
			System.out.println("Ha habido un error en el Insert " + e);
			return 0;
		}
	}

    /**
     * Realiza una actualización en la base de datos.
     *
     * @param con Objeto Connection que representa la conexión a la base de datos.
     * @param sql Sentencia SQL de actualización que hayais creado.
     */
	public static void update(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			st.execute(sql);
			System.out.println("Update hecho correctamente");
		} catch (SQLException e) {
			System.out.println("Ha habido un error en el Update " + e);
		}
	}

    /**
     * Realiza una eliminación en la base de datos.
     *
     * @param con Objeto Connection que representa la conexión a la base de datos.
     * @param sql Sentencia SQL de eliminación que hayais creado.
     */
	public static void delete(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			st.execute(sql);
			System.out.println("Delete hecho correctamente");
		} catch (SQLException e) {
			System.out.println("Ha habido un error en el Delete " + e);
		}
	}

    /**
     * Realiza una consulta en la base de datos y devuelve los resultados como un ResultSet
     *
     * @param con Objeto Connection que representa la conexión a la base de datos.
     * @param sql Sentencia SQL de consulta.
     * @return ResultSet con la query hecha
     */
	public static ResultSet select(Connection con, String sql) {
		try {
			Statement st = con.createStatement();
			return st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Unexpected error");
		return null;
	}

    /**
     * Imprime los resultados de una consulta SELECT en la base de datos.
     * EN ESTE CASO SI PODEIS IMPRIMIR MÁS DE UNA FILA.
     *
     * @param con Objeto Connection que representa la conexión a la base de datos.
     * @param sql Sentencia SQL de consulta.
     * @param listaElementosSeleccionados Array de Strings con los nombres de las columnas seleccionadas.
     */
	public static void print(Connection con, String sql, String[] listaElementosSeleccionados) {
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);

			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					for (int i = 0; i < listaElementosSeleccionados.length; i++) {
						System.out.println(listaElementosSeleccionados[i] +
								": " + rs.getString(listaElementosSeleccionados[i]));
					}
				}
			} else {
				System.out.println("No se ha encontrado nada");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
