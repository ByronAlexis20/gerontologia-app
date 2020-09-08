package ec.gerontologia.controlador.administracion;

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

import ec.gerontologia.modelo.Servicio;
import ec.gerontologia.modelo.ServicioDAO;

@SuppressWarnings("serial")
public class ServicioListaC extends SelectorComposer<Component>{
	@Wire private Window winServicioLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstServicios;
	
	private List<Servicio> servicio;
	private Servicio servicioSeleccionado;
	ServicioDAO servicioDAO = new ServicioDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");

		if (servicio != null) {
			servicio = null; 
		}
		servicio = servicioDAO.getServicios(txtBuscar.getValue());
		lstServicios.setModel(new ListModelList(servicio));
		servicioSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Instruccion", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/servicioEditar.zul", winServicioLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (servicioSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un perfil.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		servicioDAO.getEntityManager().refresh(servicioSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Servicio", servicioSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/servicioEditar.zul", winServicioLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (servicioSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un servicio.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	servicioSeleccionado.setEstado("I");
                    	servicioDAO.getEntityManager().getTransaction().begin();
                    	servicioDAO.getEntityManager().merge(servicioSeleccionado);
                    	servicioDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        servicioDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Servicio> getServicio() {
		return servicio;
	}
	public void setServicio(List<Servicio> servicio) {
		this.servicio = servicio;
	}
	public Servicio getServicioSeleccionado() {
		return servicioSeleccionado;
	}
	public void setServicioSeleccionado(Servicio servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}
}
