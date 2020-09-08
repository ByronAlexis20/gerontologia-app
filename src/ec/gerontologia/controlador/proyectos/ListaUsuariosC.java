package ec.gerontologia.controlador.proyectos;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Usuario;
import ec.gerontologia.modelo.UsuarioDAO;

@SuppressWarnings("serial")
public class ListaUsuariosC extends SelectorComposer<Component>{
	@Wire private Window winSeleccionarUsuario;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstUsuario;
	
	List<Usuario> usuarios = new ArrayList<Usuario>();
	private Centro centroSeleccionado;
	private Usuario usuarioSeleccionado;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	ResponsableCentroC responsable;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		responsable = (ResponsableCentroC)Executions.getCurrent().getArg().get("VentanaPadre");
		centroSeleccionado = (Centro)Executions.getCurrent().getArg().get("Centro");
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		usuarios.clear();
		List<Usuario> listaTecnicos = usuarioDAO.getListaUsuariosActivosTecnicos(txtBuscar.getValue());//solo usuarios tecnicos
		//verificar qu eno este asignado a ningun centro
		System.out.println(centroSeleccionado.getNombreCentro() + centroSeleccionado.getIdCentro());
		List<Encargado> listaAsignados = encargadoDAO.getEncargadosAsignados();
		System.out.println(listaAsignados.size());
		if(listaAsignados.size() > 0) {
			for(Usuario us : listaTecnicos) {
				boolean bandera = false;
				System.out.println(us.getIdUsuario() + " usuario disponible");
				for(Encargado en : listaAsignados) {
					System.out.println(en.getUsuario().getIdUsuario() + " usuario encargado");
					if(en.getUsuario().getIdUsuario() == us.getIdUsuario()) {
						bandera = true;
						System.out.println("son iguales");
					}
				}
				if(bandera == false) {
					usuarios.add(us);
					System.out.println("agrega al listado " + us.getIdUsuario());
				}
			}
		}else {
			for(Usuario us : listaTecnicos) {
				usuarios.add(us);
			}
		}
		lstUsuario.setModel(new ListModelList(usuarios));
		usuarioSeleccionado = null;
	}
	@Listen("onClick=#btnAceptar")
	public void aceptar(){
		if(usuarioSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un registro");
			return;
		}
		
		Encargado encargadoGrabar = new Encargado();
		encargadoGrabar.setCentro(centroSeleccionado);
		encargadoGrabar.setEstado("A");
		encargadoGrabar.setIdEncargado(null);
		encargadoGrabar.setUsuario(usuarioSeleccionado);
		
		encargadoDAO.getEntityManager().getTransaction().begin();
		encargadoDAO.getEntityManager().persist(encargadoGrabar);
		encargadoDAO.getEntityManager().getTransaction().commit();
		Clients.showNotification("Datos Grabados");
		responsable.recuperarResponsables(centroSeleccionado);
		salir();
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winSeleccionarUsuario.detach();
	}
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Centro getCentroSeleccionado() {
		return centroSeleccionado;
	}
	public void setCentroSeleccionado(Centro centroSeleccionado) {
		this.centroSeleccionado = centroSeleccionado;
	}
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
}
