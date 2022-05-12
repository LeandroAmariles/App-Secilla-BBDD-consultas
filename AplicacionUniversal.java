package AppConsultaUniversal;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class AplicacionUniversal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MarcoBBDD miMarco=new MarcoBBDD();
		miMarco.setVisible(true);
		miMarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
class MarcoBBDD extends JFrame{
	public MarcoBBDD() {
		setBounds(300,300,700,700);
		LaminaBBDD miLamina=new LaminaBBDD();
		add(miLamina);
	}
}
class LaminaBBDD extends JPanel{
	
	public LaminaBBDD() {
		setLayout(new BorderLayout());
		comboTablas=new JComboBox();
		conectarBBDD();
		obtenerTablas();
		comboTablas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String nombreTabla=(String)comboTablas.getSelectedItem();
				mostrarInfoTabla(nombreTabla);
			}
			
		});
		areaInformacion=new JTextArea();
		add(areaInformacion, BorderLayout.CENTER);
		add(comboTablas, BorderLayout.NORTH);
		
	}
	
	public void conectarBBDD() {
		miConexion=null;
		
		 datos=new String[4];
		 try {
			 try {
			entrada=new FileReader("src/AppConsultaUniversal/Configuracioon.txt");
			 }catch(IOException e) {
			//-------------- En caso de que el archivo no este, se creo una clase marco con un metodo donde va FileChooser para poder buscar el fichero correcto
			JOptionPane.showMessageDialog(this, "No se ha encontrado el archivo de conexion");
			
			MarcoElegir marco=new MarcoElegir();
			entrada=new FileReader(marco.chooseFile());
			 }	
			BufferedReader miBuffer=new BufferedReader(entrada);
			
			for(int i=0; i<=3; i++) {
				
			datos[i]=miBuffer.readLine();
			}
			miConexion=DriverManager.getConnection(datos[0],datos[1],datos[2]);
			
			entrada.close();
			
		}catch(IOException e) {
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void obtenerTablas() {
		ResultSet mRs=null;
		
		try {
			DatabaseMetaData datosBBDD=miConexion.getMetaData();
			mRs=datosBBDD.getTables(datos[3], null, null, null);
			
			while(mRs.next()) {
				comboTablas.addItem(mRs.getString("TABLE_NAME"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void mostrarInfoTabla(String tabla) {
		ArrayList<String> campos=new ArrayList<String>();// para almacenar los campos
		
		String consulta="SELECT *FROM "+ tabla;
		areaInformacion.append("");
		try {
			
			areaInformacion.setText("");
			Statement miStatement=miConexion.createStatement();
			ResultSet miResultSet=miStatement.executeQuery(consulta);// Guarda en el resultset la consulta
			
			ResultSetMetaData rsBBDD=miResultSet.getMetaData();// Almacena los metadatas
			
			
			for(int i=1; i<=rsBBDD.getColumnCount();i++) {// Bucle for para recorrer las columnas y almacenar el nombre en la Array
				//System.out.println(rsBBDD.getColumnLabel(1));
				campos.add(rsBBDD.getColumnLabel(i));
			}
			while(miResultSet.next()) {
				for(String nombreCampo:campos) {
					//System.out.println(nombreCampo);//Nombre campo es el nombre de cada columna
					areaInformacion.append(miResultSet.getString(nombreCampo)+" ");// Al ponerse aqui el nombre de cada columna, extrae los datos
				}
				areaInformacion.append("\n");
				
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	class MarcoElegir extends JFrame{
		public MarcoElegir() {
			setBounds(300,300,300,300);
			setVisible(true);
			
		}
		public String chooseFile() {
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto","txt");// Filtro para archivos que se van a buscar
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);// This hace referencia al panel contenedor sea un marco o un panel
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("You chose to open this file: " +
		            chooser.getSelectedFile().getName());     
		}
		    return chooser.getSelectedFile().getAbsolutePath();//Hago que me retorne la ruta del archivo para usarla en el el contru de FileReader
		    
	}
}
	private JComboBox comboTablas;
	private JTextArea areaInformacion;
	private Connection miConexion;
	private FileReader entrada;
	private String[] datos;
}
