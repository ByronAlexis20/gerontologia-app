package ec.gerontologia.controlador.seguridad;

import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Perfil;
import ec.gerontologia.modelo.PerfilDAO;
import ec.gerontologia.modelo.Usuario;
import ec.gerontologia.modelo.UsuarioDAO;

@SuppressWarnings({ "serial", "rawtypes" })
public class UsuarioEditarC extends SelectorComposer{
	@Wire private Window winUsuarioEditar;
	
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtDireccion;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtCorreo;
	@Wire private Textbox txtNombUsuario;
	@Wire private Textbox txtClave;
	
	@Wire private Combobox cboPerfil;
	
	private UsuarioListaC usuarioListaC; 
	private UsuarioDAO usuarioDAO = new UsuarioDAO();
	private Usuario usuario;
	
	PerfilDAO perfilDAO = new PerfilDAO();
	
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		usuarioListaC = (UsuarioListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		usuario = null; 
		if (Executions.getCurrent().getArg().get("Usuario") != null) {
			usuario = (Usuario)Executions.getCurrent().getArg().get("Usuario");
			cboPerfil.setText(usuario.getPerfil().getNombre());
		}else
			usuario = new Usuario(); 
	}
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		System.out.println("entra grabando");
		try {
			if (isValidarDatos() == true) {
				// Inicia la transaccion
				usuarioDAO.getEntityManager().getTransaction().begin();	
				Perfil p = (Perfil)cboPerfil.getSelectedItem().getValue();
				usuario.setPerfil(p);
				usuario.setEstado("A");
				if (usuario.getIdUsuario() == null) {
					usuario.setClave(encriptar(txtClave.getText()));
					usuarioDAO.getEntityManager().persist(usuario);
				}else{
					usuario.setClave(encriptar(txtClave.getText()));	
					usuario = (Usuario) usuarioDAO.getEntityManager().merge(usuario);
								
				}

				// Cierra la transaccion.
				usuarioDAO.getEntityManager().getTransaction().commit();

				Clients.showNotification("Proceso Ejecutado con exito.");

				// Actualiza la lista
				usuarioListaC.buscar();

				// Cierra la ventana
				salir();
			}

		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			usuarioDAO.getEntityManager().getTransaction().rollback();
		}		
	}
	private String encriptar(String clave) throws Exception {

		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] b = md.digest(clave.getBytes());
		int size = b.length;
		StringBuilder h = new StringBuilder(size);
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255;
			if (u < 16){
				h.append("0").append(Integer.toHexString(u));
			}else{
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}
	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;
			if(validarDeCedula(txtCedula.getText())== false) {
				Clients.showNotification("La cédula ingresada no es válida!!");
				txtCedula.focus();
				resultado = false;
				return resultado;
			}
			if(txtCorreo.getText() != "") {
				if(ValidarEmail(txtCorreo.getText()) == false) {
					Clients.showNotification("El correo ingresado no es válido!!");
					txtCorreo.focus();
					resultado = false;
					return resultado;
				}
			}
			if(validarUsuario() == true) {
				Clients.showNotification("El nombre de usuario ingresado ya existe!!");
				txtNombUsuario.focus();
				resultado = false;
				return resultado;
			}
			
			if(txtCorreo.getText() == null) {
				Clients.showNotification("Por favor ingrese un correo electrónico.!!");
				txtCorreo.focus();
				return resultado;
			}
			if(txtNombUsuario.getText() == null) {
				Clients.showNotification("Por favor ingrese nombre de usuario.!!");
				txtNombUsuario.focus();
				return resultado;
			}
			if(txtClave.getText() == null) {
				Clients.showNotification("Por favor ingrese una clave de usuario.!!");
				txtClave.focus();
				return resultado;
			}
			if(txtNombres.getText() == null) {
				Clients.showNotification("Por favor ingrese nombres.!!");
				txtNombres.focus();
				return resultado;
			}
			if(txtApellidos.getText() == null) {
				Clients.showNotification("Por favor ingrese apellidos.!!");
				txtApellidos.focus();
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	boolean validarUsuario() {
		try {
			int idUsuario;
			if(usuario.getIdUsuario() != null)
				idUsuario = usuario.getIdUsuario();
			else
				idUsuario = 0;
			
			if(usuarioDAO.getBuscarUsuario(txtNombUsuario.getText(),idUsuario).size() > 0) 
				return true;
			else
				return false;

		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	private boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
	private boolean validarDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validación cédula
					// El decimo digito se lo considera dígito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}

					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validacion");
			cedulaCorrecta = false;
		}
		if (!cedulaCorrecta) {
			System.out.println("La Cédula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winUsuarioEditar.detach();
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public List<Perfil> getPerfiles(){
		return perfilDAO.getPerfiles();
	}
	
}
