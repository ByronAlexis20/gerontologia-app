package ec.gerontologia.controlador.seguridad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Usuario;
import ec.gerontologia.modelo.UsuarioDAO;

@SuppressWarnings("serial")
public class UsuarioListaC extends SelectorComposer<Component>{
	@Wire private Window winUsuarioLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstUsuario;
	
	List<Usuario> usuarios;
	Usuario usuarioSeleccionado;
	UsuarioDAO usuarioDAO = new UsuarioDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		try {
			if (usuarios != null)
				usuarios = null; 
			usuarios = usuarioDAO.getListaUsuariosActivos(txtBuscar.getValue());
			lstUsuario.setModel(new ListModelList(usuarios));
			usuarioSeleccionado = null;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/seguridad/usuarioEditar.zul", winUsuarioLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (usuarioSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un usuario.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		usuarioDAO.getEntityManager().refresh(usuarioSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Usuario", usuarioSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/seguridad/usuarioEditar.zul", winUsuarioLista, params);
		ventanaCargar.doModal();
	}
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (usuarioSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un usuario.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	usuarioSeleccionado.setEstado("I");
                    	usuarioDAO.getEntityManager().getTransaction().begin();
                    	usuarioDAO.getEntityManager().merge(usuarioSeleccionado);
                    	usuarioDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        usuarioDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Usuario> getUsuarios() {
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	
}
